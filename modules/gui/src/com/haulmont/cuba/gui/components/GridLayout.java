/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

/**
 * @author abramov
 * @version $Id$
 */
public interface GridLayout 
        extends Component.Container, Component.Spacing, Component.Margin, Component.BelongToFrame
{
    String NAME = "grid";

    float getColumnExpandRatio(int col);
    void setColumnExpandRatio(int col, float ratio);

    float getRowExpandRatio(int row);
    void setRowExpandRatio(int row, float ratio);

    void add(Component component, int col, int row);
    void add(Component component, int col, int row, int col2, int row2);

    int getRows();
    void setRows(int rows);

    int getColumns();
    void setColumns(int columns);
}