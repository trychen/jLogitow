package com.trychen.logitow.jni;

public enum SystemType {
    WINDOWS("dll"), MACOSX("jnilib"), LINUX("so"), OTHERS("");

    private String jnilibSuffix;

    SystemType(String jnilibSuffix) {
        this.jnilibSuffix = jnilibSuffix;
    }

    public String getJniLibSuffix() {
        return jnilibSuffix;
    }

    private static SystemType currentSystem;

    public static SystemType getCurrentSystem() {
        if (currentSystem == null) {
            String OSNAME = System.getProperty("os.name");
            if (OSNAME.equalsIgnoreCase("Linux")) currentSystem = LINUX;
            else if (OSNAME.equalsIgnoreCase("Mac OS X")) currentSystem = MACOSX;
            else if (OSNAME.startsWith("Windows")) currentSystem = WINDOWS;
            else currentSystem = OTHERS;
        }
        return currentSystem;
    }
}
