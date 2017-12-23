package com.trychen.logitow.forge;

import com.trychen.logitow.stack.Color;
import com.trychen.logitow.stack.Facing;
import com.trychen.logitow.stack.Structure;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface StructureHelper {
    static void check(Structure structure, TileEntity tileEntity, BlockPos pos) {
        int direction = 3;
        if (structure.getColor() == Color.CORE) {
            Structure[] children = structure.getChildren();
            Structure upStructure = children[Facing.UP.id];
            World world = tileEntity.getWorld();
            BlockPos upPos = transformPos(pos, direction, 1, 0, 0);
            IBlockState blockState = world.getBlockState(upPos);
            if (blockState.getBlock() == Blocks.WOOL && upStructure == null) world.setBlockToAir(upPos);
            else if (blockState.getBlock() == Blocks.AIR && upStructure != null) {
                if (blockState.getBlock() != Blocks.WOOL) {
                    world.setBlockState(upPos, Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, transformToEnumDyeColor(upStructure.getColor())));
                } else if (blockState.getBlock() != Blocks.WOOL && transformToColor(blockState.getValue(BlockColored.COLOR)) != upStructure.getColor()) {
                    world.setBlockState(upPos, blockState.withProperty(BlockColored.COLOR, transformToEnumDyeColor(upStructure.getColor())));
                }
            }
            if (upStructure != null)
                for (Structure child : upStructure.getChildren())
                    if (child != null) check(structure, tileEntity, upPos);
        } else {
            if (structure.getFacing() == Facing.UP) {
                Structure up = structure.getChildren(Facing.FRONT);

            }
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
}
