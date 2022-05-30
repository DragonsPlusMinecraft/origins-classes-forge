package limonblaze.originsclasses.mixin.compat.farmersdelight;

import limonblaze.originsclasses.common.event.ModifyCraftResultEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotResultSlot;

import javax.annotation.Nonnull;

@Mixin(CookingPotResultSlot.class)
public class CookingPotResultSlotMixin extends SlotItemHandler {
    
    @Shadow(remap = false) @Final private Player player;
    
    public CookingPotResultSlotMixin(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
    
    @Override
    @Nonnull
    public ItemStack getItem() {
        ModifyCraftResultEvent event = new ModifyCraftResultEvent(this.player, super.getItem().copy());
        MinecraftForge.EVENT_BUS.post(event);
        return event.getCrafted();
    }
    
}
