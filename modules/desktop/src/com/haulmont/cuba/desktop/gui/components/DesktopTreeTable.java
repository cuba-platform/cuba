/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.desktop.gui.data.TreeTableModelAdapter;
import com.haulmont.cuba.desktop.sys.vcl.JXTreeTableExt;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsHelper;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopTreeTable<E extends Entity>
        extends DesktopAbstractTable<JXTreeTableExt, E>
        implements TreeTable<E> {

    protected String hierarchyProperty;

    public DesktopTreeTable() {
        impl = new JXTreeTableExt() {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                TableCellRenderer columnRenderer = DesktopTreeTable.this.getColumnRenderer(column);
                if (columnRenderer != null) {
                    return columnRenderer;
                }

                if (styleProviders != null) {
                    TableCellRenderer defaultRenderer = super.getCellRenderer(row, column);
                    return new StylingCellRenderer(defaultRenderer);
                } else {
                    return super.getCellRenderer(row, column);
                }
            }

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                TableCellEditor cellEditor = getColumnEditor(column);
                if (cellEditor != null) {
                    return cellEditor;
                }

                return super.getCellEditor(row, column);
            }

            @Override
            public void setFont(Font font) {
                super.setFont(font);
                applyFont(this, font);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column < 0 || row < 0) {
                    return false;
                }

                if (DesktopTreeTable.this.isCustomCellEditable(row, column)) {
                    return true;
                }

                DesktopTreeTable<E> treeTable = DesktopTreeTable.this;
                Column editColumn = treeTable.getColumns().get(column);
                return (treeTable.isEditable() && editColumn.isEditable())
                        || tableModel.isGeneratedColumn(editColumn);
            }

            /* Copies the JTable standard behaviour, that was overridden in JXTreeTable.
             */
            @Override
            public void setRowSorter(RowSorter<? extends TableModel> sorter) {
                setTableRowSorter(sorter);
            }

            /**
             * Default implementation uses row sorter to return rows count,
             * but there is nonconformity in how RowSorterImpl and tree table wrapper counts rows.
             * Absence of this method will lead to nontrivial NPE with obscure stack trace.
             */
            @Override
            public int getRowCount() {
                if (datasource != null) {
                    CollectionDsHelper.autoRefreshInvalid(datasource, true);
                }

                return getModel().getRowCount();
            }

            @Override
            public void editingStopped(ChangeEvent e) {
                TableCellEditor editor = getCellEditor();
                if (editor != null) {
                    Object value = editor.getCellEditorValue();
                    DesktopTreeTable<E> tableComponent = DesktopTreeTable.this;
                    Column editColumn = tableComponent.getColumns().get(editingColumn);

                    if (!(editor instanceof DesktopAbstractTable.EditableColumnTableCellEditor)) {
                        if (tableComponent.isEditable() && editColumn.isEditable() && !tableModel.isGeneratedColumn(editColumn)) {
                            setValueAt(value, editingRow, editingColumn);
                        }
                    }
                    removeEditor();
                }
            }

//            @Override
//            protected void beforeDelayedStructureChange() {
//                ((com.haulmont.cuba.desktop.gui.data.TreeTableModelAdapter) tableModel).beforeDelayedStructureChange();
//            }
//
//            @Override
//            protected void afterDelayedStructureChange() {
//                ((com.haulmont.cuba.desktop.gui.data.TreeTableModelAdapter) tableModel).afterDelayedStructureChange();
//            }
        };

        impl.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                if (tableModel.hasGeneratedColumns())
                    repaint();
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                if (tableModel.hasGeneratedColumns())
                    repaint();
            }
        });
        impl.setRootVisible(false);
        impl.setColumnControlVisible(true);

        // apply alternate row color
        impl.setHighlighters(new AbstractHighlighter() {
            @Override
            protected Component doHighlight(Component component, ComponentAdapter adapter) {
                if (adapter.isHierarchical()) {
                    if (adapter.isSelected()) {
                        component.setBackground(impl.getSelectionBackground());
                        component.setForeground(impl.getSelectionForeground());
                    } else {
                        component.setForeground(impl.getForeground());
                        Color background = UIManager.getDefaults().getColor("Table:\"Table.cellRenderer\".background");
                        if (adapter.row % 2 == 1) {
                            Color alternateColor = UIManager.getDefaults().getColor("Table.alternateRowColor");
                            if (alternateColor != null) {
                                background = alternateColor;
                            }
                        }
                        component.setBackground(background);
                    }
                }
                return component;
            }
        });

        initComponent();

        tableSettings = new SwingXTableSettings(impl, columnsOrder);
    }

    @Override
    protected void initTableModel(CollectionDatasource datasource) {
        tableModel = new TreeTableModelAdapter(
                impl,
                ((HierarchicalDatasource) datasource),
                columnsOrder,
                true
        );
        impl.setTreeTableModel(((TreeTableModelAdapter) tableModel));
    }

    @Override
    protected void initSelectionListener(final CollectionDatasource datasource) {
        impl.getTreeSelectionModel().addTreeSelectionListener(
                new TreeSelectionListener() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public void valueChanged(TreeSelectionEvent e) {
                        if (isAdjusting || datasource == null) {
                            return;
                        }
                        selectedItems = getSelected();

                        // noinspection unchecked
                        if (selectedItems.isEmpty()) {
                            datasource.setItem(null);
                        } else {
                            // reset selection and select new item
                            if (isMultiSelect())
                                datasource.setItem(null);
                            datasource.setItem(selectedItems.iterator().next());
                        }
                    }
                }
        );
    }

    @Override
    public String getHierarchyProperty() {
        return hierarchyProperty;
    }

    @Override
    public void setDatasource(HierarchicalDatasource datasource) {
        setDatasource((CollectionDatasource) datasource);
        this.hierarchyProperty = datasource.getHierarchyPropertyName();
    }

    @Override
    public void expandAll() {
        TreeTableModelAdapter model = (TreeTableModelAdapter) tableModel;
        if (!model.isLeaf(model.getRoot())) {
            recursiveExpand(model.getRoot());
        }
        readjustColumns();
    }

    private void recursiveExpand(Object node) {
        TreeTableModelAdapter model = (TreeTableModelAdapter) tableModel;
        impl.expandPath(model.getTreePath(node));
        for (int i = 0; i < model.getChildCount(node); i++) {
            Object child = model.getChild(node, i);
            if (!model.isLeaf(child)) {
                impl.expandPath(model.getTreePath(child));
                recursiveExpand(child);
            }
        }
    }

    @Override
    public void expand(Object itemId) {
        if (datasource == null)
            return;
        Entity<Object> item = datasource.getItem(itemId);
        if (item == null)
            return;

        impl.expandPath(((TreeTableModelAdapter) tableModel).getTreePath(item));
    }

    @Override
    public void collapseAll() {
        if (tableModel == null)
            return;

        impl.collapsePath(new TreePath(((TreeTableModelAdapter) tableModel).getRoot()));
        readjustColumns();
    }

    @Override
    public void collapse(Object itemId) {
        if (datasource == null)
            return;
        Entity<Object> item = datasource.getItem(itemId);
        if (item == null)
            return;

        impl.collapsePath(((TreeTableModelAdapter) tableModel).getTreePath(item));
    }

    @Override
    public int getLevel(Object itemId) {
        Object parentId;
        if ((parentId = ((HierarchicalDatasource) datasource).getParent(itemId)) == null) {
            return 0;
        }
        return getLevel(parentId) + 1;
    }

    @Override
    public boolean isExpanded(Object itemId) {
        if (datasource == null)
            return false;
        Entity<Object> item = datasource.getItem(itemId);
        if (item == null)
            return false;

        return impl.isExpanded(((TreeTableModelAdapter) tableModel).getTreePath(item));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<E> getSelected() {
        Set<E> selected = new HashSet<>();
        TreePath[] selectionPaths = impl.getTreeSelectionModel().getSelectionPaths();
        if (selectionPaths != null) {
            for (TreePath path : selectionPaths) {
                Entity entity = ((TreeTableModelAdapter) tableModel).getEntity(path.getLastPathComponent());
                if (entity != null)
                    selected.add((E) entity);
            }
        }
        return selected;
    }

    @Override
    public void setSelected(E item) {
        if (item != null) {
            setSelected(Collections.singleton(item));
        } else {
            setSelected(Collections.<E>emptySet());
        }
    }

    @Override
    public void setSelected(Collection<E> items) {
        if (items == null) {
            items = Collections.emptySet();
        }
        for (Entity item : items) {
            // noinspection unchecked
            if (!datasource.containsItem(item.getId())) {
                throw new IllegalStateException("Datasource does not contain specified item: " + item.getId());
            }
        }
        impl.clearSelection();
        if (!items.isEmpty()) {
            for (Entity item : items) {
                TreePath treePath = ((TreeTableModelAdapter) tableModel).getTreePath(item);
                impl.getTreeSelectionModel().addSelectionPath(treePath);
            }
        }
    }

    @Override
    public void packRows() {
        if (!contentRepaintEnabled)
            return;

        impl.setRowHeight(defaultRowHeight);
    }

    @Override
    protected void applyFont(JTable table, Font font) {
        JXTreeTable treeTable = (JXTreeTable) table;
        if (treeTable.getModel() != null && impl != null) {
            int hierarchicalColumn = treeTable.getHierarchicalColumn();
            TableCellRenderer cellRenderer = treeTable.getCellRenderer(0, hierarchicalColumn);
            if (cellRenderer instanceof DesktopAbstractTable.StylingCellRenderer) {
                cellRenderer = ((DesktopAbstractTable.StylingCellRenderer) cellRenderer).getDelegate();
            }
            if (cellRenderer instanceof JXTree) {
                // default JXTreeTable renderer for hierarchical column is JXTree
                ((JXTree) cellRenderer).setFont(font);
            }
        }
        super.applyFont(table, font);
    }

    @Override
    public void setSortable(boolean sortable) {
        super.setSortable(sortable);
        impl.setSortable(sortable);
    }

    @Override
    public HierarchicalDatasource getDatasource() {
        return (HierarchicalDatasource) super.getDatasource();
    }

    @Override
    public void addGeneratedColumn(String columnId, ColumnGenerator generator,
                                   Class<? extends com.haulmont.cuba.gui.components.Component> componentClass) {
        if (columnId == null)
            throw new IllegalArgumentException("columnId is null");
        if (generator == null)
            throw new IllegalArgumentException("generator is null");

        Column col = getColumn(columnId);

        TableColumnModel columnModel = impl.getColumnModel();
        int columnIndex = columnModel.getColumnIndex(col);
        if (columnIndex == 0)
            throw new UnsupportedOperationException("Unable to add cell renderer for hierarchical column in TreeTable");

        addGeneratedColumnInternal(columnId, generator, componentClass);
    }
}