/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.gui.data.AnyTableModelAdapter;
import com.haulmont.cuba.desktop.gui.data.TableModelAdapter;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.table.TableModel;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopTable extends DesktopAbstractTable<JXTable> {

    public DesktopTable() {
        impl = new JXTable();
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
}
