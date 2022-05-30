package limonblaze.originsclasses.common.event;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import limonblaze.originsclasses.common.OriginsClassesCommon;
import limonblaze.originsclasses.common.apoli.power.ActionOnTamePower;
import limonblaze.originsclasses.common.apoli.power.ModifyCraftedFoodPower;
import limonblaze.originsclasses.common.apoli.power.MultiMinePower;
import limonblaze.originsclasses.common.data.tag.OriginsClassesEntityTypeTags;
import limonblaze.originsclasses.common.network.S2CInfiniteTrader;
import limonblaze.originsclasses.common.registry.OriginsClassesAttributes;
import limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import limonblaze.originsclasses.mixin.accessor.LivingEntityAccessor;
import limonblaze.originsclasses.util.MathUtils;
import limonblaze.originsclasses.util.NbtType;
import limonblaze.originsclasses.util.NbtUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Objects;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

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
    public static void onPotionGained(PotionEvent.PotionAddedEvent event) {
        MobEffectInstance effect = event.getPotionEffect();
        LivingEntity entity = event.getEntityLiving();
        if(!effect.isAmbient() && IPowerContainer.hasPower(entity, OriginsClassesPowers.TAMED_POTION_DIFFUSAL.get())) {
            entity.level.getEntitiesOfClass(LivingEntity.class,
                entity.getBoundingBox().expandTowards(8F, 2F, 8F).expandTowards(-8F, -2F, -8F),
                e -> e instanceof OwnableEntity ownable && Objects.equals(ownable.getOwnerUUID(), entity.getUUID())
            ).forEach(e -> e.addEffect(effect, event.getPotionSource() == null ? entity : event.getPotionSource()));
        }
    }

    //ModifyEnchantingLevel
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEnchantmentLevel(EnchantmentLevelSetEvent event) {
        NbtUtils.getOriginsClassesData(event.getItem(), NbtUtils.ENCHANTER, NbtType.UUID).ifPresent(uuid -> {
            Player player = event.getWorld().getPlayerByUUID(uuid);
            if(player != null) {
                event.setLevel(Mth.floor(IPowerContainer.modify(player, OriginsClassesPowers.MODIFY_ENCHANTING_LEVEL.get(), event.getLevel(),
                    cp -> cp.isActive(player)
                )));
            }
        });
    }

    //ModifyBoneMeal
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBoneMeal(BonemealEvent event) {
        int count = MathUtils.randomFloor(IPowerContainer.modify(event.getPlayer(), OriginsClassesPowers.MODIFY_BONE_MEAL.get(), 1.0D), event.getPlayer().getRandom());
        if(count == 0) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
        } else if(count > 1 && event.getBlock().getBlock() instanceof BonemealableBlock fertilizable) {
            Level world = event.getWorld();
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
        if(event.getPlayer() instanceof ServerPlayer sp) {
            if(event.getTarget().getType().is(OriginsClassesEntityTypeTags.INFINITE_TRADER) &&
                IPowerContainer.hasPower(sp, OriginsClassesPowers.INFINITE_TRADE.get())) {
                OriginsClassesCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), new S2CInfiniteTrader(true));
            } else {
                OriginsClassesCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), new S2CInfiniteTrader(false));
            }
        }
    }

    //Multimine & MiningSpeed
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        double multiplier = event.getPlayer().getAttributeValue(OriginsClassesAttributes.MINING_SPEED.get());
        event.setNewSpeed(MultiMinePower.modifyBreakingSpeed((float) (event.getNewSpeed() * multiplier), event.getPlayer(), event.getPos(), event.getState()));
    }

    //ProjectileStrength
    @SubscribeEvent
    public static void onArrowShoots(EntityJoinWorldEvent event) {
        if(event.getEntity() instanceof AbstractArrow arrow && arrow.getOwner() instanceof LivingEntity owner) {
            arrow.setBaseDamage(arrow.getBaseDamage() * owner.getAttributeValue(OriginsClassesAttributes.PROJECTILE_STRENGTH.get()));
        }
    }

    //ModifyEntityLoot
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if(event.getSource() instanceof EntityDamageSource eds && eds.getEntity() instanceof Player player) {
            LivingEntity target = event.getEntityLiving();
            int amount = MathUtils.randomFloor(IPowerContainer.modify(player, OriginsClassesPowers.MODIFY_ENTITY_LOOT.get(), 1.0F, cp -> cp.isActive(player) && ConfiguredBiEntityCondition.check(cp.getConfiguration().condition(), player, target)), player.getRandom());
            for(int i = 1; i < amount; ++i) {
                ((LivingEntityAccessor)target).invokeDropFromLootTable(event.getSource(), true);
            }
        }
    }
    
    //ModifyCraftedFood
    @SubscribeEvent
    public static void onFoodCrafted(ModifyCraftResultEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = event.getCrafted();
        if(stack.getFoodProperties(player) != null) {
            ModifyCraftedFoodPower.modifyCrafted(player, stack);
        }
    }

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if(event.getFlags().isAdvanced()) {
            ItemStack stack = event.getItemStack();
            List<Component> tooltip = event.getToolTip();
            if(stack.getFoodProperties(event.getPlayer()) != null) {
                ModifyCraftedFoodPower.getModifiers(stack, NbtUtils.FOOD_MODIFIERS)
                    .forEach(modifier -> tooltip.add(modifierTooltip(modifier, FOOD_TRANSLATION_KEY)));
                ModifyCraftedFoodPower.getModifiers(stack, NbtUtils.SATURATION_MODIFIERS)
                    .forEach(modifier -> tooltip.add(modifierTooltip(modifier, SATURATION_MODIFIER_TRANSLATION_KEY)));
            }
            if(stack.getItem() instanceof PotionItem || stack.getItem() instanceof TippedArrowItem) {
                NbtUtils.getOriginsClassesData(stack, NbtUtils.POTION_BONUS, NbtType.BYTE).ifPresent(b ->
                    tooltip.add(new TranslatableComponent(POTION_BONUS_TRANSLATION_KEY, b).withStyle(ChatFormatting.BLUE))
                );
            }
        }
    }
    
    private static Component modifierTooltip(AttributeModifier modifier, String translationKey) {
        double value = modifier.getAmount() * (modifier.getOperation() == AttributeModifier.Operation.ADDITION ? 1 : 100);
        if (value > 0.0D) {
            return (new TranslatableComponent(
                "attribute.modifier.plus." + modifier.getOperation().toValue(),
                ATTRIBUTE_MODIFIER_FORMAT.format(value),
                new TranslatableComponent(translationKey))
            ).withStyle(ChatFormatting.BLUE);
        } else {
            value *= -1.0D;
            return (new TranslatableComponent(
                "attribute.modifier.take." + modifier.getOperation().toValue(),
                ATTRIBUTE_MODIFIER_FORMAT.format(value),
                new TranslatableComponent(translationKey))
            ).withStyle(ChatFormatting.RED);
        }
    }

}