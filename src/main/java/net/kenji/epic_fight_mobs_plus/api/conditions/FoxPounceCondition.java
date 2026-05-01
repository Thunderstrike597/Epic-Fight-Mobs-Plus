package net.kenji.epic_fight_mobs_plus.api.conditions;

import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.FoxPatch;
import net.kenji.epic_fight_mobs_plus.goals.AnimatedFoxPounceGoal;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Fox;
import yesman.epicfight.data.conditions.Condition;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FoxPounceCondition implements Condition<MobPatch<?>> {

    private final boolean invertedCondition;
    public FoxPounceCondition(boolean invert){
        invertedCondition = invert;
    }

    @Override
    public Condition read(CompoundTag compoundTag) throws IllegalArgumentException {
        return null;
    }

    @Override
    public CompoundTag serializePredicate() {
        return null;
    }

    public boolean isPounceGoalRunning(MobPatch<?> mobPatch) {
        for (WrappedGoal goal : mobPatch.getOriginal().goalSelector.getRunningGoals().toList()) {
            if (goal.getGoal() instanceof Fox.FoxPounceGoal || goal.getGoal() instanceof AnimatedFoxPounceGoal) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean predicate(MobPatch<?> patch) {
        if(patch instanceof FoxPatch<?> foxPatch) {
            Fox fox = foxPatch.getOriginal();
            return !invertedCondition ? fox.isPouncing() || isPounceGoalRunning(foxPatch) : !fox.isPouncing() && !isPounceGoalRunning(foxPatch);
        }
        return false;
    }

    @Override
    public List<ParameterEditor> getAcceptingParameters(Screen screen) {
        return List.of();
    }
}
