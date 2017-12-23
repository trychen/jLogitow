package com.trychen.logitow.forge;

import com.trychen.logitow.stack.Color;
import com.trychen.logitow.stack.Facing;
import com.trychen.logitow.stack.Structure;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;

@SuppressWarnings("Duplicates")
public interface StructureHelper {
    static void check(Structure structure, World world, BlockPos pos, Collection<BlockPos> useCheck) {
        int direction = 5;
        if (structure.getColor() == Color.CORE) {
            useCheck.add(pos);
            Structure upStructure = structure.getChildren(Facing.UP);
            BlockPos upPos = transformPos(pos, direction, 1, 0, 0);

            update(world, upPos, upStructure, useCheck);
            if (upStructure != null)
                for (Structure child : upStructure.getChildren())
                    if (child != null) check(upStructure, world, upPos, useCheck);
        } else {
            for (Structure child : structure.getChildren()) {
                if (child == null) continue;
                BlockPos tranPos = transformPos(pos, direction, child);
                if (tranPos.equals(pos)) continue;
                update(world, tranPos, child, useCheck);
                check(child, world, tranPos, useCheck);
            }
        }
    }

    static void update(World world, BlockPos pos, Structure aimStructure, Collection<BlockPos> useCheck) {
        if (useCheck.contains(pos)) return;
        IBlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() == Blocks.AIR && aimStructure != null) {
            if (blockState.getBlock() != Blocks.WOOL) {
                world.setBlockState(pos, Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, transformToEnumDyeColor(aimStructure.getColor())));
            } else if (blockState.getBlock() != Blocks.WOOL && transformToColor(blockState.getValue(BlockColored.COLOR)) != aimStructure.getColor()) {
                world.setBlockState(pos, blockState.withProperty(BlockColored.COLOR, transformToEnumDyeColor(aimStructure.getColor())));
            }
            useCheck.add(pos);
        } else if (aimStructure != null && blockState.getBlock() == Blocks.WOOL) {
            useCheck.add(pos);
        }
    }

    static EnumDyeColor transformToEnumDyeColor(Color color) {
        switch (color) {
            case RED:
                return EnumDyeColor.RED;
            case BLUE:
                return EnumDyeColor.BLUE;
            case YELLOW:
                return EnumDyeColor.YELLOW;
            case CYAN:
                return EnumDyeColor.CYAN;
            case WHITE:
                return EnumDyeColor.WHITE;
            case ORANGE:
                return EnumDyeColor.ORANGE;
            case PINK:
                return EnumDyeColor.PINK;
            case PURPLE:
                return EnumDyeColor.PURPLE;
            case BLACK:
                return EnumDyeColor.BLACK;
            case GREEN:
                return EnumDyeColor.GREEN;
            default:
                return EnumDyeColor.WHITE;
        }
    }

    static Color transformToColor(EnumDyeColor color) {
        switch (color) {
            case RED:
                return Color.RED;
            case BLUE:
                return Color.BLUE;
            case YELLOW:
                return Color.YELLOW;
            case CYAN:
                return Color.CYAN;
            case WHITE:
                return Color.WHITE;
            case ORANGE:
                return Color.ORANGE;
            case PINK:
                return Color.PINK;
            case PURPLE:
                return Color.PURPLE;
            case BLACK:
                return Color.BLACK;
            case GREEN:
                return Color.GREEN;
            default:
                return Color.WHITE;
        }
    }

    static BlockPos transformPos(BlockPos pos, int direction, int x, int y, int z) {
        if (direction == EnumFacing.NORTH.getIndex()) return pos.add(z, y, -x);
        if (direction == EnumFacing.SOUTH.getIndex()) return pos.add(z, y, x);
        if (direction == EnumFacing.WEST.getIndex()) return pos.add(-x, y, z);
        if (direction == EnumFacing.EAST.getIndex()) return pos.add(x, y, z);
        return pos.add(x, y, z);
    }

    static BlockPos transformPos(BlockPos pos, int direction, Structure structure) {
        System.out.println("=======");
        Facing facing = structure.getFacing();
        System.out.println("color: " + structure.getColor());
        System.out.println("facing: " + facing);
        Facing parentFacing = structure.getParent().getFacing();
        System.out.println("parent: " + parentFacing);
        Facing finalFacing = structure.getFinalStructure().getFacing();
        System.out.println("final: " + finalFacing);
        Facing parentParentFacing = structure.getParent().getParent().getFacing();
        System.out.println("parent parent: " + parentParentFacing);
        int x = 0, y = 0, z = 0;

        switch (parentFacing) {
            case UP:
                switch (finalFacing) {
                    case FRONT:
                        if (facing == Facing.FRONT) x = 1;
                        break;
                }
                break;
            case FRONT:
                switch (finalFacing) {
                    case BACK:
                        switch (parentParentFacing) {
                            case DOWN:
                            case LEFT:
                                if (facing == Facing.FRONT) z = -1;
                                break;
                        }
                        break;
                    case FRONT:
                        switch (parentParentFacing) {
                            case UP:
                                if (facing == Facing.FRONT) x = 1;
                                break;
                        }
                }
                break;
        }

//        if (parentFacing == Facing.FRONT) {
//            if (finalFacing == Facing.BACK) {
//                if (parentParentFacing == Facing.DOWN || parentParentFacing == Facing.LEFT) {
//                    if (facing == Facing.FRONT) z = -1;
//                } else if (parentParentFacing == Facing.UP) {
//                    if (facing == Facing.FRONT) x = 1;
//                }
//            } else if (finalFacing == Facing.UP) {
//
//            }
//        } else if (parentFacing== Facing.UP) {
//            if (finalFacing == Facing.BACK) {
//                if (facing == Facing.FRONT) x = 1;
//            }
//        }

//        if ((parentFacing== Facing.UP && parentParentFacing == Facing.BACK && finalFacing == Facing.BACK)
//                || (parentFacing == Facing.FRONT && finalFacing == Facing.BACK && parentParentFacing != Facing.DOWN && parentParentFacing != Facing.LEFT)
//                || (parentFacing == Facing.FRONT && finalFacing == Facing.UP)) {
//            if (facing == Facing.FRONT) x = 1;
//            else if (facing == Facing.DOWN) y = 1;
//            else if (facing == Facing.UP) y = -1;
//            else if (facing == Facing.LEFT) z = -1;
//            else if (facing == Facing.RIGHT) z = 1;
//        } else if ((parentFacing == Facing.DOWN && finalFacing == Facing.UP)
//                || (parentFacing == Facing.FRONT && finalFacing == Facing.BACK)
//                || (parentFacing == Facing.DOWN && finalFacing == Facing.BACK)){
//            if (facing == Facing.FRONT) y = 1;
//        } else if (parentFacing == Facing.LEFT && finalFacing == Facing.BACK) {
//            if (facing == Facing.FRONT) z = -1;
//        }
//
//
//        if ((structure.getParent().getFacing() == Facing.UP && structure.getParent().getParent().getColor() == Color.CORE) || structure.getParent().getFacing() == Facing.FRONT) {
//
//            if (facing == Facing.FRONT) x = 1;
//            else if (facing == Facing.DOWN) y = 1;
//            else if (facing == Facing.UP) y = -1;
//            else if (facing == Facing.LEFT) z = -1;
//            else if (facing == Facing.RIGHT) z = 1;
//        } else if (structure.getParent().getFacing() == Facing.DOWN) {
//            if (facing == Facing.FRONT) y = 1;
//        }

        return transformPos(pos, direction, x, y, z);
    }
}
