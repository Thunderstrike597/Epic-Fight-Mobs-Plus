package net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next;

import doggytalents.DoggyEntityTypes;
import doggytalents.DoggyTalentsNext;
import net.kenji.epic_fight_mobs_plus.api.MobPatchFactory;
import net.kenji.epic_fight_mobs_plus.events.MobPatchEvents;
import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusArmatures;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jline.utils.Log;
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
