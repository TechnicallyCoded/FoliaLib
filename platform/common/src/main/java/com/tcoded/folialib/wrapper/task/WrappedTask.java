package com.tcoded.folialib.wrapper.task;

import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public interface WrappedTask {

    void cancel();

    boolean isCancelled();

    Plugin getOwningPlugin();

    /**
     * Whether the task is async or not
     * <p>
     * Async tasks are never run on any world threads, including on Folia
     *
     * @return true if the task is async
     */
    boolean isAsync();

}
