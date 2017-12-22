package com.trychen.logitow.api.device;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * device means a connected ForgeMod product
 */
public abstract class Device {
//    /**
//     * get all the connected block
//     */
//    public abstract Collection<LogiBlock> getAllBlocks();

    private CompletableFuture<Float> voltageFutureCache;

    /**
     * The range of the voltage is from 2.1V to 1.5V.
     * when the voltage is close to 1.5V, it means that ForgeMod may no battery
     */
    public CompletableFuture<Float> getVoltage(){
        return voltageFutureCache == null ? (voltageFutureCache = new CompletableFuture<>()) : voltageFutureCache;
    }

    public float getMinVoltage() { return 1.5f; }
    public float getMaxVoltage() { return 2.1f; }

    /**
     * get the percent of rest battery
     */
    public CompletableFuture<Float> getRestBattery() {
        return getVoltage().thenApply((voltage) -> (voltage - getMinVoltage()) / (getMaxVoltage() - getMinVoltage()));
    }

    /**
     * this method is for jni to notify changing the voltage for future
     */
    public void notifyVoltage(float voltage) {
        voltageFutureCache.complete(voltage);
    }

    /**
     * this method is for jni to notify changing the voltage for future
     */
    public void notifyBlocksChanged() {
        // TODO:
    }
}
