/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.gui.data.TableModelAdapter;
import com.haulmont.cuba.desktop.sys.vcl.JXTableExt;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.jdesktop.swingx.JXTable;

import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopTable extends DesktopAbstractTable<JXTable> {

    public DesktopTable() {
        impl = new JXTableExt() {
            @Override
            public void setFont(Font font) {
                super.setFont(font);
                applyFont(this, font);
            }

            @Override
            public void editingStopped(ChangeEvent e) {
                TableCellEditor editor = getCellEditor();
                if (editor != null) {
                    Object value = editor.getCellEditorValue();
                    DesktopTable tableComponent = DesktopTable.this;
                    if (editingColumn >= 0) {
                        Column editColumn = tableComponent.getColumns().get(editingColumn);

                        if (!(editor instanceof DesktopAbstractTable.EditableColumnTableCellEditor)) {
                            if (tableComponent.isEditable() && editColumn.isEditable() &&
                                    !tableModel.isGeneratedColumn(editColumn)) {
                                setValueAt(value, editingRow, editingColumn);
                            }
                        }
                        removeEditor();
                    }
                }
            }

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                TableCellEditor tableCellEditor = DesktopTable.this.getCellEditor(row, column);
                if (tableCellEditor != null)
                    return tableCellEditor;

                return super.getCellEditor(row, column);
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
}