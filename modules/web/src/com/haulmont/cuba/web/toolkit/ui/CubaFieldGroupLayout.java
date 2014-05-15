/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.fieldgrouplayout.CubaFieldGroupLayoutState;
import com.vaadin.ui.GridLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaFieldGroupLayout extends GridLayout {

    protected Map<Integer, Integer> columnFieldCaptionWidth = null;

    @Override
    protected CubaFieldGroupLayoutState getState() {
        return (CubaFieldGroupLayoutState) super.getState();
    }

    @Override
    protected CubaFieldGroupLayoutState getState(boolean markAsDirty) {
        return (CubaFieldGroupLayoutState) super.getState(markAsDirty);
    }

    public int getFixedCaptionWidth() {
        return getState(false).fieldCaptionWidth;
    }

    public void setFixedCaptionWidth(int fixedCaptionWidth) {
        if (getState(false).fieldCaptionWidth != fixedCaptionWidth) {
            getState().fieldCaptionWidth = fixedCaptionWidth;
        }
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        if (initial && columnFieldCaptionWidth != null) {
            int[] newColumnFieldCaptionWidth = new int[getColumns()];
            for (Map.Entry<Integer, Integer> entry : columnFieldCaptionWidth.entrySet()) {
                int index = entry.getKey();
                int width = entry.getValue();

                if (index >= 0 && index < getColumns() && width > 0) {
                    newColumnFieldCaptionWidth[index] = width;
                }
            }
            getState().columnFieldCaptionWidth = newColumnFieldCaptionWidth;
        }
    }

    public int getFieldCaptionWidth(int column) {
        if (columnFieldCaptionWidth == null) {
            return -1;
        }

        Integer value = columnFieldCaptionWidth.get(column);
        return value != null ? value : -1;
    }

    public void setFieldCaptionWidth(int column, int width) {
        if (columnFieldCaptionWidth == null) {
            columnFieldCaptionWidth = new HashMap<>();
        }
        columnFieldCaptionWidth.put(column, width);
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