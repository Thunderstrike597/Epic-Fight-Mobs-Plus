package net.kenji.epic_fight_mobs_plus.events.client_events;

import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.kenji.epic_fight_mobs_plus.api.MobPatchFactory;
import net.kenji.epic_fight_mobs_plus.client.patched_renderers.WolfPatchRenderer;
import net.minecraft.world.entity.EntityType;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import org.jline.utils.Log;
import yesman.epicfight.EpicFightClient;
import yesman.epicfight.api.client.event.types.registry.RegisterPatchedRenderersEvent;

public class EpicFightClientEvents {


    public static void registerPatchedEntityRenderers(RegisterPatchedRenderersEvent.AddEntity event) {
        for (MobPatchFactory.MobPatchDefinitions def : MobPatchFactory.mobPatches) {
            event.addPatchedEntityRenderer(
                    def.entityType,
                    entityType -> def.rendererFactory.create(event.getContext(), entityType)
            );
        }
    }
}