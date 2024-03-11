package com.tcoded.folialib.wrapper.task;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;

public class WrappedFoliaTask implements WrappedTask {

    private static final Class<? extends ScheduledTask> ASYNC_TASK_CLASS;

    static {
        Class<? extends ScheduledTask> asyncTaskClass = null;
        try {
            // noinspection unchecked
            asyncTaskClass = (Class<? extends ScheduledTask>) Class.forName("io.papermc.paper.threadedregions.scheduler.FoliaAsyncScheduler.AsyncScheduledTask");
        } catch (ClassNotFoundException e) {
            // ignore
        }
        ASYNC_TASK_CLASS = asyncTaskClass;
    }

    private final ScheduledTask task;

    private final boolean async;

    public WrappedFoliaTask(ScheduledTask task) {
        this.task = task;

        if (ASYNC_TASK_CLASS == null) this.async = false;
        else this.async = ASYNC_TASK_CLASS.isAssignableFrom(task.getClass());
    }

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

    @Override
    public boolean isAsync() {
        return this.async;
    }
}
