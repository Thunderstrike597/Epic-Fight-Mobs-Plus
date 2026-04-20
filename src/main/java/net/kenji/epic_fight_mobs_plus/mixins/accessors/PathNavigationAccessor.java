package net.kenji.epic_fight_mobs_plus.mixins.accessors;

import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PathNavigation.class)
public interface PathNavigationAccessor {
    @Accessor("speedModifier")
    double getSpeedModifier();
}
