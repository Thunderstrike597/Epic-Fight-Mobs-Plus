package net.kenji.epic_fight_mobs_plus.compat;

import net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.events.DoggyTalentsEvents;
import net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.events.DoggyTalentsClientEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class CompatEvents {
    public static void OnInit(IEventBus event){
        if(ModList.get().isLoaded("doggytalents")) {
            event.addListener(DoggyTalentsEvents::registerPatchedEntities);
            event.addListener(DoggyTalentsEvents::commonSetup);

            if (FMLEnvironment.dist == Dist.CLIENT) {
                event.addListener(DoggyTalentsClientEvents::registerPatchedEntityRenderers);        }

        }
    }
}
