package com.tcoded.folialib;

import com.tcoded.folialib.enums.ImplementationType;
import com.tcoded.folialib.impl.*;
import org.bukkit.plugin.java.JavaPlugin;

public class FoliaLib {

    private final JavaPlugin plugin;

    private final ImplementationType implementationType;
    private final ServerImplementation implementation;

    public FoliaLib(JavaPlugin plugin) {
        this.plugin = plugin;

        // Find the implementation type based on the class names
        ImplementationType foundType = ImplementationType.UNKNOWN;
        typeLoop:
        for (ImplementationType type : ImplementationType.values()) {
            String[] classNames = type.getClassNames();

            // Check if any of the class names are present
            for (String className : classNames) {
                try {
                    // Try to load the class
                    Class.forName(className);

                    // Found the server type, remember that and break the loop
                    foundType = type;
                    break typeLoop;
                } catch (ClassNotFoundException ignored) {}
            }
        }

        // Apply the implementation based on the type
        this.implementationType = foundType;
        switch (foundType) {
            case FOLIA -> this.implementation = new FoliaImplementation(this);
            case PAPER -> this.implementation = new PaperImplementation(this);
            case SPIGOT -> this.implementation = new SpigotImplementation(this);
            default -> this.implementation = new UnsupportedImplementation(this);
        }
    }

    @SuppressWarnings("unused")
    public ImplementationType getImplType() {
        return implementationType;
    }

    @SuppressWarnings("unused")
    public ServerImplementation getImpl() {
        return implementation;
    }

    @SuppressWarnings("unused")
    public boolean isFolia() {
        return implementationType == ImplementationType.FOLIA;
    }

    @SuppressWarnings("unused")
    public boolean isPaper() {
        return implementationType == ImplementationType.PAPER;
    }

    @SuppressWarnings("unused")
    public boolean isSpigot() {
        return implementationType == ImplementationType.SPIGOT;
    }

    @SuppressWarnings("unused")
    public boolean isUnsupported() {
        return implementationType == ImplementationType.UNKNOWN;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}