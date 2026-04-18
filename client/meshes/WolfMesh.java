package net.kenji.epic_fight_mobs_plus.client.meshes;

import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.client.model.MeshPartDefinition;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.client.model.VertexBuilder;

import java.util.List;
import java.util.Map;

public class WolfMesh extends SkinnedMesh {
    public final SkinnedMesh.SkinnedMeshPart main;
    public WolfMesh(@Nullable Map<String, Number[]> arrayMap, @Nullable Map<MeshPartDefinition, List<VertexBuilder>> partBuilders, @Nullable SkinnedMesh parent, RenderProperties properties) {
        super(arrayMap, partBuilders, parent, properties);
        this.main = this.getOrLogException(this.parts, "noGroups");
    }
}
