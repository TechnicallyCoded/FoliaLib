package com.tcoded.folialib.wrapper.task;

import com.tcoded.folialib.wrapper.WrappedTask;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public record WrappedBukkitTask(BukkitTask task) implements WrappedTask {

    @Override
    public void cancel() {
        this.task.cancel();
    }

    @Override
    public boolean isCancelled() {
        return this.task.isCancelled();
    }

    @Override
    public Plugin getOwningPlugin() {
        return this.task.getOwner();
    }
}
