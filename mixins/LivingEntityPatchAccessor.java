package net.kenji.epic_fight_mobs_plus.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.network.common.AnimatorControlPacket;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin(value = LivingEntityPatch.class, remap = false)
public interface LivingEntityPatchAccessor {
    @Invoker("handleAnimationPacket")
    void onHandleAnimationPacket(AnimatorControlPacket.Action action, AssetAccessor<? extends StaticAnimation> animation, float transitionTimeModifier, LivingEntityPatch.ServerAnimationPacketProvider packetProvider);
}
