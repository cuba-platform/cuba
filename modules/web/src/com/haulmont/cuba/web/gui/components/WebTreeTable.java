/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 06.04.2009 10:39:36
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.MetadataHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.TreeTableDatasource;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.HierarchicalDsWrapper;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.data.AggregationContainer;
import com.haulmont.cuba.web.toolkit.data.TreeTableContainer;
import com.vaadin.data.Item;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;

import java.util.*;

public class WebTreeTable
    extends
        WebAbstractTable<com.haulmont.cuba.web.toolkit.ui.TreeTable>
    implements
        TreeTable, Component.Wrapper
{
    protected String hierarchyProperty;
    
    private static final long serialVersionUID = 4587793124533649610L;

    public WebTreeTable() {
        component = new com.haulmont.cuba.web.toolkit.ui.TreeTable() {
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
    public void setDatasource(CollectionDatasource datasource) {
        super.setDatasource(datasource);
        this.hierarchyProperty = ((HierarchicalDatasource) datasource).getHierarchyPropertyName();

        // if showProperty is null, the Tree will use itemId.toString
        MetaProperty metaProperty = hierarchyProperty == null ? null : datasource.getMetaClass().getProperty(hierarchyProperty);
        component.setItemCaptionPropertyId(metaProperty);
    }

    public String getHierarchyProperty() {
        return hierarchyProperty;
    }

    public void setDatasource(HierarchicalDatasource datasource) {
        setDatasource((CollectionDatasource) datasource);
    }

    protected CollectionDsWrapper createContainerDatasource(
            CollectionDatasource datasource, Collection<MetaPropertyPath> columns) {
        return new TreeTableDsWrapper((HierarchicalDatasource) datasource);
    }

    public void expandAll() {
        component.expandAll();
    }

    public void expand(Object itemId) {
        component.setExpanded(itemId);
    }

    public void collapseAll() {
        component.collapseAll();
    }

    public void collapse(Object itemId) {
        component.setCollapsed(itemId);
    }

    public int getLevel(Object itemId) {
        return component.getLevel(itemId);
    }

    public boolean isExpanded(Object itemId) {
        return component.isExpanded(itemId);
    }

    protected class TreeTableDsWrapper
            extends HierarchicalDsWrapper
            implements TreeTableContainer, com.vaadin.data.Container.Sortable, AggregationContainer
    {
        protected boolean treeTableDatasource;

        private List<Object> aggregationProperties = null;

        public TreeTableDsWrapper(HierarchicalDatasource datasource) {
            super(datasource);
            treeTableDatasource  = (datasource instanceof TreeTableDatasource);
        }

        @Override
        protected void createProperties(View view, MetaClass metaClass) {
            if (columns.isEmpty()) {
                super.createProperties(view, metaClass);
            } else {
                for (Map.Entry<MetaPropertyPath, Column> entry : columns.entrySet()) {
                    final MetaPropertyPath propertyPath = entry.getKey();
                    if (view == null || MetadataHelper.viewContainsProperty(view, propertyPath)) {
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
                    return new TablePropertyWrapper(item, propertyPath);
                }
            };
        }

        public boolean isCaption(Object itemId) {
            return treeTableDatasource && ((TreeTableDatasource<Entity, Object>) datasource)
                    .isCaption(itemId);
        }

        public String getCaption(Object itemId) {
            if (treeTableDatasource) {
                return ((TreeTableDatasource<Entity, Object>) datasource).getCaption(itemId);
            }
            return null;
        }

        public boolean setCaption(Object itemId, String caption) {
            throw new UnsupportedOperationException();
        }

        public int getLevel(Object itemId) {
            return getItemLevel(itemId);
        }

        protected int getItemLevel(Object itemId) {
            Object parentId;
            if ((parentId = getParent(itemId)) == null) {
                return 0;
            }
            return getItemLevel(parentId) + 1;
        }

        public void sort(Object[] propertyId, boolean[] ascending) {
            List<CollectionDatasource.Sortable.SortInfo> infos = new ArrayList<CollectionDatasource.Sortable.SortInfo>();
            for (int i = 0; i < propertyId.length; i++) {
                final MetaPropertyPath propertyPath = (MetaPropertyPath) propertyId[i];

                final CollectionDatasource.Sortable.SortInfo<MetaPropertyPath> info =
                        new CollectionDatasource.Sortable.SortInfo<MetaPropertyPath>();
                info.setPropertyPath(propertyPath);
                info.setOrder(ascending[i] ? CollectionDatasource.Sortable.Order.ASC : CollectionDatasource.Sortable.Order.DESC);

                infos.add(info);
            }
            ((CollectionDatasource.Sortable<Entity, Object>) datasource).sort(infos.toArray(new CollectionDatasource.Sortable.SortInfo[infos.size()]));
        }

        public Collection getSortableContainerPropertyIds() {
            return properties;
        }

        public Object nextItemId(Object itemId) {
            return ((CollectionDatasource.Sortable<Entity, Object>) datasource).nextItemId(itemId);
        }

        public Object prevItemId(Object itemId) {
            return ((CollectionDatasource.Sortable<Entity, Object>) datasource).prevItemId(itemId);
        }

        public Object firstItemId() {
            return ((CollectionDatasource.Sortable<Entity, Object>) datasource).firstItemId();
        }

        public Object lastItemId() {
            return ((CollectionDatasource.Sortable<Entity, Object>) datasource).lastItemId();
        }

        public boolean isFirstId(Object itemId) {
            return ((CollectionDatasource.Sortable<Entity, Object>) datasource).isFirstId(itemId);
        }

        public boolean isLastId(Object itemId) {
            return ((CollectionDatasource.Sortable<Entity, Object>) datasource).isLastId(itemId);
        }

        public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
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

        public Map<Object, Object> aggregate(Context context) {
            return __aggregate(this, context);
        }
    }
}
