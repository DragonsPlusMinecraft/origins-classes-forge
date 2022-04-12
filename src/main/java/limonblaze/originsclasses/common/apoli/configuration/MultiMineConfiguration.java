package limonblaze.originsclasses.common.apoli.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import javax.annotation.Nullable;

import java.util.Optional;

public record MultiMineConfiguration(float speedMultiplier,
                                     @Nullable ConfiguredBlockCondition<?, ?> blockCondition,
                                     @Nullable ConfiguredItemCondition<?, ?> itemCondition,
                                     @Nullable ConfiguredEntityAction<?, ?> entityAction) implements IDynamicFeatureConfiguration {

    public static final Codec<MultiMineConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        CalioCodecHelper.optionalField(Codec.FLOAT, "speed_multiplier")
            .forGetter(x -> Optional.of(x.speedMultiplier)),
        CalioCodecHelper.optionalField(ConfiguredBlockCondition.CODEC, "block_condition")
            .forGetter(x -> Optional.ofNullable(x.blockCondition)),
        CalioCodecHelper.optionalField(ConfiguredItemCondition.CODEC, "block_condition")
            .forGetter(x -> Optional.ofNullable(x.itemCondition)),
        CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "entity_action")
            .forGetter(x -> Optional.ofNullable(x.entityAction))
        ).apply(instance, (sm, bc, ic, ea) -> new MultiMineConfiguration(sm.orElse(1.0F), bc.orElse(null), ic.orElse(null), ea.orElse(null)))
    );

}
