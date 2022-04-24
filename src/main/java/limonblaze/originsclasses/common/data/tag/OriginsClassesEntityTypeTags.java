package limonblaze.originsclasses.common.data.tag;

import limonblaze.originsclasses.OriginsClasses;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class OriginsClassesEntityTypeTags {

    public static final TagKey<EntityType<?>> INFINITE_TRADER = tag(OriginsClasses.legacyIdentifier("infinite_trader"));

    private static TagKey<EntityType<?>> tag(ResourceLocation id) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, id);
    }

}
