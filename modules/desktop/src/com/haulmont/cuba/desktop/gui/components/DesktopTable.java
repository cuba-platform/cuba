/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.gui.data.TableModelAdapter;
import com.haulmont.cuba.desktop.sys.vcl.JXTableExt;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.jdesktop.swingx.JXTable;

import java.awt.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopTable extends DesktopAbstractTable<JXTable> {

    public DesktopTable() {
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
