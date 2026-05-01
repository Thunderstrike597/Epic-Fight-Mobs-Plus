package net.kenji.epic_fight_mobs_plus.network;

import net.kenji.epic_fight_mobs_plus.api.interfaces.IAnimalMobPatch;
import net.kenji.epic_fight_mobs_plus.gameasset.animations.MobsPlusAnimations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.Objects;
import java.util.function.Supplier;

public class ClientPlayAnimationPacket {
    private final int entityId;
    private final ResourceLocation motionId;


    public ClientPlayAnimationPacket(int entityId, ResourceLocation motionId) {
        this.entityId = entityId;
        this.motionId = motionId;
    }

    // Encode: Write data to buffer
    public static void encode(ClientPlayAnimationPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.entityId);
        buf.writeBoolean(packet.motionId != null);
        if (packet.motionId != null) buf.writeResourceLocation(packet.motionId);
    }

    public static ClientPlayAnimationPacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        ResourceLocation motionId = buf.readBoolean() ? buf.readResourceLocation() : null;
        return new ClientPlayAnimationPacket(entityId, motionId);
    }

    // Handle: Process the packet on the receiving side
    public static void handle(ClientPlayAnimationPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection().getReceptionSide().isClient()) {
                executeOnClient(packet);
            }
        });
        ctx.get().setPacketHandled(true);
    }
    @OnlyIn(Dist.CLIENT)
    private static void executeOnClient(ClientPlayAnimationPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        Entity entity = player.level().getEntity(packet.entityId);
        if (entity == null) return;

        entity.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
            if (cap instanceof IAnimalMobPatch patchInterface) {
                LivingEntityPatch<?> patch = patchInterface.getEntityPatch();
                if (patch == null) return;

                AnimationManager.AnimationAccessor<StaticAnimation> anim = AnimationManager.byKey(packet.motionId);
                if(anim != null)
                    if(Objects.requireNonNull(patch.getAnimator().getPlayerFor(null)).getAnimation().get() != anim.get())
                        patch.getAnimator().playAnimation(anim, 0.05F);
            }
        });
    }
}