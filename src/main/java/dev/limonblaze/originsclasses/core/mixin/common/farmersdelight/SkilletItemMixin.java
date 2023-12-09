package dev.limonblaze.originsclasses.core.mixin.common.farmersdelight;

import dev.limonblaze.originsclasses.common.event.ModifyCraftResultEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vectorwing.farmersdelight.common.item.SkilletItem;

@Mixin(SkilletItem.class)
public class SkilletItemMixin {
    @Unique
    private static Player origins_classes_forge$player;

    @Inject(
            method = "lambda$finishUsingItem$1",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/crafting/CampfireCookingRecipe;assemble(Lnet/minecraft/world/Container;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;",
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void originsClasses$storeCraftResult(Level level, Player player, ItemStack stack, CampfireCookingRecipe recipe, CallbackInfo ci) {
        SkilletItemMixin.origins_classes_forge$player = player;
    }
    
    @ModifyVariable(
        method = "lambda$finishUsingItem$1",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"),
        name = "resultStack"
    )
    private static ItemStack originsClasses$modifyCraftResult(ItemStack result) {
        ModifyCraftResultEvent event = new ModifyCraftResultEvent(origins_classes_forge$player, result, ModifyCraftResultEvent.CraftingResultType.SKILLET);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getCrafted();
    }
    
}
