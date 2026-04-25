package net.kenji.epic_fight_mobs_plus.client.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.HorsePatch;
import net.minecraft.Util;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.entity.layers.HorseMarkingLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Markings;
import net.minecraft.world.item.DyeableHorseArmorItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.layer.ModelRenderLayer;

import java.util.Map;

public class PatchedHorseMarkingLayer<AM extends SkinnedMesh> extends ModelRenderLayer<Horse, HorsePatch<Horse>, HorseModel<Horse>, HorseMarkingLayer, AM> {
    private static final Map<Markings, ResourceLocation> LOCATION_BY_MARKINGS = Util.make(Maps.newEnumMap(Markings.class), (p_117069_) -> {
        p_117069_.put(Markings.NONE, (ResourceLocation)null);
        p_117069_.put(Markings.WHITE, new ResourceLocation("textures/entity/horse/horse_markings_white.png"));
        p_117069_.put(Markings.WHITE_FIELD, new ResourceLocation("textures/entity/horse/horse_markings_whitefield.png"));
        p_117069_.put(Markings.WHITE_DOTS, new ResourceLocation("textures/entity/horse/horse_markings_whitedots.png"));
        p_117069_.put(Markings.BLACK_DOTS, new ResourceLocation("textures/entity/horse/horse_markings_blackdots.png"));
    });

    public PatchedHorseMarkingLayer(AssetAccessor<AM> mesh) {
        super(mesh);
    }


    @Override
    protected void renderLayer(HorsePatch<Horse> horseHorsePatch, Horse horse, @Nullable HorseMarkingLayer horseMarkingLayer, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, OpenMatrix4f[] poses, float v, float v1, float v2, float v3) {
        ResourceLocation resourcelocation = LOCATION_BY_MARKINGS.get(horse.getMarkings());

        if(resourcelocation != null && !horse.isInvisible()) {
            ((SkinnedMesh) this.mesh.get()).draw(
                    poseStack, multiBufferSource,
                    RenderType.entityCutoutNoCull(resourcelocation),
                    packedLight,
                    1.0F, 1.0F, 1.0F, 1.0F,
                    OverlayTexture.NO_OVERLAY,
                    horseHorsePatch.getArmature(), poses
            );
        }
    }
}