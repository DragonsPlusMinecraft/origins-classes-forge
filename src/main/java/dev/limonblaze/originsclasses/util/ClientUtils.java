package dev.limonblaze.originsclasses.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

public class ClientUtils {
    
    public static MutableComponent translate(String key, Object... args) {
        return MutableComponent.create(new TranslatableContents(key, args));
    }
    
    public static Component modifierTooltip(AttributeModifier modifier, String translationKey) {
        double value = modifier.getAmount() * (modifier.getOperation() == AttributeModifier.Operation.ADDITION ? 1 : 100);
        if (value > 0.0D) {
            return translate(
                "attribute.modifier.plus." + modifier.getOperation().toValue(),
                ATTRIBUTE_MODIFIER_FORMAT.format(value),
                translate(translationKey)
            ).withStyle(ChatFormatting.BLUE);
        } else {
            value *= -1.0D;
            return translate(
                "attribute.modifier.take." + modifier.getOperation().toValue(),
                ATTRIBUTE_MODIFIER_FORMAT.format(value),
                translate(translationKey)
            ).withStyle(ChatFormatting.RED);
        }
    }
    
}
