package com.trychen.logitow.stack;

import scala.actors.threadpool.Arrays;

public class Structure {
    private int blockID;
    private Structure parent;
    private Color color;
    private Facing facing = Facing.UNKNOWN;

    private Structure[] children = new Structure[6];
    private Structure removedStructure;

    public Structure(int blockID, Structure parent, Facing facing) {
        this.blockID = blockID;
        this.parent = parent;
        this.facing = facing;
        this.color = Color.getColor(blockID);
    }

    /**
     * 插入方块
     * @param blockData 方块数据
     */
    public boolean insert(BlockData blockData){
        // 检查是否为当前的方块
        if (blockData.insertBlockID != blockID) {
            for (Structure child : children) {
                if (child!= null && child.insert(blockData)) return true;
            }
            return false;
        }
        // 检查是否为拆除方块
        if (blockData.newBlockID == 0) {
            removeChild(blockData.getFacing());
            return true;
        }

        if (blockID == 0) {
            if (blockData.getFacing() != Facing.UP) return false;
            addChild(Facing.UP, new Structure(blockData.newBlockID, this, Facing.UP));
        } else {
            addChild(blockData.getFacing(), new Structure(blockData.newBlockID, this, blockData.getFacing()));
        }
        return true;
    }

    private void removeChild(Facing facing) {
        removedStructure = getChildren(facing);
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

    private void addChild(Facing facing, Structure structure){
        if (facing == Facing.UNKNOWN) throw new IllegalArgumentException();
        children[facing.id] = structure;
    }


    public Color getColor(){
        return color;
    }

    public Structure getParent() {
        return parent;
    }

    public Structure getRemovedStructure() {
        return removedStructure;
    }

    public void setRemovedStructure(Structure removedStructure) {
        this.removedStructure = removedStructure;
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

    public Structure copy() {
        Structure structure = new Structure(blockID, parent, facing);
        for (int i = 0; i < children.length; i++) {
            structure.children[i] = children[i].copy();
        }
        return structure;
    }
}
