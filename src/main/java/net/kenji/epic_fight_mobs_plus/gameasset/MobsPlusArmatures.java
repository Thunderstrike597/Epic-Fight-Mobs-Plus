package net.kenji.epic_fight_mobs_plus.gameasset;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.kenji.epic_fight_mobs_plus.gameasset.armatures.WolfArmature;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EntityType;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.asset.JsonAssetLoader;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class MobsPlusArmatures {

   public static final Armatures.ArmatureAccessor<WolfArmature> WOLF = Armatures.ArmatureAccessor.<WolfArmature>create(EpicFightMobsPlus.MODID, "entity/wolf", WolfArmature::new);


}
