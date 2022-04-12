package limonblaze.originsclasses.common.registry;

import limonblaze.originsclasses.OriginsClasses;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OriginsClassesAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, OriginsClasses.MODID);

    public static final RegistryObject<RangedAttribute> MINING_SPEED = ATTRIBUTES.register("mining_speed", () ->
        new RangedAttribute("attribute.origins_classes.mining_speed", 1.0, 0.0, 1024.0));
    public static final RegistryObject<RangedAttribute> PROJECTILE_STRENGTH = ATTRIBUTES.register("projectile_strength", () ->
        new RangedAttribute("attribute.origins_classes.projectile_strength", 1.0, 0.0, 1024.0));


}
