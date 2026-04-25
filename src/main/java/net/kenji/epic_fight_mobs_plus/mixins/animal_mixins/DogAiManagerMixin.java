package net.kenji.epic_fight_mobs_plus.mixins.animal_mixins;

import doggytalents.common.entity.Dog;
import doggytalents.common.entity.ai.DogAiManager;
import doggytalents.common.entity.ai.DogMeleeAttackGoal;
import net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.patches.DogPatch;
import net.kenji.epic_fight_mobs_plus.gameasset.animations.MobsPlusAnimations;
import net.kenji.epic_fight_mobs_plus.goals.DogEpicFightMeleeAttackGoal;
import net.kenji.epic_fight_mobs_plus.mixins.accessors.DogAiManagerAccessor;
import net.minecraft.world.entity.ai.goal.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

@Mixin(value = DogAiManager.class, remap = false)
public class DogAiManagerMixin {
    @Shadow
    @Final
    private Dog dog;

    @Redirect(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Ldoggytalents/common/entity/ai/DogAiManager;registerDogGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)Lnet/minecraft/world/entity/ai/goal/WrappedGoal;"
            )
    )
    private WrappedGoal redirectAddGoal(DogAiManager instance, int i, Goal goal) {
        DogAiManager self = (DogAiManager)(Object)this;
        DogAiManagerAccessor accessor = (DogAiManagerAccessor)self;
        if (goal instanceof LeapAtTargetGoal ||
                goal instanceof MeleeAttackGoal ||
        goal instanceof DogMeleeAttackGoal) {
            DogPatch<?> dogPatch = EpicFightCapabilities.getEntityPatch(accessor.getDog(), DogPatch.class);
            accessor.invokeRegisterGoal(1,new AnimatedAttackGoal<>(dogPatch, new CombatBehaviors.Builder<>().newBehaviorSeries(CombatBehaviors.BehaviorSeries.builder().weight(10).nextBehavior(CombatBehaviors.Behavior.builder().animationBehavior(MobsPlusAnimations.WOLF_ATTACK_1).withinDistance(0.1F, 1.8F))).build(dogPatch)));
            return accessor.invokeRegisterGoal(i, new DogEpicFightMeleeAttackGoal(dogPatch, accessor.getDog(), this.dog, 0, false));
        }

        return accessor.invokeRegisterGoal(i, goal);
    }

}
