package net.kenji.epic_fight_mobs_plus.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;

public class ChasePassiveMobGoal extends TargetChasingGoal{
    private final TamableAnimal tamableMob;

    public ChasePassiveMobGoal(MobPatch<? extends PathfinderMob> mobpatch, TamableAnimal pTamableMob, Class<?> pTargetType ,double speedModifier, boolean longMemory) {
        super(mobpatch, pTamableMob, speedModifier, longMemory);
        this.tamableMob = pTamableMob;
    }
    @Override
    public boolean canUse() {
        return !this.tamableMob.isTame() && super.canUse();
    }
}
