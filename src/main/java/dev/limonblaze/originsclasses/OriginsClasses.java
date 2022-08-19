package dev.limonblaze.originsclasses;

import dev.limonblaze.originsclasses.common.OriginsClassesCommon;
import dev.limonblaze.originsclasses.common.registry.OriginsClassesConditions;
import dev.limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import dev.limonblaze.originsclasses.util.ClientConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(OriginsClasses.MODID)
public class OriginsClasses {
    public static final String MODID = "origins_classes";
    public static final String LEGACY_MODID = "origins-classes";
    public static final Logger LOGGER = LogManager.getLogger();
    
    @OnlyIn(Dist.CLIENT)
    public static boolean INFINITE_TRADER;
    @OnlyIn(Dist.CLIENT)
    public static boolean MULTI_MINING;
    
    public OriginsClasses() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        OriginsClassesConditions.BIENTITY_CONDITIONS.register(modBus);
        OriginsClassesConditions.ENTITY_CONDITIONS.register(modBus);
        OriginsClassesConditions.BLOCK_CONDITIONS.register(modBus);
        OriginsClassesConditions.ITEM_CONDITIONS.register(modBus);
        OriginsClassesPowers.POWER_FACTORIES.register(modBus);
        modBus.addListener(OriginsClassesCommon::setup);
        LOGGER.info("Origins:Classes " + ModLoadingContext.get().getActiveContainer().getModInfo().getVersion() + " has initialized. Time for work!");
    }

    public static ResourceLocation identifier(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static ResourceLocation legacyIdentifier(String path) {
        return new ResourceLocation(LEGACY_MODID, path);
    }

}
