package dev.limonblaze.originsclasses.core.mixin.common.tetra;


import dev.limonblaze.originsclasses.common.event.ModifyCraftResultEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;

import java.util.Map;

@Mixin(WorkbenchTile.class)
public class WorkbenchTileMixin {
    
    @Inject(
            method = "applyCraftingBonusEffects",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private static void originsClasses$modifyResult(ItemStack upgradedStack, String slot, boolean isReplacing, Player player, ItemStack[] preMaterials, ItemStack[] postMaterials, Map<ToolAction, Integer> tools, Level world, BlockPos pos, BlockState blockState, boolean consumeResources, CallbackInfoReturnable<ItemStack> cir){
        ModifyCraftResultEvent event = new ModifyCraftResultEvent(player, upgradedStack, ModifyCraftResultEvent.CraftingResultType.TETRA);
        MinecraftForge.EVENT_BUS.post(event);
        cir.setReturnValue(event.getCrafted());
    }
    
}