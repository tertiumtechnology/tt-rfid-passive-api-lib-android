package com.tertiumtechnology.api.rfidpassiveapilib.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

/**
 * Callback interface used to deliver BLE scan results.
 *
 * @see Scanner#Scanner(BluetoothAdapter, AbstractScanListener)
 */
public abstract class AbstractScanListener {

    /**
     * Callback when a BLE device has been found.
     *
     * @param device {@link BluetoothDevice} Identifies the remote device found.
     */
    public abstract void deviceFoundEvent(BluetoothDevice device);

    /**
     * Callback immediately after the scan has been stopped.
     */
    public abstract void stopScanEvent();
}
