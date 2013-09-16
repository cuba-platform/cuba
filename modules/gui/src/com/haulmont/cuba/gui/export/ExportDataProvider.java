/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

    InputStream provide() throws ResourceException, ClosedDataProviderException;

    void close();
}
