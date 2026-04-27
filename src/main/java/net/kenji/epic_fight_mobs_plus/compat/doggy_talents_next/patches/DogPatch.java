package net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.patches;

import doggytalents.common.entity.Dog;
import doggytalents.common.entity.ai.DogFollowOwnerGoal;
import net.kenji.epic_fight_mobs_plus.api.interfaces.AnimalMobPatchInterface;
import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusLivingMotions;
import net.kenji.epic_fight_mobs_plus.gameasset.animations.MobsPlusAnimations;
import net.kenji.epic_fight_mobs_plus.mixins.accessors.DogAccessor;
import net.kenji.epic_fight_mobs_plus.mixins.accessors.DogAiManagerAccessor;
import net.kenji.epic_fight_mobs_plus.mixins.accessors.WolfAccessor;
import net.kenji.epic_fight_mobs_plus.network.ClientOptionalLivingMotionPacket;
import net.kenji.epic_fight_mobs_plus.network.ClientPetRunPacket;
import net.kenji.epic_fight_mobs_plus.network.MobsPlusPacketHandler;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.jline.utils.Log;
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

public class DogPatch<W extends TamableAnimal> extends MobPatch<Dog> implements AnimalMobPatchInterface {
    public AnimationManager.AnimationAccessor<? extends StaticAnimation> quedIdleAction = null;
    private LivingMotion currentOptionalLivingMotion;

    public DogPatch() {
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
        if (!this.getOriginal().level().isClientSide()) {
            MobsPlusPacketHandler.sendToAll(new ClientOptionalLivingMotionPacket(getOriginal().getId(), currentOptionalLivingMotion != null ? currentOptionalLivingMotion.universalOrdinal() : -1));
        }
        super.tick(event);
    }


    public boolean computeShouldRun() {
        boolean followGoalActive = false;

        for (WrappedGoal wrappedGoal : ((DogAiManagerAccessor)this.getOriginal().dogAi).getGoals()) {
            if (wrappedGoal.getGoal() instanceof DogFollowOwnerGoal || this.getOriginal().isDogFollowingSomeone()) {
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
            currentOptionalLivingMotion = currentLivingMotion;
            return;
        }
        if (((DogAccessor)this.getOriginal()).getIsShaking()) {
            this.currentLivingMotion = MobsPlusLivingMotions.WOLF_SHAKE_OFF;
            this.currentCompositeMotion = MobsPlusLivingMotions.WOLF_SHAKE_OFF;
            currentOptionalLivingMotion = currentLivingMotion;
            return;
        }
        if(this.getOriginal().isDefeated()){
            this.currentLivingMotion = MobsPlusLivingMotions.DYING;
            this.currentCompositeMotion = MobsPlusLivingMotions.DYING;
            currentOptionalLivingMotion = currentLivingMotion;
            return;
        }
        currentOptionalLivingMotion = null;
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
        this.original.goalSelector.addGoal(1, new TargetChasingGoal(this,
                (PathfinderMob) this.original, 1.0F, false));
    }

    @Override
    protected void initAnimator(Animator animator) {
        super.initAnimator(animator);

        animator.addLivingAnimation(LivingMotions.IDLE, MobsPlusAnimations.WOLF_IDLE);
        animator.addLivingAnimation(LivingMotions.WALK, MobsPlusAnimations.WOLF_WALK);
        animator.addLivingAnimation(LivingMotions.CHASE, MobsPlusAnimations.WOLF_RUN);
        animator.addLivingAnimation(LivingMotions.SIT, MobsPlusAnimations.WOLF_SITTING);
        animator.addLivingAnimation(LivingMotions.DEATH, MobsPlusAnimations.WOLF_DEATH);

        animator.addLivingAnimation(MobsPlusLivingMotions.WOLF_SHAKE_OFF, MobsPlusAnimations.WOLF_SHAKE);
        animator.addLivingAnimation(MobsPlusLivingMotions.DYING, MobsPlusAnimations.WOLF_DYING);

    }


    @Override
    public AssetAccessor<? extends StaticAnimation> getHitAnimation(StunType stunType) {
        return MobsPlusAnimations.WOLF_IDLE;
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
    public boolean shouldRun() {
       // Log.info("Logging Should Run For Dog: " + shouldRun);
        return shouldRun || this.getOriginal().isDogFollowingSomeone();
    }

    @Override
    public boolean shouldRunWithAnim() {
        return shouldRun() && getCurrentForwardSpeed() > 0.1F;
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
    public int getMinIdleActionInterval() {
        return 2;
    }

    @Override
    public int getMaxIdleActionInterval() {
        return 12;
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
    public float getWalkSpeed() {
        return this.getOriginal().getUrgentSpeedModifier() / 2;
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
