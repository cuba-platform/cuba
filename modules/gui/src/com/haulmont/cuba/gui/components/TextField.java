/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:10:30
 * $Id$
 */
package com.haulmont.cuba.gui.components;

public interface TextField extends Field {
    int getRows();
    void setRows(int rows);

    int getColumns();
    void setColumns(int columns);
}
