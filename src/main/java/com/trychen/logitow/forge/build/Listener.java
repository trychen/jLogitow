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

import java.util.HashSet;
import java.util.Set;

import static com.trychen.logitow.forge.Utils.transformToColor;
import static com.trychen.logitow.forge.Utils.transformToEnumDyeColor;

@Mod.EventBusSubscriber
public class Listener {
    private static final BlockBuilder block = new BlockBuilder();

    @SubscribeEvent
    public static void onBlockDataReceived(LogitowBlockDataEvent event) {
        Set<BlockBuilder> previousBlocks = block.getAllBlocks(new HashSet<>());
        block.connect(event.getBlockData());
        Set<BlockBuilder> blocks = block.getAllBlocks(new HashSet<>());
        previousBlocks.removeAll(blocks);

        Set<BlockBuilder> workedBlock = new HashSet<>();

        for (TileEntity tileEntity : Minecraft.getMinecraft().world.loadedTileEntityList) {
            BlockPos pos = tileEntity.getPos();
            World world = tileEntity.getWorld();
            for (BlockBuilder blockBuilder : blocks) {
                if (workedBlock.contains(blockBuilder)) continue;
                BlockPos aim = pos.add(blockBuilder.getPos().getX(), blockBuilder.getPos().getY(), blockBuilder.getPos().getZ());
                IBlockState blockState = world.getBlockState(aim);
                if (blockState.getBlock() == Blocks.WOOL || blockState.getBlock() == Blocks.AIR || blockState.getBlock() == Blocks.GLASS) {
                    world.setBlockState(aim, Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, transformToEnumDyeColor(blockBuilder.getBlockColor())));
                }
                workedBlock.add(blockBuilder);
            }
            for (BlockBuilder blockBuilder : previousBlocks) {
                if (workedBlock.contains(blockBuilder)) continue;
                BlockPos aim = pos.add(blockBuilder.getPos().getX(), blockBuilder.getPos().getY(), blockBuilder.getPos().getZ());
                if (world.getBlockState(aim).getBlock() == Blocks.WOOL)
                    world.setBlockState(aim, Blocks.AIR.getDefaultState());
                workedBlock.add(blockBuilder);
            }
            workedBlock.clear();
        }
    }
}
