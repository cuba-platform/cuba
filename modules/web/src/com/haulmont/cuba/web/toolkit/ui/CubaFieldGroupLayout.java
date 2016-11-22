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
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CubaFieldGroupLayout extends GridLayout {

    protected int currentX = 0;
    protected int currentY = 0;

    protected Map<Object, Field> fields = new HashMap<>();

    protected Map<Integer, Integer> columnFieldCaptionWidth = null;

    public CubaFieldGroupLayout() {
        setHideEmptyRowsAndColumns(true);
        setSpacing(true);
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

    public void addField(Object propertyId, Field field) {
        addField(propertyId, field, currentX, currentY);
    }

    public void addField(Object propertyId, Field field, int col) {
        addField(propertyId, field, col, currentY);
    }

    public void addField(Object propertyId, Field field, int col, int row) {
        if (col < 0 || col >= getColumns() || row < 0 || row >= getRows()) {
            throw new IndexOutOfBoundsException();
        }

        currentX = col;
        currentY = row;

        attachField(propertyId, field);

        if (isReadOnly() != field.isReadOnly() && isReadOnly()) {
            field.setReadOnly(isReadOnly());
        }

        if (row < getRows()) {
            currentY++;
        } else if (col < getColumns()) {
            currentX++;
        }
    }

    protected void attachField(Object propertyId, Field field) {
        if (propertyId == null || field == null) {
            return;
        }

        Component oldComponent = getComponent(currentX, currentY);
        if (oldComponent != null) {
            removeComponent(oldComponent);
        }

        addComponent(field, currentX, currentY);

        fields.put(propertyId, field);
    }

    public void addCustomField(Object propertyId, CustomFieldGenerator fieldGenerator) {
        addCustomField(propertyId, fieldGenerator, currentX, currentY);
    }

    public void addCustomField(Object propertyId, CustomFieldGenerator fieldGenerator, int col) {
        addCustomField(propertyId, fieldGenerator, col, currentY);
    }

    public void addCustomField(Object propertyId, CustomFieldGenerator fieldGenerator, int col, int row) {
        Field field = fieldGenerator.generateField(propertyId, this);
        addField(propertyId, field, col, row);
    }

    public Field getField(Object propertyId) {
        return fields.get(propertyId);
    }

    public interface CustomFieldGenerator extends Serializable {
        com.vaadin.ui.Field generateField(Object propertyId, CubaFieldGroupLayout component);
    }
}