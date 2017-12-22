package com.trychen.logitow.forge.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class LogitowBlock extends Block {
    public LogitowBlock() {
        super(Material.IRON);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

}
