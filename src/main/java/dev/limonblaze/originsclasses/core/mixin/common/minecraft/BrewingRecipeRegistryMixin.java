package dev.limonblaze.originsclasses.core.mixin.common.minecraft;

import dev.limonblaze.originsclasses.util.ClericHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {

    @Inject(method = "getOutput", at = @At("RETURN"), remap = false)
    private static void originsClasses$handleAdditionalPotionNbt(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack output = cir.getReturnValue();
        if(output.getItem() instanceof PotionItem && input.hasTag()) {
            if(ClericHelper.getPotionBonus(input.getTag())) {
                ClericHelper.setPotionBonus(output.getOrCreateTag(), true);
            }
        }
    }

}
