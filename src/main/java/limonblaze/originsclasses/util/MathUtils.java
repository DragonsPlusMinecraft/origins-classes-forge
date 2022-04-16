package limonblaze.originsclasses.util;

import net.minecraft.world.entity.Entity;

import java.util.*;

public class MathUtils {

    public static int naturalRandFloor(double d, Random random) {
        if(d > 0) {
            int i = (int) d;
            return random.nextDouble() < d - i ? i + 1 : i;
        }
        return 0;
    }

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
