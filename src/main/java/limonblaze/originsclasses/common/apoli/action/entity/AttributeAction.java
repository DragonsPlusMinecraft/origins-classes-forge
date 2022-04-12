package limonblaze.originsclasses.common.apoli.action.entity;

import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.edwinmindcraft.apoli.common.power.configuration.AttributeConfiguration;
import net.minecraft.world.entity.Entity;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AttributeAction extends EntityAction<AttributeConfiguration> {

    public AttributeAction() {
        super(AttributeConfiguration.CODEC);
    }

    public void execute(AttributeConfiguration config, Entity entity) {
        config.add(entity);
    }

}
