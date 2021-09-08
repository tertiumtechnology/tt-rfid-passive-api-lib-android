package com.tertiumtechnology.api.rfidpassiveapilib.scan;

import android.bluetooth.BluetoothAdapter;
import android.os.Build;

import com.tertiumtechnology.txrxlib.scan.TxRxScanCallback;
import com.tertiumtechnology.txrxlib.scan.TxRxScanResult;
import com.tertiumtechnology.txrxlib.scan.TxRxScanner;

import java.util.Arrays;

/**
 * This class provides methods to perform scan for BLE devices.
 * Needs an implementation of {@link AbstractScanListener} to deliver scan results.
 * <p>
 * <b>Note:</b> Most of the scan methods here require
 * {@link android.Manifest.permission#BLUETOOTH_ADMIN} permission.
 *
 * @see AbstractScanListener
 */
public class PassiveScanner {

    private static String[] filteredServiceUuids = new String[]{
            "175f8f23-a570-49bd-9627-815a6a27de2a", // TxRxAckme
            "3cc30001-cb91-4947-bd12-80d2f0535a30" // Zhaga
    };

    private AbstractScanListener scanListener;
    private TxRxScanner txRxScanner;

    /**
     * Create a new {@link PassiveScanner} to perform scan for BLE devices.
     *
     * @param bluetoothAdapter {@link BluetoothAdapter} used to perform BLE task
     * @param scanListener     {@link AbstractScanListener} callback used to deliver scan results
     */
    public PassiveScanner(BluetoothAdapter bluetoothAdapter, AbstractScanListener scanListener) {
        this.scanListener = scanListener;

        TxRxScanCallback txRxScanCallback = new TxRxScanCallback() {
            @Override
            public void afterStopScan() {
                PassiveScanner.this.scanListener.stopScanEvent();
            }

            @Override
            public void onDeviceFound(TxRxScanResult scanResult) {
                PassiveScanner.this.scanListener.deviceFoundEvent(new BleDevice(scanResult.getBluetoothDevice()
                        .getName(),
                        scanResult.getBluetoothDevice().getAddress(), scanResult.getRssi()));
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
     * {@link AbstractScanListener#deviceFoundEvent(BleDevice)}.
     * <p>
     * Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN} permission.
     * <p>
     * An app must have {@link android.Manifest.permission#ACCESS_COARSE_LOCATION ACCESS_COARSE_LOCATION} permission
     * in order to get results.
     * An App targeting Android Q or later must have {@link android.Manifest.permission#ACCESS_FINE_LOCATION
     * ACCESS_FINE_LOCATION} permission in order to get results.
     */
    public void startScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            txRxScanner.startScan(Arrays.asList(filteredServiceUuids));
        }
        else {
            txRxScanner.startScan();
        }
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
