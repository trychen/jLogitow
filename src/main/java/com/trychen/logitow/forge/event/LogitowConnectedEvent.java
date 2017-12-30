package com.trychen.logitow.forge.event;

import java.util.UUID;

public class LogitowConnectedEvent extends LogitowEvent {
    public LogitowConnectedEvent(UUID deviceUUID) {
        super(deviceUUID);
    }
}
