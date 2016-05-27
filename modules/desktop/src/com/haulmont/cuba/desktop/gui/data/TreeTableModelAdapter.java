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

package com.haulmont.cuba.desktop.gui.data;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.desktop.sys.vcl.JXTreeTableExt;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

public class TreeTableModelAdapter extends AbstractTreeTableModel implements AnyTableModelAdapter {

    protected JXTreeTable treeTable;
    protected TreeModelAdapter treeDelegate;
    protected TableModelAdapter tableDelegate;

    protected List<DataChangeListener> changeListeners = new ArrayList<>();

    public TreeTableModelAdapter(JXTreeTable treeTable, HierarchicalDatasource datasource, List<Table.Column> columns,
                                 boolean autoRefresh) {

        this.treeTable = treeTable;
        this.treeDelegate = createTreeModelAdapter(datasource, autoRefresh);
        this.tableDelegate = new TableModelAdapter(datasource, columns, autoRefresh);

        //noinspection unchecked
        datasource.addCollectionChangeListener(e -> {
            Object root1 = getRoot();
            // Fixes #1160
            JXTreeTableExt impl = (JXTreeTableExt) TreeTableModelAdapter.this.treeTable;
            impl.setAutoCreateColumnsFromModel(false);
            impl.backupExpandedNodes();

            for (DataChangeListener changeListener : changeListeners) {
                changeListener.beforeChange(true);
            }

            modelSupport.fireTreeStructureChanged(root1 == null ? null : new TreePath(root1));
        });
    }

    protected TreeModelAdapter createTreeModelAdapter(HierarchicalDatasource datasource, boolean autoRefresh) {
        return new TreeModelAdapter(datasource, CaptionMode.ITEM, null, autoRefresh);
    }

    @Override
    public int getColumnCount() {
        return tableDelegate.getColumnCount();
    }

    @Override
    public String getColumnName(int column) {
        return tableDelegate.getColumnName(column);
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        return tableDelegate.isCellEditable(0, column);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return tableDelegate.isCellEditable(rowIndex, columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return tableDelegate.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        tableDelegate.setValueAt(aValue, rowIndex, columnIndex);
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        tableDelegate.addTableModelListener(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        tableDelegate.removeTableModelListener(l);
    }

    @Override
    public void sort(List<? extends RowSorter.SortKey> sortKeys) {
        tableDelegate.sort(sortKeys);
    }

    @Override
    public int getRowCount() {
        return tableDelegate.getRowCount();
    }

    @Override
    public Entity getItem(int rowIndex) {
        TreePath treePath = treeTable.getPathForRow(rowIndex);
        return treeDelegate.getEntity(treePath.getLastPathComponent());
    }

    @Override
    public int getRowIndex(Entity entity) {
        TreePath treePath = getTreePath(entity);
        return treeTable.getRowForPath(treePath);
    }

    @Override
    public void addGeneratedColumn(Table.Column column) {
        tableDelegate.addGeneratedColumn(column);
    }

    @Override
    public void removeGeneratedColumn(Table.Column column) {
        tableDelegate.removeGeneratedColumn(column);
    }

    @Override
    public boolean isGeneratedColumn(Table.Column column) {
        return tableDelegate.isGeneratedColumn(column);
    }

    @Override
    public boolean hasGeneratedColumns() {
        return tableDelegate.hasGeneratedColumns();
    }

    @Override
    public void addColumn(Table.Column column) {
    }

    @Override
    public void removeColumn(Table.Column column) {
    }

    @Override
    public Table.Column getColumn(int index) {
        return tableDelegate.getColumn(index);
    }

    @Override
    public void addChangeListener(DataChangeListener changeListener) {
        changeListeners.add(changeListener);
    }

    @Override
    public void removeChangeListener(DataChangeListener changeListener) {
        changeListeners.remove(changeListener);
    }

    @Override
    public Object getValueAt(Object node, int column) {
        Entity entity = treeDelegate.getEntity(node);
        if (entity == null) {
            return column == 0 ? node : null;
        }
        return tableDelegate.getValueAt(entity, column);
    }

    @Override
    public Object getRoot() {
        return treeDelegate.getRoot();
    }

    @Override
    public Object getChild(Object parent, int index) {
        return treeDelegate.getChild(parent, index);
    }

    @Override
    public int getChildCount(Object parent) {
        return treeDelegate.getChildCount(parent);
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return treeDelegate.getIndexOfChild(parent, child);
    }

    public TreePath getTreePath(Object object) {
        return treeDelegate.getTreePath(object);
    }

    public Entity getEntity(Object object) {
        return treeDelegate.getEntity(object);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return tableDelegate.getColumnClass(column);
    }

    public void beforeDelayedStructureChange() {
    }

    public void afterDelayedStructureChange() {
        JXTreeTableExt impl = (JXTreeTableExt) TreeTableModelAdapter.this.treeTable;
        impl.restoreExpandedNodes();

        for (DataChangeListener changeListener : changeListeners) {
            changeListener.afterChange(true);
        }
    }
}