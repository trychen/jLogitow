package com.trychen.logitow.stack;

import java.util.Arrays;

/**
 * @author trychen
 * @since 1.2
 * @version 2
 */
public class Structure {
    private int blockID;
    private Structure parent;
    private Color color;
    private Facing facing = Facing.UNKNOWN;

    private Coordinate coordinate;

    private Structure[] children = new Structure[6];

    public Structure(){
        this(0, null, Facing.UNKNOWN, new Coordinate(0, 0, 0));
    }

    public Structure(int blockID, Structure parent, Facing facing, Coordinate coordinate) {
        this.blockID = blockID;
        this.parent = parent;
        this.facing = facing;
        this.coordinate = coordinate;
        this.color = Color.getColor(blockID);
    }

    /**
     * 插入方块
     * @param blockData 方块数据
     */
    public Structure insert(BlockData blockData){
        // 检查是否为当前的方块
        if (blockData.insertBlockID != blockID) {
            for (Structure child : children) {
                if (child == null) continue;
                Structure structure = child.insert(blockData);
                if (structure != null) return structure;
            }
            return null;
        }
        // 检查是否为拆除方块
        if (blockData.newBlockID == 0) {
            removeChild(blockData.getFacing());
            return null;
        }

        Coordinate newCoordinate;
        if (blockData.insertBlockID == 0) {
            newCoordinate = coordinate.add(1, 0, 0);
        } else
        switch(blockData.getFacing()) {
            case TOP:
                newCoordinate = coordinate.add(0, 1, 0);
                break;
            case BOTTOM:
                newCoordinate = coordinate.add(0, -1, 0);
                break;
            case FRONT:
                newCoordinate = coordinate.add(1, 0, 0);
                break;
            case BACK:
                newCoordinate = coordinate.add(-1, 0, 0);
                break;
            case LEFT:
                newCoordinate = coordinate.add(0, 0, 1);
                break;
            case RIGHT:
                newCoordinate = coordinate.add(0, 0, -1);
                break;
            default:
                return null;
        }

        return addChild(blockData.getFacing(), new Structure(blockData.newBlockID, this, blockData.getFacing(), newCoordinate));
    }

    private void removeChild(Facing facing) {
        getChildren()[facing.id] = null;
    }

    private void clear(){
        children = new Structure[6];
    }

    public Structure[] getChildren() {
        return children;
    }

    public Structure getChildren(Facing face) {
        return getChildren()[face.id];
    }

    public Facing getFacing() {
        return facing;
    }

    private Structure addChild(Facing facing, Structure structure){
        if (facing == Facing.UNKNOWN) throw new IllegalArgumentException();
        children[facing.id] = structure;
        return structure;
    }

    public Color getColor(){
        return color;
    }

    public Structure getParent() {
        return parent;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * 只删除当前 Structure 的子方块
     */
    private boolean remove(){
        if (parent == null) return false;
        for (int i = 0; i < parent.children.length; i++) {
            if (this == parent.children[i]) {
                parent.children[i] = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(blockID);
    }
}
