package com.trychen.logitow.stack;

/**
 * @author trychen
 * @since 1.2
 * @version 2
 */
public enum Facing {
    TOP(0),
    BOTTOM(1),
    LEFT(2),
    RIGHT(3),
    FRONT(4),
    BACK(5),
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
