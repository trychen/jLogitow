package com.trychen.logitow.stack;

public enum Color {
    CORE(0, 0),
    WHITE(1048576, 2097151),
    BLACK(2097152,3145727),
    RED(3145728, 4194303),
    ORANGE(4194304, 5242879),
    YELLOW(5242880, 6291455),
    GREEN(6291456, 7340031),
    CYAN(7340032, 8388607),
    BLUE(8388608, 9437183),
    PURPLE(9437184, 10485759),
    PINK(10485760, 11534335),
    END(16777215, 16777215),
    UNKNOWN(-1, -1);

    public final int ID_START, ID_END;

    Color(int ID_START, int ID_END) {
        this.ID_START = ID_START;
        this.ID_END = ID_END;
    }

    public static Color getColor(int blockID){
        for (Color color : Color.values()) {
            if (blockID >= color.ID_START && blockID <= color.ID_END) return color;
        }
        return UNKNOWN;
    }
}
