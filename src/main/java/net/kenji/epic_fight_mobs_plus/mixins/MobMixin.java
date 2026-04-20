package net.kenji.epic_fight_mobs_plus.mixins;

import net.kenji.epic_fight_mobs_plus.api.interfaces.AnimalMobPatchInterface;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.Wolf;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

@Mixin(Mob.class)
public class MobMixin {

    @Inject(method = "registerGoals", at = @At("HEAD"), cancellable = true)
    public void onRegisterGoals(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object)this;

        self.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent((cap) -> {
            if (cap instanceof AnimalMobPatchInterface patchInterface) {
                if(patchInterface.shouldInterceptAi())
                    ci.cancel();
            }
        });
    }
}
