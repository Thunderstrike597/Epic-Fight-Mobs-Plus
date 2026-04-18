package net.kenji.epic_fight_mobs_plus.client;

import com.google.common.collect.Maps;
import net.kenji.epic_fight_mobs_plus.EpicFightMobsPlus;
import net.kenji.epic_fight_mobs_plus.client.meshes.WolfMesh;
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
    private static final Map<ResourceLocation, Meshes.MeshAccessor<? extends Mesh>> ACCESSORS = Maps.newHashMap();
    private static final Map<Meshes.MeshAccessor<? extends Mesh>, Mesh> MESHES = Maps.newHashMap();
    private static ResourceManager resourceManager = null;
    public static final Meshes INSTANCE = new Meshes();
    public static final Meshes.MeshAccessor<WolfMesh> WOLF = Meshes.MeshAccessor.<WolfMesh>create(EpicFightMobsPlus.MODID, "entity/wolf", (jsonModelLoader) -> (WolfMesh)jsonModelLoader.loadSkinnedMesh(WolfMesh::new));

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller1, Executor executor, Executor executor1) {
        return null;
    }
}
