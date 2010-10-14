/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Vasiliy Fontanenko
 * Created: 29.06.2010 12:23:49
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.tools;

import com.sun.star.io.BufferSizeExceededException;
import com.sun.star.io.NotConnectedException;
import com.sun.star.io.XOutputStream;

import java.io.ByteArrayOutputStream;

public class OOOutputStream extends ByteArrayOutputStream implements XOutputStream {

    public OOOutputStream() {
        super(32768);
    }

    public void writeBytes(byte[] values) throws NotConnectedException, BufferSizeExceededException, com.sun.star.io.IOException {
        try {
            this.write(values);
        }
        catch (java.io.IOException e) {
            throw (new com.sun.star.io.IOException(e.getMessage()));
        }
    }

    public void closeOutput() throws NotConnectedException, BufferSizeExceededException, com.sun.star.io.IOException {
        try {
            super.flush();
            super.close();
        }
        catch (java.io.IOException e) {
            throw (new com.sun.star.io.IOException(e.getMessage()));
        }
    }

    @Override
    public void flush() {
        try {
            super.flush();
        }
        catch (java.io.IOException e) {
        }
    }
}