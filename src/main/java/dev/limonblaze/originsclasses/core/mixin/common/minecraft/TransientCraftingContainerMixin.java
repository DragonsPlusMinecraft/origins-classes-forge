package dev.limonblaze.originsclasses.core.mixin.common.minecraft;

import dev.limonblaze.originsclasses.core.helper.TransientCraftingContainerAccessor;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TransientCraftingContainer.class)
public class TransientCraftingContainerMixin  implements TransientCraftingContainerAccessor {
    @Final
    @Shadow
    private net.minecraft.world.inventory.AbstractContainerMenu menu;
    @Override
    public AbstractContainerMenu origins_classes_forge$getMenu() {
        return this.menu;
    }
}
