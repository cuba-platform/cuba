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

public interface ExportDisplay
{
    void show(byte[] data, String name, ExportFormat format);
}
