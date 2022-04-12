package limonblaze.originsclasses.mixin;

import limonblaze.originsclasses.util.PowerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {

    @Shadow public abstract void eat(int food, float saturationModifier);

    @Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V", at = @At("TAIL"))
    private void originsClasses$handleFoodBonus(Item item, ItemStack stack, CallbackInfo ci) {
        FoodProperties food = item.getFoodProperties();
        if(food != null && stack.hasTag() && stack.getOrCreateTag().contains(PowerUtil.ORIGINS_CLASSES, Tag.TAG_COMPOUND)) {
            CompoundTag nbt = stack.getOrCreateTagElement(PowerUtil.ORIGINS_CLASSES);
            if(nbt.contains(PowerUtil.FOOD_BONUS, Tag.TAG_FLOAT)) {
                this.eat(Mth.floor(food.getNutrition() * nbt.getFloat(PowerUtil.FOOD_BONUS)), food.getSaturationModifier());
            }
        }
    }

}
