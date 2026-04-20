package net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.client.patched_renderers.p_renderers;

import doggytalents.client.entity.model.dog.DogModel;
import doggytalents.client.entity.render.DogRenderer;
import doggytalents.common.entity.Dog;
import net.kenji.epic_fight_mobs_plus.client.MobsPlusMeshes;
import net.kenji.epic_fight_mobs_plus.client.layers.PatchedWolfCollarLayer;
import net.kenji.epic_fight_mobs_plus.client.meshes.MobsPlusMesh;
import net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.DogPatch;
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

public class PDogRenderer extends PatchedLivingEntityRenderer<Dog, DogPatch<Dog>, DogModel, DogRenderer, MobsPlusMesh> {

    public PDogRenderer(Meshes.MeshAccessor<MobsPlusMesh> dog, EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(context, entityType);
    }



    public AssetAccessor<MobsPlusMesh> getDefaultMesh() {
        return MobsPlusMeshes.WOLF;
    }
}
