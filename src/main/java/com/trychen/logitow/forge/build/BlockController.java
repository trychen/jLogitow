package com.trychen.logitow.forge.build;

import com.trychen.logitow.forge.event.LogitowBlockDataEvent;
import com.trychen.logitow.stack.BlockBuilder;
import com.trychen.logitow.stack.Color;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

import static com.trychen.logitow.forge.Utils.transformToEnumDyeColor;

@Mod.EventBusSubscriber
public class BlockController {
    private static Map<UUID, BlockBuilder> blocks = new HashMap<>();
    /**
     * the last connected device
     */
    public static UUID lastConnectedDevice;

    @SubscribeEvent
    public static void onBlockDataReceived(LogitowBlockDataEvent event) {
        // handle the data to BlockBuilder
        BlockBuilder builder = getBlockBuilder(event.getDeviceUUID()).connect(event.getBlockData());

        // check NPE when haven't enter game
        if (Minecraft.getMinecraft().world == null) return;

        // get all sub block when deleting
        Set<BlockBuilder> checkingBlocks = event.getBlockData().newBlockID == 0 ? builder.getAllBlocks(new HashSet<>()) : null;

        // for-each all loaded TileEntity
        for (TileEntity tileEntity : Minecraft.getMinecraft().world.loadedTileEntityList) {
            // check if core block's TileEntity
            if (!(tileEntity instanceof TileEntityLogitowCore)) continue;
            // check if needed device
            if (!((TileEntityLogitowCore) tileEntity).checkIfSelectedDevice(event.getDeviceUUID())) continue;
            // check if disabled
            if (!((TileEntityLogitowCore) tileEntity).isEnable()) continue;

            BlockPos pos = tileEntity.getPos();
            World world = tileEntity.getWorld();

            // distinguish between deleting and inserting
            if (event.getBlockData().newBlockID == 0) {
                for (BlockBuilder checkingBlock : checkingBlocks) {
                    // get the aim block's pos
                    BlockPos aim = pos.add(checkingBlock.getPos().getX(), checkingBlock.getPos().getY(), checkingBlock.getPos().getZ());

                    // set block to air when it's wool
                    if (world.getBlockState(aim).getBlock() == Blocks.WOOL) {
                        world.setBlockState(aim, Blocks.AIR.getDefaultState());
                    }
                }
            } else {
                // get the aim block's pos
                BlockPos aim = pos.add(builder.getPos().getX(), builder.getPos().getY(), builder.getPos().getZ());
                IBlockState blockState = world.getBlockState(aim);

                // set block to wool block when it's wool, air or grass
                if (blockState.getBlock() == Blocks.WOOL || blockState.getBlock() == Blocks.AIR || blockState.getBlock() == Blocks.GLASS) {
                    world.setBlockState(aim, Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, transformToEnumDyeColor(builder.getBlockColor())));
                }

                // set to disable when end block inserted
                if (event.getBlockData().getNewBlockColor() == Color.END) {
                    ((TileEntityLogitowCore) tileEntity).setEnable(false);
                }
            }
        }
    }

    /**
     * get the block builder of device, every device should have their own BlockBuilder
     *
     * @param deviceUUID the device to be build
     */
    public static BlockBuilder getBlockBuilder(UUID deviceUUID) {
        BlockBuilder block = blocks.get(deviceUUID);
        if (block == null) blocks.put(deviceUUID, block = new BlockBuilder());
        return block;
    }
}
