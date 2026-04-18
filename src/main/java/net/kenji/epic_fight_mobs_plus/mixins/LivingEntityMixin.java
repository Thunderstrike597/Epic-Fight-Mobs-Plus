package net.kenji.epic_fight_mobs_plus.mixins;

import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.WolfPatch;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "getSpeed", at = @At("RETURN"), cancellable = true)
    public void onSetSpeed(CallbackInfoReturnable<Float> cir){
        LivingEntity self = (LivingEntity) (Object)this;
        if(self instanceof Wolf wolf) {
            if(cir.getReturnValue() != null) {
                float current = cir.getReturnValue();
                @Nullable WolfPatch wolfPatch = EpicFightCapabilities.getEntityPatch(wolf, WolfPatch.class);
                if(wolfPatch == null) return;
                if (!wolfPatch.cachedShouldRun) {
                    cir.setReturnValue(current / 2);
                }
            }
        }
    }
}
