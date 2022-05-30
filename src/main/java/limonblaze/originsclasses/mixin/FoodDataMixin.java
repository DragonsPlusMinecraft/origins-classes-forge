package limonblaze.originsclasses.mixin;

import limonblaze.originsclasses.common.apoli.power.ModifyCraftedFoodPower;
import limonblaze.originsclasses.common.duck.ModifiableFoodProperties;
import limonblaze.originsclasses.util.NbtUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {

    @ModifyVariable(
        method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V")
    )
    private FoodProperties originsClasses$modifyFoodProperties(FoodProperties food, Item item, ItemStack stack, LivingEntity entity) {
        return ((ModifiableFoodProperties)food).getModifiedFoodProperties(
            ModifyCraftedFoodPower.getModifiers(stack, NbtUtils.FOOD_MODIFIERS),
            ModifyCraftedFoodPower.getModifiers(stack, NbtUtils.SATURATION_MODIFIERS)
        );
    }

}
