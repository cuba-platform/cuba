/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.export;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ByteArrayDataProvider implements ExportDataProvider {

    private boolean closed = false;
    private byte[] data;

    public ByteArrayDataProvider(byte[] data) {
        this.data = data;
    }

    @Override
    public InputStream provide() throws ClosedDataProviderException {
        if (closed)
            throw new ClosedDataProviderException();

        return new ByteArrayInputStream(data);
    }

    @Override
    public void close() {
        if (!closed) {
            closed = true;
            data = null;
        }
    }
}