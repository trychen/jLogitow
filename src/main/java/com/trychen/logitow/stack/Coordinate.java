package com.trychen.logitow.stack;

/**
 * @author trychen
 * @since 1.2
 * @version 1
 */
public class Coordinate implements Cloneable{
    private int x, y, z;

    public Coordinate() {
        this(0, 0, 0);
    }

    public Coordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coordinate add(int x, int y, int z) {
        return new Coordinate(this.x + x, this.y + y, this.z + z);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public Coordinate clone() {
        try {
            return (Coordinate) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("Coordinate{x=%d, y=%d, z=%d}", x, y, z);
    }
}
