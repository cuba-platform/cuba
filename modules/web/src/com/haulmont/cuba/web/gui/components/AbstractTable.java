/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 06.04.2009 10:41:54
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.impl.BooleanDatatype;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.ui.TableSupport;
import com.itmill.toolkit.data.Item;
import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.data.Validator;
import com.itmill.toolkit.ui.*;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.Label;
import com.itmill.toolkit.ui.TextField;
import com.itmill.toolkit.event.ItemClickEvent;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class AbstractTable<T extends com.haulmont.cuba.web.toolkit.ui.Table>
        extends AbstractListComponent<T>
{

    protected Map<MetaPropertyPath, Table.Column> columns = new HashMap<MetaPropertyPath, Table.Column>();
    protected List<Table.Column> columnsOrder = new ArrayList<Table.Column>();
    protected Map<MetaClass, CollectionDatasource> optionsDatasources = new HashMap<MetaClass, CollectionDatasource>();
    protected boolean editable;
    protected boolean sortable;
    protected Action itemClickAction;

    protected Table.StyleProvider styleProvider;

    protected Map<Table.Column, String> requiredColumns = new HashMap<Table.Column, String>();

    protected Table.PagingMode pagingMode;

    protected Table.PagingProvider pagingProvider;

    protected Map<Table.Column, Set<com.haulmont.cuba.gui.components.Field.Validator>> validatorsMap =
            new HashMap<Table.Column, Set<com.haulmont.cuba.gui.components.Field.Validator>>();

    protected Set<com.haulmont.cuba.gui.components.Field.Validator> tableValidators =
            new LinkedHashSet<com.haulmont.cuba.gui.components.Field.Validator>();

    public java.util.List<Table.Column> getColumns() {
        return columnsOrder;
    }

    public Table.Column getColumn(String id) {
        for (Table.Column column : columnsOrder) {
            if (column.getId().toString().equals(id))
                return column;
        }
        return null;
    }

    public void addColumn(Table.Column column) {
        component.addContainerProperty(column.getId(), column.getType(), null);
        columns.put((MetaPropertyPath) column.getId(), column);
        columnsOrder.add(column);
        if (column.getWidth() != null) {
            component.setColumnWidth(column.getId(), column.getWidth());
        }
    }

    public void removeColumn(Table.Column column) {
        component.removeContainerProperty(column.getId());
        //noinspection RedundantCast
        columns.remove((MetaPropertyPath) column.getId());
        columnsOrder.remove(column);
    }

    protected void addGeneratedColumn(Object id, Object generator) {
        component.addGeneratedColumn(id, (com.itmill.toolkit.ui.Table.ColumnGenerator) generator);
    }

    protected void removeGeneratedColumn(Object id) {
        component.removeGeneratedColumn(id);
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        if (datasource != null) {
            refreshColumns(component.getContainerDataSource());
        }
        component.setEditable(editable);
    }

    protected void setEditableColumns(List<MetaPropertyPath> editableColumns) {
        component.setEditableColumns(editableColumns.toArray());
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    protected CollectionDatasource getOptionsDatasource(MetaClass metaClass, Table.Column column) {
        if (datasource == null)
            throw new IllegalStateException("Table datasource is null");

        final DsContext dsContext = datasource.getDsContext();

        String optDsName = column.getXmlDescriptor().attributeValue("optionsDatasource");
        if (StringUtils.isBlank(optDsName)) {
            CollectionDatasource ds = optionsDatasources.get(metaClass);
            if (ds != null) return ds;

            final DataService dataservice = datasource.getDataService();

            final String id = metaClass.getName();
            final String viewName = null; //metaClass.getName() + ".lookup";

            ds = new CollectionDatasourceImpl(dsContext, dataservice, id, metaClass, viewName);
            ds.refresh();

            optionsDatasources.put(metaClass, ds);

            return ds;
        } else {
            CollectionDatasource ds = dsContext.get(optDsName);
            if (ds == null)
                throw new IllegalStateException("Options datasource not found: " + optDsName);
            return ds;
        }
    }

    protected void initComponent(T component) {
        component.setMultiSelect(false);
        component.setNullSelectionAllowed(false);
        component.setImmediate(true);
        component.setValidationVisible(false);
        component.setStoreColWidth(true);
        component.setStyleName("table"); //It helps us to manage a caption style

        component.addActionHandler(new ActionsAdapter());

        setPagingMode(Table.PagingMode.SCROLLING);

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

        component.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick() && event.getItem() != null) {
                    Action action = getItemClickAction();
                    if (action == null) {
                        action = getAction("edit"); //todo gorodnov: check rights before the action performing
                        if (action == null) {
                            action = getAction("view");
                        }
                    }
                    if (action != null) {
                        action.actionPerform(AbstractTable.this);
                    }
                }
            }
        });
    }

    protected Collection<MetaPropertyPath> createColumns(com.itmill.toolkit.data.Container ds) {
        @SuppressWarnings({"unchecked"})
        final Collection<MetaPropertyPath> properties = (Collection<MetaPropertyPath>) ds.getContainerPropertyIds();
        for (MetaPropertyPath propertyPath : properties) {
            final Table.Column column = columns.get(propertyPath);
            if (column != null && !(editable && BooleanUtils.toBoolean(column.isEditable()))) {
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

                        if (BooleanDatatype.NAME.equals(datatype.getName()) && column.getFormatter() == null) {
                            addGeneratedColumn(propertyPath, new ReadOnlyBooleanDatatypeGenerator());
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

    protected void refreshColumns(com.itmill.toolkit.data.Container ds) {
        @SuppressWarnings({"unchecked"})
        final Collection<MetaPropertyPath> propertyIds = ds.getContainerPropertyIds();
        for (final MetaPropertyPath id : propertyIds) {
            removeGeneratedColumn(id);
        }
        createColumns(ds);
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

        if (columns == null) {
            throw new NullPointerException("Columns cannot be null");
        }

        List<MetaPropertyPath> editableColumns = null;
        if (isEditable()) {
            editableColumns = new LinkedList<MetaPropertyPath>();
        }

        for (final MetaPropertyPath propertyPath : columns) {
            final Table.Column column = this.columns.get(propertyPath);

            final String caption;
            if (column != null) {
                caption = StringUtils.capitalize(column.getCaption() != null ? column.getCaption() : propertyPath.getMetaProperty().getName());
            } else {
                caption = StringUtils.capitalize(propertyPath.getMetaProperty().getName());
            }

            setColumnHeader(propertyPath, caption);

            if (column != null) {
                if (editableColumns != null && column.isEditable()) {
                    editableColumns.add((MetaPropertyPath) column.getId());
                }

                if (column.isCollapsed() && component.isColumnCollapsingAllowed()) {
                    try {
                        component.setColumnCollapsed(column.getId(), true);
                    } catch (IllegalAccessException e) {
                        // do nothing
                    }
                }
            }
        }

        if (editableColumns != null) {
            setEditableColumns(editableColumns);
        }

        createColumns(containerDatasource);

        List<MetaPropertyPath> columnsOrder = new ArrayList<MetaPropertyPath>();
        for (Table.Column column : this.columnsOrder) {
            columnsOrder.add((MetaPropertyPath) column.getId());
        }

        setVisibleColumns(columnsOrder);
    }

    protected abstract CollectionDsWrapper createContainerDatasource(CollectionDatasource datasource, Collection<MetaPropertyPath> columns);

    protected void setVisibleColumns(List<MetaPropertyPath> columnsOrder) {
        component.setVisibleColumns(columnsOrder.toArray());
    }

    protected void setColumnHeader(MetaPropertyPath propertyPath, String caption) {
        component.setColumnHeader(propertyPath, caption);
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

    public void addValidator(final com.haulmont.cuba.gui.components.Field.Validator validator) {
        tableValidators.add(validator);
//        component.addValidator(new Validator() {
//            public void validate(Object value) throws InvalidValueException {
//                try {
//                    validator.validate(value);
//                } catch (ValidationException e) {
//                    throw new InvalidValueException(e.getMessage());
//                }
//            }
//
//            public boolean isValid(Object value) {
//                return validator.isValid(value);
//            }
//        });
    }

    public void validate() throws ValidationException {
        for (com.haulmont.cuba.gui.components.Field.Validator tableValidator : tableValidators) {
            tableValidator.validate(getSelected());
        }
    }

    public void setStyleProvider(final Table.StyleProvider styleProvider) {
        this.styleProvider = styleProvider;
        if (styleProvider == null) { component.setCellStyleGenerator(null); return; }

        component.setCellStyleGenerator(new com.itmill.toolkit.ui.Table.CellStyleGenerator () {
            public String getStyle(Object itemId, Object propertyId) {
                @SuppressWarnings({"unchecked"})
                final Entity item = datasource.getItem(itemId);
                return styleProvider.getStyleName(item, propertyId);
            }
        });
    }

    public void applySettings(Element element) {
        final Element columnsElem = element.element("columns");
        if (columnsElem != null) {
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

            //apply sorting
            String sortProp = columnsElem.attributeValue("sortProperty");
            if (!StringUtils.isEmpty(sortProp)) {
                MetaPropertyPath sortProperty = datasource.getMetaClass().getPropertyEx(sortProp);
                if (newColumns.contains(sortProperty)) {
                    boolean sortAscending = BooleanUtils.toBoolean(columnsElem.attributeValue("sortAscending"));

                    component.setSortContainerPropertyId(null);
                    component.setSortAscending(sortAscending);
                    component.setSortContainerPropertyId(sortProperty);
                }
            }
        }
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

        MetaPropertyPath sortProperty = (MetaPropertyPath) component.getSortContainerPropertyId();
        if (sortProperty != null) {
            Boolean sortAscending = component.isSortAscending();

            columnsElem.addAttribute("sortProperty", sortProperty.toString());
            columnsElem.addAttribute("sortAscending", sortAscending.toString());
        }

        return true;
    }

    public void setItemClickAction(Action action) {
        if (itemClickAction != null) {
            removeAction(itemClickAction);
        }
        itemClickAction = action;
        if (!getActions().contains(action)) {
            addAction(action);
        }
    }

    public Action getItemClickAction() {
        return itemClickAction;
    }

    public String getCaption() {
        return component.getCaption();
    }

    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    public Table.PagingMode getPagingMode() {
        return pagingMode;
    }

    public void setPagingMode(Table.PagingMode pagingMode) {
        this.pagingMode = pagingMode;
        component.setPagingMode(ComponentsHelper.convertPagingMode(pagingMode));
    }

    public void setPagingProvider(final Table.PagingProvider pagingProvider) {
        this.pagingProvider = pagingProvider;
        component.setPagingProvider(new com.haulmont.cuba.web.toolkit.ui.Table.PagingProvider() {
            public String firstCaption() {
                return pagingProvider.firstCaption();
            }

            public String prevCaption() {
                return pagingProvider.prevCaption();
            }

            public String nextCaption() {
                return pagingProvider.nextCaption();
            }

            public String lastCaption() {
                return pagingProvider.lastCaption();
            }
        });
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

    protected class FieldFactory extends BaseFieldFactory {
        @Override
        public com.itmill.toolkit.ui.Field createField(Class type, com.itmill.toolkit.ui.Component uiContext) {
            return super.createField(type, uiContext);
        }

        @Override
        public com.itmill.toolkit.ui.Field createField(Property property, com.itmill.toolkit.ui.Component uiContext) {
            return super.createField(property, uiContext);
        }

        @Override
        public com.itmill.toolkit.ui.Field createField(Item item, Object propertyId, com.itmill.toolkit.ui.Component uiContext) {
            return super.createField(item, propertyId, uiContext);
        }

        @Override
        public com.itmill.toolkit.ui.Field createField(com.itmill.toolkit.data.Container container, Object itemId, Object propertyId, com.itmill.toolkit.ui.Component uiContext) {
            final com.itmill.toolkit.ui.Field field;
            MetaPropertyPath propertyPath = (MetaPropertyPath) propertyId;
            final Table.Column column = columns.get(propertyPath);
            final Range range = propertyPath.getRange();
            if (range != null) {
                if (range.isClass()) {
                    final LookupField lookupField = new LookupField();
                    final CollectionDatasource optionsDatasource = getOptionsDatasource(range.asClass(), column);
                    lookupField.setOptionsDatasource(optionsDatasource);

                    field = (com.itmill.toolkit.ui.Field) ComponentsHelper.unwrap(lookupField);
                } else if (range.isEnum()) {
                    final LookupField lookupField = new LookupField();
                    if (propertyPath.get().length > 1) throw new UnsupportedOperationException();

                    lookupField.setDatasource(getDatasource(), propertyPath.getMetaProperty().getName());
                    lookupField.setOptionsList(range.asEnumiration().getValues());

                    field = (com.itmill.toolkit.ui.Field) ComponentsHelper.unwrap(lookupField);
                } else {
                    field = super.createField(container, itemId, propertyId, uiContext);
                }
            } else {
                field = super.createField(container, itemId, propertyId, uiContext);
            }
            ((com.itmill.toolkit.ui.AbstractField) field).setImmediate(true);
            if (field instanceof TextField) {
                ((TextField) field).setNullRepresentation("");
            }

            if (field instanceof com.itmill.toolkit.ui.TextField) {
                ((com.itmill.toolkit.ui.TextField) field).setNullRepresentation("");
            }

            boolean required = requiredColumns.containsKey(column);
            field.setRequired(required);
            if (required)
                field.setRequiredError(requiredColumns.get(column));

            Set<com.haulmont.cuba.gui.components.Field.Validator> validators = validatorsMap.get(column);
            if (validators != null) {
                for (final com.haulmont.cuba.gui.components.Field.Validator validator : validators) {

                    if (field instanceof com.itmill.toolkit.ui.AbstractField) {

                        field.addValidator(new Validator() {
                            public void validate(Object value) throws InvalidValueException {
                                if ((!field.isRequired() && value == null))
                                    return;
                                try {
                                    validator.validate(value);
                                } catch (ValidationException e) {
                                    throw new InvalidValueException(e.getMessage());
                                }
                            }

                            public boolean isValid(Object value) {
                                try {
                                    validate(value);
                                    return true;
                                } catch (InvalidValueException e) {
                                    return false;
                                }
                            }
                        });
                        ((com.itmill.toolkit.ui.AbstractField) field).setValidationVisible(false);
                    }
                }
            }
            return field;
        }
    }
}
