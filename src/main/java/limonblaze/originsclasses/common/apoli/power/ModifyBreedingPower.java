package limonblaze.originsclasses.common.apoli.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import limonblaze.originsclasses.common.apoli.configuration.ModifyBreedingConfiguration;
import limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import limonblaze.originsclasses.util.MathUtils;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

public class ModifyBreedingPower extends ValueModifyingPowerFactory<ModifyBreedingConfiguration> {

    public ModifyBreedingPower() {
        super(ModifyBreedingConfiguration.CODEC);
    }
    
    public static int getBreedAmount(Player player, Animal actor, Animal target) {
        return MathUtils.naturalRandFloor(IPowerContainer.modify(player, OriginsClassesPowers.MODIFY_BREEDING.get(), 1.0D,
            cp -> cp.isActive(player) && ConfiguredBiEntityCondition.check(cp.getConfiguration().parentsCondition(), actor, target),
            cp -> {
                ConfiguredBiEntityAction.execute(cp.getConfiguration().parentsAction(), actor, target);
                ConfiguredEntityAction.execute(cp.getConfiguration().playerAction(), player);
            }), player.getRandom());
    }

}