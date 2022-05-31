package limonblaze.originsclasses.mixin;

import limonblaze.originsclasses.common.event.ModifyCraftResultEvent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;

@Mixin(FurnaceResultSlot.class)
public class FurnaceResultSlotMixin extends Slot {
    
    @Shadow @Final private Player player;
    
    public FurnaceResultSlotMixin(Container pContainer, int pIndex, int pX, int pY) {
        super(pContainer, pIndex, pX, pY);
    }
    
    @Override
    @Nonnull
    public ItemStack remove(int amount) {
        ModifyCraftResultEvent event = new ModifyCraftResultEvent(this.player, super.remove(amount).copy());
        MinecraftForge.EVENT_BUS.post(event);
        return event.getCrafted();
    }
    
    @Override
    @Nonnull
    public ItemStack getItem() {
        ModifyCraftResultEvent event = new ModifyCraftResultEvent(this.player, super.getItem().copy());
        MinecraftForge.EVENT_BUS.post(event);
        return event.getCrafted();
    }
    
}
