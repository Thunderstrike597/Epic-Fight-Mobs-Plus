package net.kenji.epic_fight_mobs_plus.client.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.HorsePatch;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Horse;
import org.jline.utils.Log;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.layer.ModelRenderLayer;

public class PatchedHorseSaddleLayer<AM extends SkinnedMesh> extends ModelRenderLayer<Horse, HorsePatch<Horse>, HorseModel<Horse>, HorseArmorLayer, AM> {
    private static final ResourceLocation SADDLE_TEXTURE =
            new ResourceLocation(EpicFightMobsPlus.MODID, "textures/entity/horse/horse_saddle.png");

    public PatchedHorseSaddleLayer(AssetAccessor<AM> mesh) {
        super(mesh);
    }

    @Override
    protected void renderLayer(HorsePatch<Horse> entitypatch, Horse entityliving, HorseArmorLayer vanillaLayer, PoseStack poseStack, MultiBufferSource buffer, int packedLightIn, OpenMatrix4f[] poses, float bob, float yRot, float xRot, float partialTicks) {
        if (!entityliving.isSaddled() || entityliving.isInvisible()) return;

        ((SkinnedMesh) this.mesh.get()).draw(
                poseStack, buffer,
                RenderType.entityCutoutNoCull(SADDLE_TEXTURE),
                packedLightIn,
                1.0F, 1.0F, 1.0F, 1.0F,
                OverlayTexture.NO_OVERLAY,
                entitypatch.getArmature(), poses
        );
    }
}