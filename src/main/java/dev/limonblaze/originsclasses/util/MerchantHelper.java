package dev.limonblaze.originsclasses.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import dev.limonblaze.originsclasses.common.tag.OriginsClassesItemTags;
import dev.limonblaze.originsclasses.core.mixin.accessor.minecraft.LootTableAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class MerchantHelper extends SimplePreparableReloadListener<Void> {
    private static MerchantHelper INSTANCE;
    private final LootTables lootTables;
    public Set<Item> obtainableItems = ImmutableSet.of();
    public Set<Item> blacklistItems = ImmutableSet.of();
    
    public MerchantHelper(LootTables lootTables) {
        this.lootTables = lootTables;
    }
    
    @Override
    protected Void prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        return null;
    }
    
    @Override
    protected void apply(Void nothing, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableSet.Builder<Item> builder = ImmutableSet.builder();
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
                    builder.add(li.item);
                } else if(entry instanceof TagEntry te) {
                    Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).getTag(te.tag).forEach(builder::add);
                } else if(entry instanceof CompositeEntryBase ceb) {
                    entryQueue.addAll(Arrays.asList(ceb.children));
                }
            }
        }
        this.obtainableItems = builder.build();
        this.blacklistItems = ForgeRegistries.ITEMS.tags()
            .getTag(OriginsClassesItemTags.MERCHANT_BLACKLIST)
            .stream()
            .collect(Collectors.toUnmodifiableSet());
    }

    public Item randomObtainableItem(RandomSource random, Set<Item> exclude) {
        Item[] obtainables;
        if(exclude.isEmpty()) {
            obtainables = obtainableItems.toArray(new Item[0]);
        } else {
            obtainables = Sets.difference(obtainableItems, exclude).toArray(new Item[0]);
        }
        return obtainables.length > 0 ? obtainables[random.nextInt(obtainables.length)] : Items.AIR;
    }
    
    public static ItemStack randomEnchantedItemStack(Item item, RandomSource random, float chance, int power) {
        ItemStack stack = new ItemStack(item);
        if(item.isEnchantable(stack) && random.nextFloat() < chance) {
            EnchantmentHelper.enchantItem(random, stack, 1 + random.nextInt(power), random.nextBoolean());
        }
        return stack;
    }
    
    public static MerchantHelper instance() {
        if(INSTANCE == null) {
            throw new IllegalStateException("MerchantHelper haven't been initialized yet!");
        }
        return INSTANCE;
    }
    
    @SubscribeEvent
    public static void addSelfToReloadListeners(AddReloadListenerEvent event) {
        INSTANCE = new MerchantHelper(event.getServerResources().getLootTables());
        event.addListener(INSTANCE);
    }
    
}
