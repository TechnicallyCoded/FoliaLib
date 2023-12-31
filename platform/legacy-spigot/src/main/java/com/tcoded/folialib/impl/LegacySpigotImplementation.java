package com.tcoded.folialib.impl;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.enums.EntityTaskResult;
import com.tcoded.folialib.util.ImplementationTestsUtil;
import com.tcoded.folialib.util.TimeConverter;
import com.tcoded.folialib.wrapper.task.WrappedBukkitTask;
import com.tcoded.folialib.wrapper.task.WrappedLegacyBukkitTask;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class LegacySpigotImplementation implements ServerImplementation {

    private final JavaPlugin plugin;
    private final @NotNull BukkitScheduler scheduler;

    public LegacySpigotImplementation(FoliaLib foliaLib) {
        this.plugin = foliaLib.getPlugin();
        this.scheduler = plugin.getServer().getScheduler();
    }

    @Override
    public CompletableFuture<Void> runNextTick(Consumer<WrappedTask> consumer) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        WrappedTask[] taskReference = new WrappedTask[1];

        taskReference[0] = this.wrapTask(this.scheduler.runTask(plugin, () -> {
            consumer.accept(taskReference[0]);
            future.complete(null);
        }));

        return future;
    }

    @Override
    public CompletableFuture<Void> runAsync(Consumer<WrappedTask> consumer) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        WrappedTask[] taskReference = new WrappedTask[1];

        taskReference[0] = this.wrapTask(this.scheduler.runTaskAsynchronously(plugin, () -> {
            consumer.accept(taskReference[0]);
            future.complete(null);
        }));

        return future;
    }

    @Override
    public WrappedTask runLater(Runnable runnable, long delay) {
        return this.wrapTask(this.scheduler.runTaskLater(plugin, runnable, delay));
    }

    @Override
    public void runLater(Consumer<WrappedTask> consumer, long delay) {
        WrappedTask[] taskReference = new WrappedTask[1];

        taskReference[0] = this.wrapTask(this.scheduler.runTaskLater(plugin, () -> {
            consumer.accept(taskReference[0]);
        }, delay));
    }

    @Override
    public WrappedTask runLater(Runnable runnable, long delay, TimeUnit unit) {
        return this.runLater(runnable, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public void runLater(Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
        this.runLater(consumer, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public WrappedTask runLaterAsync(Runnable runnable, long delay) {
        return this.wrapTask(this.scheduler.runTaskLaterAsynchronously(plugin, runnable, delay));
    }

    @Override
    public void runLaterAsync(Consumer<WrappedTask> consumer, long delay) {
        WrappedTask[] taskReference = new WrappedTask[1];

        taskReference[0] = this.wrapTask(this.scheduler.runTaskLaterAsynchronously(plugin, () -> {
            consumer.accept(taskReference[0]);
        }, delay));
    }

    @Override
    public WrappedTask runLaterAsync(Runnable runnable, long delay, TimeUnit unit) {
        return this.runLaterAsync(runnable, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public void runLaterAsync(Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
        this.runLaterAsync(consumer, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public WrappedTask runTimer(Runnable runnable, long delay, long period) {
        return this.wrapTask(this.scheduler.runTaskTimer(plugin, runnable, delay, period));
    }

    @Override
    public void runTimer(Consumer<WrappedTask> consumer, long delay, long period) {
        WrappedTask[] taskReference = new WrappedTask[1];

        taskReference[0] = this.wrapTask(this.scheduler.runTaskTimer(plugin, () -> {
            consumer.accept(taskReference[0]);
        }, delay, period));
    }

    @Override
    public WrappedTask runTimer(Runnable runnable, long delay, long period, TimeUnit unit) {
        return this.runTimer(runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

    @Override
    public void runTimer(Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
        this.runTimer(consumer, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

    @Override
    public WrappedTask runTimerAsync(Runnable runnable, long delay, long period) {
        return this.wrapTask(this.scheduler.runTaskTimerAsynchronously(plugin, runnable, delay, period));
    }

    @Override
    public void runTimerAsync(Consumer<WrappedTask> consumer, long delay, long period) {
        WrappedTask[] taskReference = new WrappedTask[1];

        taskReference[0] = this.wrapTask(this.scheduler.runTaskTimerAsynchronously(plugin, () -> {
            consumer.accept(taskReference[0]);
        }, delay, period));
    }

    @Override
    public WrappedTask runTimerAsync(Runnable runnable, long delay, long period, TimeUnit unit) {
        return this.runTimerAsync(runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

    @Override
    public void runTimerAsync(Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
        this.runTimerAsync(consumer, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

    @Override
    public CompletableFuture<Void> runAtLocation(Location location, Consumer<WrappedTask> consumer) {
        return this.runNextTick(consumer);
    }

    @Override
    public WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay) {
        return this.wrapTask(this.scheduler.runTaskLater(plugin, runnable, delay));
    }

    @Override
    public void runAtLocationLater(Location location, Consumer<WrappedTask> consumer, long delay) {
        WrappedTask[] taskReference = new WrappedTask[1];

        taskReference[0] = this.wrapTask(this.scheduler.runTaskLater(plugin, () -> {
            consumer.accept(taskReference[0]);
        }, delay));
    }

    @Override
    public WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay, TimeUnit unit) {
        return this.runAtLocationLater(location, runnable, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public void runAtLocationLater(Location location, Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
        this.runAtLocationLater(location, consumer, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public WrappedTask runAtLocationTimer(Location location, Runnable runnable, long delay, long period) {
        return this.wrapTask(this.scheduler.runTaskTimer(plugin, runnable, delay, period));
    }

    @Override
    public void runAtLocationTimer(Location location, Consumer<WrappedTask> consumer, long delay, long period) {
        WrappedTask[] taskReference = new WrappedTask[1];

        taskReference[0] = this.wrapTask(this.scheduler.runTaskTimer(plugin, () -> {
            consumer.accept(taskReference[0]);
        }, delay, period));
    }

    @Override
    public WrappedTask runAtLocationTimer(Location location, Runnable runnable, long delay, long period, TimeUnit unit) {
        return this.runAtLocationTimer(location, runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

    @Override
    public void runAtLocationTimer(Location location, Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
        this.runAtLocationTimer(location, consumer, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

    @Override
    public CompletableFuture<EntityTaskResult> runAtEntity(Entity entity, Consumer<WrappedTask> consumer) {
        CompletableFuture<EntityTaskResult> future = new CompletableFuture<>();
        WrappedTask[] taskReference = new WrappedTask[1];

        taskReference[0] = this.wrapTask(this.scheduler.runTask(plugin, () -> {
            consumer.accept(taskReference[0]);
            future.complete(EntityTaskResult.SUCCESS);
        }));

        return future;
    }

    @Override
    public CompletableFuture<EntityTaskResult> runAtEntityWithFallback(Entity entity, Consumer<WrappedTask> consumer, Runnable fallback) {
        CompletableFuture<EntityTaskResult> future = new CompletableFuture<>();
        WrappedTask[] taskReference = new WrappedTask[1];

        taskReference[0] = this.wrapTask(this.scheduler.runTask(plugin, () -> {
            if (entity.isValid()) {
                consumer.accept(taskReference[0]);
                future.complete(EntityTaskResult.SUCCESS);
            } else {
                fallback.run();
                future.complete(EntityTaskResult.ENTITY_RETIRED);
            }
        }));

        return future;
    }

    @Override
    public WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay) {
        return this.wrapTask(this.scheduler.runTaskLater(plugin, runnable, delay));
    }

    @Override
    public void runAtEntityLater(Entity entity, Consumer<WrappedTask> consumer, long delay) {
        WrappedTask[] taskReference = new WrappedTask[1];

        taskReference[0] = this.wrapTask(this.scheduler.runTaskLater(plugin, () -> {
            consumer.accept(taskReference[0]);
        }, delay));
    }

    @Override
    public WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay, TimeUnit unit) {
        return this.runAtEntityLater(entity, runnable, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public void runAtEntityLater(Entity entity, Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
        this.runAtEntityLater(entity, consumer, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public WrappedTask runAtEntityTimer(Entity entity, Runnable runnable, long delay, long period) {
        return this.wrapTask(this.scheduler.runTaskTimer(plugin, runnable, delay, period));
    }

    @Override
    public void runAtEntityTimer(Entity entity, Consumer<WrappedTask> consumer, long delay, long period) {
        WrappedTask[] taskReference = new WrappedTask[1];

        taskReference[0] = this.wrapTask(this.scheduler.runTaskTimer(plugin, () -> {
            consumer.accept(taskReference[0]);
        }, delay, period));
    }

    @Override
    public WrappedTask runAtEntityTimer(Entity entity, Runnable runnable, long delay, long period, TimeUnit unit) {
        return this.runAtEntityTimer(entity, runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

    @Override
    public void runAtEntityTimer(Entity entity, Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
        this.runAtEntityTimer(entity, consumer, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
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

    @Override
    public CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, PlayerTeleportEvent.TeleportCause cause) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        this.runAtEntity(entity, (task) -> future.complete(entity.teleport(location, cause)));
        return future;
    }

    @Override
    public WrappedTask wrapTask(Object nativeTask) {
        if (!(nativeTask instanceof BukkitTask)) {
            throw new IllegalArgumentException("The nativeTask provided must be a BukkitTask. Got: " + nativeTask.getClass().getName() + " instead.");
        }

        return ImplementationTestsUtil.isCancelledSupported() ?
                new WrappedBukkitTask((BukkitTask) nativeTask) :
                new WrappedLegacyBukkitTask((BukkitTask) nativeTask);
    }

    /**
     * Internal util to get a player regardless of the calling thread
     *
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
}
