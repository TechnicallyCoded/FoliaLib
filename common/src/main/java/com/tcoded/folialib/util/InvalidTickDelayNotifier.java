package com.tcoded.folialib.util;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class InvalidTickDelayNotifier {

    private boolean notified = false;

    private final Logger logger;
    private final FoliaLibOptions options;

    public InvalidTickDelayNotifier(@NotNull Logger logger, @NotNull FoliaLibOptions options) {
        this.logger = logger;
        this.options = options;
    }

    public void notifyOnce(long span) {
        // Check if the notification was already sent or if the notifications were manually disabled
        if (notified || options.isDisableNotifications()) return;
        notified = true;

        // Send a warning message if this hasn't been sent before
        logger.warning(
                String.format("A tick based delay or timer was scheduled with a time span of %d ticks. ", span) +
                "The server is already processing the current tick and, as such, won't process new tasks in less than 1 tick. " +
                "FoliaLib will automatically change time spans of <= 0 ticks to 1 tick going forward. " +
                "This warning is purely informative and won't impact the operation of the plugin. " +
                "Plugin developers can disable this warning or enable debug mode to location the source of this warning.");

        // If the user enables debug mode, print the stack trace
        if (options.isInvalidTickDebugMode()) {
            new Exception("Debugging information to track down the location of the invalid tick input value")
                    .printStackTrace();
        }
    }

}
