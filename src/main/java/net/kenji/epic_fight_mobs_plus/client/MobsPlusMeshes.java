package net.kenji.epic_fight_mobs_plus.client;

import com.google.common.collect.Maps;
import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.kenji.epic_fight_mobs_plus.client.meshes.MobsPlusMesh;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.Meshes;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class MobsPlusMeshes implements PreparableReloadListener {
    public static final Meshes.MeshAccessor<MobsPlusMesh> WOLF = Meshes.MeshAccessor.<MobsPlusMesh>create(EpicFightMobsPlus.MODID, "entity/wolf", (jsonModelLoader) -> (MobsPlusMesh)jsonModelLoader.loadSkinnedMesh(MobsPlusMesh::new));
    public static final Meshes.MeshAccessor<MobsPlusMesh> HORSE = Meshes.MeshAccessor.<MobsPlusMesh>create(EpicFightMobsPlus.MODID, "entity/horse", (jsonModelLoader) -> (MobsPlusMesh)jsonModelLoader.loadSkinnedMesh(MobsPlusMesh::new));
    public static final Meshes.MeshAccessor<MobsPlusMesh> CAT = Meshes.MeshAccessor.<MobsPlusMesh>create(EpicFightMobsPlus.MODID, "entity/cat", (jsonModelLoader) -> (MobsPlusMesh)jsonModelLoader.loadSkinnedMesh(MobsPlusMesh::new));

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller1, Executor executor, Executor executor1) {
        return null;
    }
}
