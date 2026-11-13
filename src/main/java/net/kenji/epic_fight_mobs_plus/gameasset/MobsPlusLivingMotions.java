package net.kenji.epic_fight_mobs_plus.gameasset;

import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;

public enum MobsPlusLivingMotions implements LivingMotion {
    MOUNT_IDLE(null),
    MOUNT_SLOW_FORWARD(null),
    MOUNT_FORWARD(null),
    MOUNT_BACKWARD(null),
    HORSE_TROT(null),
    IDLE_ACTION(null),
    IDLE_VAR2(LivingMotions.IDLE),
    IDLE_VAR3(LivingMotions.IDLE),
    IDLE_VAR4(LivingMotions.IDLE),
    WOLF_SHAKE_OFF(null),
    FOX_POUNCE_READY(null),
    FOX_POUNCE(null),
    CAT_LAY(null),
    DYING(null);

    public final LivingMotion parentMotion;
    private final int id;

    MobsPlusLivingMotions(LivingMotion parentMotion) {
        this.parentMotion = parentMotion;
        this.id = LivingMotion.ENUM_MANAGER.assign(this);
    }

    @Override
    public int universalOrdinal() {
        return this.id;
    }
}