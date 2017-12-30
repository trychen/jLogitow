package com.trychen.logitow.jni;

public interface SystemVersion {
    static boolean isCurrentSystemSupport() {
        String currentVersion = getCurrentSystemVersion();
        if (SystemType.getCurrentSystem() == SystemType.MACOSX) {
            return currentVersion.startsWith("10.1");
        }
        return false;
    }

    static String getCurrentSystemVersion() {
        return System.getProperty("os.version");
    }
}
