package net.kenji.epic_fight_mobs_plus.network;

import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.WolfPatch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.jline.utils.Log;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.UUID;
import java.util.function.Supplier;

public class ClientWolfRunPacket {
    private final int entityId;
    private final boolean shouldRun;


    public ClientWolfRunPacket(int entityId, boolean shouldRun) {
        this.entityId = entityId;
        this.shouldRun = shouldRun;
    }

    // Encode: Write data to buffer
    public static void encode(ClientWolfRunPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.entityId);
        buf.writeBoolean(packet.shouldRun);
    }

    // Decode: Read data from buffer
    public static ClientWolfRunPacket decode(FriendlyByteBuf buf) {
       int entityId = buf.readInt();
        boolean shouldRun = buf.readBoolean();
        return new ClientWolfRunPacket(entityId, shouldRun);
    }

    // Handle: Process the packet on the receiving side
    public static void handle(ClientWolfRunPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection().getReceptionSide().isClient()) {
                executeOnClient(packet);
            }
        });
        ctx.get().setPacketHandled(true);
    }
    @OnlyIn(Dist.CLIENT)
    private static void executeOnClient(ClientWolfRunPacket packet){
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
        if(playerPatch == null) return;
        Entity entity = player.level().getEntity(packet.entityId);
        if(entity instanceof Wolf wolf){
            WolfPatch<?> wolfPatch = EpicFightCapabilities.getEntityPatch(wolf, WolfPatch.class);
            if(wolfPatch != null)
                wolfPatch.cachedShouldRun = packet.shouldRun;
        }
    }
}