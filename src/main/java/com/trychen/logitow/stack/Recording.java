package com.trychen.logitow.stack;

import java.util.List;

public class Recording {
    private Structure structures = new Structure(0, 0, 0);

    public void record(BlockData blockData){
        if (blockData.getInsertBlockColor() == Color.CORE) {

        }
    }

    static class Structure extends BlockData{
        private int x, y, z;
        private List<Structure> subs;

        public Structure(int newBlockID, int insertBlockID, int insertFace) {
            super(insertBlockID, insertFace, newBlockID);
            if (newBlockID == 0) {
                x = 0;
                y = 0;
                z = 0;
            }
        }
        public Structure(BlockData blockData) {
            super(blockData.insertBlockID, blockData.insertFace, blockData.newBlockID);
            if (blockData.newBlockID == 0) {
                x = 0;
                y = 0;
                z = 0;
            }
        }

        public void insert(Structure structure) {
            subs.add(structure);
        }

        public void insert(BlockData blockData) {
            Structure structure = new Structure(blockData);
            structure.x = x;
            structure.y = y;
            structure.z = z;
        }
    }
}
