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

/**
 * Represents an ISO-14443 type A tag.
 */
public class ISO14443A_tag extends Tag {

    /**
     * Class constructor
     *
     * @param ID             the tag ID
     * @param passive_reader reference to the passive reader object
     */
    public ISO14443A_tag(byte[] ID, PassiveReader passive_reader) {
        super(ID, passive_reader);
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
}
