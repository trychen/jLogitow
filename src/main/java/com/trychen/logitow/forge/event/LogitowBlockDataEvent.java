package com.trychen.logitow.forge.event;

import com.trychen.logitow.stack.BlockData;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class LogitowBlockDataEvent extends Event {
    private BlockData blockData;

    public LogitowBlockDataEvent(BlockData blockData) {
        this.blockData = blockData;
    }

    public BlockData getBlockData() {
        return blockData;
    }
}
