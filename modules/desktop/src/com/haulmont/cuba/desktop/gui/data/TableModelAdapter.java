/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.data;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsHelper;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class TableModelAdapter extends AbstractTableModel {

    private static final long serialVersionUID = -3892470031734710618L;

    protected CollectionDatasource<Entity<Object>, Object> datasource;
    protected List<MetaPropertyPath> properties = new ArrayList<MetaPropertyPath>();
    protected List<Table.Column> columns;
    protected boolean autoRefresh;

    public TableModelAdapter(
            CollectionDatasource datasource,
            List<Table.Column> columns,
            boolean autoRefresh)
    {
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
                new CollectionDsListenerAdapter() {
                    @Override
                    public void collectionChanged(CollectionDatasource ds, Operation operation) {
                        fireTableDataChanged();
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        fireTableDataChanged();
                    }
                }
        );
    }

    protected void createProperties(View view, MetaClass metaClass) {
        properties.addAll(CollectionDsHelper.createProperties(view, metaClass));
    }

    public int getRowCount() {
        CollectionDsHelper.autoRefreshInvalid(datasource, autoRefresh);
        return datasource.size();
    }

    public int getColumnCount() {
        return properties.size();
    }

    @Override
    public String getColumnName(int column) {
        Table.Column c = columns.get(column);
        return c.getCaption();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object id = getItemId(rowIndex);

        Entity item = datasource.getItem(id);
        Table.Column column = columns.get(columnIndex);
        if (column.getId() instanceof MetaPropertyPath) {
            String property = column.getId().toString();
            Object value = ((Instance) item).getValueEx(property);
            return MessageUtils.format(value, ((MetaPropertyPath) column.getId()).getMetaProperty());
        } else {
            return null;
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

    public Entity getItem(int rowIndex) {
        Object itemId = getItemId(rowIndex);
        return datasource.getItem(itemId);
    }
}
