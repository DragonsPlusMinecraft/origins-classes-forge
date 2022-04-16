package limonblaze.originsclasses.common.data.tag;

import limonblaze.originsclasses.OriginsClasses;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class OriginsClassesItemTags {

    public static final TagKey<Item> MERCHANT_BLACKLIST = tag(OriginsClasses.identifier("merchant_blacklist"));

    private static TagKey<Item> tag(ResourceLocation id) {
        return TagKey.create(Registry.ITEM_REGISTRY, id);
    }

}
