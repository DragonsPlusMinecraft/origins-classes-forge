package dev.limonblaze.originsclasses.common.apoli.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import net.minecraft.core.Holder;

public record MultiMineConfiguration(Holder<ConfiguredBlockCondition<?, ?>> blockCondition,
                                     Holder<ConfiguredItemCondition<?, ?>> itemCondition,
                                     Holder<ConfiguredEntityAction<?, ?>> entityAction) implements IDynamicFeatureConfiguration {

    public static final Codec<MultiMineConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ConfiguredBlockCondition.optional("block_condition")
            .forGetter(MultiMineConfiguration::blockCondition),
        ConfiguredItemCondition.optional("item_condition")
            .forGetter(MultiMineConfiguration::itemCondition),
        ConfiguredEntityAction.optional("entity_action")
            .forGetter(MultiMineConfiguration::entityAction)
        ).apply(instance, MultiMineConfiguration::new));

}
