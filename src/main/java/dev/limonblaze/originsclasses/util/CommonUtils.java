package dev.limonblaze.originsclasses.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class CommonUtils {
    public static final String ORIGINS_CLASSES = "OriginsClasses";
    public static final String ENCHANTER = "Enchanter";
    public static final String MODIFY_FOOD_POWERS = "ModifyFoodPowers";
    public static final String POTION_BONUS = "PotionBonus";
    
    public static CompoundTag getOriginsClassesTag(CompoundTag nbt) {
        if(nbt.contains(ORIGINS_CLASSES, Tag.TAG_COMPOUND)) {
            return nbt.getCompound(ORIGINS_CLASSES);
        }
        return new CompoundTag();
    }
    
    public static CompoundTag getOrCreateOriginsClassesTag(CompoundTag nbt) {
        if(nbt.contains(ORIGINS_CLASSES, Tag.TAG_COMPOUND)) {
            return nbt.getCompound(ORIGINS_CLASSES);
        }
        CompoundTag entry = new CompoundTag();
        nbt.put(ORIGINS_CLASSES, entry);
        return entry;
    }
    
    public static int rollInt(double d, RandomSource random) {
        int i = (int) d;
        return random.nextDouble() < d - i ? i + 1 : i;
    }
    
    public static double calcYawDiff(Entity actor, Entity target) {
        float yawTarget = target.getYRot();
        while(yawTarget < 0F) yawTarget += 360F;
        yawTarget %= 360F;

        float yawSelf = actor.getYRot();
        while(yawSelf < 0F) yawSelf += 360F;
        yawSelf %= 360F;

        return Math.abs(yawTarget - yawSelf);
    }
    
    public static List<BlockPos> lumberjackMultiMineRange(Player player, BlockState state, BlockPos pos) {
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
                        if((newState.is(BlockTags.LEAVES) || newState.getBlock() instanceof LeavesBlock) &&
                          !(newState.hasProperty(LeavesBlock.PERSISTENT) && newState.getValue(LeavesBlock.PERSISTENT))
                        ) {
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
