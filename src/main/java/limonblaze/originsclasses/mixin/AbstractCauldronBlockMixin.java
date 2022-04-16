package limonblaze.originsclasses.mixin;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import limonblaze.originsclasses.util.ClericUtils;
import limonblaze.originsclasses.util.NbtType;
import limonblaze.originsclasses.util.NbtUtils;
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

    @Inject(method = "use", at = @At("RETURN"), cancellable = true)
    private void originsClasses$addPotionBonus(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if(state.is(Blocks.WATER_CAULDRON) && state.getValue(LayeredCauldronBlock.LEVEL) > 0) {
            ItemStack stack = player.getItemInHand(hand);
            if(stack.getItem() instanceof PotionItem &&
               !PotionUtils.getPotion(stack).getEffects().isEmpty() &&
               NbtUtils.getOriginsClassesData(stack, NbtUtils.POTION_BONUS, NbtType.BYTE).isPresent()) {
                int bonus = IPowerContainer.getPowers(player, OriginsClassesPowers.POTION_BONUS.get()).stream()
                    .filter(cp -> cp.isActive(player)).mapToInt(cp -> cp.getConfiguration().value()).sum();
                if(bonus > 0) {
                    ClericUtils.setPotionBonus(stack, (byte) bonus);
                    LayeredCauldronBlock.lowerFillLevel(state, world, pos);
                    world.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    world.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
                    cir.setReturnValue(InteractionResult.SUCCESS);
                }
            }
        }
    }

}
