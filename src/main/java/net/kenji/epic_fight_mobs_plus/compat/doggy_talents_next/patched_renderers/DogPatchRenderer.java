package net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.patched_renderers;

import net.kenji.epic_fight_mobs_plus.client.MobsPlusMeshes;
import net.kenji.epic_fight_mobs_plus.compat.doggy_talents_next.patched_renderers.p_renderers.PDogRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;

public class DogPatchRenderer extends PDogRenderer {
    public DogPatchRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(MobsPlusMeshes.WOLF, context, entityType);
    }

}