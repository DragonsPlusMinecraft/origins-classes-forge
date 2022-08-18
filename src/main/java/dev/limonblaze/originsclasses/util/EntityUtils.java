package dev.limonblaze.originsclasses.util;

import net.minecraft.world.entity.Entity;

public class EntityUtils {
    
    public static double yawDiff(Entity actor, Entity target) {
        float yawTarget = target.getYRot();
        while(yawTarget < 0F) yawTarget += 360F;
        yawTarget %= 360F;

        float yawSelf = actor.getYRot();
        while(yawSelf < 0F) yawSelf += 360F;
        yawSelf %= 360F;

        return Math.abs(yawTarget - yawSelf);
    }
    
}
