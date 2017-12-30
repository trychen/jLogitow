package com.trychen.logitow.forge.event;

import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.UUID;

public class LogitowEvent extends Event{
    private final UUID deviceUUID;

    public LogitowEvent(UUID deviceUUID) {
        this.deviceUUID = deviceUUID;
    }

    public UUID getDeviceUUID() {
        return deviceUUID;
    }
}
