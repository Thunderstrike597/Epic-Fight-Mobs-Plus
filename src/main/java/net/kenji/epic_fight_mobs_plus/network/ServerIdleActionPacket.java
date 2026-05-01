package net.kenji.epic_fight_mobs_plus.network;

import net.kenji.epic_fight_mobs_plus.api.IdleActionManager;
import net.kenji.epic_fight_mobs_plus.api.animation_types.IdleActionAnimation;
import net.kenji.epic_fight_mobs_plus.api.interfaces.IAnimalMobPatch;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import org.jline.utils.Log;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.UUID;
import java.util.function.Supplier;

public class ServerIdleActionPacket {
    private final UUID entityUuid;
    private final ResourceLocation motionId;

    // Constructor only needs UUID and boolean - playerPatch is looked up on server
    public ServerIdleActionPacket(UUID entityUuid, ResourceLocation isAiming) {
        this.entityUuid = entityUuid;
        this.motionId = isAiming;
    }

    public static void encode(ServerIdleActionPacket packet, FriendlyByteBuf buf) {
        buf.writeUUID(packet.entityUuid);
        buf.writeResourceLocation(packet.motionId);
    }

    public static ServerIdleActionPacket decode(FriendlyByteBuf buf) {
        UUID entityUuid = buf.readUUID();
        ResourceLocation isAiming = buf.readResourceLocation();
        return new ServerIdleActionPacket(entityUuid, isAiming); // Fixed - matches constructor
    }

    public static void handle(ServerIdleActionPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                Log.warn("TessenInnatePacket: Sender is null!");
                return;
            }
            if (player.level() instanceof ServerLevel serverLevel) {
                Entity entity = serverLevel.getEntity(packet.entityUuid);
                if (entity == null) return;
                entity.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent((cap) -> {
                    if (cap instanceof IAnimalMobPatch patchInterface) {
                        LivingEntityPatch<?> patch = patchInterface.getEntityPatch();
                        if (patch != null) {
                            if (packet.motionId == patchInterface.getQuedIdleAction().get().getLocation()) {
                                IdleActionAnimation anim = patchInterface.getQuedIdleAction().get();

                                IdleActionManager.clearIdleActionState(
                                        anim.getAccessor(),
                                        patchInterface,
                                        IdleActionManager.getIdleActionState(entity.getUUID())
                                );
                            }
                        }
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}