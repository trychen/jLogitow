package com.trychen.logitow.forge.build;

import com.trychen.logitow.forge.Register;
import com.trychen.logitow.forge.event.LogitowBlockDataEvent;
import com.trychen.logitow.stack.BlockBuilder;
import com.trychen.logitow.stack.Color;
import com.trychen.logitow.stack.Coordinate;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

import static com.trychen.logitow.forge.Utils.transformToEnumDyeColor;
import static com.trychen.logitow.forge.build.BlockCore.ENABLED;

@Mod.EventBusSubscriber
public class BlockController {
    private static Map<UUID, BlockBuilder> blocks = new HashMap<>();
    private static Map<UUID, BlockBuilder> removedBlocks = new HashMap<>();

    /**
     * the last connected device
     */
    public static UUID lastConnectedDevice;

    @SubscribeEvent
    public static void onBlockDataReceived(LogitowBlockDataEvent event) {
        // handle the data to BlockBuilder
        BlockBuilder builder = getBlockBuilder(event.getDeviceUUID()).connect(event.getBlockData());

        if (builder == null) return;

        Minecraft.getMinecraft().addScheduledTask(() -> {
            // check NPE when haven't enter game
            if (Minecraft.getMinecraft().world == null) return;

            // get all sub block when deleting
            Set<BlockBuilder> checkingBlocks = event.getBlockData().newBlockID == 0 ? builder.getAllBlocks(new HashSet<>()) : null;

            // for-each all loaded TileEntity
            for (TileEntity tileEntity : Minecraft.getMinecraft().world.loadedTileEntityList) {
                // check if core block's TileEntity
                if (!(tileEntity instanceof TileEntityCoreBlock)) continue;
                TileEntityCoreBlock tileEntityLogitow = (TileEntityCoreBlock) tileEntity;
                IBlockState logitowState = tileEntity.getWorld().getBlockState(tileEntityLogitow.getPos());

                // check if needed device
                if (!tileEntityLogitow.checkIfSelectedDevice(event.getDeviceUUID())) continue;

                if (logitowState.getBlock() != Register.logitowCore) continue;

                // check if disabled
                if (!logitowState.getValue(ENABLED)) continue;

                BlockPos pos = tileEntity.getPos();
                World world = tileEntity.getWorld();
                EnumFacing face = world.getBlockState(pos).getValue(BlockCore.FACING);

                // distinguish between deleting and inserting
                if (event.getBlockData().newBlockID == 0) {
                    for (BlockBuilder checkingBlock : checkingBlocks) {
                        // get the aim block's pos
                        BlockPos aim = resolvePos(pos, checkingBlock.getPos(), face, tileEntityLogitow.isMirrorX());

                        IBlockState aimState = world.getBlockState(aim);
                        // set block to air when it's wool
                        if (aimState.getBlock() == Blocks.WOOL) {
                            Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(aim, aimState);
                            world.setBlockState(aim, Blocks.AIR.getDefaultState());
                        }
                    }
                } else {
                    // get the aim block's pos
                    BlockPos aim = resolvePos(pos, builder.getPos(), face, tileEntityLogitow.isMirrorX());
                    IBlockState blockState = world.getBlockState(aim);

                    // set block to wool block when it's wool, air or grass
                    if (blockState.getBlock() == Blocks.WOOL || blockState.getBlock() == Blocks.AIR || blockState.getBlock() == Blocks.GLASS) {
                        IBlockState newState = Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, transformToEnumDyeColor(builder.getBlockColor()));
                        world.setBlockState(aim, newState);
                        Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(aim, newState);
                    }

                    // set to disable when end block inserted
                    if (event.getBlockData().getNewBlockColor() == Color.END) {
                        world.setBlockState(pos, blockState.withProperty(ENABLED, false));
                    }
                }
            }
        });
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

    public static BlockPos resolvePos(BlockPos absoluteCenterPos, Coordinate relativePos, EnumFacing facing, boolean mirror) {
        if (mirror) System.out.println("mirroring");
        int x = 0, y = 0, z = 0;
        if (facing == EnumFacing.NORTH) {
            x -= relativePos.getX();
            y += relativePos.getY();
            z += relativePos.getZ();
        } else if (facing == EnumFacing.SOUTH) {
            x += relativePos.getX();
            y += relativePos.getY();
            z -= relativePos.getZ();
            if (mirror) x = -relativePos.getX();
        } else if (facing == EnumFacing.EAST) {
            z -= relativePos.getX();
            y += relativePos.getY();
            x -= relativePos.getZ();
        } else if (facing == EnumFacing.WEST) {
            z += relativePos.getX();
            y += relativePos.getY();
            x += relativePos.getZ();
        } else if (facing == EnumFacing.UP) {
            x -= relativePos.getX();
            z += relativePos.getY();
            y -= relativePos.getZ();
        } else if (facing == EnumFacing.DOWN) {
            x -= relativePos.getX();
            z += relativePos.getY();
            y += relativePos.getZ();
        }
        return absoluteCenterPos.add(x, y, z);
    }

    @SubscribeEvent
    public static void blockBreak(BlockEvent.BreakEvent event){
        if (Minecraft.getMinecraft().currentScreen instanceof GuiCoreBlockSetting){
            if (event.getPos().equals(((GuiCoreBlockSetting) Minecraft.getMinecraft().currentScreen).getTargetPos())) {
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
        }

    }
}
