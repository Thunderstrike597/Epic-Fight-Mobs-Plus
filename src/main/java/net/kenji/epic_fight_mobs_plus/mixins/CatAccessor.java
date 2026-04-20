package net.kenji.epic_fight_mobs_plus.mixins;

import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Cat.class)
public interface CatAccessor {
    @Accessor("temptGoal")
    TemptGoal getCatTemptGoal();
}
