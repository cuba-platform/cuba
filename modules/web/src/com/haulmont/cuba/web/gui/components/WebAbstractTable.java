/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.impl.BooleanDatatype;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.CollectionDsActionsNotifier;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.gui.presentations.PresentationsImpl;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Presentation;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.CompositionLayout;
import com.haulmont.cuba.web.gui.components.presentations.TablePresentations;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.data.AggregationContainer;
import com.haulmont.cuba.web.toolkit.ui.CubaEnhancedTable;
import com.haulmont.cuba.web.toolkit.ui.CubaFieldWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaPlaceHolder;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * @param <T>
 * @author abramov
 * @version $Id$
 */
public abstract class WebAbstractTable<T extends com.vaadin.ui.Table & CubaEnhancedTable>
        extends
            WebAbstractList<T>
        implements
            Table {

    protected Map<Object, Column> columns = new HashMap<>();
    protected List<Table.Column> columnsOrder = new ArrayList<>();

    protected boolean editable;
    protected Action itemClickAction;
    protected Action enterPressAction;

    protected Table.StyleProvider styleProvider;
    protected Table.IconProvider iconProvider;

    protected Map<Table.Column, String> requiredColumns = new HashMap<>();

    protected Map<Table.Column, Set<Field.Validator>> validatorsMap = new HashMap<>();

    protected Set<com.haulmont.cuba.gui.components.Field.Validator> tableValidators = new LinkedHashSet<>();

    protected Map<Entity, Datasource> fieldDatasources = new WeakHashMap<>();

    protected CompositionLayout componentComposition;

    protected HorizontalLayout topPanel;

    protected ButtonsPanel buttonsPanel;

    protected RowsCount rowsCount;

    protected Map<Table.Column, Object> aggregationCells = null;

    protected boolean usePresentations;

    protected Presentations presentations;

    protected List<ColumnCollapseListener> columnCollapseListeners = new ArrayList<>();

    // Map column id to Printable representation
    protected Map<String, Printable> printables = new HashMap<>();

    protected ShortcutsDelegate<ShortcutListener> shortcutsDelegate;

    // Use weak map and references for loyal GC support
    protected Map<Entity, List<WeakReference<ReadOnlyCheckBox>>> booleanCells = new WeakHashMap<>();

//  disabled for #PL-2035
    // Disable listener that points component value to follow the ds item.
//    protected boolean disableItemListener = false;

    protected static final int MAX_TEXT_LENGTH_GAP = 10;

    protected Security security = AppBeans.get(Security.class);

    protected WebAbstractTable() {
        shortcutsDelegate = new ShortcutsDelegate<ShortcutListener>() {
            @Override
            protected ShortcutListener attachShortcut(final String actionId, KeyCombination keyCombination) {
                ShortcutListener shortcut = new ShortcutListener(actionId, keyCombination.getKey().getCode(),
                        KeyCombination.Modifier.codes(keyCombination.getModifiers())) {

                    @Override
                    public void handleAction(Object sender, Object target) {
                        if (target == component) {
                            Action action = getAction(actionId);
                            if (action != null && action.isEnabled() && action.isVisible()) {
                                action.actionPerform(WebAbstractTable.this);
                            }
                        }
                    }
                };
                component.addShortcutListener(shortcut);
                return shortcut;
            }

            @Override
            protected void detachShortcut(Action action, ShortcutListener shortcutDescriptor) {
                component.removeShortcutListener(shortcutDescriptor);
            }

            @Override
            protected Collection<Action> getActions() {
                return WebAbstractTable.this.getActions();
            }
        };
        shortcutsDelegate.setAllowEnterShortcut(false);
    }

    @Override
    public java.util.List<Table.Column> getColumns() {
        return columnsOrder;
    }

    @Override
    public Table.Column getColumn(String id) {
        for (Table.Column column : columnsOrder) {
            if (column.getId().toString().equals(id))
                return column;
        }
        return null;
    }

    @Override
    public void addColumn(Table.Column column) {
        checkNotNullArgument(column, "Column must be non null");

        component.addContainerProperty(column.getId(), column.getType(), null);
        columns.put(column.getId(), column);
        columnsOrder.add(column);
        if (column.getWidth() != null) {
            component.setColumnWidth(column.getId(), column.getWidth());
        }
        column.setOwner(this);
    }

    @Override
    public void removeColumn(Table.Column column) {
        if (column == null) {
            return;
        }

        component.removeContainerProperty(column.getId());
        columns.remove(column.getId());
        columnsOrder.remove(column);

        // todo artamonov remove after fix #VAADIN-12980
        component.refreshRowCache();
        column.setOwner(null);
    }

    @Override
    public Datasource getItemDatasource(Entity item) {
        Datasource fieldDatasource = fieldDatasources.get(item);

        if (fieldDatasource == null) {
            fieldDatasource = new DsBuilder()
                    .setAllowCommit(false)
                    .setMetaClass(datasource.getMetaClass())
                    .setRefreshMode(CollectionDatasource.RefreshMode.NEVER)
                    .setViewName("_local")
                    .buildDatasource();

            ((DatasourceImplementation)fieldDatasource).valid();

            fieldDatasource.setItem(item);
            fieldDatasources.put(item, fieldDatasource);
        }

        return fieldDatasource;
    }

    protected void addGeneratedColumn(Object id, Object generator) {
        component.addGeneratedColumn(id, (com.vaadin.ui.Table.ColumnGenerator) generator);
    }

    protected void removeGeneratedColumn(Object id) {
        boolean wasEnabled = component.disableContentBufferRefreshing();

        com.vaadin.ui.Table.ColumnGenerator columnGenerator = component.getColumnGenerator(id);
        if (columnGenerator instanceof CustomColumnGenerator) {
            CustomColumnGenerator tableGenerator = (CustomColumnGenerator) columnGenerator;
            if (tableGenerator.getAssociatedRuntimeColumn() != null) {
                removeColumn(tableGenerator.getAssociatedRuntimeColumn());
            }
        }
        component.removeGeneratedColumn(id);

        component.enableContentBufferRefreshing(wasEnabled);
    }

    @Override
    public void addPrintable(String columnId, Printable printable) {
        printables.put(columnId, printable);
    }

    @Override
    public void removePrintable(String columnId) {
        printables.remove(columnId);
    }

    @Override
    @Nullable
    public Printable getPrintable(Table.Column column) {
        return getPrintable(String.valueOf(column.getId()));
    }

    @Nullable
    @Override
    public Printable getPrintable(String columnId) {
        Printable printable = printables.get(columnId);
        if (printable != null) {
            return printable;
        } else {
            com.vaadin.ui.Table.ColumnGenerator vColumnGenerator = component.getColumnGenerator(columnId);
            if (vColumnGenerator instanceof CustomColumnGenerator) {
                ColumnGenerator columnGenerator = ((CustomColumnGenerator) vColumnGenerator).getColumnGenerator();
                if (columnGenerator instanceof Printable)
                    return (Printable) columnGenerator;
            }
            return null;
        }
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        if (this.editable != editable) {
            this.editable = editable;

            component.disableContentBufferRefreshing();

            if (datasource != null) {
                com.vaadin.data.Container ds = component.getContainerDataSource();

                @SuppressWarnings("unchecked")
                final Collection<MetaPropertyPath> propertyIds = (Collection<MetaPropertyPath>) ds.getContainerPropertyIds();

                if (editable) {
                    final List<MetaPropertyPath> editableColumns = new ArrayList<>(propertyIds.size());
                    for (final MetaPropertyPath propertyId : propertyIds) {
                        final Table.Column column = getColumn(propertyId.toString());
                        if (BooleanUtils.isTrue(column.isEditable())) {
                            com.vaadin.ui.Table.ColumnGenerator generator = component.getColumnGenerator(column.getId());
                            if (generator != null) {
                                if (generator instanceof SystemTableColumnGenerator) {
                                    // remove default generator
                                    component.removeGeneratedColumn(propertyId);
                                } else {
                                    // do not edit generated columns
                                    continue;
                                }
                            }

                            editableColumns.add(propertyId);
                        }
                    }
                    setEditableColumns(editableColumns);
                } else {
                    setEditableColumns(Collections.<MetaPropertyPath>emptyList());

                    Window window = ComponentsHelper.getWindow(this);
                    boolean isLookup = window instanceof Window.Lookup;

                    // restore generators for some type of attributes
                    for (MetaPropertyPath propertyId : propertyIds) {
                        final Table.Column column = columns.get(propertyId);
                        if (column != null) {
                            final String clickAction = column.getXmlDescriptor() == null ?
                                    null : column.getXmlDescriptor().attributeValue("clickAction");

                            if (component.getColumnGenerator(column.getId()) == null) {
                                if (propertyId.getRange().isClass()) {
                                    if (!isLookup && StringUtils.isNotEmpty(clickAction)) {
                                        addGeneratedColumn(propertyId, new ReadOnlyAssociationGenerator(column));
                                    }
                                } else if (propertyId.getRange().isDatatype()) {
                                    if (!isLookup && !StringUtils.isEmpty(clickAction)) {
                                        addGeneratedColumn(propertyId, new CodePropertyGenerator(column));
                                    } else {
                                        final Datatype datatype = propertyId.getRange().asDatatype();
                                        if (BooleanDatatype.NAME.equals(datatype.getName()) && column.getFormatter() == null) {
                                            addGeneratedColumn(propertyId, new ReadOnlyBooleanDatatypeGenerator());
                                        } else if (column.getMaxTextLength() != null) {
                                            addGeneratedColumn(propertyId, new AbbreviatedColumnGenerator(column));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            component.setEditable(editable);

            component.enableContentBufferRefreshing(true);
        }
    }

    protected void setEditableColumns(List<MetaPropertyPath> editableColumns) {
        component.setEditableColumns(editableColumns.toArray());
    }

    @Override
    public boolean isSortable() {
        return component.isSortEnabled();
    }

    @Override
    public void setSortable(boolean sortable) {
        component.setSortEnabled(sortable);
    }

    @Override
    public void setColumnReorderingAllowed(boolean columnReorderingAllowed) {
        component.setColumnReorderingAllowed(columnReorderingAllowed);
    }

    @Override
    public boolean getColumnReorderingAllowed() {
        return component.isColumnReorderingAllowed();
    }

    @Override
    public void setColumnControlVisible(boolean columnCollapsingAllowed) {
        component.setColumnCollapsingAllowed(columnCollapsingAllowed);
    }

    @Override
    public boolean getColumnControlVisible() {
        return component.isColumnCollapsingAllowed();
    }

    @Override
    public void sortBy(Object propertyId, boolean ascending) {
        if (isSortable()) {
            component.setSortAscending(ascending);
            component.setSortContainerPropertyId(propertyId);
            component.sort();
        }
    }

    @Override
    public RowsCount getRowsCount() {
        return rowsCount;
    }

    @Override
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
            com.vaadin.ui.Component rc = WebComponentsHelper.unwrap(rowsCount);
            topPanel.addComponent(rc);
            topPanel.setExpandRatio(rc, 1);
            topPanel.setComponentAlignment(rc, com.vaadin.ui.Alignment.BOTTOM_RIGHT);
        }
    }

    @Override
    public boolean isAllowMultiStringCells() {
        return false;
//        vaadin 7
//        return component.isAllowMultiStringCells();
    }

    @Override
    public void setAllowMultiStringCells(boolean value) {
//        vaadin7
//        component.setAllowMultiStringCells(value);
    }

    @Override
    public boolean isAggregatable() {
        return false;
//        vaadin7
//        return component.isAggregatable();
    }

    @Override
    public void setAggregatable(boolean aggregatable) {
//        vaadin7
//        component.setAggregatable(aggregatable);
    }

    @Override
    public void setShowTotalAggregation(boolean showAggregation) {
//        vaadin7
//        component.setShowTotalAggregation(showAggregation);
    }

    @Override
    public boolean isShowTotalAggregation() {
        return false;
//        vaadin7
//        return component.isShowTotalAggregation();
    }

    @Override
    public com.vaadin.ui.Component getComposition() {
        return componentComposition;
    }

    @Override
    public boolean isAllowPopupMenu() {
        return component.isAllowPopupMenu();
    }

    @Override
    public void setAllowPopupMenu(boolean value) {
        component.setAllowPopupMenu(value);
    }

    protected void setTablePresentations(TablePresentations tablePresentations) {
        component.setPresentations(tablePresentations);
    }

    protected void initComponent(T component) {
        component.setMultiSelect(false);
        component.setNullSelectionAllowed(false);
        component.setImmediate(true);
        component.setValidationVisible(false);
//        vaadin7
//        component.setStoreColWidth(true);
        component.setPageLength(15);
        // CAUTION: vaadin considers null as row header property id;
        component.setColumnWidth(null, 16);

        component.addActionHandler(new ActionsAdapter());

        component.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void valueChange(Property.ValueChangeEvent event) {
                if (datasource == null) return;

                final Set<Entity> selected = getSelected();
//                disabled for #PL-2035
//                disableItemListener = true;
                if (selected.isEmpty()) {
                    datasource.setItem(null);
                } else {
                    // reset selection and select new item
                    if (isMultiSelect())
                        datasource.setItem(null);
                    datasource.setItem(selected.iterator().next());
                }
//                disabled for #PL-2035
//                disableItemListener = false;
            }
        });

        component.addShortcutListener(new ShortcutListener("tableEnter", com.vaadin.event.ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                if (target == WebAbstractTable.this.component) {
                    if (enterPressAction != null) {
                        enterPressAction.actionPerform(WebAbstractTable.this);
                    } else {
                        handleClickAction();
                    }
                }
            }
        });

        component.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick() && event.getItem() != null) {
                    handleClickAction();
                }
            }
        });

        // vaadin7
//        component.addColumnCollapseListener(new com.haulmont.cuba.web.toolkit.ui.Table.CollapseListener() {
//            @Override
//            public void columnCollapsed(Object columnId, boolean collapsed) {
//                final Column collapsedColumn = getColumn(columnId.toString());
//                for (ColumnCollapseListener listener : columnCollapseListeners) {
//                    listener.columnCollapsed(collapsedColumn, collapsed);
//                }
//            }
//        });

        component.setSelectable(true);
        component.setTableFieldFactory(new WebTableFieldFactory());
        component.setColumnCollapsingAllowed(true);
        component.setColumnReorderingAllowed(true);

        setEditable(false);

        componentComposition = new CompositionLayout(component);
        componentComposition.setSpacing(true);
        componentComposition.setMargin(false);
        componentComposition.setWidth("-1px");

        // todo artamonov adjust component size relative to composition size

        component.setSizeFull();
        componentComposition.setExpandRatio(component, 1);
    }

    @Override
    public void addAction(Action action) {
        checkNotNullArgument(action, "action must be non null");

        Action oldAction = getAction(action.getId());

        super.addAction(action);

        shortcutsDelegate.addAction(oldAction, action);
    }

    @Override
    public void removeAction(Action action) {
        super.removeAction(action);

        shortcutsDelegate.removeAction(action);
    }

    protected void handleClickAction() {
        Action action = getItemClickAction();
        if (action == null) {
            action = getEnterAction();
            if (action == null) {
                action = getAction("edit");
                if (action == null) {
                    action = getAction("view");
                }
            }
        }
        if (action != null && action.isEnabled()) {
            Window window = ComponentsHelper.getWindow(WebAbstractTable.this);
            if (window instanceof Window.Wrapper)
                window = ((Window.Wrapper) window).getWrappedWindow();

            if (!(window instanceof Window.Lookup)) {
                action.actionPerform(WebAbstractTable.this);
            } else {
                Window.Lookup lookup = (Window.Lookup) window;

                com.haulmont.cuba.gui.components.Component lookupComponent = lookup.getLookupComponent();
                if (lookupComponent != this)
                    action.actionPerform(WebAbstractTable.this);
                else if (action.getId().equals(WindowDelegate.LOOKUP_ITEM_CLICK_ACTION_ID)) {
                    action.actionPerform(WebAbstractTable.this);
                }
            }
        }
    }

    protected Action getEnterAction() {
        for (Action action : getActions()) {
            KeyCombination kc = action.getShortcut();
            if (kc != null) {
                if ((kc.getModifiers() == null || kc.getModifiers().length == 0)
                        && kc.getKey() == KeyCombination.Key.ENTER) {
                    return action;
                }
            }
        }
        return null;
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
                    if (!isLookup && StringUtils.isNotEmpty(clickAction)) {
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
                        } else if (column.getMaxTextLength() != null) {
                            addGeneratedColumn(propertyPath, new AbbreviatedColumnGenerator(column));
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

    protected void updateReadonlyBooleanCell(Entity source, String property, Object value) {
        List<WeakReference<ReadOnlyCheckBox>> booleanProperties = booleanCells.get(source);
        if (booleanProperties != null) {
            for (WeakReference<ReadOnlyCheckBox> booleanBox : new LinkedList<>(booleanProperties)) {
                ReadOnlyCheckBox checkbox = booleanBox.get();
                if (checkbox != null) {
                    if (ObjectUtils.equals(property, String.valueOf(checkbox.getColumnId()))) {
                        checkbox.updateValue((Boolean) value);
                    }
                } else {
                    booleanProperties.remove(booleanBox);
                }
            }
        }
    }

    @Override
    public void setDatasource(CollectionDatasource datasource) {
        UserSession userSession = AppBeans.get(UserSessionSource.class).getUserSession();
        MessageTools messageTools = AppBeans.get(MessageTools.class);
        MetadataTools metadataTools = AppBeans.get(MetadataTools.class);

        final Collection<Object> columns;
        if (this.columns.isEmpty()) {
            Collection<MetaPropertyPath> paths = metadataTools.getViewPropertyPaths(datasource.getView(), datasource.getMetaClass());
            for (MetaPropertyPath metaPropertyPath : paths) {
                MetaProperty property = metaPropertyPath.getMetaProperty();
                if (!property.getRange().getCardinality().isMany() && !metadataTools.isSystem(property)) {
                    Table.Column column = new Table.Column(metaPropertyPath);

                    column.setCaption(messageTools.getPropertyCaption(property));
                    column.setType(metaPropertyPath.getRangeJavaClass());

                    Element element = DocumentHelper.createElement("column");
                    column.setXmlDescriptor(element);

                    addColumn(column);
                }
            }
        }
        columns = this.columns.keySet();

        this.datasource = datasource;

        // drop cached datasources for components before update table cells on client
        datasource.addListener(new CollectionDsListenerAdapter<Entity>() {
            @Override
            public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
                switch (operation) {
                    case CLEAR:
                    case REFRESH:
                        fieldDatasources.clear();
                        booleanCells.clear();
                        break;

                    case UPDATE:
                    case REMOVE:
                        for (Entity entity : items) {
                            fieldDatasources.remove(entity);
                            booleanCells.remove(entity);
                        }
                        break;
                }
            }

            @Override
            public void valueChanged(Entity source, String property, @Nullable Object prevValue, @Nullable Object value) {
                if (!booleanCells.isEmpty() && value instanceof Boolean) {
                    updateReadonlyBooleanCell(source, property, value);
                }
            }
        });

        final CollectionDsWrapper containerDatasource = createContainerDatasource(datasource, getPropertyColumns());

        component.setContainerDataSource(containerDatasource);

        if (columns == null) {
            throw new NullPointerException("Columns cannot be null");
        }

        List<MetaPropertyPath> editableColumns = null;
        if (isEditable()) {
            editableColumns = new LinkedList<>();
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
                    component.setColumnCollapsed(column.getId(), true);
                }

//                vaadin7
//                if (column.getAggregation() != null && isAggregatable()) {
//                    component.addContainerPropertyAggregation(column.getId(),
//                            WebComponentsHelper.convertAggregationType(column.getAggregation().getType()));
//                }
            }
        }

        if (editableColumns != null && !editableColumns.isEmpty()) {
            setEditableColumns(editableColumns);
        }

        createColumns(containerDatasource);

        for (Table.Column column : this.columnsOrder) {
            if (editable && column.getAggregation() != null
                    && (BooleanUtils.isTrue(column.isEditable()) || BooleanUtils.isTrue(column.isCalculatable()))) {
                addAggregationCell(column);
            }
        }

//        vaadin7
//        if (aggregationCells != null) {
//            getDatasource().addListener(createAggregationDatasourceListener());
//        }

        setVisibleColumns(getPropertyColumns());

        if (AppBeans.get(UserSessionSource.class).getUserSession().isSpecificPermitted(ShowInfoAction.ACTION_PERMISSION)) {
            ShowInfoAction action = (ShowInfoAction) getAction(ShowInfoAction.ACTION_ID);
            if (action == null) {
                action = new ShowInfoAction();
                addAction(action);
            }
            action.setDatasource(datasource);
        }

        if (rowsCount != null)
            rowsCount.setDatasource(datasource);

        datasource.addListener(new CollectionDsActionsNotifier(this) {
            @Override
            public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
                // #PL-2035, reload selection from ds
                Set<Object> selectedItemIds = getSelectedItemIds();
                if (selectedItemIds == null) {
                    selectedItemIds = Collections.emptySet();
                }

                Set<Object> newSelection = new HashSet<>();
                for (Object entityId : selectedItemIds) {
                    if (ds.containsItem(entityId)) {
                        newSelection.add(entityId);
                    }
                }

                if (ds.getState() == Datasource.State.VALID && ds.getItem() != null) {
                    newSelection.add(ds.getItem().getId());
                }

                if (newSelection.isEmpty()) {
                    setSelected((Entity) null);
                } else {
                    setSelectedIds(newSelection);
                }
            }
        });

//        disabled for #PL-2035
        // noinspection unchecked
//        datasource.addListener(new CollectionDsActionsNotifier(this) {
//            @Override
//            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
//                super.itemChanged(ds, prevItem, item);
//                if (!disableItemListener && !getSelected().contains(item)) {
//                    setSelected(item);
//                }
//            }
//        });

        for (Action action : getActions()) {
            action.refreshState();
        }

        assignAutoDebugId();
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);

        if (id != null) {
            componentComposition.setId(AppUI.getCurrent().getTestIdManager().getTestId(id + "_composition"));
        }
    }

    @Override
    public void setId(String id) {
        super.setId(id);

        if (id != null && AppUI.getCurrent().isTestMode()) {
            componentComposition.setCubaId(id + "_composition");
        }
    }

    @Override
    public void assignAutoDebugId() {
        super.assignAutoDebugId();

        if (buttonsPanel != null) {
            for (com.haulmont.cuba.gui.components.Component subComponent : buttonsPanel.getComponents()) {
                if (subComponent instanceof WebAbstractComponent) {
                    ((WebAbstractComponent) subComponent).assignAutoDebugId();
                }
            }
        }
    }

    @Override
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }
        if (datasource != null && StringUtils.isNotEmpty(datasource.getId())) {
            return "table_" + datasource.getId();
        }

        return getClass().getSimpleName();
    }

    protected String getColumnCaption(Object columnId) {
        if (columnId instanceof MetaPropertyPath)
            return ((MetaPropertyPath) columnId).getMetaProperty().getName();
        else
            return columnId.toString();
    }

    protected List<MetaPropertyPath> getPropertyColumns() {
        UserSession userSession = AppBeans.get(UserSessionSource.class).getUserSession();
        List<MetaPropertyPath> result = new ArrayList<>();
        for (Column column : columnsOrder) {
            if (column.getId() instanceof MetaPropertyPath) {
                MetaProperty colMetaProperty = ((MetaPropertyPath) column.getId()).getMetaProperty();
                MetaClass colMetaClass = colMetaProperty.getDomain();
                if (userSession.isEntityOpPermitted(colMetaClass, EntityOp.READ)
                        && userSession.isEntityAttrPermitted(
                        colMetaClass, colMetaProperty.getName(), EntityAttrAccess.VIEW)) {
                    result.add((MetaPropertyPath) column.getId());
                }
            }
        }
        return result;
    }

    protected abstract CollectionDsWrapper createContainerDatasource(CollectionDatasource datasource,
                                                                     Collection<MetaPropertyPath> columns);

    protected void setVisibleColumns(List<?> columnsOrder) {
        component.setVisibleColumns(columnsOrder.toArray());
    }

    protected void setColumnHeader(Object columnId, String caption) {
        component.setColumnHeader(columnId, caption);
    }

    @Override
    public void setRowHeaderMode(com.haulmont.cuba.gui.components.Table.RowHeaderMode rowHeaderMode) {
        switch (rowHeaderMode) {
            case NONE: {
                component.setRowHeaderMode(com.vaadin.ui.Table.RowHeaderMode.HIDDEN);
                break;
            }
            case ICON: {
                component.setRowHeaderMode(com.vaadin.ui.Table.RowHeaderMode.ICON_ONLY);
                break;
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }

    @Override
    public void setRequired(Table.Column column, boolean required, String message) {
        if (required)
            requiredColumns.put(column, message);
        else
            requiredColumns.remove(column);
    }

    @Override
    public void addValidator(Table.Column column, final com.haulmont.cuba.gui.components.Field.Validator validator) {
        Set<com.haulmont.cuba.gui.components.Field.Validator> validators = validatorsMap.get(column);
        if (validators == null) {
            validators = new HashSet<>();
            validatorsMap.put(column, validators);
        }
        validators.add(validator);
    }

    @Override
    public void addValidator(final com.haulmont.cuba.gui.components.Field.Validator validator) {
        tableValidators.add(validator);
    }

    public void validate() throws ValidationException {
        for (com.haulmont.cuba.gui.components.Field.Validator tableValidator : tableValidators) {
            tableValidator.validate(getSelected());
        }
    }

    @Override
    public void setStyleProvider(final Table.StyleProvider styleProvider) {
        this.styleProvider = styleProvider;
        if (styleProvider == null) {
            component.setCellStyleGenerator(null);
            return;
        }

        component.setCellStyleGenerator(new com.vaadin.ui.Table.CellStyleGenerator() {
            @Override
            public String getStyle(com.vaadin.ui.Table source, Object itemId, Object propertyId) {
                @SuppressWarnings({"unchecked"})
                final Entity item = datasource.getItem(itemId);
                return styleProvider.getStyleName(item, propertyId == null ? null : propertyId.toString());
            }
        });
    }

    @Override
    public void setIconProvider(IconProvider iconProvider) {
        this.iconProvider = iconProvider;
        if (iconProvider != null) {
            setRowHeaderMode(RowHeaderMode.ICON);
        } else {
            setRowHeaderMode(RowHeaderMode.NONE);
        }
        component.refreshRowCache();
    }

    // For vaadin component extensions.
    protected Resource getItemIcon(Object itemId) {
        if (iconProvider == null) {
            return null;
        }
        // noinspection unchecked
        Entity item = datasource.getItem(itemId);
        if (item == null) {
            return null;
        }
        // noinspection unchecked
        String resourceUrl = iconProvider.getItemIcon(item);
        if (StringUtils.isBlank(resourceUrl)) {
            return null;
        }
        // noinspection ConstantConditions
        if (!resourceUrl.contains(":")) {
            resourceUrl = "theme:" + resourceUrl;
        }
        return WebComponentsHelper.getResource(resourceUrl);
    }

    @Override
    public int getRowHeaderWidth() {
        // CAUTION: vaadin considers null as row header property id;
        return component.getColumnWidth(null);
    }

    @Override
    public void setRowHeaderWidth(int width) {
        // CAUTION: vaadin considers null as row header property id;
        component.setColumnWidth(null, width);
    }

    @Override
    public void applySettings(Element element) {
        final Element columnsElem = element.element("columns");
        if (columnsElem != null) {
            Collection<String> modelIds = new LinkedList<>();
            for (Object column : component.getVisibleColumns()) {
                modelIds.add(String.valueOf(column));
            }

            Collection<String> loadedIds = new LinkedList<>();
            for (Element colElem : Dom4j.elements(columnsElem, "columns")) {
                loadedIds.add(colElem.attributeValue("id"));
            }

            Configuration configuration = AppBeans.get(Configuration.NAME);
            ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);

            if (clientConfig.getLoadObsoleteSettingsForTable()
                    || CollectionUtils.isEqualCollection(modelIds, loadedIds)) {
                applyColumnSettings(element);
            }
        }
    }

    protected void applyColumnSettings(Element element) {
        final Element columnsElem = element.element("columns");

        Object[] oldColumns = component.getVisibleColumns();
        List<Object> newColumns = new ArrayList<>();

        // add columns from saved settings
        for (Element colElem : Dom4j.elements(columnsElem, "columns")) {
            for (Object column : oldColumns) {
                if (column.toString().equals(colElem.attributeValue("id"))) {
                    newColumns.add(column);

                    String width = colElem.attributeValue("width");
                    if (width != null) {
                        component.setColumnWidth(column, Integer.valueOf(width));
                    }

                    String visible = colElem.attributeValue("visible");
                    if (visible != null) {
                        if (component.isColumnCollapsingAllowed()) { // throws exception if not
                            component.setColumnCollapsed(column, !Boolean.valueOf(visible));
                        }
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
            if (component.isColumnCollapsingAllowed()) { // throws exception if not
                component.setColumnCollapsed(newColumns.get(0), false);
            }
        }

        component.setVisibleColumns(newColumns.toArray());

        if (isSortable()) {
            //apply sorting
            String sortProp = columnsElem.attributeValue("sortProperty");
            if (!StringUtils.isEmpty(sortProp)) {
                MetaPropertyPath sortProperty = datasource.getMetaClass().getPropertyPath(sortProp);
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

    @Override
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
    public Action getEnterPressAction() {
        return enterPressAction;
    }

    @Override
    public void setItemClickAction(Action action) {
        if (itemClickAction != null) {
            removeAction(itemClickAction);
        }
        itemClickAction = action;
        if (!getActions().contains(action)) {
            addAction(action);
        }
    }

    @Override
    public Action getItemClickAction() {
        return itemClickAction;
    }

    public String getCaption() {
        return component.getCaption();
    }

    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        component.setNullSelectionAllowed(multiselect);
        super.setMultiSelect(multiselect);
    }

    @Override
    public ButtonsPanel getButtonsPanel() {
        return buttonsPanel;
    }

    @Override
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

    @Override
    public void addGeneratedColumn(String columnId, ColumnGenerator generator) {
        checkNotNullArgument(columnId, "columnId is null");
        checkNotNullArgument(generator, "generator is null for column id '%s'", columnId);

        MetaPropertyPath targetCol = getDatasource().getMetaClass().getPropertyPath(columnId);
        Object generatedColumnId = targetCol != null ? targetCol : columnId;

        Column column = getColumn(columnId);
        Column associatedRuntimeColumn = null;
        if (column == null) {
            Column newColumn = new Column(columnId);

            columns.put(newColumn.getId(), newColumn);
            columnsOrder.add(newColumn);

            associatedRuntimeColumn = newColumn;
            newColumn.setOwner(this);
        }

        // replace generator for column if exist
        if (component.getColumnGenerator(generatedColumnId) != null) {
            component.removeGeneratedColumn(generatedColumnId);
        }

        component.addGeneratedColumn(
                generatedColumnId,
                new CustomColumnGenerator(generator, associatedRuntimeColumn) {
                    @Override
                    public com.vaadin.ui.Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
                        Entity entity = getDatasource().getItem(itemId);
                        com.haulmont.cuba.gui.components.Component component = getColumnGenerator().generateCell(entity);
                        if (component == null)
                            return null;
                        else {
                            com.vaadin.ui.Component vComponent = WebComponentsHelper.unwrap(component);
                            // wrap field for show required asterisk
                            if ((vComponent instanceof com.vaadin.ui.Field)
                                    && (((com.vaadin.ui.Field) vComponent).isRequired())) {
                                VerticalLayout layout = new VerticalLayout();
                                layout.addComponent(vComponent);
                                vComponent = layout;
                            }
                            return vComponent;
                        }
                    }
                }
        );
    }

    @Override
    public void addGeneratedColumn(String columnId, ColumnGenerator generator,
                                   Class<? extends com.haulmont.cuba.gui.components.Component> componentClass) {
        // web ui doesn't make any improvements with componentClass known
        addGeneratedColumn(columnId, generator);
    }

    @Override
    public void removeGeneratedColumn(String columnId) {
        MetaPropertyPath targetCol = getDatasource().getMetaClass().getPropertyPath(columnId);
        removeGeneratedColumn(targetCol == null ? columnId : targetCol);
    }

    @Override
    public void setColumnCaption(String columnId, String caption) {
        Column column = getColumn(columnId);
        if (column == null) {
            throw new IllegalStateException(String.format("Column with id '%s' not found", columnId));
        }

        setColumnCaption(column, caption);
    }

    @Override
    public void setColumnCaption(Column column, String caption) {
        checkNotNullArgument(column, "column must be non null");

        if (!StringUtils.equals(column.getCaption(), caption)) {
            column.setCaption(caption);
        }
        component.setColumnHeader(column.getId(), caption);
    }

    @Override
    public void setColumnCollapsed(String columnId, boolean collapsed) {
        Column column = getColumn(columnId);
        if (column == null) {
            throw new IllegalStateException(String.format("Column with id '%s' not found", columnId));
        }

        setColumnCollapsed(column, collapsed);
    }

    @Override
    public void setColumnCollapsed(Column column, boolean collapsed) {
        if (!getColumnControlVisible()) {
            return;
        }

        checkNotNullArgument(column, "column must be non null");

        if (column.isCollapsed() != collapsed) {
            column.setCollapsed(collapsed);
        }
        component.setColumnCollapsed(column.getId(), collapsed);
    }

    @Override
    public void setColumnWidth(Column column, int width) {
        checkNotNullArgument(column, "column must be non null");

        if (column.getWidth() == null || column.getWidth() != width) {
            column.setWidth(width);
        }
        component.setColumnWidth(column.getId(), width);
    }

    @Override
    public void setColumnWidth(String columnId, int width) {
        Column column = getColumn(columnId);
        if (column == null) {
            throw new IllegalStateException(String.format("Column with id '%s' not found", columnId));
        }

        setColumnWidth(column, width);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void repaint() {
        component.refreshRowCache();
        component.markAsDirtyRecursive();
    }

    @Override
    public void selectAll() {
        if (isMultiSelect()) {
            component.setValue(component.getItemIds());
        }
    }

    protected Map<Object, Object> __aggregate(AggregationContainer container, AggregationContainer.Context context) {
        final List<AggregationInfo> aggregationInfos = new LinkedList<>();
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

        public TablePropertyWrapper(Object item, MetaPropertyPath propertyPath) {
            super(item, propertyPath);
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
        public String getFormattedValue() {
            final Table.Column column = WebAbstractTable.this.columns.get(propertyPath);
            if (column != null) {
                if (column.getFormatter() != null) {
                    return column.getFormatter().format(getValue());
                } else if (column.getXmlDescriptor() != null) {
                    String captionProperty = column.getXmlDescriptor().attributeValue("captionProperty");
                    if (!StringUtils.isEmpty(captionProperty) && propertyPath.getRange().isClass()) {
                        final Object value = getValue();
                        return value != null ? String.valueOf(((Instance) value).getValue(captionProperty)) : null;
                    }
                }
            }
            return super.getFormattedValue();
        }
    }

    protected interface SystemTableColumnGenerator extends com.vaadin.ui.Table.ColumnGenerator {
    }

    protected static abstract class CustomColumnGenerator implements com.vaadin.ui.Table.ColumnGenerator {

        protected ColumnGenerator columnGenerator;

        // Used for properly removing column from table
        protected Column associatedRuntimeColumn;

        protected CustomColumnGenerator(ColumnGenerator columnGenerator) {
            this.columnGenerator = columnGenerator;
        }

        protected CustomColumnGenerator(ColumnGenerator columnGenerator, @Nullable Column associatedRuntimeColumn) {
            this.columnGenerator = columnGenerator;
            this.associatedRuntimeColumn = associatedRuntimeColumn;
        }

        public Column getAssociatedRuntimeColumn() {
            return associatedRuntimeColumn;
        }

        public ColumnGenerator getColumnGenerator() {
            return columnGenerator;
        }
    }

    protected abstract class LinkGenerator implements SystemTableColumnGenerator {
        protected Table.Column column;

        public LinkGenerator(Table.Column column) {
            this.column = column;
        }

        public com.vaadin.ui.Component generateCell(AbstractSelect source, final Object itemId, Object columnId) {
            final Item item = source.getItem(itemId);
            final Property property = item.getItemProperty(columnId);
            final Object value = property.getValue();

            final com.vaadin.ui.Button component = new com.vaadin.ui.Button();
            component.setData(value);
            component.setCaption(value == null ? "" : ((PropertyWrapper)property).getFormattedValue());
            component.setStyleName(BaseTheme.BUTTON_LINK);

            component.addClickListener(new com.vaadin.ui.Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    final Element element = column.getXmlDescriptor();
                    if (element == null)
                        return;

                    // todo move it to TableLoader
                    final String clickAction = element.attributeValue("clickAction");
                    if (!StringUtils.isEmpty(clickAction)) {

                        if (clickAction.startsWith("open:")) {
                            final com.haulmont.cuba.gui.components.IFrame frame = WebAbstractTable.this.getFrame();
                            String screenName = clickAction.substring("open:".length()).trim();
                            final Window window = frame.openEditor(screenName, getItem(item, property), WindowManager.OpenType.THIS_TAB);

                            window.addListener(new Window.CloseListener() {
                                @Override
                                public void windowClosed(String actionId) {
                                    if (Window.COMMIT_ACTION_ID.equals(actionId) && window instanceof Window.Editor) {
                                        Entity item = ((Window.Editor) window).getItem();
                                        if (item != null) {
                                            datasource.updateItem(item);
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
                            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
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

        @Override
        public com.vaadin.ui.Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
            return generateCell(((AbstractSelect) source), itemId, columnId);
        }

        protected abstract Entity getItem(Item item, Property property);
    }

    protected class ReadOnlyAssociationGenerator extends LinkGenerator {
        public ReadOnlyAssociationGenerator(Table.Column column) {
            super(column);
        }

        @Override
        protected Entity getItem(Item item, Property property) {
            return (Entity) property.getValue();
        }
    }

    protected class CodePropertyGenerator extends LinkGenerator {
        public CodePropertyGenerator(Table.Column column) {
            super(column);
        }

        @Override
        protected Entity getItem(Item item, Property property) {
            return ((ItemWrapper) item).getItem();
        }
    }

    protected class ReadOnlyBooleanDatatypeGenerator implements SystemTableColumnGenerator {
        @Override
        public com.vaadin.ui.Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
            return generateCell((AbstractSelect) source, itemId, columnId);
        }

        protected com.vaadin.ui.Component generateCell(AbstractSelect source, Object itemId, final Object columnId) {
            Item item = source.getItem(itemId);
            final Property property = item.getItemProperty(columnId);
            final Object value = property.getValue();

            ReadOnlyCheckBox checkPoxImage = new ReadOnlyCheckBox(columnId);
            checkPoxImage.setSizeUndefined();
            checkPoxImage.updateValue((Boolean) value);

            Entity key = ((ItemWrapper) item).getItem();

            List<WeakReference<ReadOnlyCheckBox>> booleanProperties = booleanCells.get(key);
            if (booleanProperties == null) {
                booleanProperties = new LinkedList<>();
                booleanCells.put(key, booleanProperties);
            } else {
                for (WeakReference<ReadOnlyCheckBox> checkBoxRef : new LinkedList<>(booleanProperties)) {
                    ReadOnlyCheckBox readOnlyCheckBox = checkBoxRef.get();
                    if (readOnlyCheckBox != null) {
                        // remove old
                        if (ObjectUtils.equals(columnId, readOnlyCheckBox.getColumnId())) {
                            booleanProperties.remove(checkBoxRef);
                        }
                    } else {
                        booleanProperties.remove(checkBoxRef);
                    }
                }
            }
            booleanProperties.add(new WeakReference<>(checkPoxImage));

            return checkPoxImage;
        }
    }

    protected static class ReadOnlyCheckBox extends CubaPlaceHolder {

        public final Object columnId;

        public ReadOnlyCheckBox(Object columnId) {
            this.columnId = columnId;
        }

        public void updateValue(Boolean value) {
            if (BooleanUtils.isTrue(value)) {
                setStyleName("checkbox-checked");
            } else {
                setStyleName("checkbox-unchecked");
            }
        }

        public Object getColumnId() {
            return columnId;
        }
    }

    protected class AbbreviatedColumnGenerator implements SystemTableColumnGenerator {

        protected Table.Column column;

        public AbbreviatedColumnGenerator(Table.Column column) {
            this.column = column;
        }

        @Override
        public com.vaadin.ui.Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
            final Property property = source.getItem(itemId).getItemProperty(columnId);
            final Object value = property.getValue();

            if (value == null) {
                return null;
            }
            com.vaadin.ui.Component cell;

            String stringValue = value.toString();
            int maxTextLength = column.getMaxTextLength();
            if (stringValue.length() > maxTextLength + MAX_TEXT_LENGTH_GAP)  {
                TextArea content = new TextArea(null, stringValue);
                content.setWidth("100%");
                content.setHeight("100%");
                content.setReadOnly(true);
                CssLayout cssLayout = new CssLayout();
                cssLayout.addComponent(content);
                cell = new PopupView(StringEscapeUtils.escapeHtml(StringUtils.abbreviate(stringValue, maxTextLength)),
                        cssLayout);
                cell.addStyleName("abbreviated");
            } else {
                cell = new Label(stringValue);
            }
            return cell;
        }
    }

    protected class CalculatableColumnGenerator implements SystemTableColumnGenerator {
        @Override
        public com.vaadin.ui.Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
            return generateCell((AbstractSelect) source, itemId, columnId);
        }

        protected com.vaadin.ui.Component generateCell(AbstractSelect source, Object itemId, Object columnId) {
            CollectionDatasource ds = WebAbstractTable.this.getDatasource();
            MetaPropertyPath propertyPath = ds.getMetaClass().getPropertyPath(columnId.toString());

            PropertyWrapper propertyWrapper = (PropertyWrapper) source.getContainerProperty(itemId, propertyPath);

            com.haulmont.cuba.gui.components.Formatter formatter = null;
            Table.Column column = WebAbstractTable.this.getColumn(columnId.toString());
            if (column != null) {
                formatter = column.getFormatter();
            }

            final com.vaadin.ui.Label label = new com.vaadin.ui.Label();
            WebComponentsHelper.setLabelText(label, propertyWrapper.getValue(), formatter);
            label.setWidth("-1px");

            //add property change listener that will update a label value
            propertyWrapper.addListener(new CalculatablePropertyValueChangeListener(label, formatter));

            return label;
        }
    }

    protected static class CalculatablePropertyValueChangeListener implements Property.ValueChangeListener {
        private Label component;
        private com.haulmont.cuba.gui.components.Formatter formatter;

        private static final long serialVersionUID = 8041384664735759397L;

        private CalculatablePropertyValueChangeListener(Label component, com.haulmont.cuba.gui.components.Formatter formatter) {
            this.component = component;
            this.formatter = formatter;
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            WebComponentsHelper.setLabelText(component, event.getProperty().getValue(), formatter);
        }
    }

    protected void addAggregationCell(Table.Column column) {
        if (aggregationCells == null) {
            aggregationCells = new HashMap<>();
        }
        aggregationCells.put(column, createAggregationCell());
    }

    protected com.vaadin.ui.Label createAggregationCell() {
        com.vaadin.ui.Label label = new com.vaadin.ui.Label();
        label.setWidth("-1px");
        label.setParent(component);
        return label;
    }

    protected CollectionDatasourceListener createAggregationDatasourceListener() {
        return new AggregationDatasourceListener();
    }

    protected class AggregationDatasourceListener extends CollectionDsListenerAdapter<Entity> {

        @Override
        public void valueChanged(Entity source, String property, Object prevValue, Object value) {
//            vaadin7
//            final CollectionDatasource ds = WebAbstractTable.this.getDatasource();
//            component.aggregate(new AggregationContainer.Context(ds.getItemIds()));
        }
    }

    protected class WebTableFieldFactory extends AbstractFieldFactory implements TableFieldFactory {

        @Override
        public com.vaadin.ui.Field<?> createField(com.vaadin.data.Container container,
                                                  Object itemId, Object propertyId, Component uiContext) {

            String fieldPropertyId = String.valueOf(propertyId);

            Column columnConf = columns.get(propertyId);

            Item item = container.getItem(itemId);
            Entity entity = ((ItemWrapper)item).getItem();
            Datasource fieldDatasource = getItemDatasource(entity);

            com.haulmont.cuba.gui.components.Component columnComponent =
                    createField(fieldDatasource, fieldPropertyId, columnConf.getXmlDescriptor());

            com.vaadin.ui.Field fieldImpl = getFieldImplementation(columnComponent);

            if (columnComponent instanceof Field) {
                Field cubaField = (Field) columnComponent;

                if (columnConf.getDescription() != null) {
                    cubaField.setDescription(columnConf.getDescription());
                }
                if (requiredColumns.containsKey(columnConf)) {
                    cubaField.setRequired(true);
                    cubaField.setRequiredMessage(requiredColumns.get(columnConf));
                }
            }

            if (columnConf.getWidth() != null) {
                columnComponent.setWidth(columnConf.getWidth() + "px");
            } else {
                columnComponent.setWidth("100%");
            }

            if (columnComponent instanceof BelongToFrame) {
                BelongToFrame belongToFrame = (BelongToFrame) columnComponent;
                if (belongToFrame.getFrame() == null) {
                    belongToFrame.setFrame(getFrame());
                }
            }

            applyPermissions(columnComponent);

            return fieldImpl;
        }

        protected com.vaadin.ui.Field getFieldImplementation(com.haulmont.cuba.gui.components.Component columnComponent) {
            com.vaadin.ui.Component composition = WebComponentsHelper.getComposition(columnComponent);
            com.vaadin.ui.Field fieldImpl;
            if (composition instanceof com.vaadin.ui.Field) {
                fieldImpl = (com.vaadin.ui.Field) composition;
            } else {
                fieldImpl = new CubaFieldWrapper(columnComponent);
            }
            return fieldImpl;
        }

        protected void applyPermissions(com.haulmont.cuba.gui.components.Component columnComponent) {
            if (columnComponent instanceof DatasourceComponent) {
                DatasourceComponent dsComponent = (DatasourceComponent) columnComponent;
                MetaProperty metaProperty = dsComponent.getMetaProperty();

                if (metaProperty != null) {
                    MetaClass metaClass = dsComponent.getDatasource().getMetaClass();
                    dsComponent.setEditable(security.isEntityAttrModificationPermitted(metaClass, metaProperty.getName())
                            && dsComponent.isEditable());
                }
            }
        }

        @Override
        @Nullable
        protected CollectionDatasource getOptionsDatasource(Datasource fieldDatasource, String propertyId) {
            if (datasource == null)
                throw new IllegalStateException("Table datasource is null");

            Column columnConf = columns.get(datasource.getMetaClass().getPropertyPath(propertyId));

            final DsContext dsContext = datasource.getDsContext();

            String optDsName = columnConf.getXmlDescriptor() != null ?
                    columnConf.getXmlDescriptor().attributeValue("optionsDatasource") : "";

            if (StringUtils.isBlank(optDsName)) {
                return null;
            } else {
                CollectionDatasource ds = dsContext.get(optDsName);
                if (ds == null)
                    throw new IllegalStateException("Options datasource for table column '" + propertyId + "' not found: " + optDsName);

                return ds;
            }
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

    protected boolean needUpdatePresentation(Map<String, Object> variables) {
        return variables.containsKey("colwidth") || variables.containsKey("sortcolumn")
                || variables.containsKey("sortascending") || variables.containsKey("columnorder")
                || variables.containsKey("collapsedcolumns") || variables.containsKey("groupedcolumns");
    }

    @Override
    public List<Table.Column> getNotCollapsedColumns() {
        if (component.getVisibleColumns() == null)
            return Collections.emptyList();

        final List<Table.Column> visibleColumns = new ArrayList<>(component.getVisibleColumns().length);
        Object[] keys = component.getVisibleColumns();
        for (final Object key : keys) {
            if (!component.isColumnCollapsed(key)) {
                Column column = columns.get(key);
                if (column != null)
                    visibleColumns.add(column);
            }
        }
        return visibleColumns;
    }

    @Override
    public void usePresentations(boolean use) {
        usePresentations = use;
    }

    @Override
    public boolean isUsePresentations() {
        return usePresentations;
    }

    @Override
    public void loadPresentations() {
        if (isUsePresentations()) {
            presentations = new PresentationsImpl(this);

            setTablePresentations(new TablePresentations(this));
        } else {
            throw new UnsupportedOperationException("Component doesn't use presentations");
        }
    }

    @Override
    public Presentations getPresentations() {
        if (isUsePresentations()) {
            return presentations;
        } else {
            throw new UnsupportedOperationException("Component doesn't use presentations");
        }
    }

    @Override
    public void applyPresentation(Object id) {
        if (isUsePresentations()) {
            Presentation p = presentations.getPresentation(id);
            applyPresentation(p);
        } else {
            throw new UnsupportedOperationException("Component doesn't use presentations");
        }
    }

    @Override
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
        Element settingsElement = presentations.getSettings(p);
        applySettings(settingsElement);
        component.markAsDirty();
    }

    @Override
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