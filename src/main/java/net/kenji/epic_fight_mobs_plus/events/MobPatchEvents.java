package net.kenji.epic_fight_mobs_plus.events;

import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.kenji.epic_fight_mobs_plus.api.MobPatchFactory;
import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusArmatures;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import yesman.epicfight.api.forgeevent.EntityPatchRegistryEvent;
import yesman.epicfight.gameasset.Armatures;

@Mod.EventBusSubscriber(modid = EpicFightMobsPlus.MODID)
public class MobPatchEvents {


    @SubscribeEvent
    public static void registerPatchedEntities(EntityPatchRegistryEvent event) {
        for (MobPatchFactory.MobPatchDefinitions def : MobPatchFactory.mobPatches) {
            event.getTypeEntry().put(def.entityType, def.mobPatch);
        }
    }
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(MobPatchEvents::registerEntityTypeArmatures);
    }

    private static void registerEntityTypeArmatures() {
        for (MobPatchFactory.MobPatchDefinitions def : MobPatchFactory.mobPatches) {
            Armatures.registerEntityTypeArmature(def.entityType, def.armature);
        }
    }
}