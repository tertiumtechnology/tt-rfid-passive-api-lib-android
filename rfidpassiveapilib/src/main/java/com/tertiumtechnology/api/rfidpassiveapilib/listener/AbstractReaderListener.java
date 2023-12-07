/*
 * The MIT License
 *
 * Copyright 2021 Tertium Technology.
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
     * {@link PassiveReader#setSecurityLevel(int) setSecurityLevel} command.
     */
    public static final int SET_SECURITY_LEVEL_COMMAND = 28;
    /**
     * {@link PassiveReader#getSecurityLevel() getSecurityLevel} command.
     */
    public static final int GET_SECURITY_LEVEL_COMMAND = 29;
    /**
     * {@link PassiveReader#setName(String) setName} command.
     */
    public static final int SET_DEVICE_NAME_COMMAND = 30;
    /**
     * {@link PassiveReader#getName() getName} command.
     */
    public static final int GET_DEVICE_NAME_COMMAND = 31;
    /**
     * {@link PassiveReader#setAdvertisingInterval(int) setAdvertisingInterval} command.
     */
    public static final int SET_ADVERTISING_INTERVAL_COMMAND = 32;
    /**
     * {@link PassiveReader#getAdvertisingInterval() getAdvertisingInterval} command.
     */
    public static final int GET_ADVERTISING_INTERVAL_COMMAND = 33;
    /**
     * {@link PassiveReader#setBLEpower(int) setBLEpower} command.
     */
    public static final int SET_BLE_POWER_COMMAND = 34;
    /**
     * {@link PassiveReader#getBLEpower() getBLEpower} command.
     */
    public static final int GET_BLE_POWER_COMMAND = 35;
    /**
     * {@link PassiveReader#setConnectionInterval(float, float) setConnectionInterval} command.
     */
    public static final int SET_CONNECTION_INTERVAL_COMMAND = 36;
    /**
     * {@link PassiveReader#getConnectionInterval() getConnectionInterval} command.
     */
    public static final int GET_CONNECTION_INTERVAL_COMMAND = 37;
    /**
     * {@link PassiveReader#getConnectionIntervalAndMTU() getConnectionIntervalAndMTU} command.
     */
    public static final int GET_CONNECTION_INTERVAL_AND_MTU_COMMAND = 38;
    /**
     * {@link PassiveReader#getMACaddress() getMACaddress} command.
     */
    public static final int GET_MAC_ADDRESS_COMMAND = 39;
    /**
     * {@link PassiveReader#setSlaveLatency(int) setSlaveLatency} command.
     */
    public static final int SET_SLAVE_LATENCY_COMMAND = 40;
    /**
     * {@link PassiveReader#getSlaveLatency() getSlaveLatency} command.
     */
    public static final int GET_SLAVE_LATENCY_COMMAND = 41;
    /**
     * {@link PassiveReader#setSupervisionTimeout(int) setSupervisionTimeout} command.
     */
    public static final int SET_SUPERVISION_TIMEOUT_COMMAND = 42;
    /**
     * {@link PassiveReader#getSupervisionTimeout() getSupervisionTimeout} command.
     */
    public static final int GET_SUPERVISION_TIMEOUT_COMMAND = 43;
    /**
     * {@link PassiveReader#getBLEfirmwareVersion() getBLEfirmwareVersion} command.
     */
    public static final int GET_BLE_FIRMWARE_VERSION_COMMAND = 44;
    /**
     * {@link PassiveReader#readUserMemory(int) readUserMemory} command.
     */
    public static final int READ_USER_MEMORY_COMMAND = 45;
    /**
     * {@link PassiveReader#writeUserMemory(int, byte[]) writeUserMemory} command.
     */
    public static final int WRITE_USER_MEMORY_COMMAND = 46;
    /**
     * {@link PassiveReader#defaultSetup() defaultSetup} command.
     */
    public static final int DEFAULT_SETUP_COMMAND = 47;
    /**
     * {@link PassiveReader#reset(boolean) reset} command.
     */
    public static final int RESET_COMMAND = 48;
    /**
     * {@link PassiveReader#defaultBLEconfiguration(int, boolean) defaultBLEconfiguration} command.
     */
    public static final int DEFAULT_BLE_CONFIGURATION_COMMAND = 49;
    /**
     * {@link PassiveReader#getHMIsupport() getHMIsupport} command.
     */
    public static final int ZHAGA_GET_HMI_SUPPORT_COMMAND = 50;
    /**
     * {@link PassiveReader#setHMI(int, int, int, int, int, int, int, int, int, int, int) setHMI} command.
     */
    public static final int ZHAGA_SET_HMI_COMMAND = 51;
    /**
     * {@link PassiveReader#setRF(boolean) setRF} command.
     */
    public static final int ZHAGA_SET_RF_COMMAND = 52;
    /**
     * {@link PassiveReader#getRF() getRF} command.
     */
    public static final int ZHAGA_GET_RF_COMMAND = 53;
    /**
     * {@link PassiveReader#off() off} command.
     */
    public static final int ZHAGA_OFF_COMMAND = 54;
    /**
     * {@link PassiveReader#reboot() reboot} command.
     */
    public static final int ZHAGA_REBOOT_COMMAND = 55;
    /**
     * {@link PassiveReader#setSoundForInventory(int, int, int, int) setSoundForInventory} command.
     */
    public static final int ZHAGA_SET_INVENTORY_SOUND_COMMAND = 56;
    /**
     * {@link PassiveReader#getSoundForInventory() getSoundForInventory} command.
     */
    public static final int ZHAGA_GET_INVENTORY_SOUND_COMMAND = 57;
    /**
     * {@link PassiveReader#setSoundForCommand(int, int, int, int) setSoundForCommand} command.
     */
    public static final int ZHAGA_SET_COMMAND_SOUND_COMMAND = 58;
    /**
     * {@link PassiveReader#getSoundForCommand() getSoundForCommand} command.
     */
    public static final int ZHAGA_GET_COMMAND_SOUND_COMMAND = 59;
    /**
     * {@link PassiveReader#setSoundForError(int, int, int, int) setSoundForError} command.
     */
    public static final int ZHAGA_SET_ERROR_SOUND_COMMAND = 60;
    /**
     * {@link PassiveReader#getSoundForError() getSoundForError} command.
     */
    public static final int ZHAGA_GET_ERROR_SOUND_COMMAND = 61;
    /**
     * {@link PassiveReader#setLEDforInventory(int, int, int, int) setLEDforInventory} command.
     */
    public static final int ZHAGA_SET_INVENTORY_LED_COMMAND = 62;
    /**
     * {@link PassiveReader#getLEDforInventory() getLEDforInventory} command.
     */
    public static final int ZHAGA_GET_INVENTORY_LED_COMMAND = 63;
    /**
     * {@link PassiveReader#setLEDforCommand(int, int, int, int) setLEDforCommand} command.
     */
    public static final int ZHAGA_SET_COMMAND_LED_COMMAND = 64;
    /**
     * {@link PassiveReader#getLEDforCommand() getLEDforCommand} command.
     */
    public static final int ZHAGA_GET_COMMAND_LED_COMMAND = 65;
    /**
     * {@link PassiveReader#setLEDforError(int, int, int, int) setLEDforError} command.
     */
    public static final int ZHAGA_SET_ERROR_LED_COMMAND = 66;
    /**
     * {@link PassiveReader#getLEDforError() getLEDforError} command.
     */
    public static final int ZHAGA_GET_ERROR_LED_COMMAND = 67;
    /**
     * {@link PassiveReader#setVibrationForInventory(int, int, int) setVibrationForInventory} command.
     */
    public static final int ZHAGA_SET_INVENTORY_VIBRATION_COMMAND = 68;
    /**
     * {@link PassiveReader#getVibrationForInventory() getVibrationForInventory} command.
     */
    public static final int ZHAGA_GET_INVENTORY_VIBRATION_COMMAND = 69;
    /**
     * {@link PassiveReader#setVibrationForCommand(int, int, int) setVibrationForCommand} command.
     */
    public static final int ZHAGA_SET_COMMAND_VIBRATION_COMMAND = 70;
    /**
     * {@link PassiveReader#getVibrationForCommand() getVibrationForCommand} command.
     */
    public static final int ZHAGA_GET_COMMAND_VIBRATION_COMMAND = 71;
    /**
     * {@link PassiveReader#setVibrationForError(int, int, int) setVibrationForError} command.
     */
    public static final int ZHAGA_SET_ERROR_VIBRATION_COMMAND = 72;
    /**
     * {@link PassiveReader#getVibrationForError() getVibrationForError} command.
     */
    public static final int ZHAGA_GET_ERROR_VIBRATION_COMMAND = 73;
    /**
     * {@link PassiveReader#activateButton(int) activateButton} command.
     */
    public static final int ZHAGA_ACTIVATE_BUTTON_COMMAND = 74;
    /**
     * {@link PassiveReader#getActivatedButton() getActivatedButton} command.
     */
    public static final int ZHAGA_GET_ACTIVATED_BUTTON_COMMAND = 75;
    /**
     * {@link PassiveReader#setRFonOff(int, int, int) setRFonOff} command.
     */
    public static final int ZHAGA_SET_RF_ONOFF_COMMAND = 76;
    /**
     * {@link PassiveReader#getRFonOff() getRFonOff} command.
     */
    public static final int ZHAGA_GET_RF_ONOFF_COMMAND = 77;
    /**
     * {@link PassiveReader#setAutoOff(int) setAutoOff} command.
     */
    public static final int ZHAGA_SET_AUTOOFF_COMMAND = 78;
    /**
     * {@link PassiveReader#getAutoOff() getAutoOff} command.
     */
    public static final int ZHAGA_GET_AUTOOFF_COMMAND = 79;
    /**
     * {@link PassiveReader#defaultConfiguration() defaultConfiguration} command.
     */
    public static final int ZHAGA_DEFAULT_CONFIG_COMMAND = 80;
    /**
     * {@link PassiveReader#transparent(byte[]) transparent} command.
     */
    public static final int ZHAGA_TRANSPARENT_COMMAND = 81;

    /**
     * {@link PassiveReader#setInventoryFormat(int) setInventoryFormat} command.
     */
    public static final int SET_INVENTORY_FORMAT_COMMAND = 82;

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
     * Reader driver command answer mismatch error.
     */
    public static final int READER_DRIVER_COMMAND_ANSWER_MISMATCH_ERROR = 0x26;
    /**
     * Reader driver change-mode error.
     */
    public static final int READER_DRIVER_COMMAND_CHANGE_MODE_ERROR = 0x27;
    /**
     * Reader command mode answer error.
     */
    public static final int READER_DRIVER_COMMAND_CMD_MODE_ANSWER_ERROR = 0x28;
    /**
     * Reader set-mode error: BLE device error.
     */
    public static final int READER_SET_MODE_BLE_DEVICE_ERROR = 0x29;
    /**
     * Reader set-mode error: invalid MODE characteristic.
     */
    public static final int READER_SET_MODE_INVALID_CHARACTERISTIC_ERROR = 0x2A;
    /**
     * Reader set-mode error: previous operation in progress.
     */
    public static final int READER_SET_MODE_OPERATION_IN_PROGRESS_ERROR = 0x2B;
    /**
     * Reader answer wrong format error code.
     */
    public static final int READER_ANSWER_WRONG_FORMAT_ERROR = 0x2C;

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

    /**
     * BLE advertising -40dBm TX power
     */
    public static final int BLE_TX_POWER_MINUS_40_DBM = 0x00;
    /**
     * BLE advertising -20dBm TX power
     */
    public static final int BLE_TX_POWER_MINUS_20_DBM = 0x01;
    /**
     * BLE advertising -16dBm TX power
     */
    public static final int BLE_TX_POWER_MINUS_16_DBM = 0x02;
    /**
     * BLE advertising -12dBm TX power
     */
    public static final int BLE_TX_POWER_MINUS_12_DBM = 0x03;
    /**
     * BLE advertising -8dBm TX power
     */
    public static final int BLE_TX_POWER_MINUS_8_DBM = 0x04;
    /**
     * BLE advertising -4dBm TX power
     */
    public static final int BLE_TX_POWER_MINUS_4_DBM = 0x05;
    /**
     * BLE advertising 0dBm TX power
     */
    public static final int BLE_TX_POWER_0_DBM = 0x06;
    /**
     * BLE advertising +2dBm TX power
     */
    public static final int BLE_TX_POWER_2_DBM = 0x07;
    /**
     * BLE advertising +3dBm TX power
     */
    public static final int BLE_TX_POWER_3_DBM = 0x08;
    /**
     * BLE advertising +4dBm TX power
     */
    public static final int BLE_TX_POWER_4_DBM = 0x09;
    /**
     * BLE advertising +5dBm TX power
     */
    public static final int BLE_TX_POWER_5_DBM = 0x0A;
    /**
     * BLE advertising +6dBm TX power
     */
    public static final int BLE_TX_POWER_6_DBM = 0x0B;
    /**
     * BLE advertising +7dBm TX power
     */
    public static final int BLE_TX_POWER_7_DBM = 0x0C;
    /**
     * BLE advertising +8dBm TX power
     */
    public static final int BLE_TX_POWER_8_DBM = 0x0D;

    /**
     * Invoked after a {@link PassiveReader#getBLEfirmwareVersion()
     * getBLEfirmwareVersion} method invocation to notify result.
     *
     * @param major the BLE MCU firmware version major number
     * @param minor the BLE MCU firmware version minor number
     */
    public abstract void BLEfirmwareVersionEvent(int major, int minor);

    /**
     * Invoked after a {@link PassiveReader#getBLEpower() getBLEpower}
     * method invocation to notify result.
     *
     * @param BLE_power the BLE advertising TX power
     */
    public abstract void BLEpowerEvent(int BLE_power);

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
     * Invoked after a {@link PassiveReader#getMACaddress() getMACaddress}
     * method invocation to notify result.
     *
     * @param MAC_address the BLE device MAC address (6-byte array)
     */
    public abstract void MACaddressEvent(byte MAC_address[]);

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
     * Invoked after a {@link PassiveReader#getAdvertisingInterval() getAdvertisingInterval}
     * method invocation to notify result.
     *
     * @param advertising_interval the BLE advertising interval (ms)
     */
    public abstract void advertisingIntervalEvent(int advertising_interval);

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
     * Invoked after a {@link PassiveReader#getConnectionIntervalAndMTU() getConnectionIntervalAndMTU}
     * method invocation to notify result.
     *
     * @param connection_interval the BLE negoziated connection interval value (ms)
     * @param MTU                 the BLE negoziated MTU (byte)
     */
    public abstract void connectionIntervalAndMTUevent(float connection_interval, int MTU);

    /**
     * Invoked after a {@link PassiveReader#getConnectionInterval() getConnectionInterval}
     * method invocation to notify result.
     *
     * @param min_interval the BLE connection interval minimum value (ms)
     * @param max_interval the BLE connection interval maximum value (ms)
     */
    public abstract void connectionIntervalEvent(float min_interval, float max_interval);

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
     * Invoked after a {@link PassiveReader#getName() getName}
     * method invocation to notify result.
     *
     * @param device_name the reader name
     */
    public abstract void nameEvent(String device_name);

    /**
     * Invoked after a class {@code PassiveReader} method invocation to notify
     * result.
     *
     * @param command the command sent to the reader
     * @param error   the error code
     */
    public abstract void resultEvent(int command, int error);

    /**
     * Invoked after a {@link PassiveReader#getSecurityLevel() getSecurityLevel}
     * method invocation to notify result.
     *
     * @param level the current security level
     */
    public abstract void securityLevelEvent(int level);

    /**
     * Invoked after a {@link PassiveReader#getShutdownTime() getShutdownTime}
     * method invocation to notify result.
     *
     * @param time the shutdown time (seconds)
     */
    public abstract void shutdownTimeEvent(int time);

    /**
     * Invoked after a {@link PassiveReader#getSlaveLatency() getSlaveLatency}
     * method invocation to notify result.
     *
     * @param slave_latency the BLE slave latency value
     */
    public abstract void slaveLatencyEvent(int slave_latency);

    /**
     * Invoked after a {@link PassiveReader#getSupervisionTimeout() getSupervisionTimeout}
     * method invocation to notify result.
     *
     * @param supervision_timeout the BLE supervision timeout value (ms)
     */
    public abstract void supervisionTimeoutEvent(int supervision_timeout);

    /**
     * Invoked after a {@link PassiveReader#ISO15693tunnel(byte[]) ISO15693tunnel}
     * or {@link PassiveReader#ISO15693encryptedTunnel(byte, byte[])
     * ISO15693encryptedTunnel} method invocation to notify result.
     *
     * @param data command answer data
     */
    public abstract void tunnelEvent(byte data[]);

    /**
     * Invoked after a {@link PassiveReader#readUserMemory(int) readUserMemory}
     * method invocation to notify result.
     *
     * @param data_block the user memory data-block (64-byte array)
     */
    public abstract void userMemoryEvent(byte data_block[]);
}
