/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.gui.data.TableModelAdapter;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.GroupInfo;

import javax.swing.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopGroupTable extends DesktopAbstractTable<JTable> implements GroupTable {

    public DesktopGroupTable() {
        impl = new JTable();
        initComponent();
    }

    @Override
    protected TableModelAdapter createTableModel(CollectionDatasource datasource) {
        return new TableModelAdapter(datasource, columnsOrder, true);
    }

    public void groupBy(Object[] properties) {
    }

    public void expandAll() {
    }

    public void expand(GroupInfo groupId) {
    }

    public void collapseAll() {
    }

    public void collapse(GroupInfo groupId) {
    }

    public boolean isExpanded(GroupInfo groupId) {
        return true;
    }
}
