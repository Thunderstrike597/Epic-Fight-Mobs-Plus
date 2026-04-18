package net.kenji.epic_fight_mobs_plus.client.patched_renderers.p_renderers;

import net.kenji.epic_fight_mobs_plus.client.MobsPlusMeshes;
import net.kenji.epic_fight_mobs_plus.client.layers.PatchedWolfCollarLayer;
import net.kenji.epic_fight_mobs_plus.client.meshes.MobsPlusMesh;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.WolfPatch;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;

public class PWolfRenderer extends PatchedLivingEntityRenderer<Wolf, WolfPatch<Wolf>, WolfModel<Wolf>, WolfRenderer, MobsPlusMesh> {
    private static final ResourceLocation WOLF_LOCATION = new ResourceLocation("textures/entity/wolf/wolf.png");
    private static final ResourceLocation WOLF_TAME_LOCATION = new ResourceLocation("textures/entity/wolf/wolf_tame.png");

    public PWolfRenderer(Meshes.MeshAccessor<MobsPlusMesh> wolf, EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(context, entityType);
        this.addCustomLayer(new PatchedWolfCollarLayer<>(MobsPlusMeshes.WOLF)); // ← not addPatchedLayer
    }




    public AssetAccessor<MobsPlusMesh> getDefaultMesh() {
        return MobsPlusMeshes.WOLF;
    }
}
