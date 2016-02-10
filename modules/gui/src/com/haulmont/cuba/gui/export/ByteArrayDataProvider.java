/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.export;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author krivopustov
 */
public class ByteArrayDataProvider implements ExportDataProvider {

    private byte[] data;

    public ByteArrayDataProvider(byte[] data) {
        this.data = data;
    }

    @Override
    public InputStream provide() {
        return new ByteArrayInputStream(data);
    }
}