package dev.limonblaze.originsclasses.common.registry;

import dev.limonblaze.originsclasses.OriginsClasses;
import dev.limonblaze.originsclasses.common.apoli.power.*;
import dev.limonblaze.originsclasses.util.CommonUtils;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.power.DummyPower;
import io.github.edwinmindcraft.apoli.common.power.ModifyValueBlockPower;
import io.github.edwinmindcraft.apoli.common.power.ModifyValuePower;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class OriginsClassesPowers {
    public static final DeferredRegister<PowerFactory<?>> POWER_FACTORIES = DeferredRegister.create(ApoliRegistries.POWER_FACTORY_KEY, OriginsClasses.MODID);

    // Warrior
    public static final RegistryObject<ModifySpeedOnItemUsePower> MODIFY_SPEED_ON_ITEM_USE = POWER_FACTORIES.register("modify_speed_on_item_use", ModifySpeedOnItemUsePower::new);
    // Ranger
    public static final RegistryObject<ModifyValuePower> MODIFY_PROJECTILE_DIVERGENCE = POWER_FACTORIES.register("modify_projectile_divergence", ModifyValuePower::new);
    // Beastmaster
    public static final RegistryObject<ActionOnTamePower> ACTION_ON_TAME = POWER_FACTORIES.register("action_on_tame", ActionOnTamePower::new);
    public static final RegistryObject<DummyPower> TAMED_POTION_DIFFUSAL = POWER_FACTORIES.register("tamed_potion_diffusal", DummyPower::new);
    // Cook
    public static final RegistryObject<ModifyCraftedFoodPower> MODIFY_CRAFTED_FOOD = POWER_FACTORIES.register("modify_crafted_food", ModifyCraftedFoodPower::new);
    public static final RegistryObject<ModifyValueBlockPower> MODIFY_FURNACE_XP = POWER_FACTORIES.register("modify_furnace_xp", ModifyValueBlockPower::new);
    // Cleric
    public static final RegistryObject<DummyPower> POTION_BONUS = POWER_FACTORIES.register("potion_bonus", DummyPower::new);
    public static final RegistryObject<ModifyValuePower> MODIFY_ENCHANTING_LEVEL = POWER_FACTORIES.register("modify_enchanting_level", ModifyValuePower::new);
    // Blacksmith
    public static final RegistryObject<ModifyCraftResultPower> MODIFY_CRAFT_RESULT = POWER_FACTORIES.register("modify_craft_result", ModifyCraftResultPower::new);
    public static final RegistryObject<ModifyValuePower> MODIFY_REPAIR_MATERIAL_COST = POWER_FACTORIES.register("modify_repair_material_cost", ModifyValuePower::new);
    public static final RegistryObject<ModifyValuePower> MODIFY_COMBINE_REPAIR_DURABILITY = POWER_FACTORIES.register("modify_combine_repair_durability", ModifyValuePower::new);
    // Farmer
    public static final RegistryObject<ModifyValueBlockPower> MODIFY_BLOCK_LOOT = POWER_FACTORIES.register("modify_block_loot", ModifyValueBlockPower::new);
    public static final RegistryObject<ModifyValuePower> MODIFY_BONE_MEAL = POWER_FACTORIES.register("modify_bone_meal", ModifyValuePower::new);
    // Rancher
    public static final RegistryObject<ModifyValueBiEntityPower> MODIFY_ENTITY_LOOT = POWER_FACTORIES.register("modify_entity_loot", ModifyValueBiEntityPower::new);
    public static final RegistryObject<ModifyBreedingPower> MODIFY_BREEDING = POWER_FACTORIES.register("modify_breeding", ModifyBreedingPower::new);
    // Merchant
    public static final RegistryObject<DummyPower> INFINITE_TRADE = POWER_FACTORIES.register("infinite_trade", DummyPower::new);
    public static final RegistryObject<DummyPower> RARE_WANDERING_LOOT = POWER_FACTORIES.register("rare_wandering_loot", DummyPower::new);
    // Lumberjack
    public static final RegistryObject<MultiMinePower> LUMBERJACK = POWER_FACTORIES.register("lumberjack", () -> new MultiMinePower(CommonUtils::lumberjackMultiMineRange));
    // Miner
    public static final RegistryObject<DummyPower> NO_MINING_EXHAUSTION = POWER_FACTORIES.register("no_mining_exhaustion", DummyPower::new);
    // Adventurer
    public static final RegistryObject<DummyPower> NO_SPRINT_EXHAUSTION = POWER_FACTORIES.register("no_sprint_exhaustion", DummyPower::new);

}