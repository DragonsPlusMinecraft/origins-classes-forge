package limonblaze.originsclasses.mixin;

import limonblaze.originsclasses.util.PowerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Arrow.class)
public class ArrowMixin {

    private byte originsClass$potionBonus;

    @Inject(method = "setEffectsFromItem", at = @At("TAIL"))
    private void originsClasses$initFromAdditionalPotionNbt(ItemStack arrow, CallbackInfo ci) {
        byte bonus = PowerUtil.getPotionBonus(arrow);
        if(bonus > 0) {
            this.originsClass$potionBonus = bonus;
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void originsClasses$writeAdditionalPotionNbt(CompoundTag nbt, CallbackInfo ci){
        if(originsClass$potionBonus > 0) PowerUtil.setPotionBonus(nbt, originsClass$potionBonus);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void originsClasses$readAdditionalPotionNbt(CompoundTag nbt, CallbackInfo ci){
        originsClass$potionBonus = PowerUtil.getPotionBonus(nbt);
    }

    @Inject(method = "getPickupItem", at = @At("RETURN"), cancellable = true)
    private void originsClasses$storeAdditionalPotionNbt(CallbackInfoReturnable<ItemStack> cir) {
        if(originsClass$potionBonus > 0) cir.setReturnValue(PowerUtil.setPotionBonus(cir.getReturnValue(), originsClass$potionBonus));
    }

    @ModifyArg(method = "doPostHurtEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"), index = 0)
    private MobEffectInstance originsClasses$handlePotionBonus(MobEffectInstance effect) {
        return originsClass$potionBonus > 0 ? PowerUtil.applyPotionBonus(effect, originsClass$potionBonus) : effect;
    }

}
