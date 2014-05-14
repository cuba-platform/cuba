/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.fieldgrouplayout.CubaFieldGroupLayoutState;
import com.vaadin.ui.GridLayout;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaFieldGroupLayout extends GridLayout {

    @Override
    protected CubaFieldGroupLayoutState getState() {
        return (CubaFieldGroupLayoutState) super.getState();
    }

    @Override
    protected CubaFieldGroupLayoutState getState(boolean markAsDirty) {
        return (CubaFieldGroupLayoutState) super.getState(markAsDirty);
    }

    public int getFixedCaptionWidth() {
        return getState(false).fixedCaptionWidth;
    }

    public void setFixedCaptionWidth(int fixedCaptionWidth) {
        if (getState(false).fixedCaptionWidth != fixedCaptionWidth) {
            getState().fixedCaptionWidth = fixedCaptionWidth;
        }
    }

    public int getFixedCaptionWidth(int column) {
        if (column >= getColumns()) {
            return -1;
        }
        int[] columnCaptionWidth = getState(false).fixedColumnCaptionWidth;
        if (columnCaptionWidth != null && column < columnCaptionWidth.length) {
            return columnCaptionWidth[column];
        }
        return -1;
    }

    public void setFixedCaptionWidth(int column, int width) {
        if (column >= getColumns()) {
            throw new IllegalArgumentException("Could not find column with index " + column);
        }
        int[] columnCaptionWidth = getState(false).fixedColumnCaptionWidth;
        if (columnCaptionWidth == null || column >= columnCaptionWidth.length) {
            int[] newColumnCaptionWidth = new int[getColumns()];
            if (columnCaptionWidth != null) {
                System.arraycopy(columnCaptionWidth, 0, newColumnCaptionWidth, 0, columnCaptionWidth.length);
            }

            getState().fixedColumnCaptionWidth = newColumnCaptionWidth;
            columnCaptionWidth = newColumnCaptionWidth;
        }

        columnCaptionWidth[column] = width;
    }

    public boolean isUseInlineCaption() {
        return getState(false).useInlineCaption;
    }

    public void setUseInlineCaption(boolean useInlineCaption) {
        if (getState(false).useInlineCaption != useInlineCaption) {
            getState().useInlineCaption = useInlineCaption;
        }
    }
}