package com.trychen.logitow.stack;

public class BuildBlock {
    // 子积木和父积木连接时，俩个积木同一平面的面号对应关系,以父积木的面号为索引
    public static final int[][] CHILD_SAME_FACE = {
            {0, 0, 0, 0, 0, 0}, //连接父积木的0面, 不可能连接××××××
            {1, 2, 3, 4, 5, 6}, //连接父积木的1面
            {5, 3, 2, 6, 1, 4}, //连接父积木的2面
            {5, 3, 4, 2, 6, 1}, //连接父积木的3面
            {5, 3, 1, 4, 2, 6}, //连接父积木的4面
            {5, 3, 6, 1, 4, 2}, //连接父积木的5面
    };

    public static final int[][] FIRST_CHILD_FACE = {
            {0, 0, 0, 0, 0, 0}, //连接父积木的0面,不可能连接××××××
            {1, 2, 3, 4, 5, 6}, //连接父积木的1面
            {3, 5, 2, 4, 1, 6}, //连接父积木的2面
            {3, 5, 6, 2, 4, 1}, //连接父积木的3面
            {3, 5, 1, 6, 2, 4}, //连接父积木的4面
            {3, 5, 4, 1, 6, 2}, //连接父积木的5面
    };

    // 下上右后左前
    // 上  0, 1, 0
    // 左  0，0，1
    // 后  1，0，0
    public static final int[][] pos_offset = {{0, -1, 0}, {0, 1, 0}, {0, 0, -1}, {1, 0, 0}, {0, 0, 1}, {-1, 0, 0}};

    private int blockID;

    private int x, y, z;

    private BuildBlock parent;
    private BuildBlock[] childs = new BuildBlock[6];

    private int[] faces = { 1, 2, 3, 4, 5, 6 };

    public BuildBlock() {
        blockID = 0;
        x = 0;
        y = 0;
        z = 0;
    }

    public BuildBlock(int blockID, BuildBlock parent) {
        this.blockID = blockID;
        this.parent = parent;
    }

    public BuildBlock connect(BlockData data){
        if (data.insertBlockID != blockID) {
            for (BuildBlock child : childs) {
                if (child != null) {
                    BuildBlock buildBlock = child.connect(data);
                    if (buildBlock != null) return buildBlock;
                }
            }
        } else if (data.newBlockID == 0) {
            childs[findDirIndex(data.insertFace)] = null;
        } else {
            int dirIndex = findDirIndex(data.insertFace);
            BuildBlock child = new BuildBlock(data.newBlockID, this);
            child.x = x + pos_offset[dirIndex][0];
            child.y = y + pos_offset[dirIndex][1];
            child.z = z + pos_offset[dirIndex][2];

            for (int parentFaceID = 0; parentFaceID < 6; parentFaceID++) {
                for (int dirID = 0; dirID < 6; dirID++) {
                    if (parentFaceID + 1 == faces[dirID]) {
                        if (data.insertBlockID == 0){
                            child.faces[dirID] = FIRST_CHILD_FACE[data.insertFace - 1][parentFaceID];
                        } else {
                            child.faces[dirID] = CHILD_SAME_FACE[data.insertFace - 1][parentFaceID];
                        }
                        break;
                    }
                }
            }

            childs[dirIndex] = child;
            return child;
        }
        return null;
    }

    public int findDirIndex(int face){
        for (int i = 0; i < 6; i++) {
            if (face == faces[i]) return i;
        }
        return -1;
    }

    public BuildBlock getParent() {
        return parent;
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

    public BuildBlock[] getChilds() {
        return childs;
    }

    public int getBlockID() {
        return blockID;
    }

    @Override
    public String toString() {
        return String.format("BuildBlock{id=%d, x=%d, y=%d, z=%d}", blockID, x, y, z);
    }
}
