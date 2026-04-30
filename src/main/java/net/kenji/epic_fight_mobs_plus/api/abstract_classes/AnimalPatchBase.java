package net.kenji.epic_fight_mobs_plus.api.abstract_classes;

import net.kenji.epic_fight_mobs_plus.api.interfaces.IAnimalMobPatch;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;

import javax.swing.text.html.parser.Entity;

public abstract class AnimalPatchBase<T extends Animal> extends MobPatch<T> implements IAnimalMobPatch {

    public float getAnimForwardSpeed(float minSpeed, float maxSpeed) {

        double forwardSpeed = getCurrentForwardSpeed();

        if (forwardSpeed < -0.05F) return -1F;

        // Get the horse's actual max walk speed attribute
        double maxWalkSpeed = getEntityPatch().getOriginal().getAttributeValue(Attributes.MOVEMENT_SPEED);

        // Normalize: 0.0 when still, 1.0 at full walk speed
        double normalized = Math.min(forwardSpeed / maxWalkSpeed, 1.0);

        // Scale between your known good minimum and maximum playback speed
        return (float)(minSpeed + normalized * (maxSpeed - minSpeed));
    }
    public double getCurrentForwardSpeed() {
        Vec3 movement = this.getOriginal().getDeltaMovement();
        Vec3 forward = this.getEntityPatch().getOriginal().getForward();
        return movement.dot(forward);
    }
}
