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
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.gui.data.impl.CollectionDsActionsNotifier;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.gui.presentations.PresentationsImpl;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Presentation;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.AbstractFieldFactory;
import com.haulmont.cuba.web.gui.CompositionLayout;
import com.haulmont.cuba.web.gui.components.presentations.TablePresentations;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.DsManager;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.data.AggregationContainer;
import com.haulmont.cuba.web.toolkit.ui.CheckBox;
import com.haulmont.cuba.web.toolkit.ui.TableSupport;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.*;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import com.haulmont.cuba.web.toolkit.ui.CheckBox;

public abstract class WebAbstractTable<T extends com.haulmont.cuba.web.toolkit.ui.Table>
        extends WebAbstractList<T> implements Table {

    protected Map<Object, Table.Column> columns = new HashMap<Object, Table.Column>();
    protected List<Table.Column> columnsOrder = new ArrayList<Table.Column>();
    protected Map<MetaClass, CollectionDatasource> optionsDatasources = new HashMap<MetaClass, CollectionDatasource>();
    protected boolean editable;
    protected boolean sortable = true;
    protected Action itemClickAction;
    protected Action enterPressAction;

    protected Table.StyleProvider styleProvider;

    protected Map<Table.Column, String> requiredColumns = new HashMap<Table.Column, String>();

    protected Table.PagingMode pagingMode;

    protected Table.PagingProvider pagingProvider;

    protected Map<Table.Column, Set<com.haulmont.cuba.gui.components.Field.Validator>> validatorsMap =
            new HashMap<Table.Column, Set<com.haulmont.cuba.gui.components.Field.Validator>>();

    protected Set<com.haulmont.cuba.gui.components.Field.Validator> tableValidators =
            new LinkedHashSet<com.haulmont.cuba.gui.components.Field.Validator>();

    protected CompositionLayout componentComposition;

    protected HorizontalLayout topPanel;

    protected ButtonsPanel buttonsPanel;

    protected RowsCount rowsCount;

    protected Map<Table.Column, Object> aggregationCells = null;

    private boolean usePresentations;

    protected Presentations presentations;
    protected TablePresentations tablePresentations;

    protected DsManager dsManager;

    private List<ColumnCollapseListener> columnCollapseListeners = new ArrayList<ColumnCollapseListener>();

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
        columns.put(column.getId(), column);
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
        component.addGeneratedColumn(id, (com.vaadin.ui.Table.ColumnGenerator) generator);
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

    public void sortBy(Object propertyId, boolean ascending) {
        if (isSortable()) {
            component.setSortAscending(ascending);
            component.setSortContainerPropertyId(propertyId);
            component.sort();
        }
    }

    public RowsCount getRowsCount() {
        return rowsCount;
    }

    public void setRowsCount(RowsCount rowsCount) {
        if (this.rowsCount != null && topPanel != null) {
            topPanel.removeComponent(WebComponentsHelper.unwrap(this.rowsCount));
        }
        this.rowsCount = rowsCount;
        if (rowsCount != null) {
            if (topPanel == null) {
                topPanel = new HorizontalLayout();
                topPanel.setWidth("100%");
                componentComposition.addComponentAsFirst(topPanel);
            }
            Component rc = WebComponentsHelper.unwrap(rowsCount);
            topPanel.addComponent(rc);
            topPanel.setComponentAlignment(rc, com.vaadin.ui.Alignment.BOTTOM_RIGHT);
        }
    }

    public boolean isAllowMultiStringCells() {
        return component.isAllowMultiStringCells();
    }

    public void setAllowMultiStringCells(boolean value) {
        component.setAllowMultiStringCells(value);
    }

    public boolean isAggregatable() {
        return component.isAggregatable();
    }

    public void setAggregatable(boolean aggregatable) {
        component.setAggregatable(aggregatable);
    }

    public void setShowTotalAggregation(boolean showAggregation) {
        component.setShowTotalAggregation(showAggregation);
    }

    public boolean isShowTotalAggregation() {
        return component.isShowTotalAggregation();
    }

    @Override
    public Component getComposition() {
        return componentComposition;
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
        component.setPageLength(15);

        component.addActionHandler(new ActionsAdapter());

        setPagingMode(Table.PagingMode.SCROLLING);

        component.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                if (datasource == null) return;

                final Set selected = getSelected();
                if (selected.isEmpty()) {
                    //noinspection unchecked
                    datasource.setItem(null);
                } else if (selected.size() > 1) {
                    datasource.setItem(null);
                    datasource.setItem((Entity) selected.iterator().next());
                } else {
                    //noinspection unchecked
                    datasource.setItem(null);
                    datasource.setItem((Entity) selected.iterator().next());
                }
            }
        });

        component.addShortcutListener(new ShortcutListener("tableEnter", com.vaadin.event.ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                if (enterPressAction != null) {
                    enterPressAction.actionPerform(WebAbstractTable.this);
                } else {
                    handleClickAction();
                }
            }
        });

        component.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick() && event.getItem() != null) {
                    handleClickAction();
                }
            }
        });

        component.addColumnCollapseListener(new com.haulmont.cuba.web.toolkit.ui.Table.CollapseListener() {
            @Override
            public void columnCollapsed(Object columnId, boolean collapsed) {
                final Column collapsedColumn = getColumn(columnId.toString());
                for (ColumnCollapseListener listener : columnCollapseListeners) {
                    listener.columnCollapsed(collapsedColumn, collapsed);
                }
            }
        });

        component.setSelectable(true);
        component.setTableFieldFactory(new FieldFactory());
        component.setColumnCollapsingAllowed(true);
        component.setColumnReorderingAllowed(true);

        setEditable(false);

        componentComposition = new CompositionLayout(component);
        componentComposition.setSpacing(true);
        componentComposition.setMargin(false);
        componentComposition.setExpandRatio(component, 1);

        component.setEnableCancelSorting(ConfigProvider.getConfig(WebConfig.class).getEnableCancelTableSorting());
    }

    protected void handleClickAction() {
        Action action = getItemClickAction();
        if (action == null) {
            action = getAction("edit");
            if (action == null) {
                action = getAction("view");
            }
        }
        if (action != null && action.isEnabled()) {
            Window window = ComponentsHelper.getWindow(WebAbstractTable.this);
            if (!(window instanceof Window.Lookup))
                action.actionPerform(WebAbstractTable.this);
        }
    }

    protected Collection<MetaPropertyPath> createColumns(com.vaadin.data.Container ds) {
        @SuppressWarnings({"unchecked"})
        final Collection<MetaPropertyPath> properties = (Collection<MetaPropertyPath>) ds.getContainerPropertyIds();

        Window window = ComponentsHelper.getWindow(this);
        boolean isLookup = window instanceof Window.Lookup;

        for (MetaPropertyPath propertyPath : properties) {
            final Table.Column column = columns.get(propertyPath);
            if (column != null && !(editable && BooleanUtils.isTrue(column.isEditable()))) {
                final String clickAction =
                        column.getXmlDescriptor() == null ?
                                null : column.getXmlDescriptor().attributeValue("clickAction");

                if (propertyPath.getRange().isClass()) {
                    if (!isLookup && !StringUtils.isEmpty(clickAction)) {
                        addGeneratedColumn(propertyPath, new ReadOnlyAssociationGenerator(column));
                    }
                } else if (propertyPath.getRange().isDatatype()) {
                    if (!isLookup && !StringUtils.isEmpty(clickAction)) {
                        addGeneratedColumn(propertyPath, new CodePropertyGenerator(column));
                    } else if (editable && BooleanUtils.isTrue(column.isCalculatable())) {
                        addGeneratedColumn(propertyPath, new CalculatableColumnGenerator());
                    } else {
                        final Datatype datatype = propertyPath.getRange().asDatatype();
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

    protected void refreshColumns(com.vaadin.data.Container ds) {
        @SuppressWarnings({"unchecked"})
        final Collection<MetaPropertyPath> propertyIds = (Collection<MetaPropertyPath>) ds.getContainerPropertyIds();
        for (final MetaPropertyPath id : propertyIds) {
            removeGeneratedColumn(id);
        }

        if (isEditable()) {
            final List<MetaPropertyPath> editableColumns = new ArrayList<MetaPropertyPath>(propertyIds.size());
            for (final MetaPropertyPath propertyId : propertyIds) {
                final Table.Column column = getColumn(propertyId.toString());
                if (BooleanUtils.isTrue(column.isEditable())) {
                    editableColumns.add(propertyId);
                }
            }
            if (!editableColumns.isEmpty()) {
                setEditableColumns(editableColumns);
            }
        } else {
            setEditableColumns(Collections.<MetaPropertyPath>emptyList());
        }

        createColumns(ds);
    }

    public void setDatasource(CollectionDatasource datasource) {
        UserSession userSession = UserSessionProvider.getUserSession();
        if (!userSession.isEntityOpPermitted(datasource.getMetaClass(), EntityOp.READ)) {
            component.setVisible(false);
            return;
        }

        final Collection<Object> columns;
        if (this.columns.isEmpty()) {
            Collection<MetaPropertyPath> paths = MetadataHelper.getViewPropertyPaths(datasource.getView(), datasource.getMetaClass());
            for (MetaPropertyPath metaPropertyPath : paths) {
                MetaProperty property = metaPropertyPath.getMetaProperty();
                if (!property.getRange().getCardinality().isMany() && !MetadataHelper.isSystem(property)) {
                    Table.Column column = new Table.Column(metaPropertyPath);

                    column.setCaption(MessageUtils.getPropertyCaption(property));
                    column.setType(metaPropertyPath.getRangeJavaClass());

                    Element element = DocumentHelper.createElement("column");
                    column.setXmlDescriptor(element);

                    addColumn(column);
                }
            }
        }
        columns = this.columns.keySet();

        this.datasource = datasource;
        this.dsManager = new DsManager(datasource, this);

        final CollectionDsWrapper containerDatasource = createContainerDatasource(datasource, getPropertyColumns(), dsManager);

        component.setContainerDataSource(containerDatasource);

        if (columns == null) {
            throw new NullPointerException("Columns cannot be null");
        }

        List<MetaPropertyPath> editableColumns = null;
        if (isEditable()) {
            editableColumns = new LinkedList<MetaPropertyPath>();
        }

        for (final Object columnId : columns) {
            final Table.Column column = this.columns.get(columnId);

            final String caption;
            if (column != null) {
                caption = StringUtils.capitalize(column.getCaption() != null ? column.getCaption() : getColumnCaption(columnId));
            } else {
                caption = StringUtils.capitalize(getColumnCaption(columnId));
            }

            setColumnHeader(columnId, caption);

            if (column != null) {
                if (editableColumns != null && column.isEditable() && (columnId instanceof MetaPropertyPath)) {
                    MetaProperty colMetaProperty = ((MetaPropertyPath) columnId).getMetaProperty();
                    MetaClass colMetaClass = colMetaProperty.getDomain();
                    if (userSession.isEntityAttrPermitted(colMetaClass, colMetaProperty.getName(), EntityAttrAccess.MODIFY)) {
                        editableColumns.add((MetaPropertyPath) column.getId());
                    }
                }

                if (column.isCollapsed() && component.isColumnCollapsingAllowed()) {
//                    try {
                    component.setColumnCollapsed(column.getId(), true);
                    /*} catch (IllegalAccessException e) {
                        // do nothing
                    }*/
                }

                if (column.getAggregation() != null && isAggregatable()) {
                    component.addContainerPropertyAggregation(column.getId(),
                            WebComponentsHelper.convertAggregationType(column.getAggregation().getType()));
                }
            }
        }

        if (editableColumns != null && !editableColumns.isEmpty()) {
            setEditableColumns(editableColumns);
        }

        createColumns(containerDatasource);

        List<Object> columnsOrder = new ArrayList<Object>();
        for (Table.Column column : this.columnsOrder) {
            if (column.getId() instanceof MetaPropertyPath) {
                MetaProperty colMetaProperty = ((MetaPropertyPath) column.getId()).getMetaProperty();
                MetaClass colMetaClass = colMetaProperty.getDomain();
                if (userSession.isEntityOpPermitted(colMetaClass, EntityOp.READ)
                        && userSession.isEntityAttrPermitted(
                        colMetaClass, colMetaProperty.getName(), EntityAttrAccess.VIEW)) {
                    columnsOrder.add(column.getId());
                }
            } else {
                columnsOrder.add(column.getId());
            }
            if (editable && column.getAggregation() != null
                    && (BooleanUtils.isTrue(column.isEditable()) || BooleanUtils.isTrue(column.isCalculatable()))) 
            {
                addAggregationCell(column);
            }
        }

        if (aggregationCells != null) {
            dsManager.addListener(createAggregationDatasourceListener());
        }

        setVisibleColumns(getPropertyColumns());

        if (UserSessionProvider.getUserSession().isSpecificPermitted(ShowInfoAction.ACTION_PERMISSION)) {
            ShowInfoAction action = (ShowInfoAction) getAction(ShowInfoAction.ACTION_ID);
            if (action == null) {
                action = new ShowInfoAction();
                addAction(action);
            }
            action.setDatasource(datasource);
        }

        if (rowsCount != null)
            rowsCount.setDatasource(datasource);

        datasource.addListener(new CollectionDsActionsNotifier(this));
    }

    private String getColumnCaption(Object columnId) {
        if (columnId instanceof MetaPropertyPath)
            return ((MetaPropertyPath) columnId).getMetaProperty().getName();
        else
            return columnId.toString();
    }

    private List<MetaPropertyPath> getPropertyColumns() {
        List<MetaPropertyPath> result = new ArrayList<MetaPropertyPath>();
        for (Column column : columnsOrder) {
            if (column.getId() instanceof MetaPropertyPath) {
                result.add((MetaPropertyPath) column.getId());
            }
        }
        return result;
    }

    protected abstract CollectionDsWrapper createContainerDatasource(CollectionDatasource datasource, Collection<MetaPropertyPath> columns, DsManager dsManager);

    protected void setVisibleColumns(List<?> columnsOrder) {
        component.setVisibleColumns(columnsOrder.toArray());
    }

    protected void setColumnHeader(Object columnId, String caption) {
        component.setColumnHeader(columnId, caption);
    }

    public void setRowHeaderMode(com.haulmont.cuba.gui.components.Table.RowHeaderMode rowHeaderMode) {
        switch (rowHeaderMode) {
            case NONE: {
                component.setRowHeaderMode(com.vaadin.ui.Table.ROW_HEADER_MODE_HIDDEN);
                break;
            }
            case ICON: {
                component.setRowHeaderMode(com.vaadin.ui.Table.ROW_HEADER_MODE_ICON_ONLY);
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
    }

    public void validate() throws ValidationException {
        for (com.haulmont.cuba.gui.components.Field.Validator tableValidator : tableValidators) {
            tableValidator.validate(getSelected());
        }
    }

    public void setStyleProvider(final Table.StyleProvider styleProvider) {
        this.styleProvider = styleProvider;
        if (styleProvider == null) {
            component.setCellStyleGenerator(null);
            return;
        }

        component.setCellStyleGenerator(new com.vaadin.ui.Table.CellStyleGenerator() {
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
//                            try {
                        if (component.isColumnCollapsingAllowed()) { // throws exception if not
                            component.setColumnCollapsed(column, !Boolean.valueOf(visible));
                        }
                            /*} catch (IllegalAccessException e) {
                                // ignore
                            }*/
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
//                try {
                if (component.isColumnCollapsingAllowed()) { // throws exception if not
                    component.setColumnCollapsed(newColumns.get(0), false);
                }
                /*} catch (IllegalAccessException e) {
                    //
                }*/
            }

            component.setVisibleColumns(newColumns.toArray());

            if (isSortable()) {
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
                } else {
                    component.setSortContainerPropertyId(null);
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

    @Override
    public void setEnterPressAction(Action action) {
        enterPressAction = action;
    }

    @Override
    public Action getEnterPressAction(){
        return enterPressAction;
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
        component.setPagingMode(WebComponentsHelper.convertPagingMode(pagingMode));
        if (pagingMode == Table.PagingMode.PAGE) {
            setPagingProvider(new DefaultPagingProvider());
        }
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        component.setNullSelectionAllowed(multiselect);
        super.setMultiSelect(multiselect);
    }

    public ButtonsPanel getButtonsPanel() {
        return buttonsPanel;
    }

    public void setButtonsPanel(ButtonsPanel panel) {
        if (buttonsPanel != null && topPanel != null) {
            topPanel.removeComponent(WebComponentsHelper.unwrap(buttonsPanel));
        }
        buttonsPanel = panel;
        if (panel != null) {
            if (topPanel == null) {
                topPanel = new HorizontalLayout();
                topPanel.setWidth("100%");
                componentComposition.addComponentAsFirst(topPanel);
            }
            topPanel.addComponent(WebComponentsHelper.unwrap(panel));
        }
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

            public String pageLengthSelectorCaption() {
                return pagingProvider.pageLengthSelectorCaption();
            }

            public boolean showPageLengthSelector() {
                return pagingProvider.showPageLengthSelector();
            }

            public int[] pageLengths() {
                return pagingProvider.pageLengths();
            }
        });
    }

    public void addGeneratedColumn(String columnId, final ColumnGenerator generator) {
        if (columnId == null)
            throw new IllegalArgumentException("columnId is null");
        if (generator == null)
            throw new IllegalArgumentException("generator is null");

        MetaPropertyPath targetCol = getDatasource().getMetaClass().getPropertyPath(columnId);
        Object generatedColumnId = targetCol != null ? targetCol : columnId;
        component.addGeneratedColumn(
                generatedColumnId,
                new com.vaadin.ui.Table.ColumnGenerator() {
                    public Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
                        com.haulmont.cuba.gui.components.Component component = generator.generateCell(WebAbstractTable.this, itemId);
                        if (component == null)
                            return null;
                        else {
                            Component vComponent = WebComponentsHelper.unwrap(component);
                            return vComponent;
                        }
                    }
                }
        );
    }

    public void removeGeneratedColumn(String columnId) {
        MetaPropertyPath targetCol = getDatasource().getMetaClass().getPropertyPath(columnId);
        removeGeneratedColumn(targetCol);
    }

    protected Map<Object, Object> __aggregate(AggregationContainer container, AggregationContainer.Context context) {
        final List<AggregationInfo> aggregationInfos =
                new LinkedList<AggregationInfo>();
        for (final Object o : container.getAggregationPropertyIds()) {
            final MetaPropertyPath propertyId = (MetaPropertyPath) o;
            final Table.Column column = columns.get(propertyId);
            if (column.getAggregation() != null) {
                aggregationInfos.add(column.getAggregation());
            }
        }
        Map<Object, Object> results = ((CollectionDatasource.Aggregatable) datasource).aggregate(
                aggregationInfos.toArray(new AggregationInfo[aggregationInfos.size()]),
                context.getItemIds()
        );
        if (aggregationCells != null) {
            results = __handleAggregationResults(context, results);
        }
        return results;
    }

    protected Map<Object, Object> __handleAggregationResults(AggregationContainer.Context context, Map<Object, Object> results) {
        for (final Map.Entry<Object, Object> entry : results.entrySet()) {
            final Table.Column column = columns.get(entry.getKey());
            com.vaadin.ui.Label cell;
            if ((cell = (com.vaadin.ui.Label) aggregationCells.get(column)) != null) {
                WebComponentsHelper.setLabelText(cell, entry.getValue(), column.getFormatter());
                entry.setValue(cell);
            }
        }
        return results;
    }

    protected class TablePropertyWrapper extends PropertyWrapper {

        private ValueChangeListener calcListener;
        private static final long serialVersionUID = -7942046867909695346L;

        public TablePropertyWrapper(Object item, MetaPropertyPath propertyPath, DsManager dsManager) {
            super(item, propertyPath, dsManager);
        }

        @Override
        public void addListener(ValueChangeListener listener) {
            super.addListener(listener);
            //A listener of a calculatable property must be only one
            if (listener instanceof CalculatablePropertyValueChangeListener) {
                if (this.calcListener != null) {
                    removeListener(calcListener);
                }
                calcListener = listener;
            }
        }

        @Override
        public void removeListener(ValueChangeListener listener) {
            super.removeListener(listener);
            if (calcListener == listener) {
                calcListener = null;
            }
        }

        @Override
        public boolean isReadOnly() {
            final Table.Column column = WebAbstractTable.this.columns.get(propertyPath);
            if (column != null) {
                return !editable || !(BooleanUtils.isTrue(column.isEditable()) || BooleanUtils.isTrue(column.isCalculatable()));
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
            final Table.Column column = WebAbstractTable.this.columns.get(propertyPath);
            if (column != null) {
                if (column.getFormatter() != null) {
                    return column.getFormatter().format(getValue());
                } else if (column.getXmlDescriptor() != null) {
                    String captionProperty = column.getXmlDescriptor().attributeValue("captionProperty");
                    if (!StringUtils.isEmpty(captionProperty)) {
                        final Object value = getValue();
                        return this.propertyPath.getRange().isDatatype() ?
                                this.propertyPath.getRange().asDatatype().format(value) :
                                value != null
                                        ? String.valueOf(((Instance) value).getValue(captionProperty))
                                        : null;
                    }
                }
            }
            return super.toString();
        }
    }

    protected abstract class LinkGenerator implements com.vaadin.ui.Table.ColumnGenerator, TableSupport.ColumnGenerator {
        protected Table.Column column;

        public LinkGenerator(Table.Column column) {
            this.column = column;
        }

        public com.vaadin.ui.Component generateCell(AbstractSelect source, final Object itemId, Object columnId) {
            final Item item = source.getItem(itemId);
            final Property property = item.getItemProperty(columnId);
            final Object value = property.getValue();

            final com.vaadin.ui.Button component = new Button();
            component.setData(value);
            component.setCaption(value == null ? "" : property.toString());
            component.setStyleName("link");

            component.addListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    final Element element = column.getXmlDescriptor();

                    final String clickAction = element.attributeValue("clickAction");
                    if (!StringUtils.isEmpty(clickAction)) {

                        if (clickAction.startsWith("open:")) {
                            final com.haulmont.cuba.gui.components.IFrame frame = WebAbstractTable.this.getFrame();
                            String screenName = clickAction.substring("open:".length()).trim();
                            final Window window = frame.openEditor(screenName, getItem(item, property), WindowManager.OpenType.THIS_TAB);

                            window.addListener(new Window.CloseListener() {
                                public void windowClosed(String actionId) {
                                    if (Window.COMMIT_ACTION_ID.equals(actionId) && window instanceof Window.Editor) {
                                        Object item = ((Window.Editor) window).getItem();
                                        if (item instanceof Entity) {
                                            datasource.updateItem((Entity) item);
                                        }
                                    }
                                }
                            });
                        } else if (clickAction.startsWith("invoke:")) {
                            final com.haulmont.cuba.gui.components.IFrame frame = WebAbstractTable.this.getFrame();
                            String methodName = clickAction.substring("invoke:".length()).trim();
                            try {
                                IFrame controllerFrame = WebComponentsHelper.getControllerFrame(frame);
                                Method method = controllerFrame.getClass().getMethod(methodName, Object.class);
                                method.invoke(controllerFrame, getItem(item, property));
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

        public Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
            return generateCell(((AbstractSelect) source), itemId, columnId);
        }

        public Component generateCell(TableSupport source, Object itemId, Object columnId) {
            return generateCell(((AbstractSelect) source), itemId, columnId);
        }

        protected abstract Entity getItem(Item item, Property property);
    }

    protected class ReadOnlyAssociationGenerator extends LinkGenerator {
        public ReadOnlyAssociationGenerator(Table.Column column) {
            super(column);
        }

        protected Entity getItem(Item item, Property property) {
            return (Entity) property.getValue();
        }
    }

    protected class CodePropertyGenerator extends LinkGenerator {
        public CodePropertyGenerator(Table.Column column) {
            super(column);
        }

        protected Entity getItem(Item item, Property property) {
            return ((ItemWrapper) item).getItem();
        }
    }

    private class ReadOnlyDatatypeGenerator implements com.vaadin.ui.Table.ColumnGenerator, TableSupport.ColumnGenerator {
        protected Component generateCell(com.vaadin.ui.AbstractSelect source, Object itemId, Object columnId) {
            Property property = source.getItem(itemId).getItemProperty(columnId);
            final Object value = property.getValue();

            final Label label = new Label(value == null ? null : property.toString());
            label.setImmediate(true);

            return label;
        }

        public Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
            return generateCell(((AbstractSelect) source), itemId, columnId);
        }

        public Component generateCell(TableSupport source, Object itemId, Object columnId) {
            return generateCell(((AbstractSelect) source), itemId, columnId);
        }
    }

    private class ReadOnlyBooleanDatatypeGenerator
            implements com.vaadin.ui.Table.ColumnGenerator,
            TableSupport.ColumnGenerator {
        public Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
            return generateCell((AbstractSelect) source, itemId, columnId);
        }

        public Component generateCell(TableSupport source, Object itemId, Object columnId) {
            return generateCell((AbstractSelect) source, itemId, columnId);
        }

        protected Component generateCell(AbstractSelect source, Object itemId, Object columnId) {
            final Property property = source.getItem(itemId).getItemProperty(columnId);
            final Object value = property.getValue();

            com.vaadin.ui.Embedded checkBoxImage;
            if (BooleanUtils.isTrue((Boolean) value)){
                checkBoxImage = new com.vaadin.ui.Embedded("", new ThemeResource("table/img/checkbox-checked.png"));
            }
            else {
                checkBoxImage = new com.vaadin.ui.Embedded("", new ThemeResource("table/img/checkbox-unchecked.png"));
            }
            return checkBoxImage;
        }
    }

    private class CalculatableColumnGenerator implements com.vaadin.ui.Table.ColumnGenerator, TableSupport.ColumnGenerator {
        public Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
            return generateCell((AbstractSelect) source, itemId, columnId);
        }

        public Component generateCell(TableSupport source, Object itemId, Object columnId) {
            return generateCell((AbstractSelect) source, itemId, columnId);
        }

        protected Component generateCell(AbstractSelect source, Object itemId, Object columnId) {
            CollectionDatasource ds = WebAbstractTable.this.getDatasource();
            MetaPropertyPath propertyPath = ds.getMetaClass().getPropertyEx(columnId.toString());

            PropertyWrapper propertyWrapper = (PropertyWrapper) source.getContainerProperty(itemId, propertyPath);

            Formatter formatter = null;
            Table.Column column = WebAbstractTable.this.getColumn(columnId.toString());
            if (column != null) {
                formatter = column.getFormatter();
            }

            final Label label = new Label();
            WebComponentsHelper.setLabelText(label, propertyWrapper.getValue(), formatter);
            label.setWidth("-1px");

            //add property change listener that will update a label value
            propertyWrapper.addListener(new CalculatablePropertyValueChangeListener(label, formatter));

            return label;
        }
    }

    private static class CalculatablePropertyValueChangeListener implements Property.ValueChangeListener {
        private Label component;
        private Formatter formatter;

        private static final long serialVersionUID = 8041384664735759397L;

        private CalculatablePropertyValueChangeListener(Label component, Formatter formatter) {
            this.component = component;
            this.formatter = formatter;
        }

        public void valueChange(Property.ValueChangeEvent event) {
            WebComponentsHelper.setLabelText(component, event.getProperty().getValue(), formatter);
        }
    }

    protected void addAggregationCell(Table.Column column) {
        if (aggregationCells == null) {
            aggregationCells = new HashMap<Table.Column, Object>();
        }
        aggregationCells.put(column, createAggregationCell());
    }

    protected com.vaadin.ui.Label createAggregationCell() {
        com.vaadin.ui.Label label = new Label();
        label.setWidth("-1px");
        label.setParent(component);
        return label;
    }

    protected CollectionDatasourceListener createAggregationDatasourceListener() {
        return new AggregationDatasourceListener();
    }

    protected class AggregationDatasourceListener implements CollectionDatasourceListener<Entity> {
        public void collectionChanged(CollectionDatasource ds, Operation operation) {
        }

        public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
        }

        public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
        }

        public void valueChanged(Entity source, String property, Object prevValue, Object value) {
            final CollectionDatasource ds = WebAbstractTable.this.getDatasource();
            component.aggregate(new AggregationContainer.Context(ds.getItemIds()));
        }
    }

    protected class FieldFactory extends AbstractFieldFactory {
        @Override
        protected Datasource getDatasource() {
            return datasource;
        }

        @Override
        protected CollectionDatasource getOptionsDatasource(MetaClass metaClass, MetaPropertyPath propertyPath) {
            return WebAbstractTable.this.getOptionsDatasource(metaClass, columns.get(propertyPath));
        }

        @Override
        protected Collection<Field.Validator> getValidators(MetaPropertyPath propertyPath) {
            return validatorsMap.get(columns.get(propertyPath));
        }

        @Override
        protected boolean required(MetaPropertyPath propertyPath) {
            return requiredColumns.containsKey(columns.get(propertyPath));
        }

        @Override
        protected String requiredMessage(MetaPropertyPath propertyPath) {
            return requiredColumns.get(columns.get(propertyPath));
        }

        @Override
        protected Formatter getFormatter(MetaPropertyPath propertyPath) {
            Table.Column column = columns.get(propertyPath);
            return column.getFormatter();
        }

        @Override
        protected String getFormat(MetaPropertyPath propertyPath) {
            Table.Column column = columns.get(propertyPath);
            Element formatterElement = column.getXmlDescriptor().element("formatter");
            return formatterElement.attributeValue("format");
        }

        @Override
        protected String fieldType(MetaPropertyPath propertyPath) {
            return null; //todo gorodnov: implement this method
        }

        @Override
        protected Element getXmlDescriptor(MetaPropertyPath propertyPath) {
            Table.Column column = columns.get(propertyPath);
            return column.getXmlDescriptor();
        }

        @Override
        protected void initCommon(com.vaadin.ui.Field field, Field cubaField, MetaPropertyPath propertyPath) {
            super.initCommon(field, cubaField, propertyPath);

            final Table.Column column = columns.get(propertyPath);
            final MetaProperty metaProperty;
            if (column.getId() != null) {
                metaProperty = ((MetaPropertyPath) column.getId()).getMetaProperty();
            } else {
                metaProperty = null;
            }

            if (field instanceof com.vaadin.ui.TextField) {
                initTextField((com.vaadin.ui.TextField) field, metaProperty, column.getXmlDescriptor());
            }

            if (cubaField instanceof WebDateField) {
                initDateField(field, metaProperty, column.getXmlDescriptor());
            }
            
            if (field instanceof CheckBox) {
                ((CheckBox) field).setLayoutCaption(true);
            }
        }
    }

    class DefaultPagingProvider implements Table.PagingProvider {
        public String firstCaption() {
            return null;
        }

        public String prevCaption() {
            return MessageProvider.getMessage(App.class, "paging.prevCaption");
        }

        public String nextCaption() {
            return MessageProvider.getMessage(App.class, "paging.nextCaption");
        }

        public String lastCaption() {
            return null;
        }

        public String pageLengthSelectorCaption() {
            return null;
        }

        public boolean showPageLengthSelector() {
            return false;
        }

        public int[] pageLengths() {
            return new int[0];
        }
    }

    protected boolean handleSpecificVariables(Map<String, Object> variables) {
        boolean needReload = false;

        if (isUsePresentations()) {

            final Presentations p = getPresentations();

            if (p.getCurrent() != null && p.isAutoSave(p.getCurrent()) && needUpdatePresentation(variables)) {
                Element e = p.getSettings(p.getCurrent());
                saveSettings(e);
                p.setSettings(p.getCurrent(), e);
            }
        }

        return needReload;
    }

    private boolean needUpdatePresentation(Map<String, Object> variables) {
        return variables.containsKey("colwidth") || variables.containsKey("sortcolumn")
                || variables.containsKey("sortascending") || variables.containsKey("columnorder")
                || variables.containsKey("collapsedcolumns") || variables.containsKey("groupedcolumns");
    }

    protected void paintSpecificContent(PaintTarget target) throws PaintException {
        target.addVariable(component, "presentations", isUsePresentations());
        if (isUsePresentations()) {
            target.startTag("presentations");
            tablePresentations.paint(target);
            target.endTag("presentations");
        }
    }

    public List<Table.Column> getNotCollapsedColumns() {
        final List<Table.Column> visibleColumns = new ArrayList<Table.Column>(component.getVisibleColumns().length);
        Object[] keys = component.getVisibleColumns();
        for (final Object key : keys) {
            if (!component.isColumnCollapsed(key)) {
                visibleColumns.add(columns.get(key));
            }
        }
        return visibleColumns;
    }

    public void usePresentations(boolean use) {
        usePresentations = use;
    }

    public boolean isUsePresentations() {
        return usePresentations;
    }

    public void loadPresentations() {
        if (isUsePresentations()) {
            presentations = new PresentationsImpl(this);

            tablePresentations = new TablePresentations(this);
        } else {
            throw new UnsupportedOperationException("Component doesn't use presentations");
        }
    }

    public Presentations getPresentations() {
        if (isUsePresentations()) {
            return presentations;
        } else {
            throw new UnsupportedOperationException("Component doesn't use presentations");
        }
    }

    public void applyPresentation(Object id) {
        if (isUsePresentations()) {
            Presentation p = presentations.getPresentation(id);
            applyPresentation(p);
        } else {
            throw new UnsupportedOperationException("Component doesn't use presentations");
        }
    }

    public void applyPresentationAsDefault(Object id) {
        if (isUsePresentations()) {
            Presentation p = presentations.getPresentation(id);
            if (p != null) {
                presentations.setDefault(p);
                applyPresentation(p);
            }
        } else {
            throw new UnsupportedOperationException("Component doesn't use presentations");
        }
    }

    protected void applyPresentation(Presentation p) {
        presentations.setCurrent(p);
        Element settingsElement  = presentations.getSettings(p);
        applySettings(settingsElement);
        component.requestRepaint();
    }

    public Object getDefaultPresentationId() {
        Presentation def = presentations.getDefault();
        return def == null ? null : def.getId();
    }

    @Override
    public void addColumnCollapsedListener(ColumnCollapseListener columnCollapsedListener) {
        columnCollapseListeners.add(columnCollapsedListener);
    }

    @Override
    public void removeColumnCollapseListener(ColumnCollapseListener columnCollapseListener) {
        columnCollapseListeners.remove(columnCollapseListener);
    }
}
