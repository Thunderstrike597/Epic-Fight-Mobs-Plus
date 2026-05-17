package net.kenji.epic_fight_mobs_plus.api;

import net.kenji.epic_fight_mobs_plus.client.patched_renderers.*;
import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusArmatures;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import yesman.epicfight.api.client.event.types.registry.RegisterPatchedRenderersEvent;
import yesman.epicfight.api.event.EventHook;
import yesman.epicfight.api.event.types.registry.EntityPatchRegistryEvent;
import yesman.epicfight.api.utils.side.LogicalSide;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class MobPatchFactory {
    public static class MobPatchDefinitions {
        public final EntityType<?> entityType;
        public final Armatures.ArmatureAccessor<?> armature;
        public final RendererFactory rendererFactory;
        public final Function<? super Entity, ? extends EntityPatch<?>> mobPatch;

        // And the constructor parameter:
        public MobPatchDefinitions(
                EntityType<?> entityType,
                Armatures.ArmatureAccessor<?> accessor,
                Function<? super Entity, ? extends EntityPatch<?>> patch,
                RendererFactory rendererFactory
        ) {
            this.entityType = entityType;
            this.armature = accessor;
            this.mobPatch = patch;
            this.rendererFactory = rendererFactory;
        }
    }
    public static void registerPatchedEntities(EntityPatchRegistryEvent event) {
        for (MobPatchFactory.MobPatchDefinitions def : MobPatchFactory.mobPatches) {
            registerUnsafe(event, def);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Entity> void registerUnsafe(
            EntityPatchRegistryEvent event,
            MobPatchFactory.MobPatchDefinitions def
    ) {
        event.registerEntityPatchUnsafe(
                (EntityType<T>) def.entityType,
                (Function<? super T, ? extends EntityPatch<? extends T>>) def.mobPatch
        );
    }
    public static final List<MobPatchDefinitions> mobPatches = new ArrayList<>();

    static {
        mobPatches.add(new MobPatchDefinitions(
                EntityType.WOLF,
                MobsPlusArmatures.WOLF,
                (e) -> new WolfPatch<>((Wolf) e),
                (context, type) -> new WolfPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                EntityType.HORSE,
                MobsPlusArmatures.HORSE,
                (e) -> new HorsePatch<>((Horse) e),
                (context, type) -> new HorsePatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                EntityType.SKELETON_HORSE,
                MobsPlusArmatures.HORSE,
                (e) -> new SkeletonHorsePatch<>((SkeletonHorse) e),
                (context, type) -> new SkeletonHorsePatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                EntityType.CAT,
                MobsPlusArmatures.CAT,
                (e) -> new CatPatch<>((Cat)e),
                (context, type) -> new CatPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                EntityType.FOX,
                MobsPlusArmatures.FOX,
                (e) -> new FoxPatch<>((Fox) e),
                (context, type) -> new FoxPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                EntityType.POLAR_BEAR,
                MobsPlusArmatures.POLAR_BEAR,
                (e) -> new PolarBearPatch<>((PolarBear) e),
                (context, type) -> new PolarBearPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
    }
    @FunctionalInterface
    public interface RendererFactory {
        PatchedLivingEntityRenderer create(Object context, EntityType<?> type);
    }
}
