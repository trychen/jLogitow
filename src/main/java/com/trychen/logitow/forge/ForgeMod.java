package com.trychen.logitow.forge;

import com.trychen.logitow.LogitowBLEStack;
import com.trychen.logitow.forge.build.BlockController;
import com.trychen.logitow.forge.build.GuiCoreBlockSetting;
import com.trychen.logitow.forge.build.MessageUpdateTileEntity;
import com.trychen.logitow.forge.build.TileEntityCoreBlock;
import com.trychen.logitow.forge.event.LogitowBlockDataEvent;
import com.trychen.logitow.forge.event.LogitowConnectedEvent;
import com.trychen.logitow.forge.event.LogitowDisconnectedEvent;
import com.trychen.logitow.forge.event.LogitowVoltageEvent;
import com.trychen.logitow.forge.ui.GuiLogitow;
import com.trychen.logitow.forge.ui.LogitowToast;
import com.trychen.logitow.stack.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Iterator;
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
        if (!LogitowBLEStack.isAvailable()) return;
        LogitowBLEStack.addCallback(this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        TileEntity.register("logitow:core_block", TileEntityCoreBlock.class);
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        network.registerMessage(new MessageUpdateTileEntity.MessageHolder(), MessageUpdateTileEntity.class, 0, Side.SERVER);
        KeyHelper.INSTANCE.init();

        if (LogitowBLEStack.isAvailable()) {
            LogitowBLEStack.startScan();
        }
    }

    /* ===========================
     *          Network
     * ===========================
     */

    private static SimpleNetworkWrapper network;

    /**
     * *
     * @return shared network wrapper named "pangu"
     */
    public static SimpleNetworkWrapper getNetwork() {
        return network;
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
        } else if (Minecraft.getMinecraft().currentScreen instanceof GuiCoreBlockSetting){
            ((GuiCoreBlockSetting) Minecraft.getMinecraft().currentScreen).deviceUpdated();
        }
        LogitowToast.showConnect(deviceUUID);
        BlockController.lastConnectedDevice = deviceUUID;
    }

    @Override
    public void onDisconnected(UUID deviceUUID) {
        MinecraftForge.EVENT_BUS.post(new LogitowDisconnectedEvent(deviceUUID));

        if (Minecraft.getMinecraft().currentScreen instanceof GuiLogitow) {
            ((GuiLogitow) Minecraft.getMinecraft().currentScreen).deviceUpdated();
        } else if (Minecraft.getMinecraft().currentScreen instanceof GuiCoreBlockSetting){
            ((GuiCoreBlockSetting) Minecraft.getMinecraft().currentScreen).deviceUpdated();
        }

        LogitowToast.showDisconnect(deviceUUID);

        if (BlockController.lastConnectedDevice != null && deviceUUID.equals(BlockController.lastConnectedDevice)){
            if (LogitowBLEStack.getConnectedDevicesUUID().size() != 0) {
                Iterator<UUID> iterable = LogitowBLEStack.getConnectedDevicesUUID().iterator();
                BlockController.lastConnectedDevice = iterable.next();
            } else {
                BlockController.lastConnectedDevice = null;
            }
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
