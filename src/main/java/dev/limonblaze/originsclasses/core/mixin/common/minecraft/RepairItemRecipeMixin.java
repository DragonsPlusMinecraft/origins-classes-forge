package dev.limonblaze.originsclasses.core.mixin.common.minecraft;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import dev.limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.crafting.RepairItemRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(RepairItemRecipe.class)
public class RepairItemRecipeMixin {

    @ModifyConstant(method = "assemble(Lnet/minecraft/world/inventory/CraftingContainer;)Lnet/minecraft/world/item/ItemStack;", constant = @Constant(intValue = 5, ordinal = 0))
    private int doubleRepairDurabilityBonus(int original, CraftingContainer container) {
        AbstractContainerMenu menu = container.menu;
        Player player = null;
        if(menu instanceof CraftingMenu craftingMenu) {
            player = craftingMenu.player;
        } else if(menu instanceof InventoryMenu inventoryMenu) {
            player = inventoryMenu.owner;
        }
        return Mth.clamp(Mth.floor(IPowerContainer.modify(player, OriginsClassesPowers.MODIFY_COMBINE_REPAIR_DURABILITY.get(), original)), 0, 100);
    }

}
