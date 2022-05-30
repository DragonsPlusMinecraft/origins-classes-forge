package limonblaze.originsclasses.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

public class PotionUtils {

    public static boolean hasPotionBonus(ItemStack stack) {
        return NbtUtils.getOriginsClassesData(stack, NbtUtils.POTION_BONUS, NbtType.BYTE, (byte)0) != 0;
    }

    public static boolean hasPotionBonus(CompoundTag nbt) {
        return NbtUtils.getOriginsClassesData(nbt, NbtUtils.POTION_BONUS, NbtType.BYTE, (byte)0) != 0;
    }

    public static ItemStack addPotionBonus(ItemStack stack) {
        stack.getOrCreateTagElement(NbtUtils.ORIGINS_CLASSES).putBoolean(NbtUtils.POTION_BONUS, true);
        return stack;
    }

    public static void addPotionBonus(CompoundTag nbt) {
        CompoundTag ocNbt;
        if(!nbt.contains(NbtUtils.ORIGINS_CLASSES, Tag.TAG_COMPOUND)) {
            ocNbt = new CompoundTag();
            nbt.put(NbtUtils.ORIGINS_CLASSES, ocNbt);
        } else ocNbt = nbt.getCompound(NbtUtils.ORIGINS_CLASSES);
        ocNbt.putBoolean(NbtUtils.POTION_BONUS, true);
    }

    public static MobEffectInstance applyPotionBonus(MobEffectInstance effect) {
        boolean instant = effect.getEffect().isInstantenous();
        return new MobEffectInstance(
            effect.getEffect(),
            instant ? effect.getDuration() : effect.getDuration() * 2,
            instant ? effect.getAmplifier() + 1 : effect.getAmplifier(),
            effect.isAmbient(),
            effect.isVisible(),
            effect.showIcon()
        );
    }

}
