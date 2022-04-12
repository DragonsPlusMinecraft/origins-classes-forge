package limonblaze.originsclasses.common.data.tag;

import limonblaze.originsclasses.OriginsClasses;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

import java.util.Set;
import java.util.function.Supplier;

public class OriginsClassesItemTags {

    public static final Tags.IOptionalNamedTag<Item> MERCHANT_BLACKLIST = tag(OriginsClasses.identifier("merchant_blacklist"));

    private static Tags.IOptionalNamedTag<Item> tag(ResourceLocation id) {
        return ItemTags.createOptional(id);
    }

    private static Tags.IOptionalNamedTag<Item> tag(ResourceLocation id, Set<Supplier<Item> >defaults) {
        return ItemTags.createOptional(id, defaults);
    }

}
