package limonblaze.originsclasses.mixin;

import limonblaze.originsclasses.util.PowerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(PotionUtils.class)
public class PotionUtilMixin {

    @Inject(method = "getAllEffects(Lnet/minecraft/nbt/CompoundTag;)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private static void originsClasses$modifyPotion(@Nullable CompoundTag nbt, CallbackInfoReturnable<List<MobEffectInstance>> cir) {
        byte bonusLevel = PowerUtil.getPotionBonus(nbt);
        if(bonusLevel > 0) {
            cir.setReturnValue(cir.getReturnValue().stream().map(effect -> PowerUtil.applyPotionBonus(effect, bonusLevel)).collect(Collectors.toList()));
        }
    }

}
