package limonblaze.originsclasses.common.apoli.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.ModifyCraftingPower;
import limonblaze.originsclasses.common.apoli.configuration.ModifyCraftResultConfiguration;
import limonblaze.originsclasses.common.event.ModifyCraftResultEvent;
import limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.List;

public class ModifyCraftResultPower extends PowerFactory<ModifyCraftResultConfiguration> {
    
    public ModifyCraftResultPower() {
        super(ModifyCraftResultConfiguration.CODEC);
    }
    
    public static boolean check(
        ConfiguredPower<ModifyCraftResultConfiguration, ModifyCraftResultPower> cp,
        Level level, ItemStack stack, ModifyCraftResultEvent.CraftingResultType type) {
        return cp.getConfiguration().craftingResultTypes().contains(type) &&
               ConfiguredItemCondition.check(cp.getConfiguration().itemCondition(), level, stack);
    }
    
    public static ItemStack modify(Player player, ItemStack stack, ModifyCraftResultEvent.CraftingResultType type) {
        Mutable<ItemStack> mutable = new MutableObject<>(stack);
        IPowerContainer.getPowers(player, OriginsClassesPowers.MODIFY_CRAFT_RESULT.get())
            .stream()
            .filter(cp -> check(cp, player.level, stack, type))
            .map(cp -> cp.getConfiguration().itemAction())
            .forEach(action -> ConfiguredItemAction.execute(action, player.level, mutable));
        return mutable.getValue();
    }
    
}
