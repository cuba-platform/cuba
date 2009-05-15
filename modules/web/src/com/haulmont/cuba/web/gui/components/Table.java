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
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewHelper;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.gui.data.SortableCollectionDsWrapper;
import com.itmill.toolkit.data.Item;
import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.ui.BaseFieldFactory;

import java.util.Collection;
import java.util.Map;
import java.util.List;

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
                MetaPropertyPath propertyPath = (MetaPropertyPath) propertyId;
                final Range range = propertyPath.getRange();
                if (range != null) {
                    if (range.isClass()) {
                        final Column column = columns.get(propertyPath);

                        final LookupField lookupField = new LookupField();
                        final CollectionDatasource optionsDatasource = getOptionsDatasource(range.asClass(), column);
//                        final Entity item = optionsDatasource.getItem(itemId);

                        lookupField.setOptionsDatasource(optionsDatasource);
//                        lookupField.setDatasource(getDatasource(), metaProperty.getName());

                        return (com.itmill.toolkit.ui.Field) ComponentsHelper.unwrap(lookupField);
                    } else if (range.isEnum()) {
                        final LookupField lookupField = new LookupField();
                        if (propertyPath.get().length > 1) throw new UnsupportedOperationException();

                        lookupField.setDatasource(getDatasource(), propertyPath.getMetaProperty().getName());
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

    protected CollectionDsWrapper createContainerDatasource(CollectionDatasource datasource, Collection<MetaPropertyPath> columns) {
        return datasource instanceof CollectionDatasource.Sortable ?
            new SortableTableDsWrapper(datasource, columns) :
            new TableDsWrapper(datasource, columns);
    }

    protected void setVisibleColumns(List<MetaPropertyPath> columnsOrder) {
        component.setVisibleColumns(columnsOrder.toArray());
    }

    protected void setColumnHeader(MetaPropertyPath propertyPath, String caption) {
        component.setColumnHeader(propertyPath, caption);
    }

    public void setStyleProvider(final StyleProvider styleProvider) {
        if (styleProvider == null) {component.setCellStyleGenerator(null); return;}

        component.setCellStyleGenerator(new com.itmill.toolkit.ui.Table.CellStyleGenerator() {
            public String getStyle(Object itemId, Object propertyId) {
                final Entity item = datasource.getItem(itemId);
                return styleProvider.getStyleName(item, propertyId);
            }
        });
    }

    protected class TableDsWrapper extends CollectionDsWrapper {
        public TableDsWrapper(CollectionDatasource datasource) {
            super(datasource);
        }

        public TableDsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties) {
            super(datasource, properties);
        }

        @Override
        protected void createProperties(View view, MetaClass metaClass) {
            if (columns.isEmpty()) {
                super.createProperties(view, metaClass);
            } else {
                for (Map.Entry<MetaPropertyPath, Column> entry : columns.entrySet()) {
                    final MetaPropertyPath propertyPath = entry.getKey();
                    if (view == null || ViewHelper.contains(view, propertyPath)) {
                        properties.add(propertyPath);
                    }
                }
            }
        }

        @Override
        protected ItemWrapper createItemWrapper(Object item) {
            return new ItemWrapper(item, properties) {
                @Override
                protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                    final PropertyWrapper wrapper = new TablePropertyWrapper(item, propertyPath);

                    return wrapper;
                }
            };
        }

    }

    protected class SortableTableDsWrapper extends SortableCollectionDsWrapper {
        public SortableTableDsWrapper(CollectionDatasource datasource) {
            super(datasource);
        }

        public SortableTableDsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties) {
            super(datasource, properties);
        }

        @Override
        protected void createProperties(View view, MetaClass metaClass) {
            if (columns.isEmpty()) {
                super.createProperties(view, metaClass);
            } else {
                for (Map.Entry<MetaPropertyPath, Column> entry : columns.entrySet()) {
                    final MetaPropertyPath propertyPath = entry.getKey();
                    if (view == null || ViewHelper.contains(view, propertyPath)) {
                        properties.add(propertyPath);
                    }
                }
            }
        }

        @Override
        protected ItemWrapper createItemWrapper(Object item) {
            return new ItemWrapper(item, properties) {
                @Override
                protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                    final PropertyWrapper wrapper = new TablePropertyWrapper(item, propertyPath);

                    return wrapper;
                }
            };
        }
    }

}
