package net.kenji.epic_fight_mobs_plus.mixins.accessors;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {

    @Invoker("getBlockJumpFactor")
    float invokeGetBlockJumpFactor();
}
