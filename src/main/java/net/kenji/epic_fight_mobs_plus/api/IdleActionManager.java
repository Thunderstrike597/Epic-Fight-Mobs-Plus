package net.kenji.epic_fight_mobs_plus.api;

import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.kenji.epic_fight_mobs_plus.api.animation_types.IdleActionAnimation;
import net.kenji.epic_fight_mobs_plus.api.interfaces.IAnimalMobPatch;
import net.kenji.epic_fight_mobs_plus.network.ClientIdleActionSyncPacket;
import net.kenji.epic_fight_mobs_plus.network.MobsPlusPacketHandler;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.*;

@Mod.EventBusSubscriber(modid = EpicFightMobsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class IdleActionManager {
    // Single state object per entity — cleaned up on death/unload
    private static final Map<UUID, IdleActionState> stateMap = new HashMap<>();

    public static class IdleActionState {
        public int ticksUntilNextAction = -1; // -1 = needs initialization
        public boolean animationPlaying = false;
        public float initialRotation = -1;
        public Map<IdleActionAnimation, Integer> cooldowns = new HashMap<>(); // remaining cooldown per animation
    }

    public static IdleActionState getIdleActionState(UUID Uuid){
        return stateMap.computeIfAbsent(Uuid, k -> new IdleActionState());

    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        event.getEntity().getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
            if (cap instanceof LivingEntityPatch<?> livingEntityPatch &&
                    livingEntityPatch instanceof IAnimalMobPatch patchInterface) {

                UUID id = patchInterface.getEntityPatch().getOriginal().getUUID();
                IdleActionState state = getIdleActionState(id);

                // Only tick cooldowns when no animation is playing
                if (!state.animationPlaying) {
                    tickCooldowns(state);
                }
                if (livingEntityPatch.getCurrentLivingMotion() == LivingMotions.IDLE) {
                    if (!state.animationPlaying) {
                        handleIdleActionAssign(patchInterface, state);
                    } else {
                        handleAnimationPlay(patchInterface, state);
                    }
                }  else {
                    if (state.animationPlaying) {
                        AssetAccessor<? extends IdleActionAnimation> queued = patchInterface.getQuedIdleAction();
                        clearIdleActionState(queued, patchInterface, state);
                        // Send null to tell clients to clear
                        MobsPlusPacketHandler.sendToAll(new ClientIdleActionSyncPacket(
                                livingEntityPatch.getOriginal().getId(), null));
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        stateMap.remove(event.getEntity().getUUID());
    }

    // You'll also want to hook LivingEntityUntrackEvent or similar for unloads

    private static void tickCooldowns(IdleActionState state) {
        state.cooldowns.replaceAll((anim, ticks) -> ticks - 1);
        state.cooldowns.entrySet().removeIf(e -> e.getValue() <= 0);
    }

    private static void handleIdleActionAssign(IAnimalMobPatch patchInterface, IdleActionState state) {
        List<AnimationManager.AnimationAccessor<? extends IdleActionAnimation>> animations = patchInterface.getIdleActionAnimations();
        if (animations.isEmpty()) return;

        // Filter to only IdleActionAnimation instances (ignore plain StaticAnimations)
        List<IdleActionAnimation> idleAnims = animations.stream()
                .map(AssetAccessor::get) // however you resolve the accessor
                .filter(a -> a instanceof IdleActionAnimation)
                .map(a -> (IdleActionAnimation) a)
                .filter(a -> !state.cooldowns.containsKey(a)) // skip animations on cooldown
                .toList();

        if (idleAnims.isEmpty()) return;

        if (state.ticksUntilNextAction < 0) {
            // Pick wait time from the highest-priority eligible animation
            IdleActionAnimation representative = highestPriority(idleAnims);
            RandomSource random = patchInterface.getEntityPatch().getOriginal().getRandom();
            state.ticksUntilNextAction = (int)Mth.randomBetween(random,
                    representative.getMinWaitTicks(), representative.getMaxWaitTicks());
            return;
        }

        if (state.ticksUntilNextAction > 0) {
            state.ticksUntilNextAction--;
            return;
        }

        // Time to pick — filter to highest priority tier only, then weighted random
        int maxPriority = idleAnims.stream().mapToInt(IdleActionAnimation::getPlayPriority).max().orElse(0);
        List<IdleActionAnimation> candidates = idleAnims.stream()
                .filter(a -> a.getPlayPriority() == maxPriority)
                .toList();
        IdleActionAnimation chosen = weightedRandom(candidates, patchInterface.getEntityPatch().getOriginal().getRandom());
        patchInterface.queIdleAction(chosen.getAccessor());
        patchInterface.getEntityPatch().getServerAnimator().playAnimation(chosen.getAccessor(), chosen.getTransitionTime());

// Sync to client
        MobsPlusPacketHandler.sendToAll(new ClientIdleActionSyncPacket(
                patchInterface.getEntityPatch().getOriginal().getId(),
                chosen.getLocation()
        ));

        state.animationPlaying = true;
        state.ticksUntilNextAction = -1;
        state.initialRotation = patchInterface.getEntityPatch().getOriginal().getYRot();
    }

    private static void handleAnimationPlay(IAnimalMobPatch patchInterface, IdleActionState state) {
        LivingEntity entity = patchInterface.getEntityPatch().getOriginal();

        if (entity instanceof Mob mob) {
            // Disable AI so goals don't fight the freeze
            mob.setNoAi(true);
        }


        entity.setDeltaMovement(new Vec3(0, entity.getDeltaMovement().y, 0));
        entity.zza = 0;
        entity.xxa = 0; // strafe input too
    }

    public static void clearIdleActionState(AssetAccessor<? extends IdleActionAnimation> idleAnim, IAnimalMobPatch patchInterface, IdleActionState state) {
        LivingEntity entity = patchInterface.getEntityPatch().getOriginal();

        if (!state.animationPlaying) return;


        if (entity instanceof Mob mob) {
            mob.setNoAi(false);
            mob.getNavigation().stop();
            mob.getNavigation().recomputePath();
        }

        if (idleAnim != null) {
            state.cooldowns.forEach((k, cooldown) -> state.cooldowns.put(k, k.getCooldownTicks()));
        }
        patchInterface.queIdleAction(null);
        state.animationPlaying = false;
        state.initialRotation = -1;
        state.ticksUntilNextAction = -1;
    }

    private static IdleActionAnimation highestPriority(List<IdleActionAnimation> anims) {
        return anims.stream().max(Comparator.comparingInt(IdleActionAnimation::getPlayPriority)).orElseThrow();
    }

    private static IdleActionAnimation weightedRandom(List<IdleActionAnimation> candidates, RandomSource random) {
        float totalWeight = (float) candidates.stream().mapToDouble(IdleActionAnimation::getWeight).sum();
        float roll = random.nextFloat() * totalWeight;
        for (IdleActionAnimation anim : candidates) {
            roll -= anim.getWeight();
            if (roll <= 0) return anim;
        }
        return candidates.get(candidates.size() - 1); // fallback
    }
}
