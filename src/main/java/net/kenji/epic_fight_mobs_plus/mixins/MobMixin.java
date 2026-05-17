package net.kenji.epic_fight_mobs_plus.mixins;

import net.kenji.epic_fight_mobs_plus.api.MobsPlusCommonHandler;
import net.kenji.epic_fight_mobs_plus.api.interfaces.IAnimalMobPatch;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

@Mixin(value = Mob.class, remap = false)
public class MobMixin {

    @Inject(method = "registerGoals", at = @At("HEAD"), cancellable = true)
    public void onRegisterGoals(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object)this;
        IAnimalMobPatch patchInterface = MobsPlusCommonHandler.getIMobPatch(self);

            if (patchInterface != null) {
                if(patchInterface.shouldInterceptAi())
                    ci.cancel();
            }
    }
}
