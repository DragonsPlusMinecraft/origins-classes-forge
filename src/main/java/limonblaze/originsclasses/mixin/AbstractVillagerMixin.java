package limonblaze.originsclasses.mixin;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import limonblaze.originsclasses.common.data.tag.OriginsClassesItemTags;
import limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import limonblaze.originsclasses.util.MerchantUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Mixin(AbstractVillager.class)
public abstract class AbstractVillagerMixin extends AgeableMob {

    @Shadow
    protected MerchantOffers offers;
    @Shadow
    private Player tradingPlayer;

    private int offerCountWithoutAdditional;
    private MerchantOffers additionalOffers;

    protected AbstractVillagerMixin(EntityType<? extends AgeableMob> type, Level world) {
        super(type, world);
    }

    @Inject(method = "notifyTrade", at = @At("TAIL"))
    private void originsClasses$infiniteTrade(MerchantOffer offer, CallbackInfo ci) {
        if(IPowerContainer.hasPower(tradingPlayer, OriginsClassesPowers.INFINITE_TRADE.get())) --offer.uses;
    }

    @Inject(method = "setTradingPlayer", at = @At("HEAD"))
    private void originsClasses$addAdditionalOffers(Player customer, CallbackInfo ci) {
        if(((Object)this) instanceof WanderingTrader) {
            if (IPowerContainer.hasPower(customer, OriginsClassesPowers.RARE_WANDERING_LOOT.get())) {
                if(additionalOffers == null) {
                    offerCountWithoutAdditional = offers.size();
                    additionalOffers = originsClasses$buildAdditionalOffers();
                }
                this.offers.addAll(additionalOffers);
            } else if(additionalOffers != null) {
                while(this.offers.size() > offerCountWithoutAdditional) {
                    this.offers.remove(this.offers.size() - 1);
                }
            }
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void originsClasses$writeAdditionalOffersToTag(CompoundTag tag, CallbackInfo ci) {
        if(additionalOffers != null) {
            tag.put("AdditionalOffers", additionalOffers.createTag());
            tag.putInt("OfferCountNoAdditional", offerCountWithoutAdditional);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void originsClasses$readAdditionalOffersFromTag(CompoundTag tag, CallbackInfo ci) {
        if(tag.contains("AdditionalOffers")) {
            additionalOffers = new MerchantOffers(tag.getCompound("AdditionalOffers"));
            offerCountWithoutAdditional = tag.getInt("OfferCountNoAdditional");
        }
    }

    private MerchantOffers originsClasses$buildAdditionalOffers() {
        MerchantOffers list = new MerchantOffers();
        Random random = new Random();
        Set<Item> excludedItems = new HashSet<>(OriginsClassesItemTags.MERCHANT_BLACKLIST.getValues());
        list.add(new MerchantOffer(
            new ItemStack(Items.EMERALD, random.nextInt(12) + 6),
            MerchantUtils.createMerchantItemStack(MerchantUtils.getRandomObtainableItem(
                this.getServer(),
                random,
                excludedItems), random),
            1,
            5,
            0.05F)
        );
        Item desiredItem = MerchantUtils.getRandomObtainableItem(
            this.getServer(),
            random,
            excludedItems);
        list.add(new MerchantOffer(
            new ItemStack(desiredItem, 1 + random.nextInt(Math.min(16, desiredItem.getDefaultInstance().getMaxStackSize()))),
            MerchantUtils.createMerchantItemStack(MerchantUtils.getRandomObtainableItem(
                this.getServer(),
                random,
                excludedItems), random),
            1,
            5,
            0.05F)
        );
        return list;
    }

}
