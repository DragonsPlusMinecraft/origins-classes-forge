package dev.limonblaze.originsclasses.mixin;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import dev.limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin extends BaseContainerBlockEntity {

    @Unique private static ServerPlayer CACHED_PLAYER;
    @Unique private static BlockPos CACHED_BLOCK_POS;

    protected AbstractFurnaceBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "awardUsedRecipesAndPopExperience", at = @At("HEAD"))
    private void originsClasses$cachePlayerAndPos(ServerPlayer player, CallbackInfo ci) {
        CACHED_PLAYER = player;
        CACHED_BLOCK_POS = this.worldPosition;
    }

    @Inject(method = "awardUsedRecipesAndPopExperience", at = @At("TAIL"))
    private void originsClasses$resetPlayerAndPos(ServerPlayer player, CallbackInfo ci) {
        CACHED_PLAYER = null;
        CACHED_BLOCK_POS = null;
    }

    @ModifyVariable(method = "createExperience", at = @At("HEAD"), argsOnly = true)
    private static float originsClasses$modifyRecipeXp(float xp) {
        if(CACHED_PLAYER != null && CACHED_BLOCK_POS != null) {
            xp = IPowerContainer.modify(CACHED_PLAYER, OriginsClassesPowers.MODIFY_FURNACE_XP.get(), xp,
                cp ->
                    cp.get().isActive(CACHED_PLAYER) &&
                    ConfiguredBlockCondition.check(
                        cp.get().getConfiguration().condition(),
                        CACHED_PLAYER.level,
                        CACHED_BLOCK_POS
                    )
            );
        }
        return xp;
    }

}
