package limonblaze.originsclasses.common.registry;

import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import limonblaze.originsclasses.OriginsClasses;
import limonblaze.originsclasses.common.apoli.action.entity.AttributeAction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class OriginsClassesActions {

    public static final DeferredRegister<EntityAction<?>> ENTITY_ACTIONS = DeferredRegister.create(ApoliRegistries.ENTITY_ACTION_KEY, OriginsClasses.MODID);

    public static final RegistryObject<AttributeAction> APPLY_MODIFIER = ENTITY_ACTIONS.register("attribute", AttributeAction::new);

}
