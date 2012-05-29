/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.gui.data.TableModelAdapter;
import com.haulmont.cuba.desktop.sys.vcl.TableFocusManager;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopTable extends DesktopAbstractTable<JXTable> {

    public DesktopTable() {
        impl = new JXTable() {
            protected TableFocusManager focusManager = new TableFocusManager(this);

            @Override
            protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
                if (focusManager.processKeyBinding(ks, e, condition, pressed))
                    return true;
                else
                    return super.processKeyBinding(ks, e, condition, pressed);
            }

            @Override
            protected void processFocusEvent(FocusEvent e) {
                focusManager.processFocusEvent(e);

                super.processFocusEvent(e);
            }

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
