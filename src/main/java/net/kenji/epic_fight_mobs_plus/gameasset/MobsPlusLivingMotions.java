package net.kenji.epic_fight_mobs_plus.gameasset;

import yesman.epicfight.api.animation.LivingMotion;

public enum MobsPlusLivingMotions implements LivingMotion {
    MOUNT_FORWARD,
    MOUNT_BACKWARD,
    IDLE_ACTION,
    IDLE_VAR2,
    IDLE_VAR3,
    IDLE_VAR4,
    WOLF_SHAKE_OFF,
    FOX_POUNCE_READY,
    FOX_POUNCE,
    CAT_LAY,
    DYING;
    @Override
    public int universalOrdinal() {
        return 0;
    }
}
