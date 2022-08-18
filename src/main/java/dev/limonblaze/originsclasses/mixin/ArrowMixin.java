package dev.limonblaze.originsclasses.mixin;

import dev.limonblaze.originsclasses.util.PotionUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Arrow.class)
public class ArrowMixin {

    @Unique private boolean hasPotionBonus;

    @Inject(method = "setEffectsFromItem", at = @At("TAIL"))
    private void originsClasses$initFromAdditionalPotionNbt(ItemStack arrow, CallbackInfo ci) {
        this.hasPotionBonus = PotionUtils.hasPotionBonus(arrow);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void originsClasses$writeAdditionalPotionNbt(CompoundTag nbt, CallbackInfo ci){
        if(hasPotionBonus) PotionUtils.addPotionBonus(nbt);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void originsClasses$readAdditionalPotionNbt(CompoundTag nbt, CallbackInfo ci){
        hasPotionBonus = PotionUtils.hasPotionBonus(nbt);
    }

    @Inject(method = "getPickupItem", at = @At("RETURN"), cancellable = true)
    private void originsClasses$storeAdditionalPotionNbt(CallbackInfoReturnable<ItemStack> cir) {
        if(hasPotionBonus) cir.setReturnValue(PotionUtils.addPotionBonus(cir.getReturnValue()));
    }

    @ModifyArg(method = "doPostHurtEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"), index = 0)
    private MobEffectInstance originsClasses$handlePotionBonus(MobEffectInstance effect) {
        return hasPotionBonus ? PotionUtils.applyPotionBonus(effect) : effect;
    }

}
