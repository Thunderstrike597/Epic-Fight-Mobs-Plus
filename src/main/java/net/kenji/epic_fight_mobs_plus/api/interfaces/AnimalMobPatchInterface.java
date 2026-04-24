package net.kenji.epic_fight_mobs_plus.api.interfaces;

import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.List;

public interface AnimalMobPatchInterface {

    boolean shouldRun();
    boolean shouldRunWithAnim();
    float getWalkSpeed();
    void setShouldRun(boolean value);
    boolean shouldInterceptAi();
    List<AnimationManager.AnimationAccessor<? extends StaticAnimation>> getIdleActionAnimations();
    void queIdleAction(AnimationManager.AnimationAccessor<? extends StaticAnimation> idleAction);
    boolean isIdleActionPlaying();
    AnimationManager.AnimationAccessor<? extends StaticAnimation> getQuedIdleAction();
    LivingEntityPatch<?> getEntityPatch();
}
