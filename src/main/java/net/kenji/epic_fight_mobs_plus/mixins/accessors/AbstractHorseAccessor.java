package net.kenji.epic_fight_mobs_plus.mixins.accessors;

import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractHorse.class)
public interface AbstractHorseAccessor {

    @Accessor("playerJumpPendingScale")
    float getPlayerJumpScale();
}
