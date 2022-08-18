package dev.limonblaze.originsclasses.common.network;

import dev.limonblaze.originsclasses.client.OriginsClassesClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record S2CMultiMining(boolean multiMining) {

    public static S2CMultiMining decode(FriendlyByteBuf buf) {
        return new S2CMultiMining(buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.multiMining);
    }

    @OnlyIn(Dist.CLIENT)
    private void handleSync() {
        OriginsClassesClient.MULTI_MINING = this.multiMining;
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::handleSync));
        contextSupplier.get().setPacketHandled(true);
    }

}
