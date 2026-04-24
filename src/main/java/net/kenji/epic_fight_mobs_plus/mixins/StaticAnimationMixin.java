package net.kenji.epic_fight_mobs_plus.mixins;

import net.kenji.epic_fight_mobs_plus.api.IdleActionManager;
import net.kenji.epic_fight_mobs_plus.api.interfaces.AnimalMobPatchInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin(value = StaticAnimation.class, remap = false)
public class StaticAnimationMixin {

    @Inject(method = "end", at = @At("HEAD"), cancellable = true)
    public void onAnimEnd(LivingEntityPatch<?> entitypatch, AssetAccessor<? extends DynamicAnimation> nextAnimation, boolean isEnd, CallbackInfo ci){
        StaticAnimation self = (StaticAnimation) (Object)this;
        if(entitypatch instanceof AnimalMobPatchInterface patchInterface){
            if(patchInterface.getQuedIdleAction() != null){
                if(self == patchInterface.getQuedIdleAction()){
                    IdleActionManager.clearIdleActionState(patchInterface);
                }
            }
        }
    }
}
