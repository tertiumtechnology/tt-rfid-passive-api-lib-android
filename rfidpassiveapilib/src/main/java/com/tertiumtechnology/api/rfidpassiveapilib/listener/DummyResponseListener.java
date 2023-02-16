package com.tertiumtechnology.api.rfidpassiveapilib.listener;

import android.bluetooth.BluetoothAdapter;

import com.tertiumtechnology.api.rfidpassiveapilib.PassiveReader;
import com.tertiumtechnology.api.rfidpassiveapilib.util.BleSettings;

/**
 * Dummy listener for tag operations.
 * <p>
 * This dummy listener is used if an instance of {@code ZhagaReader} is
 * created calling {@link PassiveReader#getZhagaReaderInstance(AbstractZhagaListener, BluetoothAdapter, BleSettings)} 
 * getZhagaReaderInstance} static method.
 */
public class DummyResponseListener extends AbstractResponseListener {

    @Override
    public void killEvent(byte[] tag_ID, int error) {
    }

    @Override
    public void lockEvent(byte[] tag_ID, int error) {
    }

    @Override
    public void readEvent(byte[] tag_ID, int error, byte data[]) {
    }

    @Override
    public void readTIDevent(byte[] tag_ID, int error, byte data[]) {
    }

    @Override
    public void writeEvent(byte[] tag_ID, int error) {
    }

    @Override
    public void writeIDevent(byte[] tag_ID, int error) {
    }

    @Override
    public void writePasswordEvent(byte[] tag_ID, int error) {
    }
}
