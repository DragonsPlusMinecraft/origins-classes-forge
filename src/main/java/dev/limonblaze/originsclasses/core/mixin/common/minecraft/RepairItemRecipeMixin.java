package dev.limonblaze.originsclasses.core.mixin.common.minecraft;

import dev.limonblaze.originsclasses.core.helper.TransientCraftingContainerAccessor;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import dev.limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.crafting.RepairItemRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(RepairItemRecipe.class)
public class RepairItemRecipeMixin {

    @ModifyConstant(method = "assemble(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", constant = @Constant(intValue = 5, ordinal = 0))
    private int doubleRepairDurabilityBonus(int original, CraftingContainer container) {
        if(container instanceof TransientCraftingContainer tr) {
            AbstractContainerMenu menu = ((TransientCraftingContainerAccessor)tr).origins_classes_forge$getMenu();
            Player player = null;
            if(menu instanceof CraftingMenu craftingMenu) {
                player = craftingMenu.player;
            } else if(menu instanceof InventoryMenu inventoryMenu) {
                player = inventoryMenu.owner;
            }
            return Mth.clamp(Mth.floor(IPowerContainer.modify(player, OriginsClassesPowers.MODIFY_COMBINE_REPAIR_DURABILITY.get(), original)), 0, 100);
        }
        return original;
    }

}
