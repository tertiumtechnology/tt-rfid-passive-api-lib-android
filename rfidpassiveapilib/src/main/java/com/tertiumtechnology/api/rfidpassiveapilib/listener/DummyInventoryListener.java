package com.tertiumtechnology.api.rfidpassiveapilib.listener;

import com.tertiumtechnology.api.rfidpassiveapilib.PassiveReader;
import com.tertiumtechnology.api.rfidpassiveapilib.Tag;

/**
 * Dummy listener for inventory operations.
 * <p>
 * This dummy listener is used if an instance of {@code ZhagaReader} is
 * created calling {@link PassiveReader#getZhagaReaderInstance(AbstractZhagaListener)
 * getZhagaReaderInstance} static method.
 */
public class DummyInventoryListener extends AbstractInventoryListener {

    @Override
    public void inventoryEvent(Tag tag) {
    }
}
