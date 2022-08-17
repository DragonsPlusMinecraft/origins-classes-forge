package limonblaze.originsclasses.common.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ModifyCraftResultEvent extends PlayerEvent {
    private ItemStack crafted;
    private final CraftingResultType type;
    
    public ModifyCraftResultEvent(Player player, ItemStack crafted, CraftingResultType type) {
        super(player);
        this.crafted = crafted;
        this.type = type;
    }
    
    public void setCrafted(ItemStack crafted) {
        this.crafted = crafted;
    }
    
    public ItemStack getCrafted() {
        return crafted;
    }
    
    public CraftingResultType getType() {
        return this.type;
    }
    
    public enum CraftingResultType {
        CRAFTING, SMELTING, COOKING_POT, SKILLET, TETRA
    }
    
}
