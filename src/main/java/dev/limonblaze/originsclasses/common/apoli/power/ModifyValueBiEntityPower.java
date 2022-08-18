package dev.limonblaze.originsclasses.common.apoli.power;

import io.github.edwinmindcraft.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import dev.limonblaze.originsclasses.common.apoli.configuration.ModifyValueBiEntityConfiguration;

public class ModifyValueBiEntityPower extends ValueModifyingPowerFactory<ModifyValueBiEntityConfiguration> {

    public ModifyValueBiEntityPower() {
        super(ModifyValueBiEntityConfiguration.CODEC);
    }

}
