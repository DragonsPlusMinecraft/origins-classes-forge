package limonblaze.originsclasses.common.apoli.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import limonblaze.originsclasses.common.apoli.configuration.ModifyCraftedFoodConfiguration;
import limonblaze.originsclasses.common.event.ModifyCraftResultEvent;
import limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import limonblaze.originsclasses.util.NbtUtils;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ModifyCraftedFoodPower extends PowerFactory<ModifyCraftedFoodConfiguration> {

    public ModifyCraftedFoodPower() {
        super(ModifyCraftedFoodConfiguration.CODEC);
    }
    
    public static boolean check(ConfiguredPower<ModifyCraftedFoodConfiguration, ModifyCraftedFoodPower> cp, Level level, ItemStack stack, ModifyCraftResultEvent.CraftingResultType type) {
        return cp.getConfiguration().craftingResultTypes().contains(type) && ConfiguredItemCondition.check(cp.getConfiguration().itemCondition(), level, stack);
    }
    
    public static void modify(Player player, ItemStack stack, ModifyCraftResultEvent.CraftingResultType type) {
        ListTag tag = new ListTag();
        IPowerContainer.getPowers(player, OriginsClassesPowers.MODIFY_CRAFTED_FOOD.get()).stream()
            .filter(cp -> check(cp, player.level, stack, type))
            .forEach(cp -> tag.add(StringTag.valueOf(cp.getConfiguration().modifyFoodPower().power().toString())));
        if(!tag.isEmpty()) stack.getOrCreateTagElement(NbtUtils.ORIGINS_CLASSES).put(NbtUtils.MODIFY_FOOD_POWERS, tag);
    }

}
