package limonblaze.originsclasses.common.apoli.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.IValueModifyingPowerConfiguration;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import javax.annotation.Nullable;
import java.util.Optional;

public record ModifyBreedingConfiguration(ListConfiguration<AttributeModifier> modifiers,
                                          @Nullable ConfiguredBiEntityCondition<?, ?> parentsCondition,
                                          @Nullable ConfiguredBiEntityAction<?, ?> parentsAction,
                                          @Nullable ConfiguredEntityAction<?, ?> playerAction) implements IValueModifyingPowerConfiguration {

    public static final Codec<ModifyBreedingConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ListConfiguration.modifierCodec("modifier")
            .forGetter(ModifyBreedingConfiguration::modifiers),
        CalioCodecHelper.optionalField(ConfiguredBiEntityCondition.CODEC, "parents_condition")
            .forGetter(x -> Optional.ofNullable(x.parentsCondition)),
        CalioCodecHelper.optionalField(ConfiguredBiEntityAction.CODEC, "parents_action")
            .forGetter(x -> Optional.ofNullable(x.parentsAction)),
        CalioCodecHelper.optionalField(ConfiguredEntityAction.CODEC, "player_action")
            .forGetter(x -> Optional.ofNullable(x.playerAction))
    ).apply(instance, (mods, psc, psa, pa) -> new ModifyBreedingConfiguration(mods, psc.orElse(null), psa.orElse(null), pa.orElse(null))));

}