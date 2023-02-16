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

package com.tertiumtechnology.api.rfidpassiveapilib;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.tertiumtechnology.api.rfidpassiveapilib.listener.AbstractReaderListener;
import com.tertiumtechnology.api.rfidpassiveapilib.listener.AbstractZhagaListener;
import com.tertiumtechnology.api.rfidpassiveapilib.util.BleSettings;

/**
 * Interface for instance(s) of {@code PassiveReader} limited to Zhaga standard protocol.
 * <p>
 * A concrete instance of {@code ZhagaReader} is created calling
 * {@link PassiveReader#getZhagaReaderInstance(AbstractZhagaListener, BluetoothAdapter, BleSettings)}
 * getZhagaReaderInstance} static method..
 */
public interface ZhagaReader {

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
     * Not reset BLE configuration.
     */
    public static final int BLE_CONFIGURATION_UNCHANGED = 0x00;
    /**
     * Reset to default BLE configuration.
     */
    public static final int BLE_DEFAULT_CONFIGURATION = 0x01;
    /**
     * Reset to default BLE configuration excluding device name.
     */
    public static final int BLE_DEFAULT_CONFIGURATION_EXCLUDING_NAME = 0x02;

    /**
     * LED color RED
     */
    public static final int LED_RED = 0x01;
    /**
     * LED color GREEN
     */
    public static final int LED_GREEN = 0x02;
    /**
     * LED color BLUE
     */
    public static final int LED_BLUE = 0x04;
    /**
     * LED color CYAN
     */
    public static final int LED_CYAN = 0x08;
    /**
     * LED color MAGENTA
     */
    public static final int LED_MAGENTA = 0x10;
    /**
     * LED color YELLOW
     */
    public static final int LED_YELLOW = 0x20;
    /**
     * LED color WHITE
     */
    public static final int LED_WHITE = 0x40;

    /**
     * Reader device activated button #1
     */
    public static final int ACTIVE_BUTTON_1 = 0x01;
    /**
     * Reader device activated button #2
     */
    public static final int ACTIVE_BUTTON_2 = 0x02;
    /**
     * Reader device activated button #3
     */
    public static final int ACTIVE_BUTTON_3 = 0x04;
    /**
     * Reader device activated button #4
     */
    public static final int ACTIVE_BUTTON_4 = 0x08;
    /**
     * Reader device activated button #5
     */
    public static final int ACTIVE_BUTTON_5 = 0x10;
    /**
     * Reader device activated button #6
     */
    public static final int ACTIVE_BUTTON_6 = 0x20;
    /**
     * Reader device activated button #7
     */
    public static final int ACTIVE_BUTTON_7 = 0x40;
    /**
     * Reader device activated button #8
     */
    public static final int ACTIVE_BUTTON_8 = 0x80;

    /**
     * Reader device button(s) activation.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     * <p>
     * Only activated buttons generate events
     *
     * @param activated_button button(s) to activate
     */
    public abstract void activateButton(int activated_button);

    /**
     * Close the reader driver.
     */
    public abstract void close();

    /**
     * Connect the reader device via BLE link.
     *
     * @param reader_address the reader device address
     */
    public void connect(String reader_address, Context context);

    /**
     * Reset the reader device to BLE factory default configuration.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} or via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     * <p>
     * The new configuration will be active after a reader device reset or power off/on cycle
     *
     * @param mode          reset BLE configuration mode (0: reset none, 1: reset all, 2: reset all except device name)
     * @param erase_bonding erase bonding list of BLE devices
     */
    public abstract void defaultBLEconfiguration(int mode, boolean erase_bonding);

    /**
     * Reset to default configuratin.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent}
     * method invocation.
     */
    public abstract void defaultConfiguration();

    /**
     * Disconnect the BLE link with reader device.
     */
    public abstract void disconnect();

    /**
     * Get reader device activated button(s).
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractZhagaListener#activatedButtonEvent(int) activatedButtonEvent}
     * methods invocation.
     * <p>
     * Only activated buttons generate events
     */
    public abstract void getActivatedButton();

    /**
     * Get reader device auto-OFF time.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractZhagaListener#autoOffEvent(int) autoOffEvent}
     * methods invocation.
     */
    public abstract void getAutoOff();

    /**
     * Get Human-Machine Interface supported features of the reader device.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractZhagaListener#HMIevent(int, int, int) HMIevent} methods
     * invocation.
     */
    public abstract void getHMIsupport();

    /**
     * Get LED parameters for successfull general command.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractZhagaListener#LEDforCommandEvent(int, int, int, int) LEDforCommandEvent}
     * methods invocation.
     */
    public abstract void getLEDforCommand();

    /**
     * Get LED parameters for error condition.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractZhagaListener#LEDforErrorEvent(int, int, int, int) LEDforErrorEvent}
     * methods invocation.
     */
    public abstract void getLEDforError();

    /**
     * Get LED parameters for successfull inventory operation.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractZhagaListener#LEDforInventoryEvent(int, int, int, int) LEDforInventoryEvent}
     * methods invocation.
     */
    public abstract void getLEDforInventory();

    /**
     * Get the reader device name.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractReaderListener#nameEvent(String) nameEvent} or via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractZhagaListener#nameEvent(String) nameEvent}methods
     * invocation.
     */
    public abstract void getName();

    /**
     * Get the RF settings for reader device.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractZhagaListener#RFevent(boolean) RFvent}
     * methods invocation.
     */
    public abstract void getRF();

    /**
     * Get RF on/off settings.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractZhagaListener#RFonOffEvent(int, int, int) RFonOffEvent}
     * methods invocation.
     */
    public abstract void getRFonOff();

    /**
     * Get the reader device security level.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractReaderListener#securityLevelEvent(int) securityLevelEvent} or via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractZhagaListener#securityLevelEvent(int) securityLevelEvent} methods
     * invocation.
     */
    public abstract void getSecurityLevel();

    /**
     * Get sound parameters for successfull general command.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractZhagaListener#soundForCommandEvent(int, int, int, int) soundForCommandEvent}
     * methods invocation.
     */
    public abstract void getSoundForCommand();

    /**
     * Get sound parameters for error condition.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractZhagaListener#soundForErrorEvent(int, int, int, int) soundForErrorEvent}
     * methods invocation.
     */
    public abstract void getSoundForError();

    /**
     * Get sound parameters for successfull inventory operation.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractZhagaListener#soundForInventoryEvent(int, int, int, int) soundForInventoryEvent}
     * methods invocation.
     */
    public abstract void getSoundForInventory();

    /**
     * Get vibration parameters for successfull general command.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractZhagaListener#vibrationForCommandEvent(int, int, int) vibrationForCommandEvent}
     * methods invocation.
     */
    public abstract void getVibrationForCommand();

    /**
     * Get vibration parameters for error condition.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractZhagaListener#vibrationForErrorEvent(int, int, int) vibrationForErrorEvent}
     * methods invocation.
     */
    public abstract void getVibrationForError();

    /**
     * Get vibration parameters for successfull inventory operation.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} and {@link
     * AbstractZhagaListener#vibrationForInventoryEvent(int, int, int) vibrationForInventoryEvent}
     * methods invocation.
     */
    public abstract void getVibrationForInventory();

    /**
     * Test the BLE link with reader device.
     *
     * @param device_address the sound starting frequency (Hertz: 40-20000)
     * @return true if the reader device is linked by BLE
     */
    public abstract boolean isAvailable(String device_address, Context context);

    /**
     * Power-off the reader device.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     */
    public abstract void off();

    /**
     * Reboot the reader device.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     */
    public abstract void reboot();

    /**
     * Reset the reader device.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} or via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     *
     * @param bootloader enter FUOTA (Firmware Update On The Air) mode
     */
    public abstract void reset(boolean bootloader);

    /**
     * Setup reader device auto-OFF.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     *
     * @param OFF_time auto-OFF time (0-65535 s)
     */
    public abstract void setAutoOff(int OFF_time);

    /**
     * Control Human-Machine Interface of the reader device.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     *
     * @param sound_frequency      sound frequency (40-20000 Hz)
     * @param sound_on_time        duration of sound (0-2550 ms)
     * @param sound_off_time       duration of sound interval (0-2550 ms)
     * @param sound_repetition     number of repetition (0-255, 0 = NO suond)
     * @param light_color          LED color
     * @param light_on_time        duration of light (0-2550 ms)
     * @param light_off_time       duration of light interval (0-2550 ms)
     * @param light_repetition     number of repetition (0-255, 0 = NO light)
     * @param vibration_on_time    duration of vibration (0-2550 ms)
     * @param vibration_off_time   duration of vibration interval (0-2550 ms)
     * @param vibration_repetition number of repetition (0-255, 0 = NO vibration)
     */
    public abstract void setHMI(int sound_frequency, int sound_on_time, int sound_off_time, int sound_repetition,
                                int light_color, int light_on_time, int light_off_time, int light_repetition,
                                int vibration_on_time, int vibration_off_time, int vibration_repetition);

    /**
     * Setup LED parameters for successfull general command.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     *
     * @param light_color      LED color
     * @param light_on_time    duration of light (0-2550 ms)
     * @param light_off_time   duration of light interval (0-2550 ms)
     * @param light_repetition number of repetition (0-255, 0 = NO light)
     */
    public abstract void setLEDforCommand(int light_color, int light_on_time, int light_off_time, int light_repetition);

    /**
     * Setup LED parameters for error condition.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     *
     * @param light_color      LED color
     * @param light_on_time    duration of light (0-2550 ms)
     * @param light_off_time   duration of light interval (0-2550 ms)
     * @param light_repetition number of repetition (0-255, 0 = NO light)
     */
    public abstract void setLEDforError(int light_color, int light_on_time, int light_off_time, int light_repetition);

    /**
     * Setup LED parameters for successfull inventory operation.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     *
     * @param light_color      LED color
     * @param light_on_time    duration of light (0-2550 ms)
     * @param light_off_time   duration of light interval (0-2550 ms)
     * @param light_repetition number of repetition (0-255, 0 = NO light)
     */
    public abstract void setLEDforInventory(int light_color, int light_on_time, int light_off_time,
                                            int light_repetition);

    /**
     * Set the reader device name.
     * <p>
     * Response to the command received via {@link
     * AbstractReaderListener#resultEvent(int, int) resultEvent} or via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     * <p>
     * The new configuration will be active after a reader device reset or power off/on cycle
     *
     * @param device_name the reader name
     */
    public abstract void setName(String device_name);

    /**
     * Set the RF permanently ON or under automatic control by reader device.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     *
     * @param RF_on set RF permanently ON
     */
    public abstract void setRF(boolean RF_on);

    /**
     * Setup RF on/off settings.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     *
     * @param RF_power            RF power (0-100 %)
     * @param RF_off_timeout      timeout to switch off RF (0-65535 ms)
     * @param RF_on_preactivation time of RF preactivation (0-65535 ms)
     */
    public abstract void setRFonOff(int RF_power, int RF_off_timeout, int RF_on_preactivation);

    /**
     * Set the reader device security level.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} or via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     * The new security level will be set after a power off/on cycle of the
     * reader device.
     *
     * @param level the new security level
     */
    public abstract void setSecurityLevel(int level);

    /**
     * Setup sound parameters for successfull general command.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     *
     * @param sound_frequency  sound frequency (40-20000 Hz)
     * @param sound_on_time    duration of sound (0-2550 ms)
     * @param sound_off_time   duration of sound interval (0-2550 ms)
     * @param sound_repetition number of repetition (0-255, 0 = NO suond)
     */
    public abstract void setSoundForCommand(int sound_frequency, int sound_on_time, int sound_off_time,
                                            int sound_repetition);

    /**
     * Setup sound parameters for error condition.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     *
     * @param sound_frequency  sound frequency (40-20000 Hz)
     * @param sound_on_time    duration of sound (0-2550 ms)
     * @param sound_off_time   duration of sound interval (0-2550 ms)
     * @param sound_repetition number of repetition (0-255, 0 = NO suond)
     */
    public abstract void setSoundForError(int sound_frequency, int sound_on_time, int sound_off_time,
                                          int sound_repetition);

    /**
     * Setup sound parameters for successfull inventory operation.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     *
     * @param sound_frequency  sound frequency (40-20000 Hz)
     * @param sound_on_time    duration of sound (0-2550 ms)
     * @param sound_off_time   duration of sound interval (0-2550 ms)
     * @param sound_repetition number of repetition (0-255, 0 = NO suond)
     */
    public abstract void setSoundForInventory(int sound_frequency, int sound_on_time, int sound_off_time,
                                              int sound_repetition);

    /**
     * Setup vibration parameters for successfull general command.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     *
     * @param vibration_on_time    duration of vibration (0-2550 ms)
     * @param vibration_off_time   duration of vibration interval (0-2550 ms)
     * @param vibration_repetition number of repetition (0-255, 0 = NO vibration)
     */
    public abstract void setVibrationForCommand(int vibration_on_time, int vibration_off_time,
                                                int vibration_repetition);

    /**
     * Setup vibration parameters for error condition.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     *
     * @param vibration_on_time    duration of vibration (0-2550 ms)
     * @param vibration_off_time   duration of vibration interval (0-2550 ms)
     * @param vibration_repetition number of repetition (0-255, 0 = NO vibration)
     */
    public abstract void setVibrationForError(int vibration_on_time, int vibration_off_time, int vibration_repetition);

    /**
     * Setup vibration parameters for successfull inventory operation.
     * <p>
     * Response to the command received via {@link
     * AbstractZhagaListener#resultEvent(int, int) resultEvent} method invocation.
     *
     * @param vibration_on_time    duration of vibration (0-2550 ms)
     * @param vibration_off_time   duration of vibration interval (0-2550 ms)
     * @param vibration_repetition number of repetition (0-255, 0 = NO vibration)
     */
    public abstract void setVibrationForInventory(int vibration_on_time, int vibration_off_time,
                                                  int vibration_repetition);

    /**
     * Start Zhaga transparent operation.
     * <p>
     * In transparent operation the command bytes are in stripped ISO15693 format.
     * <p>
     * The result of the transparent operation is notified invoking Zhaga listener
     * method {@link AbstractZhagaListener#transparentEvent(byte[]) transparentEvent}.
     *
     * @param command the command to send to the tag
     */
    public abstract void transparent(byte[] command);
}
