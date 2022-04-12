package limonblaze.originsclasses.common.apoli.power;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;

public class PotionBonusPower extends PowerFactory<FieldConfiguration<Byte>> {

    public PotionBonusPower() {
        super(FieldConfiguration.codec(Codec.BYTE, "level"));
    }

}
