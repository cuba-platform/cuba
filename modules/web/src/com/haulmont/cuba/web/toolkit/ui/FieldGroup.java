/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 23.06.2010 11:02:45
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.toolkit.gwt.client.ui.VFieldGroup;
import com.vaadin.data.Item;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
@ClientWidget(VFieldGroup.class)
public class FieldGroup extends Form {

    private boolean expanded = true;
    private boolean switchable;

    private int currentX = 0;
    private int currentY = 0;

    private List<ExpandCollapseListener> listeners = null;

    public FieldGroup() {
        this(DefaultFieldFactory.get());
    }

    public FieldGroup(FormFieldFactory fieldFactory) {
        super();
        setFormFieldFactory(fieldFactory);
        setLayout(new FieldGroupLayout());
    }

    public boolean isExpanded() {
        return !switchable || expanded;
    }

    public void setExpanded(boolean expanded) {
        if (switchable) {
            this.expanded = expanded;
            getLayout().setVisible(expanded);
            requestRepaint();
        }
    }

    public boolean isSwitchable() {
        return switchable;
    }

    public void setSwitchable(boolean switchable) {
        this.switchable = switchable;
        if (!expanded) {
            setExpanded(true);
        }
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.addAttribute("switchable", isSwitchable());
        target.addAttribute("expanded", isExpanded());
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);

        if (isSwitchable()) {
            if (variables.containsKey("expand")) {
                setExpanded(true);
                getLayout().requestRepaintAll();

                fireExpandListeners();

            } else if (variables.containsKey("collapse")) {
                setExpanded(false);

                fireCollapseListeners();
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
        field.setReadOnly(isReadOnly());

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
        
        final FieldGroupLayout layout = getLayout();
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
            newLayout = new FieldGroupLayout();
        }
        if (newLayout instanceof FieldGroupLayout) {
            super.setLayout(newLayout);
            getLayout().setWidth("100%");
            getLayout().setSpacing(true);
        } else {
            throw new IllegalArgumentException("FieldGroup supports only FieldGroupLayout");
        }
    }

    @Override
    public FieldGroupLayout getLayout() {
        return (FieldGroupLayout) super.getLayout();
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

    public void addListener(ExpandCollapseListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<ExpandCollapseListener>();
        }
        listeners.add(listener);
    }

    public void removeListener(ExpandCollapseListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                listeners = null;
            }
        }
    }

    protected void fireExpandListeners() {
        if (listeners != null) {
            for (final ExpandCollapseListener listener : listeners) {
                listener.onExpand(this);
            }
        }
    }

    protected void fireCollapseListeners() {
        if (listeners != null) {
            for (final ExpandCollapseListener listener : listeners) {
                listener.onCollapse(this);
            }
        }
    }

    public interface ExpandCollapseListener extends Serializable {
        void onExpand(FieldGroup component);
        void onCollapse(FieldGroup component);
    }

    public interface CustomFieldGenerator extends Serializable {
        com.vaadin.ui.Field generateField(Item item, Object propertyId, FieldGroup component);
    }
}
