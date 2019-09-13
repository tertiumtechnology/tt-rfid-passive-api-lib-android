/*
 * The MIT License
 *
 * Copyright 2017 Tertium Technology.
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

package com.tertiumtechnology.api.rfidpassiveapilib.listener;

import com.tertiumtechnology.api.rfidpassiveapilib.PassiveReader;

/**
 * Listener template for event generated in response to a {@code PassiveReader}
 * method invocation.
 * <p>
 * A concrete instance of {@code AbstractReaderListener} has to set for the
 * instance of the class {@code PassiveReader} to receive notification about
 * methods invocation.
 */
public abstract class AbstractReaderListener {

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
     * {@link PassiveReader#sound(int, int, int, int, int) sound} command.
     */
    public static final int SOUND_COMMAND = 0;
    /**
     * {@link PassiveReader#light(boolean, int) light} command.
     */
    public static final int LIGHT_COMMAND = 1;
    /**
     * {@link PassiveReader#getBatteryStatus() getBatteryStatus} command.
     */
    public static final int GET_BATTERY_STATUS_COMMAND = 2;
    /**
     * {@link PassiveReader#getFirmwareVersion() getFirmwareVersion} command.
     */
    public static final int GET_FIRMWARE_VERSION_COMMAND = 3;
    /**
     * {@link PassiveReader#setShutdownTime(int) setShutdownTime} command.
     */
    public static final int SET_SHUTDOWN_TIME_COMMAND = 4;
    /**
     * {@link PassiveReader#getShutdownTime() getShutdownTime} command.
     */
    public static final int GET_SHUTDOWN_TIME_COMMAND = 5;
    /**
     * {@link PassiveReader#setInventoryMode(int) setInventoryMode} command.
     */
    public static final int SET_INVENTORY_MODE_COMMAND = 6;
    /**
     * {@link PassiveReader#setInventoryParameters(int, int, int)
     * setInventoryParameters} command.
     */
    public static final int SET_INVENTORY_PARAMETERS_COMMAND = 7;
    /**
     * {@link PassiveReader#setRFpower(int, int) setRFpower} command.
     */
    public static final int SET_RF_POWER_COMMAND = 8;
    /**
     * {@link PassiveReader#getRFpower() getRFpower} command.
     */
    public static final int GET_RF_POWER_COMMAND = 9;
    /**
     * {@link PassiveReader#doInventory() } command.
     */
    public static final int INVENTORY_COMMAND = 10;
    /**
     * {@link PassiveReader#setRFforISO15693tunnel(int, int)
     * setRFforISO15693tunnel} command.
     */
    public static final int SET_RF_FOR_ISO15693_TUNNEL_COMMAND = 11;
    /**
     * {@link PassiveReader#getRFforISO15693tunnel() getRFforISO15693tunnel}
     * command.
     */
    public static final int GET_RF_FOR_ISO15693_TUNNEL_COMMAND = 12;
    /**
     * {@link PassiveReader#setISO15693optionBits(int) setISO15693optionBits}
     * command.
     */
    public static final int SET_ISO15693_OPTION_BITS_COMMAND = 13;
    /**
     * {@link PassiveReader#getISO15693optionBits() getISO15693optionBits}
     * command.
     */
    public static final int GET_ISO15693_OPTION_BITS_COMMAND = 14;
    /**
     * {@link PassiveReader#setISO15693extensionFlag(boolean, boolean)
     * setISO15693extensionFlag} command.
     */
    public static final int SET_ISO15693_EXTENSION_FLAG_COMMAND = 15;
    /**
     * {@link PassiveReader#getISO15693extensionFlag() getISO15693extensionFlag}
     * command.
     */
    public static final int GET_ISO15693_EXTENSION_FLAG_COMMAND = 16;
    /**
     * {@link PassiveReader#setISO15693bitrate(int, boolean) setISO15693bitrate}
     * command.
     */
    public static final int SET_ISO15693_BITRATE_COMMAND = 17;
    /**
     * {@link PassiveReader#getISO15693bitrate() getISO15693bitrate} command.
     */
    public static final int GET_ISO15693_BITRATE_COMMAND = 18;
    /**
     * {@link PassiveReader#setEPCfrequency(int) setEPCfrequency} command.
     */
    public static final int SET_EPC_FREQUENCY_COMMAND = 19;
    /**
     * {@link PassiveReader#getEPCfrequency() getEPCfrequency} command.
     */
    public static final int GET_EPC_FREQUENCY_COMMAND = 20;
    /**
     * {@link PassiveReader#testAvailability() testAvailability} command.
     */
    public static final int TEST_AVAILABILITY_COMMAND = 21;
    /**
     * {@link PassiveReader#getBatteryLevel() getBatteryLevel} command.
     */
    public static final int GET_BATTERY_LEVEL_COMMAND = 22;
    /**
     * {@link PassiveReader#setInventoryType(int) setInventoryType} command.
     */
    public static final int SET_INVENTORY_TYPE_COMMAND = 23;
    /**
     * {@link PassiveReader#ISO15693tunnel(byte[]) ISO15693tunnel} command.
     */
    public static final int ISO15693_TUNNEL_COMMAND = 24;
    /**
     * {@link PassiveReader#ISO15693encryptedTunnel(byte, byte[])
     * ISO15693encryptedTunnel} command.
     */
    public static final int ISO15693_ENCRYPTEDTUNNEL_COMMAND = 25;
    /**
     * {@link PassiveReader#isHF() isHF} command.
     */
    public static final int IS_HF_COMMAND = 26;
    /**
     * {@link PassiveReader#isUHF() isUHF} command.
     */
    public static final int IS_UHF_COMMAND = 27;

    /**
     * Successful command code (no error).
     */
    public static final int NO_ERROR = 0x00;
    /**
     * Invalid memory location or bank error code.
     */
    public static final int INVALID_MEMORY_ERROR = 0x01;
    /**
     * Locked memory location or bank error code.
     */
    public static final int LOCKED_MEMORY_ERROR = 0x02;
    /**
     * Inventory error code.
     */
    public static final int INVENTORY_ERROR = 0x03;
    /**
     * Invalid command parameter error code.
     */
    public static final int INVALID_PARAMETER_ERROR = 0x0C;
    /**
     * Timeout error code.
     */
    public static final int TIMEOUT_ERROR = 0x0D;
    /**
     * Not implemented command error code.
     */
    public static final int UNKNOWN_COMMAND_ERROR = 0x0E;
    /**
     * Invalid command error code.
     */
    public static final int INVALID_COMMAND_ERROR = 0x0F;

    /**
     * Reader write command failed error code.
     */
    public static final int READER_WRITE_FAIL_ERROR = 0x10;
    /**
     * Reader write command timeout error code.
     */
    public static final int READER_WRITE_TIMEOUT_ERROR = 0x11;
    /**
     * Reader read answer failed error code.
     */
    public static final int READER_READ_FAIL_ERROR = 0x12;
    /**
     * Reader read answer timeout error code.
     */
    public static final int READER_READ_TIMEOUT_ERROR = 0x13;
    /**
     * Reader command/answer mismatch error code.
     */
    public static final int READER_COMMAND_ANSWER_MISMATCH_ERROR = 0x14;
    /**
     * Reader connection generic error.
     */
    public static final int READER_CONNECT_GENERIC_ERROR = 0x15;
    /**
     * Reader connection timeout error.
     */
    public static final int READER_CONNECT_TIMEOUT_ERROR = 0x16;
    /**
     * Reader connection error in discovering service.
     */
    public static final int READER_CONNECT_UNKNOW_SERVICE_ERROR = 0x17;
    /**
     * Reader connection error: device not found.
     */
    public static final int READER_CONNECT_DEVICE_NOT_FOUND_ERROR = 0x18;
    /**
     * Reader connection error: invalid BT adapter.
     */
    public static final int READER_CONNECT_INVALID_BLUETOOTH_ADAPTER_ERROR = 0x19;
    /**
     * Reader connection error: invalid device address.
     */
    public static final int READER_CONNECT_INVALID_DEVICE_ADDRESS_ERROR = 0x1A;
    /**
     * Reader disconnection error: BLE not initialized.
     */
    public static final int READER_DISCONNECT_BLE_NOT_INITIALIZED_ERROR = 0x1B;
    /**
     * Reader disconnection error: invalid BT adapter.
     */
    public static final int READER_DISCONNECT_INVALID_BLUETOOTH_ADAPTER_ERROR = 0x1C;
    /**
     * Reader read error: BLE device error.
     */
    public static final int READER_READ_BLE_DEVICE_ERROR = 0x1D;
    /**
     * Reader read error: invalid TX characteristic.
     */
    public static final int READER_READ_INVALID_TX_CHARACTERISTIC_ERROR = 0x1E;
    /**
     * Reader write error: BLE device error.
     */
    public static final int READER_WRITE_BLE_DEVICE_ERROR = 0x1F;
    /**
     * Reader write error: invalid RX characteristic.
     */
    public static final int READER_WRITE_INVALID_RX_CHARACTERISTIC_ERROR = 0x20;
    /**
     * Reader write error: previous operation in progress.
     */
    public static final int READER_WRITE_OPERATION_IN_PROGRESS_ERROR = 0x21;
    /**
     * Reader driver not ready error.
     */
    public static final int READER_DRIVER_NOT_READY_ERROR = 0x22;
    /**
     * Reader driver wrong status error.
     */
    public static final int READER_DRIVER_WRONG_STATUS_ERROR = 0x23;
    /**
     * Reader driver un-know command error.
     */
    public static final int READER_DRIVER_UNKNOW_COMMAND_ERROR = 0x24;
    /**
     * Reader driver command wrong parameter error.
     */
    public static final int READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR = 0x25;

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
     * Invoked after a {@link PassiveReader#getEPCfrequency() getEPCfrequency}
     * method invocation to notify result.
     *
     * @param frequency the RF frequency
     */
    public abstract void EPCfrequencyEvent(int frequency);

    /**
     * Invoked after a {@link PassiveReader#getISO15693bitrate()
     * getISO15693bitrate} method invocation to notify result.
     *
     * @param bitrate   the bit-rate configured
     * @param permanent if true the bit-rate is permanent configured
     */
    public abstract void ISO15693bitrateEvent(int bitrate, boolean permanent);

    /**
     * Invoked after a {@link PassiveReader#getISO15693extensionFlag()
     * getISO15693extensionFlag} method invocation to notify result.
     *
     * @param flag      if true the extension flag is configured
     * @param permanent if true the extension flag is permanent configured
     */
    public abstract void ISO15693extensionFlagEvent(boolean flag, boolean permanent);

    /**
     * Invoked after a {@link PassiveReader#getISO15693optionBits()
     * getISO15693optionBits} method invocation to notify result.
     *
     * @param option_bits the option bits
     */
    public abstract void ISO15693optionBitsEvent(int option_bits);

    /**
     * Invoked after a {@link PassiveReader#getRFforISO15693tunnel()
     * getRFforISO15693tunnel} method invocation to notify result.
     *
     * @param delay   the delay from RF power switch-on and command transmission
     *                (milliseconds)
     * @param timeout the time before RF power switch-off (seconds)
     */
    public abstract void RFforISO15693tunnelEvent(int delay, int timeout);

    /**
     * Invoked after a {@link PassiveReader#getRFpower() getRFpower} method
     * invocation to notify result.
     *
     * @param level the RF power level
     * @param mode  the RF power mode
     */
    public abstract void RFpowerEvent(int level, int mode);

    /**
     * Invoked after a {@link PassiveReader#testAvailability() testAvailibility}
     * method invocation to notify result.
     *
     * @param available if true the reader is linked by BLE to the device
     */
    public abstract void availabilityEvent(boolean available);

    /**
     * Invoked after a {@link PassiveReader#getBatteryLevel() getBatteryLevel}
     * method invocation to notify result.
     *
     * @param level the battery charge level (volt)
     */
    public abstract void batteryLevelEvent(float level);

    /**
     * Invoked after a {@link PassiveReader#getBatteryStatus() getBatteryStatus}
     * method invocation to notify result.
     *
     * @param status the battery status
     */
    public abstract void batteryStatusEvent(int status);

    /**
     * Invoked after a {@link PassiveReader#connect(String, android.content.Context)} method invocation
     * to notify failure.
     *
     * @param error error code
     */
    public abstract void connectionFailedEvent(int error);

    /**
     * Invoked after a {@link PassiveReader#connect(String, android.content.Context)} method invocation
     * to notify success.
     */
    public abstract void connectionSuccessEvent();

    /**
     * Invoked after a {@link PassiveReader#disconnect()} method invocation
     * to notify success.
     */
    public abstract void disconnectionSuccessEvent();

    /**
     * Invoked after a {@link PassiveReader#getFirmwareVersion()
     * getFirmwareVersion} method invocation to notify result.
     *
     * @param major the firmware version major number
     * @param minor the firmware version minor number
     */
    public abstract void firmwareVersionEvent(int major, int minor);

    /**
     * Invoked after a class {@code PassiveReader} method invocation to notify
     * result.
     *
     * @param command the command sent to the reader
     * @param error   the error code
     */
    public abstract void resultEvent(int command, int error);

    /**
     * Invoked after a {@link PassiveReader#getShutdownTime() getShutdownTime}
     * method invocation to notify result.
     *
     * @param time the shutdown time (seconds)
     */
    public abstract void shutdownTimeEvent(int time);

    /**
     * Invoked after a {@link PassiveReader#ISO15693tunnel(byte[]) ISO15693tunnel}
     * or {@link PassiveReader#ISO15693encryptedTunnel(byte, byte[])
     * ISO15693encryptedTunnel} method invocation to notify result.
     *
     * @param data command answer data
     */
    public abstract void tunnelEvent(byte data[]);
}
