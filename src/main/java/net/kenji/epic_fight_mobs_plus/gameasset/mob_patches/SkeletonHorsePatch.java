package net.kenji.epic_fight_mobs_plus.gameasset.mob_patches;

import net.kenji.epic_fight_mobs_plus.api.interfaces.AnimalMobPatchInterface;
import net.kenji.epic_fight_mobs_plus.gameasset.animations.MobsPlusAnimations;
import net.kenji.epic_fight_mobs_plus.mixins.accessors.LivingEntityAccessor;
import net.kenji.epic_fight_mobs_plus.network.ClientPetRunPacket;
import net.kenji.epic_fight_mobs_plus.network.MobsPlusPacketHandler;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.world.capabilities.entitypatch.Factions;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.damagesource.StunType;

public class SkeletonHorsePatch<H extends AbstractHorse> extends MobPatch<SkeletonHorse> implements AnimalMobPatchInterface {

    public SkeletonHorsePatch() {
        super(Factions.NEUTRAL);
    }

    @Override
    public void updateMotion(boolean b) {
        super.commonMobUpdateMotion(b);
    }


    public float storedJumpSpeed = -1;
    public boolean cachedShouldRun = false;

    @Override
    public void tick(LivingEvent.LivingTickEvent event) {
        if (!this.getOriginal().level().isClientSide()) {
            cachedShouldRun = computeShouldRun();
            MobsPlusPacketHandler.sendToAll(new ClientPetRunPacket(getOriginal().getId(), cachedShouldRun));
        }
        updateMotion(false);
        if(getStoredJumpSpeed() != -1 && this.getOriginal().onGround()){
            setStoredJumpSpeed(-1);
        }
        super.tick(event);
    }

    @Override
    public AssetAccessor<? extends StaticAnimation> getHitAnimation(StunType stunType) {
        return null;
    }


    public boolean computeShouldRun() {
        boolean followGoalActive = false;

        return false;
    }

    public float setStoredJumpSpeed(float value){
        return storedJumpSpeed = value;
    }
    public float getStoredJumpSpeed(){
        return storedJumpSpeed;
    }


    @Override
    protected void initAnimator(Animator animator) {
        super.initAnimator(animator);
        animator.addLivingAnimation(LivingMotions.IDLE, MobsPlusAnimations.HORSE_IDLE);
        animator.addLivingAnimation(LivingMotions.WALK, MobsPlusAnimations.HORSE_WALK);
        animator.addLivingAnimation(LivingMotions.CHASE, MobsPlusAnimations.HORSE_RUN);
        animator.addLivingAnimation(LivingMotions.JUMP, MobsPlusAnimations.HORSE_JUMP);
    }
    @Override
    public boolean shouldRunWithAnim() {
        return shouldRun();
    }

    @Override
    public boolean shouldRun() {
        Vec3 movement = this.getOriginal().getDeltaMovement();
        Vec3 forward = this.getOriginal().getForward();
        double forwardSpeed = movement.dot(forward);
        return forwardSpeed > 0.15F;
    }
    @Override
    public float getWalkSpeed() {
        return ((LivingEntityAccessor)this.getOriginal()).getSpeedAccessor();
    }

    @Override
    public void setShouldRun(boolean value) {

    }
    @Override
    public boolean shouldInterceptAi() {
        return false;
    }

    @Override
    public LivingEntityPatch<?> getEntityPatch() {
        return this;
    }
}