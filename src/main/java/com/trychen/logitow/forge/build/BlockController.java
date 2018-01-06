package com.trychen.logitow.forge.build;

import com.trychen.logitow.forge.event.LogitowBlockDataEvent;
import com.trychen.logitow.stack.BlockBuilder;
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

    @SubscribeEvent
    public static void onBlockDataReceived(LogitowBlockDataEvent event) {
        BlockBuilder builder = getBlockBuilder(event.getDeviceUUID()).connect(event.getBlockData());
        if (event.getBlockData().newBlockID == 0) {
            Set<BlockBuilder> checkingBlocks = builder.getAllBlocks(new HashSet<>());

            for (TileEntity tileEntity : Minecraft.getMinecraft().world.loadedTileEntityList) {
                BlockPos pos = tileEntity.getPos();
                World world = tileEntity.getWorld();
                for (BlockBuilder checkingBlock : checkingBlocks) {
                    BlockPos aim = pos.add(checkingBlock.getPos().getX(), checkingBlock.getPos().getY(), checkingBlock.getPos().getZ());
                    if (world.getBlockState(aim).getBlock() == Blocks.WOOL) {
                        world.setBlockState(aim, Blocks.AIR.getDefaultState());
                    }
                }
            }
        } else {
            for (TileEntity tileEntity : Minecraft.getMinecraft().world.loadedTileEntityList) {
                if (!(tileEntity instanceof TileEntityLogitowCore)) continue;

                BlockPos pos = tileEntity.getPos();
                World world = tileEntity.getWorld();

                BlockPos aim = pos.add(builder.getPos().getX(), builder.getPos().getY(), builder.getPos().getZ());
                IBlockState blockState = world.getBlockState(aim);
                if (blockState.getBlock() == Blocks.WOOL || blockState.getBlock() == Blocks.AIR || blockState.getBlock() == Blocks.GLASS) {
                    world.setBlockState(aim, Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, transformToEnumDyeColor(builder.getBlockColor())));
                }
            }
        }
    }

    public static BlockBuilder getBlockBuilder(UUID deviceUUID) {
        BlockBuilder block = blocks.get(deviceUUID);
        if (block == null) blocks.put(deviceUUID, block = new BlockBuilder());
        return block;
    }
}
