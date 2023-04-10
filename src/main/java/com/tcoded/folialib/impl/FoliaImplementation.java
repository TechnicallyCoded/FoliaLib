package com.tcoded.folialib.impl;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.enums.ThreadScope;
import com.tcoded.folialib.util.TimeConverter;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public class FoliaImplementation implements ServerImplementation {

    private final JavaPlugin plugin;
    private final GlobalRegionScheduler globalRegionScheduler;
    private final AsyncScheduler asyncScheduler;

    public FoliaImplementation(FoliaLib foliaLib) {
        this.plugin = foliaLib.getPlugin();
        this.globalRegionScheduler = plugin.getServer().getGlobalRegionScheduler();
        this.asyncScheduler = plugin.getServer().getAsyncScheduler();
    }

    @Override
    public void runLater(Runnable runnable, long delay, TimeUnit unit) {
        this.runLater(ThreadScope.ASYNC, runnable, delay, unit);
    }

    @Override
    public void runLater(ThreadScope scope, Runnable runnable, long delay, TimeUnit unit) {
        switch (scope) {
            case ASYNC:
                this.asyncScheduler.runDelayed(plugin, task -> runnable.run(), delay, unit);
                break;
            default:
                this.globalRegionScheduler.runDelayed(plugin, task -> runnable.run(), TimeConverter.toTicks(delay, unit));
                break;
        }
    }

    @Override
    public void runTimer(Runnable runnable, long delay, long period, TimeUnit unit) {
        this.runTimer(ThreadScope.ASYNC, runnable, delay, period, unit);
    }

    @Override
    public void runTimer(ThreadScope scope, Runnable runnable, long delay, long period, TimeUnit unit) {
        switch (scope) {
            case ASYNC:
                this.asyncScheduler.runAtFixedRate(plugin, task -> runnable.run(), delay, period, unit);
                break;
            default:
                this.globalRegionScheduler.runAtFixedRate(plugin, task -> runnable.run(), TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
                break;
        }
    }

    @Override
    public void runInGlobalScope(ThreadScope scope, Runnable runnable) {
        switch (scope) {
            case ASYNC:
                this.asyncScheduler.runNow(plugin, task -> runnable.run());
                break;
            default:
                this.globalRegionScheduler.execute(plugin, runnable);
                break;
        }
    }

    @Override
    public void runInRegionScope(Location location, Runnable runnable) {
        //this.globalRegionScheduler.//
        this.plugin.getServer().getRegionScheduler().run(plugin, location, task -> runnable.run());
    }

    @Override
    public void runInPlayerRegion(Player player, Runnable runnable) {
        //this.globalRegionScheduler.//
        this.plugin.getServer().getRegionScheduler().run(plugin, player.getLocation(), task -> runnable.run());
    }

}
