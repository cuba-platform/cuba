/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.toolkit.gwt.client.ui.VFieldGroupComposition;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
@ClientWidget(VFieldGroupComposition.class)
public class FieldGroupComposition extends GroupBox {

    protected int currentX = 0;
    protected int currentY = 0;

    protected boolean borderVisible = false;

    protected Map<Object, Field> fields = new HashMap<>();

    public FieldGroupComposition() {
        super(new FieldGroupLayout());

        setSizeUndefined();
    }

    public boolean isBorderVisible() {
        return borderVisible;
    }

    public void setBorderVisible(boolean borderVisible) {
        if (this.borderVisible != borderVisible) {
            this.borderVisible = borderVisible;
            requestRepaint();
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);

        for (Field field : fields.values()) {
            field.setReadOnly(readOnly);
        }
    }

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

        attachField(propertyId, field);

        if (isReadOnly() != field.isReadOnly() && isReadOnly()) {
            field.setReadOnly(isReadOnly());
        }

        if (row < getRows()) {
            currentY++;
        } else if (col < getCols()) {
            currentX++;
        }
    }

    protected void attachField(Object propertyId, Field field) {
        if (propertyId == null || field == null) {
            return;
        }

        final FieldGroupLayout layout = getContent();
        layout.addComponent(field, currentX, currentY);

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

    @Override
    public void setContent(ComponentContainer newLayout) {
        if (newLayout == null) {
            newLayout = new FieldGroupLayout();
        }
        if (newLayout instanceof FieldGroupLayout) {
            super.setContent(newLayout);

            getContent().setSpacing(true);
        } else {
            throw new IllegalArgumentException("FieldGroup supports only FieldGroupLayout");
        }
    }

    @Override
    public FieldGroupLayout getContent() {
        return (FieldGroupLayout) super.getContent();
    }

    public float getColumnExpandRatio(int col) {
        return getContent().getColumnExpandRatio(col);
    }

    public void setColumnExpandRatio(int col, float ratio) {
        getContent().setColumnExpandRatio(col, ratio);
    }

    public int getCols() {
        return getContent().getColumns();
    }

    public void setCols(int cols) {
        getContent().setColumns(cols);
    }

    public int getRows() {
        return getContent().getRows();
    }

    public void setRows(int rows) {
        getContent().setRows(rows);
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        if (borderVisible) {
            target.addAttribute("borderVisible", borderVisible);
        }
    }

    public Field getField(Object propertyId) {
        return fields.get(propertyId);
    }

    public interface CustomFieldGenerator extends Serializable {
        com.vaadin.ui.Field generateField(Object propertyId, FieldGroupComposition component);
    }
}