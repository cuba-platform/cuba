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
import org.jdesktop.swingx.JXTable;

import java.awt.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopGroupTable extends DesktopAbstractTable<JXTable> implements GroupTable {

    public DesktopGroupTable() {
        impl = new JXTable() {
            @Override
            public void setFont(Font font) {
                super.setFont(font);
                applyFont(this, font);
            }
        };
        initComponent();
        impl.setColumnControlVisible(true);
        DesktopComponentsHelper.correctTableFocusTraversal(impl);

        tableSettings = new SwingXTableSettings(impl, columnsOrder);
    }

    @Override
    protected void initTableModel(CollectionDatasource datasource) {
        tableModel = new TableModelAdapter(datasource, columnsOrder, true);
        impl.setModel(tableModel);
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

    @Override
    public void setSortable(boolean sortable) {
        super.setSortable(sortable);
        impl.setSortable(sortable);
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        impl.setEditable(editable);
    }
}
