package com.tcoded.folialib.util;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

public class ImplementationTestsUtil {

    private static boolean IS_CANCELLED_SUPPORTED;
    private static boolean IS_TASK_CONSUMERS_SUPPORTED;
    private static boolean IS_ASYNC_TELEPORT_SUPPORTED;

    static {
        try {
            // noinspection JavaReflectionMemberAccess
            BukkitTask.class.getDeclaredMethod("isCancelled");
            IS_CANCELLED_SUPPORTED = true;
        } catch (NoSuchMethodException e) {
            IS_CANCELLED_SUPPORTED = false;
        }

        try {
            // noinspection JavaReflectionMemberAccess
            BukkitScheduler.class.getDeclaredMethod("runTask", Plugin.class, Consumer.class);
            IS_TASK_CONSUMERS_SUPPORTED = true;
        } catch (NoSuchMethodException e) {
            IS_TASK_CONSUMERS_SUPPORTED = false;
        }

        try {
            // noinspection JavaReflectionMemberAccess
            Entity.class.getDeclaredMethod("teleportAsync", Location.class, PlayerTeleportEvent.TeleportCause.class);
            IS_ASYNC_TELEPORT_SUPPORTED = true;
        } catch (NoSuchMethodException e) {
            IS_ASYNC_TELEPORT_SUPPORTED = false;
        }
    }

    public static boolean isCancelledSupported() {
        return IS_CANCELLED_SUPPORTED;
    }

    public static boolean isTaskConsumersSupported() {
        return IS_TASK_CONSUMERS_SUPPORTED;
    }

    public static boolean isAsyncTeleportSupported() {
        return IS_ASYNC_TELEPORT_SUPPORTED;
    }
}
