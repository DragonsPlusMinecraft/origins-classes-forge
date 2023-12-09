package dev.limonblaze.originsclasses.common.apoli.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record ModifyValueBiEntityConfiguration(ListConfiguration<ConfiguredModifier<?>> modifiers,
                                               Holder<ConfiguredBiEntityCondition<?, ?>> condition) implements IValueModifyingPowerConfiguration {

    public static final Codec<ModifyValueBiEntityConfiguration> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        ListConfiguration.MODIFIER_CODEC
            .forGetter(ModifyValueBiEntityConfiguration::modifiers),
        ConfiguredBiEntityCondition.optional("bientity_condition")
            .forGetter(ModifyValueBiEntityConfiguration::condition)
    ).apply(instance, ModifyValueBiEntityConfiguration::new));

}
