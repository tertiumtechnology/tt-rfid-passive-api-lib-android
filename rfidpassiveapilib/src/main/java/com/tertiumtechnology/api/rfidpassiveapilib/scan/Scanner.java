package com.tertiumtechnology.api.rfidpassiveapilib.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.tertiumtechnology.txrxlib.scan.TxRxScanCallback;
import com.tertiumtechnology.txrxlib.scan.TxRxScanner;

/**
 * This class provides methods to perform scan for BLE devices.
 * Needs an implementation of {@link AbstractScanListener} to deliver scan results.
 * <p>
 * <b>Note:</b> Most of the scan methods here require
 * {@link android.Manifest.permission#BLUETOOTH_ADMIN} permission.
 *
 * @see AbstractScanListener
 */
public class Scanner {

    private AbstractScanListener scanListener;
    private TxRxScanner txRxScanner;

    /**
     * Create a new {@link Scanner} to perform scan for BLE devices.
     *
     * @param bluetoothAdapter {@link BluetoothAdapter} used to perform BLE task
     * @param scanListener     {@link AbstractScanListener} callback used to deliver scan results
     */
    public Scanner(BluetoothAdapter bluetoothAdapter, AbstractScanListener scanListener) {
        this.scanListener = scanListener;

        TxRxScanCallback txRxScanCallback = new TxRxScanCallback() {
            @Override
            public void afterStopScan() {
                Scanner.this.scanListener.stopScanEvent();
            }

            @Override
            public void onDeviceFound(BluetoothDevice device) {
                Scanner.this.scanListener.deviceFoundEvent(device);
            }
        };

        txRxScanner = new TxRxScanner(bluetoothAdapter, txRxScanCallback);
    }

    /**
     * Returns whether it is currently scanning for BLE devices.
     *
     * @return true if it is currently scanning for devices, false otherwise.
     */
    public boolean isScanning() {
        return txRxScanner.isScanning();
    }

    /**
     * Start BLE scan. The scan results will be delivered through
     * {@link AbstractScanListener#deviceFoundEvent(BluetoothDevice)}.
     * <p>
     * Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN} permission.
     * <p>
     * An app must hold
     * {@link android.Manifest.permission#ACCESS_COARSE_LOCATION ACCESS_COARSE_LOCATION} or
     * {@link android.Manifest.permission#ACCESS_FINE_LOCATION ACCESS_FINE_LOCATION} permission
     * in order to get results.
     */
    public void startScan() {
        txRxScanner.startScan();
    }

    /**
     * Stops an ongoing BLE scan.
     * <p>
     * Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN} permission.
     */
    public void stopScan() {
        txRxScanner.stopScan();
    }
}
