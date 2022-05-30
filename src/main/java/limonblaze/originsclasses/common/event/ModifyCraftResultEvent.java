package limonblaze.originsclasses.common.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ModifyCraftResultEvent extends PlayerEvent {
    private final ItemStack crafted;
    
    public ModifyCraftResultEvent(Player player, ItemStack crafted) {
        super(player);
        this.crafted = crafted;
    }
    
    public ItemStack getCrafted() {
        return crafted;
    }
    
}
