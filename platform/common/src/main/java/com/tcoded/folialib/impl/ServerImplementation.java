package com.tcoded.folialib.impl;

import com.tcoded.folialib.enums.EntityTaskResult;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

@SuppressWarnings("unused")
public interface ServerImplementation {

    // ----- Run now -----

    /**
     * Folia: Synced with the server daylight cycle tick
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param runnable Task to run
     * @return Future when the task is completed
     */
    CompletableFuture<Void> runNextTick(Runnable runnable);

    /**
     * Folia: Async
     * Paper: Async
     * Spigot: Async
     * @param runnable Task to run
     * @return Future when the task is completed
     */
    CompletableFuture<Void> runAsync(Runnable runnable);

    // ----- Run Later -----

    /**
     * Folia: Synced with the server daylight cycle tick
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param runnable Task to run
     * @param delay Delay before execution in ticks
     * @return WrappedTask instance
     */
    WrappedTask runLater(Runnable runnable, long delay);

    /**
     * Folia: Synced with the server daylight cycle tick
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param runnable Task to run
     * @param delay Delay before execution
     * @param unit Time unit
     * @return WrappedTask instance
     */
    WrappedTask runLater(Runnable runnable, long delay, TimeUnit unit);

    /**
     * Folia: Async
     * Paper: Async
     * Spigot: Async
     * @param runnable Task to run
     * @param delay Delay before execution in ticks
     * @return WrappedTask instance
     */
    WrappedTask runLaterAsync(Runnable runnable, long delay);

    /**
     * Folia: Async
     * Paper: Async
     * Spigot: Async
     * @param runnable Task to run
     * @param delay Delay before execution
     * @param unit Time unit
     * @return WrappedTask instance
     */
    WrappedTask runLaterAsync(Runnable runnable, long delay, TimeUnit unit);

    // ----- Global Timers -----

    /**
     * Folia: Synced with the server daylight cycle tick
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param runnable Task to run, return false to stop the timer
     * @param delay Delay before first execution in ticks
     * @param period Delay between executions in ticks
     * @return WrappedTask instance
     */
    WrappedTask runTimer(BooleanSupplier runnable, long delay, long period);

    /**
     * Folia: Synced with the server daylight cycle tick
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param runnable Task to run
     * @param delay Delay before first execution in ticks
     * @param period Delay between executions in ticks
     * @return WrappedTask instance
     */
    default WrappedTask runTimer(Runnable runnable, long delay, long period) {
        return runTimer(() -> {
            runnable.run();
            return true;
        }, delay, period);
    }

    /**
     * Folia: Synced with the server daylight cycle tick
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param runnable Task to run, return false to stop the timer
     * @param delay Delay before first execution
     * @param period Delay between executions
     * @param unit Time unit
     * @return WrappedTask instance
     */
    WrappedTask runTimer(BooleanSupplier runnable, long delay, long period, TimeUnit unit);

    /**
     * Folia: Synced with the server daylight cycle tick
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param runnable Task to run
     * @param delay Delay before first execution
     * @param period Delay between executions
     * @param unit Time unit
     * @return WrappedTask instance
     */
    default WrappedTask runTimer(Runnable runnable, long delay, long period, TimeUnit unit) {
        return runTimer(() -> {
            runnable.run();
            return true;
        }, delay, period, unit);
    }

    /**
     * Folia: Async
     * Paper: Async
     * Spigot: Async
     * @param runnable Task to run, return false to stop the timer
     * @param delay Delay before first execution in ticks
     * @param period Delay between executions in ticks
     * @return WrappedTask instance
     */
    WrappedTask runTimerAsync(BooleanSupplier runnable, long delay, long period);

    /**
     * Folia: Async
     * Paper: Async
     * Spigot: Async
     * @param runnable Task to run
     * @param delay Delay before first execution in ticks
     * @param period Delay between executions in ticks
     * @return WrappedTask instance
     */
    default WrappedTask runTimerAsync(Runnable runnable, long delay, long period) {
        return runTimerAsync(() -> {
            runnable.run();
            return true;
        }, delay, period);
    }

    /**
     * Folia: Async
     * Paper: Async
     * Spigot: Async
     * @param runnable Task to run, return false to stop the timer
     * @param delay Delay before first execution
     * @param period Delay between executions
     * @param unit Time unit
     * @return WrappedTask instance
     */
    WrappedTask runTimerAsync(BooleanSupplier runnable, long delay, long period, TimeUnit unit);

    /**
     * Folia: Async
     * Paper: Async
     * Spigot: Async
     * @param runnable Task to run, return false to stop the timer
     * @param delay Delay before first execution
     * @param period Delay between executions
     * @param unit Time unit
     * @return WrappedTask instance
     */
    default WrappedTask runTimerAsync(Runnable runnable, long delay, long period, TimeUnit unit) {
        return runTimerAsync(() -> {
            runnable.run();
            return true;
        }, delay, period, unit);
    }


    // ----- Location/Region based -----

    /**
     * Folia: Synced with the tick of the region of the chunk of the location
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param location Location to run the task at
     * @param runnable Task to run
     * @return Future when the task is completed
     */
    CompletableFuture<Void> runAtLocation(Location location, Runnable runnable);

    /**
     * Folia: Synced with the tick of the region of the chunk of the location
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param location Location to run the task at
     * @param runnable Task to run
     * @param delay Delay before execution in ticks
     * @return WrappedTask instance
     */
    WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay);

    /**
     * Folia: Synced with the tick of the region of the chunk of the location
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param location Location to run the task at
     * @param runnable Task to run
     * @param delay Delay before execution
     * @param unit Time unit
     * @return WrappedTask instance
     */
    WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay, TimeUnit unit);

    /**
     * Folia: Synced with the tick of the region of the chunk of the location
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param location Location to run the task at
     * @param runnable Task to run, return false to stop the timer
     * @param delay Delay before first execution in ticks
     * @param period Delay between executions in ticks
     * @return WrappedTask instance
     */
    WrappedTask runAtLocationTimer(Location location, BooleanSupplier runnable, long delay, long period);

    /**
     * Folia: Synced with the tick of the region of the chunk of the location
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param location Location to run the task at
     * @param runnable Task to run
     * @param delay Delay before first execution
     * @param period Delay between executions
     * @return WrappedTask instance
     */
    default WrappedTask runAtLocationTimer(Location location, Runnable runnable, long delay, long period) {
        return runAtLocationTimer(location, () -> {
            runnable.run();
            return true;
        }, delay, period);
    }

    /**
     * Folia: Synced with the tick of the region of the chunk of the location
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param location Location to run the task at
     * @param runnable Task to run, return false to stop the timer
     * @param delay Delay before first execution
     * @param period Delay between executions
     * @param unit Time unit
     * @return WrappedTask instance
     */
    WrappedTask runAtLocationTimer(Location location, BooleanSupplier runnable, long delay, long period, TimeUnit unit);

    /**
     * Folia: Synced with the tick of the region of the chunk of the location
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param location Location to run the task at
     * @param runnable Task to run
     * @param delay Delay before first execution
     * @param period Delay between executions
     * @param unit Time unit
     * @return WrappedTask instance
     */
    default WrappedTask runAtLocationTimer(Location location, Runnable runnable, long delay, long period, TimeUnit unit) {
        return runAtLocationTimer(location, () -> {
            runnable.run();
            return true;
        }, delay, period, unit);
    }


    // ----- Entity based -----

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param entity Entity to run the task at
     * @param runnable Task to run
     * @return Future when the task is completed
     */
    CompletableFuture<EntityTaskResult> runAtEntity(Entity entity, Runnable runnable);

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param entity Entity to run the task at
     * @param runnable Task to run
     * @return Future when the task is completed
     */
    CompletableFuture<EntityTaskResult> runAtEntityWithFallback(Entity entity, Runnable runnable, Runnable fallback);

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param entity Entity to run the task at
     * @param runnable Task to run
     * @param delay Delay before execution in ticks
     * @return WrappedTask instance
     */
    WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay);

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param entity Entity to run the task at
     * @param runnable Task to run
     * @param delay Delay before execution
     * @param unit Time unit
     * @return WrappedTask instance
     */
    WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay, TimeUnit unit);

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param entity Entity to run the task at
     * @param runnable Task to run, return false to stop the timer
     * @param delay Delay before first execution in ticks
     * @param period Delay between executions in ticks
     * @return WrappedTask instance
     */
    WrappedTask runAtEntityTimer(Entity entity, BooleanSupplier runnable, long delay, long period);

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param entity Entity to run the task at
     * @param runnable Task to run
     * @param delay Delay before first execution
     * @param period Delay between executions
     * @return WrappedTask instance
     */
    default WrappedTask runAtEntityTimer(Entity entity, Runnable runnable, long delay, long period) {
        return runAtEntityTimer(entity, () -> {
            runnable.run();
            return true;
        }, delay, period);
    }

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param entity Entity to run the task at
     * @param runnable Task to run, return false to stop the timer
     * @param delay Delay before first execution
     * @param period Delay between executions
     * @param unit Time unit
     * @return WrappedTask instance
     */
    WrappedTask runAtEntityTimer(Entity entity, BooleanSupplier runnable, long delay, long period, TimeUnit unit);

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     * @param entity Entity to run the task at
     * @param runnable Task to run
     * @param delay Delay before first execution
     * @param period Delay between executions
     * @param unit Time unit
     * @return WrappedTask instance
     */
    default WrappedTask runAtEntityTimer(Entity entity, Runnable runnable, long delay, long period, TimeUnit unit) {
        return runAtEntityTimer(entity, () -> {
            runnable.run();
            return true;
        }, delay, period, unit);
    }

    /**
     * Cancel a task
     * @param task Task to cancel
     */
    void cancelTask(WrappedTask task);

    /**
     * Cancel all tasks
     */
    void cancelAllTasks();

    /**
     * Get a player by name (approximately).
     * When using folia, this can be run sync or async. If this is run async on non-folia platforms, it will block
     * until the next tick to get the player safely.
     * @param name Name of the player
     * @return Player instance or null if not found
     */
    Player getPlayer(String name);

    /**
     * Get a player by name (exactly)
     * When using folia, this can be run sync or async. If this is run async on non-folia platforms, it will block
     * until the next tick to get the player safely.
     * @param name Name of the player
     * @return Player instance or null if not found
     */
    Player getPlayerExact(String name);

    /**
     * Get a player by UUID
     * When using folia, this can be run sync or async. If this is run async on non-folia platforms, it will block
     * until the next tick to get the player safely.
     * @param uuid UUID of the player
     * @return Player instance or null if not found
     */
    Player getPlayer(UUID uuid);

    /**
     * Teleport a player to a location async
     * @return Future when the teleport is completed or failed
     */
    CompletableFuture<Boolean> teleportAsync(Player player, Location location);
}
