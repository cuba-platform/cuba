/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.08.2009 14:51:37
 *
 * $Id$
 */
package com.haulmont.cuba.gui.export;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ByteArrayDataProvider implements ExportDataProvider {

    private boolean closed = false;
    private byte[] data;

    public ByteArrayDataProvider(byte[] data) {
        this.data = data;
    }

    public InputStream provide() {
        if (closed)
            throw new IllegalStateException("DataProvider is closed");

        return new ByteArrayInputStream(data);
    }

    public void close() {
        closed = true;
        data = null;
    }
}
