package net.kenji.epic_fight_mobs_plus.mixins;

import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusLivingMotions;
import net.kenji.epic_fight_mobs_plus.gameasset.animations.MobsPlusAnimations;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.client.world.capabilites.entitypatch.player.AbstractClientPlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = AbstractClientPlayerPatch.class, remap = false)
public class AbstractClientPlayerPatchMixin {

    @Inject(method = "updateMotion", at = @At("TAIL"))
    public void onUpdateMotion(boolean considerInaction, CallbackInfo ci){
        AbstractClientPlayerPatch<?> self = (AbstractClientPlayerPatch<?>)(Object) this;
        if (((AbstractClientPlayer)self.getOriginal()).getVehicle() instanceof PlayerRideableJumping) {
            Entity vehicle = self.getOriginal().getVehicle();
            Vec3 movement = vehicle.getDeltaMovement();
            Vec3 forward = vehicle.getForward();
            double forwardSpeed = movement.dot(forward); // project movement onto facing direction
            if (forwardSpeed < 0.1F && forwardSpeed > -0.01F)
                self.currentLivingMotion = LivingMotions.MOUNT;
            else if (forwardSpeed < -0.01F)
                self.currentLivingMotion = MobsPlusLivingMotions.MOUNT_BACKWARD;
            else if (forwardSpeed > 0.1F)
                self.currentLivingMotion = MobsPlusLivingMotions.MOUNT_FORWARD;
        }
    }

}
