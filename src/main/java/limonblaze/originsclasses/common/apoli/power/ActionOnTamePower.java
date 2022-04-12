package limonblaze.originsclasses.common.apoli.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import limonblaze.originsclasses.common.apoli.configuration.ActionOnTameConfiguration;
import limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import net.minecraft.world.entity.Entity;

public class ActionOnTamePower extends PowerFactory<ActionOnTameConfiguration> {

    public ActionOnTamePower() {
        super(ActionOnTameConfiguration.CODEC, false);
    }

    public static void apply(Entity player, Entity tameable) {
        IPowerContainer.getPowers(player, OriginsClassesPowers.ACTION_ON_TAME.get()).stream()
            .filter(cp -> cp.isActive(player) && ConfiguredBiEntityCondition.check(cp.getConfiguration().biEntityCondition(), player, tameable))
            .forEach(cp -> cp.getConfiguration().biEntityAction().execute(player, tameable));
    }

}
