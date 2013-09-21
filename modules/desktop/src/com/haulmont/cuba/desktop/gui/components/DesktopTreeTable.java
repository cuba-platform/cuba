/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.desktop.gui.data.TreeTableModelAdapter;
import com.haulmont.cuba.desktop.sys.vcl.JXTreeTableExt;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopTreeTable
        extends DesktopAbstractTable<JXTreeTableExt>
        implements TreeTable {
    private String hierarchyProperty;

    protected Map<Integer, TableCellRenderer> cellRenderers = new HashMap<>();

    public DesktopTreeTable() {
        impl = new JXTreeTableExt() {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                TableCellRenderer cellRenderer = cellRenderers.get(column);
                if (cellRenderer != null) {
                    return cellRenderer;
                } else if (styleProvider != null) {
                    TableCellRenderer defaultRenderer = super.getCellRenderer(row, column);
                    return new StylingCellRenderer(defaultRenderer);
                } else {
                    return super.getCellRenderer(row, column);
                }
            }

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                TableCellRenderer cellRenderer = cellRenderers.get(column);
                if (cellRenderer instanceof TableCellEditor) {
                    return (TableCellEditor) cellRenderer;
                } else {
                    return super.getCellEditor(row, column);
                }
            }

            @Override
            public void setFont(Font font) {
                super.setFont(font);
                applyFont(this, font);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column < 0 || row < 0)
                    return false;

                DesktopTreeTable treeTable = DesktopTreeTable.this;
                Column editColumn = treeTable.getColumns().get(column);
                return (treeTable.isEditable() && editColumn.isEditable())
                        || tableModel.isGeneratedColumn(editColumn);
            }

            /* Copies the JTable standard behaviour, that was overridden in JXTreeTable,
             * using reflection to access private fields and methods.
             */
            @Override
            public void setRowSorter(RowSorter sorter) {
                try {
                    RowSorter oldRowSorter = null;

                    Class<?> cSortManager = ReflectionHelper.getClass("javax.swing.JTable$SortManager");
                    Field fSortManager = JTable.class.getDeclaredField("sortManager");
                    fSortManager.setAccessible(true);

                    Object sortManager = fSortManager.get(this);
                    if (sortManager != null) {
                        Field fSorter = cSortManager.getDeclaredField("sorter");
                        fSorter.setAccessible(true);
                        oldRowSorter = (RowSorter) fSorter.get(sortManager);
                        cSortManager.getDeclaredMethod("dispose").invoke(sortManager);
                        fSortManager.set(this, null);
                    }

                    Field fRowModel = JTable.class.getDeclaredField("rowModel");
                    fRowModel.setAccessible(true);
                    fRowModel.set(this, null);

                    Method mClearSelection = JTable.class.getDeclaredMethod("clearSelectionAndLeadAnchor");
                    mClearSelection.setAccessible(true);
                    mClearSelection.invoke(this);
                    if (sorter != null) {
                        Constructor cons =  cSortManager.getDeclaredConstructor(JTable.class, RowSorter.class);
                        cons.setAccessible(true);
                        fSortManager.set(this, cons.newInstance(this, sorter));
                    }
                    resizeAndRepaint();
                    firePropertyChange("rowSorter", oldRowSorter, sorter);
                    firePropertyChange("sorter", oldRowSorter, sorter);
                    configureSorterProperties();
                } catch (ReflectiveOperationException e) {
                    // In fact should never happen.
                    throw new RuntimeException("JXTreeTable row sorter is not set due to reflection exception", e);
                }
            }

            /* Default implementation uses row sorter to return rows count,
             * but there is nonconformity in how RowSorterImpl and tree table wrapper counts rows.
             * Absence of this method will lead to nontrivial NPE with obscure stack trace.
             */
            @Override
            public int getRowCount() {
                return getModel().getRowCount();
            }
        };

        impl.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                if (!cellRenderers.isEmpty())
                    repaint();
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                if (!cellRenderers.isEmpty())
                    repaint();
            }
        });
        impl.setRootVisible(false);
        impl.setColumnControlVisible(true);
        impl.setEditable(false);

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
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        impl.setEditable(editable);
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
                        if (isAdjusting) {
                            return;
                        }
                        selectedItems = getSelected();
                        disableItemListener = true;
                        // noinspection unchecked
                        if (selectedItems.isEmpty()) {
                            datasource.setItem(null);
                        } else {
                            datasource.setItem(selectedItems.iterator().next());
                        }
                        disableItemListener = false;
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

    @Override
    public <T extends Entity> Set<T> getSelected() {
        Set<T> selected = new HashSet<>();
        TreePath[] selectionPaths = impl.getTreeSelectionModel().getSelectionPaths();
        if (selectionPaths != null) {
            for (TreePath path : selectionPaths) {
                Entity entity = ((TreeTableModelAdapter) tableModel).getEntity(path.getLastPathComponent());
                if (entity != null)
                    selected.add((T) entity);
            }
        }
        return selected;
    }

    @Override
    public void setSelected(final Entity item) {
        if (item != null) {
            setSelected(Collections.singleton(item));
        } else {
            setSelected(Collections.<Entity>emptySet());
        }
    }

    @Override
    public void setSelected(Collection<Entity> items) {
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
                // default JXTreeTable renderer for hiehrhical column is JXTree
                ((JXTree) cellRenderer).setFont(font);
            }
        }
        super.applyFont(table, font);
    }

    @Override
    public void repaint() {
        TableCellEditor cellEditor = impl.getCellEditor();
        if (cellEditor instanceof DesktopTableCellEditor) {
            ((DesktopTableCellEditor) cellEditor).clearCache();
        }
        for (TableCellRenderer renderer : cellRenderers.values()) {
            if (renderer instanceof DesktopTableCellEditor) {
                ((DesktopTableCellEditor) renderer).clearCache();
            }
        }
        impl.repaint();
    }

    @Override
    protected void onDataChange() {
        for (TableCellRenderer renderer : cellRenderers.values()) {
            if (renderer instanceof DesktopTableCellEditor) {
                ((DesktopTableCellEditor) renderer).clearCache();
            }
        }
        super.onDataChange();
    }

    @Override
    public void setSortable(boolean sortable) {
        super.setSortable(sortable);
        impl.setSortable(sortable);
    }

    @Override
    public void addGeneratedColumn(String columnId, ColumnGenerator generator,
                                   Class<? extends com.haulmont.cuba.gui.components.Component> componentClass) {
        if (columnId == null)
            throw new IllegalArgumentException("columnId is null");
        if (generator == null)
            throw new IllegalArgumentException("generator is null");

        Column col = getColumn(columnId);
        tableModel.addGeneratedColumn(col);
        TableColumnModel columnModel = impl.getColumnModel();
        int columnIndex = columnModel.getColumnIndex(col);
        if (columnIndex == 0)
            throw new UnsupportedOperationException("Unable to add cell renderer for hierarchical column in TreeTable");
        cellRenderers.put(columnIndex, new DesktopTableCellEditor(this, generator, componentClass));
    }
}
