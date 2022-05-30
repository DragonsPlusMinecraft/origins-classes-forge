package limonblaze.originsclasses.mixin;

import com.mojang.datafixers.util.Pair;
import io.github.apace100.apoli.util.AttributeUtil;
import limonblaze.originsclasses.common.duck.ModifiableFoodProperties;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.function.Supplier;

@Mixin(FoodProperties.class)
public abstract class FoodPropertiesMixin implements ModifiableFoodProperties {
    
    @Shadow @Final private int nutrition;
    @Shadow @Final private float saturationModifier;
    @Shadow @Final private boolean isMeat;
    @Shadow @Final private boolean canAlwaysEat;
    @Shadow @Final private boolean fastFood;
    
    @Shadow @Final private List<Pair<Supplier<MobEffectInstance>, Float>> effects;
    
    @Override
    public FoodProperties getModifiedFoodProperties(List<AttributeModifier> nutritionModifiers, List<AttributeModifier> saturationModifiers) {
        FoodProperties.Builder builder = new FoodProperties.Builder();
        builder.nutrition(Mth.floor(AttributeUtil.applyModifiers(nutritionModifiers, nutrition)));
        builder.saturationMod((float) AttributeUtil.applyModifiers(nutritionModifiers, saturationModifier));
        if(isMeat) builder.meat();
        if(canAlwaysEat) builder.alwaysEat();
        if(fastFood) builder.fast();
        effects.forEach(entry -> builder.effect(entry.getFirst(), entry.getSecond()));
        return builder.build();
    }
    
}
