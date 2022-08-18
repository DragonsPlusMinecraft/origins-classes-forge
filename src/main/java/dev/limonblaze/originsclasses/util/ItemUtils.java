package dev.limonblaze.originsclasses.util;

import com.google.common.collect.Sets;
import dev.limonblaze.originsclasses.mixin.accessor.LootTableAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

public class ItemUtils {
    private static boolean INITIALIZED_OBTAINABLE_ITEMS;
    private static final Set<Item> OBTAINABLE = new HashSet<>();

    public static ItemStack randomEnchantedItemStack(Item item, RandomSource random, float chance, int power) {
        ItemStack stack = new ItemStack(item);
        if(item.isEnchantable(stack) && random.nextFloat() < chance) {
            EnchantmentHelper.enchantItem(random, stack, 1 + random.nextInt(power), random.nextBoolean());
        }
        return stack;
    }

    public static Item randomObtainableItem(MinecraftServer server, RandomSource random, Set<Item> exclude) {
        if(!INITIALIZED_OBTAINABLE_ITEMS) {
            initObtainableItems(server);
        }
        Item[] obtainables;
        if(exclude.isEmpty()) {
            obtainables = OBTAINABLE.toArray(new Item[0]);
        } else {
            obtainables = Sets.difference(OBTAINABLE, exclude).toArray(new Item[0]);
        }
        return obtainables.length > 0 ? obtainables[random.nextInt(obtainables.length)] : Items.AIR;
    }

    private static void initObtainableItems(MinecraftServer server) {
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
                    ItemUtils.OBTAINABLE.add(li.item);
                } else if(entry instanceof TagEntry te) {
                    Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).getTag(te.tag).forEach(ItemUtils.OBTAINABLE::add);
                } else if(entry instanceof CompositeEntryBase ceb) {
                    entryQueue.addAll(Arrays.asList(ceb.children));
                }
            }
        }
        INITIALIZED_OBTAINABLE_ITEMS = true;
    }
    
    public static Component modifierTooltip(AttributeModifier modifier, String translationKey) {
        double value = modifier.getAmount() * (modifier.getOperation() == AttributeModifier.Operation.ADDITION ? 1 : 100);
        if (value > 0.0D) {
            return MutableComponent.create(new TranslatableContents(
                    "attribute.modifier.plus." + modifier.getOperation().toValue(),
                    ATTRIBUTE_MODIFIER_FORMAT.format(value),
                    MutableComponent.create(new TranslatableContents(translationKey))
                )).withStyle(ChatFormatting.BLUE);
        } else {
            value *= -1.0D;
            return MutableComponent.create(new TranslatableContents(
                    "attribute.modifier.take." + modifier.getOperation().toValue(),
                    ATTRIBUTE_MODIFIER_FORMAT.format(value),
                    MutableComponent.create(new TranslatableContents(translationKey))
                )).withStyle(ChatFormatting.RED);
        }
    }
    
}
