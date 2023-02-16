package com.tertiumtechnology.api.rfidpassiveapilib.listener;

import android.bluetooth.BluetoothAdapter;

import com.tertiumtechnology.api.rfidpassiveapilib.PassiveReader;
import com.tertiumtechnology.api.rfidpassiveapilib.util.BleSettings;

/**
 * Dummy listener for reader device.
 * <p>
 * This dummy listener is used if an instance of {@code ZhagaReader} is
 * created calling {@link PassiveReader#getZhagaReaderInstance(AbstractZhagaListener, BluetoothAdapter, BleSettings)} 
 * getZhagaReaderInstance} static method.
 */
public class DummyReaderListener extends AbstractReaderListener {

    @Override
    public void BLEfirmwareVersionEvent(int major, int minor) {
    }

    @Override
    public void BLEpowerEvent(int power) {
    }

    @Override
    public void EPCfrequencyEvent(int frequency) {
    }

    @Override
    public void ISO15693bitrateEvent(int bitrate, boolean permanent) {
    }

    @Override
    public void ISO15693extensionFlagEvent(boolean flag, boolean permanent) {
    }

    @Override
    public void ISO15693optionBitsEvent(int option_bits) {
    }

    @Override
    public void MACaddressEvent(byte MAC[]) {
    }

    @Override
    public void RFforISO15693tunnelEvent(int delay, int timeout) {
    }

    @Override
    public void RFpowerEvent(int level, int mode) {
    }

    @Override
    public void advertisingIntervalEvent(int interval) {
    }

    @Override
    public void availabilityEvent(boolean available) {
    }

    @Override
    public void batteryLevelEvent(float level) {
    }

    @Override
    public void batteryStatusEvent(int status) {
    }

    @Override
    public void connectionFailedEvent(int error) {
    }

    @Override
    public void connectionIntervalAndMTUevent(float interval, int MTU) {
    }

    @Override
    public void connectionIntervalEvent(float min, float max) {
    }

    @Override
    public void connectionSuccessEvent() {
    }

    @Override
    public void disconnectionSuccessEvent() {
    }

    @Override
    public void firmwareVersionEvent(int major, int minor) {
    }

    @Override
    public void nameEvent(String name) {
    }

    @Override
    public void resultEvent(int command, int error) {
    }

    @Override
    public void securityLevelEvent(int level) {
    }

    @Override
    public void shutdownTimeEvent(int time) {
    }

    @Override
    public void slaveLatencyEvent(int latency) {
    }

    @Override
    public void supervisionTimeoutEvent(int timeout) {
    }

    @Override
    public void tunnelEvent(byte data[]) {
    }

    @Override
    public void userMemoryEvent(byte data[]) {
    }
}
