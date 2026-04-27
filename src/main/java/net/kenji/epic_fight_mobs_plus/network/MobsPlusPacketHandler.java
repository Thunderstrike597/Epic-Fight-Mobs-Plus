package net.kenji.epic_fight_mobs_plus.network;

import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class MobsPlusPacketHandler {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(EpicFightMobsPlus.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        INSTANCE.messageBuilder(ClientPetRunPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientPetRunPacket::decode)
                .encoder(ClientPetRunPacket::encode)
                .consumerMainThread(ClientPetRunPacket::handle)
                .add();
        INSTANCE.messageBuilder(ClientOptionalLivingMotionPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientOptionalLivingMotionPacket::decode)
                .encoder(ClientOptionalLivingMotionPacket::encode)
                .consumerMainThread(ClientOptionalLivingMotionPacket::handle)
                .add();
    }

    // Helper method to send packet to server
    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }

    // Helper method to send packet to specific player
    public static void sendToPlayer(Object packet, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    // Helper method to send packet to all players
    public static void sendToAll(Object packet) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), packet);
    }
}