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

import com.tertiumtechnology.api.rfidpassiveapilib.listener.AbstractResponseListener;

/**
 * Represents an EPC Gen2 tag.
 */
public class EPC_tag extends Tag {

    /**
     * EPC tag reserved memory bank.
     */
    public static final int RESERVED_MEMORY_BANK = 0x00;
    /**
     * EPC tag kill password reserved memory address.
     */
    public static final int KILL_PASSWORD_ADDRESS = 0x00;
    /**
     * EPC tag access password reserved memory address.
     */
    public static final int ACCESS_PASSWORD_ADDRESS = 0x02;
    /**
     * EPC tag ID memory bank.
     */
    public static final int EPC_MEMORY_BANK = 0x01;
    /**
     * EPC tag TID memory bank.
     */
    public static final int TID_MEMORY_BANK = 0x02;
    /**
     * EPC tag user memory memory bank.
     */
    public static final int USER_MEMORY_BANK = 0x03;

    /**
     * EPC tag ID writable and not lockable lock code.
     */
    public static final int ID_WRITABLE_NOTLOCKABLE = 0x0C010F;
    /**
     * EPC tag TID writable and not lockable lock code.
     */
    public static final int TID_WRITABLE_NOTLOCKABLE = 0x03004F;
    /**
     * EPC tag user memory writable and not lockable lock code.
     */
    public static final int MEMORY_WRITABLE_NOTLOCKABLE = 0x00C01F;
    /**
     * EPC tag ID password writable lock code.
     */
    public static final int ID_PASSWORD_WRITABLE = 0x0C020F;
    /**
     * EPC tag TID password writable lock code.
     */
    public static final int TID_PASSWORD_WRITABLE = 0x03008F;
    /**
     * EPC tag user memory password writable lock code.
     */
    public static final int MEMORY_PASSWORD_WRITABLE = 0x00C02F;
    /**
     * EPC tag ID unwritable lock code.
     */
    public static final int ID_NOTWRITABLE = 0x0C030F;
    /**
     * EPC tag ID unwritable lock code and memory password (read/write) lock code.
     */
    public static final int ID_NOTWRITABLE_BOTHPASSWORD_NOTACCESSIBLE = 0xFC3F0F;
    /**
     * EPC tag TID unwritable lock code.
     */
    public static final int TID_NOTWRITABLE = 0x0300CF;
    /**
     * EPC tag user memory unwritable lock code.
     */
    public static final int MEMORY_NOTWRITABLE = 0x00C03F;
    /**
     * EPC tag kill password readable/writable and not lockable lock code.
     */
    public static final int KILLPASSWORD_READABLE_WRITABLE_NOTLOCKABLE = 0xC0100F;
    /**
     * EPC tag access password readable/writable and not lockable lock code.
     */
    public static final int ACCESSPASSWORD_READABLE_WRITABLE_NOTLOCKABLE = 0x30040F;
    /**
     * EPC tag kill password password readable/writable lock code.
     */
    public static final int KILLPASSWORD_PASSWORD_READABLE_WRITABLE = 0xC0200F;
    /**
     * EPC tag access password password readable/writable lock code.
     */
    public static final int ACCESSPASSWORD_PASSWORD_READABLE_WRITABLE = 0x30080F;
    /**
     * EPC tag kill password unreadable/unwritable lock code.
     */
    public static final int KILLPASSWORD_UNREADABLE_UNWRITABLE = 0xC0300F;
    /**
     * EPC tag access password unreadable/unwritable lock code.
     */
    public static final int ACCESSPASSWORD_UNREADABLE_UNWRITABLE = 0x300C0F;

    protected final short PC;
    protected short RSSI;

    /**
     * Class constructor
     *
     * @param RSSI           the tag RSSI at inventory time (dBm)
     * @param PC             the tag PC (Protocol Code)
     * @param ID             the tag ID
     * @param passive_reader reference to the passive reader object
     */
    public EPC_tag(short RSSI, short PC, byte[] ID, PassiveReader passive_reader) {
        super(ID, passive_reader);
        this.PC = PC;
        this.RSSI = RSSI;
    }

    /**
     * Get tag PC + ID.
     * <p>
     *
     * @return the tag PC + ID as byte array
     */
    public synchronized byte[] getExtendedID() {
        byte[] extendedID = new byte[2 + ID.length];
        String tmp = String.format("%04X", PC);
        extendedID[0] = (byte) PassiveReader.hexToByte(tmp.substring(0, 2));
        extendedID[1] = (byte) PassiveReader.hexToByte(tmp.substring(2, 4));
        for (int n = 0; n < ID.length; n++) {
            extendedID[n + 2] = ID[n];
        }
        return extendedID;
    }

    /**
     * Get tag PC (Protocol Control).
     *
     * @return the tag Protocol Control
     */
    public synchronized short getPC() {
        return PC;
    }

    /**
     * Get tag RSSI at inventory time.
     *
     * @return the tag RSSI value in dBm
     */
    public synchronized short getRSSI() {
        return RSSI;
    }

    /**
     * Start a tag kill operation.
     * <p>
     * The result of the kill operation is notified invoking response listener
     * method {@link AbstractResponseListener#killEvent(byte[], int) killEvent}.
     *
     * @param password tag kill password
     */
    public synchronized void kill(byte[] password) {
        String tmp, command;
        byte PC_number[] = new byte[2];

        if (passive_reader.status != PassiveReader.READY_STATUS) {
            passive_reader.response_listener.killEvent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (password.length != 4) {
            passive_reader.response_listener.killEvent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        tmp = String.format("%04X", PC);
        PC_number[0] = (byte) PassiveReader.hexToByte(tmp.substring(0, 2));
        PC_number[1] = (byte) PassiveReader.hexToByte(tmp.substring(2, 4));
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.KILL_COMMAND;
        passive_reader.tag_ID = getExtendedID();
        command = passive_reader.buildCommand(PassiveReader.EPC_KILL_COMMAND, (byte) (timeout / 100),
                    PC_number[0], PC_number[1]);
        command = passive_reader.appendDataToCommand(command, ID);
        command = passive_reader.appendDataToCommand(command, password);
        passive_reader.device_manager.requestWriteData(command);
    }

    /**
     * Start a tag lock operation.
     * <p>
     * The result of the lock operation is notified invoking response listener
     * method {@link AbstractResponseListener#lockEvent(byte[], int) lockEvent}.
     *
     * @param lock_type the lock type
     * @param password  tag access password (may be null or empty)
     */
    public synchronized void lock(int lock_type, byte[] password) {
        String tmp, command;
        byte payload[] = new byte[3];
        byte PC_number[] = new byte[2];

        if (passive_reader.status != PassiveReader.READY_STATUS) {
            passive_reader.response_listener.lockEvent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        tmp = String.format("%04X", PC);
        PC_number[0] = (byte) PassiveReader.hexToByte(tmp.substring(0, 2));
        PC_number[1] = (byte) PassiveReader.hexToByte(tmp.substring(2, 4));
        tmp = String.format("%06X", lock_type);
        payload[0] = (byte) PassiveReader.hexToByte(tmp.substring(0, 2));
        payload[1] = (byte) PassiveReader.hexToByte(tmp.substring(2, 4));
        payload[2] = (byte) PassiveReader.hexToByte(tmp.substring(4, 6));
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.LOCK_COMMAND;
        passive_reader.tag_ID = getExtendedID();
        command = passive_reader.buildCommand(PassiveReader.EPC_LOCK_COMMAND, (byte) (timeout / 100),
                    PC_number[0], PC_number[1]);
        command = passive_reader.appendDataToCommand(command, ID);
        command = passive_reader.appendDataToCommand(command, payload);
        if (password != null) {
            command = passive_reader.appendDataToCommand(command, password);
        }
        passive_reader.device_manager.requestWriteData(command);
    }

    /**
     * Start a tag memory read operation.
     * <p>
     * The result of the read operation is notified invoking response listener
     * method {@link AbstractResponseListener#readEvent(byte[], int, byte[]) readEvent}.
     *
     * @param address the tag memory address
     * @param blocks  the number of memory 2-bytes blocks to read (1-50)
     */
    public synchronized void read(int address, int blocks) {
        String tmp, command;
        byte memory_to_read[] = new byte[3];
        byte PC_number[] = new byte[2];

        if (passive_reader.status != PassiveReader.READY_STATUS) {
            passive_reader.response_listener.readEvent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_WRONG_STATUS_ERROR, null);
            return;
        }
        if (address < 0 || address > 255) {
            passive_reader.response_listener.readEvent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR, null);
            return;
        }
        if (blocks < 0 || blocks > 50) {
            passive_reader.response_listener.readEvent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR, null);
            return;
        }
        memory_to_read[0] = (byte) USER_MEMORY_BANK;
        memory_to_read[1] = (byte) address;
        memory_to_read[2] = (byte) blocks;
        tmp = String.format("%04X", PC);
        PC_number[0] = (byte) PassiveReader.hexToByte(tmp.substring(0, 2));
        PC_number[1] = (byte) PassiveReader.hexToByte(tmp.substring(2, 4));
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.READ_COMMAND;
        passive_reader.tag_ID = getExtendedID();
        command = passive_reader.buildCommand(PassiveReader.EPC_READ_COMMAND, (byte) (timeout / 100),
                    PC_number[0], PC_number[1]);
        command = passive_reader.appendDataToCommand(command, ID);
        command = passive_reader.appendDataToCommand(command, memory_to_read);
        /*
        if (password != null)
            AckMe_command = passive_reader.appendDataToCommand(AckMe_command, password);
        */
        passive_reader.device_manager.requestWriteData(command);
    }

    /**
     * Start a tag memory TID read operation.
     * <p>
     * The result of the read operation is notified invoking response listener
     * method {@link AbstractResponseListener#readTIDevent(byte[], int, byte[])
     * readTIDevent}.
     *
     * @param length   TID length (bytes)
     * @param password tag read password (may be null or empty)
     */
    public synchronized void readTID(int length, byte[] password) {
        String tmp, command;
        byte memory_to_read[] = new byte[3];
        byte PC_number[] = new byte[2];

        if (passive_reader.status != PassiveReader.READY_STATUS) {
            passive_reader.response_listener.readTIDevent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_WRONG_STATUS_ERROR, null);
            return;
        }
        if (length % 2 != 0 || length > 100) {
            passive_reader.response_listener.readTIDevent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR, null);
            return;
        }
        memory_to_read[0] = (byte) TID_MEMORY_BANK;
        memory_to_read[1] = (byte) 0x00;
        memory_to_read[2] = (byte) (length / 2);
        tmp = String.format("%04X", PC);
        PC_number[0] = (byte) PassiveReader.hexToByte(tmp.substring(0, 2));
        PC_number[1] = (byte) PassiveReader.hexToByte(tmp.substring(2, 4));
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.READ_TID_COMMAND;
        passive_reader.tag_ID = getExtendedID();
        command = passive_reader.buildCommand(PassiveReader.EPC_READ_COMMAND, (byte) (timeout / 100),
                    PC_number[0], PC_number[1]);
        command = passive_reader.appendDataToCommand(command, ID);
        command = passive_reader.appendDataToCommand(command, memory_to_read);
        if (password != null) {
            command = passive_reader.appendDataToCommand(command, password);
        }
        passive_reader.device_manager.requestWriteData(command);
    }

    @Override
    public String toString() {
        String tmp = String.format("%04X", PC);

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
     * method {@link AbstractResponseListener#writeEvent(byte[], int) writeEvent}.
     *
     * @param address  the tag memory address
     * @param data     the data bytes to write
     * @param password tag access password (may be null or empty)
     */
    public synchronized void write(int address, byte[] data, byte[] password) {
        String tmp, command;
        byte memory_to_write[] = new byte[3];
        byte PC_number[] = new byte[2];
        byte blocks;

        if (passive_reader.status != PassiveReader.READY_STATUS) {
            passive_reader.response_listener.writeEvent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (address < 0 || address > 255) {
            passive_reader.response_listener.writeEvent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        if (data.length % 2 != 0 || data.length > 100) {
            passive_reader.response_listener.writeEvent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        blocks = (byte) (data.length / 2);
        memory_to_write[0] = (byte) USER_MEMORY_BANK;
        memory_to_write[1] = (byte) address;
        memory_to_write[2] = blocks;
        tmp = String.format("%04X", PC);
        PC_number[0] = (byte) PassiveReader.hexToByte(tmp.substring(0, 2));
        PC_number[1] = (byte) PassiveReader.hexToByte(tmp.substring(2, 4));
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.WRITE_COMMAND;
        passive_reader.tag_ID = getExtendedID();
        command = passive_reader.buildCommand(PassiveReader.EPC_WRITE_COMMAND, (byte) (timeout / 100),
                    PC_number[0], PC_number[1]);
        command = passive_reader.appendDataToCommand(command, ID);
        command = passive_reader.appendDataToCommand(command, memory_to_write);
        command = passive_reader.appendDataToCommand(command, data);
        if (password != null) {
            command = passive_reader.appendDataToCommand(command, password);
        }
        passive_reader.device_manager.requestWriteData(command);
    }

    /**
     * Start a tag access password operation.
     * <p>
     * The result of the write operation is notified invoking response listener
     * methods {@link AbstractResponseListener#writePasswordEvent(byte[], int)
     * writePasswordEvent}.
     *
     * @param access_password the new tag access password (4 bytes)
     * @param password        tag access password (may be null or empty)
     */
    public synchronized void writeAccessPassword(byte[] access_password, byte[] password) {
        String tmp, command;
        byte memory_to_write[] = new byte[3];
        byte PC_number[] = new byte[2];

        if (passive_reader.status != PassiveReader.READY_STATUS) {
            passive_reader.response_listener.writePasswordEvent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (access_password.length != 4) {
            passive_reader.response_listener.writePasswordEvent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        memory_to_write[0] = (byte) RESERVED_MEMORY_BANK;
        memory_to_write[1] = (byte) ACCESS_PASSWORD_ADDRESS;
        memory_to_write[2] = (byte) (2);
        tmp = String.format("%04X", PC);
        PC_number[0] = (byte) PassiveReader.hexToByte(tmp.substring(0, 2));
        PC_number[1] = (byte) PassiveReader.hexToByte(tmp.substring(2, 4));
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.WRITEACCESSPASSWORD_COMMAND;
        passive_reader.tag_ID = getExtendedID();
        command = passive_reader.buildCommand(PassiveReader.EPC_WRITE_COMMAND, (byte) (timeout / 100),
                    PC_number[0], PC_number[1]);
        command = passive_reader.appendDataToCommand(command, ID);
        command = passive_reader.appendDataToCommand(command, memory_to_write);
        command = passive_reader.appendDataToCommand(command, access_password);
        if (password != null) {
            command = passive_reader.appendDataToCommand(command, password);
        }
        passive_reader.device_manager.requestWriteData(command);
    }

    /**
     * Start a tag memory ID write operation.
     * <p>
     * The result of the write operation is notified invoking response listener
     * methods {@link AbstractResponseListener#writeIDevent(byte[], int) writeIDevent}.
     *
     * @param ID  the new tag ID to write
     * @param NSI the tag Number System Identifier to write
     */
    public synchronized void writeID(byte[] ID, short NSI) {
        String tmp, command;
        byte Numbering_System_Identifier[] = new byte[2];

        if (passive_reader.status != PassiveReader.READY_STATUS) {
            passive_reader.response_listener.writeIDevent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (ID.length % 2 != 0 || ID.length < 12 || ID.length > 30) {
            passive_reader.response_listener.writeIDevent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        tmp = String.format("%04X", NSI);
        Numbering_System_Identifier[0] = (byte) PassiveReader.hexToByte(tmp.substring(0, 2));
        Numbering_System_Identifier[1] = (byte) PassiveReader.hexToByte(tmp.substring(2, 4));
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.WRITEID_COMMAND;
        passive_reader.tag_ID = getExtendedID();
        command = passive_reader.buildCommand(PassiveReader.EPC_WRITEID_COMMAND, (byte) (timeout / 100));
        command = passive_reader.appendDataToCommand(command, ID);
        command = passive_reader.appendDataToCommand(command, Numbering_System_Identifier);
        passive_reader.device_manager.requestWriteData(command);
    }

    /**
     * Start a tag kill password operation.
     * <p>
     * The result of the write operation is notified invoking response listener
     * methods {@link AbstractResponseListener#writePasswordEvent(byte[], int)
     * writePasswordEvent}.
     *
     * @param kill_password the new tag kill password (4 bytes)
     * @param password      tag access password (may be null or empty)
     */
    public synchronized void writeKillPassword(byte[] kill_password, byte[] password) {
        String tmp, command;
        byte memory_to_write[] = new byte[3];
        byte PC_number[] = new byte[2];

        if (passive_reader.status != PassiveReader.READY_STATUS) {
            passive_reader.response_listener.writePasswordEvent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        if (kill_password.length != 4) {
            passive_reader.response_listener.writePasswordEvent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_COMMAND_WRONG_PARAMETER_ERROR);
            return;
        }
        memory_to_write[0] = (byte) RESERVED_MEMORY_BANK;
        memory_to_write[1] = (byte) KILL_PASSWORD_ADDRESS;
        memory_to_write[2] = (byte) (2);
        tmp = String.format("%04X", PC);
        PC_number[0] = (byte) PassiveReader.hexToByte(tmp.substring(0, 2));
        PC_number[1] = (byte) PassiveReader.hexToByte(tmp.substring(2, 4));
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.WRITEKILLPASSWORD_COMMAND;
        passive_reader.tag_ID = getExtendedID();
        command = passive_reader.buildCommand(PassiveReader.EPC_WRITE_COMMAND, (byte) (timeout / 100),
                    PC_number[0], PC_number[1]);
        command = passive_reader.appendDataToCommand(command, ID);
        command = passive_reader.appendDataToCommand(command, memory_to_write);
        command = passive_reader.appendDataToCommand(command, kill_password);
        if (password != null) {
            command = passive_reader.appendDataToCommand(command, password);
        }
        passive_reader.device_manager.requestWriteData(command);
    }
}
