package com.tcoded.folialib.impl;

import com.tcoded.folialib.enums.EntityTaskResult;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface ServerImplementation {

    // ----- Run now -----

    /**
     * Folia: Synced with the server daylight cycle tick
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param consumer Task to run
     * @return Future when the task is completed
     */
    CompletableFuture<Void> runNextTick(Consumer<WrappedTask> consumer);

    /**
     * Folia: Async
     * Paper: Async
     * Spigot: Async
     *
     * @param consumer Task to run
     * @return Future when the task is completed
     */
    CompletableFuture<Void> runAsync(Consumer<WrappedTask> consumer);

    // ----- Run Later -----

    /**
     * Folia: Synced with the server daylight cycle tick
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param runnable Task to run
     * @param delay    Delay before execution in ticks
     * @return WrappedTask instance
     */
    WrappedTask runLater(Runnable runnable, long delay);

    /**
     * Folia: Synced with the server daylight cycle tick
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param consumer Task to run
     * @param delay    Delay before execution in ticks
     */
    void runLater(Consumer<WrappedTask> consumer, long delay);

    /**
     * Folia: Synced with the server daylight cycle tick
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param runnable Task to run
     * @param delay    Delay before execution
     * @param unit     Time unit
     * @return WrappedTask instance
     */
    WrappedTask runLater(Runnable runnable, long delay, TimeUnit unit);

    /**
     * Folia: Synced with the server daylight cycle tick
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param consumer Task to run
     * @param delay    Delay before execution
     * @param unit     Time unit
     */
    void runLater(Consumer<WrappedTask> consumer, long delay, TimeUnit unit);

    /**
     * Folia: Async
     * Paper: Async
     * Spigot: Async
     *
     * @param runnable Task to run
     * @param delay    Delay before execution in ticks
     * @return WrappedTask instance
     */
    WrappedTask runLaterAsync(Runnable runnable, long delay);

    /**
     * Folia: Async
     * Paper: Async
     * Spigot: Async
     *
     * @param consumer Task to run
     * @param delay    Delay before execution in ticks
     */
    void runLaterAsync(Consumer<WrappedTask> consumer, long delay);

    /**
     * Folia: Async
     * Paper: Async
     * Spigot: Async
     *
     * @param runnable Task to run
     * @param delay    Delay before execution
     * @param unit     Time unit
     * @return WrappedTask instance
     */
    WrappedTask runLaterAsync(Runnable runnable, long delay, TimeUnit unit);

    /**
     * Folia: Async
     * Paper: Async
     * Spigot: Async
     *
     * @param consumer Task to run
     * @param delay    Delay before execution
     * @param unit     Time unit
     */
    void runLaterAsync(Consumer<WrappedTask> consumer, long delay, TimeUnit unit);

    // ----- Global Timers -----

    /**
     * Folia: Synced with the server daylight cycle tick
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param runnable Task to run
     * @param delay    Delay before first execution in ticks
     * @param period   Delay between executions in ticks
     * @return WrappedTask instance
     */
    WrappedTask runTimer(Runnable runnable, long delay, long period);

    /**
     * Folia: Synced with the server daylight cycle tick
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param consumer Task to run
     * @param delay    Delay before first execution in ticks
     * @param period   Delay between executions in ticks
     */
    void runTimer(Consumer<WrappedTask> consumer, long delay, long period);

    /**
     * Folia: Synced with the server daylight cycle tick
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param runnable Task to run
     * @param delay    Delay before first execution
     * @param period   Delay between executions
     * @param unit     Time unit
     * @return WrappedTask instance
     */
    WrappedTask runTimer(Runnable runnable, long delay, long period, TimeUnit unit);

    /**
     * Folia: Synced with the server daylight cycle tick
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param consumer Task to run
     * @param delay    Delay before first execution
     * @param period   Delay between executions
     * @param unit     Time unit
     */
    void runTimer(Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit);

    /**
     * Folia: Async
     * Paper: Async
     * Spigot: Async
     *
     * @param runnable Task to run
     * @param delay    Delay before first execution in ticks
     * @param period   Delay between executions in ticks
     * @return WrappedTask instance
     */
    WrappedTask runTimerAsync(Runnable runnable, long delay, long period);

    /**
     * Folia: Async
     * Paper: Async
     * Spigot: Async
     *
     * @param consumer Task to run
     * @param delay    Delay before first execution in ticks
     * @param period   Delay between executions in ticks
     */
    void runTimerAsync(Consumer<WrappedTask> consumer, long delay, long period);

    /**
     * Folia: Async
     * Paper: Async
     * Spigot: Async
     *
     * @param runnable Task to run
     * @param delay    Delay before first execution
     * @param period   Delay between executions
     * @param unit     Time unit
     * @return WrappedTask instance
     */
    WrappedTask runTimerAsync(Runnable runnable, long delay, long period, TimeUnit unit);

    /**
     * Folia: Async
     * Paper: Async
     * Spigot: Async
     *
     * @param consumer Task to run
     * @param delay    Delay before first execution
     * @param period   Delay between executions
     * @param unit     Time unit
     */
    void runTimerAsync(Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit);


    // ----- Location/Region based -----

    /**
     * Folia: Synced with the tick of the region of the chunk of the location
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param location Location to run the task at
     * @param consumer Task to run
     * @return Future when the task is completed
     */
    CompletableFuture<Void> runAtLocation(Location location, Consumer<WrappedTask> consumer);

    /**
     * Folia: Synced with the tick of the region of the chunk of the location
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param location Location to run the task at
     * @param runnable Task to run
     * @param delay    Delay before execution in ticks
     * @return WrappedTask instance
     */
    WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay);

    /**
     * Folia: Synced with the tick of the region of the chunk of the location
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param location Location to run the task at
     * @param consumer Task to run
     * @param delay    Delay before execution in ticks
     */
    void runAtLocationLater(Location location, Consumer<WrappedTask> consumer, long delay);

    /**
     * Folia: Synced with the tick of the region of the chunk of the location
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param location Location to run the task at
     * @param runnable Task to run
     * @param delay    Delay before execution
     * @param unit     Time unit
     * @return WrappedTask instance
     */
    WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay, TimeUnit unit);

    /**
     * Folia: Synced with the tick of the region of the chunk of the location
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param location Location to run the task at
     * @param consumer Task to run
     * @param delay    Delay before execution
     * @param unit     Time unit
     */
    void runAtLocationLater(Location location, Consumer<WrappedTask> consumer, long delay, TimeUnit unit);

    /**
     * Folia: Synced with the tick of the region of the chunk of the location
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param location Location to run the task at
     * @param runnable Task to run
     * @param delay    Delay before first execution in ticks
     * @param period   Delay between executions in ticks
     * @return WrappedTask instance
     */
    WrappedTask runAtLocationTimer(Location location, Runnable runnable, long delay, long period);

    /**
     * Folia: Synced with the tick of the region of the chunk of the location
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param location Location to run the task at
     * @param consumer Task to run
     * @param delay    Delay before first execution in ticks
     * @param period   Delay between executions in ticks
     */
    void runAtLocationTimer(Location location, Consumer<WrappedTask> consumer, long delay, long period);

    /**
     * Folia: Synced with the tick of the region of the chunk of the location
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param location Location to run the task at
     * @param runnable Task to run
     * @param delay    Delay before first execution
     * @param period   Delay between executions
     * @param unit     Time unit
     * @return WrappedTask instance
     */
    WrappedTask runAtLocationTimer(Location location, Runnable runnable, long delay, long period, TimeUnit unit);

    /**
     * Folia: Synced with the tick of the region of the chunk of the location
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param location Location to run the task at
     * @param consumer Task to run
     * @param delay    Delay before first execution
     * @param period   Delay between executions
     * @param unit     Time unit
     */
    void runAtLocationTimer(Location location, Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit);


    // ----- Entity based -----

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param entity   Entity to run the task at
     * @param consumer Task to run
     * @return Future when the task is completed
     */
    CompletableFuture<EntityTaskResult> runAtEntity(Entity entity, Consumer<WrappedTask> consumer);

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param entity   Entity to run the task at
     * @param consumer Task to run
     * @return Future when the task is completed
     */
    CompletableFuture<EntityTaskResult> runAtEntityWithFallback(Entity entity, Consumer<WrappedTask> consumer, Runnable fallback);

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param entity   Entity to run the task at
     * @param runnable Task to run
     * @param delay    Delay before execution in ticks
     * @return WrappedTask instance
     */
    WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay);

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param entity   Entity to run the task at
     * @param consumer Task to run
     * @param delay    Delay before execution in ticks
     */
    void runAtEntityLater(Entity entity, Consumer<WrappedTask> consumer, long delay);

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param entity   Entity to run the task at
     * @param runnable Task to run
     * @param delay    Delay before execution
     * @param unit     Time unit
     * @return WrappedTask instance
     */
    WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay, TimeUnit unit);

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param entity   Entity to run the task at
     * @param consumer Task to run
     * @param delay    Delay before execution
     * @param unit     Time unit
     */
    void runAtEntityLater(Entity entity, Consumer<WrappedTask> consumer, long delay, TimeUnit unit);

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param entity   Entity to run the task at
     * @param runnable Task to run
     * @param delay    Delay before first execution in ticks
     * @param period   Delay between executions in ticks
     * @return WrappedTask instance
     */
    WrappedTask runAtEntityTimer(Entity entity, Runnable runnable, long delay, long period);

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param entity   Entity to run the task at
     * @param consumer Task to run
     * @param delay    Delay before first execution in ticks
     * @param period   Delay between executions in ticks
     */
    void runAtEntityTimer(Entity entity, Consumer<WrappedTask> consumer, long delay, long period);

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param entity   Entity to run the task at
     * @param runnable Task to run
     * @param delay    Delay before first execution
     * @param period   Delay between executions
     * @param unit     Time unit
     * @return WrappedTask instance
     */
    WrappedTask runAtEntityTimer(Entity entity, Runnable runnable, long delay, long period, TimeUnit unit);

    /**
     * Folia: Synced with the tick of the region of the entity (even if the entity moves)
     * Paper: Synced with the server main thread
     * Spigot: Synced with the server main thread
     *
     * @param entity   Entity to run the task at
     * @param consumer Task to run
     * @param delay    Delay before first execution
     * @param period   Delay between executions
     * @param unit     Time unit
     */
    void runAtEntityTimer(Entity entity, Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit);

    /**
     * Cancel a task
     *
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
     *
     * @param name Name of the player
     * @return Player instance or null if not found
     */
    Player getPlayer(String name);

    /**
     * Get a player by name (exactly)
     * When using folia, this can be run sync or async. If this is run async on non-folia platforms, it will block
     * until the next tick to get the player safely.
     *
     * @param name Name of the player
     * @return Player instance or null if not found
     */
    Player getPlayerExact(String name);

    /**
     * Get a player by UUID
     * When using folia, this can be run sync or async. If this is run async on non-folia platforms, it will block
     * until the next tick to get the player safely.
     *
     * @param uuid UUID of the player
     * @return Player instance or null if not found
     */
    Player getPlayer(UUID uuid);

    /**
     * Teleport a player to a location async
     *
     * @return Future when the teleport is completed or failed
     */
    CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, TeleportCause cause);

    default CompletableFuture<Boolean> teleportAsync(Entity entity, Location location) {
        return teleportAsync(entity, location, TeleportCause.PLUGIN);
    }

    /**
     * Wraps a native task (Folia or Bukkit) into a WrappedTask
     *
     * @param nativeTask The native task object
     * @return WrappedTask instance
     */
    WrappedTask wrapTask(Object nativeTask);
}
