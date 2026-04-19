package net.kenji.epic_fight_mobs_plus.gameasset.mob_patches;

import net.kenji.epic_fight_mobs_plus.api.interfaces.AnimalMobPatchInterface;
import net.kenji.epic_fight_mobs_plus.gameasset.animations.MobsPlusAnimations;
import net.kenji.epic_fight_mobs_plus.goals.ChasePassiveMobGoal;
import net.kenji.epic_fight_mobs_plus.network.ClientWolfRunPacket;
import net.kenji.epic_fight_mobs_plus.network.MobsPlusPacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.jline.utils.Log;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.capabilities.entitypatch.Factions;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.capabilities.entitypatch.mob.SpiderPatch;
import yesman.epicfight.world.capabilities.entitypatch.mob.WitherSkeletonPatch;
import yesman.epicfight.world.capabilities.entitypatch.mob.ZombiePatch;
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
    public boolean cachedShouldRun = false;

    @Override
    public void tick(LivingEvent.LivingTickEvent event) {
        if (!this.getOriginal().level().isClientSide()) {
            cachedShouldRun = computeShouldRun();
            MobsPlusPacketHandler.sendToAll(new ClientWolfRunPacket(getOriginal().getId(), cachedShouldRun));
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
        this.original.goalSelector.addGoal(1, new FloatGoal(this.original));
        this.original.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this.original));
        this.original.goalSelector.addGoal(
                1,
                new AnimatedAttackGoal<>(this, new CombatBehaviors.Builder<>().newBehaviorSeries(CombatBehaviors.BehaviorSeries.builder().weight(10).nextBehavior(CombatBehaviors.Behavior.builder().animationBehavior(MobsPlusAnimations.WOLF_ATTACK_1))).build(this))
        );
        this.original.goalSelector.addGoal(6, new FollowOwnerGoal(this.original, 1.0D, 7.5F, 2.0F, false));
        this.original.goalSelector.addGoal(7, new BreedGoal(this.original, 1.0D));
        this.original.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this.original, 1.0D));
        this.original.goalSelector.addGoal(9, new BegGoal(this.original, 8.0F));
        this.original.goalSelector.addGoal(10, new LookAtPlayerGoal(this.original, Player.class, 8.0F));
        this.original.goalSelector.addGoal(10, new RandomLookAroundGoal(this.original));
        this.original.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this.original));
        this.original.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this.original));
        this.original.targetSelector.addGoal(3, (new HurtByTargetGoal(this.original)).setAlertOthers());
        this.original.goalSelector.addGoal(1, new TargetChasingGoal(this, (PathfinderMob)this.original, (double)1.0F, false));
        this.original.targetSelector.addGoal(5, new ChasePassiveMobGoal(this, this.getOriginal(), Sheep.class, 1.0D, false));
        this.original.targetSelector.addGoal(5, new ChasePassiveMobGoal(this, this.getOriginal(), Turtle.class, 1.0D, false));
        this.original.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this.original, AbstractSkeleton.class, false));
        this.original.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this.original, true));

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
        return cachedShouldRun;
    }
    @Override
    public LivingEntityPatch<?> getEntityPatch() {
        return this;
    }
}