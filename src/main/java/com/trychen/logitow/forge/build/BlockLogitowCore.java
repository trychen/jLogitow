package com.trychen.logitow.forge.build;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockLogitowCore extends Block {
    public BlockLogitowCore() {
        super(Material.IRON);
        setRegistryName("logitow:core_block");
        setUnlocalizedName("core_block");
        setHardness(2f);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityLogitowCore();
    }
}