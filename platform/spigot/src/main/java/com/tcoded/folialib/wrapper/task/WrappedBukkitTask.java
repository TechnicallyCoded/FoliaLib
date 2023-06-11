package com.tcoded.folialib.wrapper.task;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class WrappedBukkitTask implements WrappedTask {

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
        // todo: this is not correct
        return false; // this.task.isCancelled();
    }

    @Override
    public Plugin getOwningPlugin() {
        return this.task.getOwner();
    }
}
