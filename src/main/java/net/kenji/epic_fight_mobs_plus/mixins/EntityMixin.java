package net.kenji.epic_fight_mobs_plus.mixins;

import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusArmatures;
import net.kenji.epic_fight_mobs_plus.gameasset.armatures.HorseArmature;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.HorsePatch;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.JointTransform;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "getPassengersRidingOffset", at = @At("RETURN"), cancellable = true)
    public void getPassengerRidingPosition(CallbackInfoReturnable<Double> cir) {
        Entity self = (Entity)(Object)this;

        if (self instanceof AbstractHorse horse) {
            HorsePatch<?> patch = EpicFightCapabilities.getEntityPatch(horse, HorsePatch.class);
            if (patch == null) return;
            HorseArmature horseArmature = (HorseArmature) patch.getArmature();

            Pose pose = patch.getAnimator().getPose(1.0f);
            if (pose == null) return;

// Accumulate from root to chest using bind pose + animation
            OpenMatrix4f modelMatrix = accumulateAnimated(
                    horseArmature.rootJoint,
                    horseArmature.chest,
                    new OpenMatrix4f(),
                    pose
            );

            if (modelMatrix == null) return;

// m31 is the Y translation in the accumulated model matrix
            float chestModelY = modelMatrix.m31;
            cir.setReturnValue((double) chestModelY);
        }
    }
    @Unique
    private OpenMatrix4f accumulateAnimated(Joint current, Joint target, OpenMatrix4f parentTransform, Pose pose) {
        // Instead of copying, multiply bind pose into a fresh matrix
        OpenMatrix4f currentTransform = OpenMatrix4f.mul(parentTransform, current.getLocalTransform(), null);

        // Apply animation delta on top
        JointTransform jt = pose.get(current.getName());
        if (jt != null) {
            // Check what JointTransform exposes — autocomplete on jt.
            // Look for: toMatrix(), getMatrix(), translation/rotation fields
            OpenMatrix4f animMatrix = jt.toMatrix(); // adjust method name
            OpenMatrix4f.mul(currentTransform, animMatrix, currentTransform);
        }

        if (current.equals(target)) {
            return currentTransform;
        }

        for (Joint child : current.getSubJoints()) {
            OpenMatrix4f result = accumulateAnimated(child, target, currentTransform, pose);
            if (result != null) return result;
        }

        return null;
    }
}
