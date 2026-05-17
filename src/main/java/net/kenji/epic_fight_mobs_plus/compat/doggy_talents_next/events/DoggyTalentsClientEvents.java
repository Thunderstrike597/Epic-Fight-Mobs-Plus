package net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.events;

import doggytalents.DoggyEntityTypes;
import net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.patched_renderers.DogPatchRenderer;
import yesman.epicfight.api.client.event.types.registry.RegisterPatchedRenderersEvent;

public class DoggyTalentsClientEvents {

    public static void registerPatchedEntityRenderers(RegisterPatchedRenderersEvent.AddEntity event) {

        event.addPatchedEntityRenderer(
               DoggyEntityTypes.DOG.get(),
                entityType -> new DogPatchRenderer(event.getContext(), entityType)
        );
    }
}
