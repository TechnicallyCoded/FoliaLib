package com.tcoded.folialib.impl;

import com.tcoded.folialib.FoliaLib;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class UnsupportedImplementation extends SpigotImplementation {

    public UnsupportedImplementation(FoliaLib foliaLib) {
        super(foliaLib);

        JavaPlugin plugin = foliaLib.getPlugin();
        Logger logger = plugin.getLogger();

        logger.warning("\n" +
                "---------------------------------------------------------------------\n" +
                "FoliaLib does not support this server software! (" + plugin.getServer().getVersion() + ")\n" +
                "FoliaLib will attempt to use the default spigot implementation.\n" +
                "---------------------------------------------------------------------\n"
        );
    }
}
