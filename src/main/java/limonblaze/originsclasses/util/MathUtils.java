package limonblaze.originsclasses.util;

import java.util.Random;

public class MathUtils {

    public static int randomFloor(double d, Random random) {
        int i = (int) d;
        return random.nextDouble() < d - i ? i + 1 : i;
    }
    
}
