package com.trychen.logitow.stack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BlockBuilder {
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

    /**
     * The block's id
     */
    private int blockID;

    /**
     * the
     */
    private Coordinate pos;

    private BlockBuilder parent;
    private BlockBuilder[] childs = new BlockBuilder[6];

    private int[] faces = { 1, 2, 3, 4, 5, 6 };

    public BlockBuilder() {
        blockID = 0;
        pos = new Coordinate();
    }

    public BlockBuilder(int blockID, BlockBuilder parent) {
        this.blockID = blockID;
        this.parent = parent;
    }

    public BlockBuilder connect(BlockData data){
        if (data.insertBlockID != blockID) {
            for (BlockBuilder child : childs) {
                if (child != null) {
                    BlockBuilder buildBlock = child.connect(data);
                    if (buildBlock != null) return buildBlock;
                }
            }
        } else if (data.newBlockID == 0) {
            BlockBuilder blockBuilders = childs[findDirIndex(data.insertFace)];
            childs[findDirIndex(data.insertFace)] = null;
            return blockBuilders;
        } else {
            int dirIndex = findDirIndex(data.insertFace);
            BlockBuilder child = new BlockBuilder(data.newBlockID, this);
            child.pos = pos.add(pos_offset[dirIndex][0], pos_offset[dirIndex][1], pos_offset[dirIndex][2]);

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

    public BlockBuilder getParent() {
        return parent;
    }

    public BlockBuilder[] getChilds() {
        return childs;
    }

    public int getBlockID() {
        return blockID;
    }

    public Color getBlockColor() {
        return Color.getColor(getBlockID());
    }

    public Coordinate getPos() {
        return pos;
    }

    public Set<BlockBuilder> getAllBlocks(Set<BlockBuilder> set){
        set.add(this);
        for (BlockBuilder child : childs) {
            if (child != null) child.getAllBlocks(set);
        }
        return set;
    }

    @Override
    public String toString() {
        return String.format("BlockBuilder{id=%d, pos=%s}", blockID, pos);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[]{blockID, parent == null?0:parent.blockID});
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BlockBuilder)) return false;
        BlockBuilder builder = (BlockBuilder) obj;

        return this.blockID == builder.blockID && this.parent == builder.parent;
    }
}
