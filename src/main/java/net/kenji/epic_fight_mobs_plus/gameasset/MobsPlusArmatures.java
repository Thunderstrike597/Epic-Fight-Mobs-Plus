package net.kenji.epic_fight_mobs_plus.gameasset;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.kenji.epic_fight_mobs_plus.gameasset.armatures.CatArmature;
import net.kenji.epic_fight_mobs_plus.gameasset.armatures.FoxArmature;
import net.kenji.epic_fight_mobs_plus.gameasset.armatures.HorseArmature;
import net.kenji.epic_fight_mobs_plus.gameasset.armatures.WolfArmature;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EntityType;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.asset.JsonAssetLoader;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.client.renderer.patched.entity.PatchedEntityRenderer;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class MobsPlusArmatures {

    public static final Armatures.ArmatureAccessor<WolfArmature> WOLF = Armatures.ArmatureAccessor.<WolfArmature>create(EpicFightMobsPlus.MODID, "entity/wolf", WolfArmature::new);
    public static final Armatures.ArmatureAccessor<HorseArmature> HORSE = Armatures.ArmatureAccessor.<HorseArmature>create(EpicFightMobsPlus.MODID, "entity/horse", HorseArmature::new);
    public static final Armatures.ArmatureAccessor<CatArmature> CAT = Armatures.ArmatureAccessor.<CatArmature>create(EpicFightMobsPlus.MODID, "entity/cat", CatArmature::new);
    public static final Armatures.ArmatureAccessor<FoxArmature> FOX = Armatures.ArmatureAccessor.<FoxArmature>create(EpicFightMobsPlus.MODID, "entity/fox", FoxArmature::new);

}
