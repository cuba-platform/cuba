/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.fieldgrouplayout.CubaFieldGroupLayoutState;
import com.vaadin.ui.GridLayout;

import java.util.HashMap;
import java.util.Map;

public class CubaFieldGroupLayout extends GridLayout {

    protected Map<Integer, Integer> columnFieldCaptionWidth = null;

    public CubaFieldGroupLayout() {
        setHideEmptyRowsAndColumns(true);
    }

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