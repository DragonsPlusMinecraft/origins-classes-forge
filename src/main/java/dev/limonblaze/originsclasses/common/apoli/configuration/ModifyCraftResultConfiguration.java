package dev.limonblaze.originsclasses.common.apoli.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.limonblaze.originsclasses.common.event.ModifyCraftResultEvent;
import dev.limonblaze.originsclasses.common.calio.OriginsClassesDataTypes;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.core.Holder;

import java.util.EnumSet;

public record ModifyCraftResultConfiguration(EnumSet<ModifyCraftResultEvent.CraftingResultType> craftingResultTypes,
                                             Holder<ConfiguredItemCondition<?, ?>> itemCondition,
                                             Holder<ConfiguredItemAction<?, ?>> itemAction) implements IDynamicFeatureConfiguration {
    
    public static final Codec<ModifyCraftResultConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        CalioCodecHelper.optionalField(OriginsClassesDataTypes.CRAFTING_RESULT_TYPE_SET, "crafting_result_type", EnumSet.allOf(ModifyCraftResultEvent.CraftingResultType.class))
            .forGetter(ModifyCraftResultConfiguration::craftingResultTypes),
        ConfiguredItemCondition.optional("item_condition")
            .forGetter(ModifyCraftResultConfiguration::itemCondition),
        ConfiguredItemAction.optional("item_action")
            .forGetter(ModifyCraftResultConfiguration::itemAction)
    ).apply(instance, ModifyCraftResultConfiguration::new));
    
}
