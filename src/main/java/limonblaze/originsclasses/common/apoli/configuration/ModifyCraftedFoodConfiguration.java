package limonblaze.originsclasses.common.apoli.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.*;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record ModifyCraftedFoodConfiguration(ListConfiguration<AttributeModifier> foodModifiers,
                                             ListConfiguration<AttributeModifier> saturationModifiers,
                                             Holder<ConfiguredItemCondition<?, ?>> itemCondition,
                                             Holder<ConfiguredEntityCondition<?, ?>> entityCondition) implements IDynamicFeatureConfiguration {
    
    public static final Codec<ModifyCraftedFoodConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ListConfiguration.modifierCodec("food_modifier")
            .forGetter(ModifyCraftedFoodConfiguration::foodModifiers),
        ListConfiguration.modifierCodec("saturation_modifier")
            .forGetter(ModifyCraftedFoodConfiguration::saturationModifiers),
        ConfiguredItemCondition.optional("item_condition")
            .forGetter(ModifyCraftedFoodConfiguration::itemCondition),
        ConfiguredEntityCondition.optional("entity_condition")
            .forGetter(ModifyCraftedFoodConfiguration::entityCondition)
    ).apply(instance, ModifyCraftedFoodConfiguration::new));
    
}
