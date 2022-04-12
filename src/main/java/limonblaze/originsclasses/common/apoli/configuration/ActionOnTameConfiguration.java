package limonblaze.originsclasses.common.apoli.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;

import javax.annotation.Nullable;
import java.util.Optional;

public record ActionOnTameConfiguration(ConfiguredBiEntityAction<?, ?> biEntityAction,
                                        @Nullable ConfiguredBiEntityCondition<?, ?> biEntityCondition) implements IDynamicFeatureConfiguration {

    public static final Codec<ActionOnTameConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        CalioCodecHelper.optionalField(ConfiguredBiEntityAction.CODEC, "bientity_action")
            .forGetter(x -> Optional.ofNullable(x.biEntityAction)),
        CalioCodecHelper.optionalField(ConfiguredBiEntityCondition.CODEC, "bientity_condition")
            .forGetter(x -> Optional.ofNullable(x.biEntityCondition))
    ).apply(instance, (bea, bec) -> new ActionOnTameConfiguration(bea.orElse(null), bec.orElse(null))));

}
