package limonblaze.originsclasses.compat;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class OriginsClassesCompat implements IMixinConfigPlugin {
    private static boolean APOTHEOSIS;

    private static boolean checkForClass(String cls) {
        try {
            Class.forName(cls, false, OriginsClassesCompat.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    @Override
    public void onLoad(String mixinPackage) {
        if(checkForClass("shadows.apotheosis.Apotheosis")) {
            APOTHEOSIS = true;
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if(mixinClassName.equals("limonblaze.originsclasses.mixin.compat.apotheosis.ApotheosisEnchantmentMenuMixin")) {
            return APOTHEOSIS;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
