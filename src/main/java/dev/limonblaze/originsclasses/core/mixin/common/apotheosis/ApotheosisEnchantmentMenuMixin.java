package dev.limonblaze.originsclasses.core.mixin.common.apotheosis;

import dev.limonblaze.originsclasses.util.CommonUtils;
import dev.shadowsoffire.apotheosis.ench.table.ApothEnchantmentMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ApothEnchantmentMenu.class)
public class ApotheosisEnchantmentMenuMixin {

    @Shadow(remap = false) @Final protected Player player;

    @ModifyVariable(
        method = "lambda$slotsChanged$1",
        at = @At(value = "INVOKE", target = "Ldev/shadowsoffire/apotheosis/ench/table/ApothEnchantmentMenu;gatherStats()V", remap = false),
        name = "toEnchant"
    )
    private ItemStack originsClasses$storeEnchanter(ItemStack stack) {
        stack.getOrCreateTagElement(CommonUtils.ORIGINS_CLASSES).putUUID(CommonUtils.ENCHANTER, this.player.getUUID());
        return stack;
    }

    @Inject(method = "slotsChanged", at = @At("TAIL"))
    private void originsClasses$clearEnchanter(Container inventoryIn, CallbackInfo ci) {
        ItemStack stack = inventoryIn.getItem(0);
        CompoundTag entry = stack.getOrCreateTagElement(CommonUtils.ORIGINS_CLASSES);
        entry.remove(CommonUtils.ENCHANTER);
        if(entry.isEmpty()) stack.removeTagKey(CommonUtils.ORIGINS_CLASSES);
    }

}
