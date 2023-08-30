package com.tcoded.folialib.enums;

import com.tcoded.folialib.impl.ServerImplementation;
import com.tcoded.folialib.util.ImplementationTestsUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This enum is used to determine the server implementation type.
 * <p>
 * The enum is ordered by PRIORITY. The first implementation that passes all tests will be used.
 * <p>
 * A server is considered 'legacy' if it does not support task consumers. This feature was added in 1.13.2.
 * This means that even 1.13.1 servers are considered legacy servers as they do not support highly important
 * features that developers may want to use. Refer to {@link ServerImplementation#runLater(Consumer, long)}
 * for more information on the behavior of task consumer-enabled methods on servers which do not support this feature.
 */
@SuppressWarnings("SpellCheckingInspection")
public enum ImplementationType {

    FOLIA (
            "FoliaImplementation",
            new Supplier[0],
            "io.papermc.paper.threadedregions.RegionizedServer"
    ),
    PAPER (
            "PaperImplementation",
            new Supplier[] {ImplementationTestsUtil::isTaskConsumersSupported},
            "com.destroystokyo.paper.PaperConfig", "io.papermc.paper.configuration.Configuration"
    ),
    LEGACY_PAPER (
            "LegacyPaperImplementation",
            new Supplier[0],
            "com.destroystokyo.paper.PaperConfig", "io.papermc.paper.configuration.Configuration"
    ),
    SPIGOT (
            "SpigotImplementation",
            new Supplier[] {ImplementationTestsUtil::isTaskConsumersSupported},
            "org.spigotmc.SpigotConfig"
    ),
    LEGACY_SPIGOT (
            "LegacySpigotImplementation",
            new Supplier[0],
            "org.spigotmc.SpigotConfig"
    ),
    UNKNOWN (
            "UnsupportedImplementation",
            new Supplier[0]
    );

    private final String implementationClassName;
    private final Supplier<Boolean>[] tests;
    private final String[] classNames;

    ImplementationType(String implementationClassName, Supplier<Boolean>[] tests, String... classNames) {
        this.implementationClassName = implementationClassName;
        this.tests = tests;
        this.classNames = classNames;
    }

    public String getImplementationClassName() {
        return implementationClassName;
    }

    public Supplier<Boolean>[] getTests() {
        return tests;
    }

    public String[] getClassNames() {
        return classNames;
    }

    public boolean selfCheck() {
        // Run self-tests
        for (Supplier<Boolean> test : this.getTests()) {
            if (!test.get()) return false;
        }

        // Self-test for class names
        String[] classNames = this.getClassNames();

        // Check if any of the class names are present
        for (String className : classNames) {
            try {
                // Try to load the class
                Class.forName(className);

                // Found the server type, remember that and break the loop
                return true;
            } catch (ClassNotFoundException ignored) {}
        }

        return false;
    }
}

