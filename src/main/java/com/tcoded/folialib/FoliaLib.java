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

        String version = plugin.getServer().getVersion();

        // Init implementation based on server version string
        if (version.startsWith("git-Folia-")) {
            this.implementationType = ImplementationType.FOLIA;
            this.implementation = new FoliaImplementation(this);
        }
        else if (version.startsWith("git-Paper-")) {
            this.implementationType = ImplementationType.PAPER;
            this.implementation = new PaperImplementation(this);
        }
        else if (version.contains("-Spigot-")) {
            this.implementationType = ImplementationType.SPIGOT;
            this.implementation = new SpigotImplementation(this);
        }
        else {
            this.implementationType = ImplementationType.UNKNOWN;
            this.implementation = new UnsupportedImplementation(this);
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