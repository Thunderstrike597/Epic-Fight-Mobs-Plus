package net.kenji.epic_fight_mobs_plus.mixins;

import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PathNavigation.class)
public interface PathNavigationMixin {
    @Accessor("speedModifier")
    double getSpeedModifier();
}
