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

package com.tertiumtechnology.api.rfidpassiveapilib;

import com.tertiumtechnology.api.rfidpassiveapilib.listener.AbstractResponseListener;

/**
 * Represents an ISO-15693 tag.
 */
public class ISO15693_tag extends Tag {
    /**
     * Class constructor
     *
     * @param ID             the tag ID
     * @param passive_reader reference to the passive reader object
     */
    public ISO15693_tag(byte[] ID, PassiveReader passive_reader) {
        super(ID, passive_reader);
    }

    /**
     * Start a tag lock operation.
     * <p>
     * The result of the lock operation is notified invoking response listener
     * method {@link AbstractResponseListener#lockEvent(byte[], int)} lockEvent}.
     *
     * @param address the tag memory address
     * @param blocks  the number of memory 4-bytes blocks to lock (1-25)
     */
    public synchronized void lock(int address, int blocks) {
        String tmp;
        byte memory_address[] = new byte[2];

        if (passive_reader.status != PassiveReader.READY_STATUS) {
            passive_reader.response_listener.lockEvent
                    (getID(), AbstractResponseListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (address < 0 || address > 65535) {
            passive_reader.response_listener.lockEvent
                    (getID(), AbstractResponseListener
                            .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        if (blocks < 0 || blocks > 25) {
            passive_reader.response_listener.lockEvent
                    (getID(), AbstractResponseListener
                            .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        tmp = String.format("%04X", address);
        memory_address[0] = (byte) PassiveReader.hexToByte(tmp.substring(0, 2));
        memory_address[1] = (byte) PassiveReader.hexToByte(tmp.substring(2, 4));
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.LOCK_COMMAND;
        passive_reader.tag_id = getID();
        passive_reader.device_manager.requestWriteData(passive_reader
                .buildCommand(PassiveReader.ISO15693_LOCK_COMMAND, (byte)
                                (timeout / 100), ID[0], ID[1], ID[2], ID[3],
                        ID[4], ID[5], ID[6], ID[7],
                        memory_address[0], memory_address[1], (byte) (blocks)));
    }

    /**
     * Start a tag memory read operation.
     * <p>
     * The result of the read operation is notified invoking response listener
     * method {@link AbstractResponseListener#readEvent(byte[], int, byte[])} readEvent}.
     *
     * @param address the tag memory address
     * @param blocks  the number of memory 4-byte blocks to read (1-25)
     */
    public synchronized void read(int address, int blocks) {
        String tmp;
        byte memory_address[] = new byte[2];

        if (passive_reader.status != PassiveReader.READY_STATUS) {
            passive_reader.response_listener.readEvent
                    (getID(), AbstractResponseListener
                            .READER_DRIVER_WRONG_STATUS_ERROR, null);
            return;
        }
        if (address < 0 || address > 65535) {
            passive_reader.response_listener.readEvent
                    (getID(), AbstractResponseListener
                            .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR, null);
            return;
        }
        if (blocks < 0 || blocks > 25) {
            passive_reader.response_listener.readEvent
                    (getID(), AbstractResponseListener
                            .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR, null);
            return;
        }
        tmp = String.format("%04X", address);
        memory_address[0] = (byte) PassiveReader.hexToByte(tmp.substring(0, 2));
        memory_address[1] = (byte) PassiveReader.hexToByte(tmp.substring(2, 4));
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.READ_COMMAND;
        passive_reader.tag_id = getID();
        passive_reader.device_manager.requestWriteData(passive_reader
                .buildCommand(PassiveReader.ISO15693_READ_COMMAND, (byte)
                                (timeout / 100), ID[0], ID[1], ID[2], ID[3],
                        ID[4], ID[5], ID[6], ID[7],
                        memory_address[0], memory_address[1], (byte) (blocks)));
    }

    @Override
    public String toString() {
        String tmp = "";

        if (reverseID) {
            for (int n = ID.length - 1; n >= 0; n--) {
                tmp += byteToHex(ID[n]);
            }
        }
        else {
            for (int n = 0; n < ID.length; n++) {
                tmp += byteToHex(ID[n]);
            }
        }
        return tmp;
    }

    /**
     * Start a tag memory write operation.
     * <p>
     * The result of the write operation is notified invoking response listener
     * method {@link AbstractResponseListener#writeEvent(byte[], int)}  writeEvent}.
     *
     * @param address the tag memory address
     * @param data    the data bytes to write
     */
    public synchronized void write(int address, byte[] data) {
        String tmp, command;
        byte memory_address[] = new byte[2];
        byte blocks;

        if (passive_reader.status != PassiveReader.READY_STATUS) {
            passive_reader.response_listener.writeEvent
                    (getID(), AbstractResponseListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (address < 0 || address > 65535) {
            passive_reader.response_listener.writeEvent
                    (getID(), AbstractResponseListener
                            .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        if (data.length % 4 != 0 || data.length > 100) {
            passive_reader.response_listener.writeEvent
                    (getID(), AbstractResponseListener
                            .READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        blocks = (byte) (data.length / 4);
        tmp = String.format("%04X", address);
        memory_address[0] = (byte) PassiveReader.hexToByte(tmp.substring(0, 2));
        memory_address[1] = (byte) PassiveReader.hexToByte(tmp.substring(2, 4));
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.WRITE_COMMAND;
        passive_reader.tag_id = getID();
        command = passive_reader.buildCommand(PassiveReader
                        .ISO15693_WRITE_COMMAND, (byte) (timeout / 100), ID[0],
                ID[1], ID[2], ID[3], ID[4], ID[5], ID[6], ID[7],
                memory_address[0], memory_address[1], blocks);
        passive_reader.device_manager.requestWriteData(passive_reader
                .appendDataToCommand(command, data));
    }
}
