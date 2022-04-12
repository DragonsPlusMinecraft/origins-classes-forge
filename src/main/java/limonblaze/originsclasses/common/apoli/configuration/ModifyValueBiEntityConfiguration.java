package limonblaze.originsclasses.common.apoli.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ModifyValueBiEntityConfiguration(ListConfiguration<AttributeModifier> modifiers,
                                               @Nullable ConfiguredBiEntityCondition<?, ?> condition) implements IValueModifyingPowerConfiguration {

    public static final Codec<ModifyValueBiEntityConfiguration> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        ListConfiguration.MODIFIER_CODEC
            .forGetter(ModifyValueBiEntityConfiguration::modifiers),
        CalioCodecHelper.optionalField(ConfiguredBiEntityCondition.CODEC, "bientity_condition")
            .forGetter(x -> Optional.ofNullable(x.condition))
    ).apply(instance, (mods, bec) -> new ModifyValueBiEntityConfiguration(mods, bec.orElse(null))));

}
