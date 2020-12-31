package org.kakara.engine.utils;

import java.util.Random;
import java.util.UUID;

public class UUIDUtils {
    private static Random random = new Random();

    /**
     * Random UUID generator
     * UUID.randomUUID() is slow. More information here https://stackoverflow.com/a/14534126
     *
     * @return a random UUID
     */
    public static UUID randomUUID() {
        return new UUID(random.nextLong(), random.nextLong());
    }
}
