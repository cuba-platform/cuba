/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.gui.data.DesktopTableModel;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.swing.*;
import javax.swing.table.TableModel;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopTable extends DesktopAbstractTable<JTable> {

    public DesktopTable() {
        impl = new JTable();
        initComponent();
    }

    @Override
    protected TableModel createTableModel(CollectionDatasource datasource) {
        return new DesktopTableModel(datasource, columnsOrder, true);
    }
}
