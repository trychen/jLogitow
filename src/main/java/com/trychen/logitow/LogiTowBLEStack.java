package com.trychen.logitow;

import com.trychen.logitow.stack.BlockData;
import com.trychen.logitow.stack.BluetoothState;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public enum LogiTowBLEStack {
    /**
     * single instance of stack
     */
    INSTANCE;

    private static boolean available = false;

    public static boolean isAvailable() {
        return available;
    }

    static {
        // start loading library
        System.loadLibrary("logitow");
    }

    private boolean isScanning = false, isConnected = false;

    public static ExecutorService executorService;

    /**
     * init native lib's JavaVM & INSTANCE of LogiTowBLEStack
     */
    public void init() {
        try {
            setup();
            executorService = Executors.newSingleThreadExecutor();
            available = true;
        } catch (UnsatisfiedLinkError error) {
            // fount it unable to found usable native lib for current system
            System.err.println("Couldn't find a satisfied native logitow lib for current system");
            error.printStackTrace();
        }
    }

    private native void setup();

    /**
     * the device's unique uuid
     */
    @Deprecated
    private UUID connectedDeviceUUID;

    /**
     * start scanning logitow if scan haven't started.
     *
     * @return start scanning success
     */
    public boolean startScan() {
        if (isScanning) return true;
        isScanning = true;
        return startScanDevice();
    }

    /**
     * get the bluetooth state,
     *
     * @return the state code {@link }
     */
    public BluetoothState getBluetoothState() {
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
    public native int getNativeBluetoothState();

    /**
     * start scanning device
     */
    private native boolean startScanDevice();

    /**
     * stop scanning devices
     */
    private void stopScan() {
        if (isScanning) stopScanDevice();
    }

    /**
     * stop scanning logitow
     */
    private native void stopScanDevice();

    /**
     * disconnect device
     *
     * @param scanForOtherDevice restart scanning
     */
    public native void disconnect(boolean scanForOtherDevice);

    private final List<Runnable> disconnectedRunnables = new LinkedList<>();

    public void addDisconnectedRunnable(Runnable runnable) {
        disconnectedRunnables.add(runnable);
    }

    private void notifyDisconnected(boolean isScanning) {
        this.isScanning = isScanning;
        this.isConnected = false;

        for (Runnable runnable : disconnectedRunnables) {
            runnable.run();
        }
    }

    private List<Runnable> connectedRunnables = new ArrayList<>();

    public void addConnectedRunnable(Runnable runnable) {
        connectedRunnables.add(runnable);
    }

    /**
     * this method is for jni to notify changing the voltage for future
     */
    private void notifyConnected() {
        isConnected = true;
        isScanning = false;
        for (Runnable runnable : connectedRunnables) {
            runnable.run();
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    private byte[] previousData;
    private List<Consumer<BlockData>> blockdataConsumers = new ArrayList<>();

    /**
     * received the new data input
     */
    private void notifyBlockData(byte[] data) {
        // ignore the verification data
        if (previousData != null && Arrays.equals(data, previousData)) {
            previousData = null;
            return;
        }
        previousData = data;
        int insertBlockID = data[2] & 0xFF |
                (data[1] & 0xFF) << 8 |
                (data[0] & 0xFF) << 16;
        int insertFace = data[3] & 0xFF;
        int newBlockID = data[6] & 0xFF |
                (data[5] & 0xFF) << 8 |
                (data[4] & 0xFF) << 16;
        BlockData blockData = new BlockData(insertBlockID, insertFace, newBlockID);

        for (Consumer<BlockData> blockdataConsumer : blockdataConsumers) {
            blockdataConsumer.accept(blockData);
        }
//            blockdataConsumers.forEach(it -> it.accept(blockData));

        System.out.println(blockData);

//        executorService.submit(() -> {
//            int insertBlockID = data[2] & 0xFF |
//                    (data[1] & 0xFF) << 8 |
//                    (data[0] & 0xFF) << 16;
//            int insertFace = data[3] & 0xFF;
//            int newBlockID = data[6] & 0xFF |
//                    (data[5] & 0xFF) << 8 |
//                    (data[4] & 0xFF) << 16;
//            BlockData blockData = new BlockData(insertBlockID, insertFace, newBlockID);
//
//            for (Consumer<BlockData> blockdataConsumer : blockdataConsumers) {
//                blockdataConsumer.accept(blockData);
//            }
////            blockdataConsumers.forEach(it -> it.accept(blockData));
//
//            System.out.println(blockData);
//        });
    }

    public void addBlockDataConsumer(Consumer<BlockData> consumer) {
        blockdataConsumers.add(consumer);
    }
}
