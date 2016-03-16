/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.desktop.gui.data.TableModelAdapter;
import com.haulmont.cuba.desktop.sys.vcl.JXTableExt;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.jdesktop.swingx.JXTable;

import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 */
public class DesktopTable<E extends Entity> extends DesktopAbstractTable<JXTable, E> {

    public DesktopTable() {
        impl = new JXTableExt() {
            @Override
            public void setFont(Font font) {
                super.setFont(font);
                applyFont(this, font);
            }

            /**
             * Default implementation uses row sorter to return rows count,
             * but there is nonconformity in how RowSorterImpl and table wrapper counts rows.
             * Absence of this method will lead to empty table in case of sortable=false.
             */
            @Override
            public int getRowCount() {
                return getModel().getRowCount();
            }

            @Override
            public void editingStopped(ChangeEvent e) {
                TableCellEditor editor = getCellEditor();
                if (editor != null) {
                    Object value = editor.getCellEditorValue();
                    DesktopTable<E> tableComponent = DesktopTable.this;
                    if (editingColumn >= 0) {
                        Column editColumn = tableComponent.getColumns().get(editingColumn);

                        if (!(editor instanceof DesktopAbstractTable.EditableColumnTableCellEditor)
                                && !(editor instanceof DesktopAbstractTable.CellProviderEditor)) {
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
            public TableCellRenderer getCellRenderer(int row, int column) {
                TableCellRenderer columnRenderer = DesktopTable.this.getColumnRenderer(column);
                if (columnRenderer != null) {
                    return columnRenderer;
                }

                return super.getCellRenderer(row, column);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                if (DesktopTable.this.isCustomCellEditable(row, column)) {
                    return true;
                }

                return super.isCellEditable(row, column);
            }

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                TableCellEditor cellEditor = getColumnEditor(column);
                if (cellEditor != null) {
                    return cellEditor;
                }

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