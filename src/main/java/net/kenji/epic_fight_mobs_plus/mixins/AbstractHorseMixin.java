package net.kenji.epic_fight_mobs_plus.mixins;

import net.kenji.epic_fight_mobs_plus.gameasset.armatures.HorseArmature;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.HorsePatch;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.JointTransform;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

@Mixin(value = AbstractHorse.class, remap = false)
public class AbstractHorseMixin {

    @Shadow
    private float standAnimO;

    @Inject(method = "getPassengerAttachmentPoint", at = @At("RETURN"), cancellable = true)
    public void getPassengerRidingPosition(Entity p_294756_, EntityDimensions p_295396_, float p_296362_, CallbackInfoReturnable<Vec3> cir) {
        AbstractHorse self = (AbstractHorse)(Object)this;


        HorsePatch<?> patch = EpicFightCapabilities.getEntityPatch(self, HorsePatch.class);
        if (patch == null) return;
        HorseArmature horseArmature = (HorseArmature) patch.getArmature();

        Pose pose = patch.getAnimator().getPose(1.0f);
        if (pose == null) return;

        OpenMatrix4f modelMatrix = accumulateAnimated(
                horseArmature.rootJoint,
                horseArmature.chest,
                new OpenMatrix4f(),
                pose
        );

        if (modelMatrix == null) return;

        float chestModelY = modelMatrix.m31;
        Vec3 base = cir.getReturnValue();

        // base already has vanilla rearing offset applied (since we're at RETURN)
        // Calculate what vanilla's base Y would have been without rearing
        // by getting the super result — but since we can't call super easily,
        // just add chestModelY as the animated component while keeping
        // the rearing delta (base.y - defaultAttachmentY)
         double rearingDelta = 0.15;

        cir.setReturnValue(new Vec3(base.x, chestModelY + rearingDelta, base.z));
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
