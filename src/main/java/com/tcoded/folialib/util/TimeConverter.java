package com.tcoded.folialib.util;

import java.util.concurrent.TimeUnit;

public class TimeConverter {

    public static long toTicks(long time, TimeUnit unit) {
        return unit.toMillis(time) / 50;
    }

    public static long toMillis(long ticks) {
        return Math.max(1, ticks) * 50;
    }

}
