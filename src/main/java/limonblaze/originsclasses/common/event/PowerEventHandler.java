package limonblaze.originsclasses.common.event;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBiEntityCondition;
import limonblaze.originsclasses.common.OriginsClassesCommon;
import limonblaze.originsclasses.common.apoli.power.ActionOnTamePower;
import limonblaze.originsclasses.common.apoli.power.MultiMinePower;
import limonblaze.originsclasses.common.data.tag.OriginsClassesEntityTypeTags;
import limonblaze.originsclasses.common.network.S2CInfiniteTrader;
import limonblaze.originsclasses.common.registry.OriginsClassesAttributes;
import limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import limonblaze.originsclasses.mixin.accessor.LivingEntityAccessor;
import limonblaze.originsclasses.util.PowerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
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

@Mod.EventBusSubscriber
public class PowerEventHandler {
    public static final String FOOD_BONUS_TRANSLATION_KEY = "tooltip.origins_classes.food_bonus";
    public static final String POTION_BONUS_TRANSLATION_KEY = "tooltip.origins_classes.potion_bonus";

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

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEnchantmentLevel(EnchantmentLevelSetEvent event) {
        ItemStack stack = event.getItem();
        if(stack.hasTag()) {
            CompoundTag nbt = stack.getOrCreateTag();
            if(nbt.hasUUID(PowerUtil.ORIGINS_CLASSES_ENCHANTER)) {
                Player player = event.getWorld().getPlayerByUUID(nbt.getUUID(PowerUtil.ORIGINS_CLASSES_ENCHANTER));
                if(player != null) {
                    event.setLevel(Mth.floor(IPowerContainer.modify(player, OriginsClassesPowers.MODIFY_ENCHANTING_LEVEL.get(), event.getLevel(), cp -> cp.isActive(player))));
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBoneMeal(BonemealEvent event) {
        int count = PowerUtil.naturalRandFloor(IPowerContainer.modify(event.getPlayer(), OriginsClassesPowers.MODIFY_BONE_MEAL.get(), 1.0D), event.getPlayer().getRandom());
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

    @SubscribeEvent
    public static void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
        if(event.getPlayer() instanceof ServerPlayer sp) {
            if(OriginsClassesEntityTypeTags.INFINITE_TRADER.contains(event.getTarget().getType()) &&
                IPowerContainer.hasPower(sp, OriginsClassesPowers.INFINITE_TRADE.get())) {
                OriginsClassesCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), new S2CInfiniteTrader(true));
            } else {
                OriginsClassesCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), new S2CInfiniteTrader(false));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        double multiplier = event.getPlayer().getAttributeValue(OriginsClassesAttributes.MINING_SPEED.get());
        event.setNewSpeed(MultiMinePower.modifyBreakingSpeed((float) (event.getNewSpeed() * multiplier), event.getPlayer(), event.getPos(), event.getState()));
    }

    @SubscribeEvent
    public static void onArrowShoots(EntityJoinWorldEvent event) {
        if(event.getEntity() instanceof AbstractArrow arrow && arrow.getOwner() instanceof LivingEntity owner) {
            arrow.setBaseDamage(arrow.getBaseDamage() * owner.getAttributeValue(OriginsClassesAttributes.PROJECTILE_STRENGTH.get()));
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if(event.getSource() instanceof EntityDamageSource eds && eds.getEntity() instanceof Player player) {
            LivingEntity target = event.getEntityLiving();
            int amount = PowerUtil.naturalRandFloor(IPowerContainer.modify(player, OriginsClassesPowers.MODIFY_ENTITY_LOOT.get(), 1.0F, cp -> cp.isActive(player) && ConfiguredBiEntityCondition.check(cp.getConfiguration().condition(), player, target)), player.getRandom());
            for(int i = 1; i < amount; ++i) {
                ((LivingEntityAccessor)target).invokeDropFromLootTable(event.getSource(), true);
            }
        }
    }

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if(event.getFlags().isAdvanced()) {
            ItemStack stack = event.getItemStack();
            if(stack.hasTag() && stack.getOrCreateTag().contains(PowerUtil.ORIGINS_CLASSES, Tag.TAG_COMPOUND)) {
                CompoundTag bonus = stack.getOrCreateTagElement(PowerUtil.ORIGINS_CLASSES);
                List<Component> tooltip = event.getToolTip();
                if(bonus.contains(PowerUtil.FOOD_BONUS, Tag.TAG_FLOAT)) {
                    FoodProperties food = stack.getItem().getFoodProperties();
                    if(food != null) {
                        tooltip.add(new TranslatableComponent(FOOD_BONUS_TRANSLATION_KEY, Mth.floor(bonus.getFloat(PowerUtil.FOOD_BONUS) * food.getNutrition())).withStyle(ChatFormatting.BLUE));
                    }
                }
                if(bonus.contains(PowerUtil.POTION_BONUS, Tag.TAG_BYTE)) {
                    tooltip.add(new TranslatableComponent(POTION_BONUS_TRANSLATION_KEY, bonus.getByte(PowerUtil.POTION_BONUS)).withStyle(ChatFormatting.BLUE));
                }
            }
        }
    }

}