package com.trychen.logitow.stack;

public enum Facing {
    BACK(1),
    FRONT(2),
    UP(3),
    LEFT(4),
    DOWN(5),
    RIGHT(6),
    UNKNOWN(-1);

    public final int id;

    Facing(int id) {
        this.id = id;
    }

    public static Facing getFacing(int id){
        for (Facing facing : values()) {
            if (facing.id == id) return facing;
        }
        return Facing.UNKNOWN;
    }
}
