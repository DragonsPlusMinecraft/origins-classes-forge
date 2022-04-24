package limonblaze.originsclasses;

import limonblaze.originsclasses.client.OriginsClassesClient;
import limonblaze.originsclasses.common.OriginsClassesCommon;
import limonblaze.originsclasses.common.registry.OriginsClassesActions;
import limonblaze.originsclasses.common.registry.OriginsClassesAttributes;
import limonblaze.originsclasses.common.registry.OriginsClassesConditions;
import limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(OriginsClasses.MODID)
public class OriginsClasses {
    public static final String MODID = "origins_classes";
    public static final String LEGACY_MODID = "origins-classes";
    public static final Logger LOGGER = LogManager.getLogger();

    public OriginsClasses() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        OriginsClassesAttributes.ATTRIBUTES.register(modBus);
        OriginsClassesActions.ENTITY_ACTIONS.register(modBus);
        OriginsClassesConditions.BIENTITY_CONDITIONS.register(modBus);
        OriginsClassesConditions.ENTITY_CONDITIONS.register(modBus);
        OriginsClassesConditions.BLOCK_CONDITIONS.register(modBus);
        OriginsClassesConditions.ITEM_CONDITIONS.register(modBus);
        OriginsClassesPowers.POWER_FACTORIES.register(modBus);
        modBus.addListener(this::attachAttributes);
        modBus.addListener(OriginsClassesCommon::setup);
        modBus.addListener(OriginsClassesClient::setup);
        LOGGER.info("Origins:Classes " + ModLoadingContext.get().getActiveContainer().getModInfo().getVersion() + " has initialized. Time for work!");
    }

    public static ResourceLocation identifier(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static ResourceLocation legacyIdentifier(String path) {
        return new ResourceLocation(LEGACY_MODID, path);
    }

    public void attachAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(type -> event.add(type, OriginsClassesAttributes.PROJECTILE_STRENGTH.get()));
        event.add(EntityType.PLAYER, OriginsClassesAttributes.MINING_SPEED.get());
    }

}
