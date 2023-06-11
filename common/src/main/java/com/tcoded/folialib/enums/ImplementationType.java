package com.tcoded.folialib.enums;

public enum ImplementationType {

    // Ordered by priority
    @SuppressWarnings("SpellCheckingInspection")
    FOLIA ("io.papermc.paper.threadedregions.RegionizedServer"),
    @SuppressWarnings("SpellCheckingInspection")
    PAPER ("com.destroystokyo.paper.PaperConfig", "io.papermc.paper.configuration.Configuration"),
    @SuppressWarnings("SpellCheckingInspection")
    SPIGOT ("org.spigotmc.SpigotConfig"),
    UNKNOWN;

    private final String[] classNames;

    ImplementationType(String... classNames) {
        this.classNames = classNames;
    }

    public String[] getClassNames() {
        return classNames;
    }
}
