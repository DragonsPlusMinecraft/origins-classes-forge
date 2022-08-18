package dev.limonblaze.originsclasses.common.tag;

import dev.limonblaze.originsclasses.OriginsClasses;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;

public class OriginsClassesItemTags {

    public static final TagKey<Item> MERCHANT_BLACKLIST = originsclasses("merchant_blacklist");
    public static final TagKey<Item> DIGGERS = originsclasses("diggers");
    public static final TagKey<Item> MELEE_WEAPONS = originsclasses("melee_weapons");
    public static final TagKey<Item> RANGE_WEAPONS = originsclasses("range_weapons");
    public static final TagKey<Item> ARMORS = originsclasses("armors");
    public static final TagKey<Item> HOES = originsclasses("hoes");
    public static final TagKey<Item> PICKAXES = originsclasses("pickaxes");
    public static final TagKey<Item> AXES = originsclasses("axes");
    public static final TagKey<Item> SHOVELS = originsclasses("shovels");
    public static final TagKey<Item> SHEARS = originsclasses("shears");
    public static final TagKey<Item> SWORDS = originsclasses("swords");
    public static final TagKey<Item> BOWS = originsclasses("bows");
    public static final TagKey<Item> CROSSBOWS = originsclasses("cross_bows");
    public static final TagKey<Item> SHIELDS = originsclasses("shields");
    public static final TagKey<Item> HELMETS = originsclasses("helmets");
    public static final TagKey<Item> CHESTPLATES = originsclasses("chestplates");
    public static final TagKey<Item> LEGGINGS = originsclasses("leggings");
    public static final TagKey<Item> SHOES = originsclasses("shoes");

    private static TagKey<Item> originsclasses(String name) {
        return ForgeRegistries.ITEMS.tags().createOptionalTagKey(OriginsClasses.legacyIdentifier(name), Collections.emptySet());
    }

}
