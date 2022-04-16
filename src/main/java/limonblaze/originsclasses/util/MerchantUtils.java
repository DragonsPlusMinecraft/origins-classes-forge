package limonblaze.originsclasses.util;

import com.google.common.collect.Sets;
import limonblaze.originsclasses.mixin.accessor.LootTableAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.TagEntry;

import java.util.*;

public class MerchantUtils {
    private static boolean APPENDED_OBTAINABLE_ITEMS;
    private static final Set<Item> OBTAINABLE = new HashSet<>();

    public static ItemStack createMerchantItemStack(Item item, Random random) {
        ItemStack stack = new ItemStack(item);
        if(item.isEnchantable(stack) && random.nextFloat() < 0.5) {
            EnchantmentHelper.enchantItem(random, stack, 1 + random.nextInt(30), random.nextBoolean());
        }
        return stack;
    }

    public static Item getRandomObtainableItem(MinecraftServer server, Random random, Set<Item> exclude) {
        if(!APPENDED_OBTAINABLE_ITEMS) {
            appendObtainableItems(server);
        }
        Item[] items;
        if(exclude == null || exclude.isEmpty()) {
            items = OBTAINABLE.toArray(new Item[0]);
        } else {
            Set<Item> possibles = Sets.difference(OBTAINABLE, exclude);
            items = possibles.toArray(new Item[0]);
        }
        return items[random.nextInt(items.length)];
    }

    public static void appendObtainableItems(MinecraftServer server) {
        LootTables lootTables = server.getLootTables();
        for(ResourceLocation id : lootTables.getIds()) {
            List<LootPool> pools = ((LootTableAccessor)lootTables.get(id)).getPools();
            Queue<LootPoolEntryContainer> entryQueue = new LinkedList<>();
            for (LootPool pool : pools) {
                LootPoolEntryContainer[] entries = pool.entries;
                entryQueue.addAll(Arrays.asList(entries));
            }
            while(!entryQueue.isEmpty()) {
                LootPoolEntryContainer entry = entryQueue.remove();
                if(entry instanceof LootItem li) {
                    MerchantUtils.OBTAINABLE.add(li.item);
                } else if(entry instanceof TagEntry te) {
                    MerchantUtils.OBTAINABLE.addAll(te.tag.getValues());
                } else if(entry instanceof CompositeEntryBase ceb) {
                    entryQueue.addAll(Arrays.asList(ceb.children));
                }
            }
        }
        APPENDED_OBTAINABLE_ITEMS = true;
    }

}
