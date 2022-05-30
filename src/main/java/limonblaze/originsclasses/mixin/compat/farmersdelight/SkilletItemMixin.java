package limonblaze.originsclasses.mixin.compat.farmersdelight;

import limonblaze.originsclasses.common.event.ModifyCraftResultEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import vectorwing.farmersdelight.common.item.SkilletItem;

@Mixin(SkilletItem.class)
public class SkilletItemMixin {
    
    @ModifyVariable(
        method = "lambda$finishUsingItem$1",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"),
        name = "resultStack",
        remap = false
    )
    private static ItemStack originsClasses$modifyCraftResult(ItemStack result, Player player, ItemStack skillet, CampfireCookingRecipe recipe) {
        ModifyCraftResultEvent event = new ModifyCraftResultEvent(player, result);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getCrafted();
    }
    
}
