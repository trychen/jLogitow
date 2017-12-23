package com.trychen.logitow.forge;

import com.trychen.logitow.LogiTowBLEStack;
import com.trychen.logitow.forge.block.TileEntityLogitow;
import com.trychen.logitow.forge.event.LogitowBlockDataEvent;
import com.trychen.logitow.forge.event.LogitowConnectedEvent;
import com.trychen.logitow.forge.event.LogitowDisconnectedEvent;
import com.trychen.logitow.stack.Facing;
import com.trychen.logitow.stack.Structure;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod(modid = ForgeMod.MODID, version = ForgeMod.VERSION)
public class ForgeMod {
    public static final String MODID = "logitow";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
        LogiTowBLEStack.addConnectedRunnable(() -> MinecraftForge.EVENT_BUS.post(new LogitowConnectedEvent()));
        LogiTowBLEStack.addBlockDataConsumer(blockData -> MinecraftForge.EVENT_BUS.post(new LogitowBlockDataEvent(blockData)));
        LogiTowBLEStack.addDisconnectedRunnable(() -> MinecraftForge.EVENT_BUS.post(new LogitowDisconnectedEvent()));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
//        LogiTowBLEStack.init();
        if (!LogiTowBLEStack.isAvailable()) return;
        LogiTowBLEStack.startScan();
        MinecraftForge.EVENT_BUS.register(this);

        TileEntity.register("logitow_core", TileEntityLogitow.class);
    }

    private Structure structure = new Structure(0, null, Facing.BACK);
    private Set<BlockPos> lastUse;
    @SubscribeEvent
    public void data(LogitowBlockDataEvent event) {
        if (Minecraft.getMinecraft().world == null) return;
        Minecraft.getMinecraft().world.loadedTileEntityList.stream().filter(it -> it instanceof TileEntityLogitow).forEach(tileentity -> {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Set<BlockPos> useCheck = new HashSet<>();
                structure.insert(event.getBlockData());
                StructureHelper.check(structure, tileentity.getWorld(), tileentity.getPos(), useCheck);
                if (lastUse != null) {
                    lastUse.removeAll(useCheck);
                    for (BlockPos blockPos : lastUse) {
                        if (tileentity.getWorld().getBlockState(blockPos).getBlock() == Blocks.WOOL) tileentity.getWorld().setBlockToAir(blockPos);
                    }
                }
                lastUse = useCheck;
                System.out.println(tileentity.getBlockMetadata());
            });
        });
    }
}
