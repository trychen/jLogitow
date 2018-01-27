package com.trychen.logitow.forge.build;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageUpdateTileEntity implements IMessage {
    public int x, y, z;
    public Operate operate;

    public MessageUpdateTileEntity() {

    }

    public MessageUpdateTileEntity(int x, int y, int z, Operate operate) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.operate = operate;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = ByteBufUtils.readVarInt(buf, 5);
        y = ByteBufUtils.readVarInt(buf, 5);
        z = ByteBufUtils.readVarInt(buf, 5);
        operate = Operate.valueOf(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeVarInt(buf, x, 5);
        ByteBufUtils.writeVarInt(buf, y, 5);
        ByteBufUtils.writeVarInt(buf, z, 5);
        ByteBufUtils.writeUTF8String(buf, operate.name());
    }

    public static class MessageHolder implements IMessageHandler<MessageUpdateTileEntity, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageUpdateTileEntity message, final MessageContext ctx) {
            World world = ctx.getServerHandler().player.getServerWorld();
            BlockPos blockPos = new BlockPos(message.x, message.y, message.z);
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if (tileEntity instanceof TileEntityCoreBlock) {
                if (message.operate == Operate.XMIRROR) {
                    ((TileEntityCoreBlock) tileEntity).setMirrorX(!((TileEntityCoreBlock) tileEntity).isMirrorX());
                }
//                tileEntity.markDirty();
            }
            return null;
        }
    }

    enum Operate {
        XMIRROR, YMIRROR, ZMIRROR;
    }
}
