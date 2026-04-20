package net.kenji.epic_fight_mobs_plus.mixins;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Cat.class)
public class CatMixin {
    @Redirect(
            method = "registerGoals",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V"
            )
    )
    private void redirectAddGoal(GoalSelector selector, int priority, Goal goal) {

        // ❌ Filter out combat-related goals
        if (goal instanceof LeapAtTargetGoal ||
                goal instanceof OcelotAttackGoal) {
            return; // skip adding
        }

        // ✅ Let everything else through
        selector.addGoal(priority, goal);
    }
}
