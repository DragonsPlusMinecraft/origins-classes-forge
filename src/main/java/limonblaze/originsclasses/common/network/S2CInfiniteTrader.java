package limonblaze.originsclasses.common.network;

import limonblaze.originsclasses.client.OriginsClassesClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record S2CInfiniteTrader(boolean infiniteTrader) {

    public static S2CInfiniteTrader decode(FriendlyByteBuf buf) {
        return new S2CInfiniteTrader(buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.infiniteTrader);
    }

    @OnlyIn(Dist.CLIENT)
    private void handleSync() {
        OriginsClassesClient.INFINITE_TRADER = this.infiniteTrader;
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::handleSync));
        contextSupplier.get().setPacketHandled(true);
    }

}
