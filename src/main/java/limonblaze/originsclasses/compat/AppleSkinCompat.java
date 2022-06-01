package limonblaze.originsclasses.compat;

import io.github.edwinmindcraft.apoli.common.power.ModifyFoodPower;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyFoodConfiguration;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import squeek.appleskin.api.event.FoodValuesEvent;
import squeek.appleskin.api.food.FoodValues;

public class AppleSkinCompat {
    
    @SubscribeEvent
    public static void onAppleSkinFoodValueEvent(FoodValuesEvent event) {
        Player player = event.player;
        ItemStack stack = event.itemStack;
        int hunger = (int) ModifyFoodPower.apply(
            ModifyFoodPower.getValidPowers(player, stack),
            player.level,
            stack,
            event.modifiedFoodValues.hunger,
            ModifyFoodConfiguration::foodModifiers
        );
        float saturation = (float) ModifyFoodPower.apply(
            ModifyFoodPower.getValidPowers(player, stack),
            player.level,
            stack,
            event.modifiedFoodValues.saturationModifier,
            ModifyFoodConfiguration::saturationModifiers
        );
        event.modifiedFoodValues = new FoodValues(hunger, saturation);
    }
    
}
