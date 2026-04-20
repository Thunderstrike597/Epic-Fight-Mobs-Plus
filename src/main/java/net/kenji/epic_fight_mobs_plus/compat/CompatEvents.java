package net.kenji.epic_fight_mobs_plus.compat;

import net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.DoggyTalentsEvents;
import net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.client.DoggyTalentsClientEvents;
import net.kenji.epic_fight_mobs_plus.events.MobPatchEvents;
import net.kenji.epic_fight_mobs_plus.events.client_events.EpicFightClientEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.IModBusEvent;
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
