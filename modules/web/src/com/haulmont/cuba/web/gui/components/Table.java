/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 14:34:57
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.gui.data.SortableCollectionDsWrapper;
import com.itmill.toolkit.data.Item;
import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.ui.BaseFieldFactory;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.Label;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.*;

public class Table
    extends
        AbstractListComponent<com.itmill.toolkit.ui.Table> 
    implements
        com.haulmont.cuba.gui.components.Table, Component.Wrapper
{
    protected Map<MetaProperty, Table.Column> columns = new HashMap<MetaProperty, Column>();
    protected List<Table.Column> columnsOrder = new ArrayList<Column>();

    protected Map<MetaClass, CollectionDatasource> optionsDatasources = new HashMap<MetaClass, CollectionDatasource>();

    protected boolean editable;

    public Table() {
        component = new com.itmill.toolkit.ui.Table();

        component.setSelectable(true);
        component.setMultiSelect(false);
        component.setNullSelectionAllowed(false);
        component.setImmediate(true);

        component.addActionHandler(new ActionsAdapter());
        component.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                if (datasource == null) return;

                final Set selected = getSelected();
                if (selected.isEmpty()) {
                    //noinspection unchecked
                    datasource.setItem(null);
                } else {
                    //noinspection unchecked
                    datasource.setItem((Entity) selected.iterator().next());
                }
            }
        });
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

    protected CollectionDatasource getOptionsDatasource(MetaClass metaClass, Column column) {
        CollectionDatasource ds = optionsDatasources.get(metaClass);
        if (ds != null) return ds;

        if (datasource == null) throw new UnsupportedOperationException("Table datasource is null");

        final DataService dataservice = datasource.getDataService();
        final DsContext dsContext = datasource.getDsContext();

        final String id = metaClass.getName();
        final String viewName = null; //metaClass.getName() + ".lookup";

        ds = new CollectionDatasourceImpl(dsContext, dataservice, id, metaClass, viewName);
        ds.refresh();
        
        optionsDatasources.put(metaClass, ds);

        return ds;
    }

    public List<Column> getColumns() {
        return columnsOrder;
    }

    public void addColumn(Column column) {
        component.addContainerProperty(column.getId(), column.getType(), null);
        columns.put((MetaProperty) column.getId(), column);
        columnsOrder.add(column);
    }

    public void removeColumn(Column column) {
        component.removeContainerProperty(column.getId());
        //noinspection RedundantCast
        columns.remove((MetaProperty) column.getId());
        columnsOrder.remove(column);
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
        final CollectionDsWrapper ds =
                datasource instanceof CollectionDatasource.Sortable ?
                        new SortableTableDsWrapper(datasource) :
                        new TableDsWrapper(datasource);

        @SuppressWarnings({"unchecked"})
        final Collection<MetaProperty> properties = (Collection<MetaProperty>) ds.getContainerPropertyIds();
        for (MetaProperty metaProperty : properties) {
            final Column column = columns.get(metaProperty);
            if (column != null && !column.isEditable()) {
                final String clickAction =
                        column.getXmlDescriptor() == null ?
                                null : column.getXmlDescriptor().attributeValue("clickAction");

                if (metaProperty.getRange().isClass()) {
                    if (!StringUtils.isEmpty(clickAction)) {
                        component.addGeneratedColumn(metaProperty, new ReadOnlyAssociationGenerator(column));
                    }
                } else if (metaProperty.getRange().isDatatype()) {
                    if (!StringUtils.isEmpty(clickAction)) {
                        component.addGeneratedColumn(metaProperty, new CodePropertyGenerator(column));
                    } else {
                        if (editable) {
                            component.addGeneratedColumn(metaProperty, new ReadOnlyDatatypeGenerator());
                        }
                    }
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        }

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

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        component.setEditable(editable);
//        component.setRowHeaderMode(
//                editable ?
//                    com.itmill.toolkit.ui.Table.ROW_HEADER_MODE_INDEX :
//                    com.itmill.toolkit.ui.Table.ROW_HEADER_MODE_HIDDEN);
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

    protected abstract class LinkGenerator implements com.itmill.toolkit.ui.Table.ColumnGenerator {
        protected Column column;

        public LinkGenerator(Column column) {
            this.column = column;
        }

        public com.itmill.toolkit.ui.Component generateCell(com.itmill.toolkit.ui.Table source, final Object itemId, Object columnId) {
            final Item item = source.getItem(itemId);
            final Property property = item.getItemProperty(columnId);
            final Object value = property.getValue();

            final Button component = new Button();
            component.setData(value);
            component.setCaption(value == null ? "" : property.toString());
            component.setStyleName("link");

            component.addListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    final Element element = column.getXmlDescriptor();

                    final String clickAction = element.attributeValue("clickAction");
                    if (!StringUtils.isEmpty(clickAction)) {
                        if (clickAction.startsWith("open:")) {
                            final com.haulmont.cuba.gui.components.Window window = Table.this.getFrame();
                            window.openEditor(clickAction.substring("open:".length()), getItem(item, property), WindowManager.OpenType.THIS_TAB);
                        } else {
                            throw new UnsupportedOperationException();
                        }
                    }
                }
            });

            return component;
        }

        protected abstract Object getItem(Item item, Property property);
    }

    protected class ReadOnlyAssociationGenerator extends LinkGenerator {
        public ReadOnlyAssociationGenerator(Column column) {
            super(column);
        }

        protected Object getItem(Item item, Property property) {
            return property.getValue();
        }
    }

    protected class CodePropertyGenerator extends LinkGenerator {
        public CodePropertyGenerator(Column column) {
            super(column);
        }

        protected Object getItem(Item item, Property property) {
            return ((ItemWrapper) item).getItem();
        }
    }

    private static class ReadOnlyDatatypeGenerator implements com.itmill.toolkit.ui.Table.ColumnGenerator {
        public com.itmill.toolkit.ui.Component generateCell(com.itmill.toolkit.ui.Table source, Object itemId, Object columnId) {
            Property property = source.getItem(itemId).getItemProperty(columnId);
            final Object value = property.getValue();

            final Label label = new Label(value == null ? null : property.toString());
            label.setImmediate(true);

            return label;
        }
    }

    private class TablePropertyWrapper extends PropertyWrapper {
        private final MetaProperty property;

        public TablePropertyWrapper(Object item, MetaProperty property) {
            super(item, property);
            this.property = property;
        }

        @Override
        public boolean isReadOnly() {
            final Column column = Table.this.columns.get(property);
            if (column != null) {
                return !column.isEditable();
            } else {
                return super.isReadOnly();
            }
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            super.setReadOnly(newStatus);
        }

        @Override
        public String toString() {
            final Column column = Table.this.columns.get(property);
            if (column != null && column.getXmlDescriptor() != null) {
                String captionProperty = column.getXmlDescriptor().attributeValue("captionProperty");
                if (!StringUtils.isEmpty(captionProperty)) {
                    final Object value = getValue();
                    return metaProperty.getRange().isDatatype() ?
                            metaProperty.getRange().asDatatype().format(value) :
                            value == null ? null : String.valueOf(((Instance) value).getValue(captionProperty));
                } else {
                    return super.toString();
                }
            } else {
                return super.toString();
            }
        }
    }
}
