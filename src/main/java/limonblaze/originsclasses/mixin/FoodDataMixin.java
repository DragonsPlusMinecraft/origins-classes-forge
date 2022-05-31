package limonblaze.originsclasses.mixin;

import io.github.apace100.apoli.util.AttributeUtil;
import limonblaze.originsclasses.common.apoli.power.ModifyCraftedFoodPower;
import limonblaze.originsclasses.util.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {
    
    @Unique private List<AttributeModifier> currentFoodModifiers = new ArrayList<>();
    @Unique private List<AttributeModifier> currentSaturationModifiers = new ArrayList<>();
    
    @ModifyVariable(method = "eat(IF)V", at = @At("HEAD"), argsOnly = true)
    private int originsClasses$modifyFoodValue(int original) {
        return Mth.floor(AttributeUtil.applyModifiers(currentFoodModifiers, original));
    }
    
    @ModifyVariable(method = "eat(IF)V", at = @At("HEAD"), argsOnly = true)
    private float originsClasses$modifySaturationModifiers(float original) {
        return (float) AttributeUtil.applyModifiers(currentSaturationModifiers, original);
    }
    
    @Inject(
        method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V")
    )
    private void originsClasses$modifyFoodProperties(Item pItem, ItemStack stack, LivingEntity entity, CallbackInfo ci) {
        currentFoodModifiers = ModifyCraftedFoodPower.getModifiers(stack, NbtUtils.FOOD_MODIFIERS);
        currentSaturationModifiers = ModifyCraftedFoodPower.getModifiers(stack, NbtUtils.SATURATION_MODIFIERS);
    }

}
