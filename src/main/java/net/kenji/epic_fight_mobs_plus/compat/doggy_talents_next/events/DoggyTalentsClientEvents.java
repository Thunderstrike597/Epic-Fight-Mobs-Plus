package net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.events;

import doggytalents.DoggyEntityTypes;
import net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.patched_renderers.DogPatchRenderer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;

public class DoggyTalentsClientEvents {

    @SubscribeEvent
    public static void registerPatchedEntityRenderers(PatchedRenderersEvent.Add event) {

        event.addPatchedEntityRenderer(
               DoggyEntityTypes.DOG.get(),
                entityType -> new DogPatchRenderer(event.getContext(), entityType)
        );
    }
}
