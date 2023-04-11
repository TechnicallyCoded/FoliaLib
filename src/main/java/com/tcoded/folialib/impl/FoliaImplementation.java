package com.tcoded.folialib.impl;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.enums.EntityTaskResult;
import com.tcoded.folialib.util.TimeConverter;
import com.tcoded.folialib.wrapper.WrappedTask;
import com.tcoded.folialib.wrapper.task.WrappedFoliaTask;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;
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
    public CompletableFuture<Void> runNextTick(Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.globalRegionScheduler.execute(plugin, () -> {
            runnable.run();
            future.complete(null);
        });

        return future;
    }

    @Override
    public CompletableFuture<Void> runAsync(Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.asyncScheduler.runNow(plugin, task -> {
            runnable.run();
            future.complete(null);
        });

        return future;
    }

    @Override
    public WrappedTask runLater(Runnable runnable, long delay, TimeUnit unit) {
        return new WrappedFoliaTask(
                this.globalRegionScheduler.runDelayed(
                        plugin, task -> runnable.run(), TimeConverter.toTicks(delay, unit)
                )
        );
    }

    @Override
    public WrappedTask runLaterAsync(Runnable runnable, long delay, TimeUnit unit) {
        return new WrappedFoliaTask(
                this.asyncScheduler.runDelayed(
                        plugin, task -> runnable.run(), delay, unit
                )
        );
    }

    @Override
    public WrappedTask runTimer(Runnable runnable, long delay, long period, TimeUnit unit) {
        return new WrappedFoliaTask(
                this.globalRegionScheduler.runAtFixedRate(
                        plugin, task -> runnable.run(),
                        TimeConverter.toTicks(delay, unit),
                        TimeConverter.toTicks(period, unit)
                )
        );
    }

    @Override
    public WrappedTask runTimerAsync(Runnable runnable, long delay, long period, TimeUnit unit) {
        return new WrappedFoliaTask(
                this.asyncScheduler.runAtFixedRate(
                        plugin, task -> runnable.run(),
                        delay, period, unit
                )
        );
    }

    @Override
    public CompletableFuture<Void> runAtLocation(Location location, Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.plugin.getServer().getRegionScheduler().execute(plugin, location, () -> {
            runnable.run();
            future.complete(null);
        });

        return future;
    }

    @Override
    public WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay, TimeUnit unit) {
        return new WrappedFoliaTask(
                this.plugin.getServer().getRegionScheduler().runDelayed(
                        plugin, location, task -> runnable.run(),
                        TimeConverter.toTicks(delay, unit)
                )
        );
    }

    @Override
    public WrappedTask runAtLocationTimer(Location location, Runnable runnable, long delay, long period, TimeUnit unit) {
        return new WrappedFoliaTask(
                this.plugin.getServer().getRegionScheduler().runAtFixedRate(
                        plugin, location, task -> runnable.run(),
                        TimeConverter.toTicks(delay, unit),
                        TimeConverter.toTicks(period, unit)
                )
        );
    }

    @Override
    public CompletableFuture<EntityTaskResult> runAtEntity(Entity entity, Runnable runnable) {
        CompletableFuture<EntityTaskResult> future = new CompletableFuture<>();

        boolean success = entity.getScheduler().execute(this.plugin, () -> {
            runnable.run();
            future.complete(EntityTaskResult.SUCCESS);
        }, null, 0);

        if (!success) {
            future.complete(EntityTaskResult.SCHEDULER_RETIRED);
        }

        return future;
    }

    @Override
    public CompletableFuture<EntityTaskResult> runAtEntityWithFallback(Entity entity, Runnable runnable, Runnable fallback) {
        CompletableFuture<EntityTaskResult> future = new CompletableFuture<>();

        boolean success = entity.getScheduler().execute(this.plugin, () -> {
            runnable.run();
            future.complete(EntityTaskResult.SUCCESS);
        }, () -> {
            fallback.run();
            future.complete(EntityTaskResult.ENTITY_RETIRED);
        }, 0);

        if (!success) {
            future.complete(EntityTaskResult.SCHEDULER_RETIRED);
        }

        return future;
    }

    @Override
    public WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay, TimeUnit unit) {
        return new WrappedFoliaTask(
                entity.getScheduler().runDelayed(
                        plugin,
                        task -> runnable.run(),
                        null,
                        TimeConverter.toTicks(delay, unit)
                )
        );
    }

    @Override
    public WrappedTask runAtEntityTimer(Entity entity, Runnable runnable, long delay, long period, TimeUnit unit) {
        return new WrappedFoliaTask(
                entity.getScheduler().runAtFixedRate(
                        plugin,
                        task -> runnable.run(),
                        null,
                        TimeConverter.toTicks(delay, unit),
                        TimeConverter.toTicks(period, unit)
                )
        );
    }
}
