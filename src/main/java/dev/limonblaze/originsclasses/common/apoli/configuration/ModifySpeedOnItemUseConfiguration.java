package dev.limonblaze.originsclasses.common.apoli.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record ModifySpeedOnItemUseConfiguration(ListConfiguration<ConfiguredModifier<?>> modifiers,
                                                Holder<ConfiguredItemCondition<?, ?>> itemCondition) implements IValueModifyingPowerConfiguration {

    public static final Codec<ModifySpeedOnItemUseConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ListConfiguration.modifierCodec("modifier")
            .forGetter(ModifySpeedOnItemUseConfiguration::modifiers),
        ConfiguredItemCondition.optional("item_condition")
            .forGetter(ModifySpeedOnItemUseConfiguration::itemCondition)
    ).apply(instance, ModifySpeedOnItemUseConfiguration::new));

}
