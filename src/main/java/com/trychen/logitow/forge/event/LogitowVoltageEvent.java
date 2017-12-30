package com.trychen.logitow.forge.event;

import java.util.UUID;

public class LogitowVoltageEvent extends LogitowEvent {
    private final float voltage;

    public LogitowVoltageEvent(UUID deviceUUID, float voltage) {
        super(deviceUUID);
        this.voltage = voltage;
    }

    public float getVoltage() {
        return voltage;
    }
}
