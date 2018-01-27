package com.trychen.logitow.forge.build;

import com.trychen.logitow.LogitowBLEStack;
import com.trychen.logitow.forge.ForgeMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuiCoreBlockSetting extends GuiScreen {
    private IBlockState blockState;
    private World worldIn;
    private BlockPos pos;
    private boolean mirrorX;
    private DeviceList deviceList;

    public static final String stateEnabled = I18n.format("core_block.setting.enable.desc");
    public static final String stateDisabled = I18n.format("core_block.setting.disable.desc");
    public static final String mirrorEnabled = I18n.format("core_block.setting.mirror.enable.desc");
    public static final String mirrorDisabled = I18n.format("core_block.setting.mirror.disable.desc");
//    public static final String stateNoDevice = I18n.format("core_block.setting.no_device.desc");

    public GuiCoreBlockSetting(World worldIn, BlockPos pos, IBlockState blockState) {
        this.worldIn = worldIn;
        this.pos = pos;
        this.blockState = blockState;
        mirrorX = ((TileEntityCoreBlock) worldIn.getTileEntity(pos)).isMirrorX();
    }

    @Override
    public void initGui() {
        this.deviceList = new DeviceList(this.mc, 80, this.height / 2, this.height / 4, this.height - (this.height / 4), width / 6, 14);
//        if (tileEntity.getSelectedDevice() != null) {
//            this.deviceList.selectedIndex = this.deviceList.devices.indexOf(tileEntity.getSelectedDevice()) + 1;
//        }
        this.buttonList.add(new GuiButton(1, width / 6, this.height / 4 + this.height / 2, 80, 20, I18n.format("manager.refresh.desc")));

        int align = (width - width / 6 + 100) / 2 - 50;
        String state = blockState.getValue(BlockCore.ENABLED)?stateEnabled:stateDisabled;
        buttonList.add(new GuiButton(2, align, this.height / 4, 200, 20, state));
        buttonList.add(new GuiButton(3, align, this.height / 4 + 30, 200, 20, mirrorX?mirrorEnabled:mirrorDisabled));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, I18n.format("core_block.setting.title.desc"), this.width / 2, 30, 16777215);
        this.deviceList.drawScreen(mouseX, mouseY, partialTicks);
        for (GuiButton guiButton : buttonList) {
            guiButton.drawButton(this.mc, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            deviceList.update();
        } else if (button.id == 2) {
            boolean now = !blockState.getValue(BlockCore.ENABLED);
            worldIn.setBlockState(pos, blockState = blockState.withProperty(BlockCore.ENABLED, now));
            button.displayString = now?stateEnabled:stateDisabled;
        } else if (button.id == 3) {
//            ((TileEntityCoreBlock) worldIn.getTileEntity(pos)).setMirrorX(!mirrorX);
            mirrorX = !mirrorX;
            ForgeMod.getNetwork().sendToServer(new MessageUpdateTileEntity(pos.getX(), pos.getY(), pos.getZ(), MessageUpdateTileEntity.Operate.XMIRROR));
            button.displayString = ((TileEntityCoreBlock) worldIn.getTileEntity(pos)).isMirrorX()?mirrorEnabled:mirrorDisabled;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void deviceUpdated() {
        deviceList.update();
    }

    class DeviceList extends GuiScrollingList {
        int selectedIndex = 0;
        List<UUID> devices;

        DeviceList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight) {
            super(client, width, height, top, bottom, left, entryHeight, GuiCoreBlockSetting.this.width, GuiCoreBlockSetting.this.height);
            devices = new ArrayList<>(LogitowBLEStack.getConnectedDevicesUUID());
        }

        protected boolean isSelected(int index) {
            return index == selectedIndex;
        }

        @Override
        protected void drawBackground() {
        }

        protected int getSize() {
            return devices.size() + 1;
        }

        protected void elementClicked(int index, boolean doubleClick) {
            this.selectedIndex = index;
//            if (selectedIndex == 0) {
//                tileEntity.setSelectedDevice(null);
//            } else {
//                tileEntity.setSelectedDevice(devices.get(selectedIndex - 1));
//            }
        }

        public void update() {
            List<UUID> newDevices = new ArrayList<>(LogitowBLEStack.getConnectedDevicesUUID());
            if (selectedIndex > 0 && newDevices.contains(devices.get(selectedIndex - 1))) {
                selectedIndex = newDevices.indexOf(devices.get(selectedIndex - 1));
            } else {
                selectedIndex = 0;
            }
            devices = newDevices;
        }

        protected void drawSlot(int var1, int width, int height, int var4, Tessellator tess) {
            if (var1 == 0)
                mc.fontRenderer.drawString("Latest", GuiCoreBlockSetting.this.width / 6 + 7, height, 0xFFFFFF);
            else
                mc.fontRenderer.drawString("Logitow " + devices.get(var1 - 1).toString().substring(0, 8), GuiCoreBlockSetting.this.width / 6 + 7, height, 0xFFFFFF);
        }
    }

    public BlockPos getTargetPos() {
        return pos;
    }
}
