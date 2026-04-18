package net.kenji.epic_fight_mobs_plus.gameasset.animations;

import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusArmatures;
import net.kenji.epic_fight_mobs_plus.gameasset.armatures.WolfArmature;
import net.kenji.epic_fight_mobs_plus.gameasset.mob_patches.HorsePatch;
import net.kenji.epic_fight_mobs_plus.mixins.AbstractHorseAccessor;
import net.kenji.epic_fight_mobs_plus.mixins.EntityAccessor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jline.utils.Log;
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

    public static AnimationManager.AnimationAccessor<StaticAnimation> HORSE_IDLE;
    public static AnimationManager.AnimationAccessor<StaticAnimation> HORSE_WALK;
    public static AnimationManager.AnimationAccessor<StaticAnimation> HORSE_RUN;
    public static AnimationManager.AnimationAccessor<StaticAnimation> HORSE_JUMP;


    private static void build(AnimationManager.AnimationBuilder builder){
        WOLF_TEST_ANIM = builder.nextAccessor("wolf/wolf_test_anim", (accessor -> new StaticAnimation(0.2F,true, accessor, MobsPlusArmatures.WOLF)));
        WOLF_IDLE = builder.nextAccessor("wolf/living/wolf_idle", (accessor -> new StaticAnimation(0.2F, true, accessor, MobsPlusArmatures.WOLF)));
        WOLF_WALK = builder.nextAccessor("wolf/living/wolf_walk", (accessor -> new StaticAnimation(0.2F,true, accessor, MobsPlusArmatures.WOLF)));
        WOLF_RUN = builder.nextAccessor("wolf/living/wolf_run", (accessor -> new StaticAnimation(0.2F,true, accessor, MobsPlusArmatures.WOLF)));

        WOLF_ATTACK_1 = builder.nextAccessor("wolf/combat/wolf_attack_1", (accessor) -> new AttackAnimation(0.15F, 0.22F, 0.30F, 0.38F, 0.44F, ColliderPreset.FIST, ((WolfArmature) MobsPlusArmatures.WOLF.get()).head, accessor, MobsPlusArmatures.WOLF));
        WOLF_SITTING = builder.nextAccessor("wolf/living/wolf_sit", (accessor -> new StaticAnimation(0.2F,true, accessor, MobsPlusArmatures.WOLF)));


        HORSE_IDLE = builder.nextAccessor("horse/living/horse_idle", (accessor -> new StaticAnimation(0.2F, true, accessor, MobsPlusArmatures.HORSE)));
        HORSE_WALK = builder.nextAccessor("horse/living/horse_walk", (accessor -> new StaticAnimation(0.2F,true, accessor, MobsPlusArmatures.HORSE)));
        HORSE_RUN = builder.nextAccessor("horse/living/horse_run", (accessor -> new StaticAnimation(0.2F,true, accessor, MobsPlusArmatures.HORSE)));
        HORSE_JUMP = builder.nextAccessor("horse/living/horse_jump", (accessor -> new StaticAnimation(0.05F,false, accessor, MobsPlusArmatures.HORSE).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> {
            if(entitypatch instanceof HorsePatch<?> horsePatch){
                if(horsePatch.getStoredJumpSpeed() != -1) return horsePatch.getStoredJumpSpeed();
                double d0 = horsePatch.getOriginal().getCustomJump() * (double)((AbstractHorseAccessor)horsePatch.getOriginal()).getPlayerJumpScale() * (double)((EntityAccessor)horsePatch.getOriginal()).invokeGetBlockJumpFactor();
                float boost = (float)( d0 + (double)horsePatch.getOriginal().getJumpBoostPower());

// normalize boost into 0 → 1 range (roughly)
                float normalized = Math.min(boost, 1.0F);

// invert it
                float multiplier = 1.0F + (1.0F - normalized);
                float finalSpeed = speed * multiplier;
                
                if(finalSpeed <= 1.8F){
                    finalSpeed = 1.0F;
                }
                if(finalSpeed >= 2.0F){
                    finalSpeed = 2.25F;
                }

                return horsePatch.setStoredJumpSpeed(finalSpeed);
            }
            return speed;
        })));
    }


}

