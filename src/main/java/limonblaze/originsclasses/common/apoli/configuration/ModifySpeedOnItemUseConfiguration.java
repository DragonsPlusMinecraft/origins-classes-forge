package limonblaze.originsclasses.common.apoli.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import javax.annotation.Nullable;
import java.util.Optional;

public record ModifySpeedOnItemUseConfiguration(ListConfiguration<AttributeModifier> modifiers,
                                                @Nullable ConfiguredItemCondition<?, ?> itemCondition) implements IValueModifyingPowerConfiguration {

    public static final Codec<ModifySpeedOnItemUseConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ListConfiguration.modifierCodec("modifier")
            .forGetter(ModifySpeedOnItemUseConfiguration::modifiers),
        CalioCodecHelper.optionalField(ConfiguredItemCondition.CODEC, "item_condition")
            .forGetter(x -> Optional.ofNullable(x.itemCondition))
    ).apply(instance, (mods, ic) -> new ModifySpeedOnItemUseConfiguration(mods, ic.orElse(null))));

}
