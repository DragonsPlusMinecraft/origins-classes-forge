package limonblaze.originsclasses.mixin;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin extends BaseContainerBlockEntity {

    private static ServerPlayer ORIGINS_CLASSES_CACHED_PLAYER;
    private static BlockPos ORIGINS_CLASSES_CACHED_BLOCK_POS;

    protected AbstractFurnaceBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "awardUsedRecipesAndPopExperience", at = @At("HEAD"))
    private void originsClasses$cachePlayerAndPos(ServerPlayer player, CallbackInfo ci) {
        ORIGINS_CLASSES_CACHED_PLAYER = player;
        ORIGINS_CLASSES_CACHED_BLOCK_POS = this.worldPosition;
    }

    @Inject(method = "awardUsedRecipesAndPopExperience", at = @At("TAIL"))
    private void originsClasses$resetPlayerAndPos(ServerPlayer player, CallbackInfo ci) {
        ORIGINS_CLASSES_CACHED_PLAYER = null;
        ORIGINS_CLASSES_CACHED_BLOCK_POS = null;
    }

    @ModifyVariable(method = "createExperience", at = @At("HEAD"), argsOnly = true)
    private static float originsClasses$modifyRecipeXp(float xp) {
        if(ORIGINS_CLASSES_CACHED_PLAYER != null && ORIGINS_CLASSES_CACHED_BLOCK_POS != null) {
            xp = IPowerContainer.modify(ORIGINS_CLASSES_CACHED_PLAYER, OriginsClassesPowers.MODIFY_FURNACE_XP.get(), xp,
                cp -> cp.isActive(ORIGINS_CLASSES_CACHED_PLAYER) && ConfiguredBlockCondition.check(cp.getConfiguration().condition(), ORIGINS_CLASSES_CACHED_PLAYER.level, ORIGINS_CLASSES_CACHED_BLOCK_POS));
        }
        return xp;
    }

}
