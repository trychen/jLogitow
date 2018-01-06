package com.trychen.logitow.forge.build;

import net.minecraft.client.gui.GuiScreen;

public class GuiCoreBlockSetting extends GuiScreen {
    private TileEntityLogitowCore tileEntity;
    public GuiCoreBlockSetting(TileEntityLogitowCore tileEntity) {
        this.tileEntity = tileEntity;
    }
}
