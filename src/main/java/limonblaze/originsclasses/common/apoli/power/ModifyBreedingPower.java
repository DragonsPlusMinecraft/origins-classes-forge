package limonblaze.originsclasses.common.apoli.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.IValueModifyingPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import limonblaze.originsclasses.common.apoli.configuration.ModifyBreedingConfiguration;
import limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import limonblaze.originsclasses.util.PowerUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;
import java.util.List;

public class ModifyBreedingPower extends PowerFactory<ModifyBreedingConfiguration> implements IValueModifyingPower<ModifyBreedingConfiguration> {

    public ModifyBreedingPower() {
        super(ModifyBreedingConfiguration.CODEC);
    }
    
    public static int getBreedAmount(Player player, Animal actor, Animal target) {
        return PowerUtil.naturalRandFloor(IPowerContainer.modify(player, OriginsClassesPowers.MODIFY_BREEDING.get(), 1.0D,
            cp -> cp.isActive(player) && ConfiguredBiEntityCondition.check(cp.getConfiguration().parentsCondition(), actor, target),
            cp -> {
                ConfiguredBiEntityAction.execute(cp.getConfiguration().parentsAction(), actor, target);
                ConfiguredEntityAction.execute(cp.getConfiguration().playerAction(), player);
            }), player.getRandom());
    }

    @Override
    public @Nonnull List<AttributeModifier> getModifiers(ConfiguredPower<ModifyBreedingConfiguration, ?> configuredPower, Entity entity) {
        return configuredPower.getConfiguration().modifiers().getContent();
    }

}