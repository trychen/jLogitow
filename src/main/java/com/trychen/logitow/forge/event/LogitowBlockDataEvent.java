package com.trychen.logitow.forge.event;

import com.trychen.logitow.stack.BlockData;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.UUID;

@Cancelable
public class LogitowBlockDataEvent extends LogitowEvent {
    private final BlockData blockData;

    public LogitowBlockDataEvent(UUID deviceUUID, BlockData blockData) {
        super(deviceUUID);
        this.blockData = blockData;
    }

    public BlockData getBlockData() {
        return blockData;
    }
}
