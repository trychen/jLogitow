package com.trychen.logitow.stack;

public enum BluetoothState {
    UnknownState(-1),
    Unknown(0),
    Resetting(1),
    Unsupported(2),
    Unauthorized(3),
    PoweredOff(4),
    PoweredOn(5);

    public final int code;

    BluetoothState(int code) {
        this.code = code;
    }

    public static BluetoothState valueOf(int code){
        for (BluetoothState bluetoothState : values()) {
            if (bluetoothState.code == code) return bluetoothState;
        }
        return UnknownState;
    }
}