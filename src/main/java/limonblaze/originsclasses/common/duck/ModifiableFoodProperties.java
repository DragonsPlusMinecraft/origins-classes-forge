package limonblaze.originsclasses.common.duck;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;

import java.util.List;

public interface ModifiableFoodProperties {
    
    FoodProperties getModifiedFoodProperties(List<AttributeModifier> hungerModifiers,
                                             List<AttributeModifier> saturationModifiers);
    
}
