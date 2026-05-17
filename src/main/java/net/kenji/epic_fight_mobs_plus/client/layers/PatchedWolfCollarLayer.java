package net.kenji.epic_fight_mobs_plus.client.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.WolfPatch;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.layer.ModelRenderLayer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class PatchedWolfCollarLayer<AM extends SkinnedMesh> extends ModelRenderLayer<Wolf, WolfPatch<Wolf>, WolfModel<Wolf>, WolfCollarLayer, AM> {
    private static final ResourceLocation WOLF_COLLAR_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/wolf/wolf_collar.png");

    public PatchedWolfCollarLayer(AssetAccessor<AM> mesh) {
        super(mesh);
    }

    @Override
    protected void renderLayer(WolfPatch<Wolf> entitypatch, Wolf entityliving, WolfCollarLayer vanillaLayer, PoseStack poseStack, MultiBufferSource buffer, int packedLightIn, OpenMatrix4f[] poses, float bob, float yRot, float xRot, float partialTicks) {
        if (!entityliving.isTame() || entityliving.isInvisible()) return;

        int color = entityliving.getCollarColor().getTextureDiffuseColor();
        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8)  & 0xFF) / 255.0F;
        float b = ( color        & 0xFF) / 255.0F;

        ((SkinnedMesh) this.mesh.get()).draw(
                poseStack, buffer,
                RenderType.entityCutoutNoCull(WOLF_COLLAR_LOCATION),
                packedLightIn,
                r, g, b, 1.0F,
                OverlayTexture.NO_OVERLAY,
                entitypatch.getArmature(), poses
        );
    }
}

