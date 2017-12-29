package com.trychen.logitow.stack;

import java.util.UUID;

/**
 * @since 1.2
 * @author trychen
 */
public interface BLEStackCallback {
    default boolean onStartScan() { return false; }

    default void onDisconnected(UUID deviceUUID) {}

    default void onConnected(UUID deviceUUID) {}

    default boolean onBlockDataReceived(UUID deviceUUID, BlockData blockData) { return false; }

    default boolean onVoltageDataReceived(UUID deviceUUID, float voltage) { return false; }
}
