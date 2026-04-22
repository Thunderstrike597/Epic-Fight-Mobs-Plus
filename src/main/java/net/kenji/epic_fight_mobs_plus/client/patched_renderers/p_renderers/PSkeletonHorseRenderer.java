package net.kenji.epic_fight_mobs_plus.client.patched_renderers.p_renderers;

import net.kenji.epic_fight_mobs_plus.client.MobsPlusMeshes;
import net.kenji.epic_fight_mobs_plus.client.layers.PatchedHorseArmorLayer;
import net.kenji.epic_fight_mobs_plus.client.layers.PatchedHorseSaddleLayer;
import net.kenji.epic_fight_mobs_plus.client.meshes.MobsPlusMesh;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.HorsePatch;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.SkeletonHorsePatch;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;

public class PSkeletonHorseRenderer extends PatchedLivingEntityRenderer<SkeletonHorse, SkeletonHorsePatch<SkeletonHorse>, HorseModel<SkeletonHorse>, AbstractHorseRenderer<SkeletonHorse, HorseModel<SkeletonHorse>>, MobsPlusMesh> {
    public PSkeletonHorseRenderer(Meshes.MeshAccessor<MobsPlusMesh> horse, EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(context, entityType);
    }
    public AssetAccessor<MobsPlusMesh> getDefaultMesh() {
        return MobsPlusMeshes.HORSE;
    }
}
