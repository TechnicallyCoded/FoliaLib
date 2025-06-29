package com.tcoded.folialib;

import com.tcoded.folialib.enums.ImplementationType;
import com.tcoded.folialib.impl.PlatformScheduler;
import com.tcoded.folialib.util.FoliaLibOptions;
import com.tcoded.folialib.util.InvalidTickDelayNotifier;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class FoliaLib {

    private final Plugin plugin;

    private final ImplementationType implementationType;
    private final PlatformScheduler scheduler;
    private FoliaLibOptions options;
    private InvalidTickDelayNotifier invalidTickDelayNotifier;

    public FoliaLib(Plugin plugin) {
        this(plugin, new FoliaLibOptions());
    }

    public FoliaLib(Plugin plugin, FoliaLibOptions customOptions) {
        this.plugin = plugin;

        // Initialize options
        this.options = customOptions;

        // Initialize the invalid tick delay notifier
        this.invalidTickDelayNotifier = new InvalidTickDelayNotifier(plugin.getLogger(), this.options);

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
        this.scheduler = this.createServerImpl(this.implementationType.getImplementationClassName());

        // Check for valid implementation
        if (this.scheduler == null) {
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
            logger.severe("FoliaLib is not relocated correctly! This will cause conflicts");
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
    public PlatformScheduler getImpl() {
        return getScheduler();
    }

    public PlatformScheduler getScheduler() {
        return scheduler;
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

    public Plugin getPlugin() {
        return plugin;
    }

    // Public Options

    /**
     * Use {@link FoliaLib#getOptions()} instead.
     */
    @SuppressWarnings("unused")
    @Deprecated
    public void disableInvalidTickValueWarning() {
        getOptions().disableNotifications();
    }

    /**
     * Use {@link FoliaLib#getOptions()} instead.
     */
    @SuppressWarnings("unused")
    @Deprecated
    public void enableInvalidTickValueDebug() {
        getOptions().enableInvalidTickDebugMode();
    }

    public FoliaLibOptions getOptions() {
        return this.options;
    }

    // Internal Utils

    /**
     * This is an internal tool, do NOT use!
     * You're probably looking for {@link FoliaLib#getOptions()} instead.
     * **/
    @Deprecated
    @SuppressWarnings("DeprecatedIsStillUsed")
    public InvalidTickDelayNotifier getInvalidTickDelayNotifier() {
        return this.invalidTickDelayNotifier;
    }

    private PlatformScheduler createServerImpl(String implName) {
        String basePackage = this.getClass().getPackage().getName() + ".impl.";

        try {
            return (PlatformScheduler) Class.forName(basePackage + implName)
                    .getConstructor(this.getClass())
                    .newInstance(this);
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}