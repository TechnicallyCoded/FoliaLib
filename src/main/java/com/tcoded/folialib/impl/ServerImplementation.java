package com.tcoded.folialib.impl;

import com.tcoded.folialib.enums.EntityTaskResult;
import com.tcoded.folialib.wrapper.WrappedTask;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.tcoded.folialib.util.TimeConverter.toMillis;

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
     * @param delay Delay before execution
     * @param unit Time unit
     * @return WrappedTask instance
     */
    WrappedTask runLater(Runnable runnable, long delay, TimeUnit unit);

    /**
     * {@link #runLater(Runnable, long, TimeUnit)} with the delay in ticks
     * @param runnable Task to run
     * @param delay Delay before execution
     * @return WrappedTask instance
     * @see #runLater(Runnable, long, TimeUnit)
     */
    default WrappedTask runLater(Runnable runnable, long delay) {
        return runLater(runnable, toMillis(delay), TimeUnit.MILLISECONDS);
    }

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

    /**
     * {@link #runLaterAsync(Runnable, long, TimeUnit)} with the delay in ticks
     * @param runnable Task to run
     * @param delay Delay before execution
     * @return WrappedTask instance
     * @see #runLaterAsync(Runnable, long, TimeUnit)
     */
    default WrappedTask runLaterAsync(Runnable runnable, long delay) {
        return runLaterAsync(runnable, toMillis(delay), TimeUnit.MILLISECONDS);
    }

    // ----- Global Timers -----

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
    WrappedTask runTimer(Runnable runnable, long delay, long period, TimeUnit unit);

    /**
     * {@link #runTimer(Runnable, long, long, TimeUnit)} with the delay and period in ticks
     * @param runnable Task to run
     * @param delay Delay before first execution
     * @param period Delay between executions
     * @return WrappedTask instance
     * @see #runTimer(Runnable, long, long, TimeUnit)
     */
    default WrappedTask runTimer(Runnable runnable, long delay, long period) {
        return runTimer(runnable, toMillis(delay), toMillis(period), TimeUnit.MILLISECONDS);
    }

    /**
     * Folia: Async
     * Paper: Async
     * Spigot: Async
     * @param runnable Task to run
     * @param delay Delay before first execution
     * @param period Delay between executions
     * @param unit Time unit
     * @return WrappedTask instance
     */
    WrappedTask runTimerAsync(Runnable runnable, long delay, long period, TimeUnit unit);

    /**
     * {@link #runTimerAsync(Runnable, long, long, TimeUnit)} with the delay and period in ticks
     * @param runnable Task to run
     * @param delay Delay before first execution
     * @param period Delay between executions
     * @return WrappedTask instance
     * @see #runTimerAsync(Runnable, long, long, TimeUnit)
     */
    default WrappedTask runTimerAsync(Runnable runnable, long delay, long period) {
        return runTimerAsync(runnable, toMillis(delay), toMillis(period), TimeUnit.MILLISECONDS);
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
     * @param delay Delay before execution
     * @param unit Time unit
     * @return WrappedTask instance
     */
    WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay, TimeUnit unit);

    /**
     * {@link #runAtLocationLater(Location, Runnable, long, TimeUnit)} with the delay in ticks
     * @param location Location to run the task at
     * @param runnable Task to run
     * @param delay Delay before execution
     * @return WrappedTask instance
     * @see #runAtLocationLater(Location, Runnable, long, TimeUnit)
     */
    default WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay) {
        return runAtLocationLater(location, runnable, toMillis(delay), TimeUnit.MILLISECONDS);
    }

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
    WrappedTask runAtLocationTimer(Location location, Runnable runnable, long delay, long period, TimeUnit unit);

    /**
     * {@link #runAtLocationTimer(Location, Runnable, long, long, TimeUnit)} with the delay and period in ticks
     * @param location Location to run the task at
     * @param runnable Task to run
     * @param delay Delay before first execution
     * @param period Delay between executions
     * @return WrappedTask instance
     * @see #runAtLocationTimer(Location, Runnable, long, long, TimeUnit)
     */
    default WrappedTask runAtLocationTimer(Location location, Runnable runnable, long delay, long period) {
        return runAtLocationTimer(location, runnable, toMillis(delay), toMillis(period), TimeUnit.MILLISECONDS);
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
     * @param delay Delay before execution
     * @param unit Time unit
     * @return WrappedTask instance
     */
    WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay, TimeUnit unit);

    /**
     * {@link #runAtEntityLater(Entity, Runnable, long, TimeUnit)} with the delay in ticks
     * @param entity Entity to run the task at
     * @param runnable Task to run
     * @param delay Delay before execution
     * @return WrappedTask instance
     * @see #runAtEntityLater(Entity, Runnable, long, TimeUnit)
     */
    default WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay) {
        return runAtEntityLater(entity, runnable, toMillis(delay), TimeUnit.MILLISECONDS);
    }

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
    WrappedTask runAtEntityTimer(Entity entity, Runnable runnable, long delay, long period, TimeUnit unit);

    /**
     * {@link #runAtEntityTimer(Entity, Runnable, long, long, TimeUnit)} with the delay and period in ticks
     * @param entity Entity to run the task at
     * @param runnable Task to run
     * @param delay Delay before first execution
     * @param period Delay between executions
     * @return WrappedTask instance
     * @see #runAtEntityTimer(Entity, Runnable, long, long, TimeUnit)
     */
    default WrappedTask runAtEntityTimer(Entity entity, Runnable runnable, long delay, long period) {
        return runAtEntityTimer(entity, runnable, toMillis(delay), toMillis(period), TimeUnit.MILLISECONDS);
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
     * Get a player by name (approximately)
     * @param name Name of the player
     * @return Player instance
     */
    Player getPlayer(String name);

    /**
     * Get a player by name (exactly)
     * @param name Name of the player
     * @return Player instance
     */
    Player getPlayerExact(String name);

    /**
     * Get a player by UUID
     * @param uuid UUID of the player
     * @return Player instance
     */
    Player getPlayer(UUID uuid);

    /**
     * Teleport a player to a location async
     * @return Future when the teleport is completed or failed
     */
    CompletableFuture<Boolean> teleportAsync(Player player, Location location);
}
