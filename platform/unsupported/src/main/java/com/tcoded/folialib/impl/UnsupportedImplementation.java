package com.tcoded.folialib.impl;

import com.tcoded.folialib.FoliaLib;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

@SuppressWarnings("unused")
public class UnsupportedImplementation extends LegacySpigotImplementation {

    public UnsupportedImplementation(FoliaLib foliaLib) {
        super(foliaLib);

        Plugin plugin = foliaLib.getPlugin();
        Logger logger = plugin.getLogger();

        logger.warning(
                String.format("\n" +
                              "---------------------------------------------------------------------\n" +
                              "FoliaLib does not support this server software! (%s)\n" +
                              "FoliaLib will attempt to use the legacy spigot implementation.\n" +
                              "---------------------------------------------------------------------\n",
                        plugin.getServer().getVersion()
                )
        );
    }
}
