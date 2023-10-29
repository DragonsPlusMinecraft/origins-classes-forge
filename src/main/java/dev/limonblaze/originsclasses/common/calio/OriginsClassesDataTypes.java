package dev.limonblaze.originsclasses.common.calio;

import java.util.EnumSet;

import dev.limonblaze.originsclasses.common.event.ModifyCraftResultEvent;
import io.github.apace100.calio.data.SerializableDataType;
import net.minecraft.world.entity.EquipmentSlot;

public class OriginsClassesDataTypes {
    
    public static final SerializableDataType<ModifyCraftResultEvent.CraftingResultType> CRAFTING_RESULT_TYPE =
        SerializableDataType.enumValue(ModifyCraftResultEvent.CraftingResultType.class);
    
    public static final SerializableDataType<EnumSet<ModifyCraftResultEvent.CraftingResultType>> CRAFTING_RESULT_TYPE_SET =
        SerializableDataType.enumSet(ModifyCraftResultEvent.CraftingResultType.class, CRAFTING_RESULT_TYPE);
    
    public static final SerializableDataType<EquipmentSlot> EQUIPMENT_SLOT_TYPE =
            SerializableDataType.enumValue(EquipmentSlot.class);
    
    public static final SerializableDataType<EnumSet<EquipmentSlot>> EQUIPMENT_SLOT =
            SerializableDataType.enumSet(EquipmentSlot.class, EQUIPMENT_SLOT_TYPE);
}
