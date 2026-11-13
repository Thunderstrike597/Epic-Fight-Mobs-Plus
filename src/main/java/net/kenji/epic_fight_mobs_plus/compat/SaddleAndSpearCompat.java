package net.kenji.epic_fight_mobs_plus.compat;

import com.eugene.saddleandspur.HorseSystems;
import com.eugene.saddleandspur.RidingInputState;
import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusLivingMotions;
import net.kenji.epic_fight_mobs_plus.gameasset.animations.MobsPlusAnimations;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.HorsePatch;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class SaddleAndSpearCompat {


    public static boolean isHorseRunning(PlayerPatch<?> patch, AbstractHorse horse){
        if(horse == null)return true;
        if(EpicFightMobsPlus.SAS_LOADED){
            return RidingInputState.running(patch.getOriginal());
        }
        return true;
    }
    public static LivingMotion getTrotPlayerMotion(PlayerPatch<?> patch, AbstractHorse horse){
        LivingMotion defaultMotion = EpicFightMobsPlus.EFX_LOADED ? MobsPlusLivingMotions.MOUNT_IDLE: LivingMotions.MOUNT;

        if(EpicFightMobsPlus.SAS_LOADED){
            HorsePatch<?> horsePatch = EpicFightCapabilities.getEntityPatch(horse, HorsePatch.class);

            if(horsePatch == null)
                return defaultMotion;
            if(horsePatch.getCurrentForwardSpeed() > 0.02F){
                return MobsPlusLivingMotions.MOUNT_SLOW_FORWARD;
            }
            return defaultMotion;

        }
        return defaultMotion;
    }


}
