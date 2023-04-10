package com.tcoded.folialib.impl;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.enums.ThreadScope;
import com.tcoded.folialib.util.TimeConverter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class SpigotImplementation implements ServerImplementation {

    private final JavaPlugin plugin;
    private final @NotNull BukkitScheduler scheduler;

    public SpigotImplementation(FoliaLib foliaLib) {
        this.plugin = foliaLib.getPlugin();
        this.scheduler = plugin.getServer().getScheduler();
    }


    @Override
    public void runLater(Runnable runnable, long delay, TimeUnit unit) {
        this.runLater(ThreadScope.SYNC, runnable, delay, unit);
    }

    @Override
    public void runLater(ThreadScope scope, Runnable runnable, long delay, TimeUnit unit) {
        switch (scope) {
            case ASYNC:
                this.scheduler.runTaskLaterAsynchronously(plugin, runnable, TimeConverter.toTicks(delay, unit));
                break;
            default:
                this.scheduler.runTaskLater(plugin, runnable, TimeConverter.toTicks(delay, unit));
                break;
        }
    }

    @Override
    public void runTimer(Runnable runnable, long delay, long period, TimeUnit unit) {
        this.runTimer(ThreadScope.SYNC, runnable, delay, period, unit);
    }

    @Override
    public void runTimer(ThreadScope scope, Runnable runnable, long delay, long period, TimeUnit unit) {
        switch (scope) {
            case ASYNC:
                this.scheduler.runTaskTimerAsynchronously(plugin, runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
                break;
            default:
                this.scheduler.runTaskTimer(plugin, runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
                break;
        }
    }

    @Override
    public void runInGlobalScope(ThreadScope scope, Runnable runnable) {
        switch (scope) {
            case ASYNC:
                this.scheduler.runTaskAsynchronously(plugin, runnable);
                break;
            default:
                this.scheduler.runTask(plugin, runnable);
                break;
        }
    }

    @Override
    public void runInRegion(Location location, Runnable runnable) {
        this.runInGlobalScope(ThreadScope.SYNC, runnable);
    }

    @Override
    public void runInPlayerRegion(Player player, Runnable runnable) {
        this.runInGlobalScope(ThreadScope.SYNC, runnable);
    }
}
