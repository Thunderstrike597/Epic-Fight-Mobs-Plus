package net.kenji.epic_fight_mobs_plus.client.patched_renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kenji.epic_fight_mobs_plus.client.MobsPlusMeshes;
import net.kenji.epic_fight_mobs_plus.client.patched_renderers.p_renderers.PHorseRenderer;
import net.kenji.epic_fight_mobs_plus.client.patched_renderers.p_renderers.PWolfRenderer;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.HorsePatch;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.WolfPatch;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Horse;
import yesman.epicfight.api.utils.math.OpenMatrix4f;

public class HorsePatchRenderer extends PHorseRenderer {
    public HorsePatchRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(MobsPlusMeshes.HORSE, context, entityType);
    }

    @Override
    public void render(Horse entity, HorsePatch<Horse> entitypatch, HorseRenderer renderer, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks) {
        super.render(entity, entitypatch, renderer, buffer, poseStack, packedLight, partialTicks);
    }
}