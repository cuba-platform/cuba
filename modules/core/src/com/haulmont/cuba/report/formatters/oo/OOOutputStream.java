/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Vasiliy Fontanenko
 * Created: 29.06.2010 12:23:49
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.oo;

import com.sun.star.io.XOutputStream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Proxy stream
 */
public class OOOutputStream extends OutputStream implements XOutputStream {

    private OutputStream outputStream;

    public OOOutputStream(OutputStream outputStream) {
        if (outputStream == null)
            throw new NullPointerException();

        this.outputStream = outputStream;
    }

    @Override
    public void write(int b) throws IOException {
        this.outputStream.write(b);
    }

    public void writeBytes(byte[] values) throws com.sun.star.io.IOException {
        try {
            this.outputStream.write(values);
        } catch (java.io.IOException e) {
            throw (new com.sun.star.io.IOException(e.getMessage()));
        }
    }

    public void closeOutput() throws com.sun.star.io.IOException {
        try {
            this.outputStream.flush();
            this.outputStream.close();
        } catch (java.io.IOException e) {
            throw (new com.sun.star.io.IOException(e.getMessage()));
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.outputStream.write(b, off, len);
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.outputStream.write(b);
    }

    @Override
    public void flush() {
        try {
            this.outputStream.flush();
        } catch (java.io.IOException ignored) {
        }
    }

    @Override
    public void close() throws IOException {
        this.outputStream.close();
    }
}