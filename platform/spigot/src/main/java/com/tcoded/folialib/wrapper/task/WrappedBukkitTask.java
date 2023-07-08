package com.tcoded.folialib.wrapper.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WrappedBukkitTask implements WrappedTask {

    private static final boolean IS_CANCELLED_SUPPORTED;

    private static Method isCancelledMethod;

    static {
        boolean supported = false;
        try {
            Class<BukkitTask> bukkitTaskClass = BukkitTask.class;
            //noinspection JavaReflectionMemberAccess
            isCancelledMethod = bukkitTaskClass.getDeclaredMethod("isCancelled");
            supported = true;
        } catch (NoSuchMethodException e) {
            // ignore
        }

        // Set class-wide
        IS_CANCELLED_SUPPORTED = supported;
    }

    private final BukkitTask task;

    public WrappedBukkitTask(BukkitTask task) {
        this.task = task;
    }

    @Override
    public void cancel() {
        this.task.cancel();
    }

    @Override
    public boolean isCancelled() {
        // The updated way of doing this
        if (IS_CANCELLED_SUPPORTED)
            return isCancelledUpdated();

        // Legacy support
        return isCancelledLegacy();
    }

    private boolean isCancelledUpdated() {
        try {
            return (boolean) isCancelledMethod.invoke(this.task);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isCancelledLegacy() {
        int taskId = this.task.getTaskId();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        return !(scheduler.isCurrentlyRunning(taskId) || scheduler.isQueued(taskId));
    }

    @Override
    public Plugin getOwningPlugin() {
        return this.task.getOwner();
    }
}
