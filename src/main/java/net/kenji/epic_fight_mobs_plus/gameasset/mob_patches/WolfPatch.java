package net.kenji.epic_fight_mobs_plus.gameasset.mob_patches;

import net.kenji.epic_fight_mobs_plus.api.interfaces.AnimalMobPatchInterface;
import net.kenji.epic_fight_mobs_plus.gameasset.animations.MobsPlusAnimations;
import net.kenji.epic_fight_mobs_plus.goals.ChasePassiveMobGoal;
import net.kenji.epic_fight_mobs_plus.mixins.accessors.LivingEntityAccessor;
import net.kenji.epic_fight_mobs_plus.network.ClientPetRunPacket;
import net.kenji.epic_fight_mobs_plus.network.MobsPlusPacketHandler;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotion;
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

public class WolfPatch<W extends TamableAnimal> extends MobPatch<Wolf> implements AnimalMobPatchInterface {

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

    }


    @Override
    public AssetAccessor<? extends StaticAnimation> getHitAnimation(StunType stunType) {
        return MobsPlusAnimations.WOLF_IDLE;
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
    public float getWalkSpeed() {
        return ((LivingEntityAccessor)this.getOriginal()).getSpeedAccessor() / 2;
    }

    @Override
    public LivingEntityPatch<?> getEntityPatch() {
        return this;
    }
}