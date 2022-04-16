package limonblaze.originsclasses.common.apoli.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record ModifyBreedingConfiguration(ListConfiguration<AttributeModifier> modifiers,
                                          Holder<ConfiguredBiEntityCondition<?, ?>> parentsCondition,
                                          Holder<ConfiguredBiEntityAction<?, ?>> parentsAction,
                                          Holder<ConfiguredEntityAction<?, ?>> playerAction) implements IValueModifyingPowerConfiguration {

    public static final Codec<ModifyBreedingConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ListConfiguration.modifierCodec("modifier")
            .forGetter(ModifyBreedingConfiguration::modifiers),
        ConfiguredBiEntityCondition.optional("parents_condition")
            .forGetter(ModifyBreedingConfiguration::parentsCondition),
        ConfiguredBiEntityAction.optional("parents_action")
            .forGetter(ModifyBreedingConfiguration::parentsAction),
        ConfiguredEntityAction.optional("player_action")
            .forGetter(ModifyBreedingConfiguration::playerAction)
    ).apply(instance, ModifyBreedingConfiguration::new));

}