/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.export;

import java.io.InputStream;

/**
 * Provides data for {@link ExportDisplay}.
 * Can be closed after usage to release resources.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface ExportDataProvider {

    InputStream provide() throws ResourceException;

    void close();
}
