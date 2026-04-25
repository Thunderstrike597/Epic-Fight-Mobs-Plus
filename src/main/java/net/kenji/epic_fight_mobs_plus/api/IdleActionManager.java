package net.kenji.epic_fight_mobs_plus.api;

import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.kenji.epic_fight_mobs_plus.api.interfaces.AnimalMobPatchInterface;
import net.kenji.epic_fight_mobs_plus.gameasset.MobsPlusLivingMotions;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jline.utils.Log;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.*;

@Mod.EventBusSubscriber(modid = EpicFightMobsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class IdleActionManager {
    private static Map<UUID, Integer> entityTickWaitIdleActionMap = new HashMap<>();
    private static Map<UUID, Integer> entityIdleActionMap = new HashMap<>();
    private static Map<UUID, Boolean> entityIdlePlayedAnimMap = new HashMap<>();


    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event){
        event.getEntity().getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent((cap) ->{
            if(cap instanceof LivingEntityPatch<?> livingEntityPatch) {
                if(livingEntityPatch instanceof AnimalMobPatchInterface patchInterface) {
                    UUID entityId = patchInterface.getEntityPatch().getOriginal().getUUID();

                    handleIdleActionAssign(patchInterface);
                    if(livingEntityPatch.getCurrentLivingMotion() == LivingMotions.IDLE && patchInterface.getQuedIdleAction() != null) {
                        handleAnimationPlay(patchInterface);
                    }
                    if(entityIdlePlayedAnimMap.getOrDefault(entityId, false)){
                        if(patchInterface.getQuedIdleAction() == null){
                            entityIdlePlayedAnimMap.put(entityId, false);
                        }
                    }
                }
            }
        });
    }

    public static void handleIdleActionAssign(AnimalMobPatchInterface patchInterface){

            UUID entityId = patchInterface.getEntityPatch().getOriginal().getUUID();
            LivingEntityPatch<?> patch = patchInterface.getEntityPatch();
            List<AnimationManager.AnimationAccessor<? extends StaticAnimation>> animations = patchInterface.getIdleActionAnimations();
            int maxIndex = animations.size() - 1;
            if(maxIndex < 0)return;

            if(entityTickWaitIdleActionMap.get(entityId) == null)
                entityTickWaitIdleActionMap.put(entityId, (int)Mth.randomBetween(patchInterface.getEntityPatch().getOriginal().getRandom(), patchInterface.getMinIdleActionInterval() * 20, patchInterface.getMaxIdleActionInterval() * 20));
            else{
                int tickWait = entityTickWaitIdleActionMap.get(entityId);
                int currentTick = entityIdleActionMap.getOrDefault(entityId, tickWait);
                RandomSource random = patch.getOriginal().getRandom();
                if (currentTick > 0) {
                    entityIdleActionMap.put(entityId, currentTick - 1);
                } else {
                    int finalIndex = random.nextInt(maxIndex + 1);
                    var animation = animations.get(finalIndex);

                    patchInterface.queIdleAction(animation);

                    entityTickWaitIdleActionMap.remove(entityId);
                    entityIdleActionMap.remove(entityId);
                }
            }
        }
    public static void handleAnimationPlay(AnimalMobPatchInterface patchInterface) {
        AnimationPlayer animPlayer = Objects.requireNonNull(patchInterface.getEntityPatch().getAnimator().getPlayerFor(null));
            if (!entityIdlePlayedAnimMap.getOrDefault(patchInterface.getEntityPatch().getOriginal().getUUID(), false)) {
                patchInterface.getEntityPatch().getAnimator().playAnimation(patchInterface.getQuedIdleAction(), 0.1F);
                entityIdlePlayedAnimMap.put(patchInterface.getEntityPatch().getOriginal().getUUID(), true);
            } else {
                patchInterface.getEntityPatch().getOriginal().zza = 0;
                patchInterface.getEntityPatch().getOriginal().setDeltaMovement(new Vec3(0, patchInterface.getEntityPatch().getOriginal().getDeltaMovement().y, 0));
            }
    }
    public static void clearIdleActionState(AnimalMobPatchInterface patchInterface){
        patchInterface.queIdleAction(null);
        entityIdlePlayedAnimMap.put(patchInterface.getEntityPatch().getOriginal().getUUID(), false);
    }
}
