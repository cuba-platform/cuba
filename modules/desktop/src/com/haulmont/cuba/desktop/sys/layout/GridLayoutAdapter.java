/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.layout;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class GridLayoutAdapter extends LayoutAdapter {

    protected int rowCount;
    protected int colCount;

    public static GridLayoutAdapter create(JComponent container) {
        MigGridLayoutAdapter layoutAdapter = new MigGridLayoutAdapter(container);
        container.setLayout(layoutAdapter.getLayout());
        return layoutAdapter;
    }

    public static GridLayoutAdapter create(LayoutManager layout, JComponent container) {
        if (layout instanceof MigLayout) {
            MigGridLayoutAdapter layoutAdapter = new MigGridLayoutAdapter((MigLayout) layout, container);
            container.setLayout(layoutAdapter.getLayout());
            return layoutAdapter;
        }
        else
            throw new UnsupportedOperationException("Unsupported layout manager: " + layout);
    }

    public int getRows() {
        return rowCount;
    }

    public void setRows(int rows) {
        rowCount = rows;
        update();
    }

    public int getColumns() {
        return colCount;
    }

    public void setColumns(int columns) {
        colCount = columns;
        update();
    }

    public abstract void add(Component component, int col, int row, int col2, int row2);

    public abstract void add(Component component);
}
