package com.trychen.logitow.forge.ui;

import com.trychen.logitow.LogiTowBLEStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuiLogitow extends GuiScreen{
    private DeviceList deviceList;
    private UUID currentDevice;
    private GuiButton disconnect, refresh;

    @Override
    public void initGui() {

        int align = (width - width / 6 + 100) / 2 - 50;
        this.deviceList = new DeviceList(this.mc, 80, this.height / 2, this.height / 4, this.height - (this.height / 4), width / 6, 14);
        refresh = new GuiButton(1, width / 6, this.height / 4 + this.height / 2, 80, 20, "刷新");
        
        disconnect = new GuiButton(2, align, this.height / 4 + 30, 50, 20, "断开");
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (mc.world == null)this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Logitow 设备管理", this.width / 2, 30, 16777215);
        this.deviceList.drawScreen(mouseX, mouseY, partialTicks);
        int align = (width - width / 6 + 100) / 2 - 50;
        if (currentDevice != null) {
            String id = "设备标识符： " + currentDevice.toString();
            this.drawString(this.fontRenderer, id, align, this.height / 4 + 10, 16777215);

            disconnect.drawButton(this.mc, mouseX, mouseY, partialTicks);
        } else {
            this.drawString(this.fontRenderer, "请在左侧选择连接的设备", (width - width / 6 + 100) / 2, (this.height - (this.height / 2 + 10)), 16777215);
        }
        refresh.drawButton(this.mc, mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            deviceList.update();
        } else if (button.id == 2) {
            if (currentDevice != null)
                LogiTowBLEStack.disconnect(currentDevice.toString());
        }
    }

    public void deviceUpdated(){
        deviceList.update();
        updateScreen();
    }

    class DeviceList extends GuiScrollingList {
        int selectedIndex = -1;
        List<UUID> devices;

        DeviceList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight) {
            super(client, width, height, top, bottom, left, entryHeight, GuiLogitow.this.width, GuiLogitow.this.height);
            devices = new ArrayList<>(LogiTowBLEStack.getConnectedDevicesUUID());
        }

        protected boolean isSelected(int index) {
            return index == selectedIndex;
        }

        @Override protected void drawBackground() { }

        protected int getSize() {
            return devices.size();
        }

        protected void elementClicked(int index, boolean doubleClick) {
            this.selectedIndex = index;
            this.isSelected(selectedIndex);
            GuiLogitow.this.currentDevice = devices.get(selectedIndex);
        }

        public void update(){
            List<UUID> newDevices = new ArrayList<>(LogiTowBLEStack.getConnectedDevicesUUID());
            if (selectedIndex != -1 && newDevices.contains(devices.get(selectedIndex))) {
                selectedIndex = newDevices.indexOf(devices.get(selectedIndex));
            } else {
                selectedIndex = -1;
                currentDevice = null;
            }
            devices = newDevices;
        }

        protected void drawSlot(int var1, int width, int height, int var4, Tessellator tess) {
            mc.fontRenderer.drawString("Logitow " + devices.get(var1).toString().substring(0, 8), GuiLogitow.this.width / 6 + 7, height, 0xFFFFFF);
        }
    }
}
