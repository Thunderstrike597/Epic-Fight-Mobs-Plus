package net.kenji.epic_fight_mobs_plus.api;

import net.kenji.epic_fight_mobs_plus.api.interfaces.IAnimalMobPatch;
import net.minecraft.world.entity.LivingEntity;
import yesman.epicfight.registry.entries.EpicFightAttachmentTypes;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class MobsPlusCommonHandler {

    public static LivingEntityPatch<?> getLivingEntityPatch(LivingEntity livingEntity){
        EntityPatch<?> entityPatch = livingEntity.getData(EpicFightAttachmentTypes.ENTITY_PATCH).getCapability();
       if(entityPatch instanceof LivingEntityPatch<?> livingEntityPatch){
            return livingEntityPatch;
        }
       return null;
    }

    public static IAnimalMobPatch getIMobPatch(LivingEntity livingEntity){
        if(getLivingEntityPatch(livingEntity) instanceof IAnimalMobPatch animalMobPatch){
            return animalMobPatch;
        }
        return null;
    }
}
