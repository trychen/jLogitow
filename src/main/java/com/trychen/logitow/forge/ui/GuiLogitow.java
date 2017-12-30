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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.trychen.logitow.LogiTowBLEStack.getMaxVoltage;
import static com.trychen.logitow.LogiTowBLEStack.getMinVoltage;

public class GuiLogitow extends GuiScreen{
    private DeviceList deviceList;
    private UUID currentDevice;
    private CompletableFuture<Float> futureVoltage;
    private int restBattery = -1;
    private float voltage = -1;
    private GuiButton disconnect, refresh, refreshBattery;

    @Override
    public void initGui() {
        int align = (width - width / 6 + 100) / 2 - 50;
        this.deviceList = new DeviceList(this.mc, 80, this.height / 2, this.height / 4, this.height - (this.height / 4), width / 6, 14);
        buttonList.clear();
        buttonList.add(refresh = new GuiButton(1, width / 6, this.height / 4 + this.height / 2, 80, 20, "刷新"));

        buttonList.add(disconnect = new GuiButton(2, align + 70, this.height / 4 + this.height / 2, 30, 20, "断开"));
        buttonList.add(refreshBattery = new GuiButton(3, align, this.height / 4 + this.height / 2, 60, 20, "刷新电量"));
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
            String id = "设备标识符：  " + currentDevice.toString();
            this.drawString(this.fontRenderer, id, align, this.height / 4 + 10, 16777215);

            String restBatteryString;
            if (futureVoltage == null) futureVoltage = LogiTowBLEStack.getVoltage(currentDevice);
            if (futureVoltage.isDone()) {
                if (restBattery < 1 || voltage < 1){
                    try {
                        voltage = futureVoltage.get();
                        if (voltage > getMaxVoltage())
                         restBattery = 100;
                        else restBattery = (int) (((voltage - getMinVoltage()) / (getMaxVoltage() - getMinVoltage())) * 100);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                if (restBattery > 20)
                    restBatteryString = String.format("剩余电量:  §2%d / 100 §r(%.1f)", restBattery, voltage);
                else
                    restBatteryString = String.format("剩余电量:  §c%d / 100 §r(%.1f)", restBattery, voltage);
            } else {
                restBatteryString = "剩余电量:  §3获取中";
            }
            this.drawString(this.fontRenderer, restBatteryString, align, this.height / 4 + 30, 16777215);

            disconnect.drawButton(this.mc, mouseX, mouseY, partialTicks);
            refreshBattery.drawButton(this.mc, mouseX, mouseY, partialTicks);
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
        } else {
            if (currentDevice != null)
                if (button.id == 2) {
                        LogiTowBLEStack.disconnect(currentDevice.toString());
                } else if (button.id == 3) {
                    futureVoltage = null;
                    voltage = -1;
                    restBattery = -1;
                }
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
            GuiLogitow.this.futureVoltage = null;
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
