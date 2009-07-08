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
import com.haulmont.cuba.core.global.ViewHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.HierarchicalDsWrapper;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.data.TreeTableContainer;
import com.haulmont.cuba.web.toolkit.ui.TableSupport;
import com.haulmont.bali.util.Dom4j;
import com.itmill.toolkit.terminal.Resource;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.dom4j.Element;

public class TreeTable
    extends
        AbstractTable<com.haulmont.cuba.web.toolkit.ui.TreeTable>
    implements
        com.haulmont.cuba.gui.components.TreeTable, Component.Wrapper
{
    protected String hierarchyProperty;

    public TreeTable() {
        component = new com.haulmont.cuba.web.toolkit.ui.TreeTable() {
            @Override
            public Resource getItemIcon(Object itemId) {
                if (styleProvider != null) {
                    @SuppressWarnings({"unchecked"})
                    final Entity item = datasource.getItem(itemId);
                    final String resURL = styleProvider.getItemIcon(item);

                    return resURL == null ? null : ComponentsHelper.getResource(resURL);
                } else {
                    return null;
                }
            }
        };
        initComponent(component);
    }

    public String getHierarchyProperty() {
        return hierarchyProperty;
    }

    public void setDatasource(HierarchicalDatasource datasource)
    {
        setDatasource((CollectionDatasource)datasource);
    }

    protected CollectionDsWrapper createContainerDatasource(CollectionDatasource datasource, Collection<MetaPropertyPath> columns) {
        return new TreeTableDsWrapper((HierarchicalDatasource) datasource);
    }

    protected void setVisibleColumns(List<MetaPropertyPath> columnsOrder) {
        component.setVisibleColumns(columnsOrder.toArray());
    }

    protected void setColumnHeader(MetaPropertyPath propertyPath, String caption) {
        component.setColumnHeader(propertyPath, caption);
    }

    public void setDatasource(CollectionDatasource datasource) {
        super.setDatasource(datasource);
        this.hierarchyProperty = ((HierarchicalDatasource) datasource).getHierarchyPropertyName();

        // if showProperty is null, the Tree will use itemId.toString
        MetaProperty metaProperty = hierarchyProperty == null ? null : datasource.getMetaClass().getProperty(hierarchyProperty);
        component.setItemCaptionPropertyId(metaProperty);
    }

    public void setRowHeaderMode(com.haulmont.cuba.gui.components.Table.RowHeaderMode rowHeaderMode) {
        switch (rowHeaderMode) {
            case NONE: {
                component.setRowHeaderMode(com.itmill.toolkit.ui.Table.ROW_HEADER_MODE_HIDDEN);
                break;
            }
            case ICON: {
                component.setRowHeaderMode(com.itmill.toolkit.ui.Table.ROW_HEADER_MODE_ICON_ONLY);
                break;
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }

    public void setStyleProvider(final StyleProvider styleProvider) {
        this.styleProvider = styleProvider;
        if (styleProvider == null) { component.setCellStyleGenerator(null); return; }

        component.setCellStyleGenerator(new com.haulmont.cuba.web.toolkit.ui.TreeTable.CellStyleGenerator () {
            public String getStyle(Object itemId, Object propertyId) {
                @SuppressWarnings({"unchecked"})
                final Entity item = datasource.getItem(itemId);
                return styleProvider.getStyleName(item, propertyId);
            }
        });
    }

    @Override
    protected void addGeneratedColumn(Object id, Object generator) {
        component.addGeneratedColumn(id, (TableSupport.ColumnGenerator) generator);
    }

    public void expandAll() {
        component.expandAll();
    }

    public void expand(Object itemId) {
        component.setExpanded(itemId);
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        component.setEditable(editable);
    }

    @Override
    public void setSortable(boolean sortable) {
        super.setSortable(sortable);
        component.setSortDisabled(!sortable);
    }

    @Override
    protected void initComponent(com.haulmont.cuba.web.toolkit.ui.TreeTable component) {
        super.initComponent(component);
        component.setSelectable(true);
        component.setSortDisabled(true);
        component.setColumnCollapsingAllowed(true);
        component.setColumnReorderingAllowed(true);
        setSortable(false);
    }

    public void applySettings(Element element) {
        final Element columnsElem = element.element("columns");
        if (columnsElem != null) {
            if (!Datasource.State.VALID.equals(getDatasource().getState())) {
                getDatasource().addListener(new DatasourceListener<Entity>() {
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                    }

                    public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
                        __applySettings(columnsElem);
                    }

                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                    }
                });
            } else {
                __applySettings(columnsElem);
            }
        }
    }

    private void __applySettings(Element columnsElem) {
        Object[] oldColumns = component.getVisibleColumns();
        List<Object> newColumns = new ArrayList<Object>();
        // add columns from saved settings
        for (Element colElem : Dom4j.elements(columnsElem, "columns")) {
            for (Object column : oldColumns) {
                if (column.toString().equals(colElem.attributeValue("id"))) {
                    newColumns.add(column);

                    String width = colElem.attributeValue("width");
                    if (width != null)
                        component.setColumnWidth(column, Integer.valueOf(width));

                    String visible = colElem.attributeValue("visible");
                    if (visible != null)
                        try {
                            component.setColumnCollapsed(column, !Boolean.valueOf(visible));
                        } catch (IllegalAccessException e) {
                            // ignore
                        }
                    break;
                }
            }
        }
        // add columns not saved in settings (perhaps new)
        for (Object column : oldColumns) {
            if (!newColumns.contains(column)) {
                newColumns.add(column);
            }
        }
        // if the table contains only one column, always show it
        if (newColumns.size() == 1) {
            try {
                component.setColumnCollapsed(newColumns.get(0), false);
            } catch (IllegalAccessException e) {
                //
            }
        }

        component.setVisibleColumns(newColumns.toArray());
    }

    public boolean saveSettings(Element element) {
        Element columnsElem = element.element("columns");
        if (columnsElem != null)
            element.remove(columnsElem);
        columnsElem = element.addElement("columns");

        Object[] visibleColumns = component.getVisibleColumns();
        for (Object column : visibleColumns) {
            Element colElem = columnsElem.addElement("columns");
            colElem.addAttribute("id", column.toString());

            int width = component.getColumnWidth(column);
            if (width > -1)
                colElem.addAttribute("width", String.valueOf(width));

            Boolean visible = !component.isColumnCollapsed(column);
            colElem.addAttribute("visible", visible.toString());
        }
        return true;
    }

    protected class TreeTableDsWrapper
            extends HierarchicalDsWrapper
            implements TreeTableContainer
    {
        protected boolean treeTableDatasource;

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

        public boolean isCaption(Object itemId) {
            if (treeTableDatasource) {
                return ((TreeTableDatasource<Entity, Object>) datasource).isCaption(itemId);
            }
            return false;
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
    }
}
