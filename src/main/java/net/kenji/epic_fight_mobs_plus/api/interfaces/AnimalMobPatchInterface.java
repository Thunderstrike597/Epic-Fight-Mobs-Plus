package net.kenji.epic_fight_mobs_plus.api.interfaces;

import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.List;

public interface AnimalMobPatchInterface {

    LivingMotion getOptionalLivingMotion();
    void setOptionalLivingMotion(int motionId);
    boolean shouldRun();
    boolean shouldRunWithAnim();
    float getWalkSpeed();
    double getCurrentForwardSpeed();
    void setShouldRun(boolean value);
    boolean shouldInterceptAi();
    List<AnimationManager.AnimationAccessor<? extends StaticAnimation>> getIdleActionAnimations();
    void queIdleAction(AnimationManager.AnimationAccessor<? extends StaticAnimation> idleAction);
    boolean isIdleActionPlaying();
    int getMinIdleActionInterval();
    int getMaxIdleActionInterval();
    AnimationManager.AnimationAccessor<? extends StaticAnimation> getQuedIdleAction();
    LivingEntityPatch<?> getEntityPatch();
}
