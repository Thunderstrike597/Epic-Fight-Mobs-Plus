package net.kenji.epic_fight_mobs_plus.events;

import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.kenji.epic_fight_mobs_plus.api.MobPatchFactory;
import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusArmatures;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import yesman.epicfight.api.event.types.registry.EntityPatchRegistryEvent;
import yesman.epicfight.gameasset.Armatures;

public class MobPatchEvents {



    public static void registerPatchedEntities(EntityPatchRegistryEvent event) {
      MobPatchFactory.registerPatchedEntities(event);
    }

    public static void registerEntityTypeArmatures() {
        for (MobPatchFactory.MobPatchDefinitions def : MobPatchFactory.mobPatches) {
            Armatures.registerEntityTypeArmature(def.entityType, def.armature);
        }
    }
}