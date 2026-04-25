package net.kenji.epic_fight_mobs_plus.mixins.accessors;

import doggytalents.common.entity.Dog;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Dog.class)
public interface DogAccessor {
    @Accessor("isShaking")
    boolean getIsShaking();
}
