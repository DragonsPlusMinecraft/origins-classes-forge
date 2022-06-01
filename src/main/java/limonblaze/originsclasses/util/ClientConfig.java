package limonblaze.originsclasses.util;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ClientConfig {
    public static final ClientConfig CONFIG;
    public static final ForgeConfigSpec SPEC;
    
    static {
        Pair<ClientConfig, ForgeConfigSpec> entry = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CONFIG = entry.getKey();
        SPEC = entry.getValue();
    }
    
    public final ForgeConfigSpec.BooleanValue showModifyFoodTooltip;
    public final ForgeConfigSpec.BooleanValue showPotionBonusTooltip;
    
    public ClientConfig(ForgeConfigSpec.Builder builder) {
        showModifyFoodTooltip = builder.translation("config.origins_classes.show_modify_food_tooltip")
            .define("modify_food_tooltip", true);
        showPotionBonusTooltip = builder.translation("config.origins_classes.show_potion_bonus_tooltip")
            .define("potion_bonus_tooltip", true);
    }
    
}
