package limonblaze.originsclasses.common.apoli.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.Holder;

import javax.annotation.Nullable;
import java.util.Optional;

public record ActionOnTameConfiguration(Holder<ConfiguredBiEntityAction<?, ?>> biEntityAction,
                                        Holder<ConfiguredBiEntityCondition<?, ?>> biEntityCondition) implements IDynamicFeatureConfiguration {

    public static final Codec<ActionOnTameConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ConfiguredBiEntityAction.required("bientity_action")
            .forGetter(ActionOnTameConfiguration::biEntityAction),
        ConfiguredBiEntityCondition.optional("bientity_condition")
            .forGetter(ActionOnTameConfiguration::biEntityCondition)
    ).apply(instance, ActionOnTameConfiguration::new));

}
