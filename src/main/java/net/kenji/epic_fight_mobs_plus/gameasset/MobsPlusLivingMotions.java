package net.kenji.epic_fight_mobs_plus.gameasset;

import yesman.epicfight.api.animation.LivingMotion;

public enum MobsPlusLivingMotions implements LivingMotion {
    MOUNT_FORWARD,
    MOUNT_BACKWARD,
    IDLE_ACTION,
    WOLF_SHAKE_OFF;
    @Override
    public int universalOrdinal() {
        return 0;
    }
}
