package limonblaze.originsclasses.common.apoli.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.Holder;

import javax.annotation.Nullable;

import java.util.Optional;

public record MultiMineConfiguration(float speedMultiplier,
                                     Holder<ConfiguredBlockCondition<?, ?>> blockCondition,
                                     Holder<ConfiguredItemCondition<?, ?>> itemCondition,
                                     Holder<ConfiguredEntityAction<?, ?>> entityAction) implements IDynamicFeatureConfiguration {

    public static final Codec<MultiMineConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        CalioCodecHelper.optionalField(Codec.FLOAT, "speed_multiplier")
            .forGetter(x -> Optional.of(x.speedMultiplier)),
        ConfiguredBlockCondition.optional("block_condition")
            .forGetter(MultiMineConfiguration::blockCondition),
        ConfiguredItemCondition.optional("item_condition")
            .forGetter(MultiMineConfiguration::itemCondition),
        ConfiguredEntityAction.optional("entity_action")
            .forGetter(MultiMineConfiguration::entityAction)
        ).apply(instance, (sm, bc, ic, ea) -> new MultiMineConfiguration(sm.orElse(1.0F), bc, ic, ea)));

}
