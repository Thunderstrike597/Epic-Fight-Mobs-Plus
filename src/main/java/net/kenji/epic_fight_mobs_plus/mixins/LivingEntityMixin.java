package net.kenji.epic_fight_mobs_plus.mixins;

import net.kenji.epic_fight_mobs_plus.api.MobsPlusCommonHandler;
import net.kenji.epic_fight_mobs_plus.api.interfaces.IAnimalMobPatch;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin(value = LivingEntity.class, remap = false)
public class LivingEntityMixin {

    @Shadow
    public float zza;

    @Inject(method = "getSpeed", at = @At("RETURN"), cancellable = true)
    public void onSetSpeed(CallbackInfoReturnable<Float> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        IAnimalMobPatch patchInterface = MobsPlusCommonHandler.getIMobPatch(self);

        if (patchInterface != null) {
            if (cir.getReturnValue() != null) {
                float current = cir.getReturnValue();
                LivingEntityPatch<?> patch = patchInterface.getEntityPatch();
                if (patch == null) return;
                if (!patchInterface.shouldRun()) {
                    cir.setReturnValue(patchInterface.getWalkSpeed());
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"), cancellable = true)
    public void onAiStep(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        IAnimalMobPatch patchInterface = MobsPlusCommonHandler.getIMobPatch(self);

        if (patchInterface != null) {
            if (patchInterface.isIdleActionPlaying()) {
                //zza = 0;
            }
        }
    }
}