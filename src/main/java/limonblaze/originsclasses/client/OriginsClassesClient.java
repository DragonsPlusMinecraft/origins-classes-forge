package limonblaze.originsclasses.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class OriginsClassesClient {
    @OnlyIn(Dist.CLIENT) public static boolean INFINITE_TRADER;
    @OnlyIn(Dist.CLIENT) public static boolean MULTI_MINING;

    public static void setup(final FMLClientSetupEvent event) {

    }

}
