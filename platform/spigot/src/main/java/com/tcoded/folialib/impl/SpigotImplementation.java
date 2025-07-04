package com.tcoded.folialib.impl;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.enums.EntityTaskResult;
import com.tcoded.folialib.type.Ref;
import com.tcoded.folialib.util.FoliaLibOptions;
import com.tcoded.folialib.util.TimeConverter;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import com.tcoded.folialib.wrapper.task.WrappedBukkitTask;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unused")
public class SpigotImplementation implements PlatformScheduler {

    private final FoliaLib foliaLib;
    private final FoliaLibOptions options;
    private final Plugin plugin;
    private final @NotNull BukkitScheduler scheduler;

    public SpigotImplementation(FoliaLib foliaLib) {
        this.foliaLib = foliaLib;
        this.options = foliaLib.getOptions();
        this.plugin = foliaLib.getPlugin();
        this.scheduler = plugin.getServer().getScheduler();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull Location location) {
        return this.plugin.getServer().isPrimaryThread();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull Location location, int squareRadiusChunks) {
        return this.plugin.getServer().isPrimaryThread();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull Block block) {
        return this.plugin.getServer().isPrimaryThread();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull World world, int chunkX, int chunkZ) {
        return this.plugin.getServer().isPrimaryThread();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull World world, int chunkX, int chunkZ, int squareRadiusChunks) {
        return this.plugin.getServer().isPrimaryThread();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull Entity entity) {
        return this.plugin.getServer().isPrimaryThread();
    }

    @Override
    public boolean isGlobalTickThread() {
        return this.plugin.getServer().isPrimaryThread();
    }

    @Override
    public @NotNull CompletableFuture<Void> runNextTick(@NotNull Consumer<WrappedTask> consumer) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.scheduler.runTask(plugin, (task) -> {
            consumer.accept(this.wrapTask(task));
            future.complete(null);
        });

        return future;
    }

    @Override
    public @NotNull CompletableFuture<Void> runAsync(@NotNull Consumer<WrappedTask> consumer) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.scheduler.runTaskAsynchronously(plugin, (task) -> {
            consumer.accept(this.wrapTask(task));
            future.complete(null);
        });

        return future;
    }

    @Override
    public WrappedTask runLater(@NotNull Runnable runnable, long delay) {
        return this.wrapTask(this.scheduler.runTaskLater(plugin, runnable, delay));
    }

    @Override
    public @NotNull CompletableFuture<Void> runLater(@NotNull Consumer<WrappedTask> consumer, long delay) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.scheduler.runTaskLater(plugin, task -> {
            consumer.accept(this.wrapTask(task));
            future.complete(null);
        }, delay);

        return future;
    }

    @Override
    public WrappedTask runLater(@NotNull Runnable runnable, long delay, TimeUnit unit) {
        return runLater(runnable, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public @NotNull CompletableFuture<Void> runLater(@NotNull Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
        return this.runLater(consumer, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public WrappedTask runLaterAsync(@NotNull Runnable runnable, long delay) {
        return this.wrapTask(this.scheduler.runTaskLaterAsynchronously(plugin, runnable, delay));
    }

    @Override
    public @NotNull CompletableFuture<Void> runLaterAsync(@NotNull Consumer<WrappedTask> consumer, long delay) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.scheduler.runTaskLaterAsynchronously(plugin, task -> {
            consumer.accept(this.wrapTask(task));
            future.complete(null);
        }, delay);

        return future;
    }

    @Override
    public WrappedTask runLaterAsync(@NotNull Runnable runnable, long delay, TimeUnit unit) {
        return this.runLaterAsync(runnable, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public @NotNull CompletableFuture<Void> runLaterAsync(@NotNull Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
        return this.runLaterAsync(consumer, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public WrappedTask runTimer(@NotNull Runnable runnable, long delay, long period) {
        return this.wrapTask(this.scheduler.runTaskTimer(plugin, runnable, delay, period));
    }

    @Override
    public void runTimer(@NotNull Consumer<WrappedTask> consumer, long delay, long period) {
        this.scheduler.runTaskTimer(plugin, task -> consumer.accept(this.wrapTask(task)), delay, period);
    }

    @Override
    public WrappedTask runTimer(@NotNull Runnable runnable, long delay, long period, TimeUnit unit) {
        return this.runTimer(runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

    @Override
    public void runTimer(@NotNull Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
        this.runTimer(consumer, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

    @Override
    public WrappedTask runTimerAsync(@NotNull Runnable runnable, long delay, long period) {
        return this.wrapTask(this.scheduler.runTaskTimerAsynchronously(plugin, runnable, delay, period));
    }

    @Override
    public void runTimerAsync(@NotNull Consumer<WrappedTask> consumer, long delay, long period) {
        this.scheduler.runTaskTimerAsynchronously(plugin, task -> consumer.accept(this.wrapTask(task)), delay, period);
    }

    @Override
    public WrappedTask runTimerAsync(@NotNull Runnable runnable, long delay, long period, TimeUnit unit) {
        return this.runTimerAsync(runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

    @Override
    public void runTimerAsync(@NotNull Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
        this.runTimerAsync(consumer, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

    @Override
    public @NotNull CompletableFuture<Void> runAtLocation(Location location, @NotNull Consumer<WrappedTask> consumer) {
        return this.runNextTick(consumer);
    }

    @Override
    public WrappedTask runAtLocationLater(Location location, @NotNull Runnable runnable, long delay) {
        return this.runLater(runnable, delay);
    }

    @Override
    public @NotNull CompletableFuture<Void> runAtLocationLater(Location location, @NotNull Consumer<WrappedTask> consumer, long delay) {
        return this.runLater(consumer, delay);
    }

    @Override
    public WrappedTask runAtLocationLater(Location location, @NotNull Runnable runnable, long delay, TimeUnit unit) {
        return this.runAtLocationLater(location, runnable, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public @NotNull CompletableFuture<Void> runAtLocationLater(Location location, @NotNull Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
        return this.runAtLocationLater(location, consumer, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public WrappedTask runAtLocationTimer(Location location, @NotNull Runnable runnable, long delay, long period) {
        return this.runTimer(runnable, delay, period);
    }

    @Override
    public void runAtLocationTimer(Location location, @NotNull Consumer<WrappedTask> consumer, long delay, long period) {
        this.runTimer(consumer, delay, period);
    }

    @Override
    public WrappedTask runAtLocationTimer(Location location, @NotNull Runnable runnable, long delay, long period, TimeUnit unit) {
        return this.runAtLocationTimer(location, runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

    @Override
    public void runAtLocationTimer(Location location, @NotNull Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
        this.runAtLocationTimer(location, consumer, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

    @Override
    public @NotNull CompletableFuture<EntityTaskResult> runAtEntity(Entity entity, @NotNull Consumer<WrappedTask> consumer) {
        CompletableFuture<EntityTaskResult> future = new CompletableFuture<>();

        this.runNextTick(task -> {
            consumer.accept(task);
            future.complete(EntityTaskResult.SUCCESS);
        });

        return future;
    }

    @Override
    public @NotNull CompletableFuture<EntityTaskResult> runAtEntityWithFallback(Entity entity, @NotNull Consumer<WrappedTask> consumer, Runnable fallback) {
        CompletableFuture<EntityTaskResult> future = new CompletableFuture<>();

        this.runNextTick(task -> {
            if (isValid(entity)) {
                consumer.accept(task);
                future.complete(EntityTaskResult.SUCCESS);
            } else {
                fallback.run();
                future.complete(EntityTaskResult.ENTITY_RETIRED);
            }
        });

        return future;
    }

    @Override
    public WrappedTask runAtEntityLater(Entity entity, @NotNull Runnable runnable, long delay) {
        return this.runAtEntityLater(entity, runnable, null, delay);
    }

    @Override
    public WrappedTask runAtEntityLater(Entity entity, @NotNull Runnable runnable, Runnable fallback, long delay) {
        if (!isValid(entity)) {
            if (fallback != null) fallback.run();
            return null;
        }
//        return this.runAtEntityLater(entity, runnable, delay); // todo: remove old
        return this.runLater(runnable, delay);
    }

    @Override
    public @NotNull CompletableFuture<Void> runAtEntityLater(Entity entity, @NotNull Consumer<WrappedTask> consumer, long delay) {
        return this.runAtEntityLater(entity, consumer, null, delay);
    }

    @Override
    public @NotNull CompletableFuture<Void> runAtEntityLater(Entity entity, @NotNull Consumer<WrappedTask> consumer, @Nullable Runnable fallback, long delay) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Entity is invalid. Complete the future and run the fallback if provided
        if (!isValid(entity)) {
            future.complete(null);

            if (fallback != null) {
                fallback.run();
            }

            return future;
        }

        this.runLater(task -> {
            // Handle the case where the entity becomes invalid between scheduling and task execution
            if (!isValid(entity)) {
                if (fallback != null) fallback.run();
                future.complete(null);
                return;
            }

            // Everything is healthy, run the consumer and complete the future
            consumer.accept(task);
            future.complete(null);
        }, delay);

        return future;
    }

    @Override
    public WrappedTask runAtEntityLater(Entity entity, @NotNull Runnable runnable, long delay, TimeUnit unit) {
        return this.runAtEntityLater(entity, runnable, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public @NotNull CompletableFuture<Void> runAtEntityLater(Entity entity, @NotNull Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
        return this.runAtEntityLater(entity, consumer, TimeConverter.toTicks(delay, unit));
    }

    @Override
    public WrappedTask runAtEntityTimer(Entity entity, @NotNull Runnable runnable, long delay, long period) {
        return this.runAtEntityTimer(entity, runnable, null, delay, period);
    }

    @Override
    public WrappedTask runAtEntityTimer(Entity entity, @NotNull Runnable runnable, @Nullable Runnable fallback, long delay, long period) {
        if (!isValid(entity)) {
            if (fallback != null) fallback.run();
            return null;
        }

        // This is called a magic trick...
        // Close your eyes!
        Ref<WrappedTask> wtRef = new Ref<>();

        WrappedTask task = this.runTimer(() -> {
            // Handle case where entity becomes invalid. The task should no longer run.
            // We first check if the WrappedTask is non-null, since we never want to call the fallback
            // multiple times: we need to be able to cancel the recurring task,
            WrappedTask wt = wtRef.get();
            if (wt != null && !isValid(entity)) {
                wt.cancel();
                // Run the fallback if provided
                if (fallback != null) fallback.run();
                return;
            }

            // Everything is healthy, run the task.
            runnable.run();
        }, delay, period);

        wtRef.set(task);
        // Ok, you can open your eyes again.

        return task;
    }

    @Override
    public void runAtEntityTimer(Entity entity, @NotNull Consumer<WrappedTask> consumer, long delay, long period) {
        this.runAtEntityTimer(entity, consumer, null, delay, period);
    }

    @Override
    public void runAtEntityTimer(Entity entity, @NotNull Consumer<WrappedTask> consumer, Runnable fallback, long delay, long period) {
        // Sanity check before running once
        if (!isValid(entity)) {
            if (fallback != null) fallback.run();
            return;
        }

        this.runTimer(task -> {
            // Perform sanity check before running each time
            if (!isValid(entity)) {
                if (fallback != null) fallback.run();
                return;
            }
            consumer.accept(task);
        }, delay, period);
    }

    @Override
    public WrappedTask runAtEntityTimer(Entity entity, @NotNull Runnable runnable, long delay, long period, TimeUnit unit) {
        return this.runAtEntityTimer(entity, runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

    @Override
    public void runAtEntityTimer(Entity entity, @NotNull Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
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
    public List<WrappedTask> getAllTasks() {
        return this.scheduler.getPendingTasks().stream()
                .filter(task -> task.getOwner().equals(plugin))
                .map(this::wrapTask)
                .collect(Collectors.toList());
    }

    @Override
    public List<WrappedTask> getAllServerTasks() {
        return this.scheduler.getPendingTasks().stream()
                .map(this::wrapTask)
                .collect(Collectors.toList());
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
    public CompletableFuture<Boolean> teleportAsync(Entity entity, Location location) {
        return this.teleportAsync(entity, location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override
    public CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, PlayerTeleportEvent.TeleportCause cause) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        this.runAtEntity(entity, (task) -> {
            if (isValid(entity)) {
                boolean result = entity.teleport(location);
                future.complete(result);
            } else {
                future.complete(false);
            }
        });

        return future;
    }

    @Override
    public WrappedTask wrapTask(@NotNull Object nativeTask) {
        requireNonNull(nativeTask, "nativeTask cannot be null");
        if (!(nativeTask instanceof BukkitTask)) {
            throw new IllegalArgumentException("The nativeTask provided must be a BukkitTask. Got: " + nativeTask.getClass().getName() + " instead.");
        }

        return new WrappedBukkitTask((BukkitTask) nativeTask);
    }

    private boolean isValid(Entity entity) {
        boolean skipValidCheck = !this.options.useIsValidOnNonFolia();
        if (skipValidCheck) {
            return true; // Skip the valid check if the option is disabled
        }

        if (entity.isValid()) {
            return !(entity instanceof Player) || ((Player) entity).isOnline();
        }

        return entity instanceof Projectile && !entity.isDead();
    }
}
