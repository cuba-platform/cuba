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

import com.google.common.base.Strings;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.client.sys.PersistenceManagerClient;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesTools;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.LocaleHelper;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.LookupComponent.LookupSelectionChangeNotifier;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.EntityTableSource;
import com.haulmont.cuba.gui.components.data.TableSource;
import com.haulmont.cuba.gui.components.data.table.CollectionDatasourceTableAdapter;
import com.haulmont.cuba.gui.components.data.table.SortableCollectionDatasourceTableAdapter;
import com.haulmont.cuba.gui.components.sys.ShowInfoAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.aggregation.Aggregation;
import com.haulmont.cuba.gui.data.aggregation.Aggregations;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.gui.presentations.PresentationsImpl;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.security.entity.Presentation;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.components.presentations.TablePresentations;
import com.haulmont.cuba.web.gui.components.table.*;
import com.haulmont.cuba.web.gui.components.util.ShortcutListenerDelegate;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CubaEnhancedTable;
import com.haulmont.cuba.web.widgets.data.AggregationContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.data.util.converter.ConverterUtil;
import com.vaadin.v7.ui.Table.ColumnHeaderMode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

@SuppressWarnings("deprecation")
public abstract class WebAbstractTable<T extends com.vaadin.v7.ui.Table & CubaEnhancedTable, E extends Entity>
        extends WebAbstractActionsHolderComponent<T>
        implements Table<E>, TableSourceEventsDelegate<E>, LookupSelectionChangeNotifier, InitializingBean {

    public static final int MAX_TEXT_LENGTH_GAP = 10;

    protected static final com.vaadin.v7.ui.Table.ColumnGenerator VOID_COLUMN_GENERATOR =
            (source, itemId, columnId) -> null;

    protected static final String HAS_TOP_PANEL_STYLENAME = "has-top-panel";
    protected static final String CUSTOM_STYLE_NAME_PREFIX = "cs ";

    // Vaadin considers null as row header property id
    protected static final Object ROW_HEADER_PROPERTY_ID = null;

    // Beans

    protected ApplicationContext applicationContext;
    protected Configuration configuration;
    protected IconResolver iconResolver;
    protected MetadataTools metadataTools;
    protected Security security;
    protected Messages messages;
    protected MessageTools messageTools;
    protected PersistenceManagerClient persistenceManagerClient;
    protected DatatypeRegistry datatypeRegistry;
    protected DynamicAttributesTools dynamicAttributesTools;

    protected Locale locale;

    // Style names used by table itself
    protected List<String> internalStyles = new ArrayList<>(2);

    protected Map<Object, Table.Column<E>> columns = new HashMap<>();
    protected List<Table.Column<E>> columnsOrder = new ArrayList<>();

    protected boolean editable;
    protected Action itemClickAction;
    protected Action enterPressAction;

    protected List<Table.StyleProvider> styleProviders; // lazily initialized List
    protected Table.IconProvider<? super E> iconProvider;

    protected Map<Table.Column, String> requiredColumns; // lazily initialized Map

    protected Map<Entity, Datasource> fieldDatasources; // lazily initialized WeakHashMap;

    protected TableComposition componentComposition;

    protected HorizontalLayout topPanel;

    protected ButtonsPanel buttonsPanel;

    protected RowsCount rowsCount;

    protected Map<Table.Column, String> aggregationCells = null;

    protected boolean usePresentations;
    protected Presentations presentations;
    protected Document defaultSettings;

    protected List<ColumnCollapseListener> columnCollapseListeners; // lazily initialized List

    // Map column id to Printable representation
    // todo this functionality should be moved to Excel action
    protected Map<String, Printable> printables; // lazily initialized Map

    protected boolean settingsEnabled = true;

    protected TableDataContainer<E> dataBinding;

    protected boolean ignoreUnfetchedAttributes;

    protected WebAbstractTable() {
    }

    @Override
    public void afterPropertiesSet() {
        initComponent(component);
    }

    @Inject
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Inject
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;

        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        ignoreUnfetchedAttributes = clientConfig.getIgnoreUnfetchedAttributesInTable();
    }

    @Inject
    public void setIconResolver(IconResolver iconResolver) {
        this.iconResolver = iconResolver;
    }

    @Inject
    public void setSecurity(Security security) {
        this.security = security;
    }

    @Inject
    public void setMetadataTools(MetadataTools metadataTools) {
        this.metadataTools = metadataTools;
    }

    @Inject
    public void setMessageTools(MessageTools messageTools) {
        this.messageTools = messageTools;
    }

    @Inject
    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    @Inject
    public void setPersistenceManagerClient(PersistenceManagerClient persistenceManagerClient) {
        this.persistenceManagerClient = persistenceManagerClient;
    }

    @Inject
    public void setUserSessionSource(UserSessionSource userSessionSource) {
        this.locale = userSessionSource.getLocale();
    }

    @Inject
    public void setDatatypeRegistry(DatatypeRegistry datatypeRegistry) {
        this.datatypeRegistry = datatypeRegistry;
    }

    @Inject
    public void setDynamicAttributesTools(DynamicAttributesTools dynamicAttributesTools) {
        this.dynamicAttributesTools = dynamicAttributesTools;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    protected Set<Object> getSelectedItemIds() {
        Object value = component.getValue();
        if (value == null) {
            return null;
        } else if (value instanceof Set) {
            return (Set) value;
        } else if (value instanceof Collection) {
            return new LinkedHashSet((Collection) value);
        } else {
            return Collections.singleton(value);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public E getSingleSelected() {
        TableSource<E> tableSource = getTableSource();
        if (tableSource == null
                || tableSource.getState() == BindingState.INACTIVE) {
            return null;
        }

        Set selected = getSelectedItemIds();
        return selected == null || selected.isEmpty() ?
                null : tableSource.getItem(selected.iterator().next());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<E> getSelected() {
        TableSource<E> tableSource = getTableSource();
        if (tableSource == null
                || tableSource.getState() == BindingState.INACTIVE) {
            return Collections.emptySet();
        }

        Set<Object> itemIds = getSelectedItemIds();

        if (itemIds != null) {
            if (itemIds.size() == 1) {
                E item = tableSource.getItem(itemIds.iterator().next());
                return Collections.singleton(item);
            }

            Set res = new LinkedHashSet<>();
            for (Object id : itemIds) {
                Entity item = tableSource.getItem(id);
                if (item != null)
                    res.add(item);
            }
            return res;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public void setSelected(E item) {
        if (item == null) {
            component.setValue(null);
        } else {
            setSelected(Collections.singletonList(item));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setSelected(Collection<E> items) {
        TableSource<E> tableSource = getTableSource();
        if (tableSource == null
                || tableSource.getState() == BindingState.INACTIVE) {
            throw new IllegalStateException("TableSource is not active");
        }

        if (items.isEmpty()) {
            setSelectedIds(Collections.emptyList());
        } else if (items.size() == 1) {
            E item = items.iterator().next();
            if (tableSource.getItem(item.getId()) == null) {
                throw new IllegalArgumentException("Datasource doesn't contain item to select: " + item);
            }
            setSelectedIds(Collections.singletonList(item.getId()));
        } else {
            Set itemIds = new HashSet();
            for (Entity item : items) {
                if (tableSource.getItem(item.getId()) == null) {
                    throw new IllegalArgumentException("Datasource doesn't contain item to select: " + item);
                }
                itemIds.add(item.getId());
            }
            setSelectedIds(itemIds);
        }
    }

    protected void setSelectedIds(Collection<Object> itemIds) {
        if (component.isMultiSelect()) {
            component.setValue(itemIds);
        } else {
            component.setValue(itemIds.size() > 0 ? itemIds.iterator().next() : null);
        }
    }

    @Override
    protected void attachAction(Action action) {
        if (action instanceof Action.HasTarget) {
            ((Action.HasTarget) action).setTarget(this);
        }

        super.attachAction(action);
    }

    @Override
    public void focus() {
        component.focus();
    }

    @Override
    public boolean isMultiSelect() {
        return component.isMultiSelect();
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        component.setMultiSelect(multiselect);
    }

    @Override
    public List<Table.Column<E>> getColumns() {
        return Collections.unmodifiableList(columnsOrder);
    }

    public Map<Object, Column<E>> getColumnsInternal() {
        return columns;
    }

    @Nullable
    public Map<Column, String> getRequiredColumnsInternal() {
        return requiredColumns;
    }

    @Override
    public Table.Column<E> getColumn(String id) {
        for (Table.Column<E> column : columnsOrder) {
            if (column.getStringId().equals(id))
                return column;
        }
        return null;
    }

    @Override
    public void addColumn(Table.Column<E> column) {
        checkNotNullArgument(column, "Column must be non null");

        Object columnId = column.getId();
        component.addContainerProperty(columnId, column.getType(), null);

        if (StringUtils.isNotBlank(column.getDescription())) {
            component.setColumnDescription(columnId, column.getDescription());
        }

        if (StringUtils.isNotBlank(column.getValueDescription())) {
            component.setAggregationDescription(columnId, column.getValueDescription());
        } else if (column.getAggregation() != null
                && column.getAggregation().getType() != AggregationInfo.Type.CUSTOM) {
            setColumnAggregationDescriptionByType(column, columnId);
        }

        if (!column.isSortable()) {
            component.setColumnSortable(columnId, column.isSortable());
        }

        columns.put(columnId, column);
        columnsOrder.add(column);
        if (column.getWidth() != null) {
            component.setColumnWidth(columnId, column.getWidth());
        }
        if (column.getAlignment() != null) {
            component.setColumnAlignment(columnId,
                    WebWrapperUtils.convertColumnAlignment(column.getAlignment()));
        }

        setColumnHeader(columnId, getColumnCaption(columnId, column));

        column.setOwner(this);

        MetaPropertyPath propertyPath = column.getBoundProperty();
        if (propertyPath != null) {
            MetaProperty metaProperty = propertyPath.getMetaProperty();
            MetaClass propertyMetaClass = metadataTools.getPropertyEnclosingMetaClass(propertyPath);
            String storeName = metadataTools.getStoreName(propertyMetaClass);
            if (metadataTools.isLob(metaProperty)
                    && !persistenceManagerClient.supportsLobSortingAndFiltering(storeName)) {
                component.setColumnSortable(columnId, false);
            }
        }
    }

    protected void setColumnAggregationDescriptionByType(Column<E> column, Object columnId) {
        String aggregationTypeLabel;

        switch (column.getAggregation().getType()) {
            case AVG:
                aggregationTypeLabel = "aggregation.avg";
                break;
            case COUNT:
                aggregationTypeLabel = "aggregation.count";
                break;
            case SUM:
                aggregationTypeLabel = "aggregation.sum";
                break;
            case MIN:
                aggregationTypeLabel = "aggregation.min";
                break;
            case MAX:
                aggregationTypeLabel = "aggregation.max";
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("AggregationType %s is not supported",
                                column.getAggregation().getType().toString()));
        }

        component.setAggregationDescription(columnId, messages.getMainMessage(aggregationTypeLabel));
    }

    @Override
    public void removeColumn(Table.Column column) {
        if (column == null) {
            return;
        }

        component.removeContainerProperty(column.getId());
        columns.remove(column.getId());
        columnsOrder.remove(column);

        // vaadin8 it seems that it is not required
        if (!(component.getContainerDataSource() instanceof com.vaadin.v7.data.Container.ItemSetChangeNotifier)) {
            component.refreshRowCache();
        }

        column.setOwner(null);
    }

    // vaadin8 rework this cache
    @SuppressWarnings("unchecked")
    @Override
    public Datasource getItemDatasource(Entity item) {
        if (fieldDatasources == null) {
            fieldDatasources = new WeakHashMap<>();
        }

        Datasource fieldDatasource = fieldDatasources.get(item);

        if (fieldDatasource == null) {
            fieldDatasource = DsBuilder.create()
                    .setAllowCommit(false)
                    .setMetaClass(getDatasource().getMetaClass())  // vaadin8 rework
                    .setRefreshMode(CollectionDatasource.RefreshMode.NEVER)
                    .setViewName("_local")
                    .buildDatasource();

            ((DatasourceImplementation) fieldDatasource).valid();

            fieldDatasource.setItem(item);
            fieldDatasources.put(item, fieldDatasource);
        }

        return fieldDatasource;
    }

    protected void addGeneratedColumnInternal(Object id, com.vaadin.v7.ui.Table.ColumnGenerator generator) {
        component.addGeneratedColumn(id, generator);
    }

    protected void removeGeneratedColumnInternal(Object id) {
        boolean wasEnabled = component.disableContentBufferRefreshing();

        com.vaadin.v7.ui.Table.ColumnGenerator columnGenerator = component.getColumnGenerator(id);
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
    public void addPrintable(String columnId, Printable<? super E, ?> printable) {
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
        return getPrintable(column.getStringId());
    }

    @Nullable
    @Override
    public Printable getPrintable(String columnId) {
        Printable printable = printables != null ? printables.get(columnId) : null;
        if (printable != null) {
            return printable;
        } else {
            com.vaadin.v7.ui.Table.ColumnGenerator vColumnGenerator = component.getColumnGenerator(getColumn(columnId).getId());
            if (vColumnGenerator instanceof CustomColumnGenerator) {
                ColumnGenerator columnGenerator = ((CustomColumnGenerator) vColumnGenerator).getColumnGenerator();
                if (columnGenerator instanceof Printable) {
                    return (Printable) columnGenerator;
                }
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

            EntityTableSource<E> entityTableSource = (EntityTableSource<E>) getTableSource();

            if (entityTableSource != null) {
                com.vaadin.v7.data.Container ds = component.getContainerDataSource();

                @SuppressWarnings("unchecked")
                Collection<MetaPropertyPath> propertyIds = (Collection<MetaPropertyPath>) ds.getContainerPropertyIds();

                if (editable) {
                    enableEditableColumns(entityTableSource, propertyIds);
                } else {
                    disableEditableColumns(entityTableSource, propertyIds);
                }
            }

            component.setEditable(editable);

            component.enableContentBufferRefreshing(true);
        }
    }

    protected void enableEditableColumns(EntityTableSource<E> entityTableSource,
                                         Collection<MetaPropertyPath> propertyIds) {
        MetaClass metaClass = entityTableSource.getEntityMetaClass();

        List<MetaPropertyPath> editableColumns = new ArrayList<>(propertyIds.size());
        for (MetaPropertyPath propertyId : propertyIds) {
            if (!security.isEntityAttrUpdatePermitted(metaClass, propertyId.toString())) {
                continue;
            }

            Column column = getColumn(propertyId.toString());
            if (BooleanUtils.isTrue(column.isEditable())) {
                com.vaadin.v7.ui.Table.ColumnGenerator generator = component.getColumnGenerator(column.getId());
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
    }

    protected void disableEditableColumns(@SuppressWarnings("unused") EntityTableSource<E> entityTableSource,
                                          Collection<MetaPropertyPath> propertyIds) {
        setEditableColumns(Collections.emptyList());

        Window window = ComponentsHelper.getWindowImplementation(this);
        boolean isLookup = window instanceof Window.Lookup;

        // restore generators for some type of attributes
        for (MetaPropertyPath propertyId : propertyIds) {
            Column column = columns.get(propertyId);
            if (column != null) {
                String isLink = column.getXmlDescriptor() == null ?
                        null : column.getXmlDescriptor().attributeValue("link");

                if (component.getColumnGenerator(column.getId()) == null) {
                    if (propertyId.getRange().isClass()) {
                        if (!isLookup && StringUtils.isNotEmpty(isLink)) {
                            setClickListener(propertyId.toString(), new LinkCellClickListener(this, applicationContext));
                        }
                    } else if (propertyId.getRange().isDatatype()) {
                        if (!isLookup && !StringUtils.isEmpty(isLink)) {
                            setClickListener(propertyId.toString(), new LinkCellClickListener(this, applicationContext));
                        } else {
                            if (column.getMaxTextLength() != null) {
                                addGeneratedColumnInternal(propertyId, new AbbreviatedColumnGenerator(column, dynamicAttributesTools));
                            }
                        }
                    }
                }
            }
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
        component.setSortEnabled(sortable && canBeSorted(getTableSource()));
    }

    @Override
    public boolean getColumnReorderingAllowed() {
        return component.isColumnReorderingAllowed();
    }

    @Override
    public void setColumnReorderingAllowed(boolean columnReorderingAllowed) {
        component.setColumnReorderingAllowed(columnReorderingAllowed);
    }

    @Override
    public boolean getColumnControlVisible() {
        return component.isColumnCollapsingAllowed();
    }

    @Override
    public void setColumnControlVisible(boolean columnCollapsingAllowed) {
        component.setColumnCollapsingAllowed(columnCollapsingAllowed);
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
    public void sort(String columnId, SortDirection direction) {
        Column column = getColumn(columnId);
        if (column == null) {
            throw new IllegalArgumentException("Unable to find column " + columnId);
        }

        if (isSortable()) {
            component.setSortAscending(direction == SortDirection.ASCENDING);
            component.setSortContainerPropertyId(column.getId());
            component.sort();
        }
    }

    @Nullable
    @Override
    public SortInfo getSortInfo() {
        Object sortContainerPropertyId = component.getSortContainerPropertyId();
        return sortContainerPropertyId != null
                ? new SortInfo(sortContainerPropertyId, component.isSortAscending())
                : null;
    }

    @Override
    public RowsCount getRowsCount() {
        return rowsCount;
    }

    @Override
    public void setRowsCount(RowsCount rowsCount) {
        if (this.rowsCount != null && topPanel != null) {
            topPanel.removeComponent(this.rowsCount.unwrap(com.vaadin.ui.Component.class));
        }
        this.rowsCount = rowsCount;
        if (rowsCount != null) {
            if (topPanel == null) {
                topPanel = createTopPanel();
                topPanel.setWidth("100%");
                componentComposition.addComponentAsFirst(topPanel);
            }
            com.vaadin.ui.Component rc = rowsCount.unwrap(com.vaadin.ui.Component.class);
            topPanel.addComponent(rc);
            topPanel.setExpandRatio(rc, 1);
            topPanel.setComponentAlignment(rc, com.vaadin.ui.Alignment.BOTTOM_RIGHT);

            if (rowsCount instanceof VisibilityChangeNotifier) {
                ((VisibilityChangeNotifier) rowsCount).addVisibilityChangeListener(event ->
                        updateCompositionStylesTopPanelVisible()
                );
            }
        }

        updateCompositionStylesTopPanelVisible();
    }

    // if buttons panel becomes hidden we need to set top panel height to 0
    protected void updateCompositionStylesTopPanelVisible() {
        if (topPanel != null) {
            boolean hasChildren = topPanel.getComponentCount() > 0;
            boolean anyChildVisible = false;
            for (Component childComponent : topPanel) {
                if (childComponent.isVisible()) {
                    anyChildVisible = true;
                    break;
                }
            }
            boolean topPanelVisible = hasChildren && anyChildVisible;

            if (!topPanelVisible) {
                componentComposition.removeStyleName(HAS_TOP_PANEL_STYLENAME);

                internalStyles.remove(HAS_TOP_PANEL_STYLENAME);
            } else {
                componentComposition.addStyleName(HAS_TOP_PANEL_STYLENAME);

                if (!internalStyles.contains(HAS_TOP_PANEL_STYLENAME)) {
                    internalStyles.add(HAS_TOP_PANEL_STYLENAME);
                }
            }
        }
    }

    @Override
    public boolean isMultiLineCells() {
        return component.isMultiLineCells();
    }

    @Override
    public void setMultiLineCells(boolean multiLineCells) {
        component.setMultiLineCells(multiLineCells);
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
    public AggregationStyle getAggregationStyle() {
        return AggregationStyle.valueOf(component.getAggregationStyle().name());
    }

    @Override
    public void setAggregationStyle(AggregationStyle aggregationStyle) {
        component.setAggregationStyle(CubaEnhancedTable.AggregationStyle.valueOf(aggregationStyle.name()));
    }

    @Override
    public boolean isShowTotalAggregation() {
        return component.isShowTotalAggregation();
    }

    @Override
    public void setShowTotalAggregation(boolean showAggregation) {
        component.setShowTotalAggregation(showAggregation);
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

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    protected void setTablePresentations(TablePresentations tablePresentations) {
        component.setPresentations(tablePresentations);
    }

    @SuppressWarnings("unchecked")
    protected void initComponent(T component) {
        component.setMultiSelect(false);
        component.setValidationVisible(false);
        component.setShowBufferedSourceException(false);

        component.setCustomCellValueFormatter(this::formatCellValue);

        component.addValueChangeListener(this::tableSelectionChanged);
        component.setSpecificVariablesHandler(this::handleSpecificVariables);
        component.setIconProvider(this::getItemIcon);
        component.setBeforePaintListener(this::beforeComponentPaint);

        component.setSortAscendingLabel(messages.getMainMessage("tableSort.ascending"));
        component.setSortResetLabel(messages.getMainMessage("tableSort.reset"));
        component.setSortDescendingLabel(messages.getMainMessage("tableSort.descending"));

        setClientCaching(component);

        int defaultRowHeaderWidth = 16;
        ThemeConstantsManager themeConstantsManager =
                applicationContext.getBean(ThemeConstantsManager.NAME, ThemeConstantsManager.class);
        ThemeConstants theme = themeConstantsManager.getConstants();
        if (theme != null) {
            defaultRowHeaderWidth = theme.getInt("cuba.web.Table.defaultRowHeaderWidth", 16);
        }

        component.setColumnWidth(ROW_HEADER_PROPERTY_ID, defaultRowHeaderWidth);

        contextMenuPopup.setParent(component);
        component.setContextMenuPopup(contextMenuPopup);

        shortcutsDelegate.setAllowEnterShortcut(false);

        component.addShortcutListener(
                new ShortcutListenerDelegate("tableEnter", KeyCode.ENTER, null)
                        .withHandler((sender, target) -> {
                            if (target == this.component) {
                                if (enterPressAction != null) {
                                    enterPressAction.actionPerform(this);
                                } else {
                                    handleClickAction();
                                }
                            }
                        }));

        component.addShortcutListener(
                new ShortcutListenerDelegate("tableSelectAll", KeyCode.A,
                        new int[] { com.vaadin.event.ShortcutAction.ModifierKey.CTRL })
                        .withHandler((sender, target) -> {
                            if (target == this.component) {
                                selectAll();
                            }
                        }));

        component.addItemClickListener(event -> {
            if (event.isDoubleClick() && event.getItem() != null) {
                handleClickAction();
            }
        });

        component.setSelectable(true);
        component.setTableFieldFactory(createFieldFactory());
        component.setColumnCollapsingAllowed(true);
        component.setColumnReorderingAllowed(true);

        setEditable(false);

        componentComposition = new TableComposition();
        componentComposition.setTable(component);
        componentComposition.setPrimaryStyleName("c-table-composition");
        componentComposition.addComponent(component);

        component.setCellStyleGenerator(createStyleGenerator());
        component.addColumnCollapseListener(this::handleColumnCollapsed);

        // force default sizes
        componentComposition.setHeightUndefined();
        componentComposition.setWidthUndefined();
    }

    protected void tableSelectionChanged(@SuppressWarnings("unused") Property.ValueChangeEvent event) {
        EntityTableSource<E> entityTableSource = (EntityTableSource<E>) getTableSource();

        if (entityTableSource == null
                || entityTableSource.getState() == BindingState.INACTIVE) {
            return;
        }

        Set<E> selected = getSelected();
        if (selected.isEmpty()) {
            entityTableSource.setSelectedItem(null);
        } else {
            // reset selection and select new item
            if (isMultiSelect()) {
                entityTableSource.setSelectedItem(null);
            }

            E newItem = selected.iterator().next();
            entityTableSource.setSelectedItem(newItem);
        }

        LookupSelectionChangeEvent selectionChangeEvent = new LookupSelectionChangeEvent(this);
        getEventRouter().fireEvent(LookupSelectionChangeListener.class,
                LookupSelectionChangeListener::lookupValueChanged, selectionChangeEvent);

        // todo implement selection change events
    }

    @SuppressWarnings("unchecked")
    protected String formatCellValue(@SuppressWarnings("unused") Object rowId, Object colId, Property<?> property) {
        TableSource<E> tableSource = getTableSource();
        if (tableSource == null
                || tableSource.getState() == BindingState.INACTIVE) {
            return null;
        }

        Object cellValue;
        if (ignoreUnfetchedAttributes
                && colId instanceof MetaPropertyPath) {
            E item = tableSource.getItem(rowId);
            cellValue = getValueExIgnoreUnfetched(item, ((MetaPropertyPath) colId).getPath());
        } else {
            cellValue = property.getValue();
        }

        if (colId instanceof MetaPropertyPath) {
            MetaPropertyPath propertyPath = (MetaPropertyPath) colId;

            Table.Column column = this.columns.get(propertyPath);
            if (column != null) {
                if (column.getFormatter() != null) {
                    return column.getFormatter().format(cellValue);
                } else if (column.getXmlDescriptor() != null) {
                    // vaadin8 move to Column
                    String captionProperty = column.getXmlDescriptor().attributeValue("captionProperty");
                    if (StringUtils.isNotEmpty(captionProperty)) {
                        E item = getTableSource().getItemNN(rowId);
                        Object captionValue = item.getValueEx(captionProperty);
                        return captionValue != null ? String.valueOf(captionValue) : null;
                    }
                }
            }

            return metadataTools.format(cellValue, propertyPath.getMetaProperty());
        }

        // fallback to Vaadin formatting

        UI ui = component.getUI();
        VaadinSession session = ui != null ? ui.getSession() : null;
        Converter converter = ConverterUtil.getConverter(String.class, property.getType(), session);
        if (converter != null) {
            return (String) converter.convertToPresentation(cellValue, String.class, locale);
        }

        return (null != cellValue) ? cellValue.toString() : "";
    }

    protected WebTableFieldFactory createFieldFactory() {
        return new WebTableFieldFactory(this, security, metadataTools);
    }

    protected void setClientCaching(T component) {
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
    protected String getGeneratedCellStyle(Object itemId, Object propertyId) { // vaadin8 return StringBuilder
        if (styleProviders == null) {
            return null;
        }
        TableSource<E> tableSource = getTableSource();
        if (tableSource == null) {
            return null;
        }

        E item = tableSource.getItem(itemId);

        String propertyStringId = propertyId == null ? null : propertyId.toString();

        String joinedStyle = styleProviders.stream()
                .map(sp -> sp.getStyleName(item, propertyStringId))
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));

        return Strings.emptyToNull(joinedStyle);
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
            protected com.haulmont.cuba.gui.components.Component getActionEventTarget() {
                return WebAbstractTable.this;
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
            Window window = ComponentsHelper.getWindowImplementation(this);
            if (window instanceof Window.Wrapper) {
                window = ((Window.Wrapper) window).getWrappedWindow();
            }

            if (!(window instanceof Window.Lookup)) {
                action.actionPerform(WebAbstractTable.this);
            } else {
                Window.Lookup lookup = (Window.Lookup) window;

                com.haulmont.cuba.gui.components.Component lookupComponent = lookup.getLookupComponent();
                if (lookupComponent != this
                        || WindowDelegate.LOOKUP_ITEM_CLICK_ACTION_ID.equals(action.getId())) {
                    action.actionPerform(WebAbstractTable.this);
                }
            }
        }
    }

    @Override
    public void setLookupSelectHandler(Runnable selectHandler) {
        Consumer<Action.ActionPerformedEvent> actionHandler = event -> selectHandler.run();

        setEnterPressAction(
                new BaseAction(WindowDelegate.LOOKUP_ENTER_PRESSED_ACTION_ID)
                        .withHandler(actionHandler)
        );

        setItemClickAction(
                new BaseAction(WindowDelegate.LOOKUP_ITEM_CLICK_ACTION_ID)
                        .withHandler(actionHandler)
        );
    }

    @Override
    public Collection getLookupSelectedItems() {
        return getSelected();
    }

    protected Action getEnterAction() {
        for (Action action : getActions()) {
            KeyCombination kc = action.getShortcutCombination();
            if (kc != null) {
                if ((kc.getModifiers() == null || kc.getModifiers().length == 0)
                        && kc.getKey() == KeyCombination.Key.ENTER) {
                    return action;
                }
            }
        }
        return null;
    }

    protected void createColumns(com.vaadin.v7.data.Container ds) {
        @SuppressWarnings("unchecked")
        Collection<MetaPropertyPath> properties = (Collection<MetaPropertyPath>) ds.getContainerPropertyIds();

        Window window = ComponentsHelper.getWindowImplementation(this);
        boolean isLookup = window instanceof Window.Lookup;

        for (MetaPropertyPath propertyPath : properties) {
            Table.Column column = columns.get(propertyPath);

            if (column != null && !(editable && column.isEditable())) {
                String isLink = column.getXmlDescriptor() == null ?
                                null : column.getXmlDescriptor().attributeValue("link");

                if (propertyPath.getRange().isClass()) {
                    if (!isLookup && StringUtils.isNotEmpty(isLink)) {
                        setClickListener(propertyPath.toString(), new LinkCellClickListener(this, applicationContext));
                    }
                } else if (propertyPath.getRange().isDatatype()) {
                    if (!isLookup && !StringUtils.isEmpty(isLink)) {
                        setClickListener(propertyPath.toString(), new LinkCellClickListener(this, applicationContext));
                    } else if (editable && BooleanUtils.isTrue(column.isCalculatable())) {
                        addGeneratedColumnInternal(propertyPath, new CalculatableColumnGenerator(this));
                    } else {
                        if (column.getMaxTextLength() != null) {
                            addGeneratedColumnInternal(propertyPath, new AbbreviatedColumnGenerator(column, dynamicAttributesTools));
                            setClickListener(propertyPath.toString(), new AbbreviatedCellClickListener(this, dynamicAttributesTools));
                        }
                    }
                } else if (!propertyPath.getRange().isEnum()) {
                    throw new UnsupportedOperationException();
                }
            }
        }
    }

    @Override
    public TableSource<E> getTableSource() {
        return this.dataBinding != null ? this.dataBinding.getTableSource() : null;
    }

    @Override
    public void setTableSource(TableSource<E> tableSource) {
        if (this.dataBinding != null) {
            this.dataBinding.unbind();
            this.dataBinding = null;

            // todo fieldDatasources

            this.component.setContainerDataSource(null);
        }

        if (tableSource != null) {
            // Table supports only EntityTableSource
            EntityTableSource<E> entityTableSource = (EntityTableSource<E>) tableSource;

            if (this.columns.isEmpty()) {
                setupAutowiredColumns(entityTableSource);
            }

            // bind new datasource
            this.dataBinding = createTableDataContainer(tableSource); // vaadin8 pass delegate there
            this.dataBinding.setProperties(getPropertyColumns(entityTableSource, columnsOrder));
            this.component.setContainerDataSource(this.dataBinding);

            setupColumnSettings(entityTableSource);

            createColumns(component.getContainerDataSource());

            for (Table.Column column : this.columnsOrder) {
                if (editable && column.getAggregation() != null
                        && (BooleanUtils.isTrue(column.isEditable()) || BooleanUtils.isTrue(column.isCalculatable()))) {
                    addAggregationCell(column);
                }
            }

            createStubsForGeneratedColumns();

            setVisibleColumns(getInitialVisibleColumnIds(entityTableSource));

            if (security.isSpecificPermitted(ShowInfoAction.ACTION_PERMISSION)) {
                if (getAction(ShowInfoAction.ACTION_ID) == null) {
                    addAction(new ShowInfoAction());
                }
            }

            if (rowsCount != null) {
                rowsCount.setRowsCountTarget(this);
            }

            if (!canBeSorted(tableSource)) {
                setSortable(false);
            }

            refreshActionsState();
        }
    }

    protected List<Object> getPropertyColumns(EntityTableSource<E> entityTableSource, List<Column<E>> columnsOrder) {
        return columnsOrder.stream()
                .filter(c -> {
                    MetaPropertyPath propertyPath = c.getBoundProperty();
                    return propertyPath != null
                            && security.isEntityAttrReadPermitted(entityTableSource.getEntityMetaClass(), propertyPath.toPathString());
                })
                .map(Column::getBoundProperty)
                .collect(Collectors.toList());
    }

    protected void setupColumnSettings(EntityTableSource<E> entityTableSource) {
        MetaClass metaClass = entityTableSource.getEntityMetaClass();

        List<MetaPropertyPath> editableColumns = Collections.emptyList();

        for (Map.Entry<Object, Table.Column<E>> entry : this.columns.entrySet()) {
            Object columnId = entry.getKey();
            Column<E> column = entry.getValue();

            String caption;
            if (column != null) {
                caption = getColumnCaption(columnId, column);
            } else {
                caption = StringUtils.capitalize(getColumnCaption(columnId));
            }

            setColumnHeader(columnId, caption);

            if (column != null) {
                if (column.isEditable() && (columnId instanceof MetaPropertyPath)) {
                    MetaPropertyPath propertyPath = ((MetaPropertyPath) columnId);
                    if (security.isEntityAttrUpdatePermitted(metaClass, propertyPath.toString())) {
                        if (editableColumns.isEmpty()) {
                            editableColumns = new ArrayList<>();
                        }
                        editableColumns.add(propertyPath);
                    }
                }

                if (column.isCollapsed() && component.isColumnCollapsingAllowed()) {
                    if (!(columnId instanceof MetaPropertyPath) ||
                            security.isEntityAttrReadPermitted(metaClass, columnId.toString())) {
                        component.setColumnCollapsed(column.getId(), true);
                    }
                }

                if (column.getAggregation() != null && isAggregatable()) {
                    checkAggregation(column.getAggregation());

                    component.addContainerPropertyAggregation(column.getId(),
                            WebWrapperUtils.convertAggregationType(column.getAggregation().getType()));
                }
            }
        }

        if (isEditable() && !editableColumns.isEmpty()) {
            setEditableColumns(editableColumns);
        }
    }

    protected void setupAutowiredColumns(EntityTableSource<E> entityTableSource) {
        Collection<MetaPropertyPath> paths = entityTableSource.getAutowiredProperties();

        for (MetaPropertyPath metaPropertyPath : paths) {
            MetaProperty property = metaPropertyPath.getMetaProperty();
            if (!property.getRange().getCardinality().isMany()
                    && !metadataTools.isSystem(property)) {
                Table.Column<E> column = new Table.Column<>(metaPropertyPath);

                String propertyName = property.getName();
                MetaClass propertyMetaClass = metadataTools.getPropertyEnclosingMetaClass(metaPropertyPath);

                column.setCaption(messageTools.getPropertyCaption(propertyMetaClass, propertyName));
                column.setType(metaPropertyPath.getRangeJavaClass());

                addColumn(column);
            }
        }
    }

    @Override
    public void tableSourceItemSetChanged(TableSource.ItemSetChangeEvent<E> event) {
        // replacement for collectionChangeSelectionListener
        // #PL-2035, reload selection from ds
        Set<Object> selectedItemIds = getSelectedItemIds();
        if (selectedItemIds == null) {
            selectedItemIds = Collections.emptySet();
        }

        Set<Object> newSelection = new HashSet<>();
        for (Object entityId : selectedItemIds) {
            //noinspection unchecked
            if (event.getSource().getItem(entityId) != null) {
                newSelection.add(entityId);
            }
        }

        if (event.getSource().getState() == BindingState.ACTIVE
                && event.getSource().getSelectedItem() != null) {
            newSelection.add(event.getSource().getSelectedItem().getId());
        }

        if (newSelection.isEmpty()) {
            setSelected((E) null);
        } else {
            setSelectedIds(newSelection);
        }

        refreshActionsState();
    }

    @Override
    public void tableSourcePropertyValueChanged(TableSource.ValueChangeEvent<E> event) {
        refreshActionsState();
    }

    @Override
    public void tableSourceSelectedItemChanged(TableSource.SelectedItemChangeEvent<E> event) {
        refreshActionsState();
    }

    @Override
    public void tableSourceStateChanged(TableSource.StateChangeEvent<E> event) {
        refreshActionsState();
    }

    protected TableDataContainer<E> createTableDataContainer(TableSource<E> tableSource) {
        if (tableSource instanceof TableSource.Sortable) {
            return new SortableDataContainer<>((TableSource.Sortable<E>) tableSource, this);
        }
        return new TableDataContainer<>(tableSource, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setDatasource(CollectionDatasource datasource) {
        if (datasource == null) {
            setTableSource(null);
        } else {
            TableSource<E> tableSource;
            if (datasource instanceof CollectionDatasource.Sortable) {
                tableSource = new SortableCollectionDatasourceTableAdapter((CollectionDatasource.Sortable) datasource);
            } else {
                tableSource = new CollectionDatasourceTableAdapter(datasource);
            }
            setTableSource(tableSource);
        }
    }

    protected boolean canBeSorted(@Nullable TableSource<E> tableSource) {
        //noinspection SimplifiableConditionalExpression
        return tableSource instanceof TableSource.Sortable;
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);

        AppUI ui = AppUI.getCurrent();
        if (id != null && ui != null) {
            componentComposition.setId(ui.getTestIdManager().getTestId(id + "_composition"));
        }
    }

    @Override
    public void setId(String id) {
        super.setId(id);

        AppUI ui = AppUI.getCurrent();
        if (id != null
                && ui != null
                && ui.isTestMode()) {
            componentComposition.setCubaId(id + "_composition");
        }
    }

    @Override
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }

        return getClass().getSimpleName();
    }

    protected String getColumnCaption(Object columnId) {
        if (columnId instanceof MetaPropertyPath)
            return ((MetaPropertyPath) columnId).getMetaProperty().getName();
        else
            return columnId.toString();
    }

    protected String getColumnCaption(Object columnId, Column column) {
        String caption = column.getCaption();

        if (caption != null) {
            return caption;
        }

        if (!(columnId instanceof MetaPropertyPath)) {
            return StringUtils.capitalize(getColumnCaption(columnId));
        }

        MetaPropertyPath mpp = (MetaPropertyPath) columnId;
        MetaProperty metaProperty = mpp.getMetaProperty();

        if (DynamicAttributesUtils.isDynamicAttribute(metaProperty)) {
            CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(metaProperty);
            if (LocaleHelper.isLocalizedValueDefined(categoryAttribute.getLocaleNames())) {
                return categoryAttribute.getLocaleName();
            }

            caption = StringUtils.capitalize(categoryAttribute.getName());
        } else {
            caption = StringUtils.capitalize(getColumnCaption(columnId));
        }

        return caption;
    }

    protected void createStubsForGeneratedColumns() {
        for (Column column : columnsOrder) {
            if (!(column.getId() instanceof MetaPropertyPath)
                    && component.getColumnGenerator(column.getId()) == null) {
                component.addGeneratedColumn(column.getId(), VOID_COLUMN_GENERATOR);
            }
        }
    }

    protected List<Object> getInitialVisibleColumnIds(EntityTableSource<E> entityTableSource) {
        List<Object> result = new ArrayList<>();

        MetaClass metaClass = entityTableSource.getEntityMetaClass();
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
                component.setRowHeaderMode(com.vaadin.v7.ui.Table.RowHeaderMode.HIDDEN);
                break;
            }
            case ICON: {
                component.setRowHeaderMode(com.vaadin.v7.ui.Table.RowHeaderMode.ICON_ONLY);
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
    public String getStyleName() {
        String styleName = super.getStyleName();
        for (String internalStyle : internalStyles) {
            styleName = styleName.replace(internalStyle, "");
        }
        return StringUtils.normalizeSpace(styleName);
    }

    @Override
    public void setStyleName(String name) {
        super.setStyleName(name);

        for (String internalStyle : internalStyles) {
            componentComposition.addStyleName(internalStyle);
        }
    }

    @Override
    public void setStyleProvider(@Nullable Table.StyleProvider<? super E> styleProvider) {
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
    public void addStyleProvider(StyleProvider<? super E> styleProvider) {
        if (this.styleProviders == null) {
            this.styleProviders = new LinkedList<>();
        }

        if (!this.styleProviders.contains(styleProvider)) {
            this.styleProviders.add(styleProvider);

            component.refreshCellStyles();
        }
    }

    @Override
    public void removeStyleProvider(StyleProvider<? super E> styleProvider) {
        if (this.styleProviders != null) {
            if (this.styleProviders.remove(styleProvider)) {
                component.refreshCellStyles();
            }
        }
    }

    @Override
    public void setIconProvider(IconProvider<? super E> iconProvider) {
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
        if (iconProvider == null
                || getTableSource() == null) {
            return null;
        }

        E item = getTableSource().getItem(itemId);
        if (item == null) {
            return null;
        }
        String resourceUrl = iconProvider.getItemIcon(item);
        return iconResolver.getIconResource(resourceUrl);
    }

    @Override
    public int getRowHeaderWidth() {
        return component.getColumnWidth(ROW_HEADER_PROPERTY_ID);
    }

    @Override
    public void setRowHeaderWidth(int width) {
        component.setColumnWidth(ROW_HEADER_PROPERTY_ID, width);
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
                ((TablePresentations) component.getPresentations()).updateTextSelection();
            }
        }

        Element columnsElem = element.element("columns");
        if (columnsElem != null) {
            boolean refreshWasEnabled = component.disableContentBufferRefreshing();

            Collection<String> modelIds = new ArrayList<>();
            for (Object column : component.getVisibleColumns()) {
                modelIds.add(String.valueOf(column));
            }

            Collection<String> loadedIds = new ArrayList<>();
            for (Element colElem : Dom4j.elements(columnsElem, "columns")) {
                loadedIds.add(colElem.attributeValue("id"));
            }

            ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);

            if (clientConfig.getLoadObsoleteSettingsForTable()
                    || CollectionUtils.isEqualCollection(modelIds, loadedIds)) {
                applyColumnSettings(element);
            }

            component.enableContentBufferRefreshing(refreshWasEnabled);
        }
    }

    protected void applyColumnSettings(Element element) {
        Element columnsElem = element.element("columns");

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
                @SuppressWarnings("unchecked")
                EntityTableSource<E> entityTableSource = (EntityTableSource) getTableSource();

                MetaPropertyPath sortProperty = entityTableSource.getEntityMetaClass().getPropertyPath(sortProp);
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
    public Action getEnterPressAction() {
        return enterPressAction;
    }

    @Override
    public void setEnterPressAction(Action action) {
        enterPressAction = action;
    }

    @Override
    public Action getItemClickAction() {
        return itemClickAction;
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
    public String getCaption() {
        return getComposition().getCaption();
    }

    @Override
    public void setCaption(String caption) {
        getComposition().setCaption(caption);
    }

    @Override
    public ButtonsPanel getButtonsPanel() {
        return buttonsPanel;
    }

    @Override
    public void setButtonsPanel(ButtonsPanel panel) {
        if (buttonsPanel != null && topPanel != null) {
            topPanel.removeComponent(buttonsPanel.unwrap(Component.class));
            buttonsPanel.setParent(null);
        }
        buttonsPanel = panel;
        if (panel != null) {
            if (panel.getParent() != null
                    && panel.getParent() != this) {
                throw new IllegalStateException("Component already has parent");
            }

            if (topPanel == null) {
                topPanel = createTopPanel();
                topPanel.setWidth(100, Sizeable.Unit.PERCENTAGE);
                componentComposition.addComponentAsFirst(topPanel);
            }
            topPanel.addComponent(panel.unwrap(Component.class));
            if (panel instanceof VisibilityChangeNotifier) {
                ((VisibilityChangeNotifier) panel).addVisibilityChangeListener(event ->
                        updateCompositionStylesTopPanelVisible()
                );
            }
            panel.setParent(this);
        }

        updateCompositionStylesTopPanelVisible();
    }

    protected HorizontalLayout createTopPanel() {
        HorizontalLayout topPanel = new HorizontalLayout();
        topPanel.setMargin(false);
        topPanel.setSpacing(false);
        topPanel.setStyleName("c-table-top");
        return topPanel;
    }

    @Override
    public void addGeneratedColumn(String columnId, ColumnGenerator<? super E> generator) {
        checkNotNullArgument(columnId, "columnId is null");
        checkNotNullArgument(generator, "generator is null for column id '%s'", columnId);

        EntityTableSource<E> entityTableSource = (EntityTableSource<E>) getTableSource();

        MetaPropertyPath targetCol = entityTableSource != null ?
                entityTableSource.getEntityMetaClass().getPropertyPath(columnId) : null;

        Object generatedColumnId = targetCol != null ? targetCol : columnId;

        Column column = getColumn(columnId);
        Column associatedRuntimeColumn = null;
        if (column == null) {
            Column<E> newColumn = new Column<>(generatedColumnId);

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
                    public Object generateCell(com.vaadin.v7.ui.Table source, Object itemId, Object columnId) {
                        EntityTableSource<E> entityTableSource = (EntityTableSource<E>) getTableSource();
                        if (entityTableSource == null) {
                            return null;
                        }

                        E entity = entityTableSource.getItem(itemId);

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

                        com.vaadin.ui.Component vComponent = component.unwrapComposition(Component.class);

                        // vaadin8 rework
                        // wrap field for show required asterisk
                        if ((vComponent instanceof com.vaadin.v7.ui.Field)
                                && (((com.vaadin.v7.ui.Field) vComponent).isRequired())) {
                            VerticalLayout layout = new VerticalLayout(); // vaadin8 replace with CssLayout
                            layout.setMargin(false);
                            layout.setSpacing(false);
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
    public void addGeneratedColumn(String columnId, ColumnGenerator<? super E> generator,
                                   Class<? extends com.haulmont.cuba.gui.components.Component> componentClass) {
        // web ui doesn't make any improvements with componentClass known
        addGeneratedColumn(columnId, generator);
    }

    @Override
    public void removeGeneratedColumn(String columnId) {
        EntityTableSource<E> entityTableSource = (EntityTableSource<E>) getTableSource();

        MetaPropertyPath targetCol = entityTableSource != null ?
                entityTableSource.getEntityMetaClass().getPropertyPath(columnId) : null;
        removeGeneratedColumnInternal(targetCol == null ? columnId : targetCol);
    }

    @Override
    public void addAggregationProperty(String columnId, AggregationInfo.Type type) {
        addAggregationProperty(getColumn(columnId), type);
    }

    @Override
    public void addAggregationProperty(Column column, AggregationInfo.Type type) {
        checkAggregation(column.getAggregation());

        component.addContainerPropertyAggregation(column.getId(), WebWrapperUtils.convertAggregationType(type));

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

        if (!Objects.equals(column.getCaption(), caption)) {
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

        if (!Objects.equals(column.getDescription(), description)) {
            column.setDescription(description);
        }
        component.setColumnDescription(column.getId(), description);
    }

    @Override
    public boolean isTextSelectionEnabled() {
        return component.isTextSelectionEnabled();
    }

    @Override
    public void setTextSelectionEnabled(boolean value) {
        component.setTextSelectionEnabled(value);
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
        component.setColumnAlignment(column.getId(), WebWrapperUtils.convertColumnAlignment(alignment));
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

    @Deprecated
    @Override
    public void refresh() {
        TableSource<E> tableSource = getTableSource();
        if (tableSource instanceof CollectionDatasourceTableAdapter) {
            ((CollectionDatasourceTableAdapter) tableSource).getDatasource().refresh();
        }
    }

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

    protected void checkAggregation(AggregationInfo aggregationInfo) {
        AggregationInfo.Type aggregationType = aggregationInfo.getType();

        if (aggregationType == AggregationInfo.Type.CUSTOM) {
            return;
        }

        MetaPropertyPath propertyPath = aggregationInfo.getPropertyPath();
        Class<?> javaType = propertyPath.getMetaProperty().getJavaType();
        Aggregation<?> aggregation = Aggregations.get(javaType);

        if (aggregation != null && aggregation.getSupportedAggregationTypes().contains(aggregationType)) {
            return;
        }

        String msg = String.format("Unable to aggregate column \"%s\" with data type %s with default aggregation strategy: %s",
                propertyPath, propertyPath.getRange(), aggregationInfo.getType());

        throw new IllegalArgumentException(msg);
    }

    protected Map<Object, Object> __aggregate(AggregationContainer container, AggregationContainer.Context context) {
        List<AggregationInfo> aggregationInfos = new ArrayList<>();
        for (Object propertyId : container.getAggregationPropertyIds()) {
            Table.Column column = columns.get(propertyId);
            AggregationInfo aggregation = column.getAggregation();
            if (aggregation != null) {
                checkAggregation(aggregation);
                aggregationInfos.add(aggregation);
            }
        }

        // vaadin8
        @SuppressWarnings("unchecked")
        Map<AggregationInfo, Object> results = ((CollectionDatasource.Aggregatable) getDatasource()).aggregate(
                aggregationInfos.toArray(new AggregationInfo[0]),
                context.getItemIds()
        );
        Map<Object, Object> resultsByColumns = new LinkedHashMap<>();
        for (Object propertyId : container.getAggregationPropertyIds()) {
            Table.Column column = columns.get(propertyId);
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
        for (Map.Entry<Object, Object> entry : results.entrySet()) {
            Table.Column column = columns.get(entry.getKey());
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
                    Datatype datatype = datatypeRegistry.get(value.getClass());
                    if (datatype != null) {
                        cellText = datatype.format(value, this.locale);
                    } else {
                        cellText = value.toString();
                    }
                }
            }
        }
        return cellText;
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

    protected boolean handleSpecificVariables(Map<String, Object> variables) {
        boolean needReload = false;

        if (isUsePresentations() && presentations != null) {
            Presentations p = getPresentations();

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
        Object[] componentVisibleColumns = component.getVisibleColumns();
        if (componentVisibleColumns == null)
            return Collections.emptyList();

        List<Table.Column> visibleColumns = new ArrayList<>(componentVisibleColumns.length);
        for (Object key : componentVisibleColumns) {
            if (!component.isColumnCollapsed(key)) {
                Column column = columns.get(key);
                if (column != null) {
                    visibleColumns.add(column);
                }
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

            component.addColumnCollapseListener((com.vaadin.v7.ui.Table.ColumnCollapseListener) event -> {
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
    public void setClickListener(String columnId, CellClickListener<? super E> clickListener) {
        checkNotNullArgument(getColumn(columnId), String.format("column with id '%s' not found", columnId));

        component.setClickListener(getColumn(columnId).getId(), (itemId, columnId1) -> {
//            TableItemWrapper wrapper = (TableItemWrapper) component.getItem(itemId);
//            Object itemId2 = wrapper.getItemId();
            TableSource<E> tableSource = getTableSource();
            if (tableSource == null) {
                return;
            }

            E item = tableSource.getItem(itemId);
            clickListener.onClick(item, columnId1.toString());
        });
    }

    @Override
    public void removeClickListener(String columnId) {
        component.removeClickListener(getColumn(columnId).getId());
    }

    @Override
    public void showCustomPopup(com.haulmont.cuba.gui.components.Component popupComponent) {
        Component vComponent = popupComponent.unwrap(com.vaadin.ui.Component.class);
        component.showCustomPopup(vComponent);
        component.setCustomPopupAutoClose(false);
    }

    @Override
    public void showCustomPopupActions(List<Action> actions) {
        VerticalLayout customContextMenu = new VerticalLayout();
        customContextMenu.setMargin(false);
        customContextMenu.setSpacing(false);
        customContextMenu.setWidthUndefined();
        customContextMenu.setStyleName("c-cm-container");

        for (Action action : actions) {
            ContextMenuButton contextMenuButton = createContextMenuButton();
            contextMenuButton.setStyleName("c-cm-button");
            contextMenuButton.setAction(action);

            Component vButton = contextMenuButton.unwrap(com.vaadin.ui.Component.class);
            customContextMenu.addComponent(vButton);
        }

        if (customContextMenu.getComponentCount() > 0) {
            component.showCustomPopup(customContextMenu);
            component.setCustomPopupAutoClose(true);
        }
    }

    @Override
    public boolean isColumnHeaderVisible() {
        return component.getColumnHeaderMode() != ColumnHeaderMode.HIDDEN;
    }

    @Override
    public void setColumnHeaderVisible(boolean visible) {
        component.setColumnHeaderMode(visible ?
                ColumnHeaderMode.EXPLICIT_DEFAULTS_ID :
                ColumnHeaderMode.HIDDEN);
    }

    @Override
    public boolean isShowSelection() {
        return component.isSelectable();
    }

    @Override
    public void setShowSelection(boolean showSelection) {
        component.setSelectable(showSelection);
    }

    protected String generateCellStyle(Object itemId, Object propertyId) {
        String style = null;
        if (propertyId != null && itemId != null
                && !component.isColumnEditable(propertyId)
                && (component.getColumnGenerator(propertyId) == null
                || component.getColumnGenerator(propertyId) instanceof AbbreviatedColumnGenerator)) {

            MetaPropertyPath propertyPath;
            if (propertyId instanceof MetaPropertyPath) {
                propertyPath = (MetaPropertyPath) propertyId;
            } else {
                EntityTableSource<E> entityTableSource = (EntityTableSource<E>) getTableSource();

                propertyPath = entityTableSource != null ?
                        entityTableSource.getEntityMetaClass().getPropertyPath(propertyId.toString()) : null;
            }

            if (propertyPath != null) {
                style = generateDefaultCellStyle(itemId, propertyId, propertyPath);
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

    @SuppressWarnings("unchecked")
    protected String generateDefaultCellStyle(Object itemId, Object propertyId, MetaPropertyPath propertyPath) {
        String style = null;

        String stringPropertyId = propertyId.toString();

        Column column = getColumn(stringPropertyId);
        if (column != null) {
            String isLink = column.getXmlDescriptor() == null ?
                    null : column.getXmlDescriptor().attributeValue("link");

            if (propertyPath.getRange().isClass()) {
                if (StringUtils.isNotEmpty(isLink) && Boolean.valueOf(isLink)) {
                    style = "c-table-cell-link";
                }
            } else if (propertyPath.getRange().isDatatype()) {
                if (StringUtils.isNotEmpty(isLink) && Boolean.valueOf(isLink)) {
                    style = "c-table-cell-link";
                } else if (column.getMaxTextLength() != null) {
                    EntityTableSource<E> entityTableSource = (EntityTableSource<E>) getTableSource();

                    if (entityTableSource == null) {
                        throw new IllegalStateException("TableSource is not set");
                    }

                    E item = entityTableSource.getItemNN(itemId);

                    Object value = item.getValueEx(stringPropertyId);
                    String stringValue;
                    if (value instanceof String) {
                        stringValue = item.getValueEx(stringPropertyId);
                    } else {
                        if (DynamicAttributesUtils.isDynamicAttribute(propertyPath.getMetaProperty())) {
                            stringValue = dynamicAttributesTools.getDynamicAttributeValueAsString(propertyPath.getMetaProperty(), value);
                        } else {
                            stringValue = value == null ? null : value.toString();
                        }
                    }
                    if (column.getMaxTextLength() != null) {
                        boolean isMultiLineCell = StringUtils.contains(stringValue, "\n");
                        if ((stringValue != null && stringValue.length() > column.getMaxTextLength() + MAX_TEXT_LENGTH_GAP)
                                || isMultiLineCell) {
                            style = "c-table-cell-textcut";
                        } else {
                            // use special marker stylename
                            style = "c-table-clickable-text";
                        }
                    }
                }
            }
        }

        if (propertyPath.getRangeJavaClass() == Boolean.class
                && dataBinding != null) {
            Entity item = dataBinding.getTableSource().getItem(itemId);
            if (item != null) {
                Boolean value = item.getValueEx(stringPropertyId);
                if (BooleanUtils.isTrue(value)) {
                    style = "boolean-cell boolean-cell-true";
                } else {
                    style = "boolean-cell boolean-cell-false";
                }
            }
        }
        return style;
    }

    @Override
    public void requestFocus(E item, String columnId) {
        Preconditions.checkNotNullArgument(item);
        Preconditions.checkNotNullArgument(columnId);

        component.requestFocus(item.getId(), getColumn(columnId).getId());
    }

    @Override
    public void scrollTo(E item) {
        Preconditions.checkNotNullArgument(item);
        if (!component.getItemIds().contains(item.getId())) {
            throw new IllegalArgumentException("Unable to find item in Table");
        }

        component.setCurrentPageFirstItemId(item.getId());
    }

    @Override
    public void addLookupValueChangeListener(LookupSelectionChangeListener listener) {
        getEventRouter().addListener(LookupSelectionChangeListener.class, listener);
    }

    @Override
    public void removeLookupValueChangeListener(LookupSelectionChangeListener listener) {
        getEventRouter().removeListener(LookupSelectionChangeListener.class, listener);
    }

    protected void handleColumnCollapsed(com.vaadin.v7.ui.Table.ColumnCollapseEvent event) {
        Object propertyId = event.getPropertyId();
        boolean columnCollapsed = component.isColumnCollapsed(propertyId);

        columns.get(propertyId).setCollapsed(columnCollapsed);
    }

    @SuppressWarnings("unchecked")
    protected void beforeComponentPaint() {
        com.vaadin.v7.ui.Table.CellStyleGenerator generator = component.getCellStyleGenerator();
        if (generator instanceof WebAbstractTable.StyleGeneratorAdapter) {
            ((StyleGeneratorAdapter) generator).resetExceptionHandledFlag();
        }
    }

    protected class StyleGeneratorAdapter implements com.vaadin.v7.ui.Table.CellStyleGenerator {
        protected boolean exceptionHandled = false;

        @SuppressWarnings({"unchecked"})
        @Override
        public String getStyle(com.vaadin.v7.ui.Table source, Object itemId, Object propertyId) {
            if (exceptionHandled) {
                return null;
            }

            try {
                return generateCellStyle(itemId, propertyId);
            } catch (Exception e) {
                LoggerFactory.getLogger(WebAbstractTable.class).error("Uncautch exception in Table StyleProvider", e);
                this.exceptionHandled = true;
                return null;
            }
        }

        public void resetExceptionHandledFlag() {
            this.exceptionHandled = false;
        }
    }

    protected Object getValueExIgnoreUnfetched(Instance instance, String[] properties) {
        Object currentValue = null;
        Instance currentInstance = instance;
        for (String property : properties) {
            if (currentInstance == null) {
                break;
            }

            if (!PersistenceHelper.isLoaded(currentInstance, property)) {
                LoggerFactory.getLogger(WebAbstractTable.class)
                        .warn("Ignored unfetched attribute {} of instance {} in Table cell",
                                property, currentInstance);
                return null;
            }

            currentValue = currentInstance.getValue(property);
            if (currentValue == null) {
                break;
            }

            currentInstance = currentValue instanceof Instance ? (Instance) currentValue : null;
        }
        return currentValue;
    }
}