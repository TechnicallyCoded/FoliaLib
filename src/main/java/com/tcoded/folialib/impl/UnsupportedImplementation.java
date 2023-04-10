package com.tcoded.folialib.impl;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.enums.ThreadScope;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class UnsupportedImplementation extends SpigotImplementation {

    private final JavaPlugin plugin;
    private final Logger logger;

    public UnsupportedImplementation(FoliaLib foliaLib) {
        super(foliaLib);

        this.plugin = foliaLib.getPlugin();
        this.logger = plugin.getLogger();

        logger.warning(String.format(
                "\n" +
                        "---------------------------------------------------------------------\n" +
                        "FoliaLib does not support this server software! (%s)\n" +
                        "FoliaLib will attempt to use the default spigot implementation.\n" +
                        "---------------------------------------------------------------------\n",
                plugin.getServer().getVersion()));
    }
}
