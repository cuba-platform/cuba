/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.06.2009 12:29:36
 *
 * $Id$
 */
package com.haulmont.cuba.gui.export;

/**
 * Generic interace to show data exported from the system.
 * <br>Use one of client-specific implementations.
 */
public interface ExportDisplay
{
    void show(ExportDataProvider dataProvider, String resourceName, ExportFormat format);
}
