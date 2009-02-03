/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 03.02.2009 13:01:37
 * $Id$
 */
package com.haulmont.cuba.gui.components;

public interface GridLayout extends Component.Container {
    void add(Component component, int col, int row);

    int getRows();
    void setRows(int rows);

    int getColumns();
    void setColumns(int columns);
}
