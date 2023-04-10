package com.tcoded.folialib.impl;

import com.tcoded.folialib.enums.ThreadScope;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public interface ServerImplementation {

    // Delayed
    void runLater(Runnable runnable, long delay, TimeUnit unit);

    void runLater(ThreadScope scope, Runnable runnable, long delay, TimeUnit unit);

    // Timers
    void runTimer(Runnable runnable, long delay, long period, TimeUnit unit);

    void runTimer(ThreadScope scope, Runnable runnable, long delay, long period, TimeUnit unit);

    // Run now
    void runInGlobalScope(ThreadScope scope, Runnable runnable);

    void runInRegion(Location location, Runnable runnable);

    void runInPlayerRegion(Player player, Runnable runnable);
}
