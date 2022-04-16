package limonblaze.originsclasses.util;

import limonblaze.originsclasses.compat.OriginsClassesCompat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import shadows.apotheosis.Apotheosis;

public class ClericUtils {

    public static byte getPotionBonus(ItemStack stack) {
        return NbtUtils.getOriginsClassesData(stack, NbtUtils.POTION_BONUS, NbtType.BYTE, (byte)-1);
    }

    public static byte getPotionBonus(CompoundTag nbt) {
        return NbtUtils.getOriginsClassesData(nbt, NbtUtils.POTION_BONUS, NbtType.BYTE, (byte)-1);
    }

    public static ItemStack setPotionBonus(ItemStack stack, byte bonus) {
        stack.getOrCreateTagElement(NbtUtils.ORIGINS_CLASSES).putByte(NbtUtils.POTION_BONUS, bonus);
        return stack;
    }

    public static CompoundTag setPotionBonus(CompoundTag tag, byte bonus) {
        if(!tag.contains(NbtUtils.ORIGINS_CLASSES, Tag.TAG_COMPOUND)) {
            tag.put(NbtUtils.ORIGINS_CLASSES, new CompoundTag());
        }
        tag.getCompound(NbtUtils.ORIGINS_CLASSES).putByte(NbtUtils.POTION_BONUS, bonus);
        return tag;
    }

    public static MobEffectInstance applyPotionBonus(MobEffectInstance effect, byte bonusLevel) {
        boolean instant = effect.getEffect().isInstantenous();
        return new MobEffectInstance(
            effect.getEffect(),
            instant ? effect.getDuration() : effect.getDuration() * (1 + bonusLevel),
            instant ? effect.getAmplifier() + bonusLevel : effect.getAmplifier(),
            effect.isAmbient(),
            effect.isVisible(),
            effect.showIcon()
        );
    }

}
