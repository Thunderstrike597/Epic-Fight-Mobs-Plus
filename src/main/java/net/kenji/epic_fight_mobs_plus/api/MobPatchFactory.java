package net.kenji.epic_fight_mobs_plus.api;

import net.kenji.epic_fight_mobs_plus.client.patched_renderers.HorsePatchRenderer;
import net.kenji.epic_fight_mobs_plus.client.patched_renderers.WolfPatchRenderer;
import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusArmatures;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.HorsePatch;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.WolfPatch;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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
        public final Function<Entity, Supplier<EntityPatch<?>>> mobPatch;

        MobPatchDefinitions(
                EntityType<?> entityType,
                Armatures.ArmatureAccessor<?> accessor,
                Function<Entity, Supplier<EntityPatch<?>>> patch,
                RendererFactory rendererFactory
        ){
            this.entityType = entityType;
            this.armature = accessor;
            this.mobPatch = patch;
            this.rendererFactory = rendererFactory;
        }
    }
    public static final List<MobPatchDefinitions> mobPatches = new ArrayList<>();

    static {
        mobPatches.add(new MobPatchDefinitions(
                EntityType.WOLF,
                MobsPlusArmatures.WOLF,
                (e) -> WolfPatch::new,
                (context, type) -> new WolfPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                EntityType.HORSE,
                MobsPlusArmatures.HORSE,
                (e) -> HorsePatch::new,
                (context, type) -> new HorsePatchRenderer((EntityRendererProvider.Context) context, type)
        ));
    }
    @FunctionalInterface
    public interface RendererFactory {
        PatchedLivingEntityRenderer create(Object context, EntityType<?> type);
    }
}
