/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.export;

import java.io.InputStream;

/**
 * Provides data for {@link ExportDisplay}.
 *
 * @author krivopustov
 */
public interface ExportDataProvider {

    InputStream provide();

    @Deprecated
    default void close() {
    }
}