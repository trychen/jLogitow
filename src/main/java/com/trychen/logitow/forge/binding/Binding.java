package com.trychen.logitow.forge.binding;

import com.trychen.logitow.forge.event.LogitowBlockDataEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Binding {
    @SubscribeEvent(priority = EventPriority.LOW)
    public void blockData(LogitowBlockDataEvent event) {

    }
}
