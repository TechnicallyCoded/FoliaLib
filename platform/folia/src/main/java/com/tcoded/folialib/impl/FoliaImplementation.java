package com.tcoded.folialib.impl;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.enums.EntityTaskResult;
import com.tcoded.folialib.util.TimeConverter;
import com.tcoded.folialib.wrapper.task.WrappedFoliaTask;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class FoliaImplementation implements ServerImplementation {

    private final JavaPlugin plugin;
    private final GlobalRegionScheduler globalRegionScheduler;
    private final AsyncScheduler asyncScheduler;

    public FoliaImplementation(FoliaLib foliaLib) {
        this.plugin = foliaLib.getPlugin();
        this.globalRegionScheduler = plugin.getServer().getGlobalRegionScheduler();
        this.asyncScheduler = plugin.getServer().getAsyncScheduler();
    }

    private static Consumer<ScheduledTask> wrapRunnable(BooleanSupplier runnable) {
        return task -> {
            if (runnable.getAsBoolean()) {
                task.cancel();
            }
        };
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
    public WrappedTask runLater(Runnable runnable, long delay) {
        return new WrappedFoliaTask(this.globalRegionScheduler.runDelayed(plugin, task -> runnable.run(), delay));
    }

    @Override
    public WrappedTask runLater(Runnable runnable, long delay, TimeUnit unit) {
        return this.runLater(runnable, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public WrappedTask runLaterAsync(Runnable runnable, long delay) {
        return this.runLaterAsync(runnable, TimeConverter.toMillis(delay), TimeUnit.MILLISECONDS);
    }

    @Override
    public WrappedTask runLaterAsync(Runnable runnable, long delay, TimeUnit unit) {
        return new WrappedFoliaTask(
                this.asyncScheduler.runDelayed(plugin, task -> runnable.run(), delay, unit)
        );
    }

    @Override
    public WrappedTask runTimer(BooleanSupplier runnable, long delay, long period) {
        return new WrappedFoliaTask(
                this.globalRegionScheduler.runAtFixedRate(plugin, wrapRunnable(runnable), delay, period)
        );
    }

    @Override
    public WrappedTask runTimer(BooleanSupplier runnable, long delay, long period, TimeUnit unit) {
        return this.runTimer(runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

    @Override
    public WrappedTask runTimerAsync(BooleanSupplier runnable, long delay, long period) {
        return this.runTimerAsync(
                runnable, TimeConverter.toMillis(delay), TimeConverter.toMillis(period), TimeUnit.MILLISECONDS
        );
    }

    @Override
    public WrappedTask runTimerAsync(BooleanSupplier runnable, long delay, long period, TimeUnit unit) {
        return new WrappedFoliaTask(
                this.asyncScheduler.runAtFixedRate(plugin, wrapRunnable(runnable), delay, period, unit)
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
    public WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay) {
        return new WrappedFoliaTask(
                this.plugin.getServer().getRegionScheduler().runDelayed(plugin, location, task -> runnable.run(), delay)
        );
    }

    @Override
    public WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay, TimeUnit unit) {
        return this.runAtLocationLater(location, runnable, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public WrappedTask runAtLocationTimer(Location location, BooleanSupplier runnable, long delay, long period) {
        return new WrappedFoliaTask(
                this.plugin.getServer().getRegionScheduler().runAtFixedRate(plugin, location, wrapRunnable(runnable), delay, period)
        );
    }

    @Override
    public WrappedTask runAtLocationTimer(Location location, BooleanSupplier runnable, long delay, long period, TimeUnit unit) {
        return this.runAtLocationTimer(location, runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
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
    public WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay) {
        return new WrappedFoliaTask(entity.getScheduler().runDelayed(plugin, task -> runnable.run(), null, delay));
    }

    @Override
    public WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay, TimeUnit unit) {
        return this.runAtEntityLater(entity, runnable, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public WrappedTask runAtEntityTimer(Entity entity, BooleanSupplier runnable, long delay, long period) {
        return new WrappedFoliaTask(
                entity.getScheduler().runAtFixedRate(plugin, wrapRunnable(runnable), null, delay, period)
        );
    }

    @Override
    public WrappedTask runAtEntityTimer(Entity entity, BooleanSupplier runnable, long delay, long period, TimeUnit unit) {
        return this.runAtEntityTimer(entity, runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

    @Override
    public void cancelTask(WrappedTask task) {
        task.cancel();
    }

    @Override
    public void cancelAllTasks() {
        this.globalRegionScheduler.cancelTasks(plugin);
        this.asyncScheduler.cancelTasks(plugin);
    }

    @Override
    public Player getPlayer(String name) {
        // This is thread-safe in folia
        return this.plugin.getServer().getPlayer(name);
    }

    @Override
    public Player getPlayerExact(String name) {
        // This is thread-safe in folia
        return this.plugin.getServer().getPlayerExact(name);
    }

    @Override
    public Player getPlayer(UUID uuid) {
        // This is thread-safe in folia
        return this.plugin.getServer().getPlayer(uuid);
    }

    @Override
    public CompletableFuture<Boolean> teleportAsync(Player player, Location location) {
        return player.teleportAsync(location);
    }
}
