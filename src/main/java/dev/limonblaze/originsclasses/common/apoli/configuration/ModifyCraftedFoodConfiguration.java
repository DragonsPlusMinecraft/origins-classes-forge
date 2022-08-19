package dev.limonblaze.originsclasses.common.apoli.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.limonblaze.originsclasses.common.event.ModifyCraftResultEvent;
import dev.limonblaze.originsclasses.common.calio.OriginsClassesDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.PowerReference;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.Holder;

import java.util.EnumSet;

public record ModifyCraftedFoodConfiguration(PowerReference modifyFoodPower,
                                             EnumSet<ModifyCraftResultEvent.CraftingResultType> craftingResultTypes,
                                                Holder<ConfiguredItemCondition<?, ?>>itemCondition) implements IDynamicFeatureConfiguration {
    
    public static final Codec<ModifyCraftedFoodConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        PowerReference.mapCodec("modify_food_power")
            .forGetter(ModifyCraftedFoodConfiguration::modifyFoodPower),
        CalioCodecHelper.optionalField(OriginsClassesDataTypes.CRAFTING_RESULT_TYPE_SET, "crafting_result_type", EnumSet.allOf(ModifyCraftResultEvent.CraftingResultType.class))
            .forGetter(ModifyCraftedFoodConfiguration::craftingResultTypes),
        ConfiguredItemCondition.optional("item_condition")
            .forGetter(ModifyCraftedFoodConfiguration::itemCondition)
    ).apply(instance, ModifyCraftedFoodConfiguration::new));
    
}
