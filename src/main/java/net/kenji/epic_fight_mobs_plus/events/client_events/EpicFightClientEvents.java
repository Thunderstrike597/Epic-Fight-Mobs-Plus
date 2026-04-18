package net.kenji.epic_fight_mobs_plus.events.client_events;

import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.kenji.epic_fight_mobs_plus.client.patched_renderers.WolfPatchRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jline.utils.Log;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;

@Mod.EventBusSubscriber(modid = EpicFightMobsPlus.MODID, value = Dist.CLIENT)
public class EpicFightClientEvents {
    @SubscribeEvent
    public static void registerPatchedEntityRenderers(PatchedRenderersEvent.Add event) {
        event.addPatchedEntityRenderer(EntityType.WOLF, entityType -> new WolfPatchRenderer(
                        event.getContext(),
                        entityType
                )
        );
    }
}