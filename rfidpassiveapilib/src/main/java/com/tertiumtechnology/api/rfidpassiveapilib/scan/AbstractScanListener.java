package com.tertiumtechnology.api.rfidpassiveapilib.scan;

import android.bluetooth.BluetoothAdapter;

/**
 * Callback interface used to deliver BLE scan results.
 *
 * @see PassiveScanner#PassiveScanner(BluetoothAdapter, AbstractScanListener)
 */
public abstract class AbstractScanListener {

    /**
     * Callback when a BLE device has been found.
     *
     * @param device {@link BleDevice} Identifies the remote device found.
     */
    public abstract void deviceFoundEvent(BleDevice device);

    /**
     * Callback immediately after the scan has been stopped.
     */
    public abstract void stopScanEvent();
}
