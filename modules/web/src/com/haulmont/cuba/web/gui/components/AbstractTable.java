/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 06.04.2009 10:41:54
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.impl.BooleanDatatype;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.ui.TableSupport;
import com.itmill.toolkit.data.Item;
import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.event.Action;
import com.itmill.toolkit.ui.*;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.Label;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.*;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractTable<T extends AbstractSelect> extends AbstractListComponent<T> {

    protected Map<MetaPropertyPath, Table.Column> columns = new HashMap<MetaPropertyPath, Table.Column>();
    protected List<Table.Column> columnsOrder = new ArrayList<Table.Column>();
    protected Map<MetaClass, CollectionDatasource> optionsDatasources = new HashMap<MetaClass, CollectionDatasource>();
    protected boolean editable;
    protected boolean sortable;

    protected Table.StyleProvider styleProvider;

    protected Map<Table.Column, String> requiredColumns = new HashMap<Table.Column, String>();

    protected Map<Table.Column, Set<com.haulmont.cuba.gui.components.Field.Validator>> validatorsMap =
            new HashMap<Table.Column, Set<com.haulmont.cuba.gui.components.Field.Validator>>();

    public java.util.List<Table.Column> getColumns() {
        return columnsOrder;
    }

    public Table.Column getColumn(String id) {
        for (Table.Column column : columnsOrder) {
            if (column.getId().toString().equals(id))
                return column;
        }
        throw new IllegalArgumentException("Column not found: " + id);
    }

    public void addColumn(Table.Column column) {
        component.addContainerProperty(column.getId(), column.getType(), null);
        columns.put((MetaPropertyPath) column.getId(), column);
        columnsOrder.add(column);
    }

    public void removeColumn(Table.Column column) {
        component.removeContainerProperty(column.getId());
        //noinspection RedundantCast
        columns.remove((MetaPropertyPath) column.getId());
        columnsOrder.remove(column);
    }

    protected abstract void addGeneratedColumn(Object id, Object generator);

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    protected CollectionDatasource getOptionsDatasource(MetaClass metaClass, Table.Column column) {
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

    protected void initComponent(T component) {
        component.setMultiSelect(false);
        component.setNullSelectionAllowed(false);
        component.setImmediate(true);

        if (component instanceof com.itmill.toolkit.event.Action.Container) {
            ((Action.Container) component).addActionHandler(new ActionsAdapter());
        }
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
    }

    protected Collection<MetaPropertyPath> createColumns(CollectionDsWrapper ds) {
        @SuppressWarnings({"unchecked"})
        final Collection<MetaPropertyPath> properties = (Collection<MetaPropertyPath>) ds.getContainerPropertyIds();
        for (MetaPropertyPath propertyPath : properties) {
            final Table.Column column = columns.get(propertyPath);
            if (column != null && !BooleanUtils.toBoolean(column.isEditable())) {
                final String clickAction =
                        column.getXmlDescriptor() == null ?
                                null : column.getXmlDescriptor().attributeValue("clickAction");

                if (propertyPath.getRange().isClass()) {
                    if (!StringUtils.isEmpty(clickAction)) {
                        addGeneratedColumn(propertyPath, new ReadOnlyAssociationGenerator(column));
                    }
                } else if (propertyPath.getRange().isDatatype()) {
                    if (!StringUtils.isEmpty(clickAction)) {
                        addGeneratedColumn(propertyPath, new CodePropertyGenerator(column));
                    } else {

                        final Datatype datatype = propertyPath.getRange()
                                .asDatatype();
                        if (BooleanDatatype.NAME.equals(datatype.getName())) {
                            addGeneratedColumn(propertyPath, new ReadOnlyBooleanDatatypeGenerator());
                        } else {
                            if (editable) {
                                addGeneratedColumn(propertyPath, new ReadOnlyDatatypeGenerator());
                            }
                        }

                    }
                } else if (propertyPath.getRange().isEnum()) {
                    // TODO (abramov) 
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        }

        return properties;
    }

    public void setDatasource(CollectionDatasource datasource) {

        final Collection<MetaPropertyPath> columns;
        if (this.columns.isEmpty()) {
            columns = null;
        } else {
            columns = this.columns.keySet();
        }

        final CollectionDsWrapper containerDatasource = createContainerDatasource(datasource, columns);

        this.datasource = datasource;


        component.setContainerDataSource(containerDatasource);
        createColumns(containerDatasource);

        for (MetaPropertyPath propertyPath : columns) {
            final Table.Column column = this.columns.get(propertyPath);

            final String caption;
            if (column != null) {
                caption = StringUtils.capitalize(column.getCaption() != null ? column.getCaption() : propertyPath.getMetaProperty().getName());
            } else {
                caption = StringUtils.capitalize(propertyPath.getMetaProperty().getName());
            }

            setColumnHeader(propertyPath, caption);
        }

        List<MetaPropertyPath> columnsOrder = new ArrayList<MetaPropertyPath>();
        for (Table.Column column : this.columnsOrder) {
            columnsOrder.add((MetaPropertyPath) column.getId());
        }

        setVisibleColumns(columnsOrder);
    }

    protected abstract CollectionDsWrapper createContainerDatasource(CollectionDatasource datasource, Collection<MetaPropertyPath> columns);
    protected abstract void setVisibleColumns(List<MetaPropertyPath> columnsOrder);
    protected abstract void setColumnHeader(MetaPropertyPath propertyPath, String caption);

    public void setRequired(Table.Column column, boolean required, String message) {
        if (required)
            requiredColumns.put(column, message);
        else
            requiredColumns.remove(column);
    }

    public void addValidator(Table.Column column, final com.haulmont.cuba.gui.components.Field.Validator validator) {
        Set<com.haulmont.cuba.gui.components.Field.Validator> validators = validatorsMap.get(column);
        if (validators == null) {
            validators = new HashSet<com.haulmont.cuba.gui.components.Field.Validator>();
            validatorsMap.put(column, validators);
        }
        validators.add(validator);
    }

    protected class TablePropertyWrapper extends PropertyWrapper {
        public TablePropertyWrapper(Object item, MetaPropertyPath propertyPath) {
            super(item, propertyPath);
        }

        @Override
        public boolean isReadOnly() {
            final Table.Column column = AbstractTable.this.columns.get(propertyPath);
            if (column != null) {
                return column.isEditable() != null && !column.isEditable();
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
            final Table.Column column = AbstractTable.this.columns.get(propertyPath);
            if (column != null && column.getXmlDescriptor() != null) {
                String captionProperty = column.getXmlDescriptor().attributeValue("captionProperty");
                if (column.getFormatter() != null) {
                    return column.getFormatter().format(getValue());
                } else if (!StringUtils.isEmpty(captionProperty)) {
                    final Object value = getValue();
                    return this.propertyPath.getRange().isDatatype() ?
                            this.propertyPath.getRange().asDatatype().format(value) :
                            value == null ? null : String.valueOf(((Instance) value).getValue(captionProperty));
                } else {
                    return super.toString();
                }
            } else {
                return super.toString();
            }
        }
    }

    protected abstract class LinkGenerator implements com.itmill.toolkit.ui.Table.ColumnGenerator, TableSupport.ColumnGenerator {
        protected Table.Column column;

        public LinkGenerator(Table.Column column) {
            this.column = column;
        }

        public com.itmill.toolkit.ui.Component generateCell(AbstractSelect source, final Object itemId, Object columnId) {
            final Item item = source.getItem(itemId);
            final Property property = item.getItemProperty(columnId);
            final Object value = property.getValue();

            final com.itmill.toolkit.ui.Button component = new Button();
            component.setData(value);
            component.setCaption(value == null ? "" : property.toString());
            component.setStyleName("link");

            component.addListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    final Element element = column.getXmlDescriptor();

                    final String clickAction = element.attributeValue("clickAction");
                    if (!StringUtils.isEmpty(clickAction)) {

                        if (clickAction.startsWith("open:")) {
                            final com.haulmont.cuba.gui.components.Window window = AbstractTable.this.getFrame();
                            String screenName = clickAction.substring("open:".length()).trim();
                            window.openEditor(screenName, getItem(item, property), WindowManager.OpenType.THIS_TAB);

                        } else if (clickAction.startsWith("invoke:")) {
                            final com.haulmont.cuba.gui.components.Window window = AbstractTable.this.getFrame();
                            String methodName = clickAction.substring("invoke:".length()).trim();
                            try {
                                Method method = window.getClass().getMethod(methodName, Object.class);
                                method.invoke(window, getItem(item, property));
                            } catch (NoSuchMethodException e) {
                                throw new RuntimeException("Unable to invoke clickAction", e);
                            } catch (InvocationTargetException e) {
                                throw new RuntimeException("Unable to invoke clickAction", e);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException("Unable to invoke clickAction", e);
                            }

                        } else {
                            throw new UnsupportedOperationException("Unsupported clickAction format: " + clickAction);
                        }
                    }
                }
            });

            return component;
        }

        public Component generateCell(com.itmill.toolkit.ui.Table source, Object itemId, Object columnId) {
            return generateCell(((AbstractSelect) source), itemId, columnId);
        }

        public Component generateCell(TableSupport source, Object itemId, Object columnId) {
            return generateCell(((AbstractSelect) source), itemId, columnId);
        }

        protected abstract Object getItem(Item item, Property property);
    }

    protected class ReadOnlyAssociationGenerator extends LinkGenerator {
        public ReadOnlyAssociationGenerator(Table.Column column) {
            super(column);
        }

        protected Object getItem(Item item, Property property) {
            return property.getValue();
        }
    }

    protected class CodePropertyGenerator extends LinkGenerator {
        public CodePropertyGenerator(Table.Column column) {
            super(column);
        }

        protected Object getItem(Item item, Property property) {
            return ((ItemWrapper) item).getItem();
        }
    }

    private class ReadOnlyDatatypeGenerator implements com.itmill.toolkit.ui.Table.ColumnGenerator, TableSupport.ColumnGenerator {
        protected Component generateCell(com.itmill.toolkit.ui.AbstractSelect source, Object itemId, Object columnId) {
            Property property = source.getItem(itemId).getItemProperty(columnId);
            final Object value = property.getValue();

            final Label label = new Label(value == null ? null : property.toString());
            label.setImmediate(true);

            return label;
        }

        public Component generateCell(com.itmill.toolkit.ui.Table source, Object itemId, Object columnId) {
            return generateCell(((AbstractSelect) source), itemId, columnId);
        }

        public Component generateCell(TableSupport source, Object itemId, Object columnId) {
            return generateCell(((AbstractSelect) source), itemId, columnId);
        }
    }

    private class ReadOnlyBooleanDatatypeGenerator
            implements com.itmill.toolkit.ui.Table.ColumnGenerator,
            TableSupport.ColumnGenerator
    {
        public Component generateCell(com.itmill.toolkit.ui.Table source, Object itemId, Object columnId) {
            return generateCell((AbstractSelect) source, itemId, columnId);
        }

        public Component generateCell(TableSupport source, Object itemId, Object columnId) {
            return generateCell((AbstractSelect) source, itemId, columnId);
        }

        protected Component generateCell(AbstractSelect source, Object itemId, Object columnId) {
            final Property property = source.getItem(itemId).getItemProperty(columnId);
            final Object value = property.getValue();

            final com.itmill.toolkit.ui.CheckBox checkBox = new com.itmill.toolkit.ui.CheckBox();
            checkBox.setValue(BooleanUtils.toBoolean((Boolean) value));
            checkBox.setEnabled(false);

            return checkBox;
        }
    }
}
