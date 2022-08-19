package dev.limonblaze.originsclasses.core.mixin.common.minecraft;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import dev.limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

    public AnvilMenuMixin(MenuType<?> type, int syncId, Inventory playerInventory, ContainerLevelAccess context) {
        super(type, syncId, playerInventory, context);
    }

    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 4, ordinal = 0))
    private int originsClasses$modfifyRepairMaterialCost0(int original) {
        return Mth.clamp(Mth.floor(IPowerContainer.modify(this.player, OriginsClassesPowers.MODIFY_REPAIR_MATERIAL_COST.get(), original)), 1, Integer.MAX_VALUE);
    }

    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 4, ordinal = 1))
    private int originsClasses$modfifyRepairMaterialCost1(int original) {
        return Mth.clamp(Mth.floor(IPowerContainer.modify(this.player, OriginsClassesPowers.MODIFY_REPAIR_MATERIAL_COST.get(), original)), 1, Integer.MAX_VALUE);
    }

    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 12, ordinal = 0))
    private int originsClasses$modifyCombineRepairDurabilityBonus(int original) {
        return Mth.clamp(Mth.floor(IPowerContainer.modify(this.player, OriginsClassesPowers.MODIFY_COMBINE_REPAIR_DURABILITY.get(), original)), 0, 100);
    }

}