package net.kenji.epic_fight_mobs_plus.compat;

import com.eugene.saddleandspur.HorseSystems;
import com.eugene.saddleandspur.RidingInputState;
import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class SaddleAndSpearCompat {


    public static boolean isHorseRunning(PlayerPatch<?> patch, AbstractHorse horse){
        if(horse == null)return true;
        if(EpicFightMobsPlus.SAS_LOADED){
            return RidingInputState.running(patch.getOriginal());
        }
        return true;
    }

}
