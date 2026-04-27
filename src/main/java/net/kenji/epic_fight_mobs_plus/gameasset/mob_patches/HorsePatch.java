package net.kenji.epic_fight_mobs_plus.gameasset.mob_patches;

import net.kenji.epic_fight_mobs_plus.api.interfaces.AnimalMobPatchInterface;
import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusLivingMotions;
import net.kenji.epic_fight_mobs_plus.gameasset.animations.MobsPlusAnimations;
import net.kenji.epic_fight_mobs_plus.mixins.accessors.LivingEntityAccessor;
import net.kenji.epic_fight_mobs_plus.network.ClientOptionalLivingMotionPacket;
import net.kenji.epic_fight_mobs_plus.network.ClientPetRunPacket;
import net.kenji.epic_fight_mobs_plus.network.MobsPlusPacketHandler;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.*;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.world.capabilities.entitypatch.Factions;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.damagesource.StunType;

import java.util.List;

public class HorsePatch<H extends AbstractHorse> extends MobPatch<Horse> implements AnimalMobPatchInterface {
    public AnimationManager.AnimationAccessor<? extends StaticAnimation> quedIdleAction = null;
    private LivingMotion currentOptionalLivingMotion;

    public HorsePatch() {
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
        if (!this.getOriginal().level().isClientSide()) {
            MobsPlusPacketHandler.sendToAll(new ClientOptionalLivingMotionPacket(getOriginal().getId(), currentOptionalLivingMotion != null ? currentOptionalLivingMotion.universalOrdinal() : -1));
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
        animator.addLivingAnimation(LivingMotions.DEATH, MobsPlusAnimations.HORSE_DEATH);

    }

    @Override
    public LivingMotion getOptionalLivingMotion() {
        return this.currentOptionalLivingMotion;
    }

    @Override
    public void setOptionalLivingMotion(int motionId) {
        if(motionId == -1){
            this.currentOptionalLivingMotion = null;
            return;
        }
        this.currentOptionalLivingMotion = LivingMotion.ENUM_MANAGER.get(motionId);
    }
    @Override
    public boolean shouldRunWithAnim() {
        return shouldRun();
    }

    @Override
    public boolean shouldRun() {
        return getCurrentForwardSpeed() > 0.15F;
    }
    @Override
    public float getWalkSpeed() {
        return ((LivingEntityAccessor)this.getOriginal()).getSpeedAccessor();
    }
    @Override
    public double getCurrentForwardSpeed() {
        Vec3 movement = this.getOriginal().getDeltaMovement();
        Vec3 forward = this.getEntityPatch().getOriginal().getForward();
        return movement.dot(forward);
    }
    @Override
    public void setShouldRun(boolean value) {

    }
    @Override
    public boolean shouldInterceptAi() {
        return false;
    }

    @Override
    public List<AnimationManager.AnimationAccessor<? extends StaticAnimation>> getIdleActionAnimations() {
        return List.of(MobsPlusAnimations.HORSE_IDLE_ACTION_1, MobsPlusAnimations.HORSE_IDLE_ACTION_2);
    }

    @Override
    public void queIdleAction(AnimationManager.AnimationAccessor<? extends StaticAnimation> idleAction) {
        quedIdleAction = idleAction;
    }
    @Override
    public int getMinIdleActionInterval() {
        return 2;
    }

    @Override
    public int getMaxIdleActionInterval() {
        return 16;
    }
    @Override
    public boolean isIdleActionPlaying() {
        return this.getCurrentLivingMotion() == MobsPlusLivingMotions.IDLE_ACTION;
    }

    @Override
    public AnimationManager.AnimationAccessor<? extends StaticAnimation> getQuedIdleAction() {
        return quedIdleAction;
    }

    @Override
    public LivingEntityPatch<?> getEntityPatch() {
        return this;
    }
}