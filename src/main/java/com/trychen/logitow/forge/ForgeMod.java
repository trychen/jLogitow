package com.trychen.logitow.forge;

import com.trychen.logitow.LogiTowBLEStack;
import com.trychen.logitow.forge.event.LogitowBlockDataEvent;
import com.trychen.logitow.forge.event.LogitowConnectedEvent;
import com.trychen.logitow.forge.event.LogitowDisconnectedEvent;
import com.trychen.logitow.forge.event.LogitowVoltageEvent;
import com.trychen.logitow.forge.ui.GuiLogitow;
import com.trychen.logitow.stack.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.UUID;

/**
 * Minecraft forge impl
 *
 * @author trychen
 * @since 1.2
 */
@Mod(modid = ForgeMod.MODID, version = ForgeMod.VERSION, name = ForgeMod.NAME)
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
        if (!LogiTowBLEStack.isAvailable()) return;
        LogiTowBLEStack.startScan();

        MinecraftForge.EVENT_BUS.register(this);
    }

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

    public static Structure structure = new Structure();

    @Override
    public boolean onBlockDataReceived(UUID deviceUUID, BlockData blockData) {
        MinecraftForge.EVENT_BUS.post(new LogitowBlockDataEvent(deviceUUID, blockData));
        System.out.println(structure.insert(blockData).getCoordinate());
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

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void handleKey(GuiScreenEvent.KeyboardInputEvent e) {
        if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
            if (!(Minecraft.getMinecraft().currentScreen instanceof GuiLogitow)) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiLogitow());
            }
        }
    }
}
