package net.kenji.epic_fight_mobs_plus.client.patched_renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kenji.epic_fight_mobs_plus.client.MobsPlusMeshes;
import net.kenji.epic_fight_mobs_plus.client.patched_renderers.p_renderers.PHorseRenderer;
import net.kenji.epic_fight_mobs_plus.client.patched_renderers.p_renderers.PWolfRenderer;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.WolfPatch;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import yesman.epicfight.api.utils.math.OpenMatrix4f;

public class HorsePatchRenderer extends PHorseRenderer {
    public HorsePatchRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(MobsPlusMeshes.HORSE, context, entityType);
    }

}