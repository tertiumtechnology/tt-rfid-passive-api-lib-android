/*
 * The MIT License
 *
 * Copyright 2020 Tertium Technology.
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

import com.tertiumtechnology.api.rfidpassiveapilib.EPC_tag;
import com.tertiumtechnology.api.rfidpassiveapilib.ISO15693_tag;

/**
 * Listener template for event generated in response to a {@code Tag} method
 * invocation.
 * <p>
 * A concrete instance of {@code AbstractResponseListener} has to set for every
 * class {@code Tag} object instance to receive notification about methods
 * invocation.
 */
public abstract class AbstractResponseListener {

    /**
     * {@link EPC_tag#read(int, int) read} or
     * {@link ISO15693_tag#read(int, int) read} command.
     */
    public static final int READ_COMMAND = 100;
    /**
     * {@link EPC_tag#write(int, byte[], byte[]) write} or
     * {@link ISO15693_tag#write(int, byte[]) write} command.
     */
    public static final int WRITE_COMMAND = 101;
    /**
     * {@link EPC_tag#lock(int, byte[]) lock} or
     * {@link ISO15693_tag#lock(int, int) lock command.
     */
    public static final int LOCK_COMMAND = 102;
    /**
     * {@link EPC_tag#writeID(byte[], short) writeID} command.
     */
    public static final int WRITEID_COMMAND = 103;
    /**
     * {@link EPC_tag#kill(byte[]) kill} command.
     */
    public static final int KILL_COMMAND = 104;
    /**
     * {@link EPC_tag#readTID(int, byte[]) readTID} command.
     */
    public static final int READ_TID_COMMAND = 105;
    /**
     * {@link EPC_tag#writeKillPassword(byte[], byte[]) writeKillPassword} command.
     */
    public static final int WRITEKILLPASSWORD_COMMAND = 106;
    /**
     * {@link EPC_tag#writeAccessPassword(byte[], byte[]) writeAccessPassword} command.
     */
    public static final int WRITEACCESSPASSWORD_COMMAND = 107;

    /**
     * Successful tag operation (no error).
     */
    public static final int NO_ERROR = 0x00;
    /**
     * Tag operation with memory error.
     */
    public static final int MEMORY_ERROR = 0x01;
    /**
     * Tag operation with locked memory error.
     */
    public static final int MEMORY_LOCKED = 0x02;
    /**
     * Tag operation with invalid parameter error.
     */
    public static final int PARAMETER_INVALID = 0x0C;
    /**
     * Timeout error for tag operation.
     */
    public static final int TIMEOUT_ERROR = 0x0D;
    /**
     * Wrong command in tag operation.
     */
    public static final int WRONG_COMMAND = 0x0E;
    /**
     * Invalid command in tag operation.
     */
    public static final int INVALID_COMMAND = 0x0F;
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
     * Invoked after a {@link EPC_tag#kill(byte[]) kill} method invocation to
     * notify result.
     *
     * @param tag_ID the tag ID
     * @param error  the error code
     */
    public abstract void killEvent(byte[] tag_ID, int error);

    /**
     * Invoked after a {@link EPC_tag#lock(int, byte[]) lock} or {@link
     * ISO15693_tag#lock(int, int) lock} method invocation to notify result.
     *
     * @param tag_ID the tag ID
     * @param error  the error code
     */
    public abstract void lockEvent(byte[] tag_ID, int error);

    /**
     * Invoked after a {@link EPC_tag#read(int, int) read} or
     * {@link ISO15693_tag#read(int, int) read} method invocation to notify
     * result.
     *
     * @param tag_ID the tag ID
     * @param error  the error code
     * @param data   data read
     */
    public abstract void readEvent(byte[] tag_ID, int error, byte data[]);

    /**
     * Invoked after a {@link EPC_tag#readTID(int, byte[]) readTID} method invocation
     * to notify result.
     *
     * @param tag_ID the tag ID
     * @param error  the error code
     * @param TID    tag TID read
     */
    public abstract void readTIDevent(byte[] tag_ID, int error, byte TID[]);

    /**
     * Invoked after a {@link EPC_tag#write(int, byte[], byte[]) write} or
     * {@link ISO15693_tag#write(int, byte[]) write} method invocation to notify
     * result.
     *
     * @param tag_ID the tag ID
     * @param error  the error code
     */
    public abstract void writeEvent(byte[] tag_ID, int error);

    /**
     * Invoked after a {@link EPC_tag#writeID(byte[], short) writeID} method
     * invocation to notify result.
     *
     * @param tag_ID the tag ID
     * @param error  the error code
     */
    public abstract void writeIDevent(byte[] tag_ID, int error);

    /**
     * Invoked after a {@link EPC_tag#writeKillPassword(byte[], byte[])
     * writeKillPassword} or {@link EPC_tag#writeAccessPassword(byte[], byte[])
     * writeAccessPassword} method invocation to notify result.
     *
     * @param tag_ID the tag ID
     * @param error  the error code
     */
    public abstract void writePasswordEvent(byte[] tag_ID, int error);
}
