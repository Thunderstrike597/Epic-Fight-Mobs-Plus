package net.kenji.epic_fight_mobs_plus.gameasset.mob_patches;

import net.kenji.epic_fight_mobs_plus.api.interfaces.AnimalMobPatchInterface;
import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusLivingMotions;
import net.kenji.epic_fight_mobs_plus.gameasset.animations.MobsPlusAnimations;
import net.kenji.epic_fight_mobs_plus.goals.ChasePassiveMobGoal;
import net.kenji.epic_fight_mobs_plus.mixins.accessors.LivingEntityAccessor;
import net.kenji.epic_fight_mobs_plus.network.ClientPetRunPacket;
import net.kenji.epic_fight_mobs_plus.network.MobsPlusPacketHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.world.capabilities.entitypatch.Factions;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;

import java.util.List;
import java.util.function.Predicate;

public class CatPatch<H extends Cat> extends MobPatch<Cat> implements AnimalMobPatchInterface {
    public AnimationManager.AnimationAccessor<? extends StaticAnimation> quedIdleAction = null;

    public CatPatch() {
        super(Factions.NEUTRAL);
    }



    public boolean shouldRun = false;

    @Override
    public void tick(LivingEvent.LivingTickEvent event) {
        if (!this.getOriginal().level().isClientSide()) {
            shouldRun = computeRun();
            MobsPlusPacketHandler.sendToAll(new ClientPetRunPacket(getOriginal().getId(), shouldRun));
        }
        updateMotion(false);
        super.tick(event);
    }

    public boolean isFollowingOwner = false;
    public static int MAX_COUNTER = 20;
    public int isFollowingOwnerCounter = 20;
    public boolean computeRun() {
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
            isFollowingOwner = followGoalActive || isFollowingOwnerCounter > 0;
        } else {
            isFollowingOwner = false;
            this.getOriginal().getPersistentData().putBoolean("is_running", false);
        }

        return isFollowingOwner ||  this.getOriginal().getPersistentData().getBoolean("is_following");
    }

    @Override
    public AssetAccessor<? extends StaticAnimation> getHitAnimation(StunType stunType) {
        return null;
    }

    @Override
    public void updateMotion(boolean b) {
        if (this.getOriginal().isInSittingPose()) {
            this.currentLivingMotion = LivingMotions.SIT;
            this.currentCompositeMotion = LivingMotions.SIT;
            return;
        }
        super.commonMobUpdateMotion(b);
    }
    @Override
    protected void initAI() {
        this.original.goalSelector.addGoal(
                1,
                new AnimatedAttackGoal<>(this, new CombatBehaviors.Builder<>().newBehaviorSeries(CombatBehaviors.BehaviorSeries.builder().weight(10).nextBehavior(CombatBehaviors.Behavior.builder().animationBehavior(MobsPlusAnimations.CAT_ATTACK).withinDistance(0.1F, 1.75F))).build(this))
        );
        this.original.goalSelector.addGoal(1, new TargetChasingGoal(this, (PathfinderMob)this.original, (double)1.0F, false));

        this.original.targetSelector.addGoal(2, new ChasePassiveMobGoal(this, this.getOriginal(), Rabbit.class, (Predicate<LivingEntity>)null,1.0D, false));
        this.original.targetSelector.addGoal(2, new ChasePassiveMobGoal(this, this.getOriginal(), Turtle.class, Turtle.BABY_ON_LAND_SELECTOR,1.0D, false));

    }
    @Override
    protected void initAnimator(Animator animator) {
        super.initAnimator(animator);
        animator.addLivingAnimation(LivingMotions.IDLE, MobsPlusAnimations.CAT_IDLE);
        animator.addLivingAnimation(LivingMotions.WALK, MobsPlusAnimations.CAT_WALK);
        animator.addLivingAnimation(LivingMotions.CHASE, MobsPlusAnimations.CAT_RUN);
        animator.addLivingAnimation(LivingMotions.SIT, MobsPlusAnimations.CAT_SITTING);

    }
    @Override
    public boolean shouldRunWithAnim() {
        return shouldRun() && (getCurrentForwardSpeed() > this.getWalkSpeed());
    }
    @Override
    public boolean shouldRun() {
       if(!this.getOriginal().isSteppingCarefully()) {
           return shouldRun || this.getOriginal().getTarget() != null;
       }
        return false;
    }

    @Override
    public float getWalkSpeed() {
        return ((LivingEntityAccessor)this.getOriginal()).getSpeedAccessor() / 2;
    }
    @Override
    public double getCurrentForwardSpeed() {
        Vec3 movement = this.getOriginal().getDeltaMovement();
        Vec3 forward = this.getEntityPatch().getOriginal().getForward();
        return movement.dot(forward);
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
    public List<AnimationManager.AnimationAccessor<? extends StaticAnimation>> getIdleActionAnimations() {
        return List.of();
    }
    @Override
    public void queIdleAction(AnimationManager.AnimationAccessor<? extends StaticAnimation> idleAction) {
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
    public AnimationManager.AnimationAccessor<? extends StaticAnimation> getQuedIdleAction() {
        return quedIdleAction;
    }
    @Override
    public LivingEntityPatch<?> getEntityPatch() {
        return this;
    }
}