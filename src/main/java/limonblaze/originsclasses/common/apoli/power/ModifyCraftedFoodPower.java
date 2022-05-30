package limonblaze.originsclasses.common.apoli.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import limonblaze.originsclasses.common.apoli.configuration.ModifyCraftedFoodConfiguration;
import limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import limonblaze.originsclasses.util.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ModifyCraftedFoodPower extends PowerFactory<ModifyCraftedFoodConfiguration> {

    public ModifyCraftedFoodPower() {
        super(ModifyCraftedFoodConfiguration.CODEC);
    }
    
    public static void modifyCrafted(Player player, ItemStack stack) {
        List<ModifyCraftedFoodConfiguration> configs = IPowerContainer.getPowers(player, OriginsClassesPowers.MODIFY_CRAFTED_FOOD.get())
            .stream()
            .filter(cp -> check(cp, player, stack))
            .map(ConfiguredPower::getConfiguration)
            .toList();
        ListTag foodModifierTag = new ListTag();
        configs.stream()
            .flatMap(config -> config.foodModifiers().getContent().stream())
            .forEach(modifier -> foodModifierTag.add(modifier.save()));
        ListTag saturationModifierTag = new ListTag();
        configs.stream()
            .flatMap(config -> config.saturationModifiers().getContent().stream())
            .forEach(modifier -> saturationModifierTag.add(modifier.save()));
        CompoundTag originsClassesTag = stack.getOrCreateTagElement(NbtUtils.ORIGINS_CLASSES);
        if(!foodModifierTag.isEmpty()) originsClassesTag.put(NbtUtils.FOOD_MODIFIERS, foodModifierTag);
        if(!saturationModifierTag.isEmpty()) originsClassesTag.put(NbtUtils.SATURATION_MODIFIERS, saturationModifierTag);
    }
    
    public static boolean check(ConfiguredPower<ModifyCraftedFoodConfiguration, ModifyCraftedFoodPower> cp, Player player, ItemStack stack) {
        return ConfiguredItemCondition.check(cp.getConfiguration().itemCondition(), player.level, stack) &&
               ConfiguredEntityCondition.check(cp.getConfiguration().entityCondition(), player);
    }
    
    public static List<AttributeModifier> getModifiers(ItemStack stack, String key) {
        return NbtUtils.getOriginsClassesData(stack, key, Tag.TAG_COMPOUND)
            .stream()
            .map(tag -> (CompoundTag)tag)
            .map(AttributeModifier::load)
            .toList();
    }

}
