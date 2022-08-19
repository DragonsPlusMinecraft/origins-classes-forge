package dev.limonblaze.originsclasses.common.apoli.power;

import dev.limonblaze.originsclasses.util.CommonUtils;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import dev.limonblaze.originsclasses.common.apoli.configuration.ModifyBreedingConfiguration;
import dev.limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

public class ModifyBreedingPower extends ValueModifyingPowerFactory<ModifyBreedingConfiguration> {

    public ModifyBreedingPower() {
        super(ModifyBreedingConfiguration.CODEC);
    }
    
    public static int getBreedAmount(Player player, Animal actor, Animal target) {
        return CommonUtils.rollInt(IPowerContainer.modify(player, OriginsClassesPowers.MODIFY_BREEDING.get(), 1.0D,
            cp ->
                cp.get().isActive(player) &&
                ConfiguredBiEntityCondition.check(cp.get().getConfiguration().parentsCondition(), actor, target),
            cp -> {
                ConfiguredBiEntityAction.execute(cp.get().getConfiguration().parentsAction(), actor, target);
                ConfiguredEntityAction.execute(cp.get().getConfiguration().playerAction(), player);
            }), player.getRandom());
    }

}