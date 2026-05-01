package net.kenji.epic_fight_mobs_plus.network;

import net.kenji.epic_fight_mobs_plus.api.IdleActionManager;
import net.kenji.epic_fight_mobs_plus.api.animation_types.IdleActionAnimation;
import net.kenji.epic_fight_mobs_plus.api.interfaces.IAnimalMobPatch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.function.Supplier;

public class ClientIdleActionSyncPacket {
    private final int entityId;
    private final ResourceLocation motionId;


    public ClientIdleActionSyncPacket(int entityId, ResourceLocation motionId) {
        this.entityId = entityId;
        this.motionId = motionId;
    }

    // Encode: Write data to buffer
    public static void encode(ClientIdleActionSyncPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.entityId);
        buf.writeBoolean(packet.motionId != null);
        if (packet.motionId != null) buf.writeResourceLocation(packet.motionId);
    }

    public static ClientIdleActionSyncPacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        ResourceLocation motionId = buf.readBoolean() ? buf.readResourceLocation() : null;
        return new ClientIdleActionSyncPacket(entityId, motionId);
    }

    // Handle: Process the packet on the receiving side
    public static void handle(ClientIdleActionSyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection().getReceptionSide().isClient()) {
                executeOnClient(packet);
            }
        });
        ctx.get().setPacketHandled(true);
    }
    @OnlyIn(Dist.CLIENT)
    private static void executeOnClient(ClientIdleActionSyncPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        Entity entity = player.level().getEntity(packet.entityId);
        if (entity == null) return;

        entity.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
            if (cap instanceof IAnimalMobPatch patchInterface) {
                LivingEntityPatch<?> patch = patchInterface.getEntityPatch();
                if (patch == null) return;

                // null motionId = clear signal from server
                if (packet.motionId == null) {
                    patchInterface.queIdleAction(null);
                    return;
                }

                patchInterface.getIdleActionAnimations().stream()
                        .filter(accessor -> accessor.get().getLocation().equals(packet.motionId))
                        .findFirst()
                        .ifPresent(accessor -> {
                            patchInterface.queIdleAction(accessor);
                            patch.getAnimator().playAnimation(accessor, accessor.get().getTransitionTime());
                        });
            }
        });
    }
}