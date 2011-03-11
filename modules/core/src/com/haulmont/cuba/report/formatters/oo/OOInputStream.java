/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Vasiliy Fontanenko
 * Created: 29.06.2010 12:15:57
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.oo;

import com.sun.star.io.XInputStream;
import com.sun.star.io.XSeekable;

import java.io.ByteArrayInputStream;

/**
 * Implementation for XInputStream
 */
public class OOInputStream extends ByteArrayInputStream implements XInputStream, XSeekable {

    public OOInputStream(byte[] buf) {
        super(buf);
    }

    public int readBytes(byte[][] buffer, int bufferSize) throws com.sun.star.io.IOException {
        int numberOfReadBytes;
        try {
            byte[] bytes = new byte[bufferSize];
            numberOfReadBytes = super.read(bytes);
            if(numberOfReadBytes > 0) {
                if(numberOfReadBytes < bufferSize) {
                    byte[] smallerBuffer = new byte[numberOfReadBytes];
                    System.arraycopy(bytes, 0, smallerBuffer, 0, numberOfReadBytes);
                    bytes = smallerBuffer;
                }
            }
            else {
                bytes = new byte[0];
                numberOfReadBytes = 0;
            }
            buffer[0]=bytes;
            return numberOfReadBytes;
        }
        catch (java.io.IOException e) {
            throw new com.sun.star.io.IOException(e.getMessage(),this);
        }
    }

    public int readSomeBytes(byte[][] buffer, int bufferSize) throws com.sun.star.io.IOException {
        return readBytes(buffer, bufferSize);
    }

    public void skipBytes(int skipLength) throws com.sun.star.io.IOException {
        skip(skipLength);
    }

    public void closeInput() throws com.sun.star.io.IOException {
        try {
            close();
        }
        catch (java.io.IOException e) {
            throw new com.sun.star.io.IOException(e.getMessage(), this);
        }
    }

    public long getLength() throws com.sun.star.io.IOException {
        return count;
    }

    public long getPosition() throws com.sun.star.io.IOException {
        return pos;
    }

    public void seek(long position) throws IllegalArgumentException, com.sun.star.io.IOException {
        pos = (int) position;
    }
}