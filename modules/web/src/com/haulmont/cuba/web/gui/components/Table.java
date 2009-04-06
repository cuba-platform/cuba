/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 14:34:57
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.gui.data.SortableCollectionDsWrapper;
import com.itmill.toolkit.data.Item;
import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.ui.BaseFieldFactory;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Table
    extends
        AbstractTable<com.itmill.toolkit.ui.Table>
    implements
        com.haulmont.cuba.gui.components.Table, Component.Wrapper
{

    public Table() {
        component = new com.itmill.toolkit.ui.Table();
        initComponent(component);
    }

    @Override
    protected void initComponent(com.itmill.toolkit.ui.Table component) {
        super.initComponent(component);

        component.setSelectable(true);
        component.setFieldFactory(new BaseFieldFactory() {
            @Override
            public com.itmill.toolkit.ui.Field createField(Class type, com.itmill.toolkit.ui.Component uiContext) {
                return super.createField(type, uiContext);    //To change body of overridden methods use File | Settings | File Templates.
            }

            @Override
            public com.itmill.toolkit.ui.Field createField(Property property, com.itmill.toolkit.ui.Component uiContext) {
                return super.createField(property, uiContext);    //To change body of overridden methods use File | Settings | File Templates.
            }

            @Override
            public com.itmill.toolkit.ui.Field createField(Item item, Object propertyId, com.itmill.toolkit.ui.Component uiContext) {
                return super.createField(item, propertyId, uiContext);    //To change body of overridden methods use File | Settings | File Templates.
            }

            @Override
            public com.itmill.toolkit.ui.Field createField(com.itmill.toolkit.data.Container container, Object itemId, Object propertyId, com.itmill.toolkit.ui.Component uiContext) {
                MetaProperty metaProperty = (MetaProperty) propertyId;
                final Range range = metaProperty.getRange();
                if (range != null) {
                    if (range.isClass()) {
                        final Column column = columns.get(metaProperty);

                        final LookupField lookupField = new LookupField();
                        final CollectionDatasource optionsDatasource = getOptionsDatasource(range.asClass(), column);
//                        final Entity item = optionsDatasource.getItem(itemId);

                        lookupField.setOptionsDatasource(optionsDatasource);
//                        lookupField.setDatasource(getDatasource(), metaProperty.getName());

                        return (com.itmill.toolkit.ui.Field) ComponentsHelper.unwrap(lookupField);
                    } else if (range.isEnum()) {
                        final LookupField lookupField = new LookupField();
                        lookupField.setDatasource(getDatasource(), metaProperty.getName());
                        lookupField.setOptionsList(range.asEnumiration().getValues());

                        return (com.itmill.toolkit.ui.Field) ComponentsHelper.unwrap(lookupField);
                    } else {
                        return super.createField(container, itemId, propertyId, uiContext);
                    }
                } else {
                    return super.createField(container, itemId, propertyId, uiContext);
                }
            }
        });
    }

    @Override
    protected void addGeneratedColumn(Object id, Object generator) {
        component.addGeneratedColumn(id, (com.itmill.toolkit.ui.Table.ColumnGenerator) generator);
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        component.setEditable(editable);
//        component.setRowHeaderMode(
//                editable ?
//                    com.itmill.toolkit.ui.Table.ROW_HEADER_MODE_INDEX :
//                    com.itmill.toolkit.ui.Table.ROW_HEADER_MODE_HIDDEN);
    }

    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
        final CollectionDsWrapper ds =
                datasource instanceof CollectionDatasource.Sortable ?
                        new SortableTableDsWrapper(datasource) :
                        new TableDsWrapper(datasource);

        @SuppressWarnings({"unchecked"})
        final Collection<MetaProperty> properties = createColumns(ds);

        component.setContainerDataSource(ds);

        for (MetaProperty metaProperty : properties) {
            final Column column = columns.get(metaProperty);

            final String caption;
            if (column != null) {
                caption = StringUtils.capitalize(column.getCaption() != null ? column.getCaption() : metaProperty.getName());
            } else {
                caption = StringUtils.capitalize(metaProperty.getName());
            }

            component.setColumnHeader(metaProperty, caption);
        }

        @SuppressWarnings({"unchecked"})
        final Collection<MetaProperty> collection = component.getContainerPropertyIds();
        if (!columns.isEmpty()) {
            for (MetaProperty metaProperty : collection) {
                if (!columns.containsKey(metaProperty)) {
                    component.removeContainerProperty(metaProperty);
                }
            }
        }

        List<MetaProperty> columnsOrder = new ArrayList<MetaProperty>();
        for (Column column : this.columnsOrder) {
            columnsOrder.add((MetaProperty) column.getId());
        }

        component.setVisibleColumns(columnsOrder.toArray());
    }

    protected class TableDsWrapper extends CollectionDsWrapper {
        public TableDsWrapper(CollectionDatasource datasource) {
            super(datasource);
        }

        @Override
        protected void createProperties(View view, MetaClass metaClass) {
            if (columns.isEmpty()) {
                super.createProperties(view, metaClass);
            } else {
                for (Map.Entry<MetaProperty, Column> entry : columns.entrySet()) {
                    final MetaProperty metaProperty = entry.getKey();
                    if (view == null || view.getProperty(metaProperty.getName()) != null) {
                        properties.add(metaProperty);
                    }
                }
            }
        }

        @Override
        protected ItemWrapper createItemWrapper(Object item) {
            return new ItemWrapper(item, properties) {
                @Override
                protected PropertyWrapper createPropertyWrapper(Object item, MetaProperty property) {
                    final PropertyWrapper wrapper = new TablePropertyWrapper(item, property);

                    return wrapper;
                }
            };
        }

    }

    protected class SortableTableDsWrapper extends SortableCollectionDsWrapper {
        public SortableTableDsWrapper(CollectionDatasource datasource) {
            super(datasource);
        }

        @Override
        protected void createProperties(View view, MetaClass metaClass) {
            if (columns.isEmpty()) {
                super.createProperties(view, metaClass);
            } else {
                for (Map.Entry<MetaProperty, Column> entry : columns.entrySet()) {
                    final MetaProperty metaProperty = entry.getKey();
                    if (view == null || view.getProperty(metaProperty.getName()) != null) {
                        properties.add(metaProperty);
                    }
                }
            }
        }

        @Override
        protected ItemWrapper createItemWrapper(Object item) {
            return new ItemWrapper(item, properties) {
                @Override
                protected PropertyWrapper createPropertyWrapper(Object item, MetaProperty property) {
                    final PropertyWrapper wrapper = new TablePropertyWrapper(item, property);

                    return wrapper;
                }
            };
        }
    }

}
