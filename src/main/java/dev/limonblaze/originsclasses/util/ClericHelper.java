package dev.limonblaze.originsclasses.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;

public class ClericHelper {
    
    public static void setPotionBonus(CompoundTag nbt, boolean flag) {
        CommonUtils.getOrCreateOriginsClassesTag(nbt).putBoolean(CommonUtils.POTION_BONUS, flag);
    }
    
    public static boolean getPotionBonus(CompoundTag nbt) {
        CompoundTag entry = CommonUtils.getOriginsClassesTag(nbt);
        return entry.getBoolean(CommonUtils.POTION_BONUS);
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
