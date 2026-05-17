package net.kenji.epic_fight_mobs_plus.network;

import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.kenji.epic_fight_mobs_plus.api.interfaces.IAnimalMobPatch;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.CatPatch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jline.utils.Log;
import yesman.epicfight.registry.entries.EpicFightAttachmentTypes;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.function.Supplier;

public record ClientPetRunPacket(int entityId, boolean shouldRun) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientPetRunPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(EpicFightMobsPlus.MODID, "client_pet_run_packet"));
    public static final StreamCodec<FriendlyByteBuf, ClientPetRunPacket> STREAM_CODEC =
            StreamCodec.of(ClientPetRunPacket::encode, ClientPetRunPacket::decode);

    // Encode: Write data to buffer
    public static void encode(FriendlyByteBuf buf, ClientPetRunPacket packet) {
        buf.writeInt(packet.entityId);
        buf.writeBoolean(packet.shouldRun);
    }

    // Decode: Read data from buffer
    public static ClientPetRunPacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        boolean shouldRun = buf.readBoolean();
        return new ClientPetRunPacket(entityId, shouldRun);
    }

    // Handle: Process the packet on the receiving side
    public static void handle(ClientPetRunPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                executeOnClient(packet);
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void executeOnClient(ClientPetRunPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
        if (playerPatch == null) return;
        Entity entity = player.level().getEntity(packet.entityId);
        if (entity == null) return;

        EntityPatch<?> entityPatch = entity.getData(EpicFightAttachmentTypes.ENTITY_PATCH).getCapability();
        if (entityPatch == null) return;
        if (entityPatch instanceof IAnimalMobPatch patchInterface) {
            LivingEntityPatch<?> patch = patchInterface.getEntityPatch();
            if (patch != null) {
                patchInterface.setShouldRun(packet.shouldRun);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}