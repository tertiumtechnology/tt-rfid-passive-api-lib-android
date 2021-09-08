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

import com.tertiumtechnology.api.rfidpassiveapilib.ZhagaReader;

/**
 * Listener template for event generated in response to a {@code ZhagaReader}
 * method invocation.
 * <p>
 * A concrete instance of {@code AbstractZhagaListener} has to set for the
 * instance of the class {@code PassiveReader} to receive notification about
 * methods invocation.
 */
public abstract class AbstractZhagaListener {

    /**
     * {@link ZhagaReader#setSecurityLevel(int) setSecurityLevel} command.
     */
    public static final int SET_SECURITY_LEVEL_COMMAND = 28;
    /**
     * {@link ZhagaReader#getSecurityLevel() getSecurityLevel} command.
     */
    public static final int GET_SECURITY_LEVEL_COMMAND = 29;
    /**
     * {@link ZhagaReader#setName(String) setName} command.
     */
    public static final int SET_DEVICE_NAME_COMMAND = 30;
    /**
     * {@link ZhagaReader#getName() getName} command.
     */
    public static final int GET_DEVICE_NAME_COMMAND = 31;
    /**
     * {@link ZhagaReader#reset(boolean) reset} command.
     */
    public static final int RESET_COMMAND = 48;
    /**
     * {@link ZhagaReader#defaultBLEconfiguration(int, boolean) defaultBLEconfiguration} command.
     */
    public static final int DEFAULT_BLE_CONFIGURATION_COMMAND = 49;
    /**
     * {@link ZhagaReader#getHMIsupport() getHMIsupport} command.
     */
    public static final int ZHAGA_GET_HMI_SUPPORT_COMMAND = 50;
    /**
     * {@link ZhagaReader#setHMI(int, int, int, int, int, int, int, int, int, int, int) setHMI} command.
     */
    public static final int ZHAGA_SET_HMI_COMMAND = 51;
    /**
     * {@link ZhagaReader#setRF(boolean) setRF} command.
     */
    public static final int ZHAGA_SET_RF_COMMAND = 52;
    /**
     * {@link ZhagaReader#getRF() getRF} command.
     */
    public static final int ZHAGA_GET_RF_COMMAND = 53;
    /**
     * {@link ZhagaReader#off() off} command.
     */
    public static final int ZHAGA_OFF_COMMAND = 54;
    /**
     * {@link ZhagaReader#reboot() reboot} command.
     */
    public static final int ZHAGA_REBOOT_COMMAND = 55;
    /**
     * {@link ZhagaReader#setSoundForInventory(int, int, int, int) setSoundForInventory} command.
     */
    public static final int ZHAGA_SET_INVENTORY_SOUND_COMMAND = 56;
    /**
     * {@link ZhagaReader#getSoundForInventory() getSoundForInventory} command.
     */
    public static final int ZHAGA_GET_INVENTORY_SOUND_COMMAND = 57;
    /**
     * {@link ZhagaReader#setSoundForCommand(int, int, int, int) setSoundForCommand} command.
     */
    public static final int ZHAGA_SET_COMMAND_SOUND_COMMAND = 58;
    /**
     * {@link ZhagaReader#getSoundForCommand() getSoundForCommand} command.
     */
    public static final int ZHAGA_GET_COMMAND_SOUND_COMMAND = 59;
    /**
     * {@link ZhagaReader#setSoundForError(int, int, int, int) setSoundForError} command.
     */
    public static final int ZHAGA_SET_ERROR_SOUND_COMMAND = 60;
    /**
     * {@link ZhagaReader#getSoundForError() getSoundForError} command.
     */
    public static final int ZHAGA_GET_ERROR_SOUND_COMMAND = 61;
    /**
     * {@link ZhagaReader#setLEDforInventory(int, int, int, int) setLEDforInventory} command.
     */
    public static final int ZHAGA_SET_INVENTORY_LED_COMMAND = 62;
    /**
     * {@link ZhagaReader#getLEDforInventory() getLEDforInventory} command.
     */
    public static final int ZHAGA_GET_INVENTORY_LED_COMMAND = 63;
    /**
     * {@link ZhagaReader#setLEDforCommand(int, int, int, int) setLEDforCommand} command.
     */
    public static final int ZHAGA_SET_COMMAND_LED_COMMAND = 64;
    /**
     * {@link ZhagaReader#getLEDforCommand() getLEDforCommand} command.
     */
    public static final int ZHAGA_GET_COMMAND_LED_COMMAND = 65;
    /**
     * {@link ZhagaReader#setLEDforError(int, int, int, int) setLEDforError} command.
     */
    public static final int ZHAGA_SET_ERROR_LED_COMMAND = 66;
    /**
     * {@link ZhagaReader#getLEDforError() getLEDforError} command.
     */
    public static final int ZHAGA_GET_ERROR_LED_COMMAND = 67;
    /**
     * {@link ZhagaReader#setVibrationForInventory(int, int, int) setVibrationForInventory} command.
     */
    public static final int ZHAGA_SET_INVENTORY_VIBRATION_COMMAND = 68;
    /**
     * {@link ZhagaReader#getVibrationForInventory() getVibrationForInventory} command.
     */
    public static final int ZHAGA_GET_INVENTORY_VIBRATION_COMMAND = 69;
    /**
     * {@link ZhagaReader#setVibrationForCommand(int, int, int) setVibrationForCommand} command.
     */
    public static final int ZHAGA_SET_COMMAND_VIBRATION_COMMAND = 70;
    /**
     * {@link ZhagaReader#getVibrationForCommand() getVibrationForCommand} command.
     */
    public static final int ZHAGA_GET_COMMAND_VIBRATION_COMMAND = 71;
    /**
     * {@link ZhagaReader#setVibrationForError(int, int, int) setVibrationForError} command.
     */
    public static final int ZHAGA_SET_ERROR_VIBRATION_COMMAND = 72;
    /**
     * {@link ZhagaReader#getVibrationForError() getVibrationForError} command.
     */
    public static final int ZHAGA_GET_ERROR_VIBRATION_COMMAND = 73;
    /**
     * {@link ZhagaReader#activateButton(int) activateButton} command.
     */
    public static final int ZHAGA_ACTIVATE_BUTTON_COMMAND = 74;
    /**
     * {@link ZhagaReader#getActivatedButton() getActivatedButton} command.
     */
    public static final int ZHAGA_GET_ACTIVATED_BUTTON_COMMAND = 75;
    /**
     * {@link ZhagaReader#setRFonOff(int, int, int) setRFonOff} command.
     */
    public static final int ZHAGA_SET_RF_ONOFF_COMMAND = 76;
    /**
     * {@link ZhagaReader#getRFonOff() getRFonOff} command.
     */
    public static final int ZHAGA_GET_RF_ONOFF_COMMAND = 77;
    /**
     * {@link ZhagaReader#setAutoOff(int) setAutoOff} command.
     */
    public static final int ZHAGA_SET_AUTOOFF_COMMAND = 78;
    /**
     * {@link ZhagaReader#getAutoOff() getAutoOff} command.
     */
    public static final int ZHAGA_GET_AUTOOFF_COMMAND = 79;
    /**
     * {@link ZhagaReader#defaultConfiguration() defaultConfiguration} command.
     */
    public static final int ZHAGA_DEFAULT_CONFIG_COMMAND = 80;
    /**
     * {@link ZhagaReader#transparent(byte[]) transparent} command.
     */
    public static final int ZHAGA_TRANSPARENT_COMMAND = 81;

    /**
     * Successful command code (no error).
     */
    public static final int NO_ERROR = 0x00;
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
     * Reader driver unknow command error.
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
     * Reader device button #1
     */
    public static final int BUTTON_1 = 0x01;
    /**
     * Reader device button #2
     */
    public static final int BUTTON_2 = 0x02;
    /**
     * Reader device button #3
     */
    public static final int BUTTON_3 = 0x03;
    /**
     * Reader device button #4
     */
    public static final int BUTTON_4 = 0x04;
    /**
     * Reader device button #5
     */
    public static final int BUTTON_5 = 0x05;
    /**
     * Reader device button #6
     */
    public static final int BUTTON_6 = 0x06;
    /**
     * Reader device button #7
     */
    public static final int BUTTON_7 = 0x07;
    /**
     * Reader device button #8
     */
    public static final int BUTTON_8 = 0x08;

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
     * Buzzer and vibration device NOT supported.
     */
    public static final int NO_BUZZER_NO_VIBRATION = 0x00;
    /**
     * Buzzer supported and vibration device NOT supported.
     */
    public static final int BUZZER_BUT_NO_VIBRATION = 0x01;
    /**
     * Buzzer NOT supported and vibration device supported.
     */
    public static final int NO_BUZZER_BUT_VIBRATION = 0x02;
    /**
     * Buzzer and vibration device supported.
     */
    public static final int BUZZER_AND_VIBRATION = 0x03;

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
     * Invoked after a {@link ZhagaReader#getHMIsupport() getHMIsupport}
     * method invocation to notify result.
     *
     * @param LED_color       the color(s) supported by reader device LED
     * @param sound_vibration the sound and/or vibration capabilities of reader device
     * @param button_number   the number of button(s) of the reader device
     */
    public abstract void HMIevent(int LED_color, int sound_vibration, int button_number);

    /**
     * Invoked after a {@link ZhagaReader#getLEDforCommand() getLEDforCommand}
     * method invocation to notify result.
     *
     * @param light_color      LED color
     * @param light_on_time    duration of light (0-2550 ms)
     * @param light_off_time   duration of light interval (0-2550 ms)
     * @param light_repetition number of repetition (0-255, 0 = NO light)
     */
    public abstract void LEDforCommandEvent(int light_color, int light_on_time, int light_off_time,
                                            int light_repetition);

    /**
     * Invoked after a {@link ZhagaReader#getLEDforError() getLEDforError}
     * method invocation to notify result.
     *
     * @param light_color      LED color
     * @param light_on_time    duration of light (0-2550 ms)
     * @param light_off_time   duration of light interval (0-2550 ms)
     * @param light_repetition number of repetition (0-255, 0 = NO light)
     */
    public abstract void LEDforErrorEvent(int light_color, int light_on_time, int light_off_time, int light_repetition);

    /**
     * Invoked after a {@link ZhagaReader#getLEDforInventory() getLEDforInventory}
     * method invocation to notify result.
     *
     * @param light_color      LED color
     * @param light_on_time    duration of light (0-2550 ms)
     * @param light_off_time   duration of light interval (0-2550 ms)
     * @param light_repetition number of repetition (0-255, 0 = NO light)
     */
    public abstract void LEDforInventoryEvent(int light_color, int light_on_time, int light_off_time,
                                              int light_repetition);

    /**
     * Invoked after a {@link ZhagaReader#getRF() getRF}
     * method invocation to notify result.
     *
     * @param RF_on RF permanently set ON
     */
    public abstract void RFevent(boolean RF_on);

    /**
     * Invoked after a {@link ZhagaReader#getRFonOff() getRFonOff}
     * method invocation to notify result.
     *
     * @param RF_power            RF power (0-100 %)
     * @param RF_off_timeout      timeout to switch off RF (0-65535 ms)
     * @param RF_on_preactivation time of RF preactivation (0-65535 ms)
     */
    public abstract void RFonOffEvent(int RF_power, int RF_off_timeout, int RF_on_preactivation);

    /**
     * Invoked after a {@link ZhagaReader#getActivatedButton() getActivatedButton}
     * method invocation to notify result.
     *
     * @param activated_button activated button(s)
     */
    public abstract void activatedButtonEvent(int activated_button);

    /**
     * Invoked after a {@link ZhagaReader#getAutoOff() getAutoOff}
     * method invocation to notify result.
     *
     * @param OFF_time auto-OFF time (0-65535 s)
     */
    public abstract void autoOffEvent(int OFF_time);

    /**
     * Invoked asynchronously to detail a reader device button event.
     *
     * @param button the reader device button pressed (1-8)
     * @param time   the time that the button has been pressed (ms)
     */
    public abstract void buttonEvent(int button, int time);

    /**
     * Invoked after a {@link ZhagaReader#connect(String, android.content.Context)} method invocation
     * to notify failure.
     *
     * @param error error code
     */
    public abstract void connectionFailedEvent(int error);

    /**
     * Invoked after a {@link ZhagaReader#connect(String, android.content.Context)} method invocation
     * to notify success.
     */
    public abstract void connectionSuccessEvent();

    /**
     * Invoked asynchronously to signal a reader device event.
     *
     * @param event_number the sequence number of reader device event
     * @param event_code   the reader device event feature code
     */
    public abstract void deviceEventEvent(int event_number, int event_code);

    /**
     * Invoked after a {@link ZhagaReader#disconnect()} method invocation
     * to notify success.
     */
    public abstract void disconnectionSuccessEvent();

    /**
     * Invoked after a {@link ZhagaReader#getName() getName}
     * method invocation to notify result.
     *
     * @param device_name the reader name
     */
    public abstract void nameEvent(String device_name);

    /**
     * Invoked after a class {@code ZhagaReader} method invocation to notify
     * result.
     *
     * @param command the command sent to the reader
     * @param error   the error code
     */
    public abstract void resultEvent(int command, int error);

    /**
     * Invoked after a {@link ZhagaReader#getSecurityLevel() getSecurityLevel}
     * method invocation to notify result.
     *
     * @param level the current security level
     */
    public abstract void securityLevelEvent(int level);

    /**
     * Invoked after a {@link ZhagaReader#getSoundForCommand() getSoundForCommand}
     * method invocation to notify result.
     *
     * @param sound_frequency  sound frequency (40-20000 Hz)
     * @param sound_on_time    duration of sound (0-2550 ms)
     * @param sound_off_time   duration of sound interval (0-2550 ms)
     * @param sound_repetition number of repetition (0-255, 0 = NO suond)
     */
    public abstract void soundForCommandEvent(int sound_frequency, int sound_on_time, int sound_off_time,
                                              int sound_repetition);

    /**
     * Invoked after a {@link ZhagaReader#getSoundForError() getSoundForError}
     * method invocation to notify result.
     *
     * @param sound_frequency  sound frequency (40-20000 Hz)
     * @param sound_on_time    duration of sound (0-2550 ms)
     * @param sound_off_time   duration of sound interval (0-2550 ms)
     * @param sound_repetition number of repetition (0-255, 0 = NO suond)
     */
    public abstract void soundForErrorEvent(int sound_frequency, int sound_on_time, int sound_off_time,
                                            int sound_repetition);

    /**
     * Invoked after a {@link ZhagaReader#getSoundForInventory() getSoundForInventory}
     * method invocation to notify result.
     *
     * @param sound_frequency  sound frequency (40-20000 Hz)
     * @param sound_on_time    duration of sound (0-2550 ms)
     * @param sound_off_time   duration of sound interval (0-2550 ms)
     * @param sound_repetition number of repetition (0-255, 0 = NO suond)
     */
    public abstract void soundForInventoryEvent(int sound_frequency, int sound_on_time, int sound_off_time,
                                                int sound_repetition);

    /**
     * Invoked after a {@link ZhagaReader#transparent(byte[]) transparent}
     * method invocation to notify result.
     *
     * @param answer the answer received from the tag
     */
    public abstract void transparentEvent(byte answer[]);

    /**
     * Invoked after a {@link ZhagaReader#getVibrationForCommand() getVibrationForCommand}
     * method invocation to notify result.
     *
     * @param vibration_on_time    duration of vibration (0-2550 ms)
     * @param vibration_off_time   duration of vibration interval (0-2550 ms)
     * @param vibration_repetition number of repetition (0-255, 0 = NO vibration)
     */
    public abstract void vibrationForCommandEvent(int vibration_on_time, int vibration_off_time,
                                                  int vibration_repetition);

    /**
     * Invoked after a {@link ZhagaReader#getVibrationForError() getVibrationForError}
     * method invocation to notify result.
     *
     * @param vibration_on_time    duration of vibration (0-2550 ms)
     * @param vibration_off_time   duration of vibration interval (0-2550 ms)
     * @param vibration_repetition number of repetition (0-255, 0 = NO vibration)
     */
    public abstract void vibrationForErrorEvent(int vibration_on_time, int vibration_off_time,
                                                int vibration_repetition);

    /**
     * Invoked after a {@link ZhagaReader#getVibrationForInventory() getVibrationForInventory}
     * method invocation to notify result.
     *
     * @param vibration_on_time    duration of vibration (0-2550 ms)
     * @param vibration_off_time   duration of vibration interval (0-2550 ms)
     * @param vibration_repetition number of repetition (0-255, 0 = NO vibration)
     */
    public abstract void vibrationForInventoryEvent(int vibration_on_time, int vibration_off_time,
                                                    int vibration_repetition);
}
