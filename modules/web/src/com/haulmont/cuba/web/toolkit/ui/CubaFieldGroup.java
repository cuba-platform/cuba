/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.fieldgroup.CubaFieldGroupState;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.*;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author gorodnov
 * @version $Id$
 */
public class CubaFieldGroup extends Form {

    protected int currentX = 0;
    protected int currentY = 0;

    public CubaFieldGroup() {
        this(DefaultFieldFactory.get());
    }

    public CubaFieldGroup(FormFieldFactory fieldFactory) {
        super();
        setFormFieldFactory(fieldFactory);
        setLayout(new CubaFieldGroupLayout());
    }

    public boolean isBorderVisible() {
        return getState().borderVisible;
    }

    public void setBorderVisible(boolean borderVisible) {
        if (getState().borderVisible != borderVisible) {
            getState().borderVisible = borderVisible;
            markAsDirty();
        }
    }

    @Override
    protected CubaFieldGroupState getState() {
        return (CubaFieldGroupState) super.getState();
    }

    public void setItemDataSource(Item newDataSource, Collection propertyIds) {
        if (super.getLayout() instanceof GridLayout) {
            GridLayout gl = (GridLayout) super.getLayout();
            if (gridlayoutCursorX == -1) {
                // first setItemDataSource, remember initial cursor
                gridlayoutCursorX = gl.getCursorX();
                gridlayoutCursorY = gl.getCursorY();
            } else {
                // restore initial cursor
                gl.setCursorX(gridlayoutCursorX);
                gl.setCursorY(gridlayoutCursorY);
            }
        }

        // Removes all fields first from the form
        removeAllProperties();

        // Sets the datasource
        itemDatasource = newDataSource;

        // If the new datasource is null, just set null datasource
        if (itemDatasource == null) {
            return;
        }

        // Adds all the properties to this form
        for (final Object id : propertyIds) {
            final Property property = itemDatasource.getItemProperty(id);
            if (id != null && property != null) {
                final Field f = fieldFactory.createField(itemDatasource, id,
                        this);
                if (f != null) {
                    f.setPropertyDataSource(property);
                    addField(id, f);
                }
            }
        }
    }

    @Override
    public void addField(Object propertyId, Field field) {
        addField(propertyId, field, currentX, currentY);
    }

    public void addField(Object propertyId, Field field, int col) {
        addField(propertyId, field, col, currentY);
    }

    public void addField(Object propertyId, Field field, int col, int row) {
        if (col < 0 || col >= getCols() || row < 0 || row >= getRows()) {
            throw new IndexOutOfBoundsException();
        }

        currentX = col;
        currentY = row;

        super.addField(propertyId, field);
        if (isReadOnly() != field.isReadOnly() && isReadOnly()) {
            field.setReadOnly(isReadOnly());
        }

        if (row < getRows()) {
            currentY++;
        } else if (col < getCols()) {
            currentX++;
        }
    }

    @Override
    protected void attachField(Object propertyId, Field field) {
        if (propertyId == null || field == null) {
            return;
        }

        final CubaFieldGroupLayout layout = getLayout();
        layout.addComponent(field, currentX, currentY);
    }

    public void addCustomField(Object propertyId, CustomFieldGenerator fieldGenerator) {
        addCustomField(propertyId, fieldGenerator, currentX, currentY);
    }

    public void addCustomField(Object propertyId, CustomFieldGenerator fieldGenerator, int col) {
        addCustomField(propertyId, fieldGenerator, col, currentY);
    }

    public void addCustomField(Object propertyId, CustomFieldGenerator fieldGenerator, int col, int row) {
        Field field = fieldGenerator.generateField(itemDatasource, propertyId, this);
        addField(propertyId, field, col, row);
    }

    @Override
    public void setLayout(Layout newLayout) {
        if (newLayout == null) {
            newLayout = new CubaFieldGroupLayout();
        }
        if (newLayout instanceof CubaFieldGroupLayout) {
            super.setLayout(newLayout);
            getLayout().setWidth("100%");
            getLayout().setSpacing(true);
        } else {
            throw new IllegalArgumentException("FieldGroup supports only FieldGroupLayout");
        }
    }

    @Override
    public CubaFieldGroupLayout getLayout() {
        return (CubaFieldGroupLayout) super.getLayout();
    }

    public float getColumnExpandRatio(int col) {
        return getLayout().getColumnExpandRatio(col);
    }

    public void setColumnExpandRatio(int col, float ratio) {
        getLayout().setColumnExpandRatio(col, ratio);
    }

    public int getCols() {
        return getLayout().getColumns();
    }

    public void setCols(int cols) {
        getLayout().setColumns(cols);
    }

    public int getRows() {
        return getLayout().getRows();
    }

    public void setRows(int rows) {
        getLayout().setRows(rows);
    }

    public interface CustomFieldGenerator extends Serializable {
        com.vaadin.ui.Field generateField(Item item, Object propertyId, CubaFieldGroup component);
    }
}