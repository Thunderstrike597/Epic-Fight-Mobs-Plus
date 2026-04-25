package net.kenji.epic_fight_mobs_plus.gameasset.mob_patches;

import net.kenji.epic_fight_mobs_plus.api.interfaces.AnimalMobPatchInterface;
import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusLivingMotions;
import net.kenji.epic_fight_mobs_plus.gameasset.animations.MobsPlusAnimations;
import net.kenji.epic_fight_mobs_plus.goals.ChasePassiveMobGoal;
import net.kenji.epic_fight_mobs_plus.mixins.accessors.LivingEntityAccessor;
import net.kenji.epic_fight_mobs_plus.mixins.accessors.WolfAccessor;
import net.kenji.epic_fight_mobs_plus.network.ClientPetRunPacket;
import net.kenji.epic_fight_mobs_plus.network.MobsPlusPacketHandler;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.*;
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

public class WolfPatch<W extends TamableAnimal> extends MobPatch<Wolf> implements AnimalMobPatchInterface {
    public AnimationManager.AnimationAccessor<? extends StaticAnimation> quedIdleAction = null;

    public WolfPatch() {
        super(Factions.NEUTRAL);
    }
    public boolean isFollowingOwner = false;
    public static int MAX_COUNTER = 20;
    public int isFollowingOwnerCounter = 20;
    public boolean shouldRun = false;

    @Override
    public void tick(LivingEvent.LivingTickEvent event) {
        if (!this.getOriginal().level().isClientSide()) {
            shouldRun = computeShouldRun();
            MobsPlusPacketHandler.sendToAll(new ClientPetRunPacket(getOriginal().getId(), shouldRun));
        }
        updateMotion(false);

        super.tick(event);
    }


    public boolean computeShouldRun() {
        boolean followGoalActive = false;

        for (WrappedGoal wrappedGoal : this.getOriginal().goalSelector.getRunningGoals().toList()) {
            if (wrappedGoal.getGoal() instanceof FollowOwnerGoal) {
                followGoalActive = true;
                isFollowingOwnerCounter = MAX_COUNTER;
                this.getOriginal().getPersistentData().putBoolean("is_following", true);
            }
        }

        // Decrement counter and update flag
        if (isFollowingOwnerCounter > 0) {
            isFollowingOwnerCounter--;
            isFollowingOwner = followGoalActive || isFollowingOwnerCounter > 0;
        } else {
            isFollowingOwner = false;
            this.getOriginal().getPersistentData().putBoolean("is_following", false);
        }


        return isFollowingOwner ||  this.getOriginal().getPersistentData().getBoolean("is_following") || this.getOriginal().getTarget() != null;
    }

    @Override
    public void updateMotion(boolean b) {
        if (this.getOriginal().isInSittingPose()) {
            this.currentLivingMotion = LivingMotions.SIT;
            this.currentCompositeMotion = LivingMotions.SIT;
            return;
        }
        if (((WolfAccessor)this.getOriginal()).getIsShaking()) {
            this.currentLivingMotion = MobsPlusLivingMotions.WOLF_SHAKE_OFF;
            this.currentCompositeMotion = MobsPlusLivingMotions.WOLF_SHAKE_OFF;
            return;
        }
        super.commonMobUpdateMotion(b);
    }

    @Override
    public LivingMotion getCurrentLivingMotion() {
        return this.currentLivingMotion;
    }
    @Override
    protected void initAI() {
        this.original.goalSelector.addGoal(
                1,
                new AnimatedAttackGoal<>(this, new CombatBehaviors.Builder<>().newBehaviorSeries(CombatBehaviors.BehaviorSeries.builder().weight(10).nextBehavior(CombatBehaviors.Behavior.builder().animationBehavior(MobsPlusAnimations.WOLF_ATTACK_1).withinDistance(0.1F, 1.8F))).build(this))
        );
        this.original.goalSelector.addGoal(1, new TargetChasingGoal(this, (PathfinderMob)this.original, (double)1.0F, false));
        this.original.targetSelector.addGoal(5, new ChasePassiveMobGoal(this, this.getOriginal(), Animal.class, Wolf.PREY_SELECTOR,1.0D, false));
        this.original.targetSelector.addGoal(5, new ChasePassiveMobGoal(this, this.getOriginal(), Turtle.class, Turtle.BABY_ON_LAND_SELECTOR,1.0D, false));

    }

    @Override
    protected void initAnimator(Animator animator) {
        super.initAnimator(animator);

        animator.addLivingAnimation(LivingMotions.IDLE, MobsPlusAnimations.WOLF_IDLE);
        animator.addLivingAnimation(LivingMotions.WALK, MobsPlusAnimations.WOLF_WALK);
        animator.addLivingAnimation(LivingMotions.CHASE, MobsPlusAnimations.WOLF_RUN);
        animator.addLivingAnimation(LivingMotions.SIT, MobsPlusAnimations.WOLF_SITTING);
        animator.addLivingAnimation(MobsPlusLivingMotions.WOLF_SHAKE_OFF, MobsPlusAnimations.WOLF_SHAKE);

    }


    @Override
    public AssetAccessor<? extends StaticAnimation> getHitAnimation(StunType stunType) {
        return MobsPlusAnimations.WOLF_IDLE;
    }
    @Override
    public boolean shouldRunWithAnim() {
       return shouldRun() && (getCurrentForwardSpeed() > this.getWalkSpeed());
    }
    @Override
    public boolean shouldRun() {

        return shouldRun;
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
        return List.of(MobsPlusAnimations.WOLF_IDLE_ACTION_1);
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
    public LivingEntityPatch<?> getEntityPatch() {
        return this;
    }
}