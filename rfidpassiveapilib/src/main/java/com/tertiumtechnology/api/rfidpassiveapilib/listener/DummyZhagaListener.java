package com.tertiumtechnology.api.rfidpassiveapilib.listener;

import com.tertiumtechnology.api.rfidpassiveapilib.PassiveReader;

/**
 * Dummy listener for inventory operations.
 * <p>
 * This dummy listener is used if a legacy instance of {@code PassiveReader} is
 * created calling {@link PassiveReader#getInstance(AbstractInventoryListener,
 * AbstractReaderListener, AbstractResponseListener) getInstance} static method.
 */
public class DummyZhagaListener extends AbstractZhagaListener {

    @Override
    public void HMIevent(int LED_color, int sound_vibration, int button_number) {
    }

    @Override
    public void LEDforCommandEvent(int light_color, int light_on_time, int light_off_time, int light_repetition) {
    }

    @Override
    public void LEDforErrorEvent(int light_color, int light_on_time, int light_off_time, int light_repetition) {
    }

    @Override
    public void LEDforInventoryEvent(int light_color, int light_on_time, int light_off_time, int light_repetition) {
    }

    @Override
    public void RFevent(boolean RF_on) {
    }

    @Override
    public void RFonOffEvent(int RF_power, int RF_off_timeout, int RF_on_preactivation) {
    }

    @Override
    public void activatedButtonEvent(int activated_button) {
    }

    @Override
    public void autoOffEvent(int OFF_time) {
    }

    @Override
    public void buttonEvent(int button, int time) {
    }

    @Override
    public void connectionFailedEvent(int error) {
    }

    @Override
    public void connectionSuccessEvent() {
    }

    @Override
    public void deviceEventEvent(int event_number, int event_code) {
    }

    @Override
    public void disconnectionSuccessEvent() {
    }

    @Override
    public void nameEvent(String device_name) {
    }

    @Override
    public void resultEvent(int command, int error) {
    }

    @Override
    public void securityLevelEvent(int level) {
    }

    @Override
    public void soundForCommandEvent(int sound_frequency, int sound_on_time, int sound_off_time, int sound_repetition) {
    }

    @Override
    public void soundForErrorEvent(int sound_frequency, int sound_on_time, int sound_off_time, int sound_repetition) {
    }

    @Override
    public void soundForInventoryEvent(int sound_frequency, int sound_on_time, int sound_off_time,
                                       int sound_repetition) {
    }

    @Override
    public void transparentEvent(byte answer[]) {
    }

    @Override
    public void vibrationForCommandEvent(int vibration_on_time, int vibration_off_time, int vibration_repetition) {
    }

    @Override
    public void vibrationForErrorEvent(int vibration_on_time, int vibration_off_time, int vibration_repetition) {
    }

    @Override
    public void vibrationForInventoryEvent(int vibration_on_time, int vibration_off_time, int vibration_repetition) {
    }
}
