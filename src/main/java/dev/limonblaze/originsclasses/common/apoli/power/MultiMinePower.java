package dev.limonblaze.originsclasses.common.apoli.power;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.limonblaze.originsclasses.util.MultiMineMode;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import dev.limonblaze.originsclasses.client.OriginsClassesClient;
import dev.limonblaze.originsclasses.common.apoli.configuration.MultiMineConfiguration;
import dev.limonblaze.originsclasses.common.duck.SneakingStateSavingGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

public class MultiMinePower extends PowerFactory<MultiMineConfiguration> {
    private static ImmutableList<MultiMinePower> FACTORIES;
    public final MultiMineMode miner;

    public MultiMinePower(MultiMineMode miner) {
        super(MultiMineConfiguration.CODEC);
        this.miner = miner;
    }

    public static List<MultiMinePower> factories() {
        if(FACTORIES == null) {
            ImmutableList.Builder<MultiMinePower> factories = ImmutableList.builder();
            ApoliRegistries.POWER_FACTORY.get().getValues().forEach(pf -> {
                if(pf instanceof MultiMinePower mmp) factories.add(mmp);
            });
            FACTORIES = factories.build();
        }
        return FACTORIES;
    }

    public static boolean shouldApply(ConfiguredPower<?, ?> cp, Player player, BlockPos pos, BlockState state) {
        return cp.isActive(player) &&
            cp.getConfiguration() instanceof MultiMineConfiguration mmc &&
            ConfiguredBlockCondition.check(mmc.blockCondition(), player.level, pos, () -> state) &&
            ConfiguredItemCondition.check(mmc.itemCondition(), player.level, player.getMainHandItem());
    }

    public static Optional<Pair<List<BlockPos>, Float>> getResult(Player player, BlockPos pos, BlockState state) {
        List<ConfiguredPower<MultiMineConfiguration, MultiMinePower>> cmmps = factories().stream()
            .flatMap(pf -> IPowerContainer.getPowers(player, pf).stream())
            .map(Holder::get)
            .filter(cp -> shouldApply(cp, player, pos, state))
            .toList();
        for(ConfiguredPower<MultiMineConfiguration, MultiMinePower> cmmp : cmmps) {
            List<BlockPos> affectBlocks = cmmp.getFactory().miner.getAffectedBlocks(player, state, pos);
            if(affectBlocks.size() > 0) {
                return Optional.of(new Pair<>(affectBlocks, cmmp.getConfiguration().speedMultiplier()));
            }
        }
        return Optional.empty();
    }

    public static float modifyBreakingSpeed(float speed, Player player, BlockPos pos, BlockState state) {
        boolean processMultimine;
        if(player instanceof ServerPlayer sp) {
            SneakingStateSavingGameMode sneakingState = (SneakingStateSavingGameMode)sp.gameMode;
            processMultimine = !sneakingState.wasSneakingWhenBlockBreakingStarted();
        } else {
            processMultimine = OriginsClassesClient.MULTI_MINING;
        }
        return processMultimine ? speed * getResult(player, pos, state).map(Pair::getSecond).orElse(1.0F) : speed;
    }

}
