package net.kenji.epic_fight_mobs_plus;

import net.minecraftforge.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CompatMixinPlugin implements IMixinConfigPlugin {

    private boolean isLoaded(String modid) {
        return FMLLoader.getLoadingModList().getModFileById(modid) != null;
    }

    @Override
    public List<String> getMixins() {
        List<String> mixins = new ArrayList<>();



        return mixins;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!isLoaded("doggytalents")) {
            if (mixinClassName.endsWith("DogAiManagerMixin")) {
                return false;
            }
            if (mixinClassName.endsWith("DogAiManagerAccessor")) {
                return false;
            }
            if (mixinClassName.endsWith("DogAccessor")) {
                return false;
            }
        }
        return true;
    }

    // unused methods:
    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> a, Set<String> b) {}
    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
}