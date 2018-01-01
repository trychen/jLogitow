package com.trychen.logitow.forge.build;

import net.minecraft.tileentity.TileEntity;

public class TileEntityLogitowCore extends TileEntity {
    static {
        TileEntity.register("logitow_core", TileEntityLogitowCore.class);
    }
    public TileEntityLogitowCore() {
    }
}
