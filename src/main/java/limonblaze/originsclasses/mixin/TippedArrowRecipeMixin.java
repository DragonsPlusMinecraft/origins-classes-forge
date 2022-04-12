package limonblaze.originsclasses.mixin;

import limonblaze.originsclasses.util.PowerUtil;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.TippedArrowRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TippedArrowRecipe.class)
public class TippedArrowRecipeMixin {

    @Inject(method = "assemble(Lnet/minecraft/world/inventory/CraftingContainer;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"), cancellable = true)
    private void originsClasses$handleAdditionalPotionNbt(CraftingContainer inv, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack result = cir.getReturnValue();
        if(!result.isEmpty()) {
            ItemStack input = inv.getItem(1 + inv.getWidth());
            if(input.hasTag()) {
                byte bonus = PowerUtil.getPotionBonus(input);
                if(bonus > 0) {
                    cir.setReturnValue(PowerUtil.setPotionBonus(result, bonus));
                }
            }
        }
    }

}
