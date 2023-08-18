package com.tcoded.folialib.wrapper.task;

import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public interface WrappedTask {

    void cancel();

    boolean isCancelled();

    Plugin getOwningPlugin();

}
