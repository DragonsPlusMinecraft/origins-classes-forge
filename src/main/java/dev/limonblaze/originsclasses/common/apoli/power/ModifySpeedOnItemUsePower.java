package dev.limonblaze.originsclasses.common.apoli.power;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.power.ValueModifyingPowerFactory;
import dev.limonblaze.originsclasses.common.apoli.configuration.ModifySpeedOnItemUseConfiguration;
import dev.limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class ModifySpeedOnItemUsePower extends ValueModifyingPowerFactory<ModifySpeedOnItemUseConfiguration> {

    public ModifySpeedOnItemUsePower() {
        super(ModifySpeedOnItemUseConfiguration.CODEC);
    }

    public static float modifySlowDown(Player player, float slowdownMultiplier) {
        ItemStack stack = player.getItemInHand(player.getUsedItemHand());
        return Mth.clamp(IPowerContainer.modify(player, OriginsClassesPowers.MODIFY_SPEED_ON_ITEM_USE.get(), slowdownMultiplier,
            cp -> ConfiguredItemCondition.check(cp.get().getConfiguration().itemCondition(), player.level, stack)), 0.0F, 1.0F);
    }

    @Override
    public @Nonnull List<AttributeModifier> getModifiers(ConfiguredPower<ModifySpeedOnItemUseConfiguration, ?> configuredPower, Entity entity) {
        return configuredPower.getConfiguration().modifiers().getContent();
    }

}
