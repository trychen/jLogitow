package com.trychen.logitow.forge.build;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class TileEntityLogitowCore extends TileEntity {
    static {
        TileEntity.register("logitow_core", TileEntityLogitowCore.class);
    }

    private boolean enable = true;
    private UUID selectedDevice;
    private boolean mirror = false;

    /**
     * the last built device, to avoid switching in different device.
     */
    private UUID lastDataDevice;

    public TileEntityLogitowCore() {

    }

    public TileEntityLogitowCore(World world) {
        this.world = world;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setString("DeviceUUID", selectedDevice == null?"null":selectedDevice.toString());
        compound.setBoolean("Mirror", mirror);
        compound.setBoolean("Enable", enable);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        enable = compound.getBoolean("Enable");
        mirror = compound.getBoolean("Mirror");

        String uuid = compound.getString("DeviceUUID");
        if (uuid == null || uuid.isEmpty() || uuid.equalsIgnoreCase("null")) selectedDevice = null;
        else try {
            selectedDevice = UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            selectedDevice = null;
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {

        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new SPacketUpdateTileEntity(getPos(), 0, nbttagcompound);
    }

    public UUID getSelectedDevice() {
        return selectedDevice;
    }

    public boolean isMirror() {
        return mirror;
    }

    public void setSelectedDevice(UUID selectedDevice) {
        this.selectedDevice = selectedDevice;
    }

    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    public boolean checkIfSelectedDevice(UUID device) {
        if (selectedDevice == null) return BlockController.lastConnectedDevice != null && BlockController.lastConnectedDevice.equals(device);
        return selectedDevice.equals(device);
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }
}
