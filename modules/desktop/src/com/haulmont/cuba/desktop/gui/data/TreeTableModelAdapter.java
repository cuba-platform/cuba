/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.data;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.desktop.sys.vcl.JXTreeTableExt;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class TreeTableModelAdapter extends AbstractTreeTableModel implements AnyTableModelAdapter {

    private JXTreeTable treeTable;
    protected TreeModelAdapter treeDelegate;
    private TableModelAdapter tableDelegate;

    protected List<DataChangeListener> changeListeners = new ArrayList<>();

    public TreeTableModelAdapter(
            JXTreeTable treeTable,
            HierarchicalDatasource datasource,
            List<Table.Column> columns,
            boolean autoRefresh) {

        this.treeTable = treeTable;
        this.treeDelegate = createTreeModelAdapter(datasource, autoRefresh);
        this.tableDelegate = new TableModelAdapter(datasource, columns, autoRefresh);

        datasource.addListener(
                new CollectionDsListenerAdapter<Entity>() {
                    @Override
                    public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
                        Object root = getRoot();
                        // Fixes #1160
                        JXTreeTableExt impl = (JXTreeTableExt) TreeTableModelAdapter.this.treeTable;
                        impl.setAutoCreateColumnsFromModel(false);

                        for (DataChangeListener changeListener : changeListeners)
                            changeListener.beforeChange(true);

                        switch (operation) {
                            case CLEAR:
                            case REFRESH:
                            case ADD:
                            case REMOVE:
                                impl.backupExpandedNodes();
                                modelSupport.fireTreeStructureChanged(root == null ? null : new TreePath(root));
                                impl.restoreExpandedNodes();
                                break;

                            case UPDATE:
                                for (Entity item : items) {
                                    TreePath treePath = getTreePath(item);
                                    modelSupport.firePathChanged(treePath);
                                }
                                break;
                        }

                        for (DataChangeListener changeListener : changeListeners)
                            changeListener.afterChange(true);
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
}
