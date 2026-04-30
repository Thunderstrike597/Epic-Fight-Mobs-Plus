package net.kenji.epic_fight_mobs_plus.network;

import net.kenji.epic_fight_mobs_plus.api.interfaces.IAnimalMobPatch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.function.Supplier;

public class ClientOptionalLivingMotionPacket {
    private final int entityId;
    private final int motionId;


    public ClientOptionalLivingMotionPacket(int entityId, int motionId) {
        this.entityId = entityId;
        this.motionId = motionId;
    }

    // Encode: Write data to buffer
    public static void encode(ClientOptionalLivingMotionPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.entityId);
        buf.writeInt(packet.motionId);

    }

    // Decode: Read data from buffer
    public static ClientOptionalLivingMotionPacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        int motionId = buf.readInt();
        return new ClientOptionalLivingMotionPacket(entityId, motionId);
    }

    // Handle: Process the packet on the receiving side
    public static void handle(ClientOptionalLivingMotionPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection().getReceptionSide().isClient()) {
                executeOnClient(packet);
            }
        });
        ctx.get().setPacketHandled(true);
    }
    @OnlyIn(Dist.CLIENT)
    private static void executeOnClient(ClientOptionalLivingMotionPacket packet){
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
        if(playerPatch == null) return;
        Entity entity = player.level().getEntity(packet.entityId);
        if(entity == null) return;
        entity.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent((cap) -> {
            if(cap instanceof IAnimalMobPatch patchInterface){
                LivingEntityPatch<?> patch = patchInterface.getEntityPatch();
                if(patch != null) {
                    patchInterface.setOptionalLivingMotion(packet.motionId);
                }
            }
        });
    }
}