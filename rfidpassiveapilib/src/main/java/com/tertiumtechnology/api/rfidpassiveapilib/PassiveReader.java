/*
 * The MIT License
 *
 * Copyright 2020 Tertium Technology.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.tertiumtechnology.api.rfidpassiveapilib;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.tertiumtechnology.api.rfidpassiveapilib.listener.AbstractInventoryListener;
import com.tertiumtechnology.api.rfidpassiveapilib.listener.AbstractReaderListener;
import com.tertiumtechnology.api.rfidpassiveapilib.listener.AbstractResponseListener;
import com.tertiumtechnology.api.rfidpassiveapilib.util.BleSettings;
import com.tertiumtechnology.txrxlib.rw.TxRxDeviceCallback;
import com.tertiumtechnology.txrxlib.rw.TxRxDeviceManager;
import com.tertiumtechnology.txrxlib.rw.TxRxTimeouts;

import static com.tertiumtechnology.txrxlib.rw.TxRxDeviceManager.ERROR_CONNECT_DEVICE_NOT_FOUND;
import static com.tertiumtechnology.txrxlib.rw.TxRxDeviceManager.ERROR_CONNECT_INVALID_BLUETOOTH_ADAPTER;
import static com.tertiumtechnology.txrxlib.rw.TxRxDeviceManager.ERROR_CONNECT_INVALID_DEVICE_ADDRESS;
import static com.tertiumtechnology.txrxlib.rw.TxRxDeviceManager.ERROR_DISCONNECT_BLE_NOT_INITIALIZED;
import static com.tertiumtechnology.txrxlib.rw.TxRxDeviceManager.ERROR_DISCONNECT_INVALID_BLUETOOTH_ADAPTER;
import static com.tertiumtechnology.txrxlib.rw.TxRxDeviceManager.ERROR_READ_BLE_DEVICE_ERROR;
import static com.tertiumtechnology.txrxlib.rw.TxRxDeviceManager.ERROR_READ_INVALID_TX_CHARACTERISTIC;
import static com.tertiumtechnology.txrxlib.rw.TxRxDeviceManager.ERROR_WRITE_BLE_DEVICE_ERROR;
import static com.tertiumtechnology.txrxlib.rw.TxRxDeviceManager.ERROR_WRITE_INVALID_RX_CHARACTERISTIC;
import static com.tertiumtechnology.txrxlib.rw.TxRxDeviceManager.ERROR_WRITE_OPERATION_IN_PROGRESS;

/**
 * Represents the RFID/NFC tag reader.
 */
public final class PassiveReader {
    private class DeviceCallback implements TxRxDeviceCallback {
        private class ReaderAnswer {
            private boolean valid;
            private int length;
            private int sequential;
            private int return_code;
            private byte[] data;

            protected ReaderAnswer(String answer) {
                valid = false;
                if (answer.length() >= 6) {
                    length = hexToByte(answer.substring(2, 4));
                    if (length == answer.length() - 2) {
                        sequential = hexToByte(answer.substring(4, 6));
                        return_code = hexToByte(answer.substring(6, 8));
                        data = new byte[(length - 6) / 2];
                        for (int n = 0; n < data.length; n++) {
                            data[n] = (byte) hexToByte(answer.substring(8 + 2 * n, 8 + 2 * n + 2));
                        }
                        valid = true;
                    }
                }
            }

            protected byte[] getData() {
                if (valid) {
                    return data;
                }
                else {
                    return null;
                }
            }

            protected int getLength() {
                if (valid) {
                    return length;
                }
                else {
                    return 0;
                }
            }

            protected int getReturnCode() {
                if (valid) {
                    return return_code;
                }
                else {
                    return 0xFF;
                }
            }

            protected int getSequential() {
                if (valid) {
                    return sequential;
                }
                else {
                    return 0;
                }
            }
        }

        private PassiveReader passive_reader;

        protected DeviceCallback(PassiveReader passive_reader) {
            this.passive_reader = passive_reader;
            status = NOT_INITIALIZED_STATUS;
            sub_status = STREAM_SUBSTATUS;
        }

        @Override
        public void onConnectionError(int errorCode) {
            System.err.println("Connection error!");
            status = ERROR_STATUS;
            switch (errorCode) {
                case ERROR_CONNECT_DEVICE_NOT_FOUND:
                    reader_listener.connectionFailedEvent(AbstractReaderListener.READER_CONNECT_DEVICE_NOT_FOUND_ERROR);
                    break;
                case ERROR_CONNECT_INVALID_BLUETOOTH_ADAPTER:
                    reader_listener.connectionFailedEvent(AbstractReaderListener
                            .READER_CONNECT_INVALID_BLUETOOTH_ADAPTER_ERROR);
                    break;
                case ERROR_CONNECT_INVALID_DEVICE_ADDRESS:
                    reader_listener.connectionFailedEvent(AbstractReaderListener
                            .READER_CONNECT_INVALID_DEVICE_ADDRESS_ERROR);
                    break;
                case ERROR_DISCONNECT_BLE_NOT_INITIALIZED:
                    reader_listener.connectionFailedEvent(AbstractReaderListener
                            .READER_DISCONNECT_BLE_NOT_INITIALIZED_ERROR);
                    break;
                case ERROR_DISCONNECT_INVALID_BLUETOOTH_ADAPTER:
                    reader_listener.connectionFailedEvent(AbstractReaderListener
                            .READER_DISCONNECT_INVALID_BLUETOOTH_ADAPTER_ERROR);
                    break;
                default:
                    reader_listener.connectionFailedEvent(AbstractReaderListener.READER_CONNECT_GENERIC_ERROR);
                    break;
            }
        }

        @Override
        public void onConnectionTimeout() {
            System.err.println("Connection timeout!");
            status = ERROR_STATUS;
            reader_listener.connectionFailedEvent(AbstractReaderListener.READER_CONNECT_TIMEOUT_ERROR);
        }

        @Override
        public void onDeviceConnected() {
            System.err.println("Connected.");
        }

        @Override
        public void onDeviceDisconnected() {
            System.err.println("Disconnected.");
            reader_listener.disconnectionSuccessEvent();
            status = NOT_INITIALIZED_STATUS;
            sub_status = STREAM_SUBSTATUS;
        }

        @Override
        public void onNotifyData(String data) {
            ReaderAnswer answer = null;
            byte tunnel_answer[] = null;
            Tag tag = null;

            System.err.println("\"" + data + "\" received.");
            if (sub_status == CMD_SUBSTATUS) {
                if (data == null || data.isEmpty()) {
                    reader_listener.resultEvent(pending,
                            AbstractReaderListener.READER_DRIVER_COMMAND_CMD_MODE_ANSWER_ERROR);
                }
                else {
                    if (data.charAt(0) != '>') {
                        switch (pending) {
                            case AbstractReaderListener.GET_SECURITY_LEVEL_COMMAND:
                                if (data.charAt(0) == '0') {
                                    reader_listener.securityLevelEvent(BLE_NO_SECURITY);
                                }
                                else // data.charAt(0) == '1'
                                {
                                    reader_listener.securityLevelEvent(BLE_LEGACY_LEVEL_2_SECURITY);
                                }
                                break;
                        }
                    }
                    sub_status = SET_STREAM_SUBSTATUS;
                    device_manager.requestSetMode(STREAM_MODE);
                }
                return;
            }
            if (data == null || data.isEmpty()) {
                status = READY_STATUS;
                return;
            }

            String[] dataChunks = data.split("\r\n");

            for (String chunk : dataChunks) {
                if (data.startsWith("> ")) {
                    return;
                }
                if (chunk.charAt(0) == '$') {
                    // command answer
                    answer = new ReaderAnswer(chunk);
                }
                else {
                    if ((chunk.charAt(0) == '#') || (chunk.charAt(0) == '%')) {
                        // tunnel command answer
                        tunnel_answer = new byte[(chunk.length() - 2) / 2];
                        for (int n = 0; n < tunnel_answer.length; n++) {
                            tunnel_answer[n] = (byte) hexToByte(chunk.substring(2 + 2 * n, 2 + 2 * n + 2));
                        }
                    }
                    else {
                        // check for valid ID chars
                        for (int n = 0; n < chunk.length(); n++)
                            if (Character.digit(chunk.charAt(n), 16) < 0) {
                                return;
                            }
                        // tag info
                        if (HF_device) {
                            byte[] ID = new byte[chunk.length() / 2];

                            for (int n = 0; n < ID.length; n++) {
                                ID[n] = (byte) hexToByte(chunk.substring(2 * n, 2 * n + 2));
                            }

                            if (ID.length == 8) // ?
                            {
                                tag = new ISO15693_tag(ID, passive_reader);
                            }
                            else {
                                tag = new ISO14443A_tag(ID, passive_reader);
                            }
                            inventory_listener.inventoryEvent(tag);
                        }
                        if (UHF_device) {
                            int separator_index = chunk.indexOf(" ");

                            if (separator_index < 0) {
                                if (chunk.length() > 4) {
                                    short PC = (short) hexToWord(chunk.substring(0, 4));
                                    byte[] ID = new byte[(chunk.length() - 4) / 2];

                                    for (int n = 0; n < ID.length; n++) {
                                        ID[n] = (byte) hexToByte(chunk.substring(4 + 2 * n, 4 + 2 * n + 2));
                                    }

                                    tag = new EPC_tag(PC, ID, passive_reader);
                                    inventory_listener.inventoryEvent(tag);
                                }
                            }
                            else {
                                if (chunk.length() > 7) {
                                    short PC = (short) hexToWord(chunk.substring(0, 4));
                                    byte[] ID = new byte[(chunk.length() - 7) / 2];

                                    for (int n = 0; n < ID.length; n++) {
                                        ID[n] = (byte) hexToByte(chunk.substring(4 + 2 * n, 4 + 2 * n + 2));
                                    }

                                    String rssi = chunk.substring(separator_index + 1, separator_index + 1 + 2);

                                    int tmp = hexToWord(rssi);

                                    short RSSI;

                                    if (tmp < 127) {
                                        RSSI = (short) tmp;
                                    }
                                    else {
                                        RSSI = (short) (tmp - 256);
                                    }

                                    tag = new EPC_tag(RSSI, PC, ID, passive_reader);

                                    inventory_listener.inventoryEvent(tag);
                                }
                            }
                        }
                    }
                }
            }

            if (answer == null && tunnel_answer == null) {
                return;
            }

            switch (status) {
                case ERROR_STATUS:
                case NOT_INITIALIZED_STATUS:
                    status = ERROR_STATUS;
                    break;
                case UNINITIALIZED_STATUS:
                    if (answer.getSequential() != sequential - 1) {
                        status = ERROR_STATUS;
                        reader_listener.connectionFailedEvent(AbstractReaderListener
                                .READER_COMMAND_ANSWER_MISMATCH_ERROR);
                        break;
                    }
                    if (answer.getData().length == 0) {
                        status = ERROR_STATUS;
                        reader_listener.connectionFailedEvent(AbstractReaderListener.INVALID_PARAMETER_ERROR);
                        break;
                    }
                    if (answer.getReturnCode() != SUCCESSFUL_OPERATION_RETCODE) {
                        status = ERROR_STATUS;
                        reader_listener.connectionFailedEvent(answer.getReturnCode());
                        break;
                    }
                    if (answer.getData()[0] == EPC_STANDARD) {
                        HF_device = false;
                        UHF_device = true;
                        inventory_standard = answer.getData()[0];
                    }
                    else {
                        HF_device = true;
                        UHF_device = false;
                        inventory_standard = answer.getData()[0];
                    }
                    status = READY_STATUS;
                    reader_listener.connectionSuccessEvent();
                    break;
                case READY_STATUS:
                    break;
                case PENDING_COMMAND_STATUS:
                    if (answer != null && answer.getSequential() == sequential - 1) {
                        if (answer.getReturnCode() != SUCCESSFUL_OPERATION_RETCODE) {
                            status = READY_STATUS;
                            if (pending >= AbstractReaderListener.SOUND_COMMAND &&
                                    pending <= AbstractReaderListener.GET_SECURITY_LEVEL_COMMAND) {
                                reader_listener.resultEvent(pending, answer.getReturnCode());
                            }
                            else {
                                switch (pending) {
                                    case AbstractResponseListener.READ_COMMAND:
                                        response_listener.readEvent(tag_id, answer.getReturnCode(), null);
                                        break;
                                    case AbstractResponseListener.WRITE_COMMAND:
                                        response_listener.writeEvent(tag_id, answer.getReturnCode());
                                        break;
                                    case AbstractResponseListener.LOCK_COMMAND:
                                        response_listener.lockEvent(tag_id, answer.getReturnCode());
                                        break;
                                    case AbstractResponseListener.WRITEID_COMMAND:
                                        response_listener.writeIDevent(tag_id, answer.getReturnCode());
                                        break;
                                    case AbstractResponseListener.READ_TID_COMMAND:
                                        response_listener.readTIDevent(tag_id, answer.getReturnCode(), null);
                                        break;
                                    case AbstractResponseListener.KILL_COMMAND:
                                        response_listener.killEvent(tag_id, answer.getReturnCode());
                                        break;
                                    case AbstractResponseListener.WRITEKILLPASSWORD_COMMAND:
                                    case AbstractResponseListener.WRITEACCESSPASSWORD_COMMAND:
                                        response_listener.writePasswordEvent(tag_id, answer.getReturnCode());
                                        break;
                                }
                            }
                            break;
                        }
                        switch (pending) {
                            case AbstractReaderListener.SOUND_COMMAND:
                            case AbstractReaderListener.LIGHT_COMMAND:
                            case AbstractReaderListener.SET_SHUTDOWN_TIME_COMMAND:
                            case AbstractReaderListener.SET_RF_POWER_COMMAND:
                            case AbstractReaderListener.SET_RF_FOR_ISO15693_TUNNEL_COMMAND:
                            case AbstractReaderListener.SET_ISO15693_OPTION_BITS_COMMAND:
                            case AbstractReaderListener.SET_ISO15693_EXTENSION_FLAG_COMMAND:
                            case AbstractReaderListener.SET_ISO15693_BITRATE_COMMAND:
                            case AbstractReaderListener.SET_EPC_FREQUENCY_COMMAND:
                            case AbstractReaderListener.SET_SECURITY_LEVEL_COMMAND:
                                reader_listener.resultEvent(pending, answer.getReturnCode());
                                break;
                            case AbstractReaderListener.SET_INVENTORY_MODE_COMMAND:
                                if (answer.getReturnCode() == AbstractReaderListener.NO_ERROR) {
                                    inventory_mode = mode;
                                }
                                reader_listener.resultEvent(pending, answer.getReturnCode());
                                break;
                            case AbstractReaderListener.SET_INVENTORY_TYPE_COMMAND:
                                if (answer.getReturnCode() == AbstractReaderListener.NO_ERROR) {
                                    inventory_standard = standard;
                                }
                                reader_listener.resultEvent(pending, answer.getReturnCode());
                                break;
                            case AbstractReaderListener.SET_INVENTORY_PARAMETERS_COMMAND:
                                if (answer.getReturnCode() == AbstractReaderListener.NO_ERROR) {
                                    inventory_mode = mode;
                                    inventory_feedback = feedback;
                                    inventory_format = format;
                                    inventory_max_number = max_number;
                                    inventory_interval = interval;
                                    inventory_timeout = timeout;
                                    inventory_enabled = true;
                                }
                                reader_listener.resultEvent(pending, answer.getReturnCode());
                                break;
                            case AbstractReaderListener.TEST_AVAILABILITY_COMMAND:
                                if (answer.getReturnCode() == AbstractReaderListener.NO_ERROR) {
                                    reader_listener.availabilityEvent(true);
                                }
                                reader_listener.resultEvent(pending, answer.getReturnCode());
                                break;
                            case AbstractReaderListener.GET_BATTERY_STATUS_COMMAND:
                                if (answer.getReturnCode() == AbstractReaderListener.NO_ERROR &&
                                        answer.getData().length > 0) {
                                    int status = byteToInt(answer.getData()[0]);
                                    reader_listener.batteryStatusEvent(status);
                                }
                                reader_listener.resultEvent(pending, answer.getReturnCode());
                                break;
                            case AbstractReaderListener.GET_FIRMWARE_VERSION_COMMAND:
                                if (answer.getReturnCode() == AbstractReaderListener.NO_ERROR &&
                                        answer.getData().length > 0) {
                                    int major = byteToInt(answer.getData()[0]) / 16;
                                    int minor = byteToInt(answer.getData()[0]) % 16;
                                    reader_listener.firmwareVersionEvent(major, minor);
                                }
                                reader_listener.resultEvent(pending, answer.getReturnCode());
                                break;
                            case AbstractReaderListener.GET_SHUTDOWN_TIME_COMMAND:
                                if (answer.getReturnCode() == AbstractReaderListener.NO_ERROR &&
                                        answer.getData().length > 1) {
                                    int time = byteToInt(answer.getData()[0]) * 256;
                                    time += byteToInt(answer.getData()[1]);
                                    reader_listener.shutdownTimeEvent(time);
                                }
                                reader_listener.resultEvent(pending, answer.getReturnCode());
                                break;
                            case AbstractReaderListener.GET_RF_POWER_COMMAND:
                                if (answer.getReturnCode() == AbstractReaderListener.NO_ERROR &&
                                        answer.getData().length > 1) {
                                    int level = byteToInt(answer.getData()[0]);
                                    int mode = byteToInt(answer.getData()[1]);
                                    reader_listener.RFpowerEvent(level, mode);
                                }
                                reader_listener.resultEvent(pending, answer.getReturnCode());
                                break;
                            case AbstractReaderListener.GET_BATTERY_LEVEL_COMMAND:
                                if (answer.getReturnCode() == AbstractReaderListener.NO_ERROR &&
                                        answer.getData().length > 1) {
                                    double level = byteToInt(answer.getData()[0]) * 256;
                                    level += byteToInt(answer.getData()[1]);
                                    level = level * (3.3 / 4095) * 2.025; // ADC -> Volt
                                    reader_listener.batteryLevelEvent((float) (level));
                                }
                                reader_listener.resultEvent(pending, answer.getReturnCode());
                                break;
                            case AbstractReaderListener.GET_RF_FOR_ISO15693_TUNNEL_COMMAND:
                                if (answer.getReturnCode() == AbstractReaderListener.NO_ERROR &&
                                        answer.getData().length > 1) {
                                    int timeout = byteToInt(answer.getData()[0]);
                                    int delay = byteToInt(answer.getData()[1]);
                                    reader_listener.RFforISO15693tunnelEvent(delay, timeout);
                                }
                                reader_listener.resultEvent(pending, answer.getReturnCode());
                                break;
                            case AbstractReaderListener.GET_ISO15693_OPTION_BITS_COMMAND:
                                if (answer.getReturnCode() == AbstractReaderListener.NO_ERROR &&
                                        answer.getData().length > 0) {
                                    int bits = byteToInt(answer.getData()[0]);
                                    reader_listener.ISO15693optionBitsEvent(bits);
                                }
                                reader_listener.resultEvent(pending, answer.getReturnCode());
                                break;
                            case AbstractReaderListener.GET_ISO15693_EXTENSION_FLAG_COMMAND:
                                if (answer.getReturnCode() == AbstractReaderListener.NO_ERROR &&
                                        answer.getData().length > 0) {
                                    boolean permanent = (answer.getData()[0] & 0x02) != 0x02;
                                    boolean flag = (answer.getData()[0] & 0x01) == 0x01;
                                    reader_listener.ISO15693extensionFlagEvent(flag, permanent);
                                }
                                reader_listener.resultEvent(pending, answer.getReturnCode());
                                break;
                            case AbstractReaderListener.GET_ISO15693_BITRATE_COMMAND:
                                if (answer.getReturnCode() == AbstractReaderListener.NO_ERROR &&
                                        answer.getData().length > 0) {
                                    boolean permanent = (answer.getData()[0] & 0x02) != 0x02;

                                    int bitrate;
                                    if ((answer.getData()[0] & 0x01) == 0x01) {
                                        bitrate = PassiveReader.ISO15693_HIGH_BITRATE;
                                    }
                                    else {
                                        bitrate = PassiveReader.ISO15693_LOW_BITRATE;
                                    }
                                    reader_listener.ISO15693bitrateEvent(bitrate, permanent);
                                }
                                reader_listener.resultEvent(pending, answer.getReturnCode());
                                break;
                            case AbstractReaderListener.GET_EPC_FREQUENCY_COMMAND:
                                if (answer.getReturnCode() == AbstractReaderListener.NO_ERROR &&
                                        answer.getData().length > 0) {
                                    int frequency = byteToInt(answer.getData()[0]);
                                    reader_listener.EPCfrequencyEvent(frequency);
                                }
                                reader_listener.resultEvent(pending, answer.getReturnCode());
                                break;
                            case AbstractReaderListener.GET_SECURITY_LEVEL_COMMAND:
                                if (answer.getReturnCode() == AbstractReaderListener.NO_ERROR &&
                                        answer.getData().length > 0) {
                                    int level = byteToInt(answer.getData()[0]);
                                    reader_listener.securityLevelEvent(level);
                                }
                                reader_listener.resultEvent(pending, answer.getReturnCode());
                                break;
                            case AbstractResponseListener.READ_COMMAND:
                                response_listener.readEvent(tag_id, answer.getReturnCode(), answer.getData());
                                break;
                            case AbstractResponseListener.WRITE_COMMAND:
                                response_listener.writeEvent(tag_id, answer.getReturnCode());
                                break;
                            case AbstractResponseListener.LOCK_COMMAND:
                                response_listener.lockEvent(tag_id, answer.getReturnCode());
                                break;
                            case AbstractResponseListener.WRITEID_COMMAND:
                                response_listener.writeIDevent(tag_id, answer.getReturnCode());
                                break;
                            case AbstractResponseListener.READ_TID_COMMAND:
                                response_listener.readTIDevent(tag_id, answer.getReturnCode(), answer.getData());
                                break;
                            case AbstractResponseListener.KILL_COMMAND:
                                response_listener.killEvent(tag_id, answer.getReturnCode());
                                break;
                            case AbstractResponseListener.WRITEKILLPASSWORD_COMMAND:
                            case AbstractResponseListener.WRITEACCESSPASSWORD_COMMAND:
                                response_listener.writePasswordEvent(tag_id, answer.getReturnCode());
                                break;
                        }
                    }
                    else {
                        // tunnel operation answer
                        if (tunnel_answer != null &&
                                (pending == AbstractReaderListener.ISO15693_ENCRYPTEDTUNNEL_COMMAND ||
                                        pending == AbstractReaderListener.ISO15693_TUNNEL_COMMAND)) {
                            reader_listener.tunnelEvent(tunnel_answer);
                        }
                        else {
                            // answer mismatch
                            reader_listener.resultEvent(pending,
                                    AbstractReaderListener.READER_COMMAND_ANSWER_MISMATCH_ERROR);
                        }
                    }
                    status = READY_STATUS;
                    break;
            }
        }

        @Override
        public void onReadData(String data) {
            System.err.println("\"" + data + "\" read?");
        }

        @Override
        public void onReadError(int errorCode) {
            int error;

            switch (errorCode) {
                case ERROR_READ_BLE_DEVICE_ERROR:
                    error = AbstractReaderListener.READER_READ_BLE_DEVICE_ERROR;
                    break;
                case ERROR_READ_INVALID_TX_CHARACTERISTIC:
                    error = AbstractReaderListener.READER_READ_INVALID_TX_CHARACTERISTIC_ERROR;
                    break;
                default:
                    error = AbstractReaderListener.READER_READ_FAIL_ERROR;
                    break;
            }
            System.err.println("Read error!");
            switch (status) {
                case ERROR_STATUS:
                case NOT_INITIALIZED_STATUS:
                case UNINITIALIZED_STATUS:
                    status = ERROR_STATUS;
                    reader_listener.connectionFailedEvent(error);
                    break;
                case READY_STATUS:
                    break;
                case PENDING_COMMAND_STATUS:
                    if (sub_status == CMD_SUBSTATUS) {
                        sub_status = SET_STREAM_SUBSTATUS;
                        device_manager.requestSetMode(STREAM_MODE);
                        break;
                    }
                    if (pending >= AbstractReaderListener.SOUND_COMMAND &&
                            pending <= AbstractReaderListener.GET_SECURITY_LEVEL_COMMAND) {
                        reader_listener.resultEvent(pending, error);
                    }
                    else {
                        switch (pending) {
                            case AbstractResponseListener.READ_COMMAND:
                                response_listener.readEvent(tag_id, error, null);
                                break;
                            case AbstractResponseListener.WRITE_COMMAND:
                                response_listener.writeEvent(tag_id, error);
                                break;
                            case AbstractResponseListener.LOCK_COMMAND:
                                response_listener.lockEvent(tag_id, error);
                                break;
                            case AbstractResponseListener.WRITEID_COMMAND:
                                response_listener.writeIDevent(tag_id, error);
                                break;
                            case AbstractResponseListener.READ_TID_COMMAND:
                                response_listener.readTIDevent(tag_id, error, null);
                                break;
                            case AbstractResponseListener.KILL_COMMAND:
                                response_listener.killEvent(tag_id, error);
                                break;
                            case AbstractResponseListener.WRITEKILLPASSWORD_COMMAND:
                            case AbstractResponseListener.WRITEACCESSPASSWORD_COMMAND:
                                response_listener.writePasswordEvent(tag_id, error);
                                break;
                        }
                    }
                    status = READY_STATUS;
                    break;
            }
        }

        @Override
        public void onReadNotifyTimeout() {
            System.err.println("Read timeout!");
            switch (status) {
                case ERROR_STATUS:
                case NOT_INITIALIZED_STATUS:
                case UNINITIALIZED_STATUS:
                    status = ERROR_STATUS;
                    reader_listener.connectionFailedEvent(AbstractResponseListener.READER_READ_TIMEOUT_ERROR);
                    break;
                case READY_STATUS:
                    break;
                case PENDING_COMMAND_STATUS:
                    if (sub_status == CMD_SUBSTATUS) {
                        sub_status = SET_STREAM_SUBSTATUS;
                        device_manager.requestSetMode(STREAM_MODE);
                        break;
                    }
                    if (pending >= AbstractReaderListener.SOUND_COMMAND &&
                            pending <= AbstractReaderListener.GET_SECURITY_LEVEL_COMMAND) {
                        reader_listener.resultEvent(pending, AbstractReaderListener.READER_READ_TIMEOUT_ERROR);
                    }
                    else {
                        switch (pending) {
                            case AbstractResponseListener.READ_COMMAND:
                                response_listener.readEvent(tag_id, AbstractResponseListener
                                        .READER_READ_TIMEOUT_ERROR, null);
                                break;
                            case AbstractResponseListener.WRITE_COMMAND:
                                response_listener.writeEvent(tag_id, AbstractResponseListener
                                        .READER_READ_TIMEOUT_ERROR);
                                break;
                            case AbstractResponseListener.LOCK_COMMAND:
                                response_listener.lockEvent(tag_id, AbstractResponseListener.READER_READ_TIMEOUT_ERROR);
                                break;
                            case AbstractResponseListener.WRITEID_COMMAND:
                                response_listener.writeIDevent(tag_id, AbstractResponseListener
                                        .READER_READ_TIMEOUT_ERROR);
                                break;
                            case AbstractResponseListener.READ_TID_COMMAND:
                                response_listener.readTIDevent(tag_id, AbstractResponseListener
                                                .READER_READ_TIMEOUT_ERROR,
                                        null);
                                break;
                            case AbstractResponseListener.KILL_COMMAND:
                                response_listener.killEvent(tag_id, AbstractResponseListener.READER_READ_TIMEOUT_ERROR);
                                break;
                            case AbstractResponseListener.WRITEKILLPASSWORD_COMMAND:
                            case AbstractResponseListener.WRITEACCESSPASSWORD_COMMAND:
                                response_listener.writePasswordEvent(tag_id, AbstractResponseListener
                                        .READER_WRITE_TIMEOUT_ERROR);
                                break;
                        }
                    }
                    status = READY_STATUS;
                    break;
            }
        }

        @Override
        public void onSetMode(int mode) {
            System.err.println("\"" + mode + "\" set.");
            switch (status) {
                case ERROR_STATUS:
                case NOT_INITIALIZED_STATUS:
                case UNINITIALIZED_STATUS:
                    status = ERROR_STATUS;
                    break;
                case READY_STATUS:
                    if (mode == STREAM_MODE) {
                        sub_status = STREAM_SUBSTATUS;
                    }
                    else {
                        status = ERROR_STATUS;
                    }
                    break;
                case PENDING_COMMAND_STATUS:
                    switch (sub_status) {
                        case STREAM_SUBSTATUS:
                        case CMD_SUBSTATUS:
                            status = ERROR_STATUS;
                            break;
                        case SET_CMD_SUBSTATUS:
                            if (mode == CMD_MODE) {
                                sub_status = CMD_SUBSTATUS;
                                device_manager.requestWriteData(command + "\r\n");
                            }
                            else {
                                status = ERROR_STATUS;
                            }
                            break;
                        case SET_STREAM_SUBSTATUS:
                            if (mode == STREAM_MODE) {
                                sub_status = STREAM_SUBSTATUS;
                                status = READY_STATUS;
                                reader_listener.resultEvent(pending, AbstractReaderListener.NO_ERROR);
                            }
                            else {
                                status = ERROR_STATUS;
                            }
                            break;
                    }
                    break;
            }
        }

        @Override
        public void onSetModeError(int errorCode) {
            System.err.println("Setmode error!");
            switch (status) {
                case ERROR_STATUS:
                case NOT_INITIALIZED_STATUS:
                case UNINITIALIZED_STATUS:
                case READY_STATUS:
                    status = ERROR_STATUS;
                    break;
                case PENDING_COMMAND_STATUS:
                    switch (sub_status) {
                        case STREAM_SUBSTATUS:
                        case CMD_SUBSTATUS:
                        case SET_STREAM_SUBSTATUS:
                            status = ERROR_STATUS;
                            break;
                        case SET_CMD_SUBSTATUS:
                            status = READY_STATUS;
                            sub_status = STREAM_SUBSTATUS;
                            reader_listener.resultEvent(pending,
                             AbstractReaderListener.READER_DRIVER_COMMAND_CHANGE_MODE_ERROR);
                            break;
                    }
                    break;
            }
        }

        @Override
        public void onSetModeTimeout() {
            System.err.println("Setmode timeout!");
            switch (status) {
                case ERROR_STATUS:
                case NOT_INITIALIZED_STATUS:
                case UNINITIALIZED_STATUS:
                case READY_STATUS:
                    status = ERROR_STATUS;
                    break;
                case PENDING_COMMAND_STATUS:
                    switch (sub_status) {
                        case STREAM_SUBSTATUS:
                        case CMD_SUBSTATUS:
                        case SET_STREAM_SUBSTATUS:
                            status = ERROR_STATUS;
                            break;
                        case SET_CMD_SUBSTATUS:
                            status = READY_STATUS;
                            sub_status = STREAM_SUBSTATUS;
                            reader_listener.resultEvent(pending,
                             AbstractReaderListener.READER_DRIVER_COMMAND_CHANGE_MODE_ERROR);
                            break;
                    }
                    break;
            }
        }

        @Override
        public void onTxRxServiceDiscovered() {
            System.err.println("TxRx service discovered.");
            status = UNINITIALIZED_STATUS;
            sub_status = STREAM_SUBSTATUS;

            new Handler(Looper.getMainLooper()).postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            device_manager.requestWriteData(buildCommand(SETSTANDARD_COMMAND));
                        }
                    }, 500);
        }

        @Override
        public void onTxRxServiceNotFound() {
            System.err.println("TxRx service not found!");
            status = ERROR_STATUS;
            reader_listener.connectionFailedEvent(AbstractReaderListener.READER_CONNECT_UNKNOW_SERVICE_ERROR);
        }

        @Override
        public void onWriteData(String data) {
            System.err.println("\"" + data + "\" sent.");
        }

        @Override
        public void onWriteError(int errorCode) {
            int error;

            switch (errorCode) {
                case ERROR_WRITE_BLE_DEVICE_ERROR:
                    error = AbstractReaderListener.READER_WRITE_BLE_DEVICE_ERROR;
                    break;
                case ERROR_WRITE_INVALID_RX_CHARACTERISTIC:
                    error = AbstractReaderListener.READER_WRITE_INVALID_RX_CHARACTERISTIC_ERROR;
                    break;
                case ERROR_WRITE_OPERATION_IN_PROGRESS:
                    error = AbstractReaderListener.READER_WRITE_OPERATION_IN_PROGRESS_ERROR;
                    break;
                default:
                    error = AbstractReaderListener.READER_WRITE_FAIL_ERROR;
                    break;
            }
            System.err.println("Write error!");
            switch (status) {
                case ERROR_STATUS:
                case NOT_INITIALIZED_STATUS:
                case UNINITIALIZED_STATUS:
                    status = ERROR_STATUS;
                    reader_listener.connectionFailedEvent(error);
                    break;
                case READY_STATUS:
                    break;
                case PENDING_COMMAND_STATUS:
                    if (sub_status == CMD_SUBSTATUS) {
                        sub_status = SET_STREAM_SUBSTATUS;
                        device_manager.requestSetMode(STREAM_MODE);
                        break;
                    }
                    if (pending >= AbstractReaderListener.SOUND_COMMAND &&
                            pending <= AbstractReaderListener.GET_SECURITY_LEVEL_COMMAND) {
                        reader_listener.resultEvent(pending, error);
                    }
                    else {
                        switch (pending) {
                            case AbstractResponseListener.READ_COMMAND:
                                response_listener.readEvent(tag_id, error, null);
                                break;
                            case AbstractResponseListener.WRITE_COMMAND:
                                response_listener.writeEvent(tag_id, error);
                                break;
                            case AbstractResponseListener.LOCK_COMMAND:
                                response_listener.lockEvent(tag_id, error);
                                break;
                            case AbstractResponseListener.WRITEID_COMMAND:
                                response_listener.writeIDevent(tag_id, error);
                                break;
                            case AbstractResponseListener.READ_TID_COMMAND:
                                response_listener.readTIDevent(tag_id, error, null);
                                break;
                            case AbstractResponseListener.KILL_COMMAND:
                                response_listener.killEvent(tag_id, error);
                                break;
                            case AbstractResponseListener.WRITEKILLPASSWORD_COMMAND:
                            case AbstractResponseListener.WRITEACCESSPASSWORD_COMMAND:
                                response_listener.writePasswordEvent(tag_id, error);
                                break;
                        }
                    }
                    status = READY_STATUS;
                    break;
            }
        }

        @Override
        public void onWriteTimeout() {
            System.err.println("Write timeout!");
            switch (status) {
                case ERROR_STATUS:
                case NOT_INITIALIZED_STATUS:
                case UNINITIALIZED_STATUS:
                    status = ERROR_STATUS;
                    reader_listener.connectionFailedEvent(AbstractResponseListener.READER_WRITE_TIMEOUT_ERROR);
                    break;
                case READY_STATUS:
                    break;
                case PENDING_COMMAND_STATUS:
                    if (sub_status == CMD_SUBSTATUS) {
                        sub_status = SET_STREAM_SUBSTATUS;
                        device_manager.requestSetMode(STREAM_MODE);
                        break;
                    }
                    if (pending >= AbstractReaderListener.SOUND_COMMAND &&
                            pending <= AbstractReaderListener.GET_SECURITY_LEVEL_COMMAND) {
                        reader_listener.resultEvent(pending, AbstractReaderListener.READER_WRITE_TIMEOUT_ERROR);
                    }
                    else {
                        switch (pending) {
                            case AbstractResponseListener.READ_COMMAND:
                                response_listener.readEvent(tag_id, AbstractResponseListener
                                        .READER_WRITE_TIMEOUT_ERROR, null);
                                break;
                            case AbstractResponseListener.WRITE_COMMAND:
                                response_listener.writeEvent(tag_id, AbstractResponseListener
                                        .READER_WRITE_TIMEOUT_ERROR);
                                break;
                            case AbstractResponseListener.LOCK_COMMAND:
                                response_listener.lockEvent(tag_id, AbstractResponseListener
                                        .READER_WRITE_TIMEOUT_ERROR);
                                break;
                            case AbstractResponseListener.WRITEID_COMMAND:
                                response_listener.writeIDevent(tag_id, AbstractResponseListener
                                        .READER_WRITE_TIMEOUT_ERROR);
                                break;
                            case AbstractResponseListener.READ_TID_COMMAND:
                                response_listener.readTIDevent(tag_id, AbstractResponseListener
                                                .READER_WRITE_TIMEOUT_ERROR,
                                        null);
                                break;
                            case AbstractResponseListener.KILL_COMMAND:
                                response_listener.killEvent(tag_id, AbstractResponseListener
                                        .READER_WRITE_TIMEOUT_ERROR);
                                break;
                            case AbstractResponseListener.WRITEKILLPASSWORD_COMMAND:
                            case AbstractResponseListener.WRITEACCESSPASSWORD_COMMAND:
                                response_listener.writePasswordEvent(tag_id, AbstractResponseListener
                                        .READER_WRITE_TIMEOUT_ERROR);
                                break;
                        }
                    }
                    status = READY_STATUS;
                    break;
            }
        }

        private int byteToInt(byte b) {
            return (b < 0) ? (256 + b) : b;
        }
    }

    /**
     * EPC standard.
     */
    public static final int EPC_STANDARD = 0x00;
    /**
     * ISO-15693 standard.
     */
    public static final int ISO15693_STANDARD = 0x01;
    /**
     * ISO-15443A standard.
     */
    public static final int ISO14443A_STANDARD = 0x02;
    /**
     * ISO-15693 and ISO14443A standards.
     */
    public static final int ISO15693_AND_ISO14443A_STANDARD = 0x03;
    /**
     * Inventory scan started by {@link PassiveReader#doInventory()
     * doInventory} method invocation.
     */
    public static final int NORMAL_MODE = 0x00;
    /**
     * Inventory scan started periodically (period set by {@link
     * PassiveReader#setInventoryParameters(int, int, int) setInventoryParameters}
     * method invocation.
     */
    public static final int SCAN_ON_TIME_MODE = 0x01;
    /**
     * Inventory scan started by the reader device button pression.
     */
    public static final int SCAN_ON_INPUT_MODE = 0x02;
    /**
     * Sound and LED light feedback for inventory successful operation.
     */
    public static final int FEEDBACK_SOUND_AND_LIGHT = 0x00;
    /**
     * No local feedback for inventory successful operation.
     */
    public static final int NO_FEEDBACK = 0x01;
    /**
     * Low battery status
     */
    public static final int LOW_BATTERY_STATUS = 0x00;
    /**
     * Charged battery status
     */
    public static final int CHARGED_BATTERY_STATUS = 0x01;
    /**
     * Charging battery status
     */
    public static final int CHARGING_BATTERY_STATUS = 0x02;
    /**
     * HF reader device half RF power
     */
    public static final int HF_RF_HALF_POWER = 0x00;
    /**
     * HF reader device full RF power
     */
    public static final int HF_RF_FULL_POWER = 0x01;
    /**
     * HF reader device automatic RF power management
     */
    public static final int HF_RF_AUTOMATIC_POWER = 0x00;
    /**
     * HF reader device fixed RF power
     */
    public static final int HF_RF_FIXED_POWER = 0x01;
    /**
     * UHF reader device 0dB RF power
     */
    public static final int UHF_RF_POWER_0_DB = 0x00;
    /**
     * UHF reader device -1dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_1_DB = 0x01;
    /**
     * UHF reader device -2dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_2_DB = 0x02;
    /**
     * UHF reader device -3dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_3_DB = 0x03;
    /**
     * UHF reader device -4dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_4_DB = 0x04;
    /**
     * UHF reader device -5dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_5_DB = 0x05;
    /**
     * UHF reader device -6dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_6_DB = 0x06;
    /**
     * UHF reader device -7dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_7_DB = 0x07;
    /**
     * UHF reader device -8dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_8_DB = 0x08;
    /**
     * UHF reader device -9dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_9_DB = 0x09;
    /**
     * UHF reader device -10dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_10_DB = 0x0A;
    /**
     * UHF reader device -11dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_11_DB = 0x0B;
    /**
     * UHF reader device -12dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_12_DB = 0x0C;
    /**
     * UHF reader device -13dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_13_DB = 0x0D;
    /**
     * UHF reader device -14dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_14_DB = 0x0E;
    /**
     * UHF reader device -15dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_15_DB = 0x0F;
    /**
     * UHF reader device -16dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_16_DB = 0x10;
    /**
     * UHF reader device -17dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_17_DB = 0x011;
    /**
     * UHF reader device -18dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_18_DB = 0x012;
    /**
     * UHF reader device -19dB RF power
     */
    public static final int UHF_RF_POWER_MINUS_19_DB = 0x013;
    /**
     * UHF reader device automatic RF power management
     */
    public static final int UHF_RF_POWER_AUTOMATIC_MODE = 0x00;
    /**
     * UHF reader device fixed RF power with low bias
     */
    public static final int UHF_RF_POWER_FIXED_LOW_BIAS_MODE = 0x01;
    /**
     * UHF reader device fixed RF power with high bias
     */
    public static final int UHF_RF_POWER_FIXED_HIGH_BIAS_MODE = 0x02;
    /**
     * ISO15693 tag with no option bits
     */
    public static final int ISO15693_OPTION_BITS_NONE = 0x00;
    /**
     * ISO15693 tag with option bit for lock operations
     */
    public static final int ISO15693_OPTION_BITS_LOCK = 0x01;
    /**
     * ISO15693 tag with option bit for write operations
     */
    public static final int ISO15693_OPTION_BITS_WRITE = 0x02;
    /**
     * ISO15693 tag with option bit for read operations
     */
    public static final int ISO15693_OPTION_BITS_READ = 0x04;
    /**
     * ISO15693 tag with option bit for inventory operations
     */
    public static final int ISO15693_OPTION_BITS_INVENTORY = 0x08;
    /**
     * ISO15693 low bit-rate tag operations
     */
    public static final int ISO15693_LOW_BITRATE = 0;
    /**
     * ISO15693 high bit-rate tag operations
     */
    public static final int ISO15693_HIGH_BITRATE = 1;
    /**
     * UHF reader device RF carrier frequency from 902.75MHz to 927.5MHz
     * (50 radio channels with frequency hopping)
     */
    public static final int RF_CARRIER_FROM_902_75_TO_927_5_MHZ = 0x00;
    /**
     * UHF reader device RF carrier frequency from 915.25MHz to 927.5MHz
     * (25 radio channels with frequency hopping)
     */
    public static final int RF_CARRIER_FROM_915_25_TO_927_5_MHZ = 0x01;
    /**
     * UHF reader device RF carrier frequency 865.7MHz (no frequency hopping)
     */
    public static final int RF_CARRIER_865_7_MHZ = 0x02;
    /**
     * UHF reader device RF carrier frequency 866.3MHz (no frequency hopping)
     */
    public static final int RF_CARRIER_866_3_MHZ = 0x03;
    /**
     * UHF reader device RF carrier frequency 866.9MHz (no frequency hopping)
     */
    public static final int RF_CARRIER_866_9_MHZ = 0x04;
    /**
     * UHF reader device RF carrier frequency 867.5MHz (no frequency hopping)
     */
    public static final int RF_CARRIER_867_5_MHZ = 0x05;
    /**
     * UHF reader device RF carrier frequency from 865.7MHz to 867.5MHz
     * (4 radio channels with frequency hopping)
     */
    public static final int RF_CARRIER_FROM_865_7_TO_867_5_MHZ = 0x06;
    /**
     * UHF reader device RF carrier frequency 915.1MHz (no frequency hopping)
     */
    public static final int RF_CARRIER_915_1_MHZ = 0x07;
    /**
     * UHF reader device RF carrier frequency 915.7MHz (no frequency hopping)
     */
    public static final int RF_CARRIER_915_7_MHZ = 0x08;
    /**
     * UHF reader device RF carrier frequency 916.3MHz (no frequency hopping)
     */
    public static final int RF_CARRIER_916_3_MHZ = 0x09;
    /**
     * UHF reader device RF carrier frequency 916.9MHz (no frequency hopping)
     */
    public static final int RF_CARRIER_916_9_MHZ = 0x0A;
    /**
     * UHF reader device RF carrier frequency from 915.1MHz to 916.9MHz
     * (4 radio channels with frequency hopping)
     */
    public static final int RF_CARRIER_FROM_915_1_TO_916_9_MHZ = 0x0B;
    /**
     * UHF reader device RF carrier frequency 902.75MHz (no frequency hopping)
     */
    public static final int RF_CARRIER_902_75_MHZ = 0x0C;
    /**
     * UHF reader device RF carrier frequency 908.75MHz (no frequency hopping)
     */
    public static final int RF_CARRIER_908_75_MHZ = 0x0D;
    /**
     * UHF reader device RF carrier frequency 915.25MHz (no frequency hopping)
     */
    public static final int RF_CARRIER_915_25_MHZ = 0x0E;
    /**
     * UHF reader device RF carrier frequency 921.25MHz (no frequency hopping)
     */
    public static final int RF_CARRIER_921_25_MHZ = 0x0F;
    /**
     * UHF reader device RF carrier frequency 925.25MHz (no frequency hopping)
     */
    public static final int RF_CARRIER_925_25_MHZ = 0x10;
    /**
     * BLE security level 1 (no security).
     */
    public static final int BLE_NO_SECURITY = 0x00;
    /**
     * Legacy BLE security level 2.
     */
    public static final int BLE_LEGACY_LEVEL_2_SECURITY = 0x01;
    /**
     * LESC BLE security level 2.
     */
    public static final int BLE_LESC_LEVEL_2_SECURITY = 0x02;

    private static final byte REGISTER_RF_CHANNEL_SELECTION = (byte) (0xF0);
    private static final byte REGISTER_BIT_RATE_SELECTION = (byte) (0xF1);
    private static final byte REGISTER_PROTOCOL_EXTENSION_FLAG = (byte) (0xF3);
    private static final byte REGISTER_OPTION_BITS = (byte) (0xF6);
    private static final byte REGISTER_RF_PARAMETERS_FOR_TUNNEL_MODE = (byte) (0xFB);
    private static final byte REGISTER_ADC_BATTERY_VALUE = (byte) (0xFC);
    /**
     * Inventory operation get ISO15693 and/or ISO14443A ID only.
     */
    private static final int ID_ONLY_FORMAT = 0x01;
    /**
     * Inventory operation get EPC tag ID only.
     */
    private static final int EPC_ONLY_FORMAT = 0x01;
    /**
     * Inventory operation get ECP tag ID and PC (Protocol Code).
     */
    private static final int EPC_AND_PC_FORMAT = 0x03;
    /**
     * Inventory operation get EPC tag ID e TID (tag unique ID).
     */
    private static final int EPC_AND_TID_FORMAT = 0x05;
    /**
     * Inventory operation get EPC tag ID, PC (Protocol Code) and TID (tag
     * unique ID).
     */
    private static final int ECP_AND_PC_AND_TID_FORMAT = 0x07;

    protected static final int ERROR_STATUS = -1;
    protected static final int NOT_INITIALIZED_STATUS = 0;
    protected static final int UNINITIALIZED_STATUS = 1;
    protected static final int READY_STATUS = 3;
    protected static final int PENDING_COMMAND_STATUS = 4;
    protected static final int STREAM_SUBSTATUS = 0;
    protected static final int SET_CMD_SUBSTATUS = 1;
    protected static final int CMD_SUBSTATUS = 2;
    protected static final int SET_STREAM_SUBSTATUS = 3;
    protected static final int STREAM_MODE = 1;
    protected static final int CMD_MODE = 3;
    protected static final byte BEEPER_COMMAND = 0x01;
    protected static final byte LED_COMMAND = 0x02;
    protected static final byte BLE_CONFIG_COMMAND = 0x04;
    protected static final byte STATUS_COMMAND = 0x05;
    protected static final byte MODE_COMMAND = 0x06;
    protected static final byte SETAUTOOFF_COMMAND = 0x0D;
    protected static final byte SETMODE_COMMAND = 0x0E;
    protected static final byte SETSTANDARD_COMMAND = 0x0F;
    protected static final byte EPC_INVENTORY_COMMAND = 0x11;
    protected static final byte EPC_WRITEID_COMMAND = 0x12;
    protected static final byte EPC_READ_COMMAND = 0x13;
    protected static final byte EPC_WRITE_COMMAND = 0x14;
    protected static final byte EPC_LOCK_COMMAND = 0x15;
    protected static final byte EPC_KILL_COMMAND = 0x16;
    protected static final byte EPC_SETREGISTER_COMMAND = 0x1E;
    protected static final byte EPC_SETPOWER_COMMAND = 0x1F;
    protected static final byte ISO15693_INVENTORY_COMMAND = 0x21;
    protected static final byte ISO15693_READ_COMMAND = 0x23;
    protected static final byte ISO15693_WRITE_COMMAND = 0x24;
    protected static final byte ISO15693_LOCK_COMMAND = 0x25;
    protected static final byte ISO15693_SETREGISTER_COMMAND = 0x2E;
    protected static final byte ISO15693_SETPOWER_COMMAND = 0x2F;
    protected static final byte ISO14443A_INVENTORY_COMMAND = 0x31;
    protected static final byte BLE_DEVICE_NAME = 0x01;
    protected static final byte BLE_SECURITY_LEVEL = 0x02;
    protected static final byte BLE_ADVERTISING_INTERVAL = 0x03;
    protected static final byte BLE_TX_POWER = 0x04;
    protected static final byte BLE_CONNECTION_INTERVAL = 0x05;
    protected static final byte BLE_MAC_ADDRESS = 0x06;
    protected static final byte BLE_SLAVE_LATENCY = 0x07;
    protected static final byte BLE_SUPERVISION_TIMEOUT = 0x08;
    protected static final byte BLE_VERSION = 0x09;
    protected static final byte BLE_USER_MEMORY = 0x0A;
    protected static final byte BLE_FACTORY_DEFAULT = (byte) (0xF0);
    protected static final byte BLE_BOOTLOADER = (byte) (0xF1);
    protected static final byte SUCCESSFUL_OPERATION_RETCODE = 0x00;
    protected static final byte INVALID_MEMORY_RETCODE = 0x01;
    protected static final byte LOCKED_MEMORY_RETCODE = 0x02;
    protected static final byte INVENTORY_ERROR_RETCODE = 0x03;
    protected static final byte INVALID_PARAMETER_RETCODE = 0x0C;
    protected static final byte TIMEOUT_RETCODE = 0x0D;
    protected static final byte UNIMPLEMENTED_COMMAND_RETCODE = 0x0E;
    protected static final byte INVALID_COMMAND_RETCODE = 0x0F;

    private static PassiveReader instance = null;
    private static AbstractInventoryListener inventory_listener;
    private static AbstractReaderListener reader_listener;
    private static TxRxDeviceCallback device_callback;

    protected static AbstractResponseListener response_listener;
    protected static TxRxDeviceManager device_manager;
    protected static volatile String command;

    public static PassiveReader getInstance(AbstractInventoryListener inventory_listener,
                                            AbstractReaderListener reader_listener,
                                            AbstractResponseListener response_listener, BluetoothAdapter
                                                    bluetoothAdapter, BleSettings bleSettings) {
        if (instance == null) {
            instance = new PassiveReader(bluetoothAdapter);
        }
        instance.init(inventory_listener, reader_listener, response_listener, bleSettings);
        return instance;
    }

    protected static String byteToHex(int val) {
        byte tmp = (byte) (val % 256);
        return String.format("%02X", tmp);
    }

    protected static int hexToByte(String hex) {
        try {
            return Integer.valueOf(hex, 16);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    protected static int hexToWord(String hex) {
        return Integer.valueOf(hex, 16);
    }
    private volatile int inventory_mode, mode;
    private volatile int inventory_feedback, feedback;
    private volatile int inventory_format, format;
    private volatile int inventory_max_number, max_number;
    private volatile int inventory_interval, interval;
    private volatile int inventory_timeout, timeout;
    private volatile int inventory_standard, standard;
    private volatile boolean HF_device;
    private volatile boolean UHF_device;
    private volatile boolean inventory_enabled;
    protected volatile int status;
    protected volatile int sub_status;
    protected volatile int sequential;
    protected volatile int pending;
    protected volatile byte[] tag_id;

    private PassiveReader(BluetoothAdapter bluetoothAdapter) {
        inventory_listener = null;
        reader_listener = null;
        response_listener = null;
        device_callback = new DeviceCallback(this);

        device_manager = new TxRxDeviceManager(bluetoothAdapter, device_callback);
        sequential = 0;
        inventory_enabled = false;
        inventory_mode = NORMAL_MODE; // ???
    }

    /**
     * Start a tag encrypted tunnel operation.
     * <p>
     * In encrypted tunnel operation the command bytes are directly sent to the
     * reader device.
     * <p>
     * * The result of the tunnel operation is notified invoking reader listener
     * methods {@link AbstractReaderListener#resultEvent(int, int) resultEvent}
     * and {@link AbstractReaderListener#tunnelEvent(byte[]) tunnelEvent}.
     *
     * @param flag    flag byte (not encrypted)
     * @param command the command to send to the tag
     */
    public synchronized void ISO15693encryptedTunnel(byte flag, byte[] command) {
        byte frame[] = new byte[1 + command.length];

        int s = status;
        if (status != PassiveReader.READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.ISO15693_ENCRYPTEDTUNNEL_COMMAND,
                    AbstractReaderListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        frame[0] = flag;
        for (int n = 0; n < command.length; n++) {
            frame[n + 1] = command[n];
        }
        status = PassiveReader.PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.ISO15693_ENCRYPTEDTUNNEL_COMMAND;
        device_manager.requestWriteData(buildTunnelCommand(true, frame));
    }

    /**
     * Start an ISO15693 reader tunnel operation.
     * <p>
     * In tunnel operation the command bytes are directly sent to the reader device.
     * <p>
     * The result of the tunnel operation is notified invoking reader listener
     * methods {@link AbstractReaderListener#resultEvent(int, int) resultEvent}
     * and {@link AbstractReaderListener#tunnelEvent(byte[]) tunnelEvent}.
     *
     * @param command the command to send to the tag
     */
    public synchronized void ISO15693tunnel(byte[] command) {
        int s = status;
        if (status != PassiveReader.READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.ISO15693_TUNNEL_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        status = PassiveReader.PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.ISO15693_TUNNEL_COMMAND;
        device_manager.requestWriteData(buildTunnelCommand(false, command));
    }

    /**
     * Close the reader driver.
     */
    public synchronized void close() {
        disconnect();
        device_manager.close();
        status = NOT_INITIALIZED_STATUS;
    }

    /**
     * Connect the reader device via BLE link.
     *
     * @param reader_address the reader device address
     */
    public synchronized void connect(String reader_address, Context context) {
        disconnect();
        device_manager.connect(reader_address, context);
    }

    /**
     * Disconnect the BLE link with reader device only if it's not in {@link PassiveReader#NOT_INITIALIZED_STATUS}.
     */
    public synchronized void disconnect() {
        if (status != NOT_INITIALIZED_STATUS) {
            device_manager.disconnect();
            status = NOT_INITIALIZED_STATUS;
        }
    }

    /**
     * Start an inventory operation.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractInventoryListener#inventoryEvent(Tag)} methods invocation.
     */
    public synchronized void doInventory() {
        int s = status;
        if (status != READY_STATUS || !inventory_enabled) {
            reader_listener.resultEvent(AbstractReaderListener.INVENTORY_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (HF_device) {
            device_manager.requestWriteData(buildCommand(ISO15693_INVENTORY_COMMAND, (byte) (inventory_timeout)));
        }
        else { // isUHF
            device_manager.requestWriteData(buildCommand(EPC_INVENTORY_COMMAND, (byte) (inventory_timeout)));
        }
    }

    /**
     * Get the reader device battery level fro HF reader device.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractReaderListener#batteryLevelEvent(float) batteryLevelEvent}
     * methods invocation.
     */
    public synchronized void getBatteryLevel() {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.GET_BATTERY_LEVEL_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (UHF_device) {
            reader_listener.resultEvent(AbstractReaderListener.GET_BATTERY_LEVEL_COMMAND, AbstractReaderListener
                    .READER_DRIVER_UNKNOW_COMMAND_ERROR);
            return;
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.GET_BATTERY_LEVEL_COMMAND;
        device_manager.requestWriteData(buildCommand(ISO15693_SETREGISTER_COMMAND, REGISTER_ADC_BATTERY_VALUE));
    }

    /**
     * Get the reader device battery status.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractReaderListener#batteryStatusEvent(int) batteryStatusEvent}
     * methods invocation.
     */
    public synchronized void getBatteryStatus() {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.GET_BATTERY_STATUS_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.GET_BATTERY_STATUS_COMMAND;
        device_manager.requestWriteData(buildCommand(MODE_COMMAND, (byte) (inventory_mode)));
    }

    /**
     * Get the UHF reader device RF frequency for EPC tags.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractReaderListener#EPCfrequencyEvent(int) EPCfrequencyEvent} methods
     * invocation.
     */
    public synchronized void getEPCfrequency() //throws PassiveReaderException{
    {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.GET_EPC_FREQUENCY_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (HF_device) {
            reader_listener.resultEvent(AbstractReaderListener.GET_EPC_FREQUENCY_COMMAND, AbstractReaderListener
                    .READER_DRIVER_UNKNOW_COMMAND_ERROR);
            return;
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.GET_EPC_FREQUENCY_COMMAND;
        device_manager.requestWriteData(buildCommand(EPC_SETREGISTER_COMMAND, REGISTER_RF_CHANNEL_SELECTION));
    }

    /**
     * Get the reader device firmware version.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractReaderListener#firmwareVersionEvent(int, int)
     * firmwareversionEvent} methods invocation.
     */
    public synchronized void getFirmwareVersion() {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.GET_FIRMWARE_VERSION_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.GET_FIRMWARE_VERSION_COMMAND;
        device_manager.requestWriteData(buildCommand(SETSTANDARD_COMMAND, (byte) (inventory_standard)));
    }

    /**
     * Get the HF reader device bit-rate for ISO15693 tags.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractReaderListener#ISO15693bitrateEvent(int, boolean)
     * ISO15693bitrateEvent} methods invocation.
     */
    public synchronized void getISO15693bitrate() {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.GET_ISO15693_BITRATE_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (UHF_device) {
            reader_listener.resultEvent(AbstractReaderListener.GET_ISO15693_BITRATE_COMMAND, AbstractReaderListener
                    .READER_DRIVER_UNKNOW_COMMAND_ERROR);
            return;
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.GET_ISO15693_BITRATE_COMMAND;
        device_manager.requestWriteData(buildCommand(ISO15693_SETREGISTER_COMMAND, REGISTER_BIT_RATE_SELECTION));
    }

    /**
     * Get the HF reader device extension flag for ISO15693 tags.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractReaderListener#ISO15693extensionFlagEvent(boolean, boolean)
     * ISO15693extensionFlagEvent} methods invocation.
     */
    public synchronized void getISO15693extensionFlag() {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.GET_ISO15693_EXTENSION_FLAG_COMMAND,
                    AbstractReaderListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (UHF_device) {
            reader_listener.resultEvent(AbstractReaderListener.GET_ISO15693_EXTENSION_FLAG_COMMAND,
                    AbstractReaderListener.READER_DRIVER_UNKNOW_COMMAND_ERROR);
            return;
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.GET_ISO15693_EXTENSION_FLAG_COMMAND;
        device_manager.requestWriteData(buildCommand(ISO15693_SETREGISTER_COMMAND, REGISTER_PROTOCOL_EXTENSION_FLAG));
    }

    /**
     * Get the HF reader device option bits for ISO15693 tags.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractReaderListener#ISO15693optionBitsEvent(int)
     * ISO15693optionBitsEvent} methods invocation.
     */
    public synchronized void getISO15693optionBits() {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.GET_ISO15693_OPTION_BITS_COMMAND,
                    AbstractReaderListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (UHF_device) {
            reader_listener.resultEvent(AbstractReaderListener.GET_ISO15693_OPTION_BITS_COMMAND,
                    AbstractReaderListener.READER_DRIVER_UNKNOW_COMMAND_ERROR);
            return;
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.GET_ISO15693_OPTION_BITS_COMMAND;
        device_manager.requestWriteData(buildCommand(ISO15693_SETREGISTER_COMMAND, REGISTER_OPTION_BITS));
    }

    /**
     * Get the HF reader device RF parameters to use ISO15693 tunnel mode.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractReaderListener#RFforISO15693tunnelEvent(int, int)
     * RFforISO15693tunnelEvent} methods invocation.
     */
    public synchronized void getRFforISO15693tunnel() {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.GET_RF_FOR_ISO15693_TUNNEL_COMMAND,
                    AbstractReaderListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (UHF_device) {
            reader_listener.resultEvent(AbstractReaderListener.GET_RF_FOR_ISO15693_TUNNEL_COMMAND,
                    AbstractReaderListener.READER_DRIVER_UNKNOW_COMMAND_ERROR);
            return;
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.GET_RF_FOR_ISO15693_TUNNEL_COMMAND;
        device_manager.requestWriteData(buildCommand(ISO15693_SETREGISTER_COMMAND,
                REGISTER_RF_PARAMETERS_FOR_TUNNEL_MODE));
    }

    /**
     * Get the configured RF power for HF/UHF reader device.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractReaderListener#RFpowerEvent(int, int) RFpowerEvent} methods
     * invocation.
     */
    public synchronized void getRFpower() {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.GET_RF_POWER_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.GET_RF_POWER_COMMAND;
        if (HF_device) {
            device_manager.requestWriteData(buildCommand(ISO15693_SETPOWER_COMMAND));
        }
        else { // UHF_device
            device_manager.requestWriteData(buildCommand(EPC_SETPOWER_COMMAND));
        }
    }

    /**
     * Get the reader device security level.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractReaderListener#securityLevelEvent(int) securityLevelEvent} methods
     * invocation.
     */
    public synchronized void getSecurityLevel() {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.GET_SECURITY_LEVEL_COMMAND,
             AbstractReaderListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.GET_SECURITY_LEVEL_COMMAND;
        if (device_manager.isTxRxAckme()) {
            sub_status = SET_CMD_SUBSTATUS;
            command = "get bl e e";
            device_manager.requestSetMode(CMD_MODE);
        }
        else {
            device_manager.requestWriteData(buildCommand(BLE_CONFIG_COMMAND, BLE_SECURITY_LEVEL));
        }
    }

    /**
     * Get the configured reader device shutdown time.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractReaderListener#shutdownTimeEvent(int) shutdownTimeEvent} methods
     * invocation.
     */
    public synchronized void getShutdownTime() {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.GET_SHUTDOWN_TIME_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.GET_SHUTDOWN_TIME_COMMAND;
        device_manager.requestWriteData(buildCommand(SETAUTOOFF_COMMAND));
    }

    /**
     * Test the BLE link with reader device.
     *
     * @return true if the reader device is linked by BLE
     */

    public synchronized boolean isAvailable(String device_address, Context context) {
        return device_manager.isConnected(device_address, context);
    }

    /**
     * Test the reader device type.
     *
     * @return true if the reader is an HF device for ISO15693 and/or ISO14443
     * tags.
     */
    public synchronized boolean isHF() {
        int s = status;
        if (status < READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.IS_HF_COMMAND, AbstractReaderListener
                    .READER_DRIVER_NOT_READY_ERROR);
            return false;
        }
        return HF_device;
    }

    /**
     * Test the reader device type.
     *
     * @return true if the reader is an UHF device for EPC tags.
     */
    public synchronized boolean isUHF() {
        int s = status;
        if (status < READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.IS_UHF_COMMAND, AbstractReaderListener
                    .READER_DRIVER_NOT_READY_ERROR);
            return false;
        }
        return UHF_device;
    }

    /**
     * Command the the reader device to activate the LED light.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} method
     * invocation.
     *
     * @param led_status   if true light on the LED
     * @param led_blinking the time for LED light to blink (milliseconds:
     *                     10-2540, 0 means no blink)
     */
    public synchronized void light(boolean led_status, int led_blinking) {
        byte led[] = new byte[2];

        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.LIGHT_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (led_blinking != 0 && (led_blinking < 10 || led_blinking > 2540)) {
            reader_listener.resultEvent(AbstractReaderListener.LIGHT_COMMAND, AbstractReaderListener
                    .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        if (led_blinking == 0) {
            led[0] = (byte) (led_status ? 0xFF : 0x00);
        }
        else {
            led[0] = (byte) (led_blinking / 10);
        }
        led[1] = (byte) (0x00);
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.LIGHT_COMMAND;
        device_manager.requestWriteData(buildCommand(LED_COMMAND, led[0], led[1]));
    }

    /**
     * Set the UHF reader device RF frequency for EPC tags.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} method
     * invocation.
     *
     * @param frequency the RF frequency
     */
    public synchronized void setEPCfrequency(int frequency) {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.SET_EPC_FREQUENCY_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (HF_device) {
            reader_listener.resultEvent(AbstractReaderListener.SET_EPC_FREQUENCY_COMMAND, AbstractReaderListener
                    .READER_DRIVER_UNKNOW_COMMAND_ERROR);
            return;
        }
        if (frequency < RF_CARRIER_FROM_902_75_TO_927_5_MHZ || frequency > RF_CARRIER_925_25_MHZ) {
            reader_listener.resultEvent(AbstractReaderListener.SET_EPC_FREQUENCY_COMMAND, AbstractReaderListener
                    .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.SET_EPC_FREQUENCY_COMMAND;
        device_manager.requestWriteData(buildCommand(EPC_SETREGISTER_COMMAND, REGISTER_RF_CHANNEL_SELECTION, (byte)
                (frequency)));
    }

    /**
     * Set the HF reader device bit-rate for ISO15693 tags.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} method
     * invocation.
     *
     * @param bitrate   the bit-rate
     * @param permanent if true the extension flag configuration is permanent
     */
    public synchronized void setISO15693bitrate(int bitrate, boolean permanent) {
        byte data;

        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.SET_ISO15693_BITRATE_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (UHF_device) {
            reader_listener.resultEvent(AbstractReaderListener.SET_ISO15693_BITRATE_COMMAND, AbstractReaderListener
                    .READER_DRIVER_UNKNOW_COMMAND_ERROR);
            return;
        }
        if (bitrate < ISO15693_LOW_BITRATE || bitrate > ISO15693_HIGH_BITRATE) {
            reader_listener.resultEvent(AbstractReaderListener.SET_ISO15693_BITRATE_COMMAND, AbstractReaderListener
                    .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        if (permanent) {
            data = (bitrate == ISO15693_HIGH_BITRATE) ? (byte) (0x01) : (byte) (0x00);
        }
        else {
            data = (bitrate == ISO15693_HIGH_BITRATE) ? (byte) (0x03) : (byte) (0x02);
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.SET_ISO15693_BITRATE_COMMAND;
        device_manager.requestWriteData(buildCommand(ISO15693_SETREGISTER_COMMAND, REGISTER_BIT_RATE_SELECTION, data));
    }

    /**
     * Set the HF reader device extension flag for ISO15693 tags.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} method
     * invocation.
     *
     * @param flag      if true the extension flag is configured
     * @param permanent if true the extension flag configuration is permanent
     */
    public synchronized void setISO15693extensionFlag(boolean flag, boolean permanent) {
        byte data;

        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.SET_ISO15693_EXTENSION_FLAG_COMMAND,
                    AbstractReaderListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (UHF_device) {
            reader_listener.resultEvent(AbstractReaderListener.SET_ISO15693_EXTENSION_FLAG_COMMAND,
                    AbstractReaderListener.READER_DRIVER_UNKNOW_COMMAND_ERROR);
            return;
        }
        if (permanent) {
            data = flag ? (byte) (0x01) : (byte) (0x00);
        }
        else {
            data = flag ? (byte) (0x03) : (byte) (0x02);
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.SET_ISO15693_EXTENSION_FLAG_COMMAND;
        device_manager.requestWriteData(buildCommand(ISO15693_SETREGISTER_COMMAND, REGISTER_PROTOCOL_EXTENSION_FLAG,
                data));
    }

    /**
     * Set the HF reader device option bits for ISO15693 tags.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} method
     * invocation.
     *
     * @param option_bits the option bits
     */
    public synchronized void setISO15693optionBits(int option_bits) {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.SET_ISO15693_OPTION_BITS_COMMAND,
                    AbstractReaderListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (UHF_device) {
            reader_listener.resultEvent(AbstractReaderListener.SET_ISO15693_OPTION_BITS_COMMAND,
                    AbstractReaderListener.READER_DRIVER_UNKNOW_COMMAND_ERROR);
            return;
        }
        if (option_bits < ISO15693_OPTION_BITS_NONE || option_bits > (ISO15693_OPTION_BITS_LOCK |
                ISO15693_OPTION_BITS_WRITE | ISO15693_OPTION_BITS_READ | ISO15693_OPTION_BITS_INVENTORY)) {
            reader_listener.resultEvent(AbstractReaderListener.SET_ISO15693_OPTION_BITS_COMMAND,
                    AbstractReaderListener.READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.SET_ISO15693_OPTION_BITS_COMMAND;
        device_manager.requestWriteData(buildCommand(ISO15693_SETREGISTER_COMMAND, REGISTER_OPTION_BITS, (byte)
                (option_bits)));
    }

    /**
     * Set the inventory operating mode for the reader device.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} method
     * invocation.
     *
     * @param mode the inventory operating mode
     */
    public synchronized void setInventoryMode(int mode) {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.SET_INVENTORY_MODE_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (mode < NORMAL_MODE || mode > SCAN_ON_INPUT_MODE) {
            reader_listener.resultEvent(AbstractReaderListener.SET_INVENTORY_MODE_COMMAND, AbstractReaderListener
                    .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        this.mode = mode;
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.SET_INVENTORY_MODE_COMMAND;
        device_manager.requestWriteData(buildCommand(MODE_COMMAND, (byte) (mode)));
    }

    /**
     * Set the inventory parameters
     * <p>
     * The parameters are permanently configured.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} method
     * invocation.
     *
     * @param feedback the reader device local feedback for detected tag(s)
     * @param timeout  the inventory scan time (milliseconds: 100-2000)
     * @param interval the inventory repetition period (milliseconds: 100-25500)
     */
    public synchronized void setInventoryParameters(int feedback, int timeout, int interval) {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.SET_INVENTORY_PARAMETERS_COMMAND,
                    AbstractReaderListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (feedback < FEEDBACK_SOUND_AND_LIGHT || feedback > NO_FEEDBACK) {
            reader_listener.resultEvent(AbstractReaderListener.SET_INVENTORY_PARAMETERS_COMMAND,
                    AbstractReaderListener.READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        if (timeout < 100 || timeout > 2000) {
            reader_listener.resultEvent(AbstractReaderListener.SET_INVENTORY_PARAMETERS_COMMAND,
                    AbstractReaderListener.READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        if (interval < 100 || interval > 25500) {
            reader_listener.resultEvent(AbstractReaderListener.SET_INVENTORY_PARAMETERS_COMMAND,
                    AbstractReaderListener.READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        this.feedback = feedback;
        if (HF_device) {
            format = ID_ONLY_FORMAT;
        }
        else // UHF_device
        {
            format = EPC_AND_PC_FORMAT;
        }
        max_number = 0;
        this.timeout = timeout / 100;
        this.interval = interval / 100;

        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.SET_INVENTORY_PARAMETERS_COMMAND;
        device_manager.requestWriteData(buildCommand(SETMODE_COMMAND, (byte) (mode), (byte) (feedback), (byte)
                        (format), (byte) (max_number),
                (byte) (timeout / 100), (byte) (interval / 100)));
    }

    /**
     * Set the inventory standard type for the HF reader device.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} method
     * invocation.
     *
     * @param standard the standard type
     */
    public synchronized void setInventoryType(int standard) {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.SET_INVENTORY_TYPE_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (standard < EPC_STANDARD || standard > ISO15693_AND_ISO14443A_STANDARD) {
            reader_listener.resultEvent(AbstractReaderListener.SET_INVENTORY_TYPE_COMMAND, AbstractReaderListener
                    .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        this.standard = standard;
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.SET_INVENTORY_TYPE_COMMAND;
        device_manager.requestWriteData(buildCommand(SETSTANDARD_COMMAND, (byte) (standard)));
    }

    /**
     * Set the HF reader device RF parameters to use ISO15693 tunnel mode.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} method
     * invocation.
     *
     * @param delay   the delay from RF power switch-on and command transmission
     *                (milliseconds: 0-255)
     * @param timeout the time before RF power switch-off (seconds: 0-255)
     */
    public synchronized void setRFforISO15693tunnel(int delay, int timeout) {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.SET_RF_FOR_ISO15693_TUNNEL_COMMAND,
                    AbstractReaderListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (UHF_device) {
            reader_listener.resultEvent(AbstractReaderListener.SET_RF_FOR_ISO15693_TUNNEL_COMMAND,
                    AbstractReaderListener.READER_DRIVER_UNKNOW_COMMAND_ERROR);
            return;
        }
        if (delay < 0 || delay > 255) {
            reader_listener.resultEvent(AbstractReaderListener.SET_RF_FOR_ISO15693_TUNNEL_COMMAND,
                    AbstractReaderListener.READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        if (timeout < 0 || timeout > 255) {
            reader_listener.resultEvent(AbstractReaderListener.SET_RF_FOR_ISO15693_TUNNEL_COMMAND,
                    AbstractReaderListener.READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.SET_RF_FOR_ISO15693_TUNNEL_COMMAND;
        device_manager.requestWriteData(buildCommand(ISO15693_SETREGISTER_COMMAND,
                REGISTER_RF_PARAMETERS_FOR_TUNNEL_MODE, (byte) (timeout), (byte) (delay)));
    }

    /**
     * Set the RF power for HF/UHF reader device.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} method
     * invocation.
     *
     * @param level the RF power level
     * @param mode  the RF power mode
     */
    public synchronized void setRFpower(int level, int mode) {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.SET_RF_POWER_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (HF_device) {
            if (level < HF_RF_HALF_POWER || level > HF_RF_FULL_POWER) {
                reader_listener.resultEvent(AbstractReaderListener.SET_RF_POWER_COMMAND, AbstractReaderListener
                        .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
                return;
            }
            if (mode < HF_RF_AUTOMATIC_POWER || mode > HF_RF_FIXED_POWER) {
                reader_listener.resultEvent(AbstractReaderListener.SET_RF_POWER_COMMAND, AbstractReaderListener
                        .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
                return;
            }
        }
        else { // UHF_device
            if (level < UHF_RF_POWER_0_DB || level > UHF_RF_POWER_MINUS_19_DB) {
                reader_listener.resultEvent(AbstractReaderListener.SET_RF_POWER_COMMAND, AbstractReaderListener
                        .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
                return;
            }
            if (mode < UHF_RF_POWER_AUTOMATIC_MODE || mode > UHF_RF_POWER_FIXED_HIGH_BIAS_MODE) {
                reader_listener.resultEvent(AbstractReaderListener.SET_RF_POWER_COMMAND, AbstractReaderListener
                        .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
                return;
            }
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.SET_RF_POWER_COMMAND;
        if (HF_device) {
            device_manager.requestWriteData(buildCommand(ISO15693_SETPOWER_COMMAND, (byte) (level), (byte) (mode)));
        }
        else { // UHF_device
            device_manager.requestWriteData(buildCommand(EPC_SETPOWER_COMMAND, (byte) (level), (byte) (mode)));
        }
    }

    /**
     * Set the reader device security level.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} method
     * invocation.
     * The new security level will be set after a power off/on cycle of the
     * reader device.
     *
     * @param level the new security level
     */
    public synchronized void setSecurityLevel(int level) {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.SET_SECURITY_LEVEL_COMMAND,
             AbstractReaderListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (device_manager.isTxRxAckme()) {
            reader_listener.resultEvent(AbstractReaderListener.SET_SECURITY_LEVEL_COMMAND,
             AbstractReaderListener.READER_DRIVER_UNKNOW_COMMAND_ERROR);
            return;
        }
        if (level < BLE_NO_SECURITY || level > BLE_LESC_LEVEL_2_SECURITY) {
            reader_listener.resultEvent(AbstractReaderListener.SET_SECURITY_LEVEL_COMMAND,
             AbstractReaderListener.READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.SET_SECURITY_LEVEL_COMMAND;
        device_manager.requestWriteData(buildCommand(BLE_CONFIG_COMMAND, BLE_SECURITY_LEVEL, (byte) (level)));
    }

    /**
     * Set the shutdown time of the reader device if inactive.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} method
     * invocation.
     *
     * @param time the inactive time before reader device switch off (seconds:
     *             10-64800)
     */
    public synchronized void setShutdownTime(int time) {
        String tmp;
        byte shutdown_time[] = new byte[2];

        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.SET_SHUTDOWN_TIME_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (time < 10 || time > 64800) {
            reader_listener.resultEvent(AbstractReaderListener.SET_SHUTDOWN_TIME_COMMAND, AbstractReaderListener
                    .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        tmp = String.format("%04X", time);
        shutdown_time[0] = (byte) hexToByte(tmp.substring(0, 2));
        shutdown_time[1] = (byte) hexToByte(tmp.substring(2, 4));
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.SET_SHUTDOWN_TIME_COMMAND;
        device_manager.requestWriteData(buildCommand(SETAUTOOFF_COMMAND, shutdown_time[0], shutdown_time[1]));
    }

    /**
     * Command the the reader device to generate a sound.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} method
     * invocation.
     *
     * @param frequency  the sound starting frequency (Hertz: 40-20000)
     * @param step       the frequency step for repeated sounds (Hertz: 40-10000)
     * @param duration   the single sound duration (milliseconds: 10-2550)
     * @param interval   the time interval for repeated sounds (milliseconds:
     *                   10-2550)
     * @param repetition the number of sound repetition (0-255)
     */
    public synchronized void sound(int frequency, int step, int duration, int interval,
                                   int repetition) {

        byte start_frequency[] = new byte[2];
        byte frequency_step[] = new byte[2];

        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.SOUND_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (frequency < 40 || frequency > 20000) {
            reader_listener.resultEvent(AbstractReaderListener.SOUND_COMMAND, AbstractReaderListener
                    .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        if (step < 40 || step > 10000) {
            reader_listener.resultEvent(AbstractReaderListener.SOUND_COMMAND, AbstractReaderListener
                    .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        if (duration < 10 || duration > 2550) {
            reader_listener.resultEvent(AbstractReaderListener.SOUND_COMMAND, AbstractReaderListener
                    .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        if (interval < 10 || interval > 2550) {
            reader_listener.resultEvent(AbstractReaderListener.SOUND_COMMAND, AbstractReaderListener
                    .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        if (repetition > 255) {
            reader_listener.resultEvent(AbstractReaderListener.SOUND_COMMAND, AbstractReaderListener
                    .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }

        String tmp = String.format("%04X", frequency);
        start_frequency[0] = (byte) hexToByte(tmp.substring(0, 2));
        start_frequency[1] = (byte) hexToByte(tmp.substring(2, 4));
        if (step >= 0) {
            tmp = String.format("%04X", step);
        }
        else {
            tmp = String.format("%04X", -step);
        }
        frequency_step[0] = (byte) hexToByte(tmp.substring(0, 2));
        frequency_step[1] = (byte) hexToByte(tmp.substring(2, 4));
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.SOUND_COMMAND;
        device_manager.requestWriteData(buildCommand(BEEPER_COMMAND, start_frequency[0], start_frequency[1],
                (byte) (duration / 10), (byte) (interval / 10),
                (byte) (repetition),
                frequency_step[0], frequency_step[1],
                (byte) (step >= 0 ? 0x00 : 0x01)));
    }

    /**
     * Test the reader device functionality.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractReaderListener#availabilityEvent(boolean) availibilityEvent}
     * methods invocation.
     */
    public synchronized void testAvailability() {
        int s = status;
        if (status != READY_STATUS) {
            reader_listener.resultEvent(AbstractReaderListener.TEST_AVAILABILITY_COMMAND, AbstractReaderListener
                    .READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        status = PENDING_COMMAND_STATUS;
        pending = AbstractReaderListener.TEST_AVAILABILITY_COMMAND;
        device_manager.requestWriteData(buildCommand(SETSTANDARD_COMMAND));
    }

    private void init(AbstractInventoryListener inventory_listener,
                      AbstractReaderListener reader_listener,
                      AbstractResponseListener response_listener, BleSettings bleSettings) {
        this.inventory_listener = inventory_listener;
        this.reader_listener = reader_listener;
        this.response_listener = response_listener;

        TxRxTimeouts txrxTimeouts = new TxRxTimeouts(bleSettings.getConnectTimeout(), bleSettings.getWriteTimeout(),
                bleSettings.getFirstReadTimeout(), bleSettings.getLaterReadTimeout());
        device_manager.setTxRxTimeouts(txrxTimeouts);
    }

    protected String appendDataToCommand(String command, byte data[]) {
        int frame_length = command.length() - 2;
        String tail = command.substring(4);

        for (byte aData : data) {
            String tmp = byteToHex(aData);
            tail += tmp;
        }
        frame_length += data.length * 2;
        command = "$:" + byteToHex(frame_length) + tail;
        return command;
    }

    protected String buildCommand(byte command_code, byte... parameters) {
        String command = "$:";
        command += byteToHex(6 + 2 * parameters.length);
        command += byteToHex(sequential);
        sequential++;
        command += byteToHex(command_code);
        for (byte parameter : parameters) {
            String tmp = byteToHex(parameter);
            command += tmp;
        }
        return command;
    }

    protected String buildTunnelCommand(boolean encrypted, byte... parameters) {
        String command;
        if (encrypted) {
            command = "%:";
        }
        else {
            command = "#:";
        }
        for (byte parameter : parameters) {
            String tmp = byteToHex(parameter);
            command += tmp;
        }
        return command;
    }
}
