package com.tertiumtechnology.api.rfidpassiveapilib;

import com.tertiumtechnology.api.rfidpassiveapilib.listener.AbstractResponseListener;

/**
 * Represents an EPC Gen2 tag without PC bits
 */
public class EPC_simple_tag extends EPC_tag {

    /**
     * Class constructor
     *
     * @param RSSI           the tag RSSI at inventory time (dBm)
     * @param ID             the tag ID
     * @param passive_reader reference to the passive reader object
     */
    public EPC_simple_tag(short RSSI, byte[] ID, PassiveReader passive_reader) {
        super(RSSI, (short)(0), ID, passive_reader);
    }

    /**
     * Get tag ID.
     * <p>
     *
     * @return the tag ID as byte array
     */
    public synchronized byte[] getID() {
        byte[] id = new byte[ID.length];
        for (int n = 0; n < ID.length; n++) {
            id[n] = ID[n];
        }
        return id;
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
        String command;

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
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.KILL_COMMAND;
        passive_reader.tag_ID = getID();
        command = passive_reader.buildCommand(PassiveReader.EPC_KILL_COMMAND, (byte) (timeout / 100));
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

        if (passive_reader.status != PassiveReader.READY_STATUS) {
            passive_reader.response_listener.lockEvent(getExtendedID(),
                    AbstractResponseListener.READER_DRIVER_WRONG_STATUS_ERROR);
            return;
        }
        tmp = String.format("%06X", lock_type);
        payload[0] = (byte) PassiveReader.hexToByte(tmp.substring(0, 2));
        payload[1] = (byte) PassiveReader.hexToByte(tmp.substring(2, 4));
        payload[2] = (byte) PassiveReader.hexToByte(tmp.substring(4, 6));
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.LOCK_COMMAND;
        passive_reader.tag_ID = getID();
        command = passive_reader.buildCommand(PassiveReader.EPC_LOCK_COMMAND, (byte) (timeout / 100));
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
        String command;
        byte memory_to_read[] = new byte[3];

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
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.READ_COMMAND;
        passive_reader.tag_ID = getID();
        command = passive_reader.buildCommand(PassiveReader.EPC_READ_COMMAND, (byte) (timeout / 100));
        command = passive_reader.appendDataToCommand(command, ID);
        command = passive_reader.appendDataToCommand(command, memory_to_read);
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
        String command;
        byte memory_to_read[] = new byte[3];

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
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.READ_TID_COMMAND;
        passive_reader.tag_ID = getID();
        command = passive_reader.buildCommand(PassiveReader.EPC_READ_COMMAND, (byte) (timeout / 100));
        command = passive_reader.appendDataToCommand(command, ID);
        command = passive_reader.appendDataToCommand(command, memory_to_read);
        if (password != null) {
            command = passive_reader.appendDataToCommand(command, password);
        }
        passive_reader.device_manager.requestWriteData(command);
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
     * method {@link AbstractResponseListener#writeEvent(byte[], int) writeEvent}.
     *
     * @param address  the tag memory address
     * @param data     the data bytes to write
     * @param password tag access password (may be null or empty)
     */
    public synchronized void write(int address, byte[] data, byte[] password) {
        String command;
        byte memory_to_write[] = new byte[3];
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
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.WRITE_COMMAND;
        passive_reader.tag_ID = getID();
        command = passive_reader.buildCommand(PassiveReader.EPC_WRITE_COMMAND, (byte) (timeout / 100));
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
        String command;
        byte memory_to_write[] = new byte[3];

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
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.WRITEACCESSPASSWORD_COMMAND;
        passive_reader.tag_ID = getID();
        command = passive_reader.buildCommand(PassiveReader.EPC_WRITE_COMMAND, (byte) (timeout / 100));
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
        String command;
        byte memory_to_write[] = new byte[3];

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
        passive_reader.status = PassiveReader.PENDING_COMMAND_STATUS;
        passive_reader.pending = AbstractResponseListener.WRITEKILLPASSWORD_COMMAND;
        passive_reader.tag_ID = getExtendedID();
        command = passive_reader.buildCommand(PassiveReader.EPC_WRITE_COMMAND, (byte) (timeout / 100));
        command = passive_reader.appendDataToCommand(command, ID);
        command = passive_reader.appendDataToCommand(command, memory_to_write);
        command = passive_reader.appendDataToCommand(command, kill_password);
        if (password != null) {
            command = passive_reader.appendDataToCommand(command, password);
        }
        passive_reader.device_manager.requestWriteData(command);
    }
}