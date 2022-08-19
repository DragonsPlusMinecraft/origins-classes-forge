package dev.limonblaze.originsclasses.core.mixin.common.minecraft;

import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import dev.limonblaze.originsclasses.common.registry.OriginsClassesPowers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public class ProjectileMixin {

    private Entity originsClasses$cachedShooter;

    @Inject(method = "shootFromRotation", at = @At("HEAD"))
    private void originsClasses$cacheShooter(Entity entity, float p, float y, float r, float s, float d, CallbackInfo ci) {
        this.originsClasses$cachedShooter = entity;
    }

    @ModifyVariable(method = "shoot", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private float originsClasses$modifyDivergence(float oldDivergence) {
        if(originsClasses$cachedShooter != null) {
            float newDivergence = IPowerContainer.modify(
                originsClasses$cachedShooter,
                OriginsClassesPowers.MODIFY_PROJECTILE_DIVERGENCE.get(),
                oldDivergence,
                cp -> cp.get().isActive(originsClasses$cachedShooter)
            );
            originsClasses$cachedShooter = null;
            return newDivergence;
        }
        return oldDivergence;
    }

}