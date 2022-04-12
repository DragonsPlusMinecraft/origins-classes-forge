package limonblaze.originsclasses.common.apoli.power;

import io.github.edwinmindcraft.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import limonblaze.originsclasses.common.apoli.configuration.ModifyValueBiEntityConfiguration;

public class ModifyValueBiEntityPower extends ValueModifyingPowerFactory<ModifyValueBiEntityConfiguration> {

    public ModifyValueBiEntityPower() {
        super(ModifyValueBiEntityConfiguration.CODEC);
    }

}
