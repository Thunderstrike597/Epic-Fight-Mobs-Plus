package net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.client;

import doggytalents.DoggyEntityTypes;
import net.kenji.epic_fight_mobs_plus.client.patched_renderers.CatPatchRenderer;
import net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.client.patched_renderers.DogPatchRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jline.utils.Log;
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
