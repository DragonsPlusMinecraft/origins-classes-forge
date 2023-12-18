package dev.limonblaze.originsclasses.common;

import dev.limonblaze.originsclasses.OriginsClasses;
import dev.limonblaze.originsclasses.common.network.S2CInfiniteTrader;
import dev.limonblaze.originsclasses.common.network.S2CMultiMining;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class OriginsClassesCommon {
    public static final String NETWORK_VERSION = "1.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
        .named(OriginsClasses.identifier("channel"))
        .networkProtocolVersion(() -> NETWORK_VERSION)
        .clientAcceptedVersions(NETWORK_VERSION::equals)
        .serverAcceptedVersions(NETWORK_VERSION::equals)
        .simpleChannel();

    public static void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(OriginsClassesCommon::initNetwork);
    }

    private static void initNetwork() {
        int messageId = 0;
        CHANNEL.messageBuilder(S2CInfiniteTrader.class, ++messageId, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(S2CInfiniteTrader::encode)
            .decoder(S2CInfiniteTrader::decode)
            .consumerNetworkThread(S2CInfiniteTrader::handle)
            .add();
        CHANNEL.messageBuilder(S2CMultiMining.class, ++messageId, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(S2CMultiMining::encode)
            .decoder(S2CMultiMining::decode)
            .consumerNetworkThread(S2CMultiMining::handle)
            .add();
        OriginsClasses.LOGGER.debug("Registered {} newtork messages.", messageId);
    }

}
