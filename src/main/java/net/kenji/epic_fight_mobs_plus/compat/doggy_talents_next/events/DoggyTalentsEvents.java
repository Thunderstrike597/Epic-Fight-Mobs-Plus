package net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.events;

import doggytalents.DoggyEntityTypes;
import net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.patches.DogPatch;
import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusArmatures;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import yesman.epicfight.api.forgeevent.EntityPatchRegistryEvent;
import yesman.epicfight.gameasset.Armatures;

public class DoggyTalentsEvents {


    public static void registerPatchedEntities(EntityPatchRegistryEvent event) {
       event.getTypeEntry().put(DoggyEntityTypes.DOG.get(), entity -> DogPatch::new);
    }

    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(DoggyTalentsEvents::registerEntityTypeArmatures);
    }

    private static void registerEntityTypeArmatures() {
        Armatures.registerEntityTypeArmature(DoggyEntityTypes.DOG.get(), MobsPlusArmatures.WOLF);
    }
}
