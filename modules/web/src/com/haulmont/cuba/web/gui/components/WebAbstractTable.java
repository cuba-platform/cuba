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
import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.chile.core.datatypes.ValueConversionException;
import com.haulmont.chile.core.model.*;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.client.sys.PersistenceManagerClient;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesTools;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.LocaleHelper;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.LookupComponent.LookupSelectionChangeNotifier;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.columnmanager.ColumnManager;
import com.haulmont.cuba.gui.components.data.AggregatableTableItems;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.HasValueSource;
import com.haulmont.cuba.gui.components.data.TableItems;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.components.data.meta.DatasourceDataUnit;
import com.haulmont.cuba.gui.components.data.meta.EntityTableItems;
import com.haulmont.cuba.gui.components.data.table.DatasourceTableItems;
import com.haulmont.cuba.gui.components.sys.ShowInfoAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.aggregation.Aggregation;
import com.haulmont.cuba.gui.data.aggregation.Aggregations;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.DataComponents;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.gui.presentations.PresentationsImpl;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.InstallTargetHandler;
import com.haulmont.cuba.gui.screen.ScreenContext;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.gui.sys.UiTestIds;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.security.entity.Presentation;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.components.presentations.TablePresentations;
import com.haulmont.cuba.web.gui.components.table.*;
import com.haulmont.cuba.web.gui.components.util.ShortcutListenerDelegate;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CubaButton;
import com.haulmont.cuba.web.widgets.CubaEnhancedTable;
import com.haulmont.cuba.web.widgets.CubaEnhancedTable.AggregationInputValueChangeContext;
import com.haulmont.cuba.web.widgets.CubaUI;
import com.haulmont.cuba.web.widgets.compatibility.CubaValueChangeEvent;
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

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

@SuppressWarnings("deprecation")
public abstract class WebAbstractTable<T extends com.vaadin.v7.ui.Table & CubaEnhancedTable, E extends Entity>
        extends WebAbstractActionsHolderComponent<T>
        implements Table<E>, TableItemsEventsDelegate<E>, LookupSelectionChangeNotifier<E>,
        HasInnerComponents, InstallTargetHandler, InitializingBean, ColumnManager {

    public static final int MAX_TEXT_LENGTH_GAP = 10;

    public static final String BOOLEAN_CELL_STYLE_TRUE = "boolean-cell boolean-cell-true";
    public static final String BOOLEAN_CELL_STYLE_FALSE = "boolean-cell boolean-cell-false";

    protected static final com.vaadin.v7.ui.Table.ColumnGenerator VOID_COLUMN_GENERATOR =
            (source, itemId, columnId) -> null;

    protected static final String HAS_TOP_PANEL_STYLENAME = "has-top-panel";
    protected static final String CUSTOM_STYLE_NAME_PREFIX = "cs ";

    protected static final String EDIT_ACTION_ID = "edit";
    protected static final String VIEW_ACTION_ID = "view";

    // Vaadin considers null as row header property id
    protected static final Object ROW_HEADER_PROPERTY_ID = null;

    // Beans

    protected Configuration configuration;
    protected IconResolver iconResolver;
    protected MetadataTools metadataTools;
    protected Security security;
    protected Messages messages;
    protected MessageTools messageTools;
    protected PersistenceManagerClient persistenceManagerClient;
    protected DatatypeRegistry datatypeRegistry;
    protected DynamicAttributesTools dynamicAttributesTools;
    protected DataComponents dataComponents;
    protected ViewRepository viewRepository;

    protected Locale locale;

    // Style names used by table itself
    protected List<String> internalStyles = new ArrayList<>(2);

    protected Map<Object, Table.Column<E>> columns = new HashMap<>();
    protected List<Table.Column<E>> columnsOrder = new ArrayList<>();

    protected boolean editable;
    protected Action itemClickAction;
    protected Action enterPressAction;

    protected Function<? super E, String> iconProvider;
    @Nullable
    protected List<Table.StyleProvider> styleProviders; // lazily initialized List
    @Nullable
    protected Map<Table.Column, String> requiredColumns; // lazily initialized Map
    @Nullable
    protected Map<Entity, Object> fieldDatasources; // lazily initialized WeakHashMap;

    protected TableComposition componentComposition;

    protected HorizontalLayout topPanel;

    protected ButtonsPanel buttonsPanel;

    protected RowsCount rowsCount;

    protected Map<Table.Column, String> aggregationCells = null;

    protected boolean usePresentations;
    protected Presentations presentations;
    protected Document defaultSettings;

    protected com.vaadin.v7.ui.Table.ColumnCollapseListener columnCollapseListener;

    protected AggregationDistributionProvider<E> distributionProvider;

    // Map column id to Printable representation
    // todo this functionality should be moved to Excel action
    protected Map<String, Printable> printables; // lazily initialized Map

    protected boolean settingsEnabled = true;

    protected TableDataContainer<E> dataBinding;

    protected boolean ignoreUnfetchedAttributes;

    protected com.vaadin.v7.ui.Table.ColumnGenerator VALUE_PROVIDER_GENERATOR =
            (source, itemId, columnId) -> formatCellValue(itemId, columnId, null);

    protected WebAbstractTable() {
    }

    @Override
    public void afterPropertiesSet() {
        initComponent(component);
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

    @Inject
    public void setDataComponents(DataComponents dataComponents) {
        this.dataComponents = dataComponents;
    }

    @Inject
    public void setViewRepository(ViewRepository viewRepository) {
        this.viewRepository = viewRepository;
    }

    @Override
    public Collection<com.haulmont.cuba.gui.components.Component> getInnerComponents() {
        if (buttonsPanel != null) {
            return Collections.singletonList(buttonsPanel);
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
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

    @Override
    public E getSingleSelected() {
        TableItems<E> tableItems = getItems();
        if (tableItems == null
                || tableItems.getState() == BindingState.INACTIVE) {
            return null;
        }

        Set selected = getSelectedItemIds();
        return selected == null || selected.isEmpty() ?
                null : tableItems.getItem(selected.iterator().next());
    }

    @Override
    public Set<E> getSelected() {
        TableItems<E> tableItems = getItems();
        if (tableItems == null
                || tableItems.getState() == BindingState.INACTIVE) {
            return Collections.emptySet();
        }

        Set<Object> itemIds = getSelectedItemIds();

        if (itemIds != null) {
            if (itemIds.size() == 1) {
                E item = tableItems.getItem(itemIds.iterator().next());
                return Collections.singleton(item);
            }

            Set<E> res = new LinkedHashSet<>();
            for (Object id : itemIds) {
                E item = tableItems.getItem(id);
                if (item != null) {
                    res.add(item);
                }
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

    @Override
    public void setSelected(Collection<E> items) {
        TableItems<E> tableItems = getItems();
        if (tableItems == null
                || tableItems.getState() == BindingState.INACTIVE) {
            throw new IllegalStateException("TableItems is not active");
        }

        if (items.isEmpty()) {
            setSelectedIds(Collections.emptyList());
        } else if (items.size() == 1) {
            E item = items.iterator().next();
            if (tableItems.getItem(item.getId()) == null) {
                throw new IllegalArgumentException("Datasource doesn't contain item to select: " + item);
            }
            setSelectedIds(Collections.singletonList(item.getId()));
        } else {
            Set<Object> itemIds = new LinkedHashSet<>();
            for (Entity item : items) {
                if (tableItems.getItem(item.getId()) == null) {
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
        addColumnInternal(column, columnsOrder.size());
    }

    @Override
    public void addColumn(Column<E> column, int index) {
        addColumnInternal(column, index);

        // Update column order only if we add a column to an arbitrary position.
        component.setVisibleColumns(columnsOrder.stream()
                .map(Table.Column::getId)
                .toArray());
    }

    protected void addColumnInternal(Column<E> column, int index) {
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
        columnsOrder.add(index, column);
        if (column.getWidth() != null) {
            component.setColumnWidth(columnId, column.getWidth());
        }
        if (column.getAlignment() != null) {
            component.setColumnAlignment(columnId,
                    WebWrapperUtils.convertColumnAlignment(column.getAlignment()));
        }

        setColumnHeader(columnId, getColumnCaption(columnId, column));

        component.setColumnCaptionAsHtml(columnId, column.getCaptionAsHtml());

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

        if (column.isAggregationEditable()) {
            component.addAggregationEditableColumn(columnId);
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

        column.setOwner(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Datasource getItemDatasource(Entity item) {
        if (fieldDatasources == null) {
            fieldDatasources = new WeakHashMap<>();
        }

        Object fieldDatasource = fieldDatasources.get(item);
        if (fieldDatasource instanceof Datasource) {
            return (Datasource) fieldDatasource;
        }

        EntityTableItems containerTableItems = (EntityTableItems) getItems();
        Datasource datasource = DsBuilder.create()
                .setAllowCommit(false)
                .setMetaClass(containerTableItems.getEntityMetaClass())
                .setRefreshMode(CollectionDatasource.RefreshMode.NEVER)
                .setViewName(View.LOCAL)
                .buildDatasource();

        ((DatasourceImplementation) datasource).valid();

        datasource.setItem(item);
        fieldDatasources.put(item, datasource);

        return datasource;
    }

    @SuppressWarnings("unchecked")
    @Override
    public InstanceContainer<E> getInstanceContainer(E item) {
        if (fieldDatasources == null) {
            fieldDatasources = new WeakHashMap<>();
        }

        Object fieldDatasource = fieldDatasources.get(item);
        if (fieldDatasource instanceof InstanceContainer) {
            return (InstanceContainer<E>) fieldDatasource;
        }

        EntityTableItems containerTableItems = (EntityTableItems) getItems();
        if (containerTableItems == null) {
            throw new IllegalStateException("Table is not bound to items");
        }

        InstanceContainer<E> instanceContainer = dataComponents.createInstanceContainer(
                containerTableItems.getEntityMetaClass().getJavaClass());
        View view = viewRepository.getView(containerTableItems.getEntityMetaClass(), View.LOCAL);
        instanceContainer.setView(view);
        instanceContainer.setItem(item);

        fieldDatasources.put(item, instanceContainer);

        return instanceContainer;
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

            EntityTableItems<E> entityTableSource = (EntityTableItems<E>) getItems();

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

    protected void enableEditableColumns(EntityTableItems<E> entityTableSource,
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

    @SuppressWarnings("unchecked")
    protected void disableEditableColumns(@SuppressWarnings("unused") EntityTableItems<E> entityTableSource,
                                          Collection<MetaPropertyPath> propertyIds) {
        setEditableColumns(Collections.emptyList());

        // restore generators for some type of attributes
        for (MetaPropertyPath propertyId : propertyIds) {
            Column column = columns.get(propertyId);
            if (column != null) {
                String isLink = column.getXmlDescriptor() == null ?
                        null : column.getXmlDescriptor().attributeValue("link");

                if (component.getColumnGenerator(column.getId()) == null) {
                    if (propertyId.getRange().isClass()) {
                        if (StringUtils.isNotEmpty(isLink)) {
                            setClickListener(propertyId.toString(), new LinkCellClickListener(this, beanLocator));
                        }
                    } else if (propertyId.getRange().isDatatype()) {
                        if (StringUtils.isNotEmpty(isLink)) {
                            setClickListener(propertyId.toString(), new LinkCellClickListener(this, beanLocator));
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
        component.setSortEnabled(sortable && canBeSorted(getItems()));
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
        Collection<?> itemIds = WebAbstractTable.this.getItems().getItemIds();
        return component.aggregate(new AggregationContainer.Context(itemIds));
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

        int defaultRowHeaderWidth = 16;
        ThemeConstantsManager themeConstantsManager =
                beanLocator.get(ThemeConstantsManager.NAME, ThemeConstantsManager.class);
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
                            T tableImpl = WebAbstractTable.this.component;

                            CubaUI ui = (CubaUI) tableImpl.getUI();
                            if (!ui.isAccessibleForUser(tableImpl)) {
                                LoggerFactory.getLogger(WebAbstractTable.class)
                                        .debug("Ignore click attempt because Table is inaccessible for user");
                                return;
                            }

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
                T tableImpl = WebAbstractTable.this.component;

                CubaUI ui = (CubaUI) tableImpl.getUI();
                if (!ui.isAccessibleForUser(tableImpl)) {
                    LoggerFactory.getLogger(WebAbstractTable.class)
                            .debug("Ignore click attempt because Table is inaccessible for user");
                    return;
                }

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

        setClientCaching();
    }

    protected void tableSelectionChanged(@SuppressWarnings("unused") Property.ValueChangeEvent event) {
        EntityTableItems<E> entityTableSource = (EntityTableItems<E>) getItems();

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
            E dsItem = entityTableSource.getSelectedItem();
            entityTableSource.setSelectedItem(newItem);

            if (Objects.equals(dsItem, newItem)) {
                // in this case item change event will not be generated
                refreshActionsState();
            }
        }

        fireSelectionEvent(event);

        LookupSelectionChangeEvent<E> selectionChangeEvent = new LookupSelectionChangeEvent<>(this);
        publish(LookupSelectionChangeEvent.class, selectionChangeEvent);
    }

    protected void fireSelectionEvent(Property.ValueChangeEvent e) {
        boolean userOriginated = e instanceof CubaValueChangeEvent
                && ((CubaValueChangeEvent) e).isUserOriginated();

        SelectionEvent<E> event =
                new SelectionEvent<>(this, getSelected(), userOriginated);
        publish(SelectionEvent.class, event);
    }

    protected String formatCellValue(Object rowId, Object colId, @Nullable Property<?> property) {
        TableItems<E> tableItems = getItems();
        if (tableItems == null
                || tableItems.getState() == BindingState.INACTIVE) {
            return null;
        }

        Column<E> column = columns.get(colId);
        if (column != null && column.getValueProvider() != null) {
            E item = tableItems.getItem(rowId);
            Object generatedValue = column.getValueProvider().apply(item);
            Function<Object, String> formatter = column.getFormatter();

            if (formatter != null) {
                return column.getFormatter().apply(generatedValue);
            }

            return metadataTools.format(generatedValue);
        }

        Object cellValue;
        if (ignoreUnfetchedAttributes
                && colId instanceof MetaPropertyPath) {
            E item = tableItems.getItem(rowId);
            cellValue = getValueExIgnoreUnfetched(item, ((MetaPropertyPath) colId).getPath());
        } else if (property != null) {
            cellValue = property.getValue();
        } else {
            cellValue = null;
        }

        if (colId instanceof MetaPropertyPath) {
            MetaPropertyPath propertyPath = (MetaPropertyPath) colId;

            if (column != null) {
                if (column.getFormatter() != null) {
                    return column.getFormatter().apply(cellValue);
                } else if (column.getXmlDescriptor() != null) {
                    // vaadin8 move to Column
                    String captionProperty = column.getXmlDescriptor().attributeValue("captionProperty");
                    if (StringUtils.isNotEmpty(captionProperty)) {
                        E item = getItems().getItemNN(rowId);
                        Object captionValue = item.getValueEx(captionProperty);
                        return captionValue != null ? String.valueOf(captionValue) : null;
                    }
                }
            }

            return metadataTools.format(cellValue, propertyPath.getMetaProperty());
        }

        if (cellValue == null) {
            return "";
        }

        if (!(cellValue instanceof Component)) {
            return metadataTools.format(cellValue);
        }

        // fallback to Vaadin formatting
        UI ui = component.getUI();
        VaadinSession session = ui != null ? ui.getSession() : null;
        Converter converter = ConverterUtil.getConverter(String.class, property.getType(), session);
        if (converter != null) {
            return (String) converter.convertToPresentation(cellValue, String.class, locale);
        }

        return cellValue.toString();
    }

    protected WebTableFieldFactory createFieldFactory() {
        return new WebTableFieldFactory<>(this, security, metadataTools);
    }

    protected void setClientCaching() {
        WebConfig webConfig = configuration.getConfig(WebConfig.class);

        double cacheRate = webConfig.getTableCacheRate();
        cacheRate = cacheRate >= 0 ? cacheRate : 2;

        int pageLength = webConfig.getTablePageLength();
        pageLength = pageLength >= 0 ? pageLength : 15;

        componentComposition.setClientCaching(cacheRate, pageLength);
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
        TableItems<E> tableItems = getItems();
        if (tableItems == null) {
            return null;
        }

        E item = tableItems.getItem(itemId);

        String propertyStringId = propertyId == null ? null : propertyId.toString();

        String joinedStyle = styleProviders.stream()
                .map(sp -> sp.getStyleName(item, propertyStringId))
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));

        return Strings.emptyToNull(joinedStyle);
    }

    @Override
    protected CubaButton createContextMenuButton() {
        return new CubaButton();
    }

    @Override
    protected void beforeContextMenuButtonHandlerPerformed() {
        this.component.hideContextMenuPopup();
    }

    protected void handleClickAction() {
        Action action = getItemClickAction();
        if (action == null) {
            action = getEnterAction();
            if (action == null) {
                action = getAction(EDIT_ACTION_ID);
                if (action == null) {
                    action = getAction(VIEW_ACTION_ID);
                }
            }
        }

        if (action != null && action.isEnabled()) {
            action.actionPerform(this);
        }
    }

    @Override
    public void setLookupSelectHandler(Consumer<Collection<E>> selectHandler) {
        Consumer<Action.ActionPerformedEvent> actionHandler = event ->  {
            Set<E> selected = getSelected();
            selectHandler.accept(selected);
        };

        setEnterPressAction(
                new BaseAction(Window.Lookup.LOOKUP_ENTER_PRESSED_ACTION_ID)
                        .withHandler(actionHandler)
        );

        setItemClickAction(
                new BaseAction(Window.Lookup.LOOKUP_ITEM_CLICK_ACTION_ID)
                        .withHandler(actionHandler)
        );

        removeAllClickListeners();

        if (isEditable()) {
            EntityTableItems<E> entityTableSource = (EntityTableItems<E>) getItems();
            com.vaadin.v7.data.Container ds = component.getContainerDataSource();
            @SuppressWarnings("unchecked")
            Collection<MetaPropertyPath> propertyIds = (Collection<MetaPropertyPath>) ds.getContainerPropertyIds();

            disableEditableColumns(entityTableSource, propertyIds);
        }

        if (buttonsPanel != null && !buttonsPanel.isAlwaysVisible()) {
            buttonsPanel.setVisible(false);
        }
    }

    protected void removeAllClickListeners() {
        for (Column column : columnsOrder) {
            component.removeClickListener(column.getId());
        }
    }

    @Override
    public Collection<E> getLookupSelectedItems() {
        return getSelected();
    }

    protected Action getEnterAction() {
        for (Action action : getActions()) {
            KeyCombination kc = action.getShortcutCombination();
            if (kc != null
                    && (kc.getModifiers() == null || kc.getModifiers().length == 0)
                    && kc.getKey() == KeyCombination.Key.ENTER) {
                return action;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected void createColumns(com.vaadin.v7.data.Container ds) {
        Collection<MetaPropertyPath> properties = (Collection<MetaPropertyPath>) ds.getContainerPropertyIds();

        for (MetaPropertyPath propertyPath : properties) {
            Table.Column column = columns.get(propertyPath);

            if (column != null && !(editable && column.isEditable())) {
                String isLink = column.getXmlDescriptor() == null ?
                                null : column.getXmlDescriptor().attributeValue("link");

                if (propertyPath.getRange().isClass()) {
                    if (StringUtils.isNotEmpty(isLink)) {
                        setClickListener(propertyPath.toString(), new LinkCellClickListener(this, beanLocator));
                    }
                } else if (propertyPath.getRange().isDatatype()) {
                    if (StringUtils.isNotEmpty(isLink)) {
                        setClickListener(propertyPath.toString(), new LinkCellClickListener(this, beanLocator));
                    } else {
                        if (column.getMaxTextLength() != null) {
                            addGeneratedColumnInternal(propertyPath, new AbbreviatedColumnGenerator(column, dynamicAttributesTools));
                            setClickListener(propertyPath.toString(), new AbbreviatedCellClickListener(this, dynamicAttributesTools));
                        }
                    }
                }
            }
        }
    }

    @Override
    public TableItems<E> getItems() {
        return this.dataBinding != null ? this.dataBinding.getTableItems() : null;
    }

    @Override
    public void setItems(TableItems<E> tableItems) {
        if (this.dataBinding != null) {
            this.dataBinding.unbind();
            this.dataBinding = null;

            clearFieldDatasources();

            this.component.setContainerDataSource(null);
        }

        if (tableItems != null) {
            // Table supports only EntityTableItems
            EntityTableItems<E> entityTableSource = (EntityTableItems<E>) tableItems;

            if (this.columns.isEmpty()) {
                setupAutowiredColumns(entityTableSource);
            }

            // bind new datasource
            this.dataBinding = createTableDataContainer(tableItems);
            this.dataBinding.setProperties(getPropertyColumns(entityTableSource, columnsOrder));
            this.component.setContainerDataSource(this.dataBinding);

            setupColumnSettings(entityTableSource);

            createColumns(component.getContainerDataSource());

            for (Table.Column column : this.columnsOrder) {
                if (editable && column.getAggregation() != null
                        && (BooleanUtils.isTrue(column.isEditable()))) {
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

            if (!canBeSorted(tableItems)) {
                setSortable(false);
            }

            refreshActionsState();

            setUiTestId(tableItems);
        }
    }

    protected void setUiTestId(TableItems<E> items) {
        AppUI ui = AppUI.getCurrent();

        if (ui != null && ui.isTestMode()
                && getComponent().getCubaId() == null) {

            String testId = UiTestIds.getInferredTestId(items, "Table");
            if (testId != null) {
                getComponent().setCubaId(testId);
                componentComposition.setCubaId(testId + "_composition");
            }
        }
    }

    protected List<Object> getPropertyColumns(EntityTableItems<E> entityTableSource, List<Column<E>> columnsOrder) {
        MetaClass entityMetaClass = entityTableSource.getEntityMetaClass();
        return columnsOrder.stream()
                .filter(c -> {
                    MetaPropertyPath propertyPath = c.getBoundProperty();
                    return propertyPath != null
                            && security.isEntityAttrReadPermitted(entityMetaClass, propertyPath.toPathString());
                })
                .map(Column::getBoundProperty)
                .collect(Collectors.toList());
    }

    protected void setupColumnSettings(EntityTableItems<E> entityTableSource) {
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

    protected void setupAutowiredColumns(EntityTableItems<E> entityTableSource) {
        Collection<MetaPropertyPath> paths = getAutowiredProperties(entityTableSource);

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

    protected Collection<MetaPropertyPath> getAutowiredProperties(EntityTableItems<E> entityTableSource) {
        if (entityTableSource instanceof ContainerDataUnit) {
            CollectionContainer container = ((ContainerDataUnit) entityTableSource).getContainer();

            return container.getView() != null ?
                    // if a view is specified - use view properties
                    metadataTools.getViewPropertyPaths(container.getView(), container.getEntityMetaClass()) :
                    // otherwise use all properties from meta-class
                    metadataTools.getPropertyPaths(container.getEntityMetaClass());
        }

        if (entityTableSource instanceof DatasourceDataUnit) {
            CollectionDatasource datasource = ((DatasourceDataUnit) entityTableSource).getDatasource();

            return datasource.getView() != null ?
                    // if a view is specified - use view properties
                    metadataTools.getViewPropertyPaths(datasource.getView(), datasource.getMetaClass()) :
                    // otherwise use all properties from meta-class
                    metadataTools.getPropertyPaths(datasource.getMetaClass());
        }

        return Collections.emptyList();
    }

    @Override
    public void tableSourceItemSetChanged(TableItems.ItemSetChangeEvent<E> event) {
        clearFieldDatasources();

        // replacement for collectionChangeSelectionListener
        // #PL-2035, reload selection from ds
        Set<Object> selectedItemIds = getSelectedItemIds();
        if (selectedItemIds == null) {
            selectedItemIds = Collections.emptySet();
        }

        Set<Object> newSelection = new LinkedHashSet<>();

        TableItems<E> tableItems = event.getSource();
        for (Object entityId : selectedItemIds) {
            if (tableItems.getItem(entityId) != null) {
                newSelection.add(entityId);
            }
        }

        if (tableItems.getState() == BindingState.ACTIVE
            && tableItems instanceof EntityTableItems) {

            EntityTableItems entityTableSource = (EntityTableItems) tableItems;

            if (entityTableSource.getSelectedItem() != null) {
                newSelection.add(entityTableSource.getSelectedItem().getId());
            }
        }

        if (newSelection.isEmpty()) {
            setSelected((E) null);
        } else {
            setSelectedIds(newSelection);
        }

        refreshActionsState();
    }

    @SuppressWarnings("unchecked")
    protected void clearFieldDatasources() {
        if (fieldDatasources == null) {
            return;
        }

        // detach instance containers from entities explicitly
        for (Map.Entry<Entity, Object> entry : fieldDatasources.entrySet()) {
            if (entry.getKey() instanceof InstanceContainer) {
                InstanceContainer container = (InstanceContainer) entry.getKey();

                container.mute();
                container.setItem(null);
            }
        }

        fieldDatasources.clear();
    }

    @Override
    public void tableSourcePropertyValueChanged(TableItems.ValueChangeEvent<E> event) {
        refreshActionsState();
    }

    @Override
    public void tableSourceSelectedItemChanged(TableItems.SelectedItemChangeEvent<E> event) {
        refreshActionsState();
    }

    @Override
    public void tableSourceStateChanged(TableItems.StateChangeEvent event) {
        refreshActionsState();
    }

    protected TableDataContainer<E> createTableDataContainer(TableItems<E> tableItems) {
        if (tableItems instanceof TableItems.Sortable) {
            return new AggregatableSortableDataContainer<>((TableItems.Sortable<E>) tableItems, this);
        }
        return new AggregatableTableDataContainer<>(tableItems, this);
    }

    protected class AggregatableTableDataContainer<I> extends TableDataContainer<I> implements AggregationContainer{

        protected Collection<Object> aggregationProperties = null;

        public AggregatableTableDataContainer(TableItems<I> tableItems,
                                              TableItemsEventsDelegate<I> dataEventsDelegate) {
            super(tableItems, dataEventsDelegate);
        }

        @Override
        public Collection getAggregationPropertyIds() {
            if (aggregationProperties != null) {
                return Collections.unmodifiableCollection(aggregationProperties);
            }
            return Collections.emptyList();
        }

        @Override
        public void addContainerPropertyAggregation(Object propertyId, Type type) {
            if (aggregationProperties == null) {
                aggregationProperties = new ArrayList<>();
            } else if (aggregationProperties.contains(propertyId)) {
                throw new IllegalStateException(String.format("Aggregation property %s already exists", propertyId));
            }
            aggregationProperties.add(propertyId);
        }

        @Override
        public void removeContainerPropertyAggregation(Object propertyId) {
            if (aggregationProperties != null) {
                aggregationProperties.remove(propertyId);
                if (aggregationProperties.isEmpty()) {
                    aggregationProperties = null;
                }
            }
        }

        @Override
        public Map<Object, Object> aggregate(Context context) {
            return __aggregate(this, context);
        }
    }

    protected class AggregatableSortableDataContainer<I> extends SortableDataContainer<I>
            implements AggregationContainer {

        protected Collection<Object> aggregationProperties = null;

        public AggregatableSortableDataContainer(TableItems.Sortable<I> tableDataSource,
                                                 TableItemsEventsDelegate<I> dataEventsDelegate) {
            super(tableDataSource, dataEventsDelegate);
        }

        @Override
        public Collection getAggregationPropertyIds() {
            if (aggregationProperties != null) {
                return Collections.unmodifiableCollection(aggregationProperties);
            }
            return Collections.emptyList();
        }

        @Override
        public void addContainerPropertyAggregation(Object propertyId, AggregationContainer.Type type) {
            if (aggregationProperties == null) {
                aggregationProperties = new ArrayList<>();
            } else if (aggregationProperties.contains(propertyId)) {
                throw new IllegalStateException(String.format("Aggregation property %s already exists", propertyId));
            }
            aggregationProperties.add(propertyId);
        }

        @Override
        public void removeContainerPropertyAggregation(Object propertyId) {
            if (aggregationProperties != null) {
                aggregationProperties.remove(propertyId);
                if (aggregationProperties.isEmpty()) {
                    aggregationProperties = null;
                }
            }
        }

        @Override
        public Map<Object, Object> aggregate(AggregationContainer.Context context) {
            return __aggregate(this, context);
        }
    }

    protected boolean canBeSorted(@Nullable TableItems<E> tableItems) {
        return tableItems instanceof TableItems.Sortable;
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);

        AppUI ui = AppUI.getCurrent();
        if (id != null && ui != null && ui.isPerformanceTestMode()) {
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

    protected String getColumnCaption(Object columnId) {
        if (columnId instanceof MetaPropertyPath) {
            return ((MetaPropertyPath) columnId).getMetaProperty().getName();
        } else {
            return columnId.toString();
        }
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

                if (column.getValueProvider() == null && column.getType() == null) {
                    component.addGeneratedColumn(column.getId(), VOID_COLUMN_GENERATOR);
                } else {
                    component.addGeneratedColumn(column.getId(), VALUE_PROVIDER_GENERATOR);
                }
            }
        }
    }

    protected List<Object> getInitialVisibleColumnIds(EntityTableItems<E> entityTableSource) {
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
    public void setIconProvider(Function<? super E, String> iconProvider) {
        this.iconProvider = iconProvider;
        if (iconProvider != null) {
            setRowHeaderMode(RowHeaderMode.ICON);
        } else {
            setRowHeaderMode(RowHeaderMode.NONE);
        }
        component.refreshRowCache();
    }

    // For vaadin component extensions
    protected Resource getItemIcon(Object itemId) {
        if (iconProvider == null
                || getItems() == null) {
            return null;
        }

        E item = getItems().getItem(itemId);
        if (item == null) {
            return null;
        }
        String resourceUrl = iconProvider.apply(item);
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
            for (Element colElem : columnsElem.elements("columns")) {
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
        for (Element colElem : columnsElem.elements("columns")) {
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
                EntityTableItems<E> entityTableSource = (EntityTableItems) getItems();

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

        boolean settingsChanged = false;

        if (isUsePresentations()) {
            boolean textSelection = component.isTextSelectionEnabled();
            if (textSelection != Boolean.valueOf(element.attributeValue("textSelection"))) {
                element.addAttribute("textSelection", String.valueOf(textSelection));

                settingsChanged = true;
            }
        }

        String settingsSortProperty = null;
        String settingsSortAscending = null;

        Element columnsElem = element.element("columns");

        if (columnsElem != null) {
            settingsSortProperty = columnsElem.attributeValue("sortProperty");
            settingsSortAscending = columnsElem.attributeValue("sortAscending");
        }

        boolean commonSettingsChanged = isCommonTableSettingsChanged(columnsElem);
        boolean sortChanged = isSettingsSortPropertyChanged(settingsSortProperty, settingsSortAscending);

        if (commonSettingsChanged || sortChanged) {
            if (columnsElem != null) {
                element.remove(columnsElem);
            }
            columnsElem = element.addElement("columns");

            saveCommonTableColumnSettings(columnsElem);

            MetaPropertyPath sortProperty = (MetaPropertyPath) component.getSortContainerPropertyId();
            boolean sortAscending = component.isSortAscending();

            columnsElem.addAttribute("sortProperty", sortProperty == null ? "" : sortProperty.toString());
            columnsElem.addAttribute("sortAscending", Boolean.toString(sortAscending));

            settingsChanged = true;
        }

        String settingsDefaultPresentation = Strings.nullToEmpty(element.attributeValue("presentation"));
        String defaultPresentation = getDefaultPresentationId() == null ? "" : String.valueOf(getDefaultPresentationId());
        if (!Objects.equals(settingsDefaultPresentation, defaultPresentation)) {
            settingsChanged = true;
        }

        return settingsChanged;
    }

    /**
     * Saves common table column settings (width, visible, id).
     *
     * @param columnsElem setting element for the columns
     */
    protected void saveCommonTableColumnSettings(Element columnsElem) {
        Object[] visibleColumns = component.getVisibleColumns();
        for (Object column : visibleColumns) {
            Element colElem = columnsElem.addElement("columns");
            colElem.addAttribute("id", column.toString());

            int width = component.getColumnWidth(column);
            if (width > -1)
                colElem.addAttribute("width", String.valueOf(width));

            boolean visible = !component.isColumnCollapsed(column);
            colElem.addAttribute("visible", Boolean.toString(visible));
        }
    }

    protected boolean isCommonTableSettingsChanged(Element columnsElem) {
        if (columnsElem == null) {
            if (defaultSettings != null) {
                columnsElem = defaultSettings.getRootElement().element("columns");
                if (columnsElem == null) {
                    return true;
                }
            } else {
                return false;
            }
        }

        List<Element> settingsColumnList = columnsElem.elements("columns");
        if (settingsColumnList.size() != component.getVisibleColumns().length) {
            return true;
        }

        Object[] visibleColumns = component.getVisibleColumns();
        for (int i = 0; i < visibleColumns.length; i++) {
            Object columnId = visibleColumns[i];

            Element settingsColumn = settingsColumnList.get(i);
            String settingsColumnId = settingsColumn.attributeValue("id");

            if (columnId.toString().equals(settingsColumnId)) {
                int columnWidth = component.getColumnWidth(columnId);

                String settingsColumnWidth = settingsColumn.attributeValue("width");
                int settingColumnWidth = settingsColumnWidth == null ? -1 : Integer.parseInt(settingsColumnWidth);

                if (columnWidth != settingColumnWidth) {
                    return true;
                }

                boolean columnVisible = !component.isColumnCollapsed(columnId);
                boolean settingsColumnVisible = Boolean.parseBoolean(settingsColumn.attributeValue("visible"));

                if (columnVisible != settingsColumnVisible) {
                    return true;
                }
            } else {
                return true;
            }
        }

        return false;
    }

    protected boolean isSettingsSortPropertyChanged(String settingsSortProperty, String settingsSortAscending) {
        MetaPropertyPath sortProperty = (MetaPropertyPath) component.getSortContainerPropertyId();
        if (sortProperty == null) {
            return !Strings.isNullOrEmpty(settingsSortProperty);
        }

        if (settingsSortProperty == null ||
                !sortProperty.toString().equals(settingsSortProperty)) {
            return true;
        }

        Boolean sortAscending = component.isSortAscending();
        if (!sortAscending.equals(Boolean.parseBoolean(settingsSortAscending))) {
            return true;
        }

        return false;
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
    public boolean isCaptionAsHtml() {
        return ((com.vaadin.ui.AbstractComponent) getComposition()).isCaptionAsHtml();
    }

    @Override
    public void setCaptionAsHtml(boolean captionAsHtml) {
        ((com.vaadin.ui.AbstractComponent) getComposition()).setCaptionAsHtml(captionAsHtml);
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

        EntityTableItems<E> entityTableSource = (EntityTableItems<E>) getItems();

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
                    @Override
                    public Object generateCell(com.vaadin.v7.ui.Table source, Object itemId, Object columnId) {
                        EntityTableItems<E> entityTableSource = (EntityTableItems<E>) getItems();
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

                        if (component instanceof HasValueSource) {
                            HasValueSource<?> hasValueSource = (HasValueSource) component;

                            // if it supports value binding and bound to ValueSource, we need to unsubscribe it on detach
                            if (hasValueSource.getValueSource() != null) {
                                vComponent.addDetachListener(event ->
                                        hasValueSource.setValueSource(null)
                                );
                            }
                        }

                        // vaadin8 rework
                        // wrap field for show required asterisk
                        if ((vComponent instanceof com.vaadin.v7.ui.Field)
                                && (((com.vaadin.v7.ui.Field) vComponent).isRequired())) {
                            VerticalLayout layout = new VerticalLayout();
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
        EntityTableItems<E> entityTableSource = (EntityTableItems<E>) getItems();

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
    public void setColumnSortable(Column column, boolean sortable) {
        checkNotNullArgument(column, "column must be non null");
        if (column.isSortable() != sortable) {
            column.setSortable(sortable);
        }
        component.setColumnSortable(column.getId(), sortable);
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

    @Override
    public void setColumnCaptionAsHtml(String columnId, boolean captionAsHtml) {
        Column<E> column = getColumn(columnId);
        if (column == null) {
            throw new IllegalStateException(String.format("Column with id '%s' not found", columnId));
        }

        setColumnCaptionAsHtml(column, captionAsHtml);
    }

    @Override
    public void setColumnCaptionAsHtml(Column column, boolean captionAsHtml) {
        checkNotNullArgument(column, "Column must be non null");

        if (column.getCaptionAsHtml() != captionAsHtml) {
            column.setCaptionAsHtml(captionAsHtml);
        }
        component.setColumnCaptionAsHtml(column.getId(), captionAsHtml);
    }

    @Deprecated
    @Override
    public void refresh() {
        TableItems<E> tableItems = getItems();
        if (tableItems instanceof DatasourceTableItems) {
            ((DatasourceTableItems) tableItems).getDatasource().refresh();
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
        if (!(getItems() instanceof AggregatableTableItems)) {
            throw new IllegalStateException("Table items must implement AggregatableTableItems in " +
                    "order to use aggregation");
        }

        List<AggregationInfo> aggregationInfos = new ArrayList<>();
        for (Object propertyId : container.getAggregationPropertyIds()) {
            Table.Column column = columns.get(propertyId);
            AggregationInfo aggregation = column.getAggregation();
            if (aggregation != null) {
                checkAggregation(aggregation);
                aggregationInfos.add(aggregation);
            }
        }

        Map<AggregationInfo, String> results = ((AggregatableTableItems<E>) getItems()).aggregate(
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
            Table.Column<E> column = columns.get(entry.getKey());
            if (aggregationCells.get(column) != null) {
                Object value = entry.getValue();
                String cellText = getFormattedValue(column, value);
                entry.setValue(cellText);
            }
        }
        return results;
    }

    protected String getFormattedValue(Column<E> column, Object value) {
        String cellText;
        if (value == null) {
            cellText = "";
        } else {
            if (value instanceof String) {
                cellText = (String) value;
            } else {
                Function<Object, String> formatter = column.getFormatter();
                if (formatter != null) {
                    cellText = formatter.apply(value);
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
    public Subscription addColumnCollapseListener(Consumer<ColumnCollapseEvent> listener) {
        if (columnCollapseListener == null) {
            columnCollapseListener = this::onColumnCollapseStateChange;
            component.addColumnCollapseListener(columnCollapseListener);
        }

        getEventHub().subscribe(ColumnCollapseEvent.class, listener);

        return () -> removeColumnCollapseListener(listener);
    }

    @Override
    public void removeColumnCollapseListener(Consumer<ColumnCollapseEvent> listener) {
        unsubscribe(ColumnCollapseEvent.class, listener);

        if (!hasSubscriptions(ColumnCollapseEvent.class)
                && columnCollapseListener != null) {
            component.removeColumnCollapseListener(columnCollapseListener);
            columnCollapseListener = null;
        }
    }

    protected void onColumnCollapseStateChange(com.vaadin.v7.ui.Table.ColumnCollapseEvent e) {
        Column collapsedColumn = getColumn(e.getPropertyId().toString());
        boolean collapsed = component.isColumnCollapsed(e.getPropertyId());

        ColumnCollapseEvent<E> event = new ColumnCollapseEvent<>(this, collapsedColumn, collapsed);
        publish(ColumnCollapseEvent.class, event);
    }

    @Override
    public void setCellClickListener(String columnId, Consumer<CellClickEvent<E>> clickListener) {
        checkNotNullArgument(getColumn(columnId), String.format("column with id '%s' not found", columnId));

        component.setClickListener(getColumn(columnId).getId(), (itemId, columnId1) -> {
            TableItems<E> tableItems = getItems();
            if (tableItems == null) {
                return;
            }

            E item = tableItems.getItem(itemId);
            CellClickEvent<E> event = new CellClickEvent<>(this, item, columnId1.toString());
            clickListener.accept(event);
        });
    }

    @Override
    public void removeClickListener(String columnId) {
        component.removeClickListener(getColumn(columnId).getId());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addSelectionListener(Consumer<SelectionEvent<E>> listener) {
        return getEventHub().subscribe(SelectionEvent.class, (Consumer) listener);
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
            CubaButton contextMenuButton = createContextMenuButton();
            initContextMenuButton(contextMenuButton, action);

            customContextMenu.addComponent(contextMenuButton);
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
                    || isValueGeneratedColumn(propertyId))) {

            MetaPropertyPath propertyPath;
            if (propertyId instanceof MetaPropertyPath) {
                propertyPath = (MetaPropertyPath) propertyId;
            } else {
                EntityTableItems<E> entityTableSource = (EntityTableItems<E>) getItems();

                propertyPath = entityTableSource != null ?
                        entityTableSource.getEntityMetaClass().getPropertyPath(propertyId.toString()) : null;
            }

            style = generateDefaultCellStyle(itemId, propertyId, propertyPath);
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

    protected boolean isValueGeneratedColumn(Object propertyId) {
        return component.getColumnGenerator(propertyId) instanceof AbbreviatedColumnGenerator
                    || component.getColumnGenerator(propertyId) == VALUE_PROVIDER_GENERATOR;
    }

    protected String generateDefaultCellStyle(Object itemId, Object propertyId,
                                              @Nullable MetaPropertyPath propertyPath) {
        String style = null;

        String stringPropertyId = propertyId.toString();

        Column column = getColumn(stringPropertyId);
        if (column != null)
            if (column.getValueProvider() != null) {
                // column ValueProvider supports Boolean type
                if (dataBinding != null
                        && column.getType() == Boolean.class
                        && column.getFormatter() == null) {

                    Entity item = dataBinding.getTableItems().getItem(itemId);
                    if (item != null) {
                        Boolean value = (Boolean) column.getValueProvider().apply(item);
                        if (BooleanUtils.isTrue(value)) {
                            style = BOOLEAN_CELL_STYLE_TRUE;
                        } else {
                            style = BOOLEAN_CELL_STYLE_FALSE;
                        }
                    }
                }
            } else if (propertyPath != null) {
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
                        style = generateClickableCellStyles(itemId, column, propertyPath);
                    }
                }

                if (propertyPath.getRangeJavaClass() == Boolean.class
                        && column.getFormatter() == null
                        && dataBinding != null) {
                    Entity item = dataBinding.getTableItems().getItem(itemId);
                    if (item != null) {
                        Boolean value = item.getValueEx(propertyPath);
                        if (BooleanUtils.isTrue(value)) {
                            style = BOOLEAN_CELL_STYLE_TRUE;
                        } else {
                            style = BOOLEAN_CELL_STYLE_FALSE;
                        }
                    }
                }
            }

        return style;
    }

    protected String generateClickableCellStyles(Object itemId, Column column, MetaPropertyPath propertyPath) {
        EntityTableItems<E> entityTableSource = (EntityTableItems<E>) getItems();
        if (entityTableSource == null) {
            throw new IllegalStateException("TableItems is not set");
        }

        E item = entityTableSource.getItemNN(itemId);

        Object value = item.getValueEx(propertyPath);
        String stringValue;
        if (value instanceof String) {
            stringValue = item.getValueEx(propertyPath);
        } else {
            MetaProperty metaProperty = propertyPath.getMetaProperty();

            if (DynamicAttributesUtils.isDynamicAttribute(metaProperty)) {
                stringValue = dynamicAttributesTools.getDynamicAttributeValueAsString(metaProperty, value);
            } else {
                stringValue = value == null ? null : value.toString();
            }
        }

        if (column.getMaxTextLength() != null) {
            boolean isMultiLineCell = StringUtils.contains(stringValue, "\n");
            if ((stringValue != null && stringValue.length() > column.getMaxTextLength() + MAX_TEXT_LENGTH_GAP)
                    || isMultiLineCell) {
                return "c-table-cell-textcut";
            } else {
                // use special marker stylename
                return "c-table-clickable-text";
            }
        }
        return null;
    }

    @Override
    public void setAggregationDistributionProvider(AggregationDistributionProvider<E> distributionProvider) {
        this.distributionProvider = distributionProvider;

        component.setAggregationDistributionProvider(this::distributeAggregation);
    }

    protected boolean distributeAggregation(AggregationInputValueChangeContext context) {
        if (distributionProvider != null) {
            String value = context.getValue();
            Object columnId = context.getColumnId();
            try {
                Object parsedValue = getParsedAggregationValue(value, columnId);
                TableItems<E> tableItems = getItems();
                Collection<E> items = tableItems == null ?
                        Collections.emptyList() : tableItems.getItems();

                AggregationDistributionContext<E> distributionContext =
                        new AggregationDistributionContext<>(getColumn(columnId.toString()),
                                parsedValue, items, context.isTotalAggregation());

                distributionProvider.onDistribution(distributionContext);
            } catch (ValueConversionException e) {
                showParseErrorNotification(e.getLocalizedMessage());
                return false; // rollback to previous value
            } catch (ParseException e) {
                showParseErrorNotification(messages.getMainMessage("validationFail"));
                return false; // rollback to previous value
            }
        }
        return true;
    }

    @Override
    public AggregationDistributionProvider<E> getAggregationDistributionProvider() {
        return distributionProvider;
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

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addLookupValueChangeListener(Consumer<LookupSelectionChangeEvent<E>> listener) {
        return getEventHub().subscribe(LookupSelectionChangeEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeLookupValueChangeListener(Consumer<LookupSelectionChangeEvent<E>> listener) {
        unsubscribe(LookupSelectionChangeEvent.class, (Consumer) listener);
    }

    @Nullable
    @Override
    public Object createInstallHandler(Class<?> targetObjectType, FrameOwner frameOwner, Method method) {
        if (targetObjectType == StyleProvider.class) {
            return new InstalledStyleProvider(frameOwner, method);
        }
        return null;
    }

    protected static class InstalledStyleProvider implements StyleProvider {
        private final FrameOwner frameOwner;
        private final Method method;

        public InstalledStyleProvider(FrameOwner frameOwner, Method method) {
            this.frameOwner = frameOwner;
            this.method = method;
        }

        @Override
        public String getStyleName(Entity entity, @Nullable String property) {
            try {
                return (String) method.invoke(frameOwner, entity, property);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Exception on @Install invocation", e);
            }
        }

        @Override
        public String toString() {
            return "InstalledStyleProvider{" +
                    "frameOwner=" + frameOwner.getClass() +
                    ", method=" + method +
                    '}';
        }
    }

    protected Object getParsedAggregationValue(String value, Object columnId) throws ParseException {
        Object parsedValue = value;

        for (Column column : getColumns()) {
            if (column.getId().equals(columnId)) {
                AggregationInfo aggregationInfo = column.getAggregation();
                if (aggregationInfo == null || aggregationInfo.getFormatter() != null) {
                    return parsedValue;
                }

                MetaPropertyPath propertyPath = aggregationInfo.getPropertyPath();
                Class<?> resultClass;
                Range range = propertyPath != null ? propertyPath.getRange() : null;
                if (range != null && range.isDatatype()) {
                    if (aggregationInfo.getType() == AggregationInfo.Type.COUNT) {
                        return parsedValue;
                    }

                    if (aggregationInfo.getStrategy() == null) {
                        Class<?> rangeJavaClass = propertyPath.getRangeJavaClass();
                        Aggregation aggregation = Aggregations.get(rangeJavaClass);
                        resultClass = aggregation.getResultClass();
                    } else {
                        resultClass = aggregationInfo.getStrategy().getResultClass();
                    }

                } else if (aggregationInfo.getStrategy() == null) {
                    return parsedValue;
                } else {
                    resultClass = aggregationInfo.getStrategy().getResultClass();
                }

                parsedValue = datatypeRegistry.getNN(resultClass).parse(value, locale);

                break;
            }
        }
        return parsedValue;
    }

    protected void showParseErrorNotification(String message) {
        ScreenContext screenContext = UiControllerUtils.getScreenContext(getFrame().getFrameOwner());
        screenContext.getNotifications().create(Notifications.NotificationType.TRAY)
                .withDescription(message)
                .show();
    }
}