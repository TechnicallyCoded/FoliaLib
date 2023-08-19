package com.tcoded.folialib.util;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

public class ImplementationTestsUtil {

    private static final boolean IS_CANCELLED_SUPPORTED;
    private static final boolean IS_TASK_CONSUMERS_SUPPORTED;


    static {
        boolean isCancelledSupported = false;
        try {
            Class<BukkitTask> bukkitTaskClass = BukkitTask.class;
            // noinspection JavaReflectionMemberAccess
            bukkitTaskClass.getDeclaredMethod("isCancelled");
            isCancelledSupported = true;
        } catch (NoSuchMethodException e) {
            // ignore
        }
        // Set class-wide
        IS_CANCELLED_SUPPORTED = isCancelledSupported;

        boolean taskConsumersSupported = false;
        try {
            Class<BukkitScheduler> bukkitSchedulerClass = BukkitScheduler.class;
            // noinspection JavaReflectionMemberAccess
            bukkitSchedulerClass.getDeclaredMethod("runTask", Plugin.class, Consumer.class);
            taskConsumersSupported = true;
        } catch (NoSuchMethodException e) {
            // ignore
        }
        // Set class-wide
        IS_TASK_CONSUMERS_SUPPORTED = taskConsumersSupported;
    }

    public static boolean isCancelledSupported() {
        return IS_CANCELLED_SUPPORTED;
    }

    public static boolean isTaskConsumersSupported() {
        return IS_TASK_CONSUMERS_SUPPORTED;
    }

}
