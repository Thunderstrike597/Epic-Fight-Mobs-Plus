package net.kenji.epic_fight_mobs_plus.events;

import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusArmatures;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.WolfPatch;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jline.utils.Log;
import yesman.epicfight.api.forgeevent.EntityPatchRegistryEvent;
import yesman.epicfight.gameasset.Armatures;

@Mod.EventBusSubscriber(modid = EpicFightMobsPlus.MODID)
public class MobPatchEvents {
    @SubscribeEvent
    public static void registerPatchedEntities(EntityPatchRegistryEvent event) {
        event.getTypeEntry().put(EntityType.WOLF, (entity) -> WolfPatch::new);

    }
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(MobPatchEvents::registerEntityTypeArmatures);
    }

    private static void registerEntityTypeArmatures() {
        Armatures.registerEntityTypeArmature(EntityType.WOLF, MobsPlusArmatures.WOLF);
    }
}