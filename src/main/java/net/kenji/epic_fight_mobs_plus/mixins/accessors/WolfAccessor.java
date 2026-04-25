package net.kenji.epic_fight_mobs_plus.mixins.accessors;

import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Wolf.class)
public interface WolfAccessor {
    @Accessor("isShaking")
    boolean getIsShaking();
}
