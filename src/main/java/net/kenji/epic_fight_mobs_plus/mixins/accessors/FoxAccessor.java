package net.kenji.epic_fight_mobs_plus.mixins.accessors;

import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Fox.class)
public interface FoxAccessor {
    @Invoker("setFaceplanted")
    void invokeSetFaceplanted(boolean value);
    @Accessor("crouchAmount")
    void setCrouchAmount(float amount);
    @Accessor("crouchAmountO")
    void setCrouchAmountO(float amount);
}
