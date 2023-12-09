package dev.limonblaze.originsclasses.core.mixin.common.minecraft;

import dev.limonblaze.originsclasses.util.CommonUtils;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import dev.limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(method = "playerDestroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;Z)V"))
    private void originsClasses$additionalDrop(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack, CallbackInfo ci) {
        int amount = CommonUtils.rollInt(IPowerContainer.modify(player, OriginsClassesPowers.MODIFY_BLOCK_LOOT.get(), 1.0F, cp -> cp.get().isActive(player) && ConfiguredBlockCondition.check(cp.get().getConfiguration().condition(), world, pos, () -> state)), world.random);
        for(int i = 1; i < amount; ++i) {
                Block.dropResources(state, world, pos, blockEntity, player, stack);
        }
    }

    @ModifyConstant(method = "playerDestroy", constant = @Constant(floatValue = 0.005F, ordinal = 0))
    private float originsClasses$removeBlockMiningExhaustion(float exhaustion, Level world, Player playerEntity, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
        if(IPowerContainer.hasPower(playerEntity, OriginsClassesPowers.NO_MINING_EXHAUSTION.get())) {
            return 0;
        }
        return exhaustion;
    }

}
