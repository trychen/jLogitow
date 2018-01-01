package com.trychen.logitow.forge;

import com.trychen.logitow.LogiTowBLEStack;
import com.trychen.logitow.forge.event.LogitowBlockDataEvent;
import com.trychen.logitow.forge.event.LogitowConnectedEvent;
import com.trychen.logitow.forge.event.LogitowDisconnectedEvent;
import com.trychen.logitow.forge.event.LogitowVoltageEvent;
import com.trychen.logitow.forge.ui.GuiLogitow;
import com.trychen.logitow.stack.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.UUID;

/**
 * Minecraft forge impl
 *
 * @author trychen
 * @since 1.2
 */
@Mod(modid = ForgeMod.MODID, version = ForgeMod.VERSION, name = ForgeMod.NAME, clientSideOnly = true)
public class ForgeMod implements BLEStackCallback {
    public static final String MODID = "logitow";
    public static final String NAME = "logitow";
    public static final String VERSION = "1.2";

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
        if (!LogiTowBLEStack.isAvailable()) return;
        LogiTowBLEStack.addCallback(this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        KeyHelper.INSTANCE.init();
        if (LogiTowBLEStack.isAvailable()) {
            LogiTowBLEStack.startScan();
        }
    }

    /* ===========================
     *       BLE Stack Part
     * ===========================
     */

    @Override
    public void onConnected(UUID deviceUUID) {
        MinecraftForge.EVENT_BUS.post(new LogitowConnectedEvent(deviceUUID));
        if (Minecraft.getMinecraft().currentScreen instanceof GuiLogitow) {
            ((GuiLogitow) Minecraft.getMinecraft().currentScreen).deviceUpdated();
        }
    }

    @Override
    public void onDisconnected(UUID deviceUUID) {
        MinecraftForge.EVENT_BUS.post(new LogitowDisconnectedEvent(deviceUUID));
        if (Minecraft.getMinecraft().currentScreen instanceof GuiLogitow) {
            ((GuiLogitow) Minecraft.getMinecraft().currentScreen).deviceUpdated();
        }
    }

    @Override
    public boolean onBlockDataReceived(UUID deviceUUID, BlockData blockData) {
        MinecraftForge.EVENT_BUS.post(new LogitowBlockDataEvent(deviceUUID, blockData));

        if (Minecraft.getMinecraft().currentScreen instanceof GuiLogitow) {
            ((GuiLogitow) Minecraft.getMinecraft().currentScreen).lastInsertBlock(blockData);
        }

        return false;
    }

    @Override
    public boolean onVoltageDataReceived(UUID deviceUUID, float voltage) {
        MinecraftForge.EVENT_BUS.post(new LogitowVoltageEvent(deviceUUID, voltage));
        return false;
    }
}
