package net.kenji.epic_fight_mobs_plus.goals;

import doggytalents.api.inferface.InferTypeContext;
import doggytalents.common.entity.Dog;
import doggytalents.common.entity.DogAttackManager;
import doggytalents.common.entity.ai.DogAiManager;
import doggytalents.common.entity.ai.nav.DogFlyingNavigation;
import doggytalents.common.util.DogUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;

import java.util.EnumSet;
import java.util.Objects;

public class DogEpicFightMeleeAttackGoal extends TargetChasingGoal implements DogAiManager.IHasTickNonRunning {
    protected final Dog dog;
    private final double speedModifier;
    private int ticksUntilPathRecalc = 10;
    private int ticksUntilNextAttack;
    private final int timeOutTick = 40;
    private int waitingTick;
    private BlockPos.MutableBlockPos dogPos0;
    private boolean immediatelyPathRecalcWhenStop = false;
    private Path initialPath = null;
    private int detectReachPenalty = 0;
    private final float START_LEAPING_AT_DIS_SQR = 2.0F;
    private final float DONT_LEAP_AT_DIS_SQR = 1.0F;
    private final float LEAP_YD = 0.4F;

    public DogEpicFightMeleeAttackGoal(MobPatch<? extends PathfinderMob> mobPatch, PathfinderMob mob, Dog dog, double speedModifier, boolean longMemory) {
        super(mobPatch, mob, speedModifier, longMemory);
        this.dog = dog;
        this.speedModifier = (double)1.0F;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        if (!this.dog.getMode().shouldAttack()) {
            return false;
        } else if (this.dog.isDogLowHealth() && this.dog.getLowHealthStrategy() == Dog.LowHealthStrategy.RUN_AWAY) {
            return false;
        } else if (this.dog.fallDistance > 7.0F) {
            return false;
        } else {
            boolean restriction = false;
            if (this.dog.getMode().shouldFollowOwner() && this.dog.getCombatReturnStrategy() != Dog.CombatReturnStrategy.NONE) {
                LivingEntity owner = this.dog.getOwner();
                if (owner != null && this.dog.distanceToSqr(owner) > this.getMaxDistanceAwayFromOwner()) {
                    return false;
                }
            } else {
                restriction = !this.dog.patrolTargetLock();
            }

            LivingEntity target = this.dog.getTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                this.dog.setTarget((LivingEntity)null);
                return false;
            } else if (target.getY() >= (double)this.dog.level().getMaxBuildHeight()) {
                return false;
            } else if (this.dog.getDogRangedAttack().isApplicable(this.dog)) {
                return false;
            } else if (restriction && !this.dog.isWithinRestriction(target.blockPosition())) {
                return false;
            } else if (this.detectReachPenalty > 0) {
                return false;
            } else {
                double d0 = this.dog.distanceToSqr(target);
                this.detectReachPenalty = 5;
                this.detectReachPenalty += d0 > (double)256.0F ? 10 : 5;
                DogAttackManager attack_manager = this.dog.dogAttackManager;
                if (attack_manager.hasTaticalTarget()) {
                    this.detectReachPenalty = 25;
                    attack_manager.setDogFarChasingTarget(true);
                    this.initialPath = this.dog.getNavigation().createPath(target, 1);
                    attack_manager.setDogFarChasingTarget(false);
                    return this.initialPath != null;
                } else {
                    Path p = this.dog.getNavigation().createPath(target, 1);
                    if (p == null) {
                        return false;
                    } else if (!DogUtil.canPathReachTargetBlock(this.dog, p, target.blockPosition(), 1, this.dog.getMaxFallDistance())) {
                        this.dog.setTarget((LivingEntity)null);
                        return false;
                    } else {
                        this.initialPath = p;
                        return true;
                    }
                }
            }
        }
    }

    public void tickDogWhenNotRunning() {
        if (this.detectReachPenalty > 0) {
            --this.detectReachPenalty;
        }

    }

    public boolean canContinueToUse() {
        if (!this.dog.getMode().shouldAttack()) {
            return false;
        } else if (this.dog.fallDistance > 7.0F) {
            return false;
        } else {
            boolean restriction = false;
            if (this.dog.getMode().shouldFollowOwner() && this.dog.getCombatReturnStrategy() != Dog.CombatReturnStrategy.NONE) {
                LivingEntity owner = this.dog.getOwner();
                if (owner != null && this.dog.distanceToSqr(owner) > this.getMaxDistanceAwayFromOwner()) {
                    return false;
                }
            } else {
                restriction = !this.dog.patrolTargetLock();
            }

            int var10000 = this.waitingTick;
            Objects.requireNonNull(this);
            if (var10000 > 40) {
                return false;
            } else {
                LivingEntity livingentity = this.dog.getTarget();
                if (livingentity == null) {
                    return false;
                } else if (!livingentity.isAlive()) {
                    return false;
                } else if (livingentity.getY() >= (double)this.dog.level().getMaxBuildHeight()) {
                    return false;
                } else if (restriction && !this.dog.isWithinRestriction(livingentity.blockPosition())) {
                    return false;
                } else {
                    return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
                }
            }
        }
    }

    public void start() {
        if (this.initialPath != null) {
            this.dog.getNavigation().moveTo(this.initialPath, this.speedModifier);
        }

        this.ticksUntilPathRecalc = 10;
        this.ticksUntilNextAttack = 0;
        this.waitingTick = 0;
        this.dogPos0 = this.dog.blockPosition().mutable();
        this.immediatelyPathRecalcWhenStop = this.checkCurrentPathIfCanDoImmediateRecalc();
        DogAttackManager attack_manager = this.dog.dogAttackManager;
        attack_manager.attacking = true;
        if (attack_manager.hasTaticalTarget()) {
            attack_manager.setDogFarChasingTarget(true);
        }

    }

    public void stop() {
        this.initialPath = null;
        this.dog.setTarget((LivingEntity)null);
        this.dog.getNavigation().stop();
        DogAttackManager attack_manager = this.dog.dogAttackManager;
        attack_manager.attacking = false;
        attack_manager.setDogFarChasingTarget(false);
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity e = this.dog.getTarget();
        if (e != null) {
            PathNavigation n = this.dog.getNavigation();
            BlockPos dog_bp = this.dog.blockPosition();
            BlockPos target_bp = e.blockPosition();
            DogAttackManager attack_manager = this.dog.dogAttackManager;
            if (attack_manager.isDogFarChasingTarget()) {
                double d0 = this.dog.distanceToSqr(e.getX(), e.getY(), e.getZ());
                double min_dist = (double)(attack_manager.getStandardFollowRange() / 2 + 1);
                if (d0 < min_dist * min_dist) {
                    attack_manager.setDogFarChasingTarget(false);
                }
            }

            if (!this.dog.isDogFlying() || !this.flyingDogDashToTargetIfNeeded(e)) {
                if (dog_bp.equals(this.dogPos0)) {
                    ++this.waitingTick;
                } else {
                    this.waitingTick = 0;
                    this.dogPos0.set(dog_bp.getX(), dog_bp.getY(), dog_bp.getZ());
                }

                this.dog.getLookControl().setLookAt(e, 30.0F, 30.0F);
                double d0 = this.dog.distanceToSqr(e.getX(), e.getY(), e.getZ());
                if (this.ticksUntilPathRecalc <= 0) {
                    this.ticksUntilPathRecalc = 10;
                    if (attack_manager.isDogFarChasingTarget()) {
                        this.ticksUntilPathRecalc = 20;
                    }

                    n.moveTo(e, this.speedModifier);
                    this.immediatelyPathRecalcWhenStop = this.checkCurrentPathIfCanDoImmediateRecalc();
                }

                --this.ticksUntilPathRecalc;
                --this.ticksUntilNextAttack;
                if (n.isDone() && dog_bp.distSqr(target_bp) <= (double)2.25F && !this.canReachTarget(e, d0) && this.isTargetInSafeArea(this.dog, e, target_bp)) {
                    this.dog.getMoveControl().setWantedPosition(e.getX(), e.getY(), e.getZ(), this.speedModifier);
                }

                if (!attack_manager.isDogFarChasingTarget() && this.immediatelyPathRecalcWhenStop && n.isDone() && this.dog.tickCount % 2 != 0 && !this.canReachTarget(e, d0)) {
                    this.ticksUntilPathRecalc = 0;
                }


                this.checkAndLeapAtTarget(e);
            }
        }
    }

    protected void checkAndLeapAtTarget(LivingEntity target) {
        if (this.canLeapAtTarget(target)) {
            Vec3 vec3 = this.dog.getDeltaMovement();
            Vec3 vec31 = new Vec3(target.getX() - this.dog.getX(), (double)0.0F, target.getZ() - this.dog.getZ());
            if (vec31.lengthSqr() > 1.0E-7) {
                vec31 = vec31.normalize().scale(0.4);
            }

            Dog var10000 = this.dog;
            double var10001 = vec31.x;
            Objects.requireNonNull(this);
            var10000.setDeltaMovement(var10001, (double)0.4F, vec31.z);
        }
    }

    private boolean flyingDogDashToTargetIfNeeded(LivingEntity target) {
        double d_sqr = this.dog.distanceToSqr(target);
        if (d_sqr > (double)16.0F) {
            return false;
        } else if (this.dog.tickCount % 5 != 0) {
            return false;
        } else {
            PathNavigation var5 = this.dog.getNavigation();
            if (var5 instanceof DogFlyingNavigation) {
                DogFlyingNavigation flyNav = (DogFlyingNavigation)var5;
                if (!flyNav.canDashToTarget(target)) {
                    return false;
                } else {
                    flyNav.stop();
                    this.dog.getMoveControl().setWantedPosition(target.position().x, target.position().y, target.position().z, (double)2.0F);
                    this.ticksUntilPathRecalc = 10;
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    protected boolean canLeapAtTarget(@NotNull LivingEntity target) {
        if (this.dog.isVehicle()) {
            return false;
        } else if (!this.dog.onGround()) {
            return false;
        } else if (!target.onGround()) {
            return false;
        } else {
            double d0 = this.dog.distanceToSqr(target);
            if (d0 >= (double)1.0F && d0 <= (double)2.0F) {
                if (this.dog.getRandom().nextInt(3) != 0) {
                    return false;
                } else {
                    BlockPos tpos = target.blockPosition();
                    if (WalkNodeEvaluator.getBlockPathTypeStatic(this.dog.level(), tpos.mutable()) != BlockPathTypes.WALKABLE) {
                        return false;
                    } else {
                        Vec3 v_offset = (new Vec3(target.getX() - this.dog.getX(), (double)0.0F, target.getZ() - this.dog.getZ())).normalize();
                        Vec3 v_dog = this.dog.position();

                        for(int i = 1; i <= 3; ++i) {
                            v_dog = v_dog.add(v_offset);
                            if (WalkNodeEvaluator.getBlockPathTypeStatic(this.dog.level(), BlockPos.containing(v_dog).mutable()) != BlockPathTypes.WALKABLE) {
                                return false;
                            }
                        }

                        return true;
                    }
                }
            } else {
                return false;
            }
        }
    }

    private boolean checkCurrentPathIfCanDoImmediateRecalc() {
        Path path = this.dog.getNavigation().getPath();
        boolean ground_nav_cannot_update = this.dog.isDefaultNavigation() && !this.dog.onGround();
        if (path == null && !ground_nav_cannot_update) {
            return false;
        } else {
            return path == null || path.getNodeCount() <= 5;
        }
    }
    @Override
    protected void checkAndPerformAttack(LivingEntity target, double distanceToTargetSqr) {
    }

    protected boolean isTargetInSafeArea(Dog dog, LivingEntity target, BlockPos target_bp) {
        BlockPathTypes type = WalkNodeEvaluator.getBlockPathTypeStatic(dog.level(), target_bp.mutable());
        if (type == BlockPathTypes.OPEN) {
            return false;
        } else {
            type = dog.inferType(type, InferTypeContext.getDefault());
            return type.getDanger() == null;
        }
    }

    protected boolean canReachTarget(LivingEntity target, double distanceToTargetSqr) {
        return this.getAttackReachSqr(target) >= distanceToTargetSqr;
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(20);
    }

    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    protected int getAttackInterval() {
        return this.adjustedTickDelay(20);
    }

    protected double getAttackReachSqr(LivingEntity target) {
        return (double)(this.dog.getBbWidth() * 2.0F * this.dog.getBbWidth() * 2.0F + target.getBbWidth());
    }

    protected double getMaxDistanceAwayFromOwner() {
        double ret = (double)400.0F;
        if (this.dog.getCombatReturnStrategy() == Dog.CombatReturnStrategy.FAR) {
            ret = (double)1024.0F;
        }

        boolean has_tatical = this.dog.dogAttackManager.hasTaticalTarget();
        if (has_tatical) {
            ret += (double)10.0F;
        }

        return ret;
    }
}

