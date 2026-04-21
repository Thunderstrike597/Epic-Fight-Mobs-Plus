package net.kenji.epic_fight_mobs_plus.api.interfaces;

import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public interface AnimalMobPatchInterface {
    boolean shouldRun();
    boolean shouldRunWithAnim();
    float getWalkSpeed();
    void setShouldRun(boolean value);
    boolean shouldInterceptAi();
    LivingEntityPatch<?> getEntityPatch();

}
