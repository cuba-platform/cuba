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
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.MetadataHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsHelper;
import com.haulmont.cuba.web.gui.data.*;
import com.haulmont.cuba.web.toolkit.data.AggregationContainer;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;

import java.util.*;

public class WebTable
    extends
        WebAbstractTable<com.haulmont.cuba.web.toolkit.ui.Table>
    implements
        Component.Wrapper
{
    private static final long serialVersionUID = -471562532396731699L;

    public WebTable() {
        component = new com.haulmont.cuba.web.toolkit.ui.Table() {
            @Override
            public Resource getItemIcon(Object itemId) {
                if (styleProvider != null) {
                    @SuppressWarnings({"unchecked"})
                    final Entity item = datasource.getItem(itemId);
                    final String resURL = styleProvider.getItemIcon(item);

                    return resURL == null ? null : WebComponentsHelper.getResource(resURL);
                } else {
                    return null;
                }
            }

            @Override
            protected boolean changeVariables(Map<String, Object> variables) {
                boolean b = super.changeVariables(variables);
                b = handleSpecificVariables(variables) || b;
                return b;
            }

            @Override
            public void paintContent(PaintTarget target) throws PaintException {
                super.paintContent(target);
                paintSpecificContent(target);
            }
        };
        initComponent(component);
    }

    @Override
    protected void initComponent(com.haulmont.cuba.web.toolkit.ui.Table component) {
        super.initComponent(component);
        setSortable(true);
    }

    protected CollectionDsWrapper createContainerDatasource(CollectionDatasource datasource, Collection<MetaPropertyPath> columns, DsManager dsManager) {
        return datasource instanceof CollectionDatasource.Sortable && isSortable() ?
            new SortableTableDsWrapper(datasource, columns, dsManager) :
            new TableDsWrapper(datasource, columns, dsManager);
    }

    protected class TableDsWrapper extends CollectionDsWrapper
            implements AggregationContainer {

        private List<Object> aggregationProperties = null;

        public TableDsWrapper(CollectionDatasource datasource, DsManager dsManager) {
            super(datasource, dsManager);
        }

        public TableDsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties, DsManager dsManager) {
            super(datasource, properties, dsManager);
        }

        @Override
        protected void createProperties(View view, MetaClass metaClass) {
            if (columns.isEmpty()) {
                super.createProperties(view, metaClass);
            } else {
                for (Map.Entry<Object, Column> entry : columns.entrySet()) {
                    if (entry.getKey() instanceof MetaPropertyPath) {
                        final MetaPropertyPath propertyPath = (MetaPropertyPath) entry.getKey();
                        if (view == null || MetadataHelper.viewContainsProperty(view, propertyPath)) {
                            properties.add(propertyPath);
                        }
                    }
                }
            }
        }

        @Override
        protected ItemWrapper createItemWrapper(Object item) {
            return new ItemWrapper(item, properties, dsManager) {
                @Override
                protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath, DsManager dsManager) {
                    return new TablePropertyWrapper(item, propertyPath, dsManager);
                }
            };
        }

        public Collection getAggregationPropertyIds() {
            if (aggregationProperties != null) {
                return Collections.unmodifiableList(aggregationProperties);
            }
            return Collections.emptyList();
        }

        public Type getContainerPropertyAggregation(Object propertyId) {
            throw new UnsupportedOperationException();
        }

        public void addContainerPropertyAggregation(Object propertyId, Type type) {
            if (aggregationProperties == null) {
                aggregationProperties = new LinkedList<Object>();
            } else if (aggregationProperties.contains(propertyId)) {
                throw new IllegalStateException("Such aggregation property is already exists");
            }
            aggregationProperties.add(propertyId);
        }

        public void removeContainerPropertyAggregation(Object propertyId) {
            if (aggregationProperties != null) {
                aggregationProperties.remove(propertyId);
                if (aggregationProperties.isEmpty()) {
                    aggregationProperties = null;
                }
            }
        }

        @SuppressWarnings("unchecked")
        public Map<Object, Object> aggregate(Context context) {
            return __aggregate(this, context);
        }
    }

    protected class SortableTableDsWrapper extends SortableCollectionDsWrapper
            implements AggregationContainer {

        private List<Object> aggregationProperties = null;

        public SortableTableDsWrapper(CollectionDatasource datasource, DsManager dsManager) {
            super(datasource, true, dsManager);
        }

        public SortableTableDsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties, DsManager dsManager) {
            super(datasource, properties, true, dsManager);
        }

        @Override
        protected void createProperties(View view, MetaClass metaClass) {
            if (columns.isEmpty()) {
                super.createProperties(view, metaClass);
            } else {
                for (Map.Entry<Object, Column> entry : columns.entrySet()) {
                    if (entry.getKey() instanceof MetaPropertyPath) {
                        final MetaPropertyPath propertyPath = (MetaPropertyPath) entry.getKey();
                        if (view == null || MetadataHelper.viewContainsProperty(view, propertyPath)) {
                            properties.add(propertyPath);
                        }
                    }
                }
            }
        }

        @Override
        protected ItemWrapper createItemWrapper(Object item) {
            return new ItemWrapper(item, properties, dsManager) {
                @Override
                protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath, DsManager dsManager) {
                    return new TablePropertyWrapper(item, propertyPath, dsManager);
                }
            };
        }

        public Collection getAggregationPropertyIds() {
            if (aggregationProperties != null) {
                return Collections.unmodifiableList(aggregationProperties);
            }
            return Collections.emptyList();
        }

        public Type getContainerPropertyAggregation(Object propertyId) {
            throw new UnsupportedOperationException();
        }

        public void addContainerPropertyAggregation(Object propertyId, Type type) {
            if (aggregationProperties == null) {
                aggregationProperties = new LinkedList<Object>();
            } else if (aggregationProperties.contains(propertyId)) {
                throw new IllegalStateException("Such aggregation property is already exists");
            }
            aggregationProperties.add(propertyId);
        }

        public void removeContainerPropertyAggregation(Object propertyId) {
            if (aggregationProperties != null) {
                aggregationProperties.remove(propertyId);
                if (aggregationProperties.isEmpty()) {
                    aggregationProperties = null;
                }
            }
        }

        @SuppressWarnings("unchecked")
        public Map<Object, Object> aggregate(Context context) {
            return __aggregate(this, context);
        }
    }
}
