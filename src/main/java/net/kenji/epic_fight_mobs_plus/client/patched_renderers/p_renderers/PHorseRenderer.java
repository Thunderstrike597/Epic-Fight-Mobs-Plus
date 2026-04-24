package net.kenji.epic_fight_mobs_plus.client.patched_renderers.p_renderers;

import net.kenji.epic_fight_mobs_plus.client.MobsPlusMeshes;
import net.kenji.epic_fight_mobs_plus.client.layers.PatchedHorseArmorLayer;
import net.kenji.epic_fight_mobs_plus.client.layers.PatchedHorseSaddleLayer;
import net.kenji.epic_fight_mobs_plus.client.layers.PatchedWolfCollarLayer;
import net.kenji.epic_fight_mobs_plus.client.meshes.MobsPlusMesh;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.HorsePatch;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Horse;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.renderer.patched.entity.PZombieVillagerRenderer;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;

public class PHorseRenderer extends PatchedLivingEntityRenderer<Horse, HorsePatch<Horse>, HorseModel<Horse>, HorseRenderer, MobsPlusMesh> {
    public PHorseRenderer(Meshes.MeshAccessor<MobsPlusMesh> horse, EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(context, entityType);
        this.addCustomLayer(new PatchedHorseSaddleLayer<>(MobsPlusMeshes.HORSE));
        this.addCustomLayer(new PatchedHorseArmorLayer<>(MobsPlusMeshes.HORSE));
    }


    public AssetAccessor<MobsPlusMesh> getDefaultMesh() {
        return MobsPlusMeshes.HORSE;
    }
}
