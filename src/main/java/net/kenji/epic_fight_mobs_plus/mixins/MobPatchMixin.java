package net.kenji.epic_fight_mobs_plus.mixins;

import net.kenji.epic_fight_mobs_plus.api.IdleVarientManager;
import net.kenji.epic_fight_mobs_plus.api.interfaces.IAnimalMobPatch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;

@Mixin(value = MobPatch.class, remap = false)
public class MobPatchMixin {
    @Inject(method = "commonMobUpdateMotion", at = @At("HEAD"), cancellable = true)
    public void onUpdateMotion(boolean considerInaction, CallbackInfo ci){
        MobPatch<?> self = (MobPatch<?>) (Object)this;
        if (self instanceof IAnimalMobPatch patch) {
            ci.cancel();
            if (patch.getEntityPatch().getOriginal().getHealth() <= 0.0F) {
                patch.getEntityPatch().currentLivingMotion = LivingMotions.DEATH;
            } else if (patch.getEntityPatch().getEntityState().inaction() && considerInaction) {
                patch.getEntityPatch().currentLivingMotion = LivingMotions.INACTION;
            } else {
                if (patch.getEntityPatch().getOriginal().getVehicle() != null)
                    patch.getEntityPatch().currentLivingMotion = LivingMotions.MOUNT;
                else if (patch.getEntityPatch().getOriginal().getDeltaMovement().y < -0.55F || patch.getEntityPatch().isAirborneState())
                    patch.getEntityPatch().currentLivingMotion = LivingMotions.FALL;
                else if (patch.getEntityPatch().getOriginal().walkAnimation.speed() > 0.01F) {
                    if(patch.shouldRunWithAnim()) {
                        patch.getEntityPatch().currentLivingMotion = LivingMotions.CHASE;
                    }
                    else patch.getEntityPatch().currentLivingMotion = LivingMotions.WALK;
                }
                else if(patch.getOptionalLivingMotion() == null)
                        patch.getEntityPatch().currentLivingMotion = IdleVarientManager.getIdleVarientState(self.getOriginal().getUUID()).currentLivingMotion;
                else{
                    patch.getEntityPatch().currentLivingMotion = patch.getOptionalLivingMotion();
                }
            }
            patch.getEntityPatch().currentCompositeMotion = patch.getEntityPatch().currentLivingMotion;
        }
    }




}
