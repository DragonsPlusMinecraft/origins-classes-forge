package limonblaze.originsclasses.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

@FunctionalInterface
public interface MultiMineMode {

    List<BlockPos> getAffectedBlocks(Player player, BlockState state, BlockPos pos);

    MultiMineMode LUMBERJACK = (player, state, pos) -> {
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
                        if(newState.getBlock() instanceof LeavesBlock && !newState.getValue(LeavesBlock.PERSISTENT)) {
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
    };

}
