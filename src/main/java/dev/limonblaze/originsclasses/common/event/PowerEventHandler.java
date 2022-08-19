package dev.limonblaze.originsclasses.common.event;

import dev.limonblaze.originsclasses.common.apoli.power.ActionOnTamePower;
import dev.limonblaze.originsclasses.common.apoli.power.ModifyCraftResultPower;
import dev.limonblaze.originsclasses.common.apoli.power.ModifyCraftedFoodPower;
import dev.limonblaze.originsclasses.util.*;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.common.power.ModifyFoodPower;
import dev.limonblaze.originsclasses.common.OriginsClassesCommon;
import dev.limonblaze.originsclasses.common.network.S2CInfiniteTrader;
import dev.limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import dev.limonblaze.originsclasses.common.tag.OriginsClassesEntityTypeTags;
import dev.limonblaze.originsclasses.core.mixin.accessor.minecraft.LivingEntityAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber
public class PowerEventHandler {
    public static final String POTION_BONUS_TRANSLATION_KEY = "tooltip.origins_classes.potion_bonus";
    public static final String FOOD_TRANSLATION_KEY = "tooltip.origins_classes.food";
    public static final String SATURATION_MODIFIER_TRANSLATION_KEY = "tooltip.origins_classes.saturation_modifier";

    //ActionOnTame
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAnimalTame(AnimalTameEvent event) {
        ActionOnTamePower.apply(event.getTamer(), event.getAnimal());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBabyEntitySpawn(BabyEntitySpawnEvent event) {
        AgeableMob child = event.getChild();
        if(child != null &&
           event.getParentA() instanceof OwnableEntity a &&
           event.getParentB() instanceof OwnableEntity b &&
           a.getOwner() != null && Objects.equals(a.getOwnerUUID(), b.getOwnerUUID())) {
            ActionOnTamePower.apply(a.getOwner(), child);
        }
    }

    //TamedPotionDiffusal
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPotionGained(MobEffectEvent.Added event) {
        MobEffectInstance effect = event.getEffectInstance();
        LivingEntity owner = event.getEntity();
        Entity source = event.getEffectSource() == null ? owner : event.getEffectSource();
        if(!effect.isAmbient() && IPowerContainer.hasPower(owner, OriginsClassesPowers.TAMED_POTION_DIFFUSAL.get())) {
            owner.level.getEntitiesOfClass(LivingEntity.class,
                owner.getBoundingBox().expandTowards(8F, 2F, 8F).expandTowards(-8F, -2F, -8F),
                e -> e instanceof OwnableEntity ownable && Objects.equals(ownable.getOwnerUUID(), owner.getUUID())
            ).forEach(e -> e.addEffect(effect, source));
        }
    }

    //ModifyEnchantingLevel
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEnchantmentLevel(EnchantmentLevelSetEvent event) {
        ItemStack stack = event.getItem();
        if(!stack.hasTag()) return;
        CompoundTag entry = CommonUtils.getOriginsClassesTag(stack.getTag());
        if(!entry.contains(CommonUtils.ENCHANTER, Tag.TAG_INT_ARRAY)) return;
        Player player = event.getLevel().getPlayerByUUID(entry.getUUID(CommonUtils.ENCHANTER));
        if(player != null) {
            event.setEnchantLevel(Mth.floor(IPowerContainer.modify(
                player,
                OriginsClassesPowers.MODIFY_ENCHANTING_LEVEL.get(),
                event.getEnchantLevel(),
                cp -> cp.get().isActive(player)
            )));
        }
    }

    //ModifyBoneMeal
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBoneMeal(BonemealEvent event) {
        int count = CommonUtils.rollInt(IPowerContainer.modify(
            event.getEntity(),
            OriginsClassesPowers.MODIFY_BONE_MEAL.get(), 1.0D
        ), event.getEntity().getRandom());
        if(count == 0) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
        } else if(count > 1 && event.getBlock().getBlock() instanceof BonemealableBlock fertilizable) {
            Level world = event.getLevel();
            BlockPos pos = event.getPos();
            BlockState state = event.getBlock();
            if(world instanceof ServerLevel serverWorld && fertilizable.isValidBonemealTarget(world, pos, state, false)) {
                for(int i = 0; i < count - 1; i++) {
                    if(fertilizable.isBonemealSuccess(world, world.random, pos, state)) {
                        fertilizable.performBonemeal(serverWorld, world.random, pos, state);
                    }
                }
            }
        }
    }

    //InfiniteTrade
    @SubscribeEvent
    public static void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
        if(event.getEntity() instanceof ServerPlayer sp) {
            if(event.getTarget().getType().is(OriginsClassesEntityTypeTags.INFINITE_TRADER) &&
                IPowerContainer.hasPower(sp, OriginsClassesPowers.INFINITE_TRADE.get())) {
                OriginsClassesCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), new S2CInfiniteTrader(true));
            } else {
                OriginsClassesCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), new S2CInfiniteTrader(false));
            }
        }
    }

    //ModifyEntityLoot
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if(event.getSource() instanceof EntityDamageSource eds && eds.getEntity() instanceof Player player) {
            LivingEntity target = event.getEntity();
            int amount = CommonUtils.rollInt(IPowerContainer.modify(
                player,
                OriginsClassesPowers.MODIFY_ENTITY_LOOT.get(), 1.0F,
                cp -> cp.get().isActive(player) &&
                ConfiguredBiEntityCondition.check(cp.get().getConfiguration().condition(), player, target)
            ), player.getRandom());
            for(int i = 1; i < amount; ++i) {
                ((LivingEntityAccessor)target).invokeDropFromLootTable(event.getSource(), true);
            }
        }
    }
    
    //ModifyCraftedFood, ModifyCraftResult
    @SubscribeEvent
    public static void onItemCrafted(ModifyCraftResultEvent event) {
        Player player = event.getEntity();
        ItemStack stack = event.getCrafted();
        if(stack.getFoodProperties(player) != null) {
            stack = ModifyCraftedFoodPower.modify(player, stack, event.getType());
        }
        stack = ModifyCraftResultPower.modify(player, stack, event.getType());
        event.setCrafted(stack);
    }

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        Player player = event.getEntity();
        if(player == null) return;
        ItemStack stack = event.getItemStack();
        List<Component> tooltip = event.getToolTip();
        
        if(ClientConfig.CONFIG.showModifyFoodTooltip.get() &&
            stack.getFoodProperties(player) != null)
        {
            List<AttributeModifier> foodModifiers = new ArrayList<>();
            List<AttributeModifier> saturationModifiers = new ArrayList<>();
            ModifyFoodPower.getValidPowers(player, stack).stream()
                .map(ConfiguredPower::getConfiguration)
                .forEach(config -> {
                    foodModifiers.addAll(config.foodModifiers().entries());
                    saturationModifiers.addAll(config.saturationModifiers().entries());
                });
            foodModifiers.forEach(mod -> tooltip.add(ClientUtils.modifierTooltip(mod, FOOD_TRANSLATION_KEY)));
            saturationModifiers.forEach(mod -> tooltip.add(ClientUtils.modifierTooltip(mod, SATURATION_MODIFIER_TRANSLATION_KEY)));
        }
        
        if(ClientConfig.CONFIG.showPotionBonusTooltip.get() &&
            (stack.getItem() instanceof PotionItem || stack.getItem() instanceof TippedArrowItem) &&
            stack.hasTag() && ClericHelper.getPotionBonus(stack.getTag()))
        {
            tooltip.add(ClientUtils.translate(POTION_BONUS_TRANSLATION_KEY).withStyle(ChatFormatting.BLUE));
        }
    }
    
}