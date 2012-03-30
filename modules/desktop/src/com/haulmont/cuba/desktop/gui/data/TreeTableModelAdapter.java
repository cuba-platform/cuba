/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.data;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.tree.TreePath;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class TreeTableModelAdapter extends AbstractTreeTableModel implements AnyTableModelAdapter {

    private JXTreeTable treeTable;
    protected TreeModelAdapter treeDelegate;
    private TableModelAdapter tableDelegate;

    public TreeTableModelAdapter(
            JXTreeTable treeTable,
            HierarchicalDatasource datasource,
            List<Table.Column> columns,
            boolean autoRefresh) {
        this.treeTable = treeTable;
        treeDelegate = createTreeModelAdapter(datasource, autoRefresh);
        tableDelegate = new TableModelAdapter(datasource, columns, autoRefresh);

        datasource.addListener(
                new CollectionDsListenerAdapter() {
                    @Override
                    public void collectionChanged(CollectionDatasource ds, Operation operation) {
                        Object root = getRoot();
                        modelSupport.fireTreeStructureChanged(root == null ? null : new TreePath(root));
                    }
                }
        );
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
        throw new UnsupportedOperationException();
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
    public void addColumn(Table.Column column) {
    }

    @Override
    public void removeColumn(Table.Column column) {
    }

    @Override
    public void addChangeListener(DataChangeListener changeListener) {
    }

    @Override
    public void removeChangeListener(DataChangeListener changeListener) {
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
}
