package limonblaze.originsclasses.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import java.util.*;

public class PowerUtil {
    public static final String ORIGINS_CLASSES_ENCHANTER = "OriginsClassesEnchanter";
    public static final String ORIGINS_CLASSES = "OriginsClasses";
    public static final String FOOD_BONUS = "FoodBonus";
    public static final String POTION_BONUS = "PotionBonus";

    public static int naturalRandFloor(double d, Random random) {
        if(d > 0) {
            int i = (int) d;
            return random.nextDouble() < d - i ? i + 1 : i;
        }
        return 0;
    }

    public static double yawDiff(Entity actor, Entity target) {
        float yawTarget = target.getYRot();
        while(yawTarget < 0F) yawTarget += 360F;
        yawTarget %= 360F;

        float yawSelf = actor.getYRot();
        while(yawSelf < 0F) yawSelf += 360F;
        yawSelf %= 360F;

        return Math.abs(yawTarget - yawSelf);
    }

    public static byte getPotionBonus(ItemStack stack) {
        if(stack.hasTag()) return getPotionBonus(stack.getTag());
        return -1;
    }

    public static byte getPotionBonus(@Nullable CompoundTag nbt) {
        if(nbt != null && nbt.contains(ORIGINS_CLASSES, Tag.TAG_COMPOUND)) {
            CompoundTag ocNbt = nbt.getCompound(ORIGINS_CLASSES);
            if(ocNbt.contains(POTION_BONUS, Tag.TAG_BYTE)) {
                return ocNbt.getByte(POTION_BONUS);
            }
        }
        return -1;
    }

    public static ItemStack setPotionBonus(ItemStack stack, byte bonus) {
        stack.getOrCreateTagElement(ORIGINS_CLASSES).putByte(POTION_BONUS, bonus);
        return stack;
    }

    public static CompoundTag setPotionBonus(CompoundTag tag, byte bonus) {
        if(tag.contains(ORIGINS_CLASSES, Tag.TAG_COMPOUND)) {
            tag.getCompound(ORIGINS_CLASSES).putByte(POTION_BONUS, bonus);
        }
        return tag;
    }

    public static MobEffectInstance applyPotionBonus(MobEffectInstance effect, byte bonusLevel) {
        boolean instant = effect.getEffect().isInstantenous();
        return new MobEffectInstance(
            effect.getEffect(),
            instant ? effect.getDuration() : effect.getDuration() * (1 + bonusLevel),
            instant ? effect.getAmplifier() + bonusLevel : effect.getAmplifier(),
            effect.isAmbient(),
            effect.isVisible(),
            effect.showIcon()
        );
    }

    public static @Nonnull List<BlockPos> lumberjackMiner(Player player, BlockState state, BlockPos pos) {
        Set<BlockPos> affected = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(pos);
        boolean foundOneWithLeaves = false;
        BlockPos.MutableBlockPos pos$mutable = pos.mutable();
        BlockPos.MutableBlockPos newPos$mutable = pos.mutable();
        while(!queue.isEmpty()) {
            pos$mutable.set(queue.remove());
            for(int dx = -1; dx <= 1; dx++) {
                for(int dy = 0; dy <= 1; dy++) {
                    for(int dz = -1; dz <= 1; dz++) {
                        if(dx == 0 & dy == 0 && dz == 0) {
                            continue;
                        }
                        newPos$mutable.set(pos$mutable.getX() + dx, pos$mutable.getY() + dy, pos$mutable.getZ() + dz);
                        BlockState newState = player.level.getBlockState(newPos$mutable);
                        if(newState.is(state.getBlock()) && !affected.contains(newPos$mutable)) {
                            BlockPos savedNewPos = newPos$mutable.immutable();
                            affected.add(savedNewPos);
                            queue.add(savedNewPos);
                            if(affected.size() > 255) {
                                if(!foundOneWithLeaves) {
                                    return new ArrayList<>();
                                }
                                return new ArrayList<>(affected);
                            }
                        } else
                        if((BlockTags.LEAVES.contains(newState.getBlock()) || newState.getBlock() instanceof LeavesBlock) && !newState.getValue(LeavesBlock.PERSISTENT)) {
                            foundOneWithLeaves = true;
                        }
                    }
                }
            }
        }
        if(!foundOneWithLeaves) {
            affected.clear();
        }
        return new ArrayList<>(affected);
    }

}
