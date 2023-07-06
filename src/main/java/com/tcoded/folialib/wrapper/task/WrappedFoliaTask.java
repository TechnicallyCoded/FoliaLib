package com.tcoded.folialib.wrapper.task;

import com.tcoded.folialib.wrapper.WrappedTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;

public record WrappedFoliaTask(ScheduledTask task) implements WrappedTask {

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
        return this.task.getOwningPlugin();
    }
}
