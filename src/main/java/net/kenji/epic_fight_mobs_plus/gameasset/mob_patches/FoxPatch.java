package net.kenji.epic_fight_mobs_plus.gameasset.mob_patches;

import com.mojang.datafixers.util.Pair;
import net.kenji.epic_fight_mobs_plus.api.IdleActionManager;
import net.kenji.epic_fight_mobs_plus.api.abstract_classes.AnimalPatchBase;
import net.kenji.epic_fight_mobs_plus.api.animation_types.IdleActionAnimation;
import net.kenji.epic_fight_mobs_plus.api.conditions.FoxPounceCondition;
import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusLivingMotions;
import net.kenji.epic_fight_mobs_plus.gameasset.animations.MobsPlusAnimations;
import net.kenji.epic_fight_mobs_plus.goals.AnimatedFoxPounceGoal;
import net.kenji.epic_fight_mobs_plus.goals.TargetChasingWithPredicateGoal;
import net.kenji.epic_fight_mobs_plus.mixins.accessors.LivingEntityAccessor;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

import java.util.List;

public class FoxPatch<H extends Fox> extends AnimalPatchBase<Fox> {

    @Override
    public void tick(LivingEvent.LivingTickEvent event) {
        super.tick(event);
    }

    public static int MAX_COUNTER = 20;
    public int isFollowingOwnerCounter = 20;
    @Override
    public boolean computeShouldRun() {
        boolean followGoalActive = false;

        for (WrappedGoal wrappedGoal : this.getOriginal().goalSelector.getRunningGoals().toList()) {
            if (wrappedGoal.getGoal() instanceof FollowOwnerGoal || wrappedGoal.getGoal() instanceof AvoidEntityGoal<?>) {
                followGoalActive = true;
                isFollowingOwnerCounter = MAX_COUNTER;
                this.getOriginal().getPersistentData().putBoolean("is_running", true);
            }
        }

        // Decrement counter and update flag
        if (isFollowingOwnerCounter > 0) {
            isFollowingOwnerCounter--;
            shouldRun = followGoalActive || isFollowingOwnerCounter > 0;
        } else {
            shouldRun = false;
            this.getOriginal().getPersistentData().putBoolean("is_running", false);
        }

        return shouldRun ||  this.getOriginal().getPersistentData().getBoolean("is_following");
    }
    public boolean isPounceGoalRunning() {
        for (WrappedGoal goal : this.getOriginal().goalSelector.getRunningGoals().toList()) {
            if (goal.getGoal() instanceof Fox.FoxPounceGoal || goal.getGoal() instanceof AnimatedFoxPounceGoal) {

                return true;
            }
        }
        return false;
    }

    @Override
    public AssetAccessor<? extends StaticAnimation> getHitAnimation(StunType stunType) {
        return null;
    }

    @Override
    public void updateMotion(boolean b) {
        if (this.getOriginal().isSitting() ||
                this.getOriginal().isSleeping() || this.getOriginal().isPouncing()) {

            IdleActionManager.IdleActionState state = IdleActionManager.getIdleActionState(this.getOriginal().getUUID());
            if (state.animationPlaying) {
                IdleActionManager.clearIdleActionState(this.quedIdleAction, this, state);
            }
        }
        if (this.getOriginal().isSitting()) {
            this.currentLivingMotion = LivingMotions.SIT;
            this.currentCompositeMotion = LivingMotions.SIT;
            currentOptionalLivingMotion = currentLivingMotion;

            return;
        }
        if (this.getOriginal().isSleeping()) {
            this.currentLivingMotion = LivingMotions.SLEEP;
            this.currentCompositeMotion = LivingMotions.SLEEP;
            currentOptionalLivingMotion = currentLivingMotion;

            return;
        }
        if (isPounceGoalRunning() && !this.getOriginal().isPouncing()) {
            this.currentLivingMotion = MobsPlusLivingMotions.FOX_POUNCE_READY;
            this.currentCompositeMotion = MobsPlusLivingMotions.FOX_POUNCE_READY;
            currentOptionalLivingMotion = currentLivingMotion;

            return;
        }
        if (this.getOriginal().isPouncing()) {
            this.currentLivingMotion = MobsPlusLivingMotions.FOX_POUNCE;
            this.currentCompositeMotion = MobsPlusLivingMotions.FOX_POUNCE;
            currentOptionalLivingMotion = currentLivingMotion;

            return;
        }
        super.updateMotion(b);
    }
    @Override
    protected void initAI() {
        this.original.goalSelector.addGoal(
                1,
                new AnimatedAttackGoal<>(this, new CombatBehaviors.Builder<>().newBehaviorSeries(CombatBehaviors.BehaviorSeries.builder().weight(10).nextBehavior(CombatBehaviors.Behavior.builder().predicate(new FoxPounceCondition(true)).animationBehavior(MobsPlusAnimations.CAT_ATTACK).withinDistance(0.1F, 1.75F))).build(this))
        );
        this.original.goalSelector.addGoal(1, new TargetChasingWithPredicateGoal(this, (PathfinderMob)this.original, (double)1.0F, false, new FoxPounceCondition(false)));
        this.getOriginal().goalSelector.addGoal(6, new AnimatedFoxPounceGoal(this));
    }
    @Override
    protected void initAnimator(Animator animator) {
        super.initAnimator(animator);
        animator.addLivingAnimation(LivingMotions.IDLE, MobsPlusAnimations.FOX_IDLE);
        animator.addLivingAnimation(LivingMotions.WALK, MobsPlusAnimations.FOX_WALK);
        animator.addLivingAnimation(LivingMotions.CHASE, MobsPlusAnimations.FOX_RUN);
        animator.addLivingAnimation(LivingMotions.SIT, MobsPlusAnimations.FOX_SIT);
        animator.addLivingAnimation(LivingMotions.SLEEP, MobsPlusAnimations.FOX_SLEEP);
        animator.addLivingAnimation(LivingMotions.DEATH, MobsPlusAnimations.CAT_DEATH);
        animator.addLivingAnimation(MobsPlusLivingMotions.FOX_POUNCE, MobsPlusAnimations.FOX_POUNCE_LEAP);
        animator.addLivingAnimation(MobsPlusLivingMotions.FOX_POUNCE_READY, MobsPlusAnimations.FOX_POUNCE_READY);

    }
    public float getAnimForwardSpeed(float minSpeed, float maxSpeed) {

        double forwardSpeed = getCurrentForwardSpeed();

        if (forwardSpeed < -0.05F) return -1F;

        // Get the horse's actual max walk speed attribute
        double maxWalkSpeed = getEntityPatch().getOriginal().getAttributeValue(Attributes.MOVEMENT_SPEED);

        // Normalize: 0.0 when still, 1.0 at full walk speed
        double normalized = Math.min(forwardSpeed / maxWalkSpeed, 1.0);

        // Scale between your known good minimum and maximum playback speed
        return (float)(minSpeed + normalized * (maxSpeed - minSpeed)) + (computeShouldRun() ? 2.0F : 0);
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

        return shouldRun() && (getCurrentForwardSpeed() > this.getWalkSpeed());
    }
    @Override
    public boolean shouldRun() {
       if(!this.getOriginal().isSteppingCarefully()) {

           return this.getOriginal().getTarget() != null || (getCurrentForwardSpeed() > this.getWalkSpeed());
       }
        return false;
    }

    @Override
    public float getWalkSpeed() {
        if(!shouldRun)
            return ((LivingEntityAccessor)this.getOriginal()).getSpeedAccessor() / 2;
        return ((LivingEntityAccessor)this.getOriginal()).getSpeedAccessor();
    }

    @Override
    public void setShouldRun(boolean value) {
        shouldRun = value;
    }

    @Override
    public boolean shouldInterceptAi() {
        return false;
    }

    @Override
    public List<Pair<LivingMotion, AnimationManager.AnimationAccessor<? extends IdleActionAnimation>>> getIdleActionAnimations() {
        return List.of();
    }
    @Override
    public void queIdleAction(AnimationManager.AnimationAccessor<? extends IdleActionAnimation> idleAction) {
        quedIdleAction = idleAction;
    }
    @Override
    public boolean isIdleActionPlaying() {
        return this.getCurrentLivingMotion() == MobsPlusLivingMotions.IDLE_ACTION;
    }
    @Override
    public int getMinIdleActionInterval() {
        return 2;
    }

    @Override
    public int getMaxIdleActionInterval() {
        return 12;
    }

    @Override
    public AnimationManager.AnimationAccessor<? extends IdleActionAnimation> getQuedIdleAction() {
        return quedIdleAction;
    }
    @Override
    public LivingEntityPatch<?> getEntityPatch() {
        return this;
    }
}