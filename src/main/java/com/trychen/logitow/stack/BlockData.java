package com.trychen.logitow.stack;

import java.util.Arrays;

public class BlockData {
    /**
     * block which is inserted
     */
    public final int insertBlockID;

    /**
     * the face of block which is inserted
     */
    public final int insertFace;

    /**
     * insert
     */
    public final int newBlockID;

    public BlockData(int insertBlockID, int insertFace, int newBlockID) {
        this.insertBlockID = insertBlockID;
        this.insertFace = insertFace;
        this.newBlockID = newBlockID;
    }

    public Color getNewBlockColor(){
        return Color.getColor(newBlockID);
    }

    public Facing getFacing(){
        return Facing.getFacing(insertFace);
    }

    public Color getInsertBlockColor(){
        return Color.getColor(insertBlockID);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof BlockData) {
            BlockData another = (BlockData) obj;
            return this.insertBlockID == another.insertBlockID && this.insertFace == another.insertFace && this.newBlockID == another.newBlockID;
        } else return false;
    }

    @Override
    public String toString() {
        return String.format("BlockData{NewBlockID: %s, NewBlockColor: %s, InsertBlockID: %s, InsertFace: %s}", newBlockID, getNewBlockColor().name(), insertBlockID, getFacing());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[]{insertBlockID, insertFace, newBlockID});
    }
}
