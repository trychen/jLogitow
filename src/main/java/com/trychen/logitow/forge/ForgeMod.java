package com.trychen.logitow.forge;

import com.trychen.logitow.LogiTowBLEStack;
import com.trychen.logitow.forge.event.LogitowBlockDataEvent;
import com.trychen.logitow.forge.event.LogitowConnectedEvent;
import com.trychen.logitow.forge.event.LogitowDisconnectedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = ForgeMod.MODID, version = ForgeMod.VERSION)
public class ForgeMod {
    public static final String MODID = "logitow";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
        LogiTowBLEStack.INSTANCE.addConnectedRunnable(() -> MinecraftForge.EVENT_BUS.post(new LogitowConnectedEvent()));
        LogiTowBLEStack.INSTANCE.addBlockDataConsumer(blockData -> MinecraftForge.EVENT_BUS.post(new LogitowBlockDataEvent(blockData)));
        LogiTowBLEStack.INSTANCE.addDisconnectedRunnable(() -> MinecraftForge.EVENT_BUS.post(new LogitowDisconnectedEvent()));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LogiTowBLEStack.INSTANCE.init();
        if (!LogiTowBLEStack.isAvailable()) return;
        LogiTowBLEStack.INSTANCE.startScan();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void data(LogitowBlockDataEvent event){

    }
}
