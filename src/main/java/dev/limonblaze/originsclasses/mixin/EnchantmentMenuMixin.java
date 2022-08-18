package dev.limonblaze.originsclasses.mixin;

import dev.limonblaze.originsclasses.util.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(EnchantmentMenu.class)
public class EnchantmentMenuMixin {

    @Nullable
    private Player originsClasses$enchanter;

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    private void originsClasses$saveEnchanter(int syncId, Inventory playerInventory, ContainerLevelAccess context, CallbackInfo ci) {
        this.originsClasses$enchanter = playerInventory.player;
    }

    @ModifyVariable(method = "slotsChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ContainerLevelAccess;execute(Ljava/util/function/BiConsumer;)V"))
    private ItemStack originsClasses$storeEnchanter(ItemStack stack) {
        if(this.originsClasses$enchanter != null) {
            stack.getOrCreateTagElement(NbtUtils.ORIGINS_CLASSES).putUUID(NbtUtils.ENCHANTER, this.originsClasses$enchanter.getUUID());
        }
        return stack;
    }

    @Inject(method = "slotsChanged", at = @At("TAIL"))
    private void originsClasses$clearEnchanter(Container inventory, CallbackInfo ci) {
        ItemStack stack = inventory.getItem(0);
        CompoundTag ocNbt = stack.getOrCreateTagElement(NbtUtils.ORIGINS_CLASSES);
        ocNbt.remove(NbtUtils.ENCHANTER);
        if(ocNbt.isEmpty()) stack.removeTagKey(NbtUtils.ORIGINS_CLASSES);
    }

}
