package limonblaze.originsclasses.common.apoli.power;

import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.common.power.ResourcePower;
import io.github.edwinmindcraft.apoli.common.power.configuration.ResourceConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class SneakCountingPower extends ResourcePower {

    @Override
    public boolean canTick(ConfiguredPower<ResourceConfiguration, ?> configuration, Entity entity) {
        return true;
    }

    @Override
    public void tick(ConfiguredPower<ResourceConfiguration, ?> cp, Entity entity) {
        if(entity instanceof Player) {
            ResourceConfiguration rc = cp.getConfiguration();
            if(entity.isShiftKeyDown()) {
                this.increment(cp, entity);
            } else {
                this.assign(cp, entity, rc.initialValue());
            }
            ApoliAPI.synchronizePowerContainer(entity);
        }
    }

}
