package com.tcoded.folialib.impl;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.enums.EntityTaskResult;
import com.tcoded.folialib.util.TimeConverter;
import com.tcoded.folialib.wrapper.task.WrappedBukkitTask;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class SpigotImplementation implements ServerImplementation {

    private final JavaPlugin plugin;
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
    public void runLater(Consumer<WrappedTask> task, long delay, TimeUnit unit) {
        handleBukkitTask(wrappedTask -> this.scheduler.runTaskLater(
                plugin,
                () -> task.accept(wrappedTask.get()),
                TimeConverter.toTicks(delay, unit)
        ));
    }

    @Override
    public WrappedTask runLaterAsync(Runnable runnable, long delay, TimeUnit unit) {
        return new WrappedBukkitTask(
                this.scheduler.runTaskLaterAsynchronously(plugin, runnable, TimeConverter.toTicks(delay, unit))
        );
    }

    @Override
    public void runLaterAsync(Consumer<WrappedTask> task, long delay, TimeUnit unit) {
        handleBukkitTask(wrappedTask -> this.scheduler.runTaskLaterAsynchronously(
                plugin,
                () -> task.accept(wrappedTask.get()),
                TimeConverter.toTicks(delay, unit)
        ));
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
    public void runTimer(Consumer<WrappedTask> task, long delay, long period, TimeUnit unit) {
        handleBukkitTask(wrappedTask -> this.scheduler.runTaskTimer(
                plugin,
                () -> task.accept(wrappedTask.get()),
                TimeConverter.toTicks(delay, unit),
                TimeConverter.toTicks(period, unit)
        ));
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
    public void runTimerAsync(Consumer<WrappedTask> task, long delay, long period, TimeUnit unit) {
        handleBukkitTask(wrappedTask -> this.scheduler.runTaskTimerAsynchronously(
                plugin,
                () -> task.accept(wrappedTask.get()),
                TimeConverter.toTicks(delay, unit),
                TimeConverter.toTicks(period, unit)
        ));
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
    public void runAtLocationLater(Location location, Consumer<WrappedTask> task, long delay, TimeUnit unit) {
        runLater(task, delay, unit);
    }

    @Override
    public WrappedTask runAtLocationTimer(Location location, Runnable runnable, long delay, long period, TimeUnit unit) {
        return new WrappedBukkitTask(
                this.scheduler.runTaskTimer(
                        plugin, runnable,
                        TimeConverter.toTicks(delay, unit),
                        TimeConverter.toTicks(period, unit)
                )
        );
    }

    @Override
    public void runAtLocationTimer(Location location, Consumer<WrappedTask> task, long delay, long period, TimeUnit unit) {
        runTimer(task, delay, period, unit);
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
    public void runAtEntityLater(Entity entity, Consumer<WrappedTask> task, long delay, TimeUnit unit) {
        runLater(task, delay, unit);
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
    public void runAtEntityTimer(Entity entity, Consumer<WrappedTask> task, long delay, long period, TimeUnit unit) {
        runTimer(task, delay, period, unit);
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

    private void handleBukkitTask(Function<Supplier<WrappedTask>, BukkitTask> function) {
        final AtomicReference<WrappedTask> taskAtomicReference = new AtomicReference<>();
        final BukkitTask bukkitTask = function.apply(taskAtomicReference::get);
        taskAtomicReference.set(new WrappedBukkitTask(bukkitTask));
    }
}
