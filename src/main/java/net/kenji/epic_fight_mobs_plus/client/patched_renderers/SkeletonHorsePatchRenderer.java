package net.kenji.epic_fight_mobs_plus.client.patched_renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kenji.epic_fight_mobs_plus.client.MobsPlusMeshes;
import net.kenji.epic_fight_mobs_plus.client.patched_renderers.p_renderers.PHorseRenderer;
import net.kenji.epic_fight_mobs_plus.client.patched_renderers.p_renderers.PSkeletonHorseRenderer;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.HorsePatch;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.SkeletonHorsePatch;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;

public class SkeletonHorsePatchRenderer extends PSkeletonHorseRenderer {
    public SkeletonHorsePatchRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(MobsPlusMeshes.HORSE, context, entityType);
    }

}