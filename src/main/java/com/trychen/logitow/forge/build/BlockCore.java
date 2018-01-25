package com.trychen.logitow.forge.build;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * the core block to build
 */
public class BlockCore extends BlockDirectional implements ITileEntityProvider {
    public static final PropertyBool ENABLED = PropertyBool.create("enabled");

    public BlockCore() {
        super(Material.CLOTH);
        this.setRegistryName("logitow:core_block");
        this.setUnlocalizedName("core_block");

        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ENABLED, true));

        this.setHardness(2f);
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    @Override
    @Nonnull
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
    }

    /**
     * creating a block state with PropertyDirection
     */
    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ENABLED);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCoreBlock(worldIn);
    }

    //    @Override
//    public void breakBlock(World world, BlockPos pos, IBlockState state) {
//        super.breakBlock(world, pos, state);
//        world.removeTileEntity(pos);
//    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiCoreBlockSetting(worldIn, pos, state));
        return true;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= (state.getValue(FACING)).getIndex();

        if (state.getValue(ENABLED)) {
            i |= 8;
        }

        return i;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(ENABLED, (meta & 8) > 0);
    }

    @Nullable
    public static EnumFacing getFacing(int meta) {
        int i = meta & 7;
        return i > 5 ? null : EnumFacing.getFront(i);
    }

}