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

import com.tertiumtechnology.api.rfidpassiveapilib.Tag;

/**
 * Listener template for event generated by inventory operation.
 * <p>
 * A concrete instance of {@code AbstractInventoryListener} has to set for the
 * instance of the class {@code PassiveReader} to receive notification about
 * tags inventory.
 * <p>
 * Inventory operation can be started invoking method {@link
 * com.tertiumtechnology.api.rfidpassiveapilib.PassiveReader#doInventory() doInventory} of class {@code PassiveReader},
 * or can be periodically or asynchronously executed by reader.
 */
public abstract class AbstractInventoryListener {

    /**
     * Invoked after an inventory operation to notify the discovered tags list.
     *
     * @param tag one tag discovered
     */
    public abstract void inventoryEvent(Tag tag);
}
