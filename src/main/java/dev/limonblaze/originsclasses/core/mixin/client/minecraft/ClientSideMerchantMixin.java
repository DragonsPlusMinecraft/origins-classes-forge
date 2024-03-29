package dev.limonblaze.originsclasses.core.mixin.client.minecraft;

import dev.limonblaze.originsclasses.OriginsClasses;
import net.minecraft.world.entity.npc.ClientSideMerchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(ClientSideMerchant.class)
public class ClientSideMerchantMixin {

    @Inject(method = "notifyTrade", at = @At(value = "TAIL"))
    private void originsClasses$infiniteTrade(MerchantOffer offer, CallbackInfo ci) {
        if(OriginsClasses.INFINITE_TRADER) --offer.uses;
    }

}
