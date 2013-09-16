/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.export;

import java.io.InputStream;

/**
 * @author artamonov
 * @version $Id$
 */
public class ProxyDataProvider implements ExportDataProvider {

    private boolean closed = false;

    private InputStream dataInputStream = null;
    private ExportDataProvider dataProvider = null;

    public ProxyDataProvider(ExportDataProvider dataProvider) throws ClosedDataProviderException {
        this.dataProvider = dataProvider;
        this.dataInputStream = dataProvider.provide();
    }

    @Override
    public InputStream provide() throws ResourceException, ClosedDataProviderException {
        if (closed)
            throw new ClosedDataProviderException();

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