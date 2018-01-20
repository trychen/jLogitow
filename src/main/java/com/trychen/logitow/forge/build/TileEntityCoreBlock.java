package com.trychen.logitow.forge.build;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class TileEntityCoreBlock extends TileEntity {
    private UUID selectedDevice;
    private boolean mirrorX = false, mirrorY = false, mirrorZ = false;
    private UUID tempDevice;

    /**
     * the last built device, to avoid switching in different device.
     */
    private UUID lastDataDevice;

    public TileEntityCoreBlock() {

    }

    public TileEntityCoreBlock(World world) {
        this.world = world;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagCompound logitow = new NBTTagCompound();
        logitow.setString("DeviceUUID", selectedDevice == null ? "null" : selectedDevice.toString());
        logitow.setBoolean("XMirror", mirrorX);
        logitow.setBoolean("YMirror", mirrorY);
        logitow.setBoolean("ZMirror", mirrorZ);
        compound.setTag("Logitow", logitow);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("Logitow")) {
            NBTTagCompound logitow = compound.getCompoundTag("Logitow");

            mirrorX = logitow.getBoolean("XMirror");
            mirrorY = logitow.getBoolean("YMirror");
            mirrorZ = logitow.getBoolean("ZMirror");

            String uuid = logitow.getString("DeviceUUID");
            if (uuid.isEmpty() || uuid.equalsIgnoreCase("null")) selectedDevice = null;
            else try {
                selectedDevice = UUID.fromString(uuid);
            } catch (IllegalArgumentException e) {
                selectedDevice = null;
            }
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
        return mirrorX;
    }

    public void setSelectedDevice(UUID selectedDevice) {
        this.selectedDevice = selectedDevice;
    }

    public void setMirror(boolean mirror) {
        this.mirrorX = mirror;
    }

    public boolean checkIfSelectedDevice(UUID device) {
        if (selectedDevice == null)
            return BlockController.lastConnectedDevice != null && BlockController.lastConnectedDevice.equals(device);
        return selectedDevice.equals(device);
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    public int getBlockMetadata()
    {
        return 0;
    }
}
