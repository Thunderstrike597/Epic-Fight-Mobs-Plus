package net.kenji.epic_fight_mobs_plus.mixins.animal_mixins;

import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Wolf.class)
public class WolfMixin {
    @Redirect(
            method = "registerGoals",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V"
            )
    )
    private void redirectAddGoal(GoalSelector selector, int priority, Goal goal) {

        if (goal instanceof LeapAtTargetGoal ||
                goal instanceof MeleeAttackGoal) {
            return; // skip adding
        }

        selector.addGoal(priority, goal);
    }
}
