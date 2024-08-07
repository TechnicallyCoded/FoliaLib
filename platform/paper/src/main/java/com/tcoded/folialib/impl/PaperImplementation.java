package com.tcoded.folialib.impl;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.util.ImplementationTestsUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class PaperImplementation extends SpigotImplementation {

    private Method teleportAsyncMethod;

    public PaperImplementation(FoliaLib foliaLib) {
        super(foliaLib);

        // Pre-reflect the teleportAsync method
        if (ImplementationTestsUtil.isAsyncTeleportSupported()) {
            try {
                this.teleportAsyncMethod = Entity.class.getMethod("teleportAsync", Location.class, PlayerTeleportEvent.TeleportCause.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Failed to initialize PaperImplementation", e);
            }
        }
    }

    /** Don't need to override much, since we're extending {@link SpigotImplementation} **/

    @Override
    public CompletableFuture<Boolean> teleportAsync(Entity entity, Location location) {
        return this.teleportAsync(entity, location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override
    public CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, PlayerTeleportEvent.TeleportCause cause) {
        // If the server does not support native async teleport, use SpigotImplementation's super method
        if (!ImplementationTestsUtil.isAsyncTeleportSupported()) return super.teleportAsync(entity, location, cause);

        // We do support async teleports, try to use the method with reflection
        try {
            // noinspection unchecked
            return (CompletableFuture<Boolean>) teleportAsyncMethod.invoke(entity, location, cause);
        } catch (Exception e) {
            e.printStackTrace();
            return super.teleportAsync(entity, location, cause);
        }
    }

}
