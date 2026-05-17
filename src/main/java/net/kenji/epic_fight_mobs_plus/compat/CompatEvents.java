package net.kenji.epic_fight_mobs_plus.compat;

import net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.events.DoggyTalentsEvents;
import net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.events.DoggyTalentsClientEvents;
import net.kenji.epic_fight_mobs_plus.events.MobPatchEvents;
import net.kenji.epic_fight_mobs_plus.events.client_events.EpicFightClientEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import yesman.epicfight.api.client.event.EpicFightClientEventHooks;
import yesman.epicfight.api.event.EpicFightEventHooks;


public class CompatEvents {
    public static void OnInit(IEventBus event){
        if(ModList.get().isLoaded("doggytalents")) {
            EpicFightEventHooks.Registry.ENTITY_PATCH.registerEvent(DoggyTalentsEvents::registerPatchedEntities);
            event.addListener(DoggyTalentsEvents::commonSetup);

            if (FMLEnvironment.dist == Dist.CLIENT) {
                EpicFightClientEventHooks.Registry.ADD_PATCHED_ENTITY.registerEvent(DoggyTalentsClientEvents::registerPatchedEntityRenderers);
}
        }
    }
}
