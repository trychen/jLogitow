package com.trychen.logitow.stack;

import java.util.UUID;

/**
 * @since 1.2
 * @author trychen
 */
public interface BLEStackCallback {
    /**
     * @return cancel event
     */
    default boolean onStartScan() { return false; }

    default void onDisconnected(UUID deviceUUID) {}

    default void onConnected(UUID deviceUUID) {}

    /**
     * @return cancel event
     */
    default boolean onBlockDataReceived(UUID deviceUUID, BlockData blockData) { return false; }

    /**
     * @return cancel event
     */
    default boolean onVoltageDataReceived(UUID deviceUUID, float voltage) { return false; }
}
