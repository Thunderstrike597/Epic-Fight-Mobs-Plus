package net.kenji.epic_fight_mobs_plus.api.animation_types;

import net.kenji.epic_fight_mobs_plus.api.IdleActionManager;
import net.kenji.epic_fight_mobs_plus.api.IdleVarientManager;
import net.kenji.epic_fight_mobs_plus.api.interfaces.IAnimalMobPatch;
import org.jline.utils.Log;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class IdleVariantAnimation extends StaticAnimation {


    private final float weight;          // probability weight for weighted random selection
    private final int cooldownTicks;     // per-animation cooldown after it plays
    private final LivingMotion associatedMotion;
    public IdleVariantAnimation(int weight, double cooldownTime, LivingMotion motion, float transitionTime, boolean isRepeat, AnimationManager.AnimationAccessor<? extends StaticAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        super(transitionTime, isRepeat, accessor, armature);
        this.weight = weight;
        this.cooldownTicks = Math.round((float) cooldownTime * 20);
        this.associatedMotion = motion;
    }

    public LivingMotion getAssociatedMotion() {
        return associatedMotion;
    }


    public float getWeight() {
        return weight;
    }

    public int getCooldownTicks() {
        return cooldownTicks;
    }

    @Override
    public void tick(LivingEntityPatch<?> entitypatch) {
        super.tick(entitypatch);
        AnimationPlayer animPlayer = entitypatch.getAnimator().getPlayerFor(this.getAccessor());
        if(animPlayer == null) return;
        if (animPlayer.getElapsedTime() >= this.getTotalTime() - 0.01F) {
                IdleVarientManager.rollRandomIdleVariant(entitypatch);
        }
    }
}
