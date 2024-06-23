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
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;


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
    public CompletableFuture<Void> runNextTick(@NotNull Consumer<WrappedTask> consumer) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.globalRegionScheduler.run(plugin, task -> {
            consumer.accept(this.wrapTask(task));
            future.complete(null);
        });

        return future;
    }

	@Override
    public CompletableFuture<Void> runAsync(@NotNull Consumer<WrappedTask> consumer) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.asyncScheduler.runNow(plugin, task -> {
            consumer.accept(this.wrapTask(task));
            future.complete(null);
        });

        return future;
    }

	@Override
    public WrappedTask runLater(@NotNull Runnable runnable, long delay) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        return this.wrapTask(this.globalRegionScheduler.runDelayed(plugin, task -> runnable.run(), delay));
    }

	@Override
    public void runLater(@NotNull Consumer<WrappedTask> consumer, long delay) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        this.globalRegionScheduler.runDelayed(plugin, task -> consumer.accept(this.wrapTask(task)), delay);
    }

	@Override
    public WrappedTask runLater(@NotNull Runnable runnable, long delay, TimeUnit unit) {
        return this.runLater(runnable, TimeConverter.toTicks(delay, unit));
    }

	@Override
    public void runLater(@NotNull Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
        this.runLater(consumer, TimeConverter.toTicks(delay, unit));
    }

	@Override
    public WrappedTask runLaterAsync(@NotNull Runnable runnable, long delay) {
        return this.runLaterAsync(runnable, TimeConverter.toMillis(delay), TimeUnit.MILLISECONDS);
    }

	@Override
    public void runLaterAsync(@NotNull Consumer<WrappedTask> consumer, long delay) {
        this.runLaterAsync(consumer, TimeConverter.toMillis(delay), TimeUnit.MILLISECONDS);
    }

	@Override
    public WrappedTask runLaterAsync(@NotNull Runnable runnable, long delay, TimeUnit unit) {
        return this.wrapTask(
                this.asyncScheduler.runDelayed(plugin, task -> runnable.run(), delay, unit)
        );
    }

	@Override
    public void runLaterAsync(@NotNull Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
        this.asyncScheduler.runDelayed(plugin, task -> consumer.accept(this.wrapTask(task)), delay, unit);
    }

	@Override
    public WrappedTask runTimer(@NotNull Runnable runnable, long delay, long period) {
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
    public void runTimer(@NotNull Consumer<WrappedTask> consumer, long delay, long period) {
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
    public WrappedTask runTimer(@NotNull Runnable runnable, long delay, long period, TimeUnit unit) {
        return this.runTimer(runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

	@Override
    public void runTimer(@NotNull Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
        this.runTimer(consumer, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

	@Override
    public WrappedTask runTimerAsync(@NotNull Runnable runnable, long delay, long period) {
        return this.runTimerAsync(
                runnable, TimeConverter.toMillis(delay), TimeConverter.toMillis(period), TimeUnit.MILLISECONDS
        );
    }

	@Override
    public void runTimerAsync(@NotNull Consumer<WrappedTask> consumer, long delay, long period) {
        this.runTimerAsync(
                consumer, TimeConverter.toMillis(delay), TimeConverter.toMillis(period), TimeUnit.MILLISECONDS
        );
    }

	@Override
    public WrappedTask runTimerAsync(@NotNull Runnable runnable, long delay, long period, TimeUnit unit) {
        return this.wrapTask(
                this.asyncScheduler.runAtFixedRate(plugin, task -> runnable.run(), delay, period, unit)
        );
    }

	@Override
    public void runTimerAsync(@NotNull Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
        this.asyncScheduler.runAtFixedRate(plugin, task -> consumer.accept(this.wrapTask(task)), delay, period, unit);
    }

	@Override
    public CompletableFuture<Void> runAtLocation(Location location, @NotNull Consumer<WrappedTask> consumer) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.plugin.getServer().getRegionScheduler().run(plugin, location, task -> {
            consumer.accept(this.wrapTask(task));
            future.complete(null);
        });

        return future;
    }

	@Override
    public CompletableFuture<Void> runAtLocation(World world, int chunkX, int chunkZ, @NotNull Consumer<WrappedTask> consumer) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.plugin.getServer().getRegionScheduler().run(plugin, world, chunkX, chunkZ, task -> {
            consumer.accept(this.wrapTask(task));
            future.complete(null);
        });

        return future;
    }

	@Override
    public WrappedTask runAtLocationLater(Location location, @NotNull Runnable runnable, long delay) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        return this.wrapTask(
                this.plugin.getServer().getRegionScheduler().runDelayed(plugin, location, task -> runnable.run(), delay)
        );
    }

	@Override
    public WrappedTask runAtLocationLater(World world, int chunkX, int chunkZ, @NotNull Runnable runnable, long delay) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        return this.wrapTask(
                this.plugin.getServer().getRegionScheduler().runDelayed(plugin, world, chunkX, chunkZ, task -> runnable.run(), delay)
        );
    }

	@Override
    public void runAtLocationLater(Location location, @NotNull Consumer<WrappedTask> consumer, long delay) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        this.plugin.getServer().getRegionScheduler().runDelayed(plugin, location, task -> consumer.accept(this.wrapTask(task)), delay);
    }

	@Override
    public void runAtLocationLater(World world, int chunkX, int chunkZ, @NotNull Consumer<WrappedTask> consumer, long delay) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        this.plugin.getServer().getRegionScheduler().runDelayed(plugin, world, chunkX, chunkZ, task -> consumer.accept(this.wrapTask(task)), delay);
    }

	@Override
    public WrappedTask runAtLocationLater(Location location, @NotNull Runnable runnable, long delay, TimeUnit unit) {
        return this.runAtLocationLater(location, runnable, TimeConverter.toTicks(delay, unit));
    }

	@Override
    public WrappedTask runAtLocationLater(World world, int chunkX, int chunkZ, @NotNull Runnable runnable, long delay, TimeUnit unit) {
        return this.runAtLocationLater(world, chunkX, chunkZ, runnable, TimeConverter.toTicks(delay, unit));
    }

	@Override
    public void runAtLocationLater(Location location, @NotNull Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
        this.runAtLocationLater(location, consumer, TimeConverter.toTicks(delay, unit));
    }

	@Override
    public void runAtLocationLater(World world, int chunkX, int chunkZ, @NotNull Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
        this.runAtLocationLater(world, chunkX, chunkZ, consumer, TimeConverter.toTicks(delay, unit));
    }

	@Override
    public WrappedTask runAtLocationTimer(Location location, @NotNull Runnable runnable, long delay, long period) {
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
    public WrappedTask runAtLocationTimer(World world, int chunkX, int chunkZ, @NotNull Runnable runnable, long delay, long period) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        if (period <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), period);
            period = 1;
        }
        return this.wrapTask(
                this.plugin.getServer().getRegionScheduler().runAtFixedRate(plugin, world, chunkX, chunkZ, task -> runnable.run(), delay, period)
        );
    }

	@Override
    public void runAtLocationTimer(Location location, @NotNull Consumer<WrappedTask> consumer, long delay, long period) {
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
    public void runAtLocationTimer(World world, int chunkX, int chunkZ, @NotNull Consumer<WrappedTask> consumer, long delay, long period) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        if (period <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), period);
            period = 1;
        }
        this.plugin.getServer().getRegionScheduler().runAtFixedRate(plugin, world, chunkX, chunkZ, task -> consumer.accept(this.wrapTask(task)), delay, period);
    }

	@Override
    public WrappedTask runAtLocationTimer(Location location, @NotNull Runnable runnable, long delay, long period, TimeUnit unit) {
        return this.runAtLocationTimer(location, runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

	@Override
    public WrappedTask runAtLocationTimer(World world, int chunkX, int chunkZ, @NotNull Runnable runnable, long delay, long period, TimeUnit unit) {
        return this.runAtLocationTimer(world, chunkX, chunkZ, runnable, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

	@Override
    public void runAtLocationTimer(Location location, @NotNull Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
        this.runAtLocationTimer(location, consumer, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

	@Override
    public void runAtLocationTimer(World world, int chunkX, int chunkZ, @NotNull Consumer<WrappedTask> consumer, long delay, long period, TimeUnit unit) {
        this.runAtLocationTimer(world, chunkX, chunkZ, consumer, TimeConverter.toTicks(delay, unit), TimeConverter.toTicks(period, unit));
    }

	@Override
    public CompletableFuture<EntityTaskResult> runAtEntity(Entity entity, @NotNull Consumer<WrappedTask> consumer) {
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
    public CompletableFuture<EntityTaskResult> runAtEntityWithFallback(Entity entity, @NotNull Consumer<WrappedTask> consumer, Runnable fallback) {
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
    public WrappedTask runAtEntityLater(Entity entity, @NotNull Runnable runnable, long delay) {
        return this.runAtEntityLater(entity, runnable, null, delay);
    }

	@Override
    public WrappedTask runAtEntityLater(Entity entity, @NotNull Runnable runnable, Runnable fallback, long delay) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        return this.wrapTask(entity.getScheduler().runDelayed(plugin, task -> runnable.run(), fallback, delay));
    }

	@Override
    public void runAtEntityLater(Entity entity, @NotNull Consumer<WrappedTask> consumer, long delay) {
        this.runAtEntityLater(entity, consumer, null, delay);
    }

	@Override
    public void runAtEntityLater(Entity entity, @NotNull Consumer<WrappedTask> consumer, Runnable fallback, long delay) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        entity.getScheduler().runDelayed(plugin, task -> consumer.accept(this.wrapTask(task)), fallback, delay);
    }

	@Override
    public WrappedTask runAtEntityLater(Entity entity, @NotNull Runnable runnable, long delay, TimeUnit unit) {
        return this.runAtEntityLater(entity, runnable, TimeConverter.toTicks(delay, unit));
    }

	@Override
    public void runAtEntityLater(Entity entity, @NotNull Consumer<WrappedTask> consumer, long delay, TimeUnit unit) {
        this.runAtEntityLater(entity, consumer, TimeConverter.toTicks(delay, unit));
    }

	@Override
    public WrappedTask runAtEntityTimer(Entity entity, @NotNull Runnable runnable, long delay, long period) {
        return this.runAtEntityTimer(entity, runnable, null, delay, period);
    }

    @Override
    public WrappedTask runAtEntityTimer(Entity entity, @NotNull Runnable runnable, Runnable fallback, long delay, long period) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        if (period <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), period);
            period = 1;
        }
        return this.wrapTask(
                entity.getScheduler().runAtFixedRate(plugin, task -> runnable.run(), fallback, delay, period)
        );
    }

    @Override
    public void runAtEntityTimer(Entity entity, @NotNull Consumer<WrappedTask> consumer, long delay, long period) {
        this.runAtEntityTimer(entity, consumer, null, delay, period);
    }

    @Override
    public void runAtEntityTimer(Entity entity, @NotNull Consumer<WrappedTask> consumer, Runnable fallback, long delay, long period) {
        if (delay <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), delay);
            delay = 1;
        }
        if (period <= 0) {
            InvalidTickDelayNotifier.notifyOnce(plugin.getLogger(), period);
            period = 1;
        }
        entity.getScheduler().runAtFixedRate(plugin, task -> consumer.accept(this.wrapTask(task)), fallback, delay, period);
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
        this.globalRegionScheduler.cancelTasks(plugin);
        this.asyncScheduler.cancelTasks(plugin);
    }

    @Override
    public List<WrappedTask> getAllTasks() {
        try {
            // Filter and wrap
            return getAllScheduledTasks().stream()
                    .filter(task -> task.getOwningPlugin().equals(plugin))
                    .map(this::wrapTask)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<WrappedTask> getAllServerTasks() {
        try {
            // Filter and wrap
            return getAllScheduledTasks().stream()
                    .map(this::wrapTask)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @NotNull
    private List<ScheduledTask> getAllScheduledTasks() throws NoSuchFieldException, IllegalAccessException {
        // Global tasks
        Class<? extends GlobalRegionScheduler> globalClass = this.globalRegionScheduler.getClass();

        Field tasksByDeadlineField = globalClass.getDeclaredField("tasksByDeadline");
        boolean wasAccessible = tasksByDeadlineField.isAccessible();
        tasksByDeadlineField.setAccessible(true);

        // noinspection unchecked
        Long2ObjectOpenHashMap<List<ScheduledTask>> globalTasksMap = (Long2ObjectOpenHashMap<List<ScheduledTask>>) tasksByDeadlineField.get(this.globalRegionScheduler);
        tasksByDeadlineField.setAccessible(wasAccessible);

        // Async tasks
        Class<? extends AsyncScheduler> asyncClass = this.asyncScheduler.getClass();

        Field asyncTasksField = asyncClass.getDeclaredField("tasks");
        wasAccessible = asyncTasksField.isAccessible();
        asyncTasksField.setAccessible(true);

        Set<ScheduledTask> asyncTasks = (Set<ScheduledTask>) asyncTasksField.get(this.asyncScheduler);
        asyncTasksField.setAccessible(wasAccessible);

        // Combine global tasks
        List<ScheduledTask> globalTasks = new ArrayList<>();
        for (List<ScheduledTask> list : globalTasksMap.values()) {
            globalTasks.addAll(list);
        }

        // Combine all tasks
        List<ScheduledTask> allTasks = new ArrayList<>(globalTasks.size() + asyncTasks.size());
        allTasks.addAll(globalTasks);
        allTasks.addAll(asyncTasks);
        return allTasks;
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

	@Override
    public WrappedTask wrapTask(Object nativeTask) {
        if (nativeTask == null) {
            return null;
        }

        if (!(nativeTask instanceof ScheduledTask)) {
            String nativeTaskClassName = nativeTask.getClass().getName();
            throw new IllegalArgumentException("The nativeTask provided must be a ScheduledTask. Got: " + nativeTaskClassName + " instead.");
        }

        return new WrappedFoliaTask((ScheduledTask) nativeTask);
    }
}
