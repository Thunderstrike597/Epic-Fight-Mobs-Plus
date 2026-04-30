package net.kenji.epic_fight_mobs_plus.gameasset.mob_patches;

import net.kenji.epic_fight_mobs_plus.api.abstract_classes.AnimalPatchBase;
import net.kenji.epic_fight_mobs_plus.api.animation_types.IdleActionAnimation;
import net.kenji.epic_fight_mobs_plus.api.interfaces.IAnimalMobPatch;
import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusLivingMotions;
import net.kenji.epic_fight_mobs_plus.gameasset.animations.MobsPlusAnimations;
import net.kenji.epic_fight_mobs_plus.mixins.accessors.LivingEntityAccessor;
import net.kenji.epic_fight_mobs_plus.network.ClientOptionalLivingMotionPacket;
import net.kenji.epic_fight_mobs_plus.network.ClientPetRunPacket;
import net.kenji.epic_fight_mobs_plus.network.MobsPlusPacketHandler;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.AnimationManager;
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

import java.util.List;

public class FoxPatch<H extends Fox> extends AnimalPatchBase<Fox> {
    public AnimationManager.AnimationAccessor<? extends IdleActionAnimation> quedIdleAction = null;
    private LivingMotion currentOptionalLivingMotion;


    public boolean shouldRun = false;

    @Override
    public void tick(LivingEvent.LivingTickEvent event) {
        if (!this.getOriginal().level().isClientSide()) {
            shouldRun = computeRun();
            MobsPlusPacketHandler.sendToAll(new ClientPetRunPacket(getOriginal().getId(), shouldRun));
        }
        updateMotion(false);
        if (!this.getOriginal().level().isClientSide()) {
            MobsPlusPacketHandler.sendToAll(new ClientOptionalLivingMotionPacket(getOriginal().getId(), currentOptionalLivingMotion != null ? currentOptionalLivingMotion.universalOrdinal() : -1));
        }
        super.tick(event);
    }

    public boolean canRun = false;
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
            canRun = followGoalActive || isFollowingOwnerCounter > 0;
        } else {
            canRun = false;
            this.getOriginal().getPersistentData().putBoolean("is_running", false);
        }

        return canRun ||  this.getOriginal().getPersistentData().getBoolean("is_following");
    }

    @Override
    public AssetAccessor<? extends StaticAnimation> getHitAnimation(StunType stunType) {
        return null;
    }

    @Override
    public void updateMotion(boolean b) {
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
        super.commonMobUpdateMotion(b);
    }
    @Override
    protected void initAI() {
        this.original.goalSelector.addGoal(
                1,
                new AnimatedAttackGoal<>(this, new CombatBehaviors.Builder<>().newBehaviorSeries(CombatBehaviors.BehaviorSeries.builder().weight(10).nextBehavior(CombatBehaviors.Behavior.builder().animationBehavior(MobsPlusAnimations.CAT_ATTACK).withinDistance(0.1F, 1.75F))).build(this))
        );
        this.original.goalSelector.addGoal(1, new TargetChasingGoal(this, (PathfinderMob)this.original, (double)1.0F, false));

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
    public List<AnimationManager.AnimationAccessor<? extends IdleActionAnimation>> getIdleActionAnimations() {
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