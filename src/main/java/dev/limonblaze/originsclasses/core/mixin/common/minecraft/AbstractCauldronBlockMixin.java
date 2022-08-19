package dev.limonblaze.originsclasses.core.mixin.common.minecraft;

import dev.limonblaze.originsclasses.util.ClericHelper;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import dev.limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractCauldronBlock.class)
public class AbstractCauldronBlockMixin {

    //We have to do this as ActionOnBlockUsePower is bad at handling continous block state change
    @Inject(method = "use", at = @At("RETURN"), cancellable = true)
    private void originsClasses$addPotionBonus(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if(state.is(Blocks.WATER_CAULDRON) && state.getValue(LayeredCauldronBlock.LEVEL) > 0) {
            ItemStack stack = player.getItemInHand(hand);
            if(stack.getItem() instanceof PotionItem &&
               !PotionUtils.getPotion(stack).getEffects().isEmpty() &&
               !(stack.hasTag() && ClericHelper.getPotionBonus(stack.getTag()))
            ) {
                if(IPowerContainer.hasPower(player, OriginsClassesPowers.POTION_BONUS.get())) {
                    ClericHelper.setPotionBonus(stack.getTag(), true);
                    LayeredCauldronBlock.lowerFillLevel(state, world, pos);
                    world.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    world.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
                    cir.setReturnValue(InteractionResult.SUCCESS);
                }
            }
        }
    }

}
