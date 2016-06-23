/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.aggregation.Aggregations;
import com.haulmont.cuba.gui.data.impl.CollectionDsActionsNotifier;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.gui.presentations.PresentationsImpl;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.security.entity.Presentation;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.components.presentations.TablePresentations;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.data.AggregationContainer;
import com.haulmont.cuba.web.toolkit.ui.CubaEnhancedTable;
import com.haulmont.cuba.web.toolkit.ui.CubaResizableTextAreaWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaTextArea;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public abstract class WebAbstractTable<T extends com.vaadin.ui.Table & CubaEnhancedTable, E extends Entity>
        extends WebAbstractList<T, E>
        implements Table<E> {

    protected Map<Object, Column> columns = new HashMap<>();
    protected List<Table.Column> columnsOrder = new ArrayList<>();

    protected boolean editable;
    protected Action itemClickAction;
    protected Action enterPressAction;

    protected List<Table.StyleProvider> styleProviders; // lazily initialized List
    protected Table.IconProvider iconProvider;

    protected Map<Table.Column, String> requiredColumns; // lazily initialized Map

    protected Map<Table.Column, Set<Field.Validator>> validatorsMap; // lazily initialized Map

    protected Set<com.haulmont.cuba.gui.components.Field.Validator> tableValidators; // lazily initialized LinkedHashSet

    protected Map<Entity, Datasource> fieldDatasources; // lazily initialized WeakHashMap;

    protected VerticalLayout componentComposition;

    protected HorizontalLayout topPanel;

    protected ButtonsPanel buttonsPanel;

    protected RowsCount rowsCount;

    protected Map<Table.Column, String> aggregationCells = null;

    protected boolean usePresentations;

    protected Presentations presentations;
    protected Document defaultSettings;

    protected List<ColumnCollapseListener> columnCollapseListeners; // lazily initialized List

    // Map column id to Printable representation
    protected Map<String, Printable> printables; // lazily initialized Map

    protected static final int MAX_TEXT_LENGTH_GAP = 10;

    protected Security security = AppBeans.get(Security.NAME);
    protected boolean settingsEnabled = true;

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
        if (StringUtils.isNotBlank(column.getDescription())) {
            component.setColumnDescription(column.getId(), column.getDescription());
        }

        if (!column.isSortable()) {
            component.setColumnSortable(column.getId(), column.isSortable());
        }

        columns.put(column.getId(), column);
        columnsOrder.add(column);
        if (column.getWidth() != null) {
            component.setColumnWidth(column.getId(), column.getWidth());
        }
        if (column.getAlignment() != null) {
            component.setColumnAlignment(column.getId(),
                    WebComponentsHelper.convertColumnAlignment(column.getAlignment()));
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

        if (!(component.getContainerDataSource() instanceof com.vaadin.data.Container.ItemSetChangeNotifier)) {
            component.refreshRowCache();
        }
        column.setOwner(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Datasource getItemDatasource(Entity item) {
        if (fieldDatasources == null) {
            fieldDatasources = new WeakHashMap<>();
        }

        Datasource fieldDatasource = fieldDatasources.get(item);

        if (fieldDatasource == null) {
            fieldDatasource = new DsBuilder()
                    .setAllowCommit(false)
                    .setMetaClass(datasource.getMetaClass())
                    .setRefreshMode(CollectionDatasource.RefreshMode.NEVER)
                    .setViewName("_local")
                    .buildDatasource();

            ((DatasourceImplementation) fieldDatasource).valid();

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
        if (printables == null) {
            printables = new HashMap<>();
        }

        printables.put(columnId, printable);
    }

    @Override
    public void removePrintable(String columnId) {
        if (printables != null) {
            printables.remove(columnId);
        }
    }

    @Override
    @Nullable
    public Printable getPrintable(Table.Column column) {
        return getPrintable(String.valueOf(column.getId()));
    }

    @Nullable
    @Override
    public Printable getPrintable(String columnId) {
        Printable printable = printables != null ? printables.get(columnId) : null;
        if (printable != null) {
            return printable;
        } else {
            com.vaadin.ui.Table.ColumnGenerator vColumnGenerator = component.getColumnGenerator(getColumn(columnId).getId());
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
                    MetaClass metaClass = datasource.getMetaClass();

                    final List<MetaPropertyPath> editableColumns = new ArrayList<>(propertyIds.size());
                    for (final MetaPropertyPath propertyId : propertyIds) {
                        if (!security.isEntityAttrUpdatePermitted(metaClass, propertyId.toString())) {
                            continue;
                        }

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
                    setEditableColumns(Collections.emptyList());

                    Window window = ComponentsHelper.getWindowImplementation(this);
                    boolean isLookup = window instanceof Window.Lookup;

                    // restore generators for some type of attributes
                    for (MetaPropertyPath propertyId : propertyIds) {
                        final Table.Column column = columns.get(propertyId);
                        if (column != null) {
                            final String isLink = column.getXmlDescriptor() == null ?
                                    null : column.getXmlDescriptor().attributeValue("link");

                            if (component.getColumnGenerator(column.getId()) == null) {
                                if (propertyId.getRange().isClass()) {
                                    if (!isLookup && StringUtils.isNotEmpty(isLink)) {
                                        setClickListener(propertyId.toString(), new LinkCellClickListener());
                                    }
                                } else if (propertyId.getRange().isDatatype()) {
                                    if (!isLookup && !StringUtils.isEmpty(isLink)) {
                                        setClickListener(propertyId.toString(), new LinkCellClickListener());
                                    } else {
                                        if (column.getMaxTextLength() != null) {
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
        component.setSortEnabled(sortable && canBeSorted(datasource));
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
    public void setMultiLineCells(boolean multiLineCells) {
        component.setMultiLineCells(multiLineCells);
    }

    @Override
    public boolean isMultiLineCells() {
        return component.isMultiLineCells();
    }

    @Override
    public boolean isAggregatable() {
        return component.isAggregatable();
    }

    @Override
    public void setAggregatable(boolean aggregatable) {
        component.setAggregatable(aggregatable);
    }

    @Override
    public Map<Object, Object> getAggregationResults() {
        CollectionDatasource ds = WebAbstractTable.this.getDatasource();
        return component.aggregate(new AggregationContainer.Context(ds.getItemIds()));
    }

    @Override
    public void setAggregationStyle(AggregationStyle aggregationStyle) {
        component.setAggregationStyle(aggregationStyle);
    }

    @Override
    public AggregationStyle getAggregationStyle() {
        return component.getAggregationStyle();
    }

    @Override
    public void setShowTotalAggregation(boolean showAggregation) {
        component.setShowTotalAggregation(showAggregation);
    }

    @Override
    public boolean isShowTotalAggregation() {
        return component.isShowTotalAggregation();
    }

    @Override
    public com.vaadin.ui.Component getComposition() {
        return componentComposition;
    }

    @Override
    public boolean isContextMenuEnabled() {
        return component.isContextMenuEnabled();
    }

    @Override
    public void setContextMenuEnabled(boolean contextMenuEnabled) {
        component.setContextMenuEnabled(contextMenuEnabled);
    }

    protected void setTablePresentations(TablePresentations tablePresentations) {
        component.setPresentations(tablePresentations);
    }

    protected void initComponent(final T component) {
        component.setMultiSelect(false);
        component.setImmediate(true);
        component.setValidationVisible(false);
        component.setShowBufferedSourceException(false);

        Messages messages = AppBeans.get(Messages.NAME);
        component.setSortAscendingLabel(messages.getMessage(WebAbstractTable.class, "tableSort.ascending"));
        component.setSortResetLabel(messages.getMessage(WebAbstractTable.class, "tableSort.reset"));
        component.setSortDescendingLabel(messages.getMessage(WebAbstractTable.class, "tableSort.descending"));

        setClientCaching(component);

        int defaultRowHeaderWidth = 16;
        ThemeConstants theme = App.getInstance().getThemeConstants();
        if (theme != null) {
            defaultRowHeaderWidth = theme.getInt("cuba.web.Table.defaultRowHeaderWidth");
        }

        // CAUTION: vaadin considers null as row header property id;
        component.setColumnWidth(null, defaultRowHeaderWidth); // todo get width from theme

        contextMenuPopup.setParent(component);
        component.setContextMenuPopup(contextMenuPopup);

        shortcutsDelegate.setAllowEnterShortcut(false);

        component.addValueChangeListener(event -> {
            if (datasource == null) {
                return;
            }

            final Set<? extends Entity> selected = getSelected();
            if (selected.isEmpty()) {
                Entity dsItem = datasource.getItemIfValid();
                datasource.setItem(null);
                if (dsItem == null) {
                    // in this case item change event will not be generated
                    refreshActionsState();
                }
            } else {
                // reset selection and select new item
                if (isMultiSelect()) {
                    datasource.setItem(null);
                }

                Entity newItem = selected.iterator().next();
                Entity dsItem = datasource.getItemIfValid();
                datasource.setItem(newItem);

                if (ObjectUtils.equals(dsItem, newItem)) {
                    // in this case item change event will not be generated
                    refreshActionsState();
                }
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

        component.addItemClickListener(event -> {
            if (event.isDoubleClick() && event.getItem() != null) {
                handleClickAction();
            }
        });

        component.setSelectable(true);
        component.setTableFieldFactory(new WebTableFieldFactory());
        component.setColumnCollapsingAllowed(true);
        component.setColumnReorderingAllowed(true);

        setEditable(false);

        componentComposition = new VerticalLayout();
        componentComposition.addComponent(component);

        componentComposition.setSpacing(true);
        componentComposition.setMargin(false);
        componentComposition.setWidth("-1px");

        // todo artamonov adjust component size relative to composition size

        component.setSizeFull();
        componentComposition.setExpandRatio(component, 1);

        component.setCellStyleGenerator(createStyleGenerator());
    }

    protected void setClientCaching(T component) {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        WebConfig webConfig = configuration.getConfig(WebConfig.class);

        double cacheRate = webConfig.getTableCacheRate();
        if (cacheRate >= 0) {
            component.setCacheRate(cacheRate);
        }
        int pageLength = webConfig.getTablePageLength();
        if (pageLength >= 0) {
            component.setPageLength(pageLength);
        }
    }

    protected void refreshActionsState() {
        for (Action action : getActions()) {
            action.refreshState();
        }
    }

    protected StyleGeneratorAdapter createStyleGenerator() {
        return new StyleGeneratorAdapter();
    }

    @SuppressWarnings("unchecked")
    protected String getGeneratedCellStyle(Object itemId, Object propertyId) {
        if (styleProviders == null) {
            return null;
        }

        Entity item = datasource.getItem(itemId);
        String joinedStyle = null;
        for (StyleProvider styleProvider : styleProviders) {
            String styleName = styleProvider.getStyleName(item, propertyId == null ? null : propertyId.toString());
            if (styleName != null) {
                if (joinedStyle == null) {
                    joinedStyle = styleName;
                } else {
                    joinedStyle += " " + styleName;
                }
            }
        }

        return joinedStyle;
    }

    @Override
    protected ContextMenuButton createContextMenuButton() {
        //noinspection IncorrectCreateGuiComponent
        return new ContextMenuButton(showIconsForPopupMenuActions) {
            @Override
            protected void beforeActionPerformed() {
                WebAbstractTable.this.component.hideContextMenuPopup();
            }

            @Override
            protected void performAction(Action action) {
                // do action for table component
                action.actionPerform(WebAbstractTable.this);
            }
        };
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
            Window window = ComponentsHelper.getWindowImplementation(WebAbstractTable.this);
            if (window instanceof Window.Wrapper) {
                window = ((Window.Wrapper) window).getWrappedWindow();
            }

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

        Window window = ComponentsHelper.getWindowImplementation(this);
        boolean isLookup = window instanceof Window.Lookup;

        for (MetaPropertyPath propertyPath : properties) {
            final Table.Column column = columns.get(propertyPath);
            if (column != null && !(editable && BooleanUtils.isTrue(column.isEditable()))) {
                final String isLink =
                        column.getXmlDescriptor() == null ?
                                null : column.getXmlDescriptor().attributeValue("link");

                if (propertyPath.getRange().isClass()) {
                    if (!isLookup && StringUtils.isNotEmpty(isLink)) {
                        setClickListener(propertyPath.toString(), new LinkCellClickListener());
                    }
                } else if (propertyPath.getRange().isDatatype()) {
                    if (!isLookup && !StringUtils.isEmpty(isLink)) {
                        setClickListener(propertyPath.toString(), new LinkCellClickListener());
                    } else if (editable && BooleanUtils.isTrue(column.isCalculatable())) {
                        addGeneratedColumn(propertyPath, new CalculatableColumnGenerator());
                    } else {
                        if (column.getMaxTextLength() != null) {
                            addGeneratedColumn(propertyPath, new AbbreviatedColumnGenerator(column));
                            setClickListener(propertyPath.toString(), new AbbreviatedCellClickListener());
                        }
                    }
                } else if (!propertyPath.getRange().isEnum()) {
                    throw new UnsupportedOperationException();
                }
            }
        }

        return properties;
    }

    @Override
    public void setDatasource(final CollectionDatasource datasource) {
        Preconditions.checkNotNullArgument(datasource, "datasource is null");

        MessageTools messageTools = AppBeans.get(MessageTools.NAME);
        MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);

        final Collection<Object> columns;
        if (this.columns.isEmpty()) {
            Collection<MetaPropertyPath> paths = datasource.getView() != null ?
                    // if a view is specified - use view properties
                    metadataTools.getViewPropertyPaths(datasource.getView(), datasource.getMetaClass()) :
                    // otherwise use only string properties from meta-class - the temporary solution for KeyValue datasources
                    metadataTools.getPropertyPaths(datasource.getMetaClass()).stream()
                            .filter(mpp -> mpp.getRangeJavaClass().equals(String.class))
                            .collect(Collectors.toList());
            for (MetaPropertyPath metaPropertyPath : paths) {
                MetaProperty property = metaPropertyPath.getMetaProperty();
                if (!property.getRange().getCardinality().isMany() && !metadataTools.isSystem(property)) {
                    Table.Column column = new Table.Column(metaPropertyPath);

                    String propertyName = property.getName();
                    MetaClass propertyMetaClass = metadataTools.getPropertyEnclosingMetaClass(metaPropertyPath);

                    column.setCaption(messageTools.getPropertyCaption(propertyMetaClass, propertyName));
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
        //noinspection unchecked
        datasource.addCollectionChangeListener(e -> {
            if (fieldDatasources != null) {
                switch (e.getOperation()) {
                    case CLEAR:
                    case REFRESH:
                        fieldDatasources.clear();
                        break;

                    case UPDATE:
                    case REMOVE:
                        for (Object entity : e.getItems()) {
                            fieldDatasources.remove(entity);
                        }
                        break;
                }
            }
        });

        final CollectionDsWrapper containerDatasource = createContainerDatasource(datasource, getPropertyColumns());

        component.setContainerDataSource(containerDatasource);

        List<MetaPropertyPath> editableColumns = null;
        if (isEditable()) {
            editableColumns = new LinkedList<>();
        }

        MetaClass metaClass = datasource.getMetaClass();
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
                    MetaPropertyPath propertyPath = ((MetaPropertyPath) columnId);
                    if (security.isEntityAttrUpdatePermitted(metaClass, propertyPath.toString())) {
                        editableColumns.add(propertyPath);
                    }
                }

                if (column.isCollapsed() && component.isColumnCollapsingAllowed()) {
                    component.setColumnCollapsed(column.getId(), true);
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

        for (Table.Column column : this.columnsOrder) {
            if (editable && column.getAggregation() != null
                    && (BooleanUtils.isTrue(column.isEditable()) || BooleanUtils.isTrue(column.isCalculatable()))) {
                addAggregationCell(column);
            }
        }

        datasource.addItemPropertyChangeListener(createAggregationDatasourceListener());

        createStubsForGeneratedColumns();

        setVisibleColumns(getInitialVisibleColumns());

        if (security.isSpecificPermitted(ShowInfoAction.ACTION_PERMISSION)) {
            ShowInfoAction action = (ShowInfoAction) getAction(ShowInfoAction.ACTION_ID);
            if (action == null) {
                action = new ShowInfoAction();
                addAction(action);
            }
            action.setDatasource(datasource);
        }

        if (rowsCount != null) {
            rowsCount.setDatasource(datasource);
        }

        //noinspection unchecked
        datasource.addCollectionChangeListener(e -> {
            // #PL-2035, reload selection from ds
            Set<Object> selectedItemIds = getSelectedItemIds();
            if (selectedItemIds == null) {
                selectedItemIds = Collections.emptySet();
            }

            Set<Object> newSelection = new HashSet<>();
            for (Object entityId : selectedItemIds) {
                //noinspection unchecked
                if (e.getDs().containsItem(entityId)) {
                    newSelection.add(entityId);
                }
            }

            if (e.getDs().getState() == Datasource.State.VALID && e.getDs().getItem() != null) {
                newSelection.add(e.getDs().getItem().getId());
            }

            if (newSelection.isEmpty()) {
                setSelected((E) null);
            } else {
                setSelectedIds(newSelection);
            }
        });

        //noinspection unchecked
        new CollectionDsActionsNotifier(this).bind(datasource);

        for (Action action : getActions()) {
            action.refreshState();
        }

        if (!canBeSorted(datasource))
            setSortable(false);

        assignAutoDebugId();
    }

    protected boolean canBeSorted(CollectionDatasource datasource) {
        //noinspection SimplifiableConditionalExpression
        return datasource instanceof PropertyDatasource ?
                ((PropertyDatasource) datasource).getProperty().getRange().isOrdered() : true;
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

    protected void createStubsForGeneratedColumns() {
        com.vaadin.ui.Table.ColumnGenerator columnGeneratorStub = new com.vaadin.ui.Table.ColumnGenerator() {
            @Override
            public Object generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
                return null;
            }
        };

        for (Column column : columnsOrder) {
            if (!(column.getId() instanceof MetaPropertyPath)
                    && component.getColumnGenerator(column.getId()) == null) {
                component.addGeneratedColumn(column.getId(), columnGeneratorStub);
            }
        }
    }

    protected List<Object> getInitialVisibleColumns() {
        List<Object> result = new ArrayList<>();

        MetaClass metaClass = datasource.getMetaClass();
        for (Column column : columnsOrder) {
            if (column.getId() instanceof MetaPropertyPath) {
                MetaPropertyPath propertyPath = (MetaPropertyPath) column.getId();
                if (security.isEntityAttrReadPermitted(metaClass, propertyPath.toString())) {
                    result.add(column.getId());
                }
            } else {
                result.add(column.getId());
            }
        }
        return result;
    }

    protected List<MetaPropertyPath> getPropertyColumns() {
        List<MetaPropertyPath> result = new ArrayList<>();

        MetaClass metaClass = datasource.getMetaClass();
        for (Column column : columnsOrder) {
            if (column.getId() instanceof MetaPropertyPath) {
                MetaPropertyPath propertyPath = (MetaPropertyPath) column.getId();
                if (security.isEntityAttrReadPermitted(metaClass, propertyPath.toString())) {
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
        if (required) {
            if (requiredColumns == null) {
                requiredColumns = new HashMap<>();
            }
            requiredColumns.put(column, message);
        } else {
            if (requiredColumns != null) {
                requiredColumns.remove(column);
            }
        }
    }

    @Override
    public void addValidator(Table.Column column, final com.haulmont.cuba.gui.components.Field.Validator validator) {
        if (validatorsMap == null) {
            validatorsMap = new HashMap<>();
        }

        Set<com.haulmont.cuba.gui.components.Field.Validator> validators = validatorsMap.get(column);
        if (validators == null) {
            validators = new HashSet<>();
            validatorsMap.put(column, validators);
        }
        validators.add(validator);
    }

    @Override
    public void addValidator(final com.haulmont.cuba.gui.components.Field.Validator validator) {
        if (tableValidators == null) {
            tableValidators = new LinkedHashSet<>();
        }

        tableValidators.add(validator);
    }

    public void validate() throws ValidationException {
        if (tableValidators != null) {
            for (com.haulmont.cuba.gui.components.Field.Validator tableValidator : tableValidators) {
                tableValidator.validate(getSelected());
            }
        }
    }

    @Override
    public void setStyleProvider(@Nullable Table.StyleProvider styleProvider) {
        if (styleProvider != null) {
            if (this.styleProviders == null) {
                this.styleProviders = new LinkedList<>();
            } else {
                this.styleProviders.clear();
            }

            this.styleProviders.add(styleProvider);
        } else {
            this.styleProviders = null;
        }

        component.refreshCellStyles();
    }

    @Override
    public void addStyleProvider(StyleProvider styleProvider) {
        if (this.styleProviders == null) {
            this.styleProviders = new LinkedList<>();
        }

        if (!this.styleProviders.contains(styleProvider)) {
            this.styleProviders.add(styleProvider);

            component.refreshCellStyles();
        }
    }

    @Override
    public void removeStyleProvider(StyleProvider styleProvider) {
        if (this.styleProviders != null) {
            if (this.styleProviders.remove(styleProvider)) {
                component.refreshCellStyles();
            }
        }
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

    // For vaadin component extensions
    @SuppressWarnings("unchecked")
    protected Resource getItemIcon(Object itemId) {
        if (iconProvider == null) {
            return null;
        }
        Entity item = datasource.getItem(itemId);
        if (item == null) {
            return null;
        }
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
        if (!isSettingsEnabled()) {
            return;
        }

        if (defaultSettings == null) {
            // save default view before apply custom
            defaultSettings = DocumentHelper.createDocument();
            defaultSettings.setRootElement(defaultSettings.addElement("presentation"));

            saveSettings(defaultSettings.getRootElement());
        }

        String textSelection = element.attributeValue("textSelection");
        if (StringUtils.isNotEmpty(textSelection)) {
            component.setTextSelectionEnabled(Boolean.parseBoolean(textSelection));

            if (component.getPresentations() != null) {
                component.getPresentations().updateTextSelection();
            }
        }

        final Element columnsElem = element.element("columns");
        if (columnsElem != null) {
            boolean refreshWasEnabled = component.disableContentBufferRefreshing();

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

            component.enableContentBufferRefreshing(refreshWasEnabled);
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
                        component.setColumnWidth(column, Integer.parseInt(width));
                    } else {
                        component.setColumnWidth(column, -1);
                    }

                    String visible = colElem.attributeValue("visible");
                    if (visible != null) {
                        if (component.isColumnCollapsingAllowed()) { // throws exception if not
                            component.setColumnCollapsed(column, !Boolean.parseBoolean(visible));
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
                    boolean sortAscending = Boolean.parseBoolean(columnsElem.attributeValue("sortAscending"));

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
        if (!isSettingsEnabled()) {
            return false;
        }

        if (isUsePresentations()) {
            element.addAttribute("textSelection", String.valueOf(component.isTextSelectionEnabled()));
        }

        Element columnsElem = element.element("columns");
        if (columnsElem != null) {
            element.remove(columnsElem);
        }
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
    public boolean isSettingsEnabled() {
        return settingsEnabled;
    }

    @Override
    public void setSettingsEnabled(boolean settingsEnabled) {
        this.settingsEnabled = settingsEnabled;
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
            buttonsPanel.setParent(null);
        }
        buttonsPanel = panel;
        if (panel != null) {
            if (panel.getParent() != null && panel.getParent() != this) {
                throw new IllegalStateException("Component already has parent");
            }

            if (topPanel == null) {
                topPanel = new HorizontalLayout();
                topPanel.setWidth("100%");
                componentComposition.addComponentAsFirst(topPanel);
            }
            topPanel.addComponent(WebComponentsHelper.unwrap(panel));
            panel.setParent(this);
        }
    }

    @Override
    public void addGeneratedColumn(String columnId, ColumnGenerator<E> generator) {
        checkNotNullArgument(columnId, "columnId is null");
        checkNotNullArgument(generator, "generator is null for column id '%s'", columnId);

        MetaPropertyPath targetCol = getDatasource().getMetaClass().getPropertyPath(columnId);
        Object generatedColumnId = targetCol != null ? targetCol : columnId;

        Column column = getColumn(columnId);
        Column associatedRuntimeColumn = null;
        if (column == null) {
            Column newColumn = new Column(generatedColumnId);

            columns.put(newColumn.getId(), newColumn);
            columnsOrder.add(newColumn);

            associatedRuntimeColumn = newColumn;
            newColumn.setOwner(this);
        }

        // save column order
        Object[] visibleColumns = component.getVisibleColumns();

        boolean removeOldGeneratedColumn = component.getColumnGenerator(generatedColumnId) != null;
        // replace generator for column if exist
        if (removeOldGeneratedColumn) {
            component.removeGeneratedColumn(generatedColumnId);
        }

        component.addGeneratedColumn(
                generatedColumnId,
                new CustomColumnGenerator(generator, associatedRuntimeColumn) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Object generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
                        Entity entity = getDatasource().getItem(itemId);

                        com.haulmont.cuba.gui.components.Component component = getColumnGenerator().generateCell(entity);
                        if (component == null) {
                            return null;
                        }

                        if (component instanceof PlainTextCell) {
                            return ((PlainTextCell) component).getText();
                        }

                        if (component instanceof BelongToFrame) {
                            BelongToFrame belongToFrame = (BelongToFrame) component;
                            if (belongToFrame.getFrame() == null) {
                                belongToFrame.setFrame(getFrame());
                            }
                        }
                        component.setParent(WebAbstractTable.this);

                        com.vaadin.ui.Component vComponent = WebComponentsHelper.getComposition(component);

                        // wrap field for show required asterisk
                        if ((vComponent instanceof com.vaadin.ui.Field)
                                && (((com.vaadin.ui.Field) vComponent).isRequired())) {
                            VerticalLayout layout = new VerticalLayout();
                            layout.addComponent(vComponent);

                            if (vComponent.getWidth() < 0) {
                                layout.setWidthUndefined();
                            }

                            layout.addComponent(vComponent);
                            vComponent = layout;
                        }
                        return vComponent;
                    }
                }
        );

        if (removeOldGeneratedColumn) {
            // restore column order
            component.setVisibleColumns(visibleColumns);
        }
    }

    @Override
    public void addGeneratedColumn(String columnId, ColumnGenerator<E> generator,
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
    public void addAggregationProperty(String columnId, AggregationInfo.Type type) {
        addAggregationProperty(getColumn(columnId), type);
    }

    @Override
    public void addAggregationProperty(Column column, AggregationInfo.Type type) {
        component.addContainerPropertyAggregation(column.getId(), WebComponentsHelper.convertAggregationType(type));

        if (column.getAggregation() != null) {
            addAggregationCell(column);
        }
    }

    @Override
    public void removeAggregationProperty(String columnId) {
        component.removeContainerPropertyAggregation(getColumn(columnId).getId());
        removeAggregationCell(getColumn(columnId));
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
    public void setColumnDescription(String columnId, String description) {
        Column column = getColumn(columnId);
        if (column == null) {
            throw new IllegalStateException(String.format("Column with id '%s' not found", columnId));
        }

        setColumnDescription(column, description);
    }

    @Override
    public void setColumnDescription(Column column, String description) {
        checkNotNullArgument(column, "column must be non null");

        if (!StringUtils.equals(column.getDescription(), description)) {
            column.setDescription(description);
        }
        component.setColumnDescription(column.getId(), description);
    }

    @Override
    public void setColumnSortable(String columnId, boolean sortable) {
        Column column = getColumn(columnId);
        setColumnSortable(column, sortable);
    }

    @Override
    public boolean getColumnSortable(String columnId) {
        Column column = getColumn(columnId);
        return getColumnSortable(column);
    }

    @Override
    public void setColumnSortable(Column column, boolean sortable) {
        checkNotNullArgument(column, "column must be non null");
        if (column.isSortable() != sortable) {
            column.setSortable(sortable);
        }
        component.setColumnSortable(column.getId(), sortable);
    }

    @Override
    public boolean getColumnSortable(Column column) {
        checkNotNullArgument(column, "column must be non null");
        return component.getColumnSortable(column.getId());
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
    public void setColumnAlignment(Column column, ColumnAlignment alignment) {
        checkNotNullArgument(column, "column must be non null");

        if (column.getAlignment() != alignment) {
            column.setAlignment(alignment);
        }
        component.setColumnAlignment(column.getId(), WebComponentsHelper.convertColumnAlignment(alignment));
    }

    @Override
    public void setColumnAlignment(String columnId, ColumnAlignment alignment) {
        Column column = getColumn(columnId);
        if (column == null) {
            throw new IllegalStateException(String.format("Column with id '%s' not found", columnId));
        }

        setColumnAlignment(column, alignment);
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
        component.markAsDirtyRecursive();
    }

    @Override
    public void selectAll() {
        if (isMultiSelect()) {
            component.setValue(component.getItemIds());
        }
    }

    protected boolean isAggregationCorrect(AggregationInfo aggregationInfo) {
        MetaPropertyPath propertyPath = aggregationInfo.getPropertyPath();
        MetaProperty[] metaProperties = propertyPath.getMetaProperties();
        Class<?> javaType = metaProperties[metaProperties.length - 1].getJavaType();
        return Aggregations.get(javaType) != null || aggregationInfo.getType() == AggregationInfo.Type.CUSTOM;
    }

    protected Map<Object, Object> __aggregate(AggregationContainer container, AggregationContainer.Context context) {
        final List<AggregationInfo> aggregationInfos = new LinkedList<>();
        for (final Object propertyId : container.getAggregationPropertyIds()) {
            final Table.Column column = columns.get(propertyId);
            AggregationInfo aggregation = column.getAggregation();
            if (aggregation != null) {
                if (!isAggregationCorrect(aggregation)) {
                    String msg = String.format("Unable to aggregate column \"%s\" with data type %s with default aggregation strategy: %s",
                            column.getId(), aggregation.getPropertyPath().getRange(), aggregation.getType());
                    throw new UnsupportedOperationException(msg);
                }

                aggregationInfos.add(aggregation);
            }
        }
        @SuppressWarnings("unchecked")
        Map<AggregationInfo, Object> results = ((CollectionDatasource.Aggregatable) datasource).aggregate(
                aggregationInfos.toArray(new AggregationInfo[aggregationInfos.size()]),
                context.getItemIds()
        );
        Map<Object, Object> resultsByColumns = new LinkedHashMap<>();
        for (final Object propertyId : container.getAggregationPropertyIds()) {
            final Table.Column column = columns.get(propertyId);
            if (column.getAggregation() != null) {
                resultsByColumns.put(column.getId(), results.get(column.getAggregation()));
            }
        }
        if (aggregationCells != null) {
            resultsByColumns = __handleAggregationResults(context, resultsByColumns);
        }
        return resultsByColumns;
    }

    protected Map<Object, Object> __handleAggregationResults(AggregationContainer.Context context,
                                                             Map<Object, Object> results) {
        for (final Map.Entry<Object, Object> entry : results.entrySet()) {
            final Table.Column column = columns.get(entry.getKey());
            if (aggregationCells.get(column) != null) {
                Object value = entry.getValue();
                String cellText = getFormattedValue(column, value);
                entry.setValue(cellText);
            }
        }
        return results;
    }

    protected String getFormattedValue(Column column, Object value) {
        String cellText;
        if (value == null) {
            cellText = "";
        } else {
            if (value instanceof String) {
                cellText = (String) value;
            } else {
                Formatter formatter = column.getFormatter();
                if (formatter != null) {
                    //noinspection unchecked
                    cellText = formatter.format(value);
                } else {
                    Datatype datatype = Datatypes.get(value.getClass());
                    if (datatype != null) {
                        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
                        cellText = datatype.format(value, sessionSource.getLocale());
                    } else {
                        cellText = value.toString();
                    }
                }
            }
        }
        return cellText;
    }

    protected class TablePropertyWrapper extends PropertyWrapper {

        protected ValueChangeListener calcListener;

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

        @SuppressWarnings("unchecked")
        @Override
        public String getFormattedValue() {
            final Table.Column column = WebAbstractTable.this.columns.get(propertyPath);
            if (column != null) {
                if (column.getFormatter() != null) {
                    return column.getFormatter().format(getValue());
                } else if (column.getXmlDescriptor() != null) {
                    String captionProperty = column.getXmlDescriptor().attributeValue("captionProperty");
                    if (StringUtils.isNotEmpty(captionProperty)) {
                        final Instance item = getInstance();
                        final Object captionValue = item.getValueEx(captionProperty);
                        return captionValue != null ? String.valueOf(captionValue) : null;
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

    protected class LinkCellClickListener implements CellClickListener {

        @Override
        public void onClick(final Entity rowItem, final String columnId) {
            Column column = getColumn(columnId);
            if (column.getXmlDescriptor() != null) {
                String invokeMethodName = column.getXmlDescriptor().attributeValue("linkInvoke");
                if (StringUtils.isNotEmpty(invokeMethodName)) {
                    callControllerInvoke(rowItem, columnId, invokeMethodName);

                    return;
                }
            }

            Entity entity;
            Object value = rowItem.getValueEx(columnId);

            if (value instanceof Entity) {
                entity = (Entity) value;
            } else {
                entity = rowItem;
            }

            WindowManager wm;
            Window window = ComponentsHelper.getWindow(WebAbstractTable.this);
            if (window == null) {
                throw new IllegalStateException("Please specify Frame for Table");
            } else {
                wm = window.getWindowManager();
            }

            if (entity instanceof SoftDelete && ((SoftDelete) entity).isDeleted()) {
                Messages messages = AppBeans.get(Messages.NAME);
                wm.showNotification(messages.getMessage(EntityLinkField.class, "OpenAction.objectIsDeleted"),
                        Frame.NotificationType.HUMANIZED);
                return;
            }

            DataSupplier dataSupplier = window.getDsContext().getDataSupplier();
            entity = dataSupplier.reload(entity, View.MINIMAL);

            WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);

            String windowAlias = null;
            if (column.getXmlDescriptor() != null) {
                windowAlias = column.getXmlDescriptor().attributeValue("linkScreen");
            }
            if (StringUtils.isEmpty(windowAlias)) {
                windowAlias = windowConfig.getEditorScreenId(entity.getMetaClass());
            }

            OpenType screenOpenType = OpenType.THIS_TAB;
            if (column.getXmlDescriptor() != null) {
                String openTypeAttribute = column.getXmlDescriptor().attributeValue("linkScreenOpenType");
                if (StringUtils.isNotEmpty(openTypeAttribute)) {
                    screenOpenType = OpenType.valueOf(openTypeAttribute);
                }
            }

            final Window.Editor editor = wm.openEditor(
                    windowConfig.getWindowInfo(windowAlias),
                    entity,
                    screenOpenType
            );
            editor.addCloseListener(actionId -> {
                // move focus to component
                requestFocus();

                if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                    Entity editorItem = editor.getItem();

                    handleEditorCommit(editorItem, rowItem, columnId);
                }
            });
        }

        protected void handleEditorCommit(Entity editorItem, Entity rowItem, String columnId) {
            MetaPropertyPath mpp = rowItem.getMetaClass().getPropertyPath(columnId);
            if (mpp == null) {
                throw new IllegalStateException(String.format("Unable to find metaproperty %s for class %s",
                        columnId, rowItem.getMetaClass()));
            }

            if (mpp.getRange().isClass()) {
                DatasourceImplementation ds = ((DatasourceImplementation) getDatasource());
                boolean modifiedInTable = ds.getItemsToUpdate().contains(rowItem);
                boolean ownerDsModified = ds.isModified();

                rowItem.setValueEx(columnId, null);
                rowItem.setValueEx(columnId, editorItem);

                // restore modified for owner datasource
                // remove from items to update if it was not modified before setValue
                if (!modifiedInTable) {
                    ds.getItemsToUpdate().remove(rowItem);
                }
                ds.setModified(ownerDsModified);
            } else {
                //noinspection unchecked
                getDatasource().updateItem(editorItem);
            }
        }

        protected void callControllerInvoke(Entity rowItem, String columnId, String invokeMethodName) {
            Object controller = ComponentsHelper.getFrameController(frame);
            Method method;
            try {
                method = controller.getClass().getMethod(invokeMethodName, Entity.class, String.class);
                try {
                    method.invoke(controller, rowItem, columnId);
                } catch (Exception e) {
                    throw new RuntimeException("Unable to cal linkInvoke method for table column", e);
                }
            } catch (NoSuchMethodException e) {
                try {
                    method = controller.getClass().getMethod(invokeMethodName);
                    try {
                        method.invoke(controller);
                    } catch (Exception e1) {
                        throw new RuntimeException("Unable to cal linkInvoke method for table column", e1);
                    }
                } catch (NoSuchMethodException e1) {
                    throw new IllegalStateException("No suitable methods named " + invokeMethodName + " for invoke");
                }
            }
        }
    }

    protected static class AbbreviatedColumnGenerator implements SystemTableColumnGenerator,
            CubaEnhancedTable.PlainTextGeneratedColumn {

        protected Table.Column column;

        public AbbreviatedColumnGenerator(Table.Column column) {
            this.column = column;
        }

        @Override
        public Object generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
            final Property property = source.getItem(itemId).getItemProperty(columnId);
            final Object value = property.getValue();

            if (value == null) {
                return null;
            }

            String stringValue = value.toString();
            String cellValue = stringValue;
            boolean isMultiLineCell = StringUtils.contains(stringValue, "\n");
            if (isMultiLineCell) {
                cellValue = StringUtils.replace(cellValue, "\n", " ");
            }

            int maxTextLength = column.getMaxTextLength();
            if (stringValue.length() > maxTextLength + MAX_TEXT_LENGTH_GAP || isMultiLineCell) {
                return StringUtils.abbreviate(cellValue, maxTextLength);
            } else {
                return cellValue;
            }
        }
    }

    protected class AbbreviatedCellClickListener implements CellClickListener {
        @Override
        public void onClick(Entity item, String columnId) {
            Column column = getColumn(columnId);
            String value = item.getValueEx(columnId);
            if (column.getMaxTextLength() != null) {
                boolean isMultiLineCell = StringUtils.contains(value, "\n");
                if (value == null || (value.length() <= column.getMaxTextLength() + MAX_TEXT_LENGTH_GAP
                        && !isMultiLineCell)) {
                    // todo artamonov if we click with CTRL and Table is multiselect then we lose previous selected items
                    if (!getSelected().contains(item)) {
                        setSelected((E) item);
                    }
                    // do not show popup view
                    return;
                }
            }

            VerticalLayout layout = new VerticalLayout();
            layout.setWidthUndefined();
            layout.setStyleName("cuba-table-view-textcut");

            CubaTextArea textArea = new CubaTextArea();
            textArea.setValue(value);
            textArea.setReadOnly(true);

            CubaResizableTextAreaWrapper content = new CubaResizableTextAreaWrapper(textArea);
            content.setResizable(true);

            ThemeConstants theme = App.getInstance().getThemeConstants();
            if (theme != null) {
                content.setWidth(theme.get("cuba.web.Table.abbreviatedPopupWidth"));
                content.setHeight(theme.get("cuba.web.Table.abbreviatedPopupHeight"));
            } else {
                content.setWidth("320px");
                content.setHeight("200px");
            }

            layout.addComponent(content);

            component.showCustomPopup(layout);
            component.setCustomPopupAutoClose(false);
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

    protected void removeAggregationCell(Table.Column column) {
        if (aggregationCells != null) {
            aggregationCells.remove(column);
        }
    }

    protected void addAggregationCell(Table.Column column) {
        if (aggregationCells == null) {
            aggregationCells = new HashMap<>();
        }
        aggregationCells.put(column, "");
    }

    protected CollectionDatasource.ItemPropertyChangeListener createAggregationDatasourceListener() {
        return new AggregationDatasourceListener();
    }

    protected class AggregationDatasourceListener implements Datasource.ItemPropertyChangeListener<Entity> {

        @Override
        public void itemPropertyChanged(Datasource.ItemPropertyChangeEvent<Entity> e) {
            if (isAggregatable() && aggregationCells != null) {
                final CollectionDatasource ds = WebAbstractTable.this.getDatasource();
                component.aggregate(new AggregationContainer.Context(ds.getItemIds()));

                // trigger aggregation repaint
                component.markAsDirty();
            }
        }
    }

    protected class WebTableFieldFactory extends AbstractFieldFactory implements TableFieldFactory {

        @Override
        public com.vaadin.ui.Field<?> createField(com.vaadin.data.Container container,
                                                  Object itemId, Object propertyId, Component uiContext) {

            String fieldPropertyId = String.valueOf(propertyId);

            Column columnConf = columns.get(propertyId);

            Item item = container.getItem(itemId);
            Entity entity = ((ItemWrapper) item).getItem();
            Datasource fieldDatasource = getItemDatasource(entity);

            com.haulmont.cuba.gui.components.Component columnComponent =
                    createField(fieldDatasource, fieldPropertyId, columnConf.getXmlDescriptor());

            if (columnComponent instanceof Field) {
                Field cubaField = (Field) columnComponent;

                if (requiredColumns != null && requiredColumns.containsKey(columnConf)) {
                    cubaField.setRequired(true);
                    cubaField.setRequiredMessage(requiredColumns.get(columnConf));
                }
            }

            if (columnConf.getWidth() != null) {
                columnComponent.setWidth(columnConf.getWidth() + "px");
            } else if (!(columnComponent instanceof CheckBox)) {
                columnComponent.setWidth("100%");
            }

            if (columnComponent instanceof BelongToFrame) {
                BelongToFrame belongToFrame = (BelongToFrame) columnComponent;
                if (belongToFrame.getFrame() == null) {
                    belongToFrame.setFrame(getFrame());
                }
            }

            applyPermissions(columnComponent);

            columnComponent.setParent(WebAbstractTable.this);

            Component componentImpl = getComponentImplementation(columnComponent);
            if (componentImpl instanceof com.vaadin.ui.Field) {
                return (com.vaadin.ui.Field<?>) componentImpl;
            }

            return new EditableColumnFieldWrapper(componentImpl);
        }

        protected Component getComponentImplementation(com.haulmont.cuba.gui.components.Component columnComponent) {
            com.vaadin.ui.Component composition = WebComponentsHelper.getComposition(columnComponent);
            Component componentImpl = composition;
            if (composition instanceof com.vaadin.ui.Field
                    && ((com.vaadin.ui.Field) composition).isRequired()) {
                VerticalLayout layout = new VerticalLayout();
                layout.addComponent(composition);

                if (composition.getWidth() < 0) {
                    layout.setWidthUndefined();
                }

                componentImpl = layout;
            }
            return componentImpl;
        }

        protected void applyPermissions(com.haulmont.cuba.gui.components.Component columnComponent) {
            if (columnComponent instanceof DatasourceComponent) {
                DatasourceComponent dsComponent = (DatasourceComponent) columnComponent;
                MetaPropertyPath propertyPath = dsComponent.getMetaPropertyPath();

                if (propertyPath != null) {
                    MetaClass metaClass = dsComponent.getDatasource().getMetaClass();
                    dsComponent.setEditable(dsComponent.isEditable()
                            && security.isEntityAttrUpdatePermitted(metaClass, propertyPath.toString()));
                }
            }
        }

        @Override
        @Nullable
        protected CollectionDatasource getOptionsDatasource(Datasource fieldDatasource, String propertyId) {
            if (datasource == null)
                throw new IllegalStateException("Table datasource is null");

            MetaPropertyPath metaPropertyPath = AppBeans.get(MetadataTools.NAME, MetadataTools.class)
                    .resolveMetaPropertyPath(datasource.getMetaClass(), propertyId);
            Column columnConf = columns.get(metaPropertyPath);
            final DsContext dsContext = datasource.getDsContext();

            String optDsName = columnConf.getXmlDescriptor() != null ?
                    columnConf.getXmlDescriptor().attributeValue("optionsDatasource") : "";

            if (StringUtils.isBlank(optDsName)) {
                return null;
            } else {
                CollectionDatasource ds = (CollectionDatasource) dsContext.get(optDsName);
                if (ds == null)
                    throw new IllegalStateException("Options datasource for table column '" + propertyId + "' not found: " + optDsName);

                return ds;
            }
        }
    }

    protected boolean handleSpecificVariables(Map<String, Object> variables) {
        boolean needReload = false;

        if (isUsePresentations() && presentations != null) {

            final Presentations p = getPresentations();

            if (p.getCurrent() != null && p.isAutoSave(p.getCurrent()) && needUpdatePresentation(variables)) {
                Element e = p.getSettings(p.getCurrent());
                saveSettings(e);
                p.setSettings(p.getCurrent(), e);
            }
        }

        //noinspection ConstantConditions
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
    public void resetPresentation() {
        if (defaultSettings != null) {
            applySettings(defaultSettings.getRootElement());
            if (presentations != null) {
                presentations.setCurrent(null);
            }
        }
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
        if (isUsePresentations() && presentations != null) {
            Presentation p = presentations.getPresentation(id);
            applyPresentation(p);
        } else {
            throw new UnsupportedOperationException("Component doesn't use presentations");
        }
    }

    @Override
    public void applyPresentationAsDefault(Object id) {
        if (isUsePresentations() && presentations != null) {
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
        if (presentations != null) {
            Element settingsElement = presentations.getSettings(p);
            applySettings(settingsElement);
            presentations.setCurrent(p);
            component.markAsDirty();
        }
    }

    @Override
    public Object getDefaultPresentationId() {
        if (presentations == null) {
            return null;
        }
        Presentation def = presentations.getDefault();
        return def == null ? null : def.getId();
    }

    @Override
    public void addColumnCollapsedListener(ColumnCollapseListener columnCollapsedListener) {
        if (columnCollapseListeners == null) {
            columnCollapseListeners = new LinkedList<>();

            component.addColumnCollapseListener((com.vaadin.ui.Table.ColumnCollapseListener) event -> {
                Column collapsedColumn = getColumn(event.getPropertyId().toString());
                boolean collapsed = component.isColumnCollapsed(event.getPropertyId());

                for (ColumnCollapseListener listener : columnCollapseListeners) {
                    listener.columnCollapsed(collapsedColumn, collapsed);
                }
            });
        }

        columnCollapseListeners.add(columnCollapsedListener);
    }

    @Override
    public void removeColumnCollapseListener(ColumnCollapseListener columnCollapseListener) {
        if (columnCollapseListeners != null) {
            columnCollapseListeners.remove(columnCollapseListener);
        }
    }

    @Override
    public void setClickListener(String columnId, final CellClickListener clickListener) {
        component.setClickListener(getColumn(columnId).getId(), (itemId, columnId1) -> {
            ItemWrapper wrapper = (ItemWrapper) component.getItem(itemId);
            Entity entity = wrapper.getItem();
            clickListener.onClick(entity, columnId1.toString());
        });
    }

    @Override
    public void removeClickListener(String columnId) {
        component.removeClickListener(getColumn(columnId).getId());
    }

    @Override
    public void showCustomPopup(com.haulmont.cuba.gui.components.Component popupComponent) {
        Component vComponent = WebComponentsHelper.unwrap(popupComponent);
        component.showCustomPopup(vComponent);
        component.setCustomPopupAutoClose(false);
    }

    @Override
    public void showCustomPopupActions(List<Action> actions) {
        VerticalLayout customContextMenu = new VerticalLayout();
        customContextMenu.setWidthUndefined();
        customContextMenu.setStyleName("cuba-context-menu-container");

        for (Action action : actions) {
            ContextMenuButton contextMenuButton = createContextMenuButton();
            contextMenuButton.setStyleName("cuba-context-menu-button");
            contextMenuButton.setAction(action);

            Component vButton = WebComponentsHelper.unwrap(contextMenuButton);
            customContextMenu.addComponent(vButton);
        }

        if (customContextMenu.getComponentCount() > 0) {
            component.showCustomPopup(customContextMenu);
            component.setCustomPopupAutoClose(true);
        }
    }

    @Override
    public void setColumnHeaderVisible(boolean visible) {
        component.setColumnHeaderMode(visible ?
                com.vaadin.ui.Table.ColumnHeaderMode.EXPLICIT_DEFAULTS_ID :
                com.vaadin.ui.Table.ColumnHeaderMode.HIDDEN);
    }

    @Override
    public boolean isColumnHeaderVisible() {
        if (component.getColumnHeaderMode() == com.vaadin.ui.Table.ColumnHeaderMode.HIDDEN) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setShowSelection(boolean showSelection) {
        component.setSelectable(showSelection);
    }

    @Override
    public boolean isShowSelection() {
        return component.isSelectable();
    }

    protected class StyleGeneratorAdapter implements com.vaadin.ui.Table.CellStyleGenerator {

        public static final String CUSTOM_STYLE_NAME_PREFIX = "cs ";

        @SuppressWarnings({"unchecked"})
        @Override
        public String getStyle(com.vaadin.ui.Table source, Object itemId, Object propertyId) {
            String style = null;
            if (propertyId != null && itemId != null
                    && !component.isColumnEditable(propertyId)
                    && (component.getColumnGenerator(propertyId) == null
                    || component.getColumnGenerator(propertyId) instanceof AbbreviatedColumnGenerator)) {

                MetaPropertyPath propertyPath;
                if (propertyId instanceof MetaPropertyPath) {
                    propertyPath = (MetaPropertyPath) propertyId;
                } else {
                    propertyPath = datasource.getMetaClass().getPropertyPath(propertyId.toString());
                }

                if (propertyPath != null) {
                    Column column = getColumn(propertyId.toString());
                    if (column != null) {
                        final String isLink = column.getXmlDescriptor() == null ?
                                null : column.getXmlDescriptor().attributeValue("link");

                        if (propertyPath.getRange().isClass()) {
                            if (StringUtils.isNotEmpty(isLink) && Boolean.valueOf(isLink)) {
                                style = "cuba-table-cell-link";
                            }
                        } else if (propertyPath.getRange().isDatatype()) {
                            if (StringUtils.isNotEmpty(isLink) && Boolean.valueOf(isLink)) {
                                style = "cuba-table-cell-link";
                            } else if (column.getMaxTextLength() != null) {
                                Entity item = getDatasource().getItemNN(itemId);
                                String value = item.getValueEx(propertyId.toString());
                                if (column.getMaxTextLength() != null) {
                                    boolean isMultiLineCell = StringUtils.contains(value, "\n");
                                    if ((value != null && value.length() > column.getMaxTextLength() + MAX_TEXT_LENGTH_GAP)
                                            || isMultiLineCell) {
                                        style = "cuba-table-cell-textcut";
                                    } else {
                                        // use special marker stylename
                                        style = "cuba-table-clickable-text";
                                    }
                                }
                            }
                        }
                    }

                    if (propertyPath.getRangeJavaClass() == Boolean.class) {
                        Entity item = datasource.getItem(itemId);
                        if (item != null) {
                            Boolean value = item.getValueEx(propertyId.toString());
                            if (BooleanUtils.isTrue(value)) {
                                style = "boolean-cell boolean-cell-true";
                            } else {
                                style = "boolean-cell boolean-cell-false";
                            }
                        }
                    }
                }
            }

            if (styleProviders != null) {
                String generatedStyle = getGeneratedCellStyle(itemId, propertyId);
                // we use style names without v-table-cell-content prefix, so we add cs prefix
                // all cells with custom styles will have v-table-cell-content-cs style name in class
                if (style != null) {
                    if (generatedStyle != null) {
                        style = CUSTOM_STYLE_NAME_PREFIX + generatedStyle + " " + style;
                    }
                } else if (generatedStyle != null) {
                    style = CUSTOM_STYLE_NAME_PREFIX + generatedStyle;
                }
            }

            return style == null ? null : (CUSTOM_STYLE_NAME_PREFIX + style);
        }
    }

    protected static class EditableColumnFieldWrapper extends CustomField {

        protected Component component;

        public EditableColumnFieldWrapper(Component component) {
            this.component = component;

            if (component.getWidth() < 0) {
                setWidthUndefined();
            }
        }

        @Override
        public Class getType() {
            return Object.class;
        }

        @Override
        protected Component initContent() {
            return component;
        }

        @Override
        public void setWidth(float width, Unit unit) {
            super.setWidth(width, unit);

            if (component != null) {
                if (width < 0) {
                    component.setWidth(com.haulmont.cuba.gui.components.Component.AUTO_SIZE);
                } else {
                    component.setWidth("100%");
                }
            }
        }

        @Override
        public void setHeight(float height, Unit unit) {
            super.setHeight(height, unit);

            if (component != null) {
                if (height < 0) {
                    component.setHeight(com.haulmont.cuba.gui.components.Component.AUTO_SIZE);
                } else {
                    component.setHeight("100%");
                }
            }
        }
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);

        if (getHeight() < 0) {
            component.setHeightUndefined();
        } else {
            component.setHeight(100, Unit.PERCENTAGE);
        }
    }
}