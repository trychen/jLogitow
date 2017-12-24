package com.trychen.logitow.forge;

import com.trychen.logitow.LogiTowBLEStack;
import com.trychen.logitow.forge.event.LogitowBlockDataEvent;
import com.trychen.logitow.forge.event.LogitowConnectedEvent;
import com.trychen.logitow.forge.event.LogitowDisconnectedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Minecraft forge impl
 *
 * @author trychen
 * @since 1.2
 */
@Mod(modid = ForgeMod.MODID, version = ForgeMod.VERSION, name = ForgeMod.NAME)
public class ForgeMod {
    public static final String MODID = "logitow";
    public static final String NAME = "logitow";
    public static final String VERSION = "1.1";

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
        if (!LogiTowBLEStack.isAvailable()) return;
        LogiTowBLEStack.addConnectedRunnable(() -> MinecraftForge.EVENT_BUS.post(new LogitowConnectedEvent()));
        LogiTowBLEStack.addBlockDataConsumer(blockData -> MinecraftForge.EVENT_BUS.post(new LogitowBlockDataEvent(blockData)));
        LogiTowBLEStack.addDisconnectedRunnable(() -> MinecraftForge.EVENT_BUS.post(new LogitowDisconnectedEvent()));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (!LogiTowBLEStack.isAvailable()) return;
        LogiTowBLEStack.startScan();
    }
}
