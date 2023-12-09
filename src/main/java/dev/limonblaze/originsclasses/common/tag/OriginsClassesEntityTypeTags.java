package dev.limonblaze.originsclasses.common.tag;

import dev.limonblaze.originsclasses.OriginsClasses;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class OriginsClassesEntityTypeTags {

    public static final TagKey<EntityType<?>> INFINITE_TRADER = tag(OriginsClasses.legacyIdentifier("infinite_trader"));

    private static TagKey<EntityType<?>> tag(ResourceLocation id) {
        return TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), id);
    }

}
