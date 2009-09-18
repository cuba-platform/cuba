/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.08.2009 12:29:50
 *
 * $Id$
 */
package com.haulmont.cuba.gui.export;

import java.io.InputStream;

/**
 * Provides data for {@link ExportDisplay}.
 * Can be closed after usage to release resources.
 */
public interface ExportDataProvider {

    InputStream provide();

    void close();
}
