package net.kenji.epic_fight_mobs_plus.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class ChasePassiveMobGoal extends TargetChasingGoal{
    private final TamableAnimal tamableMob;
    public final Predicate<LivingEntity> targetPredicate;
    public final Class<?> targetType;
    public ChasePassiveMobGoal(MobPatch<? extends PathfinderMob> mobpatch, TamableAnimal pTamableMob, Class<?> pTargetType, @Nullable Predicate<LivingEntity> pTargetPredicate ,double speedModifier, boolean longMemory) {
        super(mobpatch, pTamableMob, speedModifier, longMemory);
        this.tamableMob = pTamableMob;
        this.targetPredicate = pTargetPredicate;
        this.targetType = pTargetType;
    }


    @Override
    public boolean canUse() {
        if (this.tamableMob.isTame()) {
            return false;
        }

        if (!super.canUse()) {
            return false;
        }

        if (this.targetPredicate == null) {
            return true;
        }

        LivingEntity target = this.mobpatch.getOriginal().getTarget();

        return target != null && this.targetPredicate.test(target);
    }
}
