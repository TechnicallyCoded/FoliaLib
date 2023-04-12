package com.tcoded.folialib.impl;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.enums.EntityTaskResult;
import com.tcoded.folialib.util.TimeConverter;
import com.tcoded.folialib.wrapper.WrappedTask;
import com.tcoded.folialib.wrapper.task.WrappedBukkitTask;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SpigotImplementation implements ServerImplementation {

    private final JavaPlugin plugin;
    @SuppressWarnings("deprecation")
    private final @NotNull BukkitScheduler scheduler;

    public SpigotImplementation(FoliaLib foliaLib) {
        this.plugin = foliaLib.getPlugin();
        this.scheduler = plugin.getServer().getScheduler();
    }

    @Override
    public CompletableFuture<Void> runNextTick(Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.scheduler.runTask(plugin, () -> {
            runnable.run();
            future.complete(null);
        });

        return future;
    }

    @Override
    public CompletableFuture<Void> runAsync(Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.scheduler.runTaskAsynchronously(plugin, () -> {
            runnable.run();
            future.complete(null);
        });

        return future;
    }

    @Override
    public WrappedTask runLater(Runnable runnable, long delay, TimeUnit unit) {
        return new WrappedBukkitTask(
                this.scheduler.runTaskLater(plugin, runnable, TimeConverter.toTicks(delay, unit))
        );
    }

    @Override
    public WrappedTask runLaterAsync(Runnable runnable, long delay, TimeUnit unit) {
        return new WrappedBukkitTask(
                this.scheduler.runTaskLaterAsynchronously(plugin, runnable, TimeConverter.toTicks(delay, unit))
        );
    }

    @Override
    public WrappedTask runTimer(Runnable runnable, long delay, long period, TimeUnit unit) {
        return new WrappedBukkitTask(
                this.scheduler.runTaskTimer(
                        plugin, runnable,
                        TimeConverter.toTicks(delay, unit),
                        TimeConverter.toTicks(period, unit))
        );
    }

    @Override
    public WrappedTask runTimerAsync(Runnable runnable, long delay, long period, TimeUnit unit) {
        return new WrappedBukkitTask(
                this.scheduler.runTaskTimerAsynchronously(
                        plugin, runnable,
                        TimeConverter.toTicks(delay, unit),
                        TimeConverter.toTicks(period, unit))
        );
    }

    @Override
    public CompletableFuture<Void> runAtLocation(Location location, Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.scheduler.runTask(plugin, () -> {
            runnable.run();
            future.complete(null);
        });

        return future;
    }

    @Override
    public WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay, TimeUnit unit) {
        return new WrappedBukkitTask(
                this.scheduler.runTaskLater(plugin, runnable, TimeConverter.toTicks(delay, unit))
        );
    }

    @Override
    public WrappedTask runAtLocationTimer(Location location, Runnable runnable, long delay, long period, TimeUnit unit) {
        return new WrappedBukkitTask(
                this.scheduler.runTaskTimer(
                        plugin, runnable,
                        TimeConverter.toTicks(delay, unit),
                        TimeConverter.toTicks(period, unit))
        );
    }

    @Override
    public CompletableFuture<EntityTaskResult> runAtEntity(Entity entity, Runnable runnable) {
        CompletableFuture<EntityTaskResult> future = new CompletableFuture<>();

        this.scheduler.runTask(plugin, () -> {
            runnable.run();
            future.complete(EntityTaskResult.SUCCESS);
        });

        return future;
    }

    @Override
    public CompletableFuture<EntityTaskResult> runAtEntityWithFallback(Entity entity, Runnable runnable, Runnable fallback) {
        CompletableFuture<EntityTaskResult> future = new CompletableFuture<>();

        this.scheduler.runTask(plugin, () -> {
            if (entity.isValid()) {
                runnable.run();
                future.complete(EntityTaskResult.SUCCESS);
            } else {
                fallback.run();
                future.complete(EntityTaskResult.ENTITY_RETIRED);
            }
        });

        return future;
    }

    @Override
    public WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay, TimeUnit unit) {
        return new WrappedBukkitTask(
                this.scheduler.runTaskLater(plugin, runnable, TimeConverter.toTicks(delay, unit))
        );
    }

    @Override
    public WrappedTask runAtEntityTimer(Entity entity, Runnable runnable, long delay, long period, TimeUnit unit) {
        return new WrappedBukkitTask(
                this.scheduler.runTaskTimer(
                        plugin, runnable,
                        TimeConverter.toTicks(delay, unit),
                        TimeConverter.toTicks(period, unit))
        );
    }

    @Override
    public void cancelTask(WrappedTask task) {
        task.cancel();
    }

    @Override
    public void cancelAllTasks() {
        this.scheduler.cancelTasks(plugin);
    }

    @Override
    public Player getPlayer(String name) {
        // Already on the main thread
        if (this.plugin.getServer().isPrimaryThread()) {
            return this.plugin.getServer().getPlayer(name);
        }
        // Not on the main thread, we need to wait until the next tick
        else {
            try {
                return this.scheduler.callSyncMethod(plugin, () -> this.plugin.getServer().getPlayer(name)).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        // Fallback to null
        return null;
    }

    @Override
    public Player getPlayerExact(String name) {
        // Already on the main thread
        if (this.plugin.getServer().isPrimaryThread()) {
            return this.plugin.getServer().getPlayerExact(name);
        }
        // Not on the main thread, we need to wait until the next tick
        else {
            try {
                return this.scheduler.callSyncMethod(plugin, () -> this.plugin.getServer().getPlayerExact(name)).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        // Fallback to null
        return null;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public Player getPlayer(UUID uuid) {
        // Already on the main thread
        if (this.plugin.getServer().isPrimaryThread()) {
            return this.plugin.getServer().getPlayer(uuid);
        }
        // Not on the main thread, we need to wait until the next tick
        else {
            try {
                return this.scheduler.callSyncMethod(plugin, () -> this.plugin.getServer().getPlayer(uuid)).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        // Fallback to null
        return null;
    }
}
