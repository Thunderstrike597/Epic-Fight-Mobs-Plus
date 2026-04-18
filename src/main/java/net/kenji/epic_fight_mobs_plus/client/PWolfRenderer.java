package net.kenji.epic_fight_mobs_plus.client;

import net.kenji.epic_fight_mobs_plus.client.layers.PatchedWolfCollarLayer;
import net.kenji.epic_fight_mobs_plus.client.meshes.WolfMesh;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.WolfPatch;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.entity.EndermanRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.layers.EnderEyesLayer;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.renderer.patched.entity.PEndermanRenderer;
import yesman.epicfight.client.renderer.patched.entity.PZombieVillagerRenderer;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;

public class PWolfRenderer extends PatchedLivingEntityRenderer<Wolf, WolfPatch<Wolf>, WolfModel<Wolf>, WolfRenderer, WolfMesh> {
    private static final ResourceLocation WOLF_LOCATION = new ResourceLocation("textures/entity/wolf/wolf.png");
    private static final ResourceLocation WOLF_TAME_LOCATION = new ResourceLocation("textures/entity/wolf/wolf_tame.png");

    public PWolfRenderer(Meshes.MeshAccessor<WolfMesh> wolf, EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(context, entityType);
        this.addCustomLayer(new PatchedWolfCollarLayer<>(MobsPlusMeshes.WOLF)); // ← not addPatchedLayer
    }




    public AssetAccessor<WolfMesh> getDefaultMesh() {
        return MobsPlusMeshes.WOLF;
    }
}
