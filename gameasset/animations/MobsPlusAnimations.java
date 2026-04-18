package net.kenji.epic_fight_mobs_plus.gameasset.animations;

import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusArmatures;
import net.kenji.epic_fight_mobs_plus.gameasset.armatures.WolfArmature;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.model.armature.SpiderArmature;

@Mod.EventBusSubscriber(modid = EpicFightMobsPlus.MODID)
public class MobsPlusAnimations {
    @SubscribeEvent
    public static void registerAnimations(AnimationManager.AnimationRegistryEvent event) {
        event.newBuilder(EpicFightMobsPlus.MODID, MobsPlusAnimations::build);    }

    public static AnimationManager.AnimationAccessor<StaticAnimation> WOLF_TEST_ANIM;
    public static AnimationManager.AnimationAccessor<StaticAnimation> WOLF_IDLE;
    public static AnimationManager.AnimationAccessor<StaticAnimation> WOLF_WALK;
    public static AnimationManager.AnimationAccessor<StaticAnimation> WOLF_RUN;

    public static AnimationManager.AnimationAccessor<AttackAnimation> WOLF_ATTACK_1;

    public static AnimationManager.AnimationAccessor<StaticAnimation> WOLF_SITTING;


    private static void build(AnimationManager.AnimationBuilder builder){
        WOLF_TEST_ANIM = builder.nextAccessor("wolf/wolf_test_anim", (accessor -> new StaticAnimation(0.2F,true, accessor, MobsPlusArmatures.WOLF)));
        WOLF_IDLE = builder.nextAccessor("wolf/living/wolf_idle", (accessor -> new StaticAnimation(0.2F, true, accessor, MobsPlusArmatures.WOLF)));
        WOLF_WALK = builder.nextAccessor("wolf/living/wolf_walk", (accessor -> new StaticAnimation(0.2F,true, accessor, MobsPlusArmatures.WOLF)));
        WOLF_RUN = builder.nextAccessor("wolf/living/wolf_run", (accessor -> new StaticAnimation(0.2F,true, accessor, MobsPlusArmatures.WOLF)));

        WOLF_ATTACK_1 = builder.nextAccessor("wolf/combat/wolf_attack_1", (accessor) -> new AttackAnimation(0.15F, 0.22F, 0.30F, 0.38F, 0.44F, ColliderPreset.FIST, ((WolfArmature) MobsPlusArmatures.WOLF.get()).head, accessor, MobsPlusArmatures.WOLF));
        WOLF_SITTING = builder.nextAccessor("wolf/living/wolf_sit", (accessor -> new StaticAnimation(0.2F,true, accessor, MobsPlusArmatures.WOLF)));

    }


}

