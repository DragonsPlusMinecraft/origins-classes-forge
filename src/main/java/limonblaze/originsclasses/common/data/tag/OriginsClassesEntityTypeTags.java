package limonblaze.originsclasses.common.data.tag;

import limonblaze.originsclasses.OriginsClasses;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.Tags;

import java.util.Set;
import java.util.function.Supplier;

public class OriginsClassesEntityTypeTags {

    public static final Tags.IOptionalNamedTag<EntityType<?>> INFINITE_TRADER = tag(OriginsClasses.identifier("infinite_trader"), Set.of(() -> EntityType.VILLAGER));

    private static Tags.IOptionalNamedTag<EntityType<?>> tag(ResourceLocation id) {
        return EntityTypeTags.createOptional(id);
    }

    private static Tags.IOptionalNamedTag<EntityType<?>> tag(ResourceLocation id, Set<Supplier<EntityType<?>>> defaults) {
        return EntityTypeTags.createOptional(id, defaults);
    }

}
