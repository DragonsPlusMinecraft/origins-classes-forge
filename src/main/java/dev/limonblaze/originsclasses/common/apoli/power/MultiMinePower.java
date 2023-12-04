package dev.limonblaze.originsclasses.common.apoli.power;

import dev.limonblaze.originsclasses.OriginsClasses;
import dev.limonblaze.originsclasses.common.apoli.configuration.MultiMineConfiguration;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class MultiMinePower extends PowerFactory<MultiMineConfiguration> {
    public final Range range;
    
    public MultiMinePower(Range range) {
        super(MultiMineConfiguration.CODEC);
        this.range = range;
    }

    public static boolean shouldApply(ConfiguredPower<?, ?> cp, Player player, BlockPos pos, BlockState state) {
        return cp.isActive(player) &&
            cp.getConfiguration() instanceof MultiMineConfiguration mmc &&
            ConfiguredBlockCondition.check(mmc.blockCondition(), player.level(), pos, () -> state) &&
            ConfiguredItemCondition.check(mmc.itemCondition(), player.level(), player.getMainHandItem());
    }
    
    public static List<BlockPos> apply(Player player, BlockPos pos, BlockState state) {
        List<ConfiguredPower<MultiMineConfiguration, MultiMinePower>> cmmps = ApoliRegistries.POWER_FACTORY.get()
            .getValues()
            .stream()
            .filter(pf -> pf instanceof MultiMinePower)
            .flatMap(pf -> IPowerContainer.getPowers(player, (MultiMinePower) pf).stream())
            .map(Holder::get)
            .filter(cp -> shouldApply(cp, player, pos, state))
            .toList();
        OriginsClasses.LOGGER.debug(cmmps.size());
        for(ConfiguredPower<MultiMineConfiguration, MultiMinePower> cmmp : cmmps) {
            List<BlockPos> affectBlocks = cmmp.getFactory().range.getAffectedBlocks(player, state, pos);
            if(affectBlocks.size() > 0) {
                return affectBlocks;
            }
        }
        return new ArrayList<>();
    }
    
    @FunctionalInterface
    public interface Range {
        
        List<BlockPos> getAffectedBlocks(Player player, BlockState state, BlockPos pos);
    
    }
    
}
