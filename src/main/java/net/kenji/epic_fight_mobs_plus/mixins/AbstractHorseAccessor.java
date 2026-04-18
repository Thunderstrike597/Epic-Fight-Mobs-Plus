package net.kenji.epic_fight_mobs_plus.mixins;

import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractHorse.class)
public interface AbstractHorseAccessor {

    @Accessor("playerJumpPendingScale")
    float getPlayerJumpScale();
}
