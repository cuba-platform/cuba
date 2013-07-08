/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.gui.data.PropertyValueStringify;
import com.haulmont.cuba.web.toolkit.data.util.TreeTableContainerWrapper;
import com.haulmont.cuba.web.toolkit.ui.client.treetable.CubaTreeTableState;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.HierarchicalContainer;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTreeTable extends com.vaadin.ui.TreeTable {

    @Override
    protected CubaTreeTableState getState() {
        return (CubaTreeTableState) super.getState();
    }

    @Override
    protected CubaTreeTableState getState(boolean markAsDirty) {
        return (CubaTreeTableState) super.getState(markAsDirty);
    }

    public boolean isTextSelectionEnabled() {
        return getState(false).textSelectionEnabled;
    }

    public void setTextSelectionEnabled(boolean textSelectionEnabled) {
        if (isTextSelectionEnabled() != textSelectionEnabled) {
            getState(true).textSelectionEnabled = textSelectionEnabled;
        }
    }

    @Override
    protected String formatPropertyValue(Object rowId, Object colId, Property<?> property) {
        if (property instanceof PropertyValueStringify)
            return ((PropertyValueStringify) property).getFormattedValue();

        return super.formatPropertyValue(rowId, colId, property);
    }

    @Override
    public void setContainerDataSource(Container newDataSource) {disableContentRefreshing();
        if (newDataSource == null) {
            newDataSource = new HierarchicalContainer();
        }

        super.setContainerDataSource(new TreeTableContainerWrapper(newDataSource));
    }

    public void expandAll() {
        for (Object id : getItemIds())
            setCollapsed(id, false);
    }

    public void collapseAll() {
        for (Object id : getItemIds())
            setCollapsed(id, true);
    }

    public void setExpanded(Object itemId) {
        setCollapsed(itemId, false);
    }

    public boolean isExpanded(Object itemId) {
        return !isCollapsed(itemId);
    }
}