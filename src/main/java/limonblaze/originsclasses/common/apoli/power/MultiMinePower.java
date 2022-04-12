package limonblaze.originsclasses.common.apoli.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import limonblaze.originsclasses.client.OriginsClassesClient;
import limonblaze.originsclasses.common.apoli.configuration.MultiMineConfiguration;
import limonblaze.originsclasses.common.duck.SneakingStateSave;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class MultiMinePower extends PowerFactory<MultiMineConfiguration> {
    private static final List<MultiMinePower> POWERS = new ArrayList<>();
    public final Miner miner;

    public MultiMinePower(Miner miner) {
        super(MultiMineConfiguration.CODEC);
        this.miner = miner;
    }

    public static void bootstrap() {
        ApoliRegistries.POWER_FACTORY.get().getValues().forEach(pf -> {
            if(pf instanceof MultiMinePower mmp) POWERS.add(mmp);
        });
    }

    public static boolean shouldApply(ConfiguredPower<?, ?> cp, Player player, BlockPos pos, BlockState state) {
        return cp.isActive(player) &&
            cp.getConfiguration() instanceof MultiMineConfiguration mmc &&
            ConfiguredBlockCondition.check(mmc.blockCondition(), player.level, pos, () -> state) &&
            ConfiguredItemCondition.check(mmc.itemCondition(), player.level, player.getMainHandItem());
    }

    public static MultiMineData getMultiMineData(Player player, BlockPos pos, BlockState state) {
        List<ConfiguredPower<MultiMineConfiguration, MultiMinePower>> cmmps = POWERS.stream()
            .flatMap(pf -> IPowerContainer.getPowers(player, pf).stream())
            .filter(cp -> shouldApply(cp, player, pos, state))
            .toList();
        for(ConfiguredPower<MultiMineConfiguration, MultiMinePower> cmmp : cmmps) {
            List<BlockPos> affectBlock = cmmp.getFactory().miner.getAffectedBlocks(player, state, pos);
            if(affectBlock.size() > 0) {
                return new MultiMineData(affectBlock, cmmp.getConfiguration().speedMultiplier());
            }
        }
        return new MultiMineData(new ArrayList<>(), 1F);
    }

    public static float modifyBreakingSpeed(float speed, Player player, BlockPos pos, BlockState state) {
        boolean processMultimine;
        if(player instanceof ServerPlayer sp) {
            SneakingStateSave sneakingState = (SneakingStateSave)sp.gameMode;
            processMultimine = !sneakingState.originsClasses$wasSneakingWhenBlockBreakingStarted();
        } else {
            processMultimine = OriginsClassesClient.MULTI_MINING;
        }
        if(processMultimine) {
            MultiMineData data = getMultiMineData(player, pos, state);
            if(data.affectedBlocks.size() > 0) {
                return data.getModifiedSpeed(speed);
            }
        }
        return speed;
    }

    public record MultiMineData(List<BlockPos> affectedBlocks, float speedMultiplier) {

        public float getModifiedSpeed(float speed) {
            return speed * this.speedMultiplier / affectedBlocks.size();
        }

    }

    @FunctionalInterface
    public interface Miner {
        List<BlockPos> getAffectedBlocks(Player player, BlockState state, BlockPos pos);
    }

}
