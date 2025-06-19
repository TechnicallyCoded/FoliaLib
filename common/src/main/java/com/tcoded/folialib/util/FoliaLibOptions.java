package com.tcoded.folialib.util;

public class FoliaLibOptions {

    // InvalidTickDelayNotifier
    private boolean disableNotifications = false;
    private boolean invalidTickDebugMode = false;
    
    // isValid
    private boolean useIsValidOnNonFolia = false;
    
    public boolean isDisableNotifications() {
        return disableNotifications;
    }
    
    public void disableNotifications() {
        disableNotifications = true;
    }
    
    public boolean isInvalidTickDebugMode() {
        return invalidTickDebugMode;
    }
    
    public void enableInvalidTickDebugMode() {
        invalidTickDebugMode = true;
    }
    
    public void disableInvalidTickDebugMode() {
        invalidTickDebugMode = false;
    }

    public boolean useIsValidOnNonFolia() {
        return useIsValidOnNonFolia;
    }

    /**
     * Warning: This will prevent entity based timers from ever automatically stopping.
     */
    public void disableIsValidOnNonFolia() {
        useIsValidOnNonFolia = false;
    }
    
}
