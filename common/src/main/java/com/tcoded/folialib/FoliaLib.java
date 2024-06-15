package com.tcoded.folialib;

import com.tcoded.folialib.enums.ImplementationType;
import com.tcoded.folialib.impl.SchedulerImpl;
import com.tcoded.folialib.util.InvalidTickDelayNotifier;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class FoliaLib {

    private final JavaPlugin plugin;

    private final ImplementationType implementationType;
    private final SchedulerImpl implementation;

    public FoliaLib(JavaPlugin plugin) {
        this.plugin = plugin;

        // Find the implementation type based on the class names
        ImplementationType foundType = ImplementationType.UNKNOWN;

        for (ImplementationType type : ImplementationType.values()) {
            // Implementation is not suited for this server
            if (!type.selfCheck()) continue;
            // Found implementation match
            foundType = type;
            break;
        }

        // Apply the implementation based on the type
        this.implementationType = foundType;
        this.implementation = this.createServerImpl(this.implementationType.getImplementationClassName());

        // Check for valid implementation
        if (this.implementation == null) {
            throw new IllegalStateException(
                    "Failed to create server implementation. Please report this to the FoliaLib GitHub issues page. " +
                            "Forks of server software may not all be supported. If you are using an unofficial fork, " +
                            "please report this to the fork's developers first.");
        }

        // Check for valid relocation
        // Runtime replace commas to avoid compiler relocations changing this string too
        // Not beautiful, but functional
        String originalLocation = "com,tcoded,folialib,".replace(",", ".");
        if (this.getClass().getName().startsWith(originalLocation)) {
            Logger logger = this.plugin.getLogger();
            logger.severe("****************************************************************");
            logger.severe("FoliaLib is not be relocated correctly! This will cause conflicts");
            logger.severe("with other plugins using FoliaLib. Please contact the developers");
            logger.severe(String.format("of '%s' and inform them of this issue immediately!", this.plugin.getDescription().getName()));
            logger.severe("****************************************************************");
        }
    }

    // Getters

    @SuppressWarnings("unused")
    public ImplementationType getImplType() {
        return implementationType;
    }

    /**
     * @deprecated Use {@link #getImplType()} instead. (forRemoval = true, since = "0.3.5")
     */
    @Deprecated
    @SuppressWarnings("unused")
    public SchedulerImpl getImpl() {
        return getScheduler();
    }

    public SchedulerImpl getScheduler() {
        return implementation;
    }

    @SuppressWarnings("unused")
    public boolean isFolia() {
        return implementationType == ImplementationType.FOLIA;
    }

    @SuppressWarnings("unused")
    public boolean isPaper() {
        return implementationType == ImplementationType.PAPER || implementationType == ImplementationType.LEGACY_PAPER;
    }

    @SuppressWarnings("unused")
    public boolean isSpigot() {
        return implementationType == ImplementationType.SPIGOT || implementationType == ImplementationType.LEGACY_SPIGOT;
    }

    @SuppressWarnings("unused")
    public boolean isUnsupported() {
        return implementationType == ImplementationType.UNKNOWN;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    // Public Options

    @SuppressWarnings("unused")
    public void disableInvalidTickValueWarning() {
        InvalidTickDelayNotifier.disableNotifications = true;
    }

    @SuppressWarnings("unused")
    public void enableInvalidTickValueDebug() {
        InvalidTickDelayNotifier.debugMode = true;
    }

    // Internal Utils

    private SchedulerImpl createServerImpl(String implName) {
        String basePackage = this.getClass().getPackage().getName() + ".impl.";

        try {
            return (SchedulerImpl) Class.forName(basePackage + implName)
                    .getConstructor(this.getClass())
                    .newInstance(this);
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}