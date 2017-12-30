package com.trychen.logitow.stack;

/**
 * @author trychen
 * @since 1.2
 * @version 2
 */
public enum Facing {
    BOTTOM(1),
    TOP(2),
    BACK(3),
    LEFT(4),
    FRONT(5),
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
