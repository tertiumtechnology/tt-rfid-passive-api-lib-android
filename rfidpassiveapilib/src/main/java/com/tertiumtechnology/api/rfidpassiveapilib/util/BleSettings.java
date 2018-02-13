package com.tertiumtechnology.api.rfidpassiveapilib.util;

import com.tertiumtechnology.txrxlib.rw.TxRxTimeouts;

public class BleSettings {

    private static BleSettings defaultBleSettings = new BleSettings();

    /**
     * Returns a {@link BleSettings}, configured with default settings
     *
     * @return a BleSettings with default settings
     */
    public static BleSettings getDefaultBleSettings() {
        return defaultBleSettings;
    }

    private final TxRxTimeouts txRxTimeouts;

    /**
     * Create a new {@link BleSettings} in order to manage settings.
     *
     * @param connectTimeout   long the timeout used during connection to a device
     * @param writeTimeout     long the timeout used in write operation
     * @param firstReadTimeout long the timeout used for the first read/notify operaation
     * @param laterReadTimeout long the timeout used for subsequent read/norify operaations
     */
    public BleSettings(long connectTimeout, long writeTimeout, long firstReadTimeout, long laterReadTimeout) {
        txRxTimeouts = new TxRxTimeouts(connectTimeout, writeTimeout, firstReadTimeout, laterReadTimeout);
    }

    /**
     * Create a new {@link BleSettings} configured with default settings.
     */
    public BleSettings() {
        txRxTimeouts = new TxRxTimeouts();
    }

    /**
     * Returns the connection timeout used during connection to a device
     *
     * @return a long representing the connection timeout
     */
    public long getConnectTimeout() {
        return txRxTimeouts.getConnectTimeout();
    }

    /**
     * Returns the timeout used during the first read/notify operation
     *
     * @return a long representing the first read/notify timeout
     */
    public long getFirstReadTimeout() {
        return txRxTimeouts.getFirstReadTimeout();
    }

    /**
     * Returns the timeout used during the subsequent read/notify operations, after the first read/notify operation
     *
     * @return a long representing the timeout for subsequent read/notify operations
     */
    public long getLaterReadTimeout() {
        return txRxTimeouts.getLaterReadTimeout();
    }

    /**
     * Returns the write timeout used in write operations
     *
     * @return a long representing the write timeout
     */
    public long getWriteTimeout() {
        return txRxTimeouts.getWriteTimeout();
    }
}