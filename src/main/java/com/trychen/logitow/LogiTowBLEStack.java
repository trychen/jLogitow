package com.trychen.logitow;

import com.trychen.logitow.stack.BlockData;
import com.trychen.logitow.stack.BluetoothState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public final class LogiTowBLEStack {
    private static Logger logger = LogManager.getLogger("Logitow BLE Stack");

    private static boolean available = false;

    public static boolean isAvailable() {
        return available;
    }

    static {
        // start loading library
        System.loadLibrary("logitow");
        try {
            setup();
            available = true;
        } catch (UnsatisfiedLinkError error) {
            // fount it unable to found usable native lib for current system
            System.err.println("Couldn't find a satisfied native logitow lib for current system");
            error.printStackTrace();
        }
    }

    private static boolean isScanning = false, isConnected = false;

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static native void setup();

    /**
     * the device's unique uuid
     */
    @Deprecated
    private static UUID connectedDeviceUUID;

    /**
     * start scanning logitow if scan haven't started.
     *
     * @return start scanning success
     */
    public static boolean startScan() {
        if (isScanning) return true;
        isScanning = true;
        return startScanDevice();
    }

    /**
     * get the bluetooth state,
     *
     * @return the state code {@link }
     */
    public static BluetoothState getBluetoothState() {
        return BluetoothState.valueOf(getNativeBluetoothState());
    }

    /**
     * get the native raw id (BluetoothState)
     * for mac: <code>typedef NS_ENUM(NSInteger, CBCentralManagerState) {
     * CBCentralManagerStateUnknown = 0,
     * CBCentralManagerStateResetting,
     * CBCentralManagerStateUnsupported,
     * CBCentralManagerStateUnauthorized,
     * CBCentralManagerStatePoweredOff,
     * CBCentralManagerStatePoweredOn,
     * };</code>
     */
    public static native int getNativeBluetoothState();

    /**
     * start scanning device
     */
    private static native boolean startScanDevice();

    /**
     * stop scanning devices
     */
    private static void stopScan() {
        if (isScanning) stopScanDevice();
    }

    /**
     * stop scanning logitow
     */
    private static native void stopScanDevice();

    /**
     * disconnect device
     *
     * @param scanForOtherDevice restart scanning
     */
    public static native void disconnect(boolean scanForOtherDevice);

    private static final List<Runnable> disconnectedRunnables = new LinkedList<>();

    public static void addDisconnectedRunnable(Runnable runnable) {
        disconnectedRunnables.add(runnable);
    }

    private static void notifyDisconnected(boolean isScanning) {
        LogiTowBLEStack.isScanning = isScanning;
        isConnected = false;

        executorService.submit(() -> disconnectedRunnables.forEach(Runnable::run));
    }

    private static List<Runnable> connectedRunnables = new ArrayList<>();

    public static void addConnectedRunnable(Runnable runnable) {
        connectedRunnables.add(runnable);
    }

    /**
     * this method is for jni to notify changing the voltage for future
     */
    private static void notifyConnected() {
        isConnected = true;
        isScanning = false;

        executorService.submit(() -> connectedRunnables.forEach(Runnable::run));
    }

    public static boolean isConnected() {
        return isConnected;
    }

    private static byte[] previousData;
    private static List<Consumer<BlockData>> blockdataConsumers = new ArrayList<>();

    /**
     * received the new data input
     */
    private static void notifyBlockData(byte[] data) {
        // ignore the verification data
        if (previousData != null && Arrays.equals(data, previousData)) {
            previousData = null;
            return;
        }
        previousData = data;

        executorService.submit(() -> {
            int insertBlockID = data[2] & 0xFF |
                    (data[1] & 0xFF) << 8 |
                    (data[0] & 0xFF) << 16;
            int insertFace = data[3] & 0xFF;
            int newBlockID = data[6] & 0xFF |
                    (data[5] & 0xFF) << 8 |
                    (data[4] & 0xFF) << 16;
            BlockData blockData = new BlockData(insertBlockID, insertFace, newBlockID);

            logger.info(blockData.toString());

            blockdataConsumers.forEach(it -> it.accept(blockData));
        });
    }

    public static void addBlockDataConsumer(Consumer<BlockData> consumer) {
        blockdataConsumers.add(consumer);
    }
}
