/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.export;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class ProxyDataProvider implements ExportDataProvider {

    private boolean closed = false;
    private InputStream dataInputStream;

    public ProxyDataProvider(InputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }

    @Override
    public InputStream provide() throws ResourceException {
        if (closed)
            throw new IllegalStateException("DataProvider is closed");

        return dataInputStream;
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(dataInputStream);
        closed = true;
    }
}
