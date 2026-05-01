package net.kenji.epic_fight_mobs_plus.goals;

import net.minecraft.world.entity.PathfinderMob;
import org.jline.utils.Log;
import yesman.epicfight.data.conditions.Condition;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;

public class TargetChasingWithPredicateGoal extends TargetChasingGoal {
    private final Condition<MobPatch<?>> predicate;
    public TargetChasingWithPredicateGoal(MobPatch<? extends PathfinderMob> mobpatch, PathfinderMob pathfinderMob, double speedModifier, boolean longMemory, Condition<MobPatch<?>> predicate) {
        super(mobpatch, pathfinderMob, speedModifier, longMemory);
        this.predicate = predicate;
    }

    @Override
    public void tick() {
        if (!predicate.predicate(mobpatch)) {
            return;
        }
        super.tick();
    }

    @Override
    public boolean canUse() {
        if (!predicate.predicate(mobpatch)) {
            return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if(!predicate.predicate(mobpatch)) return false;
        return super.canContinueToUse();
    }
}
