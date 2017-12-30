package com.trychen.logitow.forge.event;

import java.util.UUID;

public class LogitowDisconnectedEvent extends LogitowEvent {
    public LogitowDisconnectedEvent(UUID deviceUUID) {
        super(deviceUUID);
    }
}
