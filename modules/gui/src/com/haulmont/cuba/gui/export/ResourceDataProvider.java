/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.export;

import com.haulmont.cuba.core.global.ScriptingProvider;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;

/**
 * DataProvider for application resources
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class ResourceDataProvider implements ExportDataProvider {

    private String resourcePath;

    public ResourceDataProvider(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public InputStream provide() throws ResourceException {
        return ScriptingProvider.getResourceAsStream(resourcePath);
    }

    @Override
    public void close() {
        // do nothing
    }
}