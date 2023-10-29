package dev.limonblaze.originsclasses.common.apoli.condition.item;

import java.util.Arrays;

import javax.annotation.Nullable;

import dev.limonblaze.originsclasses.common.apoli.configuration.HasPowerConditionConfiguration;
import io.github.apace100.apoli.util.StackPowerUtil;
import io.github.apace100.apoli.util.StackPowerUtil.StackPower;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HasPowerContition extends ItemCondition<HasPowerConditionConfiguration> {

    public HasPowerContition() {
        super(HasPowerConditionConfiguration.CODEC);
    }

    @Override
    protected boolean check(HasPowerConditionConfiguration configuration, @Nullable Level level, ItemStack stack) {
    	for (EquipmentSlot equipment : configuration.equipment().isEmpty() ? Arrays.asList(EquipmentSlot.values()) : configuration.equipment()) {
    		for (StackPower power : StackPowerUtil.getPowers(stack, equipment)) {
        		if (power.powerId.equals(configuration.powerType().power())) {
    				return true;
    			}
    		}
    	}
        return false;
    }

}
