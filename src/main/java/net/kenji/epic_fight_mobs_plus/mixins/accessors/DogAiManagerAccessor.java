package net.kenji.epic_fight_mobs_plus.mixins.accessors;

import doggytalents.common.entity.Dog;
import doggytalents.common.entity.ai.DogAiManager;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.List;

@Mixin(DogAiManager.class)
public interface DogAiManagerAccessor {
    @Invoker("registerDogGoal")
    WrappedGoal invokeRegisterGoal(int priority, Goal goal);
    @Accessor("dog")
    Dog getDog();
    @Accessor("goals")
    ArrayList<WrappedGoal> getGoals();

}
