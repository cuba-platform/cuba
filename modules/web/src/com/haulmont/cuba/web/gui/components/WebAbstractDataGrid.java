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
 */

package com.haulmont.cuba.web.gui.components;

import com.google.common.collect.ImmutableMap;
import com.haulmont.bali.events.EventRouter;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.ContentMode;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.components.DescriptionProvider;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.KeyCombination;
import com.haulmont.cuba.gui.components.LookupComponent;
import com.haulmont.cuba.gui.components.MouseEventDetails;
import com.haulmont.cuba.gui.components.RowsCount;
import com.haulmont.cuba.gui.components.SecuredActionsHolder;
import com.haulmont.cuba.gui.components.StyleProvider;
import com.haulmont.cuba.gui.components.VisibilityChangeNotifier;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.WindowDelegate;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.DataGridSource;
import com.haulmont.cuba.gui.components.data.EntityDataGridSource;
import com.haulmont.cuba.gui.components.formatters.CollectionFormatter;
import com.haulmont.cuba.gui.components.security.ActionsPermissions;
import com.haulmont.cuba.gui.components.sys.ShortcutsDelegate;
import com.haulmont.cuba.gui.components.sys.ShowInfoAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.data.impl.WeakCollectionChangeListener;
import com.haulmont.cuba.gui.data.impl.WeakItemChangeListener;
import com.haulmont.cuba.gui.data.impl.WeakItemPropertyChangeListener;
import com.haulmont.cuba.gui.data.impl.WeakStateChangeListener;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.components.datagrid.DataGridDataProvider;
import com.haulmont.cuba.web.gui.components.datagrid.DataGridSourceEventsDelegate;
import com.haulmont.cuba.web.gui.components.datagrid.SortableDataGridDataProvider;
import com.haulmont.cuba.web.gui.components.renderers.RendererWrapper;
import com.haulmont.cuba.web.gui.components.renderers.WebButtonRenderer;
import com.haulmont.cuba.web.gui.components.renderers.WebCheckBoxRenderer;
import com.haulmont.cuba.web.gui.components.renderers.WebClickableTextRenderer;
import com.haulmont.cuba.web.gui.components.renderers.WebComponentRenderer;
import com.haulmont.cuba.web.gui.components.renderers.WebDateRenderer;
import com.haulmont.cuba.web.gui.components.renderers.WebHtmlRenderer;
import com.haulmont.cuba.web.gui.components.renderers.WebImageRenderer;
import com.haulmont.cuba.web.gui.components.renderers.WebLocalDateRenderer;
import com.haulmont.cuba.web.gui.components.renderers.WebLocalDateTimeRenderer;
import com.haulmont.cuba.web.gui.components.renderers.WebNumberRenderer;
import com.haulmont.cuba.web.gui.components.renderers.WebProgressBarRenderer;
import com.haulmont.cuba.web.gui.components.renderers.WebTextRenderer;
import com.haulmont.cuba.web.gui.components.util.ShortcutListenerDelegate;
import com.haulmont.cuba.web.gui.components.valueproviders.DataGridConverterBasedValueProvider;
import com.haulmont.cuba.web.gui.components.valueproviders.EntityValueProvider;
import com.haulmont.cuba.web.gui.components.valueproviders.FormatterBasedValueProvider;
import com.haulmont.cuba.web.gui.components.valueproviders.StringPresentationValueProvider;
import com.haulmont.cuba.web.gui.components.valueproviders.YesNoIconPresentationValueProvider;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CubaEnhancedGrid;
import com.haulmont.cuba.web.widgets.addons.contextmenu.Menu;
import com.haulmont.cuba.web.widgets.addons.contextmenu.MenuItem;
import com.haulmont.cuba.web.widgets.data.SortableDataProvider;
import com.haulmont.cuba.web.widgets.grid.CubaGridContextMenu;
import com.haulmont.cuba.web.widgets.grid.CubaMultiCheckSelectionModel;
import com.haulmont.cuba.web.widgets.grid.CubaMultiSelectionModel;
import com.haulmont.cuba.web.widgets.grid.CubaSingleSelectionModel;
import com.vaadin.data.HasValue;
import com.vaadin.data.SelectionModel;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.selection.MultiSelectionEvent;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DescriptionGenerator;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.StyleGenerator;
import com.vaadin.ui.components.grid.ColumnVisibilityChangeListener;
import com.vaadin.ui.components.grid.Footer;
import com.vaadin.ui.components.grid.Header;
import com.vaadin.ui.components.grid.StaticSection;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.ComponentsHelper.findActionById;

public abstract class WebAbstractDataGrid<T extends Grid<E> & CubaEnhancedGrid, E extends Entity>
        extends WebAbstractComponent<T>
        implements DataGrid<E>, SecuredActionsHolder, LookupComponent.LookupSelectionChangeNotifier,
        DataGridSourceEventsDelegate<E>, InitializingBean {

    protected static final String HAS_TOP_PANEL_STYLE_NAME = "has-top-panel";
    protected static final String TEXT_SELECTION_ENABLED_STYLE = "text-selection-enabled";

    private static final Logger log = LoggerFactory.getLogger(WebAbstractDataGrid.class);

    /* Beans */
    protected ApplicationContext applicationContext;
    protected MetadataTools metadataTools;
    protected Security security;
    protected Messages messages;
    protected MessageTools messageTools;

    // Style names used by grid itself
    protected final List<String> internalStyles = new ArrayList<>(2);

    protected final Map<String, Column<E>> columns = new HashMap<>();
    protected List<Column<E>> columnsOrder = new ArrayList<>();
    protected final Map<String, ColumnGenerator<E, ?>> columnGenerators = new HashMap<>();

    protected final List<Action> actionList = new ArrayList<>();
    protected final ShortcutsDelegate<ShortcutListener> shortcutsDelegate;
    protected final ActionsPermissions actionsPermissions = new ActionsPermissions(this);

    protected CubaGridContextMenu<E> contextMenu;
    protected final List<ActionMenuItemWrapper> contextMenuItems = new ArrayList<>();

    protected boolean settingsEnabled = true;
    protected boolean sortable = true;
    protected boolean columnsCollapsingAllowed = true;
    protected boolean textSelectionEnabled = false;

    protected Action itemClickAction;
    protected Action enterPressAction;

    protected SelectionMode selectionMode;

    protected GridComposition componentComposition;
    protected HorizontalLayout topPanel;
    protected ButtonsPanel buttonsPanel;
    protected RowsCount rowsCount;

    protected List<StyleProvider<? super E>> rowStyleProviders;
    protected List<CellStyleProvider<? super E>> cellStyleProviders;

    protected DescriptionProvider<? super E> rowDescriptionProvider;
    protected CellDescriptionProvider<? super E> cellDescriptionProvider;

    protected DetailsGenerator<E> detailsGenerator = null;

    protected Registration columnCollapsingChangeListenerRegistration;
    protected Registration columnResizeListenerRegistration;
    protected Registration sortListenerRegistration;
    protected Registration contextClickListenerRegistration;

    protected com.vaadin.event.selection.SelectionListener<E> selectionListener;

//    protected CubaGrid.EditorCloseListener editorCloseListener;
//    protected CubaGrid.BeforeEditorOpenListener beforeEditorOpenListener;
//    protected CubaGrid.EditorPreCommitListener editorPreCommitListener;
//    protected CubaGrid.EditorPostCommitListener editorPostCommitListener;

    protected final List<HeaderRow> headerRows = new ArrayList<>();
    protected final List<FooterRow> footerRows = new ArrayList<>();

    protected static final Map<Class<? extends Renderer>, Class<? extends Renderer>> rendererClasses;

    protected boolean showIconsForPopupMenuActions;

    protected DataGridDataProvider<E> dataBinding;

    static {
        ImmutableMap.Builder<Class<? extends Renderer>, Class<? extends Renderer>> builder =
                new ImmutableMap.Builder<>();

        builder.put(TextRenderer.class, WebTextRenderer.class);
        builder.put(ClickableTextRenderer.class, WebClickableTextRenderer.class);
        builder.put(HtmlRenderer.class, WebHtmlRenderer.class);
        builder.put(ProgressBarRenderer.class, WebProgressBarRenderer.class);
        builder.put(DateRenderer.class, WebDateRenderer.class);
        builder.put(LocalDateRenderer.class, WebLocalDateRenderer.class);
        builder.put(LocalDateTimeRenderer.class, WebLocalDateTimeRenderer.class);
        builder.put(NumberRenderer.class, WebNumberRenderer.class);
        builder.put(ButtonRenderer.class, WebButtonRenderer.class);
        builder.put(ImageRenderer.class, WebImageRenderer.class);
        builder.put(CheckBoxRenderer.class, WebCheckBoxRenderer.class);
        builder.put(ComponentRenderer.class, WebComponentRenderer.class);

        rendererClasses = builder.build();
    }

    public WebAbstractDataGrid() {
        component = createComponent();
        shortcutsDelegate = createShortcutsDelegate();
    }

    protected abstract T createComponent();

    protected ShortcutsDelegate<ShortcutListener> createShortcutsDelegate() {
        return new ShortcutsDelegate<ShortcutListener>() {
            @Override
            protected ShortcutListener attachShortcut(String actionId, KeyCombination keyCombination) {
                ShortcutListener shortcut =
                        new ShortcutListenerDelegate(actionId, keyCombination.getKey().getCode(),
                                KeyCombination.Modifier.codes(keyCombination.getModifiers())
                        ).withHandler((sender, target) -> {
                            if (target == component) {
                                Action action = getAction(actionId);
                                if (action != null && action.isEnabled() && action.isVisible()) {
                                    action.actionPerform(WebAbstractDataGrid.this);
                                }
                            }
                        });

                component.addShortcutListener(shortcut);
                return shortcut;
            }

            @Override
            protected void detachShortcut(Action action, ShortcutListener shortcutDescriptor) {
                component.removeShortcutListener(shortcutDescriptor);
            }

            @Override
            protected Collection<Action> getActions() {
                return WebAbstractDataGrid.this.getActions();
            }
        };
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initComponent(component);

        initHeaderRows(component);
        initFooterRows(component);
        initEditor(component);

        initContextMenu();
    }

    @Inject
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Inject
    public void setMetadataTools(MetadataTools metadataTools) {
        this.metadataTools = metadataTools;
    }

    @Inject
    public void setSecurity(Security security) {
        this.security = security;
    }

    @Inject
    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    @Inject
    public void setMessageTools(MessageTools messageTools) {
        this.messageTools = messageTools;
    }

    @Inject
    public void setThemeConstantsManager(ThemeConstantsManager themeConstantsManager) {
        ThemeConstants theme = themeConstantsManager.getConstants();
        this.showIconsForPopupMenuActions = theme.getBoolean("cuba.gui.showIconsForPopupMenuActions", false);
    }

    protected void initComponent(Grid<E> component) {
        selectionListener = createSelectionListener();
        setSelectionMode(SelectionMode.SINGLE);

        component.setColumnReorderingAllowed(true);

        component.addShortcutListener(createEnterShortcutListener());
        component.addItemClickListener(createItemClickListener());
        component.addColumnReorderListener(createColumnReorderListener());
        component.addSortListener(createSortListener());

        componentComposition = new GridComposition();
        componentComposition.setPrimaryStyleName("c-data-grid-composition");
        componentComposition.setGrid(component);
        componentComposition.addComponent(component);
        componentComposition.setWidthUndefined();

        component.setSizeUndefined();
        component.setHeightMode(HeightMode.UNDEFINED);

        component.setStyleGenerator(createRowStyleGenerator());
    }

    protected com.vaadin.event.SortEvent.SortListener<GridSortOrder<E>> createSortListener() {
        return (com.vaadin.event.SortEvent.SortListener<GridSortOrder<E>>) event -> {
            if (component.getDataProvider() instanceof SortableDataProvider) {
                //noinspection unchecked
                SortableDataProvider<E> dataProvider = (SortableDataProvider<E>) component.getDataProvider();

                List<GridSortOrder<E>> sortOrders = event.getSortOrder();
                if (sortOrders.isEmpty()) {
                    dataProvider.resetSortOrder();
                } else {
                    GridSortOrder<E> sortOrder = sortOrders.get(0);

                    Column<E> column = getColumnByGridColumn(sortOrder.getSorted());
                    if (column != null) {
                        MetaPropertyPath propertyPath = column.getPropertyPath();
                        boolean ascending = com.vaadin.shared.data.sort.SortDirection.ASCENDING
                                .equals(sortOrder.getDirection());
                        dataProvider.sort(new Object[]{propertyPath}, new boolean[]{ascending});
                    }
                }
            }
        };
    }

    protected com.vaadin.event.selection.SelectionListener<E> createSelectionListener() {
        return e -> {
            DataGridSource<E> dataGridSource = getDataGridSource();

            if (dataGridSource == null
                    || dataGridSource.getState() == BindingState.INACTIVE) {
                return;
            }

            Set<E> selected = getSelected();
            if (selected.isEmpty()) {
                dataGridSource.setSelectedItem(null);
            } else {
                // reset selection and select new item
                if (isMultiSelect()) {
                    dataGridSource.setSelectedItem(null);
                }

                E newItem = selected.iterator().next();
                dataGridSource.setSelectedItem(newItem);
            }

            LookupSelectionChangeEvent selectionChangeEvent = new LookupSelectionChangeEvent(this);
            getEventRouter().fireEvent(LookupSelectionChangeListener.class,
                    LookupSelectionChangeListener::lookupValueChanged, selectionChangeEvent);

            if (getEventRouter().hasListeners(SelectionListener.class)) {
                fireSelectionEvent(e);
            }
        };
    }

    protected void fireSelectionEvent(com.vaadin.event.selection.SelectionEvent<E> e) {
        List<E> addedItems;
        List<E> removedItems;
        List<E> selectedItems;
        if (e instanceof MultiSelectionEvent) {
            addedItems = new ArrayList<>(((MultiSelectionEvent<E>) e).getAddedSelection());
            removedItems = new ArrayList<>(((MultiSelectionEvent<E>) e).getRemovedSelection());
            selectedItems = new ArrayList<>(e.getAllSelectedItems());
        } else {
            addedItems = new ArrayList<>(e.getAllSelectedItems());
            //noinspection unchecked
            E oldValue = ((HasValue.ValueChangeEvent<E>) e).getOldValue();
            removedItems = oldValue != null ? Collections.singletonList(oldValue) : Collections.emptyList();
            selectedItems = new ArrayList<>(e.getAllSelectedItems());
        }

        SelectionEvent<E> event =
                new SelectionEvent<>(WebAbstractDataGrid.this, addedItems, removedItems, selectedItems);
        //noinspection unchecked
        getEventRouter().fireEvent(SelectionListener.class, SelectionListener::selected, event);
    }

    protected ShortcutListenerDelegate createEnterShortcutListener() {
        return new ShortcutListenerDelegate("dataGridEnter", KeyCode.ENTER, null)
                .withHandler((sender, target) -> {
                    if (target == this.component) {
                        if (WebAbstractDataGrid.this.isEditorEnabled()) {
                            // Prevent custom actions on Enter if DataGrid editor is enabled
                            // since it's the default shortcut to open editor
                            return;
                        }

                        if (enterPressAction != null) {
                            enterPressAction.actionPerform(this);
                        } else {
                            handleDoubleClickAction();
                        }
                    }
                });
    }

    protected com.vaadin.ui.components.grid.ItemClickListener<E> createItemClickListener() {
        return e -> {
            com.vaadin.shared.MouseEventDetails vMouseEventDetails = e.getMouseEventDetails();
            if (vMouseEventDetails.isDoubleClick() && e.getItem() != null
                    && !WebAbstractDataGrid.this.isEditorEnabled()) {
                // note: for now Grid doesn't send double click if editor is enabled,
                // but it's better to handle it manually
                handleDoubleClickAction();
            }

            if (getEventRouter().hasListeners(ItemClickListener.class)) {
                MouseEventDetails mouseEventDetails = WebWrapperUtils.toMouseEventDetails(vMouseEventDetails);

                //noinspection unchecked
                E item = e.getItem();
                if (item == null) {
                    // this can happen if user clicked on an item which is removed from the
                    // datasource, so we don't want to send such event because it's useless
                    return;
                }

                Column<E> column = getColumnById(item.getId());

                ItemClickEvent<E> event = new ItemClickEvent<>(WebAbstractDataGrid.this,
                        mouseEventDetails, item, item.getId(), column != null ? column.getId() : null);
                //noinspection unchecked
                getEventRouter().fireEvent(ItemClickListener.class, ItemClickListener::onItemClick, event);
            }
        };
    }

    protected com.vaadin.ui.components.grid.ColumnReorderListener createColumnReorderListener() {
        return e -> {
            if (e.isUserOriginated()) {
                // Grid doesn't know about columns hidden by security permissions,
                // so we need to return them back to they previous positions
                columnsOrder = restoreColumnsOrder(getColumnsOrderInternal());

                if (getEventRouter().hasListeners(ColumnReorderListener.class)) {
                    ColumnReorderEvent event = new ColumnReorderEvent(WebAbstractDataGrid.this);
                    getEventRouter().fireEvent(ColumnReorderListener.class,
                            ColumnReorderListener::columnReordered, event);
                }
            }
        };
    }

    protected void initEditor(Grid<E> component) {
        component.getEditor().setSaveCaption(messages.getMainMessage("actions.Ok"));
        component.getEditor().setCancelCaption(messages.getMainMessage("actions.Cancel"));
    }

    protected void initHeaderRows(Grid<E> component) {
        for (int i = 0; i < component.getHeaderRowCount(); i++) {
            com.vaadin.ui.components.grid.HeaderRow headerRow = component.getHeaderRow(i);
            addHeaderRowInternal(headerRow);
        }
    }

    protected void initFooterRows(Grid<E> component) {
        for (int i = 0; i < component.getFooterRowCount(); i++) {
            com.vaadin.ui.components.grid.FooterRow footerRow = component.getFooterRow(i);
            addFooterRowInternal(footerRow);
        }
    }

    protected List<Column<E>> getColumnsOrderInternal() {
        List<Grid.Column<E, ?>> columnsOrder = component.getColumns();
        return columnsOrder.stream()
                .map(this::getColumnByGridColumn)
                .collect(Collectors.toList());
    }

    /**
     * Inserts columns hidden by security permissions (or with visible = false,
     * which means that where is no Grid.Column associated with DatGrid.Column)
     * into a list of visible columns, passed as a parameter, in they original positions.
     *
     * @param visibleColumns the list of DataGrid columns,
     *                       not hidden by security permissions
     * @return a list of all columns in DataGrid
     */
    protected List<Column<E>> restoreColumnsOrder(List<Column<E>> visibleColumns) {
        List<Column<E>> newColumnsOrder = new ArrayList<>(visibleColumns);
        columnsOrder.stream()
                .filter(column -> !visibleColumns.contains(column))
                .forEach(column -> newColumnsOrder.add(columnsOrder.indexOf(column), column));
        return newColumnsOrder;
    }

    protected void initContextMenu() {
        contextMenu = new CubaGridContextMenu<>(component);

        contextMenu.addGridBodyContextMenuListener(event -> {
            if (!component.getSelectedItems().contains(event.getItem())) {
                // In the multi select model 'setSelected' adds item to selected set,
                // but, in case of context click, we want to have a single selected item,
                // if it isn't in a set of already selected items
                if (isMultiSelect()) {
                    component.deselectAll();
                }
                //noinspection unchecked
                setSelected(event.getItem());
            }
        });
    }

    protected void refreshActionsState() {
        getActions().forEach(Action::refreshState);
    }

    protected void handleDoubleClickAction() {
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
            Window window = ComponentsHelper.getWindowImplementation(WebAbstractDataGrid.this);
            if (window instanceof Window.Wrapper) {
                window = ((Window.Wrapper) window).getWrappedWindow();
            }

            if (!(window instanceof Window.Lookup)) {
                action.actionPerform(WebAbstractDataGrid.this);
            } else {
                Window.Lookup lookup = (Window.Lookup) window;

                com.haulmont.cuba.gui.components.Component lookupComponent = lookup.getLookupComponent();
                if (lookupComponent != this)
                    action.actionPerform(WebAbstractDataGrid.this);
                else if (action.getId().equals(WindowDelegate.LOOKUP_ITEM_CLICK_ACTION_ID)) {
                    action.actionPerform(WebAbstractDataGrid.this);
                }
            }
        }
    }

    @Override
    public void focus() {
        component.focus();
    }

    @Override
    public void setLookupSelectHandler(Runnable selectHandler) {
        setEnterPressAction(new AbstractAction(WindowDelegate.LOOKUP_ENTER_PRESSED_ACTION_ID) {
            @Override
            public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                selectHandler.run();
            }
        });

        setItemClickAction(new AbstractAction(WindowDelegate.LOOKUP_ITEM_CLICK_ACTION_ID) {
            @Override
            public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                selectHandler.run();
            }
        });
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

    protected RowStyleGeneratorAdapter<E> createRowStyleGenerator() {
        return new RowStyleGeneratorAdapter<>();
    }

//    protected CellStyleGeneratorAdapter createCellStyleGenerator() {
//        return new CellStyleGeneratorAdapter();
//    }

    /*protected CubaGridEditorFieldFactory createEditorFieldFactory() {
        return new WebDataGridEditorFieldFactory(this);
    }*/

    @Override
    public List<Column<E>> getColumns() {
        return Collections.unmodifiableList(columnsOrder);
    }

    @Override
    public List<Column<E>> getVisibleColumns() {
        return columnsOrder.stream()
                .filter(Column::isVisible)
                .collect(Collectors.toList());
    }

    @Nullable
    @Override
    public Column<E> getColumn(String id) {
        checkNotNullArgument(id);

        return columns.get(id);
    }

    @Override
    public Column<E> getColumnNN(String id) {
        Column<E> column = getColumn(id);
        if (column == null) {
            throw new IllegalStateException("Unable to find column with id " + id);
        }
        return column;
    }

    @Override
    public void addColumn(Column<E> column) {
        addColumn(column, columnsOrder.size());
    }

    @Override
    public void addColumn(Column<E> column, int index) {
        checkNotNullArgument(column, "Column must be non null");
        if (column.getOwner() != null && column.getOwner() != this) {
            throw new IllegalArgumentException("Can't add column owned by another DataGrid");
        }
        addColumnInternal((ColumnImpl<E>) column, index);
    }

    @Override
    public Column<E> addColumn(String id, MetaPropertyPath propertyPath) {
        return addColumn(id, propertyPath, columnsOrder.size());
    }

    @Override
    public Column<E> addColumn(String id, MetaPropertyPath propertyPath, int index) {
        ColumnImpl<E> column = new ColumnImpl<>(id, propertyPath, this);
        addColumnInternal(column, index);
        return column;
    }

    protected void addColumnInternal(ColumnImpl<E> column, int index) {
        Grid.Column<E, ?> gridColumn = component.addColumn(
                new EntityValueProvider<>(column.getPropertyPath()));

        columns.put(column.getId(), column);
        columnsOrder.add(index, column);

        final String caption = StringUtils.capitalize(column.getCaption() != null
                ? column.getCaption()
                : generateColumnCaption(column));
        column.setCaption(caption);

        if (column.getOwner() == null) {
            column.setOwner(this);
        }

        setupGridColumnProperties(gridColumn, column);

        component.setColumnOrder(getColumnOrder());
    }


    protected String generateColumnCaption(Column<E> column) {
        return column.getPropertyPath() != null
                ? column.getPropertyPath().getMetaProperty().getName()
                : column.getId();
    }

    protected void setupGridColumnProperties(Grid.Column<E, ?> gridColumn, Column<E> column) {
        if (gridColumn.getId() == null) {
            gridColumn.setId(column.getId());
        } else if (!Objects.equals(gridColumn.getId(), column.getId())) {
            log.warn("Trying to copy column settings with mismatched ids. Grid.Column: " +
                    gridColumn.getId() + "; DataGrid.Column: " + column.getId());
        }
        gridColumn.setCaption(column.getCaption());
        gridColumn.setHidingToggleCaption(column.getCollapsingToggleCaption());
        if (column.isWidthAuto()) {
            gridColumn.setWidthUndefined();
        } else {
            gridColumn.setWidth(column.getWidth());
        }
        gridColumn.setExpandRatio(column.getExpandRatio());
        gridColumn.setMinimumWidth(column.getMinimumWidth());
        gridColumn.setMaximumWidth(column.getMaximumWidth());
        gridColumn.setHidden(column.isCollapsed());
        gridColumn.setHidable(column.isCollapsible() && column.getOwner().isColumnsCollapsingAllowed());
        gridColumn.setResizable(column.isResizable());
        gridColumn.setSortable(column.isSortable() && column.getOwner().isSortable());
        if (gridColumn.getEditorBinding() != null) {
            gridColumn.setEditable(column.isEditable() && column.getOwner().isEditorEnabled());
        }

        AppUI current = AppUI.getCurrent();
        if (current != null && current.isTestMode()) {
            addColumnId(gridColumn, column);
        }

        //noinspection unchecked
        gridColumn.setRenderer(getDefaultPresentationValueProvider(column), getDefaultRenderer(column));
        gridColumn.setStyleGenerator(new CellStyleGeneratorAdapter<>(column));
        gridColumn.setDescriptionGenerator(new CellDescriptionGeneratorAdapter<>(column),
                WebWrapperUtils.toVaadinContentMode(((ColumnImpl) column).getDescriptionContentMode()));

        ((ColumnImpl<E>) column).setGridColumn(gridColumn);
    }

    protected ValueProvider getDefaultPresentationValueProvider(Column<E> column) {
        MetaProperty metaProperty = column.getPropertyPath() != null
                ? column.getPropertyPath().getMetaProperty()
                : null;

        if (column.getFormatter() != null) {
            //noinspection unchecked
            return new FormatterBasedValueProvider<>(column.getFormatter());
        } else if (metaProperty != null) {
            if (Collection.class.isAssignableFrom(metaProperty.getJavaType())) {
                new FormatterBasedValueProvider<>(new CollectionFormatter(metadataTools));
            }
            if (column.getType() == Boolean.class) {
                return new YesNoIconPresentationValueProvider();
            }
        }

        return new StringPresentationValueProvider(metaProperty, metadataTools);
    }

    protected com.vaadin.ui.renderers.Renderer getDefaultRenderer(Column<E> column) {
        MetaProperty metaProperty = column.getPropertyPath() != null
                ? column.getPropertyPath().getMetaProperty()
                : null;

        return column.getType() == Boolean.class && metaProperty != null
                ? new com.vaadin.ui.renderers.HtmlRenderer()
                : new com.vaadin.ui.renderers.TextRenderer();
    }

    protected void addColumnId(Grid.Column<E, ?> gridColumn, Column<E> column) {
        component.addColumnId(gridColumn.getState().internalId, column.getId());
    }

    protected void removeColumnId(Grid.Column<E, ?> gridColumn) {
        component.removeColumnId(gridColumn.getId());
    }

    @Override
    public void removeColumn(Column<E> column) {
        if (column == null) {
            return;
        }

        component.removeColumn(column.getId());

        columns.remove(column.getId());
        columnsOrder.remove(column);
        columnGenerators.remove(column.getId());

        ((ColumnImpl<E>) column).setGridColumn(null);
        column.setOwner(null);
    }

    @Override
    public void removeColumn(String id) {
        removeColumn(getColumn(id));
    }

    @Nullable
    @Override
    public DataGridSource<E> getDataGridSource() {
        return this.dataBinding != null ? this.dataBinding.getDataGridSource() : null;
    }

    protected DataGridSource<E> getDataGridSourceNN() {
        DataGridSource<E> dataGridSource = getDataGridSource();
        if (dataGridSource == null
                || dataGridSource.getState() == BindingState.INACTIVE) {
            throw new IllegalStateException("DataGridSource is not active");
        }
        return dataGridSource;
    }

    protected EntityDataGridSource<E> getEntityDataGridSource() {
        return getDataGridSource() != null ? (EntityDataGridSource<E>) getDataGridSource() : null;
    }

    protected EntityDataGridSource<E> getEntityDataGridSourceNN() {
        return (EntityDataGridSource<E>) getDataGridSourceNN();
    }

    @Override
    public void setDataGridSource(DataGridSource<E> dataGridSource) {
        if (dataGridSource != null && !(dataGridSource instanceof EntityDataGridSource)) {
            throw new IllegalArgumentException("DataGrid supports only EntityDataGridSource");
        }

        if (this.dataBinding != null) {
            this.dataBinding.unbind();
            this.dataBinding = null;

            this.component.setDataProvider(null);
        }

        if (dataGridSource != null) {
            // DataGrid supports only EntityDataGridSource
            EntityDataGridSource<E> entityDataGridSource = (EntityDataGridSource<E>) dataGridSource;

            if (this.columns.isEmpty()) {
                setupAutowiredColumns(entityDataGridSource);
            }

            // TEST: gg, do we need this?
//            component.removeAllColumns();

            // Bind new datasource
            this.dataBinding = createDataGridDataProvider(dataGridSource);
            this.component.setDataProvider(this.dataBinding);

            createStubsForGeneratedColumns();

            List<Column<E>> visibleColumnsOrder = getInitialVisibleColumns();

            setVisibleColumns(visibleColumnsOrder);

            for (Column<E> column : visibleColumnsOrder) {
                Grid.Column<E, ?> gridColumn = ((ColumnImpl<E>) column).getGridColumn();
                setupGridColumnProperties(gridColumn, column);
            }

            component.setColumnOrder(getColumnOrder());

            initShowInfoAction();

            if (rowsCount != null) {
                rowsCount.setRowsCountTarget(this);
            }

            if (!canBeSorted(dataGridSource)) {
                setSortable(false);
            }

            refreshActionsState();
        }
    }

    protected void setVisibleColumns(List<Column<E>> visibleColumnsOrder) {
        // mark columns hidden by security permissions as visible = false
        // and remove Grid.Column to prevent its property changing
        columnsOrder.stream()
                .filter(column -> !visibleColumnsOrder.contains(column))
                .forEach(column -> {
                    ColumnImpl<E> columnImpl = (ColumnImpl<E>) column;
                    columnImpl.setVisible(false);
                    columnImpl.setGridColumn(null);
                });
    }

    protected void setupAutowiredColumns(EntityDataGridSource<E> entityDataGridSource) {
        Collection<MetaPropertyPath> paths = entityDataGridSource.getAutowiredProperties();

        for (MetaPropertyPath metaPropertyPath : paths) {
            MetaProperty property = metaPropertyPath.getMetaProperty();
            if (!property.getRange().getCardinality().isMany()
                    && !metadataTools.isSystem(property)) {
                String propertyName = property.getName();
                ColumnImpl<E> column = new ColumnImpl<>(propertyName, metaPropertyPath, this);
                MetaClass propertyMetaClass = metadataTools.getPropertyEnclosingMetaClass(metaPropertyPath);
                column.setCaption(messageTools.getPropertyCaption(propertyMetaClass, propertyName));

                addColumn(column);
            }
        }
    }

    protected DataGridDataProvider<E> createDataGridDataProvider(DataGridSource<E> dataGridSource) {
        return (dataGridSource instanceof DataGridSource.Sortable)
                ? new SortableDataGridDataProvider<>((DataGridSource.Sortable<E>) dataGridSource, this)
                : new DataGridDataProvider<>(dataGridSource, this);
    }

    @Override
    public void dataGridSourceItemSetChanged(DataGridSource.ItemSetChangeEvent<E> event) {
        // #PL-2035, reload selection from ds
        Set<E> selectedItems = getSelected();
        Set<E> newSelection = new HashSet<>();
        for (E item : selectedItems) {
            //noinspection unchecked
            if (event.getSource().containsItem(item)) {
                newSelection.add(event.getSource().getItem(item.getId()));
            }
        }

        if (event.getSource().getState() == BindingState.ACTIVE
                && event.getSource().getSelectedItem() != null) {
            newSelection.add(event.getSource().getSelectedItem());
        }

        if (newSelection.isEmpty()) {
            setSelected((E) null);
        } else {
            // Workaround for the MultiSelect model.
            // Set the selected items only if the previous selection is different
            // Otherwise, the DataGrid rows will display the values before editing
            if (isMultiSelect() && !selectedItems.equals(newSelection)) {
                setSelectedItems(newSelection);
            }
        }

        refreshActionsState();
    }

    @Override
    public void dataGridSourcePropertyValueChanged(DataGridSource.ValueChangeEvent<E> event) {
        refreshActionsState();
    }

    @Override
    public void dataGridSourceStateChanged(DataGridSource.StateChangeEvent<E> event) {
        refreshActionsState();
    }

    @Override
    public void dataGridSourceSelectedItemChanged(DataGridSource.SelectedItemChangeEvent<E> event) {
        refreshActionsState();
    }

    protected String[] getColumnOrder() {
        //noinspection unchecked
        return columnsOrder.stream()
                .filter(Column::isVisible)
                .map(Column::getId)
                .toArray(String[]::new);
    }

    /**
     * Creates empty columns for columns with no property, i.e. generated columns with no value.
     */
    protected void createStubsForGeneratedColumns() {
        ValueProvider<E, Object> valueProvider = createEmptyValueProvider();
        for (Column<E> column : columnsOrder) {
            if (column.getPropertyPath() == null) {
                component.addColumn(valueProvider)
                        .setId(column.getId());
            }
        }
    }

    protected ValueProvider<E, Object> createEmptyValueProvider() {
        return (ValueProvider<E, Object>) e -> null;
    }

    protected void initShowInfoAction() {
        if (security.isSpecificPermitted(ShowInfoAction.ACTION_PERMISSION)) {
            if (getAction(ShowInfoAction.ACTION_ID) == null) {
                addAction(new ShowInfoAction());
            }
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
    public boolean isTextSelectionEnabled() {
        return textSelectionEnabled;
    }

    @Override
    public void setTextSelectionEnabled(boolean textSelectionEnabled) {
        if (this.textSelectionEnabled != textSelectionEnabled) {
            this.textSelectionEnabled = textSelectionEnabled;

            if (textSelectionEnabled) {
                if (!internalStyles.contains(TEXT_SELECTION_ENABLED_STYLE)) {
                    internalStyles.add(TEXT_SELECTION_ENABLED_STYLE);
                }

                componentComposition.addStyleName(TEXT_SELECTION_ENABLED_STYLE);
            } else {
                internalStyles.remove(TEXT_SELECTION_ENABLED_STYLE);
                componentComposition.removeStyleName(TEXT_SELECTION_ENABLED_STYLE);
            }
        }
    }

    @Override
    public boolean isColumnReorderingAllowed() {
        return component.isColumnReorderingAllowed();
    }

    @Override
    public void setColumnReorderingAllowed(boolean columnReorderingAllowed) {
        component.setColumnReorderingAllowed(columnReorderingAllowed);
    }

    @Override
    public boolean isSortable() {
        return sortable;
    }

    @Override
    public void setSortable(boolean sortable) {
        this.sortable = sortable
                && (getDataGridSource() == null
                || canBeSorted(getDataGridSource()));
        for (Column<E> column : getColumns()) {
            ((ColumnImpl<E>) column).updateSortable();
        }
    }

    @Override
    public boolean isColumnsCollapsingAllowed() {
        return columnsCollapsingAllowed;
    }

    @Override
    public void setColumnsCollapsingAllowed(boolean columnsCollapsingAllowed) {
        this.columnsCollapsingAllowed = columnsCollapsingAllowed;
        for (Column<E> column : getColumns()) {
            ((ColumnImpl<E>) column).updateCollapsible();
        }
    }

    protected Datasource createItemDatasource(Entity item) {
        EntityDataGridSource<E> entityDataGridSource = getEntityDataGridSourceNN();

        Datasource fieldDatasource = DsBuilder.create()
                .setAllowCommit(false)
                .setMetaClass(entityDataGridSource.getEntityMetaClass())
                .setRefreshMode(CollectionDatasource.RefreshMode.NEVER)
                .setViewName(View.LOCAL)
                .buildDatasource();

        ((DatasourceImplementation) fieldDatasource).valid();

        //noinspection unchecked
        fieldDatasource.setItem(item);

        return fieldDatasource;
    }

    @Override
    public boolean isEditorEnabled() {
        return component.getEditor().isEnabled();
    }

    @Override
    public void setEditorEnabled(boolean isEnabled) {
        component.getEditor().setEnabled(isEnabled);
    }

    @Override
    public boolean isEditorBuffered() {
        return component.getEditor().isBuffered();
    }

    @Override
    public void setEditorBuffered(boolean editorBuffered) {
        component.getEditor().setBuffered(editorBuffered);
    }

    @Override
    public String getEditorSaveCaption() {
        return component.getEditor().getSaveCaption();
    }

    @Override
    public void setEditorSaveCaption(String saveCaption) {
        component.getEditor().setSaveCaption(saveCaption);
    }

    @Override
    public String getEditorCancelCaption() {
        return component.getEditor().getCancelCaption();
    }

    @Override
    public void setEditorCancelCaption(String cancelCaption) {
        component.getEditor().setCancelCaption(cancelCaption);
    }

    @Nullable
    @Override
    public Object getEditedItemId() {
        E item = getEditedItem();
        return item != null ? item.getId() : null;
    }

    @Override
    public E getEditedItem() {
        return component.getEditor().getBinder().getBean();
    }

    @Override
    public boolean isEditorActive() {
        return component.getEditor().isOpen();
    }

    @Override
    public void editItem(Object itemId) {
        checkNotNullArgument(itemId, "Item's Id must be non null");

        DataGridSource<E> dataGridSource = getDataGridSource();
        if (dataGridSource == null
                || dataGridSource.getState() == BindingState.INACTIVE) {
            return;
        }

        E item = getDataGridSource().getItem(itemId);
        edit(item);
    }

    @Override
    public void edit(E item) {
        checkNotNullArgument(item, "Entity must be non null");

        DataGridSource<E> dataGridSource = getDataGridSource();
        if (dataGridSource == null
                || dataGridSource.getState() == BindingState.INACTIVE) {
            return;
        }

        if (!dataGridSource.containsItem(item)) {
            throw new IllegalArgumentException("Datasource doesn't contain item");
        }

        int rowIndex = dataGridSource.indexOfItem(item);
        component.getEditor().editRow(rowIndex);
    }

    @Override
    public void addEditorOpenListener(EditorOpenListener listener) {
        getEventRouter().addListener(EditorOpenListener.class, listener);
        // VAADIN8: gg, implement
//        if (beforeEditorOpenListener == null) {
//            beforeEditorOpenListener = event -> {
//                //noinspection ConstantConditions
//                Map<String, Field> fields = event.getColumnFieldMap().entrySet().stream()
//                        .filter(entry ->
//                                getColumnByGridColumn(entry.getKey()) != null)
//                        .collect(Collectors.toMap(
//                                entry -> getColumnByGridColumn(entry.getKey()).getId(),
//                                entry -> ((DataGridEditorCustomField) entry.getValue()).getField())
//                        );
//
//                EditorOpenEvent e = new EditorOpenEvent(WebDataGrid.this, event.getItem(), fields);
//                getEventRouter().fireEvent(EditorOpenListener.class, EditorOpenListener::beforeEditorOpened, e);
//            };
//            component.addEditorOpenListener(beforeEditorOpenListener);
//        }
    }

    @Override
    public void removeEditorOpenListener(EditorOpenListener listener) {
        getEventRouter().removeListener(EditorOpenListener.class, listener);
        // VAADIN8: gg, implement
//        if (!getEventRouter().hasListeners(EditorOpenListener.class)) {
//            component.removeEditorOpenListener(beforeEditorOpenListener);
//            beforeEditorOpenListener = null;
//        }
    }

    @Override
    public void addEditorCloseListener(EditorCloseListener listener) {
        getEventRouter().addListener(EditorCloseListener.class, listener);
        // VAADIN8: gg, implement
//        if (editorCloseListener == null) {
//            editorCloseListener = event -> {
//                EditorCloseEvent e = new EditorCloseEvent(WebDataGrid.this, event.getItem());
//                getEventRouter().fireEvent(EditorCloseListener.class, EditorCloseListener::editorClosed, e);
//            };
//            component.addEditorCloseListener(editorCloseListener);
//        }
    }

    @Override
    public void removeEditorCloseListener(EditorCloseListener listener) {
        getEventRouter().removeListener(EditorCloseListener.class, listener);
        // VAADIN8: gg, implement
//        if (!getEventRouter().hasListeners(EditorCloseListener.class)) {
//            component.removeEditorCloseListener(editorCloseListener);
//            editorCloseListener = null;
//        }
    }

    @Override
    public void addEditorPreCommitListener(EditorPreCommitListener listener) {
        getEventRouter().addListener(EditorPreCommitListener.class, listener);
        // VAADIN8: gg, implement
//        if (editorPreCommitListener == null) {
//            editorPreCommitListener = event -> {
//                EditorPreCommitEvent e = new EditorPreCommitEvent(WebDataGrid.this, event.getItem());
//                getEventRouter().fireEvent(EditorPreCommitListener.class, EditorPreCommitListener::preCommit, e);
//            };
//            component.addEditorPreCommitListener(editorPreCommitListener);
//        }
    }

    @Override
    public void removeEditorPreCommitListener(EditorPreCommitListener listener) {
        getEventRouter().removeListener(EditorPreCommitListener.class, listener);
        // VAADIN8: gg, implement
//        if (!getEventRouter().hasListeners(EditorPreCommitListener.class)) {
//            component.removeEditorPreCommitListener(editorPreCommitListener);
//            editorPreCommitListener = null;
//        }
    }

    @Override
    public void addEditorPostCommitListener(EditorPostCommitListener listener) {
        getEventRouter().addListener(EditorPostCommitListener.class, listener);
        // VAADIN8: gg, implement
//        if (editorPostCommitListener == null) {
//            editorPostCommitListener = event -> {
//                EditorPostCommitEvent e = new EditorPostCommitEvent(WebDataGrid.this, event.getItem());
//                getEventRouter().fireEvent(EditorPostCommitListener.class, EditorPostCommitListener::postCommit, e);
//            };
//            component.addEditorPostCommitListener(editorPostCommitListener);
//        }
    }

    @Override
    public void removeEditorPostCommitListener(EditorPostCommitListener listener) {
        getEventRouter().removeListener(EditorPostCommitListener.class, listener);
        // VAADIN8: gg, implement
//        if (!getEventRouter().hasListeners(EditorPostCommitListener.class)) {
//            component.removeEditorPostCommitListener(editorPostCommitListener);
//            editorPostCommitListener = null;
//        }
    }

    // VAADIN8: gg, implement
    /*protected static class WebDataGridEditorFieldFactory implements CubaGridEditorFieldFactory {

        protected WebDataGrid<?> dataGrid;
        protected DataGridEditorFieldFactory fieldFactory = AppBeans.get(DataGridEditorFieldFactory.NAME);

        public WebDataGridEditorFieldFactory(WebDataGrid dataGrid) {
            this.dataGrid = dataGrid;
        }

        @Nullable
        @Override
        public com.vaadin.v7.ui.Field<?> createField(Object itemId, Object propertyId) {
            Column column = dataGrid.getColumnByPropertyId(propertyId);
            if (column != null && !column.isEditable()) {
                return null;
            }

            //noinspection unchecked
            Entity entity = dataGrid.getDatasource().getItem(itemId);
            Datasource fieldDatasource = dataGrid.createItemDatasource(entity);
            String fieldPropertyId = String.valueOf(propertyId);

            Field columnComponent = column != null && column.getEditorFieldGenerator() != null
                    ? column.getEditorFieldGenerator().createField(fieldDatasource, fieldPropertyId)
                    : fieldFactory.createField(fieldDatasource, fieldPropertyId);
            columnComponent.setParent(dataGrid);
            columnComponent.setFrame(dataGrid.getFrame());

//            return createCustomField(columnComponent);
            return null;
        }

        protected CustomField createCustomField(final Field columnComponent) {
            if (!(columnComponent instanceof Buffered)) {
                throw new IllegalArgumentException("Editor field must implement " +
                        "com.haulmont.cuba.gui.components.Component.Buffered");
            }

            AbstractField<?> content = (AbstractField<?>) WebComponentsHelper.getComposition(columnComponent);

//            CustomField wrapper = new DataGridEditorCustomField(columnComponent) {
//                @Override
//                protected Component initContent() {
//                    return content;
//                }
//            };
//
            //noinspection unchecked
//            wrapper.setConverter(new ObjectToObjectConverter());
//            wrapper.setFocusDelegate(content);
//
//            wrapper.setReadOnly(content.isReadOnly());
//            wrapper.setRequired(content.isRequired());
//            wrapper.setRequiredError(content.getRequiredError());

//            columnComponent.addValueChangeListener(event -> wrapper.markAsDirty());
//
//            return wrapper;
            return null;
        }
    }*/

    // VAADIN8: gg, implement
    /*protected static abstract class DataGridEditorCustomField extends CustomField {

        protected Field columnComponent;

        public DataGridEditorCustomField(Field columnComponent) {
            this.columnComponent = columnComponent;
        }

        @Override
        protected AbstractField<?> getContent() {
            return (AbstractField<?>) super.getContent();
        }

        protected Field getField() {
            return columnComponent;
        }

        //        @Override
//        public Class getType() {
//            return Object.class;
//        }
//
//        @Override
//        protected void setInternalValue(Object newValue) {
//            columnComponent.setValue(newValue);
//        }
//
//        @Override
//        protected Object getInternalValue() {
//            return columnComponent.getValue();
//        }
//
        @Override
        public ErrorMessage getErrorMessage() {
//            try {
//                validate();
//            } catch (Validator.InvalidValueException ignore) {
//            }
            return getContent().getErrorMessage();
        }

//        @Override
//        public boolean isBuffered() {
//            return ((Buffered) columnComponent).isBuffered();
//        }
//
//        @Override
//        public void setBuffered(boolean buffered) {
//            ((Buffered) columnComponent).setBuffered(buffered);
//        }
//
//        @Override
//        public void commit() throws com.vaadin.v7.data.Buffered.SourceException, Validator.InvalidValueException {
//            validate();
//            ((Buffered) columnComponent).commit();
//        }
//
//        @Override
//        public void validate() throws Validator.InvalidValueException {
//            try {
//                columnComponent.validate();
//            } catch (ValidationException e) {
//                throw new Validator.InvalidValueException(e.getDetailsMessage());
//            }
//        }

        //        @Override
//        public void discard() throws com.vaadin.v7.data.Buffered.SourceException {
//            ((Buffered) columnComponent).discard();
//        }
//
//        @Override
//        public boolean isModified() {
//            return ((Buffered) columnComponent).isModified();
//        }
//
        @Override
        public void setWidth(float width, Unit unit) {
            super.setWidth(width, unit);

            if (getContent() != null) {
                if (width < 0) {
                    getContent().setWidthUndefined();
                } else {
                    getContent().setWidth(100, Unit.PERCENTAGE);
                }
            }
        }

        @Override
        public void setHeight(float height, Unit unit) {
            super.setHeight(height, unit);

            if (getContent() != null) {
                if (height < 0) {
                    getContent().setHeightUndefined();
                } else {
                    getContent().setHeight(100, Unit.PERCENTAGE);
                }
            }
        }
    }*/

    @Override
    public boolean isHeaderVisible() {
        return component.isHeaderVisible();
    }

    @Override
    public void setHeaderVisible(boolean headerVisible) {
        component.setHeaderVisible(headerVisible);
    }

    @Override
    public boolean isFooterVisible() {
        return component.isFooterVisible();
    }

    @Override
    public void setFooterVisible(boolean footerVisible) {
        component.setFooterVisible(footerVisible);
    }

    @Override
    public double getBodyRowHeight() {
        return component.getBodyRowHeight();
    }

    @Override
    public void setBodyRowHeight(double rowHeight) {
        component.setBodyRowHeight(rowHeight);
    }

    @Override
    public double getHeaderRowHeight() {
        return component.getHeaderRowHeight();
    }

    @Override
    public void setHeaderRowHeight(double rowHeight) {
        component.setHeaderRowHeight(rowHeight);
    }

    @Override
    public double getFooterRowHeight() {
        return component.getFooterRowHeight();
    }

    @Override
    public void setFooterRowHeight(double rowHeight) {
        component.setFooterRowHeight(rowHeight);
    }

    @Override
    public boolean isContextMenuEnabled() {
        return contextMenu.isEnabled();
    }

    @Override
    public void setContextMenuEnabled(boolean contextMenuEnabled) {
        contextMenu.setEnabled(contextMenuEnabled);
    }

    @Override
    public ColumnResizeMode getColumnResizeMode() {
        return WebWrapperUtils.convertToDataGridColumnResizeMode(component.getColumnResizeMode());
    }

    @Override
    public void setColumnResizeMode(ColumnResizeMode mode) {
        component.setColumnResizeMode(WebWrapperUtils.convertToGridColumnResizeMode(mode));
    }

    @Override
    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    @Override
    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        switch (selectionMode) {
            case SINGLE:
                component.setGridSelectionModel(new CubaSingleSelectionModel<>());
                break;
            case MULTI:
                component.setGridSelectionModel(new CubaMultiSelectionModel<>());
                break;
            case MULTI_CHECK:
                component.setGridSelectionModel(new CubaMultiCheckSelectionModel<>());
                break;
            case NONE:
                component.setSelectionMode(Grid.SelectionMode.NONE);
                return;
        }
        component.getSelectionModel().addSelectionListener(selectionListener);
    }

    @Override
    public boolean isMultiSelect() {
        return SelectionMode.MULTI.equals(selectionMode)
                || SelectionMode.MULTI_CHECK.equals(selectionMode);
    }

    @Nullable
    @Override
    public E getSingleSelected() {
        final Set<E> selectedItems = component.getSelectedItems();
        return CollectionUtils.isNotEmpty(selectedItems)
                ? selectedItems.iterator().next()
                : null;
    }

    @Override
    public Set<E> getSelected() {
        final Set<E> selectedItems = component.getSelectedItems();
        return selectedItems != null
                ? selectedItems
                : Collections.emptySet();
    }

    @Override
    public void setSelected(@Nullable E item) {
        if (getSelectionMode().equals(SelectionMode.NONE)) {
            return;
        }

        if (item == null) {
            component.deselectAll();
        } else {
            setSelected(Collections.singletonList(item));
        }
    }

    @Override
    public void setSelected(Collection<E> items) {
        DataGridSource<E> dataGridSource = getDataGridSourceNN();

        for (E item : items) {
            if (!dataGridSource.containsItem(item)) {
                throw new IllegalStateException("Datasource doesn't contain items");
            }
        }

        setSelectedItems(items);
    }

    @SuppressWarnings("unchecked")
    protected void setSelectedItems(Collection<E> items) {
        switch (selectionMode) {
            case SINGLE:
                if (items.size() > 0) {
                    E item = items.iterator().next();
                    component.getSelectionModel().select(item);
                } else {
                    component.deselectAll();
                }
                break;
            case MULTI:
            case MULTI_CHECK:
                component.deselectAll();
                ((SelectionModel.Multi) component.getSelectionModel()).selectItems(items.toArray());
                break;
        }
    }

    @Override
    public void selectAll() {
        if (isMultiSelect()) {
            ((SelectionModel.Multi) component.getSelectionModel()).selectAll();
        }
    }

    @Override
    public void deselect(E item) {
        checkNotNullArgument(item);

        component.deselect(item);
    }

    @Override
    public void deselectAll() {
        component.deselectAll();
    }

    @Override
    public void sort(String columnId, SortDirection direction) {
        ColumnImpl<E> column = (ColumnImpl<E>) getColumnNN(columnId);
        component.sort(column.getGridColumn(), WebWrapperUtils.convertToGridSortDirection(direction));
    }

    @Override
    public List<SortOrder> getSortOrder() {
        return convertToDataGridSortOrder(component.getSortOrder());
    }

    protected List<SortOrder> convertToDataGridSortOrder(List<GridSortOrder<E>> gridSortOrder) {
        if (CollectionUtils.isEmpty(gridSortOrder)) {
            return Collections.emptyList();
        }

        return gridSortOrder.stream()
                .map(sortOrder -> {
                    Column column = getColumnByGridColumn(sortOrder.getSorted());
                    return new SortOrder(column != null ? column.getId() : null,
                            WebWrapperUtils.convertToDataGridSortDirection(sortOrder.getDirection()));
                })
                .collect(Collectors.toList());
    }

    @Override
    public void addAction(Action action) {
        int index = findActionById(actionList, action.getId());
        if (index < 0) {
            index = actionList.size();
        }

        addAction(action, index);
    }

    @Override
    public void addAction(Action action, int index) {
        checkNotNullArgument(action, "Action must be non null");

        int oldIndex = findActionById(actionList, action.getId());
        if (oldIndex >= 0) {
            removeAction(actionList.get(oldIndex));
            if (index > oldIndex) {
                index--;
            }
        }

        if (StringUtils.isNotEmpty(action.getCaption())) {
            ActionMenuItemWrapper menuItemWrapper = createContextMenuItem(action);
            menuItemWrapper.setAction(action);
            contextMenuItems.add(menuItemWrapper);
        }

        actionList.add(index, action);
        shortcutsDelegate.addAction(null, action);
        attachAction(action);
        actionsPermissions.apply(action);
    }

    protected ActionMenuItemWrapper createContextMenuItem(Action action) {
        MenuItem menuItem = contextMenu.addItem(action.getCaption(), null);
        menuItem.setStyleName("c-cm-item");

        return new ActionMenuItemWrapper(menuItem, showIconsForPopupMenuActions) {
            @Override
            public void performAction(Action action) {
                action.actionPerform(WebAbstractDataGrid.this);
            }
        };
    }

    protected void attachAction(Action action) {
        if (action instanceof Action.HasTarget) {
            ((Action.HasTarget) action).setTarget(this);
        }

        action.refreshState();
    }

    @Override
    public void removeAction(@Nullable Action action) {
        if (actionList.remove(action)) {
            ActionMenuItemWrapper menuItemWrapper = null;
            for (ActionMenuItemWrapper menuItem : contextMenuItems) {
                if (menuItem.getAction() == action) {
                    menuItemWrapper = menuItem;
                    break;
                }
            }

            if (menuItemWrapper != null) {
                menuItemWrapper.setAction(null);
                contextMenu.removeItem(menuItemWrapper.getMenuItem());
            }

            shortcutsDelegate.removeAction(action);
        }
    }

    @Override
    public void removeAction(@Nullable String id) {
        Action action = getAction(id);
        if (action != null) {
            removeAction(action);
        }
    }

    @Override
    public void removeAllActions() {
        for (Action action : actionList.toArray(new Action[0])) {
            removeAction(action);
        }
    }

    @Override
    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actionList);
    }

    @Nullable
    @Override
    public Action getAction(String id) {
        for (Action action : getActions()) {
            if (Objects.equals(action.getId(), id)) {
                return action;
            }
        }
        return null;
    }

    @Override
    public ActionsPermissions getActionsPermissions() {
        return actionsPermissions;
    }

    protected List<Column<E>> getInitialVisibleColumns() {
        EntityDataGridSource<E> entityDataGridSource = getEntityDataGridSource();
        if (entityDataGridSource == null
                || entityDataGridSource.getState() == BindingState.INACTIVE) {
            return Collections.emptyList();
        }

        MetaClass metaClass = entityDataGridSource.getEntityMetaClass();
        return columnsOrder.stream()
                .filter(column -> {
                    MetaPropertyPath propertyPath = column.getPropertyPath();
                    return propertyPath == null
                            || security.isEntityAttrReadPermitted(metaClass, propertyPath.toString());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Component getComposition() {
        return componentComposition;
    }

    @Override
    public int getFrozenColumnCount() {
        return component.getFrozenColumnCount();
    }

    @Override
    public void setFrozenColumnCount(int numberOfColumns) {
        component.setFrozenColumnCount(numberOfColumns);
    }

    @Override
    public void scrollTo(E item) {
        scrollTo(item, ScrollDestination.ANY);
    }

    @Override
    public void scrollTo(E item, ScrollDestination destination) {
        Preconditions.checkNotNullArgument(item);
        Preconditions.checkNotNullArgument(destination);

        DataGridSource<E> dataGridSource = getDataGridSourceNN();
        if (!dataGridSource.containsItem(item)) {
            throw new IllegalArgumentException("Unable to find item in DataGrid");
        }

        int rowIndex = dataGridSource.indexOfItem(item);
        component.scrollTo(rowIndex, WebWrapperUtils.convertToGridScrollDestination(destination));
    }

    @Override
    public void scrollToStart() {
        component.scrollToStart();
    }

    @Override
    public void scrollToEnd() {
        component.scrollToEnd();
    }

    @Override
    public void repaint() {
        component.repaint();
    }

    protected boolean canBeSorted(@Nullable DataGridSource<E> dataGridSource) {
        return dataGridSource instanceof DataGridSource.Sortable;
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

        if (id != null && AppUI.getCurrent().isTestMode()) {
            componentComposition.setCubaId(id + "_composition");
        }
    }

    @Override
    public void setStyleName(String name) {
        super.setStyleName(name);

        internalStyles.forEach(internalStyle ->
                componentComposition.addStyleName(internalStyle));
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
                topPanel = createTopPanel();
                topPanel.setWidth("100%");
                componentComposition.addComponentAsFirst(topPanel);
            }
            topPanel.addComponent(WebComponentsHelper.unwrap(panel));
            if (panel instanceof VisibilityChangeNotifier) {
                ((VisibilityChangeNotifier) panel).addVisibilityChangeListener(event ->
                        updateCompositionStylesTopPanelVisible()
                );
            }
            panel.setParent(this);
        }

        updateCompositionStylesTopPanelVisible();
    }

    @Override
    public RowsCount getRowsCount() {
        return rowsCount;
    }

    @Override
    public void setRowsCount(RowsCount rowsCount) {
        if (this.rowsCount != null && topPanel != null) {
            topPanel.removeComponent(WebComponentsHelper.unwrap(this.rowsCount));
            this.rowsCount.setParent(null);
        }
        this.rowsCount = rowsCount;
        if (rowsCount != null) {
            if (rowsCount.getParent() != null && rowsCount.getParent() != this) {
                throw new IllegalStateException("Component already has parent");
            }

            if (topPanel == null) {
                topPanel = createTopPanel();
                topPanel.setWidth("100%");
                componentComposition.addComponentAsFirst(topPanel);
            }
            Component rc = WebComponentsHelper.unwrap(rowsCount);
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

    protected HorizontalLayout createTopPanel() {
        HorizontalLayout topPanel = new HorizontalLayout();
        topPanel.setMargin(false);
        topPanel.setSpacing(false);
        topPanel.setStyleName("c-data-grid-top");
        return topPanel;
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
                componentComposition.removeStyleName(HAS_TOP_PANEL_STYLE_NAME);

                internalStyles.remove(HAS_TOP_PANEL_STYLE_NAME);
            } else {
                componentComposition.addStyleName(HAS_TOP_PANEL_STYLE_NAME);

                if (!internalStyles.contains(HAS_TOP_PANEL_STYLE_NAME)) {
                    internalStyles.add(HAS_TOP_PANEL_STYLE_NAME);
                }
            }
        }
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
    public void applySettings(Element element) {
        if (!isSettingsEnabled()) {
            return;
        }

        final Element columnsElem = element.element("columns");
        if (columnsElem != null) {
            List<Column<E>> modelColumns = getVisibleColumns();
            List<String> modelIds = modelColumns.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());

            List<String> loadedIds = Dom4j.elements(columnsElem, "columns").stream()
                    .map(colElem -> colElem.attributeValue("id"))
                    .collect(Collectors.toList());

            if (CollectionUtils.isEqualCollection(modelIds, loadedIds)) {
                applyColumnSettings(element, modelColumns);
            }
        }
    }

    protected void applyColumnSettings(Element element, Collection<Column<E>> oldColumns) {
        final Element columnsElem = element.element("columns");

        List<Column<E>> newColumns = new ArrayList<>();

        // add columns from saved settings
        for (Element colElem : Dom4j.elements(columnsElem, "columns")) {
            for (Column<E> column : oldColumns) {
                if (column.getId().equals(colElem.attributeValue("id"))) {
                    newColumns.add(column);

                    String width = colElem.attributeValue("width");
                    if (width != null) {
                        column.setWidth(Double.parseDouble(width));
                    } else {
                        column.setWidthAuto();
                    }

                    String collapsed = colElem.attributeValue("collapsed");
                    if (collapsed != null && component.isColumnReorderingAllowed()) {
                        column.setCollapsed(Boolean.parseBoolean(collapsed));
                    }

                    break;
                }
            }
        }

        // add columns not saved in settings (perhaps new)
        for (Column<E> column : oldColumns) {
            if (!newColumns.contains(column)) {
                newColumns.add(column);
            }
        }

        // if the data grid contains only one column, always show it
        if (newColumns.size() == 1) {
            newColumns.get(0).setCollapsed(false);
        }

        // We don't save settings for columns hidden by security permissions,
        // so we need to return them back to they initial positions
        columnsOrder = restoreColumnsOrder(newColumns);
        component.setColumnOrder(newColumns.stream()
                .map(Column::getId)
                .toArray(String[]::new));

        if (isSortable()) {
            // apply sorting
            component.clearSortOrder();
            String sortColumnId = columnsElem.attributeValue("sortColumnId");
            if (!StringUtils.isEmpty(sortColumnId)) {
                Grid.Column<E, ?> column = component.getColumn(sortColumnId);
                if (column != null) {
                    String sortDirection = columnsElem.attributeValue("sortDirection");
                    if (StringUtils.isNotEmpty(sortDirection)) {
                        List<GridSortOrder<E>> sortOrders = Collections.singletonList(new GridSortOrder<>(column,
                                com.vaadin.shared.data.sort.SortDirection.valueOf(sortDirection))
                        );
                        component.setSortOrder(sortOrders);
                    }
                }
            }
        }
    }

    @Override
    public boolean saveSettings(Element element) {
        if (!isSettingsEnabled()) {
            return false;
        }

        Element columnsElem = element.element("columns");
        if (columnsElem != null) {
            element.remove(columnsElem);
        }
        columnsElem = element.addElement("columns");

        List<Column<E>> visibleColumns = getVisibleColumns();
        for (Column<E> column : visibleColumns) {
            Element colElem = columnsElem.addElement("columns");
            colElem.addAttribute("id", column.toString());

            double width = column.getWidth();
            if (width > -1) {
                colElem.addAttribute("width", String.valueOf(width));
            }

            colElem.addAttribute("collapsed", Boolean.toString(column.isCollapsed()));
        }

        List<GridSortOrder<E>> sortOrders = component.getSortOrder();
        if (!sortOrders.isEmpty()) {
            GridSortOrder<E> sortOrder = sortOrders.get(0);
            columnsElem.addAttribute("sortColumnId", sortOrder.getSorted().getId());
            columnsElem.addAttribute("sortDirection", sortOrder.getDirection().toString());
        }

        return true;
    }

    @Nullable
    protected Column<E> getColumnByGridColumn(Grid.Column<E, ?> gridColumn) {
        for (Column<E> column : getColumns()) {
            if (((ColumnImpl<E>) column).getGridColumn() == gridColumn) {
                return column;
            }
        }
        return null;
    }

    @Nullable
    protected Column<E> getColumnById(Object id) {
        for (Column<E> column : getColumns()) {
            String columnId = column.getId();
            if (Objects.equals(columnId, id)) {
                return column;
            }
        }
        return null;
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
    public void addRowStyleProvider(StyleProvider<? super E> styleProvider) {
        if (this.rowStyleProviders == null) {
            this.rowStyleProviders = new LinkedList<>();
        }

        if (!this.rowStyleProviders.contains(styleProvider)) {
            this.rowStyleProviders.add(styleProvider);

            repaint();
        }
    }

    @Override
    public void removeRowStyleProvider(StyleProvider<? super E> styleProvider) {
        if (this.rowStyleProviders != null) {
            if (this.rowStyleProviders.remove(styleProvider)) {
                repaint();
            }
        }
    }

    @Override
    public void addCellStyleProvider(CellStyleProvider<? super E> styleProvider) {
        if (this.cellStyleProviders == null) {
            this.cellStyleProviders = new LinkedList<>();
        }

        if (!this.cellStyleProviders.contains(styleProvider)) {
            this.cellStyleProviders.add(styleProvider);

            repaint();
        }
    }

    @Override
    public void removeCellStyleProvider(CellStyleProvider<? super E> styleProvider) {
        if (this.cellStyleProviders != null) {
            if (this.cellStyleProviders.remove(styleProvider)) {
                repaint();
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public CellDescriptionProvider<E> getCellDescriptionProvider() {
        return (CellDescriptionProvider<E>) cellDescriptionProvider;
    }

    @Override
    public void setCellDescriptionProvider(CellDescriptionProvider<? super E> provider) {
        this.cellDescriptionProvider = provider;
        repaint();
    }

    @SuppressWarnings("unchecked")
    @Override
    public DescriptionProvider<E> getRowDescriptionProvider() {
        return (DescriptionProvider<E>) rowDescriptionProvider;
    }

    @Override
    public void setRowDescriptionProvider(DescriptionProvider<? super E> provider) {
        setRowDescriptionProvider(provider, ContentMode.PREFORMATTED);
    }

    @Override
    public void setRowDescriptionProvider(DescriptionProvider<? super E> provider, ContentMode contentMode) {
        this.rowDescriptionProvider = provider;

        if (provider != null) {
            component.setDescriptionGenerator(createRowDescriptionGenerator(),
                    WebWrapperUtils.toVaadinContentMode(contentMode));
        } else {
            component.setDescriptionGenerator(null);
        }
    }

    protected DescriptionGenerator<E> createRowDescriptionGenerator() {
        return item ->
                rowDescriptionProvider.getDescription(item);
    }

    @Override
    public Column<E> addGeneratedColumn(String columnId, ColumnGenerator<E, ?> generator) {
        return addGeneratedColumn(columnId, generator, columnsOrder.size());
    }

    @Override
    public Column<E> addGeneratedColumn(String columnId, ColumnGenerator<E, ?> generator, int index) {
        checkNotNullArgument(columnId, "columnId is null");
        checkNotNullArgument(generator, "generator is null for column id '%s'", columnId);

        Column<E> existingColumn = getColumn(columnId);
        if (existingColumn != null) {
            index = columnsOrder.indexOf(existingColumn);
            removeColumn(existingColumn);
        }

        Grid.Column<E, Object> generatedColumn =
                component.addColumn(createGeneratedColumnValueProvider(columnId, generator));

        ColumnImpl<E> column = new ColumnImpl<>(columnId, generator.getType(), this);
        if (existingColumn != null) {
            copyColumnProperties(column, existingColumn);
        } else {
            column.setCaption(columnId);
        }
        column.setGenerated(true);

        columns.put(column.getId(), column);
        columnsOrder.add(index, column);
        columnGenerators.put(column.getId(), generator);

        setupGridColumnProperties(generatedColumn, column);

        component.setColumnOrder(getColumnOrder());

        return column;
    }

    protected ValueProvider<E, Object> createGeneratedColumnValueProvider(String columnId,
                                                                          ColumnGenerator<E, ?> generator) {
        return (ValueProvider<E, Object>) item -> {
            ColumnGeneratorEvent<E> event = new ColumnGeneratorEvent<>(WebAbstractDataGrid.this, item, columnId);
            return generator.getValue(event);
        };
    }

    @Override
    public ColumnGenerator<E, ?> getColumnGenerator(String columnId) {
        return columnGenerators.get(columnId);
    }

    protected void copyColumnProperties(Column<E> column, Column<E> existingColumn) {
        column.setCaption(existingColumn.getCaption());
        column.setVisible(existingColumn.isVisible());
        column.setCollapsed(existingColumn.isCollapsed());
        column.setCollapsible(existingColumn.isCollapsible());
        column.setCollapsingToggleCaption(existingColumn.getCollapsingToggleCaption());
        column.setMinimumWidth(existingColumn.getMinimumWidth());
        column.setMaximumWidth(existingColumn.getMaximumWidth());
        column.setWidth(existingColumn.getWidth());
        column.setExpandRatio(existingColumn.getExpandRatio());
        column.setResizable(existingColumn.isResizable());
        //noinspection unchecked
        column.setFormatter(existingColumn.getFormatter());
        column.setStyleProvider(existingColumn.getStyleProvider());
        column.setDescriptionProvider(existingColumn.getDescriptionProvider(),
                ((ColumnImpl) existingColumn).getDescriptionContentMode());
    }

    @Override
    public <T extends Renderer> T createRenderer(Class<T> type) {
        Class<? extends Renderer> rendererClass = rendererClasses.get(type);
        if (rendererClass == null) {
            throw new IllegalArgumentException(
                    String.format("Can't find renderer class for '%s'", type.getTypeName()));
        }

        try {
            return type.cast(rendererClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(String.format("Error creating the '%s' renderer instance",
                    type.getTypeName()), e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addColumnCollapsingChangeListener(ColumnCollapsingChangeListener listener) {
        getEventRouter().addListener(ColumnCollapsingChangeListener.class, listener);

        if (columnCollapsingChangeListenerRegistration == null) {
            columnCollapsingChangeListenerRegistration =
                    component.addColumnVisibilityChangeListener((ColumnVisibilityChangeListener) e -> {
                        if (e.isUserOriginated()) {
                            ColumnCollapsingChangeEvent event = new ColumnCollapsingChangeEvent(WebAbstractDataGrid.this,
                                    getColumnByGridColumn((Grid.Column<E, ?>) e.getColumn()), e.isHidden());
                            getEventRouter().fireEvent(ColumnCollapsingChangeListener.class,
                                    ColumnCollapsingChangeListener::columnCollapsingChanged, event);
                        }
                    });
        }
    }

    @Override
    public void removeColumnCollapsingChangeListener(ColumnCollapsingChangeListener listener) {
        getEventRouter().removeListener(ColumnCollapsingChangeListener.class, listener);

        if (!getEventRouter().hasListeners(ColumnCollapsingChangeListener.class)) {
            columnCollapsingChangeListenerRegistration.remove();
            columnCollapsingChangeListenerRegistration = null;
        }
    }

    @Override
    public void addColumnReorderListener(ColumnReorderListener listener) {
        getEventRouter().addListener(ColumnReorderListener.class, listener);
    }

    @Override
    public void removeColumnReorderListener(ColumnReorderListener listener) {
        getEventRouter().removeListener(ColumnReorderListener.class, listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addColumnResizeListener(ColumnResizeListener listener) {
        getEventRouter().addListener(ColumnResizeListener.class, listener);

        if (columnResizeListenerRegistration == null) {
            columnResizeListenerRegistration =
                    component.addColumnResizeListener((com.vaadin.ui.components.grid.ColumnResizeListener) e -> {
                        ColumnResizeEvent event = new ColumnResizeEvent(WebAbstractDataGrid.this,
                                getColumnByGridColumn((Grid.Column<E, ?>) e.getColumn()));
                        getEventRouter().fireEvent(ColumnResizeListener.class,
                                ColumnResizeListener::columnResized, event);
                    });
        }
    }

    @Override
    public void removeColumnResizeListener(ColumnResizeListener listener) {
        getEventRouter().removeListener(ColumnResizeListener.class, listener);

        if (!getEventRouter().hasListeners(ColumnResizeListener.class)) {
            columnResizeListenerRegistration.remove();
            columnResizeListenerRegistration = null;
        }
    }

    @Override
    public void addSortListener(SortListener listener) {
        getEventRouter().addListener(SortListener.class, listener);

        if (sortListenerRegistration == null) {
            sortListenerRegistration =
                    component.addSortListener((com.vaadin.event.SortEvent.SortListener<GridSortOrder<E>>) e -> {
                        if (e.isUserOriginated()) {
                            List<SortOrder> sortOrders = convertToDataGridSortOrder(e.getSortOrder());

                            SortEvent event = new SortEvent(WebAbstractDataGrid.this, sortOrders);
                            getEventRouter().fireEvent(SortListener.class, SortListener::sorted, event);
                        }
                    });
        }
    }

    @Override
    public void removeSortListener(SortListener listener) {
        getEventRouter().removeListener(SortListener.class, listener);

        if (!getEventRouter().hasListeners(SortListener.class)) {
            sortListenerRegistration.remove();
            sortListenerRegistration = null;
        }
    }

    @Override
    public void addContextClickListener(ContextClickListener listener) {
        getEventRouter().addListener(ContextClickListener.class, listener);

        if (contextClickListenerRegistration == null) {
            contextClickListenerRegistration =
                    component.addContextClickListener(e -> {
                        MouseEventDetails mouseEventDetails = WebWrapperUtils.toMouseEventDetails(e);

                        ContextClickEvent event = new ContextClickEvent(WebAbstractDataGrid.this, mouseEventDetails);
                        getEventRouter().fireEvent(ContextClickListener.class,
                                ContextClickListener::onContextClick, event);
                    });
        }
    }

    @Override
    public void removeContextClickListener(ContextClickListener listener) {
        getEventRouter().removeListener(ContextClickListener.class, listener);

        if (!getEventRouter().hasListeners(ContextClickListener.class)) {
            contextClickListenerRegistration.remove();
            contextClickListenerRegistration = null;
        }
    }

    @Override
    public void addItemClickListener(ItemClickListener<E> listener) {
        getEventRouter().addListener(ItemClickListener.class, listener);
    }

    @Override
    public void removeItemClickListener(ItemClickListener<E> listener) {
        getEventRouter().removeListener(ItemClickListener.class, listener);
    }

    @Override
    public HeaderRow getHeaderRow(int index) {
        return getHeaderRowByGridRow(component.getHeaderRow(index));
    }

    @Nullable
    protected HeaderRow getHeaderRowByGridRow(com.vaadin.ui.components.grid.HeaderRow gridRow) {
        for (HeaderRow headerRow : headerRows) {
            if (((HeaderRowImpl) headerRow).getGridRow() == gridRow) {
                return headerRow;
            }
        }
        return null;
    }

    @Override
    public HeaderRow appendHeaderRow() {
        com.vaadin.ui.components.grid.HeaderRow headerRow = component.appendHeaderRow();
        return addHeaderRowInternal(headerRow);
    }

    @Override
    public HeaderRow prependHeaderRow() {
        com.vaadin.ui.components.grid.HeaderRow headerRow = component.prependHeaderRow();
        return addHeaderRowInternal(headerRow);
    }

    @Override
    public HeaderRow addHeaderRowAt(int index) {
        com.vaadin.ui.components.grid.HeaderRow headerRow = component.addHeaderRowAt(index);
        return addHeaderRowInternal(headerRow);
    }

    protected HeaderRow addHeaderRowInternal(com.vaadin.ui.components.grid.HeaderRow headerRow) {
        HeaderRowImpl rowImpl = new HeaderRowImpl(this, (Header.Row) headerRow);
        headerRows.add(rowImpl);
        return rowImpl;
    }

    @Override
    public void removeHeaderRow(HeaderRow headerRow) {
        if (headerRow == null || !headerRows.contains(headerRow)) {
            return;
        }

        component.removeHeaderRow(((HeaderRowImpl) headerRow).getGridRow());
        headerRows.remove(headerRow);
    }

    @Override
    public void removeHeaderRow(int index) {
        HeaderRow headerRow = getHeaderRow(index);
        removeHeaderRow(headerRow);
    }

    @Override
    public HeaderRow getDefaultHeaderRow() {
        return getHeaderRowByGridRow(component.getDefaultHeaderRow());
    }

    @Override
    public void setDefaultHeaderRow(HeaderRow headerRow) {
        component.setDefaultHeaderRow(((HeaderRowImpl) headerRow).getGridRow());
    }

    @Override
    public int getHeaderRowCount() {
        return component.getHeaderRowCount();
    }

    @Override
    public FooterRow getFooterRow(int index) {
        return getFooterRowByGridRow(component.getFooterRow(index));
    }

    @Nullable
    protected FooterRow getFooterRowByGridRow(com.vaadin.ui.components.grid.FooterRow gridRow) {
        for (FooterRow footerRow : footerRows) {
            if (((FooterRowImpl) footerRow).getGridRow() == gridRow) {
                return footerRow;
            }
        }
        return null;
    }

    @Override
    public FooterRow appendFooterRow() {
        com.vaadin.ui.components.grid.FooterRow footerRow = component.appendFooterRow();
        return addFooterRowInternal(footerRow);
    }

    @Override
    public FooterRow prependFooterRow() {
        com.vaadin.ui.components.grid.FooterRow footerRow = component.prependFooterRow();
        return addFooterRowInternal(footerRow);
    }

    @Override
    public FooterRow addFooterRowAt(int index) {
        com.vaadin.ui.components.grid.FooterRow footerRow = component.addFooterRowAt(index);
        return addFooterRowInternal(footerRow);
    }

    protected FooterRow addFooterRowInternal(com.vaadin.ui.components.grid.FooterRow footerRow) {
        FooterRowImpl rowImpl = new FooterRowImpl(this, (Footer.Row) footerRow);
        footerRows.add(rowImpl);
        return rowImpl;
    }

    @Override
    public void removeFooterRow(FooterRow footerRow) {
        if (footerRow == null || !footerRows.contains(footerRow)) {
            return;
        }

        component.removeFooterRow(((FooterRowImpl) footerRow).getGridRow());
        footerRows.remove(footerRow);
    }

    @Override
    public void removeFooterRow(int index) {
        FooterRow footerRow = getFooterRow(index);
        removeFooterRow(footerRow);
    }

    @Override
    public int getFooterRowCount() {
        return component.getFooterRowCount();
    }

    @Override
    public void addSelectionListener(SelectionListener<E> listener) {
        getEventRouter().addListener(SelectionListener.class, listener);
    }

    @Override
    public void removeSelectionListener(SelectionListener<E> listener) {
        getEventRouter().removeListener(SelectionListener.class, listener);
    }

    @Nullable
    @Override
    public DetailsGenerator<E> getDetailsGenerator() {
        return detailsGenerator;
    }

    @Override
    public void setDetailsGenerator(DetailsGenerator<E> detailsGenerator) {
        this.detailsGenerator = detailsGenerator;
        component.setDetailsGenerator(detailsGenerator != null ? createDetailsGenerator() : null);
    }

    protected com.vaadin.ui.components.grid.DetailsGenerator<E> createDetailsGenerator() {
        return (com.vaadin.ui.components.grid.DetailsGenerator<E>) item -> {
            com.haulmont.cuba.gui.components.Component component = detailsGenerator.getDetails(item);
            return component != null ? component.unwrapComposition(Component.class) : null;
        };
    }

    @Override
    public boolean isDetailsVisible(E entity) {
        return component.isDetailsVisible(entity);
    }

    @Override
    public void setDetailsVisible(E entity, boolean visible) {
        component.setDetailsVisible(entity, visible);
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    @Override
    public void addLookupValueChangeListener(LookupSelectionChangeListener listener) {
        getEventRouter().addListener(LookupSelectionChangeListener.class, listener);
    }

    @Override
    public void removeLookupValueChangeListener(LookupSelectionChangeListener listener) {
        getEventRouter().removeListener(LookupSelectionChangeListener.class, listener);
    }

    protected class RowStyleGeneratorAdapter<T extends E> implements StyleGenerator<T> {
        @Override
        public String apply(T item) {
            return getGeneratedRowStyle(item);
        }
    }

    @Nullable
    protected String getGeneratedRowStyle(E item) {
        if (rowStyleProviders == null) {
            return null;
        }

        StringBuilder joinedStyle = null;
        for (StyleProvider<? super E> styleProvider : rowStyleProviders) {
            String styleName = styleProvider.getStyleName(item);
            if (styleName != null) {
                if (joinedStyle == null) {
                    joinedStyle = new StringBuilder(styleName);
                } else {
                    joinedStyle.append(" ").append(styleName);
                }
            }
        }

        return joinedStyle != null ? joinedStyle.toString() : null;
    }

    protected class CellStyleGeneratorAdapter<T extends E> implements StyleGenerator<T> {

        protected Column<T> column;

        public CellStyleGeneratorAdapter(Column<T> column) {
            this.column = column;
        }

        @Override
        public String apply(T item) {
            //noinspection unchecked
            return getGeneratedCellStyle(item, (Column<E>) column);
        }
    }

    @Nullable
    protected String getGeneratedCellStyle(E item, Column<E> column) {
        StringBuilder joinedStyle = null;

        if (column.getStyleProvider() != null) {
            String styleName = column.getStyleProvider().getStyleName(item);
            if (styleName != null) {
                joinedStyle = new StringBuilder(styleName);
            }
        }

        if (cellStyleProviders != null) {
            for (CellStyleProvider<? super E> styleProvider : cellStyleProviders) {
                String styleName = styleProvider.getStyleName(item, column.getId());
                if (styleName != null) {
                    if (joinedStyle == null) {
                        joinedStyle = new StringBuilder(styleName);
                    } else {
                        joinedStyle.append(" ").append(styleName);
                    }
                }
            }
        }

        return joinedStyle != null ? joinedStyle.toString() : null;
    }

    protected class CellDescriptionGeneratorAdapter<T extends E> implements DescriptionGenerator<T> {
        protected Column<T> column;

        public CellDescriptionGeneratorAdapter(Column<T> column) {
            this.column = column;
        }

        @Override
        public String apply(T item) {
            //noinspection unchecked
            return getGeneratedCellDescription(item, (Column<E>) column);
        }
    }

    protected String getGeneratedCellDescription(E item, Column<E> column) {
        if (column.getDescriptionProvider() != null) {
            return column.getDescriptionProvider().getDescription(item);
        }

        if (cellDescriptionProvider != null) {
            return cellDescriptionProvider.getDescription(item, column.getId());
        }

        return null;
    }

    public static abstract class AbstractRenderer<T extends Entity, V> implements RendererWrapper<V> {
        protected com.vaadin.ui.renderers.Renderer<V> renderer;
        protected WebAbstractDataGrid<?, T> dataGrid;
        protected String nullRepresentation;

        protected AbstractRenderer() {
        }

        protected AbstractRenderer(String nullRepresentation) {
            this.nullRepresentation = nullRepresentation;
        }

        @Override
        public com.vaadin.ui.renderers.Renderer<V> getImplementation() {
            if (renderer == null) {
                renderer = createImplementation();
            }
            return renderer;
        }

        protected abstract com.vaadin.ui.renderers.Renderer<V> createImplementation();

        public ValueProvider<?, V> getPresentationValueProvider() {
            // Some renderers need specific presentation ValueProvider to be set at the same time
            // (see com.vaadin.ui.Grid.Column.setRenderer(com.vaadin.data.ValueProvider<V,P>,
            //          com.vaadin.ui.renderers.Renderer<? super P>)).
            // Default `null` means do not use any presentation ValueProvider
            return null;
        }

        @Override
        public void resetImplementation() {
            renderer = null;
        }

        protected WebAbstractDataGrid<?, T> getDataGrid() {
            return dataGrid;
        }

        protected void setDataGrid(WebAbstractDataGrid<?, T> dataGrid) {
            this.dataGrid = dataGrid;
        }

        protected String getNullRepresentation() {
            return nullRepresentation;
        }

        protected void setNullRepresentation(String nullRepresentation) {
            checkRendererNotSet();
            this.nullRepresentation = nullRepresentation;
        }

        protected Column<T> getColumnByGridColumn(Grid.Column<T, ?> column) {
            return dataGrid.getColumnByGridColumn(column);
        }

        protected void checkRendererNotSet() {
            if (renderer != null) {
                throw new IllegalStateException("Renderer parameters cannot be changed after it is set to a column");
            }
        }
    }

    protected static class ColumnImpl<E extends Entity> implements Column<E> {

        protected final String id;
        protected final MetaPropertyPath propertyPath;

        protected String caption;
        protected String collapsingToggleCaption;
        protected double width;
        protected double minWidth;
        protected double maxWidth;
        protected int expandRatio;
        protected boolean collapsed;
        protected boolean visible;
        protected boolean collapsible;
        protected boolean sortable;
        protected boolean resizable;
        protected boolean editable;
        protected boolean generated;
        protected Formatter formatter;

        protected AbstractRenderer<E, ?> renderer;
        protected Function presentationProvider;
        protected Converter converter;

        protected StyleProvider<? super E> styleProvider;
        protected DescriptionProvider<? super E> descriptionProvider;
        protected ContentMode descriptionContentMode = ContentMode.PREFORMATTED;

        protected final Class type;
        protected Element element;

        protected WebAbstractDataGrid<?, E> owner;
        protected Grid.Column<E, ?> gridColumn;

        protected ColumnEditorFieldGenerator fieldGenerator;

        public ColumnImpl(String id, @Nullable MetaPropertyPath propertyPath, WebAbstractDataGrid<?, E> owner) {
            this(id, propertyPath, propertyPath != null ? propertyPath.getRangeJavaClass() : String.class, owner);
        }

        public ColumnImpl(String id, Class type, WebAbstractDataGrid<?, E> owner) {
            this(id, null, type, owner);
        }

        protected ColumnImpl(String id,
                             @Nullable MetaPropertyPath propertyPath, Class type, WebAbstractDataGrid<?, E> owner) {
            this.id = id;
            this.propertyPath = propertyPath;
            this.type = type;
            this.owner = owner;

            setupDefaults();
        }

        protected void setupDefaults() {
            resizable = true;
            editable = true;
            generated = false;
            // the generated properties are not normally sortable,
            // but require special handling to enable sorting.
            // for now sorting for generated properties is disabled
            sortable = propertyPath != null && owner.isSortable();
            collapsible = owner.isColumnsCollapsingAllowed();
            collapsed = false;

            visible = true;

            width = -1;
            maxWidth = -1;
            minWidth = 10;
            expandRatio = -1;

            ThemeConstants theme = App.getInstance().getThemeConstants();
            width = theme.getInt("cuba.web.DataGrid.defaultColumnWidth");
            maxWidth = theme.getInt("cuba.web.DataGrid.defaultColumnMaxWidth");
            minWidth = theme.getInt("cuba.web.DataGrid.defaultColumnMinWidth");
            expandRatio = theme.getInt("cuba.web.DataGrid.defaultColumnExpandRatio");
        }

        @Override
        public String getId() {
            if (gridColumn != null) {
                return gridColumn.getId();
            }
            return id;
        }

        @Nullable
        @Override
        public MetaPropertyPath getPropertyPath() {
            return propertyPath;
        }

        @Override
        public String getCaption() {
            if (gridColumn != null) {
                return gridColumn.getCaption();
            }
            return caption;
        }

        @Override
        public void setCaption(String caption) {
            this.caption = caption;
            if (gridColumn != null) {
                gridColumn.setCaption(caption);
            }
        }

        @Override
        public String getCollapsingToggleCaption() {
            if (gridColumn != null) {
                return gridColumn.getHidingToggleCaption();
            }
            return collapsingToggleCaption;
        }

        @Override
        public void setCollapsingToggleCaption(String collapsingToggleCaption) {
            this.collapsingToggleCaption = collapsingToggleCaption;
            if (gridColumn != null) {
                gridColumn.setHidingToggleCaption(collapsingToggleCaption);
            }
        }

        @Override
        public double getWidth() {
            if (gridColumn != null) {
                return gridColumn.getWidth();
            }
            return width;
        }

        @Override
        public void setWidth(double width) {
            this.width = width;
            if (gridColumn != null) {
                gridColumn.setWidth(width);
            }
        }

        @Override
        public boolean isWidthAuto() {
            if (gridColumn != null) {
                return gridColumn.isWidthUndefined();
            }
            return width < 0;
        }

        @Override
        public void setWidthAuto() {
            if (!isWidthAuto()) {
                width = -1;
                if (gridColumn != null) {
                    gridColumn.setWidthUndefined();
                }
            }
        }

        @Override
        public int getExpandRatio() {
            if (gridColumn != null) {
                return gridColumn.getExpandRatio();
            }
            return expandRatio;
        }

        @Override
        public void setExpandRatio(int expandRatio) {
            this.expandRatio = expandRatio;
            if (gridColumn != null) {
                gridColumn.setExpandRatio(expandRatio);
            }
        }

        @Override
        public void clearExpandRatio() {
            setExpandRatio(-1);
        }

        @Override
        public double getMinimumWidth() {
            if (gridColumn != null) {
                return gridColumn.getMinimumWidth();
            }
            return minWidth;
        }

        @Override
        public void setMinimumWidth(double pixels) {
            this.minWidth = pixels;
            if (gridColumn != null) {
                gridColumn.setMinimumWidth(pixels);
            }
        }

        @Override
        public double getMaximumWidth() {
            if (gridColumn != null) {
                return gridColumn.getMaximumWidth();
            }
            return maxWidth;
        }

        @Override
        public void setMaximumWidth(double pixels) {
            this.maxWidth = pixels;
            if (gridColumn != null) {
                gridColumn.setMaximumWidth(pixels);
            }
        }

        @Override
        public boolean isVisible() {
            return visible;
        }

        @Override
        public void setVisible(boolean visible) {
            if (this.visible != visible) {
                this.visible = visible;

                //noinspection unchecked
                Grid<E> grid = (Grid<E>) owner.getComponent();

                if (visible) {
                    Grid.Column<E, ?> gridColumn =
                            grid.addColumn(new EntityValueProvider<>(getPropertyPath()));
                    owner.setupGridColumnProperties(gridColumn, this);

                    grid.setColumnOrder(owner.getColumnOrder());
                } else {
                    grid.removeColumn(getId());
                    setGridColumn(null);
                }
            }
        }

        @Override
        public boolean isCollapsed() {
            if (gridColumn != null) {
                return gridColumn.isHidden();
            }
            return collapsed;
        }

        @Override
        public void setCollapsed(boolean collapsed) {
            this.collapsed = collapsed;
            if (gridColumn != null) {
                gridColumn.setHidden(collapsed);
            }
        }

        @Override
        public boolean isCollapsible() {
            if (gridColumn != null) {
                return gridColumn.isHidable();
            }
            return collapsible;
        }

        @Override
        public void setCollapsible(boolean collapsible) {
            this.collapsible = collapsible;
            updateCollapsible();
        }

        public void updateCollapsible() {
            if (gridColumn != null) {
                gridColumn.setHidable(collapsible && owner.isColumnsCollapsingAllowed());
            }
        }

        @Override
        public boolean isSortable() {
            return sortable;
        }

        @Override
        public void setSortable(boolean sortable) {
            this.sortable = sortable;
            updateSortable();
        }

        public void updateSortable() {
            if (gridColumn != null) {
                gridColumn.setSortable(this.sortable && owner.isSortable());
            }
        }

        @Override
        public boolean isResizable() {
            if (gridColumn != null) {
                return gridColumn.isResizable();
            }
            return resizable;
        }

        @Override
        public void setResizable(boolean resizable) {
            this.resizable = resizable;
            if (gridColumn != null) {
                gridColumn.setResizable(resizable);
            }
        }

        @Override
        public Formatter getFormatter() {
            return formatter;
        }

        @Override
        public void setFormatter(Formatter formatter) {
            this.formatter = formatter;
            updateRendererInternal();
        }

        @Nullable
        protected MetaProperty getMetaProperty() {
            return getPropertyPath() != null
                    ? getPropertyPath().getMetaProperty()
                    : null;
        }

        @Override
        public Element getXmlDescriptor() {
            return element;
        }

        @Override
        public void setXmlDescriptor(Element element) {
            this.element = element;
        }

        @Override
        public Renderer getRenderer() {
            return renderer;
        }

        @Override
        public void setRenderer(Renderer renderer) {
            setRenderer(renderer, null);
        }

        @Override
        public void setRenderer(Renderer renderer, Function presentationProvider) {
            if (renderer == null && this.renderer != null) {
                this.renderer.resetImplementation();
                this.renderer.setDataGrid(null);
            }

            //noinspection unchecked
            this.renderer = (AbstractRenderer) renderer;
            this.presentationProvider = presentationProvider;

            if (this.renderer != null) {
                this.renderer.setDataGrid(owner);
            }

            updateRendererInternal();
        }

        @SuppressWarnings({"unchecked"})
        protected ValueProvider createPresentationProviderWrapper(Function presentationProvider) {
            return (ValueProvider) presentationProvider::apply;
        }

        @SuppressWarnings("unchecked")
        protected void updateRendererInternal() {
            if (gridColumn != null) {
                com.vaadin.ui.renderers.Renderer vRenderer = renderer != null
                        ? renderer.getImplementation()
                        : owner.getDefaultRenderer(this);

                // The following priority is used to determine a value provider:
                // a presentation provider > a converter > a formatter > a renderer's presentation provider >
                // a value provider that always returns its input argument > a default presentation provider
                //noinspection RedundantCast
                ValueProvider vPresentationProvider = presentationProvider != null
                        ? createPresentationProviderWrapper(presentationProvider)
                        : converter != null
                        ? new DataGridConverterBasedValueProvider(converter)
                        : formatter != null
                        ? new FormatterBasedValueProvider(formatter)
                        : renderer != null && renderer.getPresentationValueProvider() != null
                        ? (ValueProvider) renderer.getPresentationValueProvider()
                        : renderer != null
                        // In case renderer != null and there are no other user specified value providers
                        // We use a value provider that always returns its input argument instead of a default
                        // value provider as we want to keep the original value type.
                        ? ValueProvider.identity()
                        : owner.getDefaultPresentationValueProvider(this);

                gridColumn.setRenderer(vPresentationProvider, vRenderer);
                owner.repaint();
            }
        }

        @Override
        public Function getPresentationProvider() {
            return presentationProvider;
        }

        @Override
        public Converter<?, ?> getConverter() {
            return converter;
        }

        @Override
        public void setConverter(Converter<?, ?> converter) {
            this.converter = converter;
            updateRendererInternal();
        }

        @Override
        public Class getType() {
            return type;
        }

        public boolean isGenerated() {
            return generated;
        }

        public void setGenerated(boolean generated) {
            this.generated = generated;
        }

        @Override
        public boolean isEditable() {
            if (gridColumn != null) {
                return gridColumn.isEditable();
            }
            return isColumnShouldBeEditable();
        }

        protected boolean isColumnShouldBeEditable() {
            return editable
                    && (!generated || fieldGenerator != null)
                    && (!isRepresentsCollection() || fieldGenerator != null)
                    && isEditingPermitted();
        }

        protected boolean isRepresentsCollection() {
            if (propertyPath != null) {
                MetaProperty metaProperty = propertyPath.getMetaProperty();
                Class<?> javaType = metaProperty.getJavaType();
                return Collection.class.isAssignableFrom(javaType);
            }
            return false;
        }

        protected boolean isEditingPermitted() {
            if (propertyPath != null) {
                MetaClass metaClass = propertyPath.getMetaClass();
                return owner.security.isEntityAttrUpdatePermitted(metaClass, propertyPath.toString());
            }
            return true;
        }

        @Override
        public void setEditable(boolean editable) {
            this.editable = editable;
            updateEditable();
        }

        protected void updateEditable() {
            if (gridColumn != null) {
                gridColumn.setEditable(isColumnShouldBeEditable());
            }
        }

        @Override
        public ColumnEditorFieldGenerator getEditorFieldGenerator() {
            return fieldGenerator;
        }

        @Override
        public void setEditorFieldGenerator(ColumnEditorFieldGenerator fieldFactory) {
            this.fieldGenerator = fieldFactory;
            updateEditable();
        }

        @SuppressWarnings("unchecked")
        @Override
        public StyleProvider<E> getStyleProvider() {
            return (StyleProvider<E>) styleProvider;
        }

        @Override
        public void setStyleProvider(StyleProvider<? super E> styleProvider) {
            this.styleProvider = styleProvider;
            owner.repaint();
        }

        @SuppressWarnings("unchecked")
        @Override
        public DescriptionProvider<E> getDescriptionProvider() {
            return (DescriptionProvider<E>) descriptionProvider;
        }

        @Override
        public void setDescriptionProvider(DescriptionProvider<? super E> descriptionProvider) {
            setDescriptionProvider(descriptionProvider, ContentMode.PREFORMATTED);
        }

        @Override
        public void setDescriptionProvider(DescriptionProvider<? super E> descriptionProvider,
                                           ContentMode contentMode) {
            this.descriptionProvider = descriptionProvider;
            this.descriptionContentMode = contentMode;

            if (gridColumn != null) {
                gridColumn.getState().tooltipContentMode = WebWrapperUtils.toVaadinContentMode(contentMode);
            }

            owner.repaint();
        }

        public ContentMode getDescriptionContentMode() {
            return descriptionContentMode;
        }

        public Grid.Column<E, ?> getGridColumn() {
            return gridColumn;
        }

        public void setGridColumn(Grid.Column<E, ?> gridColumn) {
            AppUI current = AppUI.getCurrent();
            if (gridColumn == null && current != null && current.isTestMode()) {
                owner.removeColumnId(this.gridColumn);
            }

            this.gridColumn = gridColumn;
        }

        @Override
        public DataGrid<E> getOwner() {
            return owner;
        }

        @Override
        public void setOwner(DataGrid<E> owner) {
            this.owner = (WebAbstractDataGrid<?, E>) owner;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            //noinspection unchecked
            ColumnImpl column = (ColumnImpl) o;

            return id.equals(column.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public String toString() {
            return id == null ? super.toString() : id;
        }
    }

    protected abstract static class AbstractStaticRowImp<T extends StaticCell> implements StaticRow<T> {

        protected WebAbstractDataGrid dataGrid;
        protected StaticSection.StaticRow<?> gridRow;

        public AbstractStaticRowImp(WebAbstractDataGrid dataGrid, StaticSection.StaticRow<?> gridRow) {
            this.dataGrid = dataGrid;
            this.gridRow = gridRow;
        }

        @Override
        public String getStyleName() {
            return gridRow.getStyleName();
        }

        @Override
        public void setStyleName(String styleName) {
            gridRow.setStyleName(styleName);
        }

        @Override
        public T join(String... columnIds) {
            return joinInternal(columnIds);
        }

        protected abstract T joinInternal(String... columnIds);

        @Override
        public T getCell(String columnId) {
            ColumnImpl column = (ColumnImpl) dataGrid.getColumnNN(columnId);
            return getCellInternal(column.getId());
        }

        protected abstract T getCellInternal(String columnId);

        public StaticSection.StaticRow<?> getGridRow() {
            return gridRow;
        }
    }

    protected static class HeaderRowImpl extends AbstractStaticRowImp<HeaderCell> implements HeaderRow {

        public HeaderRowImpl(WebAbstractDataGrid dataGrid, Header.Row headerRow) {
            super(dataGrid, headerRow);
        }

        @Override
        public Header.Row getGridRow() {
            return (Header.Row) super.getGridRow();
        }

        @Override
        protected HeaderCell getCellInternal(String columnId) {
            Header.Row.Cell gridCell = getGridRow().getCell(columnId);
            return new HeaderCellImpl(this, gridCell);
        }

        @Override
        protected HeaderCell joinInternal(String... columnIds) {
            Header.Row.Cell gridCell = (Header.Row.Cell) getGridRow().join(columnIds);
            return new HeaderCellImpl(this, gridCell);
        }
    }

    protected static class FooterRowImpl extends AbstractStaticRowImp<FooterCell> implements FooterRow {

        public FooterRowImpl(WebAbstractDataGrid dataGrid, Footer.Row footerRow) {
            super(dataGrid, footerRow);
        }

        @Override
        public Footer.Row getGridRow() {
            return (Footer.Row) super.getGridRow();
        }

        @Override
        protected FooterCell getCellInternal(String columnId) {
            Footer.Row.Cell gridCell = getGridRow().getCell(columnId);
            return new FooterCellImpl(this, gridCell);
        }

        @Override
        protected FooterCell joinInternal(String... columnIds) {
            Footer.Row.Cell gridCell = (Footer.Row.Cell) getGridRow().join(columnIds);
            return new FooterCellImpl(this, gridCell);
        }
    }

    protected abstract static class AbstractStaticCellImpl implements StaticCell {

        protected StaticRow<?> row;
        protected com.haulmont.cuba.gui.components.Component component;

        public AbstractStaticCellImpl(StaticRow<?> row) {
            this.row = row;
        }

        @Override
        public StaticRow<?> getRow() {
            return row;
        }

        @Override
        public com.haulmont.cuba.gui.components.Component getComponent() {
            return component;
        }

        @Override
        public void setComponent(com.haulmont.cuba.gui.components.Component component) {
            this.component = component;
        }
    }

    protected static class HeaderCellImpl extends AbstractStaticCellImpl implements HeaderCell {

        protected Header.Row.Cell gridCell;

        public HeaderCellImpl(HeaderRow row, Header.Row.Cell gridCell) {
            super(row);
            this.gridCell = gridCell;
        }

        @Override
        public HeaderRow getRow() {
            return (HeaderRow) super.getRow();
        }

        @Override
        public String getStyleName() {
            return gridCell.getStyleName();
        }

        @Override
        public void setStyleName(String styleName) {
            gridCell.setStyleName(styleName);
        }

        @Override
        public DataGridStaticCellType getCellType() {
            return WebWrapperUtils.toDataGridStaticCellType(gridCell.getCellType());
        }

        @Override
        public void setComponent(com.haulmont.cuba.gui.components.Component component) {
            super.setComponent(component);
            gridCell.setComponent(component.unwrap(Component.class));
        }

        @Override
        public String getHtml() {
            return gridCell.getHtml();
        }

        @Override
        public void setHtml(String html) {
            gridCell.setHtml(html);
        }

        @Override
        public String getText() {
            return gridCell.getText();
        }

        @Override
        public void setText(String text) {
            gridCell.setText(text);
        }
    }

    protected static class FooterCellImpl extends AbstractStaticCellImpl implements FooterCell {

        protected Footer.Row.Cell gridCell;

        public FooterCellImpl(FooterRow row, Footer.Row.Cell gridCell) {
            super(row);
            this.gridCell = gridCell;
        }

        @Override
        public FooterRow getRow() {
            return (FooterRow) super.getRow();
        }

        @Override
        public String getStyleName() {
            return gridCell.getStyleName();
        }

        @Override
        public void setStyleName(String styleName) {
            gridCell.setStyleName(styleName);
        }

        @Override
        public DataGridStaticCellType getCellType() {
            return WebWrapperUtils.toDataGridStaticCellType(gridCell.getCellType());
        }

        @Override
        public void setComponent(com.haulmont.cuba.gui.components.Component component) {
            super.setComponent(component);
            gridCell.setComponent(component.unwrap(Component.class));
        }

        @Override
        public String getHtml() {
            return gridCell.getHtml();
        }

        @Override
        public void setHtml(String html) {
            gridCell.setHtml(html);
        }

        @Override
        public String getText() {
            return gridCell.getText();
        }

        @Override
        public void setText(String text) {
            gridCell.setText(text);
        }
    }

    public static class ActionMenuItemWrapper {
        protected MenuItem menuItem;
        protected Action action;

        protected boolean showIconsForPopupMenuActions;

        protected PropertyChangeListener actionPropertyChangeListener;

        public ActionMenuItemWrapper(MenuItem menuItem, boolean showIconsForPopupMenuActions) {
            this.menuItem = menuItem;
            this.showIconsForPopupMenuActions = showIconsForPopupMenuActions;

            this.menuItem.setCommand((Menu.Command) selectedItem -> {
                if (action != null) {
                    performAction(action);
                }
            });
        }

        public void performAction(Action action) {
            action.actionPerform(null);
        }

        public MenuItem getMenuItem() {
            return menuItem;
        }

        public Action getAction() {
            return action;
        }

        public void setAction(Action action) {
            if (this.action != action) {
                if (this.action != null) {
                    this.action.removePropertyChangeListener(actionPropertyChangeListener);
                }

                this.action = action;

                if (action != null) {
                    String caption = action.getCaption();
                    if (!StringUtils.isEmpty(caption)) {
                        setCaption(caption);
                    }

                    menuItem.setEnabled(action.isEnabled());
                    menuItem.setVisible(action.isVisible());

                    if (action.getIcon() != null) {
                        setIcon(action.getIcon());
                    }

                    actionPropertyChangeListener = evt -> {
                        if (Action.PROP_ICON.equals(evt.getPropertyName())) {
                            setIcon(ActionMenuItemWrapper.this.action.getIcon());
                        } else if (Action.PROP_CAPTION.equals(evt.getPropertyName())) {
                            setCaption(ActionMenuItemWrapper.this.action.getCaption());
                        } else if (Action.PROP_ENABLED.equals(evt.getPropertyName())) {
                            setEnabled(ActionMenuItemWrapper.this.action.isEnabled());
                        } else if (Action.PROP_VISIBLE.equals(evt.getPropertyName())) {
                            setVisible(ActionMenuItemWrapper.this.action.isVisible());
                        }
                    };
                    action.addPropertyChangeListener(actionPropertyChangeListener);
                }
            }
        }

        public void setEnabled(boolean enabled) {
            menuItem.setEnabled(enabled);
        }

        public void setVisible(boolean visible) {
            menuItem.setVisible(visible);
        }

        public void setIcon(String icon) {
            if (showIconsForPopupMenuActions) {
                if (!StringUtils.isEmpty(icon)) {
                    menuItem.setIcon(AppBeans.get(IconResolver.class).getIconResource(icon));
                } else {
                    menuItem.setIcon(null);
                }
            }
        }

        public void setCaption(String caption) {
            if (action.getShortcutCombination() != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(caption);
                if (action.getShortcutCombination() != null) {
                    sb.append(" (").append(action.getShortcutCombination().format()).append(")");
                }
                caption = sb.toString();
            }

            menuItem.setText(caption);
        }
    }

    protected static class GridComposition extends CssLayout {
        protected Grid<?> grid;

        public Grid<?> getGrid() {
            return grid;
        }

        public void setGrid(Grid<?> grid) {
            this.grid = grid;
        }

        @Override
        public void setHeight(float height, Unit unit) {
            super.setHeight(height, unit);

            if (getHeight() < 0) {
                grid.setHeightUndefined();
                grid.setHeightMode(HeightMode.UNDEFINED);
            } else {
                grid.setHeight(100, Unit.PERCENTAGE);
                grid.setHeightMode(HeightMode.CSS);
            }
        }

        @Override
        public void setWidth(float width, Unit unit) {
            super.setWidth(width, unit);

            if (getWidth() < 0) {
                grid.setWidthUndefined();
            } else {
                grid.setWidth(100, Unit.PERCENTAGE);
            }
        }
    }

    @Deprecated
    public class CollectionDsListenersWrapper implements
            Datasource.ItemChangeListener,
            Datasource.ItemPropertyChangeListener,
            Datasource.StateChangeListener,
            CollectionDatasource.CollectionChangeListener {

        protected WeakItemChangeListener weakItemChangeListener;
        protected WeakItemPropertyChangeListener weakItemPropertyChangeListener;
        protected WeakStateChangeListener weakStateChangeListener;
        protected WeakCollectionChangeListener weakCollectionChangeListener;

        private EventRouter eventRouter;

        /**
         * Use EventRouter for listeners instead of fields with listeners List.
         *
         * @return lazily initialized {@link EventRouter} instance.
         * @see EventRouter
         */
        protected EventRouter getEventRouter() {
            if (eventRouter == null) {
                eventRouter = new EventRouter();
            }
            return eventRouter;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void collectionChanged(CollectionDatasource.CollectionChangeEvent e) {
            // #PL-2035, reload selection from ds
            Set<E> selectedItems = component.getSelectedItems();
            if (selectedItems == null) {
                selectedItems = Collections.emptySet();
            }

            //noinspection unchecked
            Set<E> newSelection = selectedItems.stream()
                    .filter(entity -> e.getDs().containsItem(entity.getId()))
                    .collect(Collectors.toSet());

            if (e.getDs().getState() == Datasource.State.VALID && e.getDs().getItem() != null) {
                newSelection.add((E) e.getDs().getItem());
            }

            if (newSelection.isEmpty()) {
                setSelected((E) null);
            } else {
                setSelectedItems(newSelection);
            }

            for (Action action : getActions()) {
                action.refreshState();
            }

            if (getEventRouter().hasListeners(CollectionDatasource.CollectionChangeListener.class)) {
                getEventRouter().fireEvent(CollectionDatasource.CollectionChangeListener.class,
                        CollectionDatasource.CollectionChangeListener::collectionChanged, e);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void itemChanged(Datasource.ItemChangeEvent e) {
            for (Action action : getActions()) {
                action.refreshState();
            }

            if (getEventRouter().hasListeners(Datasource.ItemChangeListener.class)) {
                getEventRouter().fireEvent(Datasource.ItemChangeListener.class,
                        Datasource.ItemChangeListener::itemChanged, e);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void itemPropertyChanged(Datasource.ItemPropertyChangeEvent e) {
            for (Action action : getActions()) {
                action.refreshState();
            }

            if (getEventRouter().hasListeners(Datasource.ItemPropertyChangeListener.class)) {
                getEventRouter().fireEvent(Datasource.ItemPropertyChangeListener.class,
                        Datasource.ItemPropertyChangeListener::itemPropertyChanged, e);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void stateChanged(Datasource.StateChangeEvent e) {
            for (Action action : getActions()) {
                action.refreshState();
            }

            if (getEventRouter().hasListeners(Datasource.StateChangeListener.class)) {
                getEventRouter().fireEvent(Datasource.StateChangeListener.class,
                        Datasource.StateChangeListener::stateChanged, e);
            }
        }

        public void addCollectionChangeListener(CollectionDatasource.CollectionChangeListener listener) {
            getEventRouter().addListener(CollectionDatasource.CollectionChangeListener.class, listener);
        }

        public void removeCollectionChangeListener(CollectionDatasource.CollectionChangeListener listener) {
            getEventRouter().removeListener(CollectionDatasource.CollectionChangeListener.class, listener);
        }

        public void addItemChangeListener(Datasource.ItemChangeListener listener) {
            getEventRouter().addListener(Datasource.ItemChangeListener.class, listener);
        }

        public void removeItemChangeListener(Datasource.ItemChangeListener listener) {
            getEventRouter().removeListener(Datasource.ItemChangeListener.class, listener);
        }

        public void addItemPropertyChangeListener(Datasource.ItemPropertyChangeListener listener) {
            getEventRouter().addListener(Datasource.ItemPropertyChangeListener.class, listener);
        }

        public void removeItemPropertyChangeListener(Datasource.ItemPropertyChangeListener listener) {
            getEventRouter().removeListener(Datasource.ItemPropertyChangeListener.class, listener);
        }

        public void addStateChangeListener(Datasource.StateChangeListener listener) {
            getEventRouter().addListener(Datasource.StateChangeListener.class, listener);
        }

        public void removeStateChangeListener(Datasource.StateChangeListener listener) {
            getEventRouter().removeListener(Datasource.StateChangeListener.class, listener);
        }

        @SuppressWarnings("unchecked")
        public void bind(CollectionDatasource ds) {
            weakItemChangeListener = new WeakItemChangeListener(ds, this);
            ds.addItemChangeListener(weakItemChangeListener);

            weakItemPropertyChangeListener = new WeakItemPropertyChangeListener(ds, this);
            ds.addItemPropertyChangeListener(weakItemPropertyChangeListener);

            weakStateChangeListener = new WeakStateChangeListener(ds, this);
            ds.addStateChangeListener(weakStateChangeListener);

            weakCollectionChangeListener = new WeakCollectionChangeListener(ds, this);
            ds.addCollectionChangeListener(weakCollectionChangeListener);
        }

        @SuppressWarnings("unchecked")
        public void unbind(CollectionDatasource ds) {
            ds.removeItemChangeListener(weakItemChangeListener);
            weakItemChangeListener = null;

            ds.removeItemPropertyChangeListener(weakItemPropertyChangeListener);
            weakItemPropertyChangeListener = null;

            ds.removeStateChangeListener(weakStateChangeListener);
            weakStateChangeListener = null;

            ds.removeCollectionChangeListener(weakCollectionChangeListener);
            weakCollectionChangeListener = null;
        }
    }
}