package limonblaze.originsclasses.compat;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import se.mickelus.tetra.TetraMod;

import java.util.List;
import java.util.Set;

public class OriginsClassesMixinPlugin implements IMixinConfigPlugin {
    private boolean apotheosis;
    private boolean farmersdelight;
    private boolean tetra;

    private boolean checkForClass(String cls) {
        try {
            Class.forName(cls, false, OriginsClassesMixinPlugin.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    @Override
    public void onLoad(String mixinPackage) {
        apotheosis = checkForClass("shadows.apotheosis.Apotheosis");
        farmersdelight = checkForClass("vectorwing.farmersdelight.FarmersDelight");
        tetra = checkForClass("se.mickelus.tetra.TetraMod");
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return switch(mixinClassName) {
            case "limonblaze.originsclasses.mixin.compat.apotheosis.ApotheosisEnchantmentMenuMixin" -> apotheosis;
            case "limonblaze.originsclasses.mixin.compat.farmersdelight.CookingPotResultSlotMixin",
                 "limonblaze.originsclasses.mixin.compat.farmersdelight.SkilletItemMixin" -> farmersdelight;
            case "limonblaze.originsclasses.mixin.compat.tetra.WorkbenchTileMixin" -> tetra;
            default -> true;
        };
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
