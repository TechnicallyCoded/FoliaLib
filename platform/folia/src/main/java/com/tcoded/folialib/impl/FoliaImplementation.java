package com.tcoded.folialib.impl;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.enums.EntityTaskResult;
import com.tcoded.folialib.util.InvalidTickDelayNotifier;
import com.tcoded.folialib.util.TimeConverter;
import com.tcoded.folialib.wrapper.task.WrappedFoliaTask;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


@SuppressWarnings("unused")
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
    public CompletableFuture<Void> runNextTick(Consumer<WrappedTask> consumer) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.globalRegionScheduler.run(plugin, task -> {
            consumer.accept(this.wrapTask(task));
            future.complete(null);
        });

        return future;
    }

    @Override
    public CompletableFuture<Void> runAsync(Consumer<WrappedTask> consumer) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.asyncScheduler.runNow(plugin, task -> {
            consumer.accept(this.wrapTask(task));
            future.complete(null);
        });

        return future;
    }

    @Override
    public WrappedTask runLater(Runnable runnable, long delay) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        return this.wrapTask(this.globalRegionScheduler.runDelayed(plugin, task -> runnable.run(), delay));
    }

    @Override
    public void runLater(Consumer<WrappedTask> consumer, long delay) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        this.globalRegionScheduler.runDelayed(plugin, task -> consumer.accept(this.wrapTask(task)), delay);
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
        return this.runLaterAsync(runnable, TimeConverter.toMillis(delay), TimeUnit.MILLISECONDS);
    }

    @Override
    public void runLaterAsync(Consumer<WrappedTask> consumer, long delay) {
        this.runLaterAsync(consumer, TimeConverter.toMillis(delay), TimeUnit.MILLISECONDS);
    }

    @Override
    public WrappedTask runLaterAsync(Runnable runnable, long delay, TimeUnit unit) {
        return this.wrapTask(
                this.asyncScheduler.runDelayed(plugin, task -> runnable.run(), delay, unit)
        );
    }

    @Override
    public void runLaterAsync(Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
        this.asyncScheduler.runDelayed(plugin, task -> consumer.accept(this.wrapTask(task)), delay, unit);
    }

    @Override
    public WrappedTask runTimer(Runnable runnable, long delay, long period) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        if (period <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), period);
            period = 1;
        }
        return this.wrapTask(
                this.globalRegionScheduler.runAtFixedRate(plugin, task -> runnable.run(), delay, period)
        );
    }

    @Override
    public void runTimer(Consumer<WrappedTask> consumer, long delay, long period) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        if (period <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), period);
            period = 1;
        }
        this.globalRegionScheduler.runAtFixedRate(plugin, task -> consumer.accept(this.wrapTask(task)), delay, period);
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
        return this.runTimerAsync(
                runnable, TimeConverter.toMillis(delay), TimeConverter.toMillis(period), TimeUnit.MILLISECONDS
        );
    }

    @Override
    public void runTimerAsync(Consumer<WrappedTask> consumer, long delay, long period) {
        this.runTimerAsync(
                consumer, TimeConverter.toMillis(delay), TimeConverter.toMillis(period), TimeUnit.MILLISECONDS
        );
    }

    @Override
    public WrappedTask runTimerAsync(Runnable runnable, long delay, long period, TimeUnit unit) {
        return this.wrapTask(
                this.asyncScheduler.runAtFixedRate(plugin, task -> runnable.run(), delay, period, unit)
        );
    }

    @Override
    public void runTimerAsync(Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
        this.asyncScheduler.runAtFixedRate(plugin, task -> consumer.accept(this.wrapTask(task)), delay, period, unit);
    }

    @Override
    public CompletableFuture<Void> runAtLocation(Location location, Consumer<WrappedTask> consumer) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.plugin.getServer().getRegionScheduler().run(plugin, location, task -> {
            consumer.accept(this.wrapTask(task));
            future.complete(null);
        });

        return future;
    }

    @Override
    public WrappedTask runAtLocationLater(Location location, Runnable runnable, long delay) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        return this.wrapTask(
                this.plugin.getServer().getRegionScheduler().runDelayed(plugin, location, task -> runnable.run(), delay)
        );
    }

    @Override
    public void runAtLocationLater(Location location, Consumer<WrappedTask> consumer, long delay) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        this.plugin.getServer().getRegionScheduler().runDelayed(plugin, location, task -> consumer.accept(this.wrapTask(task)), delay);
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
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        if (period <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), period);
            period = 1;
        }
        return this.wrapTask(
                this.plugin.getServer().getRegionScheduler().runAtFixedRate(plugin, location, task -> runnable.run(), delay, period)
        );
    }

    @Override
    public void runAtLocationTimer(Location location, Consumer<WrappedTask> consumer, long delay, long period) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        if (period <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), period);
            period = 1;
        }
        this.plugin.getServer().getRegionScheduler().runAtFixedRate(plugin, location, task -> consumer.accept(this.wrapTask(task)), delay, period);
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

        ScheduledTask scheduledTask = entity.getScheduler().run(this.plugin, task -> {
            consumer.accept(this.wrapTask(task));
            future.complete(EntityTaskResult.SUCCESS);
        }, null);

        if (scheduledTask == null) {
            future.complete(EntityTaskResult.SCHEDULER_RETIRED);
        }

        return future;
    }

    @Override
    public CompletableFuture<EntityTaskResult> runAtEntityWithFallback(Entity entity, Consumer<WrappedTask> consumer, Runnable fallback) {
        CompletableFuture<EntityTaskResult> future = new CompletableFuture<>();

        ScheduledTask scheduledTask = entity.getScheduler().run(this.plugin, task -> {
            consumer.accept(this.wrapTask(task));
            future.complete(EntityTaskResult.SUCCESS);
        }, () -> {
            fallback.run();
            future.complete(EntityTaskResult.ENTITY_RETIRED);
        });

        if (scheduledTask == null) {
            future.complete(EntityTaskResult.SCHEDULER_RETIRED);
        }

        return future;
    }

    @Override
    public WrappedTask runAtEntityLater(Entity entity, Runnable runnable, long delay) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        return this.wrapTask(entity.getScheduler().runDelayed(plugin, task -> runnable.run(), null, delay));
    }

    @Override
    public void runAtEntityLater(Entity entity, Consumer<WrappedTask> consumer, long delay) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        entity.getScheduler().runDelayed(plugin, task -> consumer.accept(this.wrapTask(task)), null, delay);
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
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        if (period <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), period);
            period = 1;
        }
        return this.wrapTask(
                entity.getScheduler().runAtFixedRate(plugin, task -> runnable.run(), null, delay, period)
        );
    }

    @Override
    public void runAtEntityTimer(Entity entity, Consumer<WrappedTask> consumer, long delay, long period) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        if (period <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), period);
            period = 1;
        }
        entity.getScheduler().runAtFixedRate(plugin, task -> consumer.accept(this.wrapTask(task)), null, delay, period);
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
    public CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, PlayerTeleportEvent.TeleportCause cause) {
        return entity.teleportAsync(location, cause);
    }

    @Override
    public WrappedTask wrapTask(Object nativeTask) {
        if (!(nativeTask instanceof ScheduledTask)) {
            throw new IllegalArgumentException("The nativeTask provided must be a ScheduledTask. Got: " + nativeTask.getClass().getName() + " instead.");
        }

        return new WrappedFoliaTask((ScheduledTask) nativeTask);
    }
}
