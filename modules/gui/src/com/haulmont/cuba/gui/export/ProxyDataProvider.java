/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.export;

import java.io.InputStream;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class ProxyDataProvider implements ExportDataProvider {

    private boolean closed = false;

    private InputStream dataInputStream = null;
    private ExportDataProvider dataProvider = null;

    public ProxyDataProvider(ExportDataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.dataInputStream = dataProvider.provide();
    }

    @Override
    public InputStream provide() throws ResourceException {
        if (closed)
            throw new IllegalStateException("DataProvider is closed");

        return dataInputStream;
    }

    @Override
    public void close() {
        if (!closed) {
            if (dataProvider != null)
                dataProvider.close();
            closed = true;
        }
    }
}