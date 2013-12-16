/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.desktop.gui.data.TableModelAdapter;
import com.haulmont.cuba.desktop.sys.vcl.JXTableExt;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.GroupInfo;
import org.jdesktop.swingx.JXTable;

import java.awt.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopGroupTable extends DesktopAbstractTable<JXTable> implements GroupTable {

    public DesktopGroupTable() {
        impl = new JXTableExt() {

            @Override
            public void setFont(Font font) {
                super.setFont(font);
                applyFont(this, font);
            }
        };
        initComponent();
        impl.setColumnControlVisible(true);

        tableSettings = new SwingXTableSettings(impl, columnsOrder);
    }

    @Override
    protected void initTableModel(CollectionDatasource datasource) {
        tableModel = new TableModelAdapter(datasource, columnsOrder, true);
        impl.setModel(tableModel);
    }

    @Override
    public void groupBy(Object[] properties) {
    }

    @Override
    public void expandAll() {
    }

    @Override
    public void expand(GroupInfo groupId) {
    }

    @Override
    public void expandPath(Entity item) {
    }

    @Override
    public void collapseAll() {
    }

    @Override
    public void collapse(GroupInfo groupId) {
    }

    @Override
    public boolean isExpanded(GroupInfo groupId) {
        return true;
    }

    @Override
    public boolean isFixedGrouping() {
        return false;
    }

    @Override
    public void setFixedGrouping(boolean groupingByUserEnabled) {
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