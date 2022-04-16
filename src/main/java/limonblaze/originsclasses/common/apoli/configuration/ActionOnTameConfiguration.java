package limonblaze.originsclasses.common.apoli.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import net.minecraft.core.Holder;

public record ActionOnTameConfiguration(Holder<ConfiguredBiEntityAction<?, ?>> biEntityAction,
                                        Holder<ConfiguredBiEntityCondition<?, ?>> biEntityCondition) implements IDynamicFeatureConfiguration {

    public static final Codec<ActionOnTameConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ConfiguredBiEntityAction.required("bientity_action")
            .forGetter(ActionOnTameConfiguration::biEntityAction),
        ConfiguredBiEntityCondition.optional("bientity_condition")
            .forGetter(ActionOnTameConfiguration::biEntityCondition)
    ).apply(instance, ActionOnTameConfiguration::new));

}
