package net.kenji.epic_fight_mobs_plus.network;

import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = EpicFightMobsPlus.MODID)
public class MobsPlusPacketHandler {

    private static final String PROTOCOL_VERSION = "1";

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);

        registrar.playToServer(
                ServerIdleActionPacket.TYPE,
                ServerIdleActionPacket.STREAM_CODEC,
                ServerIdleActionPacket::handle
        );
        registrar.playToClient(
                ClientPetRunPacket.TYPE,
                ClientPetRunPacket.STREAM_CODEC,
                ClientPetRunPacket::handle
        );
        registrar.playToClient(
                ClientOptionalLivingMotionPacket.TYPE,
                ClientOptionalLivingMotionPacket.STREAM_CODEC,
                ClientOptionalLivingMotionPacket::handle
        );
        registrar.playToClient(
                ClientIdleActionSyncPacket.TYPE,
                ClientIdleActionSyncPacket.STREAM_CODEC,
                ClientIdleActionSyncPacket::handle
        );
        registrar.playToClient(
                ClientPlayAnimationPacket.TYPE,
                ClientPlayAnimationPacket.STREAM_CODEC,
                ClientPlayAnimationPacket::handle
        );
        registrar.playToClient(
                ClientQuedIdleActionSyncPacket.TYPE,
                ClientQuedIdleActionSyncPacket.STREAM_CODEC,
                ClientQuedIdleActionSyncPacket::handle
        );
    }

    public static void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    public static void sendToPlayer(CustomPacketPayload payload, net.minecraft.server.level.ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    public static void sendToAll(CustomPacketPayload payload) {
        PacketDistributor.sendToAllPlayers(payload);
    }
}