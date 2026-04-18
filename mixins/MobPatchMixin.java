package net.kenji.epic_fight_mobs_plus.mixins;

import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.WolfPatch;
import net.minecraft.world.entity.Mob;
import org.jline.utils.Log;
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
        if (self instanceof WolfPatch<?> wolf) {
            ci.cancel();

            if (wolf.getOriginal().getHealth() <= 0.0F) {
                wolf.currentLivingMotion = LivingMotions.DEATH;
            } else if (wolf.getEntityState().inaction() && considerInaction) {
                wolf.currentLivingMotion = LivingMotions.INACTION;
            } else {
                if ( wolf.getOriginal().getVehicle() != null)
                    wolf.currentLivingMotion = LivingMotions.MOUNT;
                else if (wolf.getOriginal().getDeltaMovement().y < -0.55F || wolf.isAirborneState())
                    wolf.currentLivingMotion = LivingMotions.FALL;
                else if ( wolf.getOriginal().walkAnimation.speed() > 0.01F) {
                    if(wolf.cachedShouldRun)
                        wolf.currentLivingMotion = LivingMotions.CHASE;
                    else wolf.currentLivingMotion = LivingMotions.WALK;
                }
                else wolf.currentLivingMotion = LivingMotions.IDLE;
            }
            wolf.currentCompositeMotion = wolf.currentLivingMotion;
        }
    }




}
