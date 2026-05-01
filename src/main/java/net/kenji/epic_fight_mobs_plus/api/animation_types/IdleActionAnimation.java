package net.kenji.epic_fight_mobs_plus.api.animation_types;

import doggytalents.common.network.PacketHandler;
import net.kenji.epic_fight_mobs_plus.api.IdleActionManager;
import net.kenji.epic_fight_mobs_plus.api.interfaces.IAnimalMobPatch;
import net.kenji.epic_fight_mobs_plus.network.ClientIdleActionSyncPacket;
import net.kenji.epic_fight_mobs_plus.network.MobsPlusPacketHandler;
import net.kenji.epic_fight_mobs_plus.network.ServerIdleActionPacket;
import org.jline.utils.Log;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class IdleActionAnimation extends StaticAnimation {

    private final int playPriority;
    private final int minWaitTicks;
    private final int maxWaitTicks;
    private final float weight;          // probability weight for weighted random selection
    private final int cooldownTicks;     // per-animation cooldown after it plays
    //private final boolean canInterrupt;  // can this interrupt a currently playing idle action?

    public IdleActionAnimation(int weight, double cooldownTime, int playPriority, double minWaitTime, double maxWaitTime, float transitionTime,boolean isRepeat, AnimationManager.AnimationAccessor<? extends StaticAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        super(transitionTime, isRepeat, accessor, armature);
        this.playPriority = playPriority;
        this.minWaitTicks = Math.round((float) minWaitTime * 20);
        this.maxWaitTicks = Math.round((float) maxWaitTime * 20);
        this.weight = weight;
        this.cooldownTicks = Math.round((float) cooldownTime * 20);
    }

    public int getPlayPriority(){
        return playPriority;
    }

    public int getMinWaitTicks() {
        return minWaitTicks;
    }

    public int getMaxWaitTicks() {
        return maxWaitTicks;
    }

    public float getWeight() {
        return weight;
    }

    public int getCooldownTicks() {
        return cooldownTicks;
    }

    @Override
    public void end(LivingEntityPatch<?> entitypatch, AssetAccessor<? extends DynamicAnimation> nextAnimation, boolean isEnd) {
        super.end(entitypatch, nextAnimation, isEnd);
        if (!isEnd) return;


        if (entitypatch instanceof IAnimalMobPatch patchInterface) {
            if (patchInterface.getQuedIdleAction() != null && this == patchInterface.getQuedIdleAction().get()) {
                IdleActionManager.clearIdleActionState(
                        this.getAccessor(),
                        patchInterface,
                        IdleActionManager.getIdleActionState(entitypatch.getOriginal().getUUID())
                );        if (entitypatch.getOriginal().level().isClientSide()) return; // server handles state

            }
        }
    }
}
