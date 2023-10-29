package dev.limonblaze.originsclasses.common.registry;

import static net.minecraftforge.common.ToolActions.AXE_DIG;
import static net.minecraftforge.common.ToolActions.AXE_SCRAPE;
import static net.minecraftforge.common.ToolActions.AXE_STRIP;
import static net.minecraftforge.common.ToolActions.AXE_WAX_OFF;
import static net.minecraftforge.common.ToolActions.HOE_DIG;
import static net.minecraftforge.common.ToolActions.HOE_TILL;
import static net.minecraftforge.common.ToolActions.PICKAXE_DIG;
import static net.minecraftforge.common.ToolActions.SHEARS_CARVE;
import static net.minecraftforge.common.ToolActions.SHEARS_DIG;
import static net.minecraftforge.common.ToolActions.SHEARS_DISARM;
import static net.minecraftforge.common.ToolActions.SHEARS_HARVEST;
import static net.minecraftforge.common.ToolActions.SHIELD_BLOCK;
import static net.minecraftforge.common.ToolActions.SHOVEL_DIG;
import static net.minecraftforge.common.ToolActions.SHOVEL_FLATTEN;
import static net.minecraftforge.common.ToolActions.SWORD_DIG;
import static net.minecraftforge.common.ToolActions.SWORD_SWEEP;

import dev.limonblaze.originsclasses.OriginsClasses;
import dev.limonblaze.originsclasses.common.apoli.condition.item.HasPowerContition;
import dev.limonblaze.originsclasses.common.apoli.condition.item.ToolActionCondition;
import dev.limonblaze.originsclasses.common.tag.OriginsClassesItemTags;
import dev.limonblaze.originsclasses.util.CommonUtils;
import io.github.edwinmindcraft.apoli.api.power.factory.BiEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.condition.bientity.DoubleComparingBiEntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.block.SimpleBlockCondition;
import io.github.edwinmindcraft.apoli.common.condition.entity.SimpleEntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.item.SimpleItemCondition;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class OriginsClassesConditions {

    public static final DeferredRegister<BiEntityCondition<?>> BIENTITY_CONDITIONS = DeferredRegister.create(ApoliRegistries.BIENTITY_CONDITION_KEY, OriginsClasses.MODID);
    public static final RegistryObject<DoubleComparingBiEntityCondition> YAW_DIFF = BIENTITY_CONDITIONS.register("yaw_diff", () ->
        new DoubleComparingBiEntityCondition(CommonUtils::calcYawDiff));

    public static final DeferredRegister<EntityCondition<?>> ENTITY_CONDITIONS = DeferredRegister.create(ApoliRegistries.ENTITY_CONDITION_KEY, OriginsClasses.MODID);
    public static final RegistryObject<SimpleEntityCondition> ANIMAL = ENTITY_CONDITIONS.register("animal", () ->
        new SimpleEntityCondition(entity -> entity instanceof Animal));

    public static final DeferredRegister<ItemCondition<?>> ITEM_CONDITIONS = DeferredRegister.create(ApoliRegistries.ITEM_CONDITION_KEY, OriginsClasses.MODID);
    public static final RegistryObject<SimpleItemCondition> DIGGERS = ITEM_CONDITIONS.register("diggers", () ->
        new SimpleItemCondition( stack ->
            stack.getItem() instanceof DiggerItem ||
            stack.is(OriginsClassesItemTags.DIGGERS) ||
            stack.canPerformAction(HOE_DIG) ||
            stack.canPerformAction(PICKAXE_DIG) ||
            stack.canPerformAction(AXE_DIG) ||
            stack.canPerformAction(SHOVEL_DIG) ||
            stack.canPerformAction(SHEARS_DIG)
        ));
    public static final RegistryObject<SimpleItemCondition> MELEE = ITEM_CONDITIONS.register("melee", () ->
        new SimpleItemCondition(stack ->
            Enchantments.SHARPNESS.canEnchant(stack) ||
            stack.is(OriginsClassesItemTags.MELEE_WEAPONS)
        ));
    public static final RegistryObject<SimpleItemCondition> RANGE = ITEM_CONDITIONS.register("range", () ->
        new SimpleItemCondition(stack ->
            stack.getItem() instanceof ProjectileWeaponItem ||
            stack.is(OriginsClassesItemTags.RANGE_WEAPONS)
        ));
    public static final RegistryObject<SimpleItemCondition> ARMORS = ITEM_CONDITIONS.register("armors", () ->
        new SimpleItemCondition(stack ->
            stack.getItem() instanceof ArmorItem ||
            stack.is(OriginsClassesItemTags.ARMORS)
        ));
    public static final RegistryObject<SimpleItemCondition> HOE = ITEM_CONDITIONS.register("hoe", () ->
        new SimpleItemCondition(stack ->
            stack.canPerformAction(HOE_DIG) ||
            stack.canPerformAction(HOE_TILL) ||
            stack.is(OriginsClassesItemTags.HOES)
        ));
    public static final RegistryObject<SimpleItemCondition> PICKAXE = ITEM_CONDITIONS.register("pickaxe", () ->
        new SimpleItemCondition(stack ->
            stack.canPerformAction(PICKAXE_DIG) ||
            stack.is(OriginsClassesItemTags.PICKAXES)
        ));
    public static final RegistryObject<SimpleItemCondition> AXE = ITEM_CONDITIONS.register("axe", () ->
        new SimpleItemCondition(stack ->
            stack.canPerformAction(AXE_DIG) ||
            stack.canPerformAction(AXE_STRIP) ||
            stack.canPerformAction(AXE_SCRAPE) ||
            stack.canPerformAction(AXE_WAX_OFF) ||
            stack.is(OriginsClassesItemTags.AXES)
        ));
    public static final RegistryObject<SimpleItemCondition> SHOVEL = ITEM_CONDITIONS.register("shovel", () ->
        new SimpleItemCondition(stack ->
            stack.canPerformAction(SHOVEL_DIG) ||
            stack.canPerformAction(SHOVEL_FLATTEN) ||
            stack.is(OriginsClassesItemTags.SHOVELS)
        ));
    public static final RegistryObject<SimpleItemCondition> SHEARS = ITEM_CONDITIONS.register("shears", () ->
        new SimpleItemCondition(stack ->
            stack.canPerformAction(SHEARS_DIG) ||
            stack.canPerformAction(SHEARS_CARVE) ||
            stack.canPerformAction(SHEARS_DISARM) ||
            stack.canPerformAction(SHEARS_HARVEST) ||
            stack.is(OriginsClassesItemTags.SHEARS)
        ));
    public static final RegistryObject<SimpleItemCondition> SWORD = ITEM_CONDITIONS.register("sword", () ->
        new SimpleItemCondition(stack ->
            stack.canPerformAction(SWORD_DIG) ||
            stack.canPerformAction(SWORD_SWEEP) ||
            stack.getItem() instanceof SwordItem ||
            stack.is(OriginsClassesItemTags.SWORDS)
        ));
    public static final RegistryObject<SimpleItemCondition> BOW = ITEM_CONDITIONS.register("bow", () ->
        new SimpleItemCondition(stack ->
            stack.getItem() instanceof BowItem ||
            stack.is(OriginsClassesItemTags.BOWS)
        ));
    public static final RegistryObject<SimpleItemCondition> CROSSBOW = ITEM_CONDITIONS.register("crossbow", () ->
        new SimpleItemCondition(stack ->
            stack.getItem() instanceof CrossbowItem ||
            stack.is(OriginsClassesItemTags.CROSSBOWS)
        ));
    public static final RegistryObject<SimpleItemCondition> SHIELD = ITEM_CONDITIONS.register("shield", () ->
        new SimpleItemCondition(stack ->
            stack.canPerformAction(SHIELD_BLOCK) ||
            stack.getItem() instanceof ShieldItem ||
            stack.is(OriginsClassesItemTags.SHIELDS)
        ));
    public static final RegistryObject<SimpleItemCondition> HELMET = ITEM_CONDITIONS.register("helmet", () ->
        new SimpleItemCondition(stack ->
            (stack.getItem() instanceof ArmorItem armor && armor.getSlot() == EquipmentSlot.HEAD) ||
            stack.is(OriginsClassesItemTags.HELMETS)
        ));
    public static final RegistryObject<SimpleItemCondition> CHESTPLATE = ITEM_CONDITIONS.register("chestplate", () ->
        new SimpleItemCondition(stack ->
            (stack.getItem() instanceof ArmorItem armor && armor.getSlot() == EquipmentSlot.CHEST) ||
            stack.is(OriginsClassesItemTags.CHESTPLATES)
        ));
    public static final RegistryObject<SimpleItemCondition> LEGGINGS = ITEM_CONDITIONS.register("leggings", () ->
        new SimpleItemCondition(stack ->
            (stack.getItem() instanceof ArmorItem armor && armor.getSlot() == EquipmentSlot.LEGS) ||
            stack.is(OriginsClassesItemTags.LEGGINGS)
        ));
    public static final RegistryObject<SimpleItemCondition> SHOES = ITEM_CONDITIONS.register("shoes", () ->
        new SimpleItemCondition(stack ->
            (stack.getItem() instanceof ArmorItem armor && armor.getSlot() == EquipmentSlot.FEET) ||
            stack.is(OriginsClassesItemTags.SHOES)
        ));
    public static final RegistryObject<ToolActionCondition> TOOL_ACTION = ITEM_CONDITIONS.register("tool_action", ToolActionCondition::new);
    public static final RegistryObject<HasPowerContition> HAS_POWER = ITEM_CONDITIONS.register("has_power", HasPowerContition::new);
    
    public static final DeferredRegister<BlockCondition<?>> BLOCK_CONDITIONS = DeferredRegister.create(ApoliRegistries.BLOCK_CONDITION_KEY, OriginsClasses.MODID);
    public static final RegistryObject<SimpleBlockCondition> HARVESTABLE_CROPS = BLOCK_CONDITIONS.register("harvestable_crops", () ->
        new SimpleBlockCondition((level, pos, stateSuppplier) -> {
            BlockState state = stateSuppplier.get();
            if(state.getBlock() instanceof CropBlock crop) return crop.isMaxAge(state);
            return false;
        }));

}
