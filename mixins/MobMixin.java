package net.kenji.epic_fight_mobs_plus.mixins;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Wolf;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobMixin {

    @Inject(method = "registerGoals", at = @At("HEAD"), cancellable = true)
    public void onRegisterGoals(CallbackInfo ci){
        Mob self = (Mob) (Object)this;
        if(self instanceof Wolf wolf) {
            ci.cancel();
        }
    }
}
