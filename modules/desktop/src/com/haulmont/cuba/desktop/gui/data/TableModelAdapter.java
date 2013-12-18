/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsHelper;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class TableModelAdapter extends AbstractTableModel implements AnyTableModelAdapter {

    private static final long serialVersionUID = -3892470031734710618L;

    protected CollectionDatasource<Entity<Object>, Object> datasource;
    protected List<MetaPropertyPath> properties = new ArrayList<>();
    protected List<Table.Column> columns;
    protected List<Table.Column> generatedColumns = new ArrayList<>();
    protected boolean autoRefresh;
    protected List<DataChangeListener> changeListeners = new ArrayList<>();

    protected MetadataTools metadataTools = AppBeans.get(MetadataTools.class);

    public TableModelAdapter(CollectionDatasource datasource, List<Table.Column> columns, boolean autoRefresh) {

        this.datasource = datasource;
        this.columns = columns;
        this.autoRefresh = autoRefresh;

        final View view = datasource.getView();
        final MetaClass metaClass = datasource.getMetaClass();

        if (columns == null) {
            createProperties(view, metaClass);
        } else {
            for (Table.Column column : columns) {
                if (column.getId() instanceof MetaPropertyPath)
                    properties.add((MetaPropertyPath) column.getId());
            }
        }

        datasource.addListener(
                new CollectionDsListenerAdapter<Entity>() {
                    @Override
                    public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
                        switch (operation) {
                            case ADD:
                                fireBeforeChangeListeners(false);
                                for (Entity e : items) {
                                    int rowIndex = getRowIndex(e);
                                    fireTableRowsInserted(rowIndex, rowIndex);
                                }
                                fireAfterChangeListeners(true);
                                break;

                            case UPDATE:
                                fireBeforeChangeListeners(false);
                                for (Entity e : items) {
                                    int rowIndex = getRowIndex(e);
                                    fireTableRowsUpdated(rowIndex, rowIndex);
                                }
                                fireAfterChangeListeners(false);
                                break;

                            case REMOVE:
                            case CLEAR:
                            case REFRESH:
                                fireBeforeChangeListeners(true);
                                fireTableDataChanged();
                                fireAfterChangeListeners(true);
                                break;
                        }
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        int rowIndex = getRowIndex(source);

                        fireBeforeChangeListeners(false);
                        fireTableRowsUpdated(rowIndex, rowIndex);
                        fireAfterChangeListeners(false);
                    }
                }
        );
    }

    protected void fireBeforeChangeListeners(boolean structureChanged) {
        for (DataChangeListener changeListener : changeListeners)
            changeListener.beforeChange(structureChanged);
    }

    protected void fireAfterChangeListeners(boolean structureChanged) {
        for (DataChangeListener changeListener : changeListeners)
            changeListener.afterChange(structureChanged);
    }

    protected void createProperties(View view, MetaClass metaClass) {
        properties.addAll(CollectionDsHelper.createProperties(view, metaClass));
    }

    @Override
    public int getRowCount() {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        return datasource.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public String getColumnName(int column) {
        Table.Column c = columns.get(column);
        return c.getCaption();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object id = getItemId(rowIndex);

        Entity item = datasource.getItem(id);
        return getValueAt(item, columnIndex);
    }

    @SuppressWarnings("unchecked")
    public Object getValueAt(Entity item, int columnIndex) {
        Table.Column column = columns.get(columnIndex);
        if (column.getId() instanceof MetaPropertyPath) {
            String property = column.getId().toString();
            Object value = item.getValueEx(property);

            if (column.getFormatter() != null) {
                return column.getFormatter().format(value);
            }

            MetaPropertyPath metaProperty = ((MetaPropertyPath) column.getId());

            boolean isDataType = (metaProperty.getRange().isDatatype());
            if (isDataType && hasDefaultFormatting(metaProperty.getRangeJavaClass())) {
                if (value != null)
                    return value;
                else
                    return getDefaultValue(metaProperty.getRangeJavaClass());
            } else {
                if (value == null)
                    return null;

                return metadataTools.format(value, ((MetaPropertyPath) column.getId()).getMetaProperty());
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == -1)
            return false;
        Table.Column column = columns.get(columnIndex);
        if (column.isEditable() || generatedColumns.contains(column))
            return true;
        else
            return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Table.Column column = columns.get(columnIndex);
        if (!column.isEditable())
            return;

        Object id = getItemId(rowIndex);
        Entity item = datasource.getItem(id);

        if (column.getId() instanceof MetaPropertyPath) {
            String property = column.getId().toString();
            item.setValueEx(property, aValue);
        }
    }

    public Object getItemId(int rowIndex) {
        Object id = null;
        if (datasource instanceof CollectionDatasource.Ordered) {
            int idx = 0;
            id = ((CollectionDatasource.Ordered) datasource).firstItemId();
            while (++idx <= rowIndex) {
                id = ((CollectionDatasource.Ordered) datasource).nextItemId(id);
            }
        } else {
            Collection itemIds = datasource.getItemIds();
            int idx = 0;
            for (Object itemId : itemIds) {
                id = itemId;
                if (idx++ == rowIndex)
                    break;
            }
        }
        return id;
    }

    @Override
    public Entity getItem(int rowIndex) {
        Object itemId = getItemId(rowIndex);
        return datasource.getItem(itemId);
    }

    @Override
    public int getRowIndex(Entity entity) {
        int idx = 0;
        if (entity != null) {
            if (datasource instanceof CollectionDatasource.Ordered) {
                Object id = ((CollectionDatasource.Ordered) datasource).firstItemId();
                while (id != null) {
                    if (entity.equals(datasource.getItem(id)))
                        return idx;
                    id = ((CollectionDatasource.Ordered) datasource).nextItemId(id);
                    idx++;
                }
            } else {
                for (Entity item : datasource.getItems()) {
                    if (entity.equals(item))
                        return idx;
                    idx++;
                }
            }
        }
        return -1;
    }

    @Override
    public void addGeneratedColumn(Table.Column column) {
        generatedColumns.add(column);
    }

    @Override
    public void removeGeneratedColumn(Table.Column column) {
        generatedColumns.remove(column);
    }

    @Override
    public boolean isGeneratedColumn(Table.Column column) {
        return generatedColumns.contains(column);
    }

    @Override
    public void addColumn(Table.Column column) {
        columns.add(column);
        if (column.getId() instanceof MetaPropertyPath)
            properties.add((MetaPropertyPath) column.getId());

        fireTableStructureChanged();
    }

    @Override
    public void removeColumn(Table.Column column) {
        columns.remove(column);
        if (column.getId() instanceof MetaPropertyPath)
            properties.remove((MetaPropertyPath) column.getId());

        fireTableStructureChanged();
    }

    @Override
    public Table.Column getColumn(int index) {
        return columns.get(index);
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
    public void sort(List<? extends RowSorter.SortKey> sortKeys) {
        if (!(datasource instanceof CollectionDatasource.Sortable) || sortKeys == null)
            return;

        List<CollectionDatasource.Sortable.SortInfo> sortInfos = new ArrayList<>();
        for (RowSorter.SortKey sortKey : sortKeys) {
            if (!sortKey.getSortOrder().equals(SortOrder.UNSORTED)) {
                Table.Column c = columns.get(sortKey.getColumn());
                CollectionDatasource.Sortable.SortInfo<Object> sortInfo = new CollectionDatasource.Sortable.SortInfo<>();
                sortInfo.setPropertyPath(c.getId());
                sortInfo.setOrder(sortKey.getSortOrder().equals(SortOrder.ASCENDING)
                        ? CollectionDatasource.Sortable.Order.ASC
                        : CollectionDatasource.Sortable.Order.DESC);
                sortInfos.add(sortInfo);
            }
        }
        ((CollectionDatasource.Sortable) datasource).sort(
                sortInfos.toArray(new CollectionDatasource.Sortable.SortInfo[sortInfos.size()]));

        fireBeforeChangeListeners(true);
        fireTableDataChanged();
        fireAfterChangeListeners(true);

        for (DataChangeListener changeListener : changeListeners) {
            changeListener.dataSorted();
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Table.Column column = columns.get(columnIndex);
        Class columnType = column.getType();

        if (hasDefaultFormatting(columnType))
            return columnType;
        return super.getColumnClass(columnIndex);
    }

    private boolean hasDefaultFormatting(Class valueClass) {
        return Boolean.class.equals(valueClass);
    }

    private Object getDefaultValue(Class valueClass) {
        if (Boolean.class.equals(valueClass))
            return Boolean.FALSE;
        return null;
    }
}
