package dev.limonblaze.originsclasses.core.mixin.common.minecraft;

import dev.limonblaze.originsclasses.util.ClericHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(net.minecraft.world.item.alchemy.PotionUtils.class)
public class PotionUtilMixin {

    @Inject(method = "getAllEffects(Lnet/minecraft/nbt/CompoundTag;)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private static void originsClasses$modifyPotion(@Nullable CompoundTag nbt, CallbackInfoReturnable<List<MobEffectInstance>> cir) {
        if(nbt != null) {
            if(ClericHelper.getPotionBonus(nbt)) {
                cir.setReturnValue(cir.getReturnValue().stream().map(ClericHelper::applyPotionBonus).collect(Collectors.toList()));
            }
        }
    }

}
