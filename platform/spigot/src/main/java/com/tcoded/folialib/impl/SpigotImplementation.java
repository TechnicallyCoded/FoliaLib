package com.tcoded.folialib.impl;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.enums.EntityTaskResult;
import com.tcoded.folialib.util.TimeConverter;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import com.tcoded.folialib.wrapper.task.WrappedBukkitTask;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class SpigotImplementation implements ServerImplementation {

    private final JavaPlugin plugin;
    private final @NotNull BukkitScheduler scheduler;

    public SpigotImplementation(FoliaLib foliaLib) {
        this.plugin = foliaLib.getPlugin();
        this.scheduler = plugin.getServer().getScheduler();
    }

    private static BukkitRunnable wrapRunnable(BooleanSupplier runnable) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (!runnable.getAsBoolean()) {
                    this.cancel();
                }
            }
        };
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
    public WrappedTask runLater(Runnable runnable, long delay) {
        return new WrappedBukkitTask(this.scheduler.runTaskLater(plugin, runnable, delay));
    }

    @Override
    public WrappedTask runLater(Runnable runnable, long delay, TimeUnit unit) {
        return runLater(runnable, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public WrappedTask runLaterAsync(Runnable runnable, long delay) {
        return new WrappedBukkitTask(this.scheduler.runTaskLaterAsynchronously(plugin, runnable, delay));
    }

    @Override
    public WrappedTask runLaterAsync(Runnable runnable, long delay, TimeUnit unit) {
        return this.runLaterAsync(runnable, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public WrappedTask runTimer(BooleanSupplier runnable, long delay, long period) {
        return new WrappedBukkitTask(wrapRunnable(runnable).runTaskTimer(plugin, delay, period));
    }

    @Override
    public WrappedTask runTimer(BooleanSupplier runnable, long delay, long period, TimeUnit unit) {
        return this.runTimer(runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

    @Override
    public WrappedTask runTimerAsync(BooleanSupplier runnable, long delay, long period) {
        return new WrappedBukkitTask(wrapRunnable(runnable).runTaskTimerAsynchronously(plugin, delay, period));
    }

    @Override
    public WrappedTask runTimerAsync(BooleanSupplier runnable, long delay, long period, TimeUnit unit) {
        return this.runTimerAsync(runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
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
    public WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay) {
        return new WrappedBukkitTask(this.scheduler.runTaskLater(plugin, runnable, delay));
    }

    @Override
    public WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay, TimeUnit unit) {
        return this.runAtLocationLater(location, runnable, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public WrappedTask runAtLocationTimer(Location location, BooleanSupplier runnable, long delay, long period) {
        return new WrappedBukkitTask(wrapRunnable(runnable).runTaskTimer(plugin, delay, period));
    }

    @Override
    public WrappedTask runAtLocationTimer(Location location, BooleanSupplier runnable, long delay, long period, TimeUnit unit) {
        return this.runAtLocationTimer(location, runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
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
    public WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay) {
        return new WrappedBukkitTask(this.scheduler.runTaskLater(plugin, runnable, delay));
    }

    @Override
    public WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay, TimeUnit unit) {
        return this.runAtEntityLater(entity, runnable, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public WrappedTask runAtEntityTimer(Entity entity, BooleanSupplier runnable, long delay, long period) {
        return new WrappedBukkitTask(wrapRunnable(runnable).runTaskTimer(plugin, delay, period));
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
        this.scheduler.cancelTasks(plugin);
    }

    @Override
    public Player getPlayer(String name) {
        return this.getPlayerFromMainThread(() -> this.plugin.getServer().getPlayer(name));
    }

    @Override
    public Player getPlayerExact(String name) {
        return this.getPlayerFromMainThread(() -> this.plugin.getServer().getPlayerExact(name));
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return this.getPlayerFromMainThread(() -> this.plugin.getServer().getPlayer(uuid));
    }

    /**
     * Internal util to get a player regardless of the calling thread
     * @param playerSupplier The supplier to get the player
     * @return Player or null if not found
     */
    private Player getPlayerFromMainThread(Supplier<Player> playerSupplier) {

        // Already on the main thread
        if (this.plugin.getServer().isPrimaryThread()) {
            return playerSupplier.get();
        }
        // Not on the main thread, we need to wait until the next tick
        else {
            try {
                return this.scheduler.callSyncMethod(plugin, playerSupplier::get).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Fallback to null
        return null;
    }

    @Override
    public CompletableFuture<Boolean> teleportAsync(Player player, Location location) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        this.runAtEntity(player, () -> {
            if (player.isValid() && player.isOnline()) {
                player.teleport(location);
                future.complete(true);
            } else {
                future.complete(false);
            }
        });

        return future;
    }
}
