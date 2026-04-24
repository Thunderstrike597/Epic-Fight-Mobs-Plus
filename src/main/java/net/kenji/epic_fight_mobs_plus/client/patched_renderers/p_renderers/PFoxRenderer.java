package net.kenji.epic_fight_mobs_plus.client.patched_renderers.p_renderers;

import net.kenji.epic_fight_mobs_plus.client.MobsPlusMeshes;
import net.kenji.epic_fight_mobs_plus.client.layers.PatchedWolfCollarLayer;
import net.kenji.epic_fight_mobs_plus.client.meshes.MobsPlusMesh;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.FoxPatch;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.WolfPatch;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FoxRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Wolf;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;

public class PFoxRenderer extends PatchedLivingEntityRenderer<Fox, FoxPatch<Fox>, FoxModel<Fox>, FoxRenderer, MobsPlusMesh> {

    public PFoxRenderer(Meshes.MeshAccessor<MobsPlusMesh> wolf, EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(context, entityType);
    }



    public AssetAccessor<MobsPlusMesh> getDefaultMesh() {
        return MobsPlusMeshes.FOX;
    }
}
