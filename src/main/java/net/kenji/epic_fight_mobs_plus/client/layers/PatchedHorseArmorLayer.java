package net.kenji.epic_fight_mobs_plus.client.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.HorsePatch;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.DyeableHorseArmorItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.layer.ModelRenderLayer;

public class PatchedHorseArmorLayer<AM extends SkinnedMesh> extends ModelRenderLayer<Horse, HorsePatch<Horse>, HorseModel<Horse>, HorseArmorLayer, AM> {

    public PatchedHorseArmorLayer(AssetAccessor<AM> mesh) {
        super(mesh);
    }

    @Override
    protected void renderLayer(HorsePatch<Horse> entitypatch, Horse entityliving, HorseArmorLayer vanillaLayer, PoseStack poseStack, MultiBufferSource buffer, int packedLightIn, OpenMatrix4f[] poses, float bob, float yRot, float xRot, float partialTicks) {
        ItemStack armorStack = entityliving.getArmor();
        if (!(armorStack.getItem() instanceof HorseArmorItem horsearmoritem)) return;

        float r, g, b;
        if (horsearmoritem instanceof DyeableHorseArmorItem dyeable) {
            int color = dyeable.getColor(armorStack);
            r = (float)(color >> 16 & 255) / 255.0F;
            g = (float)(color >> 8  & 255) / 255.0F;
            b = (float)(color       & 255) / 255.0F;
        } else {
            r = g = b = 1.0F;
        }

        ((SkinnedMesh) this.mesh.get()).draw(
                poseStack, buffer,
                RenderType.entityCutoutNoCull(horsearmoritem.getTexture()),
                packedLightIn,
                r, g, b, 1.0F,
                OverlayTexture.NO_OVERLAY,
                entitypatch.getArmature(), poses
        );
    }
}