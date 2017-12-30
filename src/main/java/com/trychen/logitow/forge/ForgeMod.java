package com.trychen.logitow.forge;

import com.trychen.logitow.LogiTowBLEStack;
import com.trychen.logitow.forge.event.LogitowBlockDataEvent;
import com.trychen.logitow.forge.event.LogitowConnectedEvent;
import com.trychen.logitow.forge.event.LogitowDisconnectedEvent;
import com.trychen.logitow.stack.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Minecraft forge impl
 *
 * @author trychen
 * @since 1.2
 */
@Mod(modid = ForgeMod.MODID, version = ForgeMod.VERSION, name = ForgeMod.NAME)
public class ForgeMod implements BLEStackCallback{
    public static final String MODID = "logitow";
    public static final String NAME = "logitow";
    public static final String VERSION = "1.1";

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
        if (!LogiTowBLEStack.isAvailable()) return;
        LogiTowBLEStack.addCallback(this);
    }

    public static Structure structure = new Structure();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (!LogiTowBLEStack.isAvailable()) return;
        LogiTowBLEStack.startScan();
    }

    @Override
    public void onConnected(UUID deviceUUID) {
        System.out.println("成功连接到了：" + deviceUUID);
    }

    @Override
    public boolean onBlockDataReceived(UUID deviceUUID, BlockData blockData) {
        Coordinate c = structure.insert(blockData).getCoordinate();
        System.out.println(String.format("x: %d, y: %d, z: %d", c.getX(), c.getY(), c.getZ()));
        return false;
    }
}
