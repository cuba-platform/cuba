/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.layout;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class GridLayoutAdapter extends LayoutAdapter {

    protected int rowCount;
    protected int colCount;

    protected float[] columnRatio;
    protected float[] rowRatio;

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

        rowRatio = new float[rows];
        Arrays.fill(rowRatio, 1.0f);

        update();
    }

    public int getColumns() {
        return colCount;
    }

    public void setColumns(int columns) {
        colCount = columns;
        columnRatio = new float[columns];
        //Arrays.fill(columnRatio, 1.0f); // 0.0 by default

        update();
    }

    public abstract Object getConstraints(
            com.haulmont.cuba.gui.components.Component component, int col, int row, int col2, int row2);

    public abstract Object getCaptionConstraints(com.haulmont.cuba.gui.components.Component component,
            int col, int row, int col2, int row2
    );

    public abstract void updateConstraints(JComponent component, Object constraints);

    public void setColumnExpandRatio(int col, float ratio) {
        columnRatio[col] = ratio;
        update();
    }

    public float getColumnExpandRatio(int col) {
        return columnRatio[col];
    }

    public float getRowExpandRatio(int col) {
        return rowRatio[col];
    }

    public void setRowExpandRatio(int col, float ratio) {
        rowRatio[col] = ratio;
    }


}
