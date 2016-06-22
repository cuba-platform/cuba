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

package com.haulmont.cuba.desktop.gui.components;

import com.google.common.collect.Lists;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.data.AnyTableModelAdapter;
import com.haulmont.cuba.desktop.gui.data.RowSorterImpl;
import com.haulmont.cuba.desktop.sys.FontDialog;
import com.haulmont.cuba.desktop.sys.layout.MigLayoutHelper;
import com.haulmont.cuba.desktop.sys.vcl.Flushable;
import com.haulmont.cuba.desktop.sys.vcl.FocusableTable;
import com.haulmont.cuba.desktop.sys.vcl.TableFocusManager;
import com.haulmont.cuba.desktop.theme.DesktopTheme;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.CollectionDsActionsNotifier;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.data.impl.WeakCollectionChangeListener;
import com.haulmont.cuba.gui.data.impl.WeakItemPropertyChangeListener;
import com.haulmont.cuba.gui.presentations.Presentations;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.action.BoundAction;
import org.jdesktop.swingx.table.ColumnControlButton;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.table.TableColumnModelExt;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import javax.annotation.Nullable;
import javax.swing.AbstractAction;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.Component;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper.convertKeyCombination;

public abstract class DesktopAbstractTable<C extends JXTable, E extends Entity>
        extends DesktopAbstractActionsHolderComponent<C>
        implements Table<E> {

    protected static final int DEFAULT_ROW_MARGIN = 4;

    protected boolean contextMenuEnabled = true;
    protected MigLayout layout;
    protected JPanel panel;
    protected JPanel topPanel;
    protected JScrollPane scrollPane;
    protected AnyTableModelAdapter tableModel;
    protected CollectionDatasource datasource;
    protected ButtonsPanel buttonsPanel;
    protected RowsCount rowsCount;
    protected Map<Object, Object> aggregationResult;
    protected Map<Object, Column> columns = new HashMap<>();
    protected List<Table.Column> columnsOrder = new ArrayList<>();
    protected boolean sortable = true;
    protected TableSettings tableSettings;
    protected boolean editable;
    protected List<StyleProvider> styleProviders; // lazy initialized list
    protected IconProvider iconProvider;

    protected Action itemClickAction;
    protected Action enterPressAction;

    protected boolean columnsInitialized = false;
    protected int generatedColumnsCount = 0;

    protected boolean columnHeaderVisible = true;

    protected boolean showSelection = true;

    // Indicates that model is being changed.
    protected boolean isAdjusting = false;

    protected DesktopTableFieldFactory tableFieldFactory = new DesktopTableFieldFactory();

    protected List<MetaPropertyPath> editableColumns = new LinkedList<>();

    protected Map<Table.Column, String> requiredColumns = new HashMap<>();

    protected Security security = AppBeans.get(Security.NAME);

    protected boolean columnAdjustRequired = false;

    protected boolean fontInitialized = false;

    protected int defaultRowHeight = 24;
    protected int defaultEditableRowHeight = 28;

    protected Set<E> selectedItems = Collections.emptySet();

    protected Map<String, Printable> printables = new HashMap<>();

    protected Map<Entity, Datasource> fieldDatasources = new WeakHashMap<>();

    // Manual control for content repaint process
    protected boolean contentRepaintEnabled = true;

    protected Document defaultSettings;
    protected boolean multiLineCells;
    protected boolean settingsEnabled = true;

    protected CollectionDatasource.CollectionChangeListener collectionChangeListener;
    protected CollectionDatasource.CollectionChangeListener securityCollectionChangeListener;
    protected Datasource.ItemPropertyChangeListener itemPropertyChangeListener;

    protected CollectionDsActionsNotifier collectionDsActionsNotifier;

    protected DesktopAbstractTable() {
        shortcutsDelegate.setAllowEnterShortcut(false);
    }

    protected void initComponent() {
        layout = new MigLayout("flowy, fill, insets 0", "", "[min!][fill]");
        panel = new JPanel(layout);

        topPanel = new JPanel(new BorderLayout());
        topPanel.setVisible(false);
        panel.add(topPanel, "growx");

        scrollPane = new JScrollPane(impl);
        impl.setFillsViewportHeight(true);
        panel.add(scrollPane, "grow");

        impl.setShowGrid(true);
        impl.setGridColor(Color.lightGray);

        impl.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                            handleClickAction();
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        showPopup(e);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        showPopup(e);
                    }

                    protected void showPopup(MouseEvent e) {
                        if (e.isPopupTrigger() && contextMenuEnabled) {
                            // select row
                            Point p = e.getPoint();
                            int viewRowIndex = impl.rowAtPoint(p);

                            int rowNumber;
                            if (viewRowIndex >= 0) {
                                rowNumber = impl.convertRowIndexToModel(viewRowIndex);
                            } else {
                                rowNumber = -1;
                            }
                            ListSelectionModel model = impl.getSelectionModel();

                            if (!model.isSelectedIndex(rowNumber)) {
                                model.setSelectionInterval(rowNumber, rowNumber);
                            }

                            // show popup menu
                            JPopupMenu popupMenu = createPopupMenu();
                            if (popupMenu.getComponentCount() > 0) {
                                popupMenu.show(e.getComponent(), e.getX(), e.getY());
                            }
                        }
                    }
                }
        );

        impl.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
        impl.getActionMap().put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (enterPressAction != null) {
                    enterPressAction.actionPerform(DesktopAbstractTable.this);
                } else {
                    handleClickAction();
                }
            }
        });

        Messages messages = AppBeans.get(Messages.NAME);
        // localize default column control actions
        for (Object actionKey : impl.getActionMap().allKeys()) {
            if ("column.packAll".equals(actionKey)) {
                BoundAction action = (BoundAction) impl.getActionMap().get(actionKey);
                action.setName(messages.getMessage(DesktopTable.class, "DesktopTable.packAll"));
            } else if ("column.packSelected".equals(actionKey)) {
                BoundAction action = (BoundAction) impl.getActionMap().get(actionKey);
                action.setName(messages.getMessage(DesktopTable.class, "DesktopTable.packSelected"));
            } else if ("column.horizontalScroll".equals(actionKey)) {
                BoundAction action = (BoundAction) impl.getActionMap().get(actionKey);
                action.setName(messages.getMessage(DesktopTable.class, "DesktopTable.horizontalScroll"));
            }
        }

        // Ability to configure fonts in table
        // Add action to column control
        String configureFontsLabel = messages.getMessage(
                DesktopTable.class, "DesktopTable.configureFontsLabel");
        impl.getActionMap().put(ColumnControlButton.COLUMN_CONTROL_MARKER + "fonts",
                new AbstractAction(configureFontsLabel) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Component rootComponent = SwingUtilities.getRoot(impl);
                        final FontDialog fontDialog = FontDialog.show(rootComponent, impl.getFont());
                        fontDialog.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                Font result = fontDialog.getResult();
                                if (result != null) {
                                    impl.setFont(result);
                                    packRows();
                                }
                            }
                        });
                        fontDialog.open();
                    }
                });

        // Ability to reset settings
        String resetSettingsLabel = messages.getMessage(
                DesktopTable.class, "DesktopTable.resetSettings");
        impl.getActionMap().put(ColumnControlButton.COLUMN_CONTROL_MARKER + "resetSettings",
                new AbstractAction(resetSettingsLabel) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        resetPresentation();
                    }
                });

        scrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (!columnsInitialized) {
                    adjustColumnHeaders();
                }
                columnsInitialized = true;
            }
        });

        // init default row height
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (!fontInitialized) {
                    applyFont(impl, impl.getFont());
                }
            }
        });
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        CC cc = new CC().grow();
        MigLayoutHelper.applyWidth(cc, (int) widthSize.value, widthSize.unit, false);
        layout.setComponentConstraints(scrollPane, cc);
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
        if (action != null && action.isEnabled() && action.isVisible()) {
            Window window = ComponentsHelper.getWindow(DesktopAbstractTable.this);
            if (window instanceof Window.Wrapper) {
                window = ((Window.Wrapper) window).getWrappedWindow();
            }

            if (!(window instanceof Window.Lookup)) {
                action.actionPerform(DesktopAbstractTable.this);
            } else {
                Window.Lookup lookup = (Window.Lookup) window;

                com.haulmont.cuba.gui.components.Component lookupComponent = lookup.getLookupComponent();
                if (lookupComponent != this) {
                    action.actionPerform(DesktopAbstractTable.this);
                } else if (action.getId().equals(WindowDelegate.LOOKUP_ITEM_CLICK_ACTION_ID)) {
                    action.actionPerform(DesktopAbstractTable.this);
                }
            }
        }
    }

    protected void refreshActionsState() {
        for (Action action : getActions()) {
            action.refreshState();
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

    protected void readjustColumns() {
        if (columnAdjustRequired) {
            return;
        }

        this.columnAdjustRequired = true;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                adjustColumnHeaders();

                columnAdjustRequired = false;
            }
        });
    }

    protected void adjustColumnHeaders() {
        List<TableColumn> notInited = new LinkedList<>();
        int summaryWidth = 0;
        int componentWidth = impl.getParent().getWidth();

        // take into account only visible columns
        Enumeration<TableColumn> columnEnumeration = impl.getColumnModel().getColumns();
        while (columnEnumeration.hasMoreElements()) {
            TableColumn tableColumn = columnEnumeration.nextElement();
            Column column = (Column) tableColumn.getIdentifier();

            Integer width = column.getWidth();
            if (width != null) {
                tableColumn.setPreferredWidth(width);
                tableColumn.setWidth(width);
                summaryWidth += width;
            } else {
                notInited.add(tableColumn);
            }
        }

        if (notInited.size() != impl.getColumnCount()) {
            impl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            if (!notInited.isEmpty() && (componentWidth > summaryWidth)) {
                int defaultWidth = (componentWidth - summaryWidth) / notInited.size();
                for (TableColumn column : notInited) {
                    column.setPreferredWidth(Math.max(defaultWidth, column.getWidth()));
                }
            }
        }
    }

    protected abstract void initTableModel(CollectionDatasource datasource);

    @Override
    public JComponent getComposition() {
        return panel;
    }

    @Override
    public List<Column> getColumns() {
        return columnsOrder;
    }

    @Override
    public Column getColumn(String id) {
        for (Table.Column column : columnsOrder) {
            if (column.getId().toString().equals(id)) {
                return column;
            }
        }
        return null;
    }

    @Override
    public void addColumn(Column column) {
        checkNotNullArgument(column, "Column must be non null");

        columns.put(column.getId(), column);
        columnsOrder.add(column);

        if (tableModel != null) {
            tableModel.addColumn(column);
        }

        if (datasource != null && column.isEditable() && column.getId() instanceof MetaPropertyPath) {
            if (!editableColumns.contains(column.getId())) {
                editableColumns.add((MetaPropertyPath) column.getId());
            }
        }

        setColumnIdentifiers();
        refresh();

        column.setOwner(this);
    }

    @Override
    public void removeColumn(Column column) {
        if (column == null) {
            return;
        }

        String name;
        if (column.getId() instanceof MetaPropertyPath) {
            MetaPropertyPath metaPropertyPath = (MetaPropertyPath) column.getId();
            name = metaPropertyPath.getMetaProperty().getName();

            editableColumns.remove(metaPropertyPath);
        } else {
            name = column.getId().toString();
        }

        TableColumn tableColumn = null;

        Iterator<TableColumn> columnIterator = getAllColumns().iterator();
        while (columnIterator.hasNext() && (tableColumn == null)) {
            TableColumn xColumn = columnIterator.next();
            Object identifier = xColumn.getIdentifier();
            if (identifier instanceof String && identifier.equals(name)) {
                tableColumn = xColumn;
            } else if (column.equals(identifier)) {
                tableColumn = xColumn;
            }
        }

        if (tableColumn != null) {
            // store old cell editors / renderers
            Map<Object, TableCellEditor> cellEditors = new HashMap<>();
            Map<Object, TableCellRenderer> cellRenderers = new HashMap<>();

            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                Column tableModelColumn = tableModel.getColumn(i);

                if (tableModel.isGeneratedColumn(tableModelColumn)) {
                    TableColumn oldColumn = getColumn(tableModelColumn);

                    cellEditors.put(tableModelColumn.getId(), oldColumn.getCellEditor());
                    cellRenderers.put(tableModelColumn.getId(), oldColumn.getCellRenderer());
                }
            }

            impl.getColumnModel().removeColumn(tableColumn);
            impl.removeColumn(tableColumn);

            columns.remove(column.getId());
            columnsOrder.remove(column);

            if (tableModel != null) {
                tableModel.removeColumn(column);
            }

            // reassign column identifiers
            setColumnIdentifiers();

            // reattach old generated columns
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                Column tableModelColumn = tableModel.getColumn(i);

                if (tableModel.isGeneratedColumn(tableModelColumn)) {
                    TableColumn oldColumn = getColumn(tableModelColumn);
                    if (cellEditors.containsKey(tableModelColumn.getId())) {
                        oldColumn.setCellEditor(cellEditors.get(tableModelColumn.getId()));
                    }
                    if (cellRenderers.containsKey(tableModelColumn.getId())) {
                        oldColumn.setCellRenderer(cellRenderers.get(tableModelColumn.getId()));
                    }
                }
            }

            packRows();
            repaintImplIfNeeded();
        }

        column.setOwner(null);
    }

    @Override
    public void setDatasource(final CollectionDatasource datasource) {
        Preconditions.checkNotNullArgument(datasource, "datasource is null");

        final Collection<Object> properties;
        if (this.columns.isEmpty()) {
            MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);

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
        properties = this.columns.keySet();

        this.datasource = datasource;

        collectionChangeListener = e -> {
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
        };
        //noinspection unchecked
        datasource.addCollectionChangeListener(new WeakCollectionChangeListener(datasource, collectionChangeListener));

        initTableModel(datasource);

        initChangeListener();

        setColumnIdentifiers();

        // Major change in table columns behavior #PL-4853
        // We need not to recreate all columns on each table change after initTableModel
//        impl.setAutoCreateColumnsFromModel(false);

        if (isSortable()) {
            impl.setRowSorter(new RowSorterImpl(tableModel));
        }

        initSelectionListener(datasource);

        List<MetaPropertyPath> editableColumns = null;
        if (isEditable()) {
            editableColumns = new LinkedList<>();
        }

        MetaClass metaClass = datasource.getMetaClass();
        for (final Object property : properties) {
            final Table.Column column = this.columns.get(property);

//            todo implement setColumnHeader
//            final String caption;
//            if (column != null) {
//                caption = StringUtils.capitalize(column.getCaption() != null ? column.getCaption() : getColumnCaption(property));
//            } else {
//                caption = StringUtils.capitalize(getColumnCaption(property));
//            }
//
//            setColumnHeader(property, caption);

            if (column != null) {
                if (column.isCollapsed() && getColumnControlVisible()) {
                    TableColumn tableColumn = getColumn(column);
                    if (tableColumn instanceof TableColumnExt) {
                        ((TableColumnExt) tableColumn).setVisible(false);
                    }
                }

                if (editableColumns != null && column.isEditable() && (property instanceof MetaPropertyPath)) {
                    MetaPropertyPath propertyPath = (MetaPropertyPath) property;
                    if (security.isEntityAttrUpdatePermitted(metaClass, property.toString())) {
                        editableColumns.add(propertyPath);
                    }
                }
            }
        }

        if (editableColumns != null && !editableColumns.isEmpty()) {
            setEditableColumns(editableColumns);
        }

        List<Object> columnsOrder = new ArrayList<>();
        for (Table.Column column : this.columnsOrder) {
            if (column.getId() instanceof MetaPropertyPath) {
                MetaPropertyPath metaPropertyPath = (MetaPropertyPath) column.getId();
                if (security.isEntityAttrReadPermitted(metaClass, metaPropertyPath.toString())) {
                    columnsOrder.add(column.getId());
                }
            } else {
                columnsOrder.add(column.getId());
            }
        }

        setVisibleColumns(columnsOrder);

        if (security.isSpecificPermitted(ShowInfoAction.ACTION_PERMISSION)) {
            ShowInfoAction action = (ShowInfoAction) getAction(ShowInfoAction.ACTION_ID);
            if (action == null) {
                action = new ShowInfoAction();
                addAction(action);
            }
            action.setDatasource(datasource);
        }

        securityCollectionChangeListener = e -> {
            onDataChange();
            packRows();

            // #PL-2035, reload selection from ds
            Set<E> selectedItems1 = getSelected();
            if (selectedItems1 == null) {
                selectedItems1 = Collections.emptySet();
            }

            Set<E> newSelection = new HashSet<>();
            for (E entity : selectedItems1) {
                if (e.getDs().containsItem(entity.getId())) {
                    newSelection.add(entity);
                }
            }

            if (e.getDs().getState() == Datasource.State.VALID && e.getDs().getItem() != null) {
                if (e.getDs().containsItem(e.getDs().getItem())) {
                    newSelection.add((E) e.getDs().getItem());
                }
            }

            if (newSelection.isEmpty()) {
                setSelected((E) null);
            } else {
                setSelected(newSelection);
            }
        };
        // noinspection unchecked
        datasource.addCollectionChangeListener(new WeakCollectionChangeListener(datasource, securityCollectionChangeListener));

        itemPropertyChangeListener = e -> {
            List<Column> columns1 = getColumns();
            boolean find = false;
            int i = 0;
            while ((i < columns1.size()) & !find) {
                Object columnId = columns1.get(i).getId();
                if (columnId instanceof MetaPropertyPath) {
                    String propertyName = ((MetaPropertyPath) columnId).getMetaProperty().getName();
                    if (propertyName.equals(e.getProperty())) {
                        find = true;
                    }
                }
                i++;
            }
            if (find) {
                onDataChange();
            }
            packRows();
        };
        // noinspection unchecked
        datasource.addItemPropertyChangeListener(new WeakItemPropertyChangeListener(datasource, itemPropertyChangeListener));

        if (rowsCount != null) {
            rowsCount.setDatasource(datasource);
        }

        collectionDsActionsNotifier = new CollectionDsActionsNotifier(this);
        collectionDsActionsNotifier.bind(datasource);

        for (Action action : getActions()) {
            action.refreshState();
        }

        if (!canBeSorted(datasource))
            setSortable(false);
    }

    protected boolean canBeSorted(CollectionDatasource datasource) {
        //noinspection SimplifiableConditionalExpression
        return datasource instanceof PropertyDatasource ?
                ((PropertyDatasource) datasource).getProperty().getRange().isOrdered() : true;
    }

    protected String getColumnCaption(Object columnId) {
        if (columnId instanceof MetaPropertyPath) {
            return ((MetaPropertyPath) columnId).getMetaProperty().getName();
        } else {
            return columnId.toString();
        }
    }

    protected void setColumnIdentifiers() {
        int i = 0;
        for (TableColumn tableColumn : getAllColumns()) {
            Column column = columnsOrder.get(i++);
            if (!(tableColumn.getIdentifier() instanceof Column)) {
                tableColumn.setIdentifier(column);
            }
        }
    }

    protected List<TableColumn> getAllColumns() {
        return ((TableColumnModelExt) impl.getColumnModel()).getColumns(true);
    }

    protected void onDataChange() {
        for (TableColumn tableColumn : getAllColumns()) {
            TableCellEditor cellEditor = tableColumn.getCellEditor();
            if (cellEditor instanceof DesktopTableCellEditor) {
                ((DesktopTableCellEditor) cellEditor).clearCache();
            }
        }
        repaintImplIfNeeded();
    }

    protected void initChangeListener() {
        tableModel.addChangeListener(new AnyTableModelAdapter.DataChangeListener() {

            private boolean focused = false;
            private ThreadLocal<Set<E>> selectionBackup = new ThreadLocal<>();
            private int scrollRowIndex = -1;

            @Override
            public void beforeChange(boolean structureChanged) {
                if (!structureChanged)
                    return;

                isAdjusting = true;
                focused = impl.isFocusOwner();
                selectionBackup.set(selectedItems);

                JViewport viewport = (JViewport)impl.getParent();
                Point scrollPoint = viewport.getViewPosition();
                scrollRowIndex = impl.rowAtPoint(scrollPoint);
            }

            @Override
            public void afterChange(boolean structureChanged) {
                if (!structureChanged)
                    return;

                isAdjusting = false;
                applySelection(filterSelection(selectionBackup.get()));
                selectionBackup.remove();

                if (focused) {
                    impl.requestFocus();
                } else {
                    if (impl.getCellEditor() != null) {
                        if (!impl.getCellEditor().stopCellEditing()) {
                            impl.getCellEditor().cancelCellEditing();
                        }
                    }
                }

                TableFocusManager focusManager = ((FocusableTable) impl).getFocusManager();
                if (focusManager != null && scrollRowIndex >= 0) {
                    focusManager.scrollToSelectedRow(scrollRowIndex);
                }

                // reassign identifiers for auto created columns
                setColumnIdentifiers();
            }

            @SuppressWarnings("unchecked")
            private Set<E> filterSelection(Set<E> selection) {
                if (selection == null)
                    return Collections.emptySet();

                Set<E> newSelection = new HashSet<>(2 * selection.size());
                for (Entity item : selection) {
                    if (datasource.containsItem(item.getId())) {
                        newSelection.add((E) datasource.getItem(item.getId()));
                    }
                }
                return newSelection;
            }

            private void applySelection(Set<E> selection) {
                int minimalSelectionRowIndex = Integer.MAX_VALUE;
                if (!selection.isEmpty()) {
                    for (Entity entity : selection) {
                        int rowIndex = tableModel.getRowIndex(entity);
                        if (rowIndex < minimalSelectionRowIndex && rowIndex >= 0) {
                            minimalSelectionRowIndex = rowIndex;
                        }
                    }
                }

                setSelected(selection);

                if (!selection.isEmpty()) {
                    if (focused) {
                        impl.requestFocus();
                    } else {
                        if (impl.getCellEditor() != null) {
                            if (!impl.getCellEditor().stopCellEditing()) {
                                impl.getCellEditor().cancelCellEditing();
                            }
                        }
                    }

                    TableFocusManager focusManager = ((FocusableTable) impl).getFocusManager();
                    if (focusManager != null) {
                        focusManager.scrollToSelectedRow(minimalSelectionRowIndex);
                    }
                }
            }

            @Override
            public void dataSorted() {
                clearGeneratedColumnsCache();
                packRows();
            }
        });
    }

    protected void clearGeneratedColumnsCache() {
        for (Column column : columnsOrder) {
            if (tableModel.isGeneratedColumn(column)) {
                TableColumn tableColumn = getColumn(column);
                if (tableColumn != null) {
                    TableCellEditor tableCellEditor = tableColumn.getCellEditor();
                    if (tableCellEditor instanceof DesktopTableCellEditor) {
                        ((DesktopTableCellEditor) tableCellEditor).clearCache();
                    }
                }
            }
        }
    }

    // Get cell editor for editable column
    protected TableCellEditor getCellEditor(int row, int column) {

        TableColumn tableColumn = impl.getColumnModel().getColumn(column);
        if (tableColumn.getIdentifier() instanceof Column) {
            Column columnConf = (Column) tableColumn.getIdentifier();

            if (editableColumns != null
                    && columnConf.getId() instanceof MetaPropertyPath
                    && editableColumns.contains(columnConf.getId())) {

                return tableFieldFactory.createEditComponent(row, columnConf);
            }
        }

        return null;
    }

    protected class EditableColumnTableCellEditor extends AbstractCellEditor implements TableCellEditor {
        protected com.haulmont.cuba.gui.components.Component cellComponent;

        public EditableColumnTableCellEditor(com.haulmont.cuba.gui.components.Component cellComponent) {
            this.cellComponent = cellComponent;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            JComponent composition = DesktopComponentsHelper.getComposition(cellComponent);
            composition.putClientProperty(DesktopTableCellEditor.CELL_EDITOR_TABLE, table);
            return composition;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return DesktopAbstractTable.this.isEditable()
                    && cellComponent instanceof Editable && ((Editable) cellComponent).isEditable();
        }

        @Override
        public Object getCellEditorValue() {
            flush(DesktopComponentsHelper.getComposition(cellComponent));
            impl.requestFocus();
            if (cellComponent instanceof HasValue) {
                return ((Field) cellComponent).getValue();
            }
            return null;
        }

        protected void flush(Component component) {
            if (component instanceof Flushable) {
                ((Flushable) component).flushValue();
            } else if (component instanceof java.awt.Container) {
                for(Component child : ((java.awt.Container) component).getComponents()){
                    flush(child);
                }
            }
        }
    }

    protected class DesktopTableFieldFactory extends AbstractFieldFactory {

        public TableCellEditor createEditComponent(int row, Column columnConf) {
            MetaPropertyPath mpp = (MetaPropertyPath) columnConf.getId();

            Datasource fieldDatasource = getItemDatasource(tableModel.getItem(row));
            // create lookup
            final com.haulmont.cuba.gui.components.Component columnComponent = createField(fieldDatasource,
                    mpp.getMetaProperty().getName(), columnConf.getXmlDescriptor());

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

            if (columnComponent instanceof DesktopCheckBox) {
                JCheckBox checkboxImpl = (JCheckBox) ((DesktopCheckBox) columnComponent).getComponent();
                checkboxImpl.setHorizontalAlignment(SwingConstants.CENTER);
            }

            JComponent composition = DesktopComponentsHelper.getComposition(columnComponent);
            Color color = UIManager.getColor("Table:\"Table.cellRenderer\".background");
            composition.setBackground(new Color(color.getRGB()));
            composition.setForeground(impl.getForeground());
            composition.setFont(impl.getFont());

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

            columnComponent.setParent(DesktopAbstractTable.this);

            return new EditableColumnTableCellEditor(columnComponent);
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
                    throw new IllegalStateException("Options datasource not found: " + optDsName);

                return ds;
            }
        }
    }

    protected void initSelectionListener(final CollectionDatasource datasource) {
        impl.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public void valueChanged(ListSelectionEvent e) {
                        if (e.getValueIsAdjusting() || datasource == null) {
                            return;
                        }

                        selectedItems = getSelected();

                        if (selectedItems.isEmpty()) {
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

                            Entity newItem = selectedItems.iterator().next();
                            Entity dsItem = datasource.getItemIfValid();
                            datasource.setItem(newItem);

                            if (ObjectUtils.equals(dsItem, newItem)) {
                                // in this case item change event will not be generated
                                refreshActionsState();
                            }
                        }
                    }
                }
        );
    }

    protected void setVisibleColumns(List<?> columnsOrder) {
        for (TableColumn tableColumn : getAllColumns()) {
            Column columnIdentifier = (Column) tableColumn.getIdentifier();
            if (!columnsOrder.contains(columnIdentifier.getId())) {
                impl.removeColumn(tableColumn);
            }
        }
    }

    @Override
    public boolean isContextMenuEnabled() {
        return contextMenuEnabled;
    }

    @Override
    public void setContextMenuEnabled(boolean contextMenuEnabled) {
        this.contextMenuEnabled = contextMenuEnabled;
    }

    protected void setEditableColumns(List<MetaPropertyPath> editableColumns) {
        this.editableColumns.clear();
        this.editableColumns.addAll(editableColumns);
    }

    @Override
    public void setRequired(Table.Column column, boolean required, String message) {
        if (required)
            requiredColumns.put(column, message);
        else
            requiredColumns.remove(column);
    }

    @Override
    public void addValidator(Column column, Field.Validator validator) {
    }

    @Override
    public void addValidator(Field.Validator validator) {
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

    @Override
    public List<Column> getNotCollapsedColumns() {
        List<Column> visibleColumns = new LinkedList<>();
        for (Column column : columnsOrder) {
            TableColumnExt columnExt = impl.getColumnExt(column);
            if (columnExt != null && columnExt.isVisible()) {
                visibleColumns.add(column);
            }
        }
        return visibleColumns;
    }

    @Override
    public void setSortable(boolean sortable) {
        this.sortable = sortable && canBeSorted(datasource);
        if (this.sortable) {
            if (tableModel != null && impl.getRowSorter() == null) {
                impl.setRowSorter(new RowSorterImpl(tableModel));
            }
        } else {
            impl.setRowSorter(null);
        }
    }

    @Override
    public boolean isSortable() {
        return sortable;
    }

    @Override
    public void setColumnReorderingAllowed(boolean columnReorderingAllowed) {
        JTableHeader tableHeader = impl.getTableHeader();
        tableHeader.setReorderingAllowed(columnReorderingAllowed);
    }

    @Override
    public boolean getColumnReorderingAllowed() {
        JTableHeader tableHeader = impl.getTableHeader();
        return tableHeader.getReorderingAllowed();
    }

    @Override
    public void setColumnControlVisible(boolean columnCollapsingAllowed) {
        impl.setColumnControlVisible(columnCollapsingAllowed);
    }

    @Override
    public boolean getColumnControlVisible() {
        return impl.isColumnControlVisible();
    }

    @Override
    public void setAggregatable(boolean aggregatable) {
    }

    @Override
    public Map<Object, Object> getAggregationResults() {
        return Collections.emptyMap();
    }

    @Override
    public boolean isAggregatable() {
        return false;
    }

    @Override
    public void setAggregationStyle(AggregationStyle aggregationStyle) {
    }

    @Override
    public AggregationStyle getAggregationStyle() {
        return null;
    }

    @Override
    public void setShowTotalAggregation(boolean showAggregation) {
    }

    @Override
    public boolean isShowTotalAggregation() {
        return false;
    }

    @Override
    public void sortBy(Object propertyId, boolean ascending) {
        if (isSortable()) {
            for (int i = 0; i < columnsOrder.size(); i++) {
                Column column = columnsOrder.get(i);
                if (column.getId().equals(propertyId)) {
                    SortOrder sortOrder = ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING;
                    tableModel.sort(Collections.singletonList(new RowSorter.SortKey(i, sortOrder)));
                    onDataChange();
                    packRows();
                    break;
                }
            }
        }
    }

    @Override
    public void selectAll() {
        if (isMultiSelect()) {
            if (impl.getRowCount() > 0) {
                impl.setRowSelectionInterval(0, impl.getModel().getRowCount() - 1);
            }
        }
    }

    @Override
    public RowsCount getRowsCount() {
        return rowsCount;
    }

    @Override
    public void setRowsCount(RowsCount rowsCount) {
        if (this.rowsCount != null) {
            topPanel.remove(DesktopComponentsHelper.getComposition(this.rowsCount));
        }
        this.rowsCount = rowsCount;
        if (rowsCount != null) {
            topPanel.add(DesktopComponentsHelper.getComposition(rowsCount), BorderLayout.EAST);
            topPanel.setVisible(true);
        }
    }

    @Override
    public void setMultiLineCells(boolean multiLineCells) {
        this.multiLineCells = multiLineCells;
    }

    @Override
    public boolean isMultiLineCells() {
        return multiLineCells;
    }

    @Override
    public void setRowHeaderMode(RowHeaderMode mode) {
    }

    @Override
    public void setStyleProvider(StyleProvider styleProvider) {
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

        refreshCellStyles();
    }

    @Override
    public void addStyleProvider(StyleProvider styleProvider) {
        if (this.styleProviders == null) {
            this.styleProviders = new LinkedList<>();
        }

        if (!this.styleProviders.contains(styleProvider)) {
            this.styleProviders.add(styleProvider);

            refreshCellStyles();
        }
    }

    @Override
    public void removeStyleProvider(StyleProvider styleProvider) {
        if (this.styleProviders != null) {
            if (this.styleProviders.remove(styleProvider)) {
                refreshCellStyles();
            }
        }
    }

    protected void refreshCellStyles() {
        for (Column col : columnsOrder) {
            // generated column handles styles himself
            if (!tableModel.isGeneratedColumn(col)) {
                TableColumn tableColumn = getColumn(col);

                // If column is not hidden by security
                if (tableColumn != null) {
                    boolean useStyledCells = styleProviders != null && !styleProviders.isEmpty();
                    tableColumn.setCellRenderer(useStyledCells ? new StylingCellRenderer() : null);
                }
            }
        }
    }

    @Override
    public void setIconProvider(IconProvider iconProvider) {
        this.iconProvider = iconProvider; // TODO Kozlov: PL-2411.
    }

    @Override
    public int getRowHeaderWidth() {
        return 0; // TODO Kozlov: PL-2411.
    }

    @Override
    public void setRowHeaderWidth(int width) {
        // TODO Kozlov: PL-2411.
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

            ((DatasourceImplementation) fieldDatasource).valid();

            fieldDatasource.setItem(item);
            fieldDatasources.put(item, fieldDatasource);
        }

        return fieldDatasource;
    }

    @Override
    public void addGeneratedColumn(String columnId, ColumnGenerator<E> generator) {
        addGeneratedColumn(columnId, generator, null);
    }

    @Override
    public void addGeneratedColumn(String columnId, ColumnGenerator<E> generator,
                                   Class<? extends com.haulmont.cuba.gui.components.Component> componentClass) {
        checkArgument(columnId != null, "columnId is null");
        checkArgument(generator != null, "generator is null for column id '%s'", columnId);

        addGeneratedColumnInternal(columnId, generator, componentClass);
    }

    protected void addGeneratedColumnInternal(String columnId, ColumnGenerator generator,
                                              Class<? extends com.haulmont.cuba.gui.components.Component> componentClass) {
        Column col = getColumn(columnId);
        Column associatedRuntimeColumn = null;
        if (col == null) {
            col = addRuntimeGeneratedColumn(columnId);
            associatedRuntimeColumn = col;
        }

        tableModel.addGeneratedColumn(col);
        TableColumn tableColumn = getColumn(col);
        DesktopTableCellEditor cellEditor = new DesktopTableCellEditor(this, generator, componentClass);
        tableColumn.setCellEditor(cellEditor);
        tableColumn.setCellRenderer(cellEditor);

        cellEditor.setAssociatedRuntimeColumn(associatedRuntimeColumn);

        generatedColumnsCount++;

        packRows();
        repaintImplIfNeeded();
    }

    protected Column addRuntimeGeneratedColumn(String columnId) {
        // store old cell editors / renderers
        TableCellEditor[] cellEditors = new TableCellEditor[tableModel.getColumnCount() + 1];
        TableCellRenderer[] cellRenderers = new TableCellRenderer[tableModel.getColumnCount() + 1];

        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            Column tableModelColumn = tableModel.getColumn(i);

            if (tableModel.isGeneratedColumn(tableModelColumn)) {
                TableColumn tableColumn = getColumn(tableModelColumn);
                cellEditors[i] = tableColumn.getCellEditor();
                cellRenderers[i] = tableColumn.getCellRenderer();
            }
        }

        // if column with columnId does not exists then add new to model
        Column col = new Column(columnId, columnId);
        col.setEditable(false);

        columns.put(col.getId(), col);
        // do not touch columnsOrder, it will be synced from table model
        if (tableModel != null) {
            tableModel.addColumn(col);
        }

        // reassign column identifiers
        setColumnIdentifiers();

        // reattach old generated columns
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            Column tableModelColumn = tableModel.getColumn(i);

            if (tableModel.isGeneratedColumn(tableModelColumn)) {
                TableColumn tableColumn = getColumn(tableModelColumn);
                if (cellEditors[i] != null) {
                    tableColumn.setCellEditor(cellEditors[i]);
                }
                if (cellRenderers[i] != null) {
                    tableColumn.setCellRenderer(cellRenderers[i]);
                }
            }
        }
        return col;
    }

    @Override
    public void removeGeneratedColumn(String columnId) {
        checkArgument(columnId != null, "columnId is null");
        Column col = getColumn(columnId);
        if (col != null) {
            boolean oldContentRepaintEnabled = isContentRepaintEnabled();
            setContentRepaintEnabled(false);

            TableColumn targetTableColumn = getColumn(col);
            TableCellEditor cellEditor = targetTableColumn.getCellEditor();
            if (cellEditor instanceof DesktopTableCellEditor) {
                Column associatedRuntimeColumn = ((DesktopTableCellEditor) cellEditor).getAssociatedRuntimeColumn();

                removeColumn(associatedRuntimeColumn);
            }

            tableModel.removeGeneratedColumn(col);
            generatedColumnsCount--;

            packRows();
            repaintImplIfNeeded();
            setContentRepaintEnabled(oldContentRepaintEnabled);
        }
    }

    @Override
    public void addAggregationProperty(String columnId, AggregationInfo.Type type) {
    }

    @Override
    public void addAggregationProperty(Column columnId, AggregationInfo.Type type) {
    }

    @Override
    public void removeAggregationProperty(String columnId) {
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
        TableColumn tableColumn = getColumn(column);

        // If column is not hidden by security
        if (tableColumn != null) {
            tableColumn.setHeaderValue(caption);
        }
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
        // not supported for desktop
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

        TableColumn tableColumn = getColumn(column);
        if (tableColumn instanceof TableColumnExt) {
            ((TableColumnExt) tableColumn).setVisible(!collapsed);
        }
    }

    @Override
    public void setColumnAlignment(Column column, ColumnAlignment alignment) {
        checkNotNullArgument(column, "column must be non null");

        if (column.getAlignment() != alignment) {
            column.setAlignment(alignment);
        }
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
        readjustColumns();
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
    public void addPrintable(String columnId, Printable printable) {
        printables.put(columnId, printable);
    }

    @Override
    public void removePrintable(String columnId) {
        printables.remove(columnId);
    }

    @Override
    @Nullable
    public Printable getPrintable(Column column) {
        checkNotNullArgument(column, "column is null");

        return getPrintable(String.valueOf(column.getId()));
    }

    @Nullable
    @Override
    public Printable getPrintable(String columnId) {
        checkArgument(columnId != null, "columnId is null");

        Printable printable = printables.get(columnId);
        if (printable != null) {
            return printable;
        } else {
            Column column = getColumn(columnId);
            if (column != null) {
                TableColumn tableColumn = getColumn(column);
                TableCellEditor cellEditor = tableColumn.getCellEditor();
                if (cellEditor instanceof DesktopTableCellEditor) {
                    ColumnGenerator columnGenerator = ((DesktopTableCellEditor) cellEditor).getColumnGenerator();
                    if (columnGenerator instanceof Printable) {
                        return (Printable) columnGenerator;
                    }
                }
            }
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void repaint() {
        TableCellEditor cellEditor = impl.getCellEditor();
        if (cellEditor instanceof DesktopTableCellEditor) {
            ((DesktopTableCellEditor) cellEditor).clearCache();
        }

        List<TableColumn> implColumns = impl.getColumns();
        for (Column column : getColumns()) {
            TableColumn tableColumn = null;
            for (TableColumn implColumn : implColumns) {
                if (column.equals((implColumn.getIdentifier()))) {
                    tableColumn = implColumn;
                    break;
                }
            }
            // column may be hidden
            if (tableColumn != null) {
                TableCellEditor columnCellEditor = tableColumn.getCellEditor();
                if (columnCellEditor instanceof DesktopTableCellEditor) {
                    ((DesktopTableCellEditor) columnCellEditor).clearCache();
                }
            }
        }
        packRows();
        repaintImplIfNeeded();
    }

    protected void repaintImplIfNeeded() {
        if (contentRepaintEnabled) {
            impl.repaint();
        }
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (isEnabled() != enabled) {
            super.setEnabled(enabled);
        }
    }

    @Override
    public void updateEnabled() {
        super.updateEnabled();

        impl.setEnabled(isEnabledWithParent());

        if (buttonsPanel != null) {
            ((DesktopButtonsPanel)buttonsPanel).setParentEnabled(isEnabledWithParent());
        }
    }

    @Override
    public ButtonsPanel getButtonsPanel() {
        return buttonsPanel;
    }

    @Override
    public void setButtonsPanel(ButtonsPanel panel) {
        if (buttonsPanel != null) {
            topPanel.remove(DesktopComponentsHelper.unwrap(buttonsPanel));
            buttonsPanel.setParent(null);
        }
        buttonsPanel = panel;
        if (panel != null) {
            if (panel.getParent() != null && panel.getParent() != this) {
                throw new IllegalStateException("Component already has parent");
            }

            topPanel.add(DesktopComponentsHelper.unwrap(panel), BorderLayout.WEST);
            topPanel.setVisible(true);

            panel.setParent(this);
        }
    }

    @Override
    public void usePresentations(boolean b) {
    }

    @Override
    public boolean isUsePresentations() {
        return false;
    }

    @Override
    public void resetPresentation() {
        if (defaultSettings != null) {
            applySettings(defaultSettings.getRootElement());
        }
    }

    @Override
    public void loadPresentations() {
    }

    @Override
    public Presentations getPresentations() {
        return null;
    }

    @Override
    public void applyPresentation(Object id) {
    }

    @Override
    public void applyPresentationAsDefault(Object id) {
    }

    @Override
    public Object getDefaultPresentationId() {
        return null;
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

        tableSettings.apply(element, isSortable());
    }

    @Override
    public boolean saveSettings(Element element) {
        return isSettingsEnabled() && tableSettings.saveSettings(element);
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
    public boolean isMultiSelect() {
        return impl.getSelectionModel().getSelectionMode() != ListSelectionModel.SINGLE_SELECTION;
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        if (multiselect) {
            impl.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        } else {
            impl.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
    }

    @Override
    public E getSingleSelected() {
        Set<E> selected = getSelected();
        return selected.isEmpty() ? null : selected.iterator().next();
    }

    @Override
    public Set<E> getSelected() {
        Set<E> set = new HashSet<>();
        int[] rows = impl.getSelectedRows();
        for (int row : rows) {
            int modelRow = impl.convertRowIndexToModel(row);
            Object item = tableModel.getItem(modelRow);
            if (item != null) {
                //noinspection unchecked
                set.add((E) item);
            }
        }
        return set;
    }

    @Override
    public void setSelected(E item) {
        if (item != null) {
            setSelected(Collections.singleton(item));
        } else {
            setSelected(Collections.<E>emptySet());
        }
    }

    @Override
    public void setSelected(Collection<E> items) {
        if (items == null) {
            items = Collections.emptyList();
        }
        for (Entity item : items) {
            // noinspection unchecked
            if (!datasource.containsItem(item.getId())) {
                throw new IllegalStateException("Datasource does not contain specified item: " + item.getId());
            }
        }
        impl.clearSelection();
        if (!items.isEmpty()) {
            List<Integer> indexes = getSelectionIndexes(items);
            if (!indexes.isEmpty()) {
                applySelectionIndexes(indexes);
            }
        }
    }

    @Override
    protected void attachAction(Action action) {
        if (action instanceof Action.HasTarget) {
            ((Action.HasTarget) action).setTarget(this);
        }

        super.attachAction(action);
    }

    protected void applySelectionIndexes(List<Integer> indexes) {
        Collections.sort(indexes);
        ListSelectionModel model = impl.getSelectionModel();
        model.setValueIsAdjusting(true);
        int lastOpened = indexes.get(0);
        int current = indexes.get(0);
        for (Integer index : indexes) {
            if (index > current + 1) {
                model.addSelectionInterval(lastOpened, current);
                lastOpened = index;
            }
            current = index;
        }
        model.addSelectionInterval(lastOpened, current);
        model.setValueIsAdjusting(false);
    }

    protected List<Integer> getSelectionIndexes(Collection<? extends Entity> items) {
        if (items.isEmpty()) {
            return Collections.emptyList();
        }
        List<Integer> indexes = Lists.newArrayList();
        if (datasource instanceof CollectionDatasource.Ordered) {
            HashSet<Entity> itemSet = new HashSet<>(items);
            int itemIndex = 0;
            CollectionDatasource.Ordered orderedDs = (CollectionDatasource.Ordered) datasource;
            Object id = orderedDs.firstItemId();
            while (id != null && !itemSet.isEmpty()) {
                int rowIndex = impl.convertRowIndexToView(itemIndex);
                // noinspection unchecked
                Entity itemById = datasource.getItem(id);
                if (itemSet.contains(itemById)) {
                    indexes.add(rowIndex);
                    itemSet.remove(itemById);
                }
                // noinspection unchecked
                id = orderedDs.nextItemId(id);
                itemIndex++;
            }
        } else {
            for (Entity item : items) {
                int idx = tableModel.getRowIndex(item);
                if (idx != -1) {
                    indexes.add(impl.convertColumnIndexToView(idx));
                }
            }
        }
        return indexes;
    }

    @Override
    public CollectionDatasource getDatasource() {
        return datasource;
    }

    @Override
    public void refresh() {
        if (datasource != null) {
            datasource.refresh();
            packRows();
            repaintImplIfNeeded();
        }
    }

    protected JPopupMenu createPopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem;
        for (final Action action : actionList) {
            if (StringUtils.isNotBlank(action.getCaption())
                    && action.isVisible()) {
                menuItem = new JMenuItem(action.getCaption());
                if (action.getIcon() != null) {
                    menuItem.setIcon(App.getInstance().getResources().getIcon(action.getIcon()));
                }
                if (action.getShortcut() != null) {
                    menuItem.setAccelerator(convertKeyCombination(action.getShortcut()));
                }
                menuItem.setEnabled(action.isEnabled());
                menuItem.addActionListener(
                        new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                action.actionPerform(DesktopAbstractTable.this);
                            }
                        }
                );
                popup.add(menuItem);
            }
        }
        return popup;
    }

    /**
     * Returns the preferred height of a row.
     * The result is equal to the tallest cell in the row.
     *
     * @param rowIndex row index
     * @return row height
     */
    public int getPreferredRowHeight(int rowIndex) {
        // Get the current default height for all rows
        int height = impl.getRowHeight();

        // Determine highest cell in the row
        for (int c = 0; c < impl.getColumnCount(); c++) {
            TableCellRenderer renderer = impl.getCellRenderer(rowIndex, c);
            Component comp = impl.prepareRenderer(renderer, rowIndex, c);
            int componentHeight = comp.getPreferredSize().height;
            height = Math.max(height, componentHeight);
        }
        return height;
    }

    protected void applyFont(JTable table, Font font) {
        Graphics graphics = table.getGraphics();
        if (graphics != null) {
            FontMetrics metrics = graphics.getFontMetrics(font);
            defaultRowHeight = metrics.getHeight() + DEFAULT_ROW_MARGIN;
            fontInitialized = true;
            if (impl != null) {
                packRows();
            }
        }
    }

    /**
     * Sets the height of each row into the preferred height of the tallest cell in that row.
     */
    public void packRows() {
        if (!contentRepaintEnabled) {
            return;
        }

        impl.setRowHeight(defaultRowHeight);

        for (Column column : columnsOrder) {
            if (column.isEditable()) {
                impl.setRowHeight(defaultEditableRowHeight);
                break;
            }
        }

        if (allColumnsAreInline()) {
            return;
        }

        int preferredRowHeight = -1;
        boolean equalsRowHeight = true;

        StopWatch sw = new Log4JStopWatch("DAT packRows " + id);
        for (int r = 0; r < impl.getRowCount(); r++) {
            int h = getPreferredRowHeight(r);

            if (preferredRowHeight == -1) {
                preferredRowHeight = h;
            } else if (preferredRowHeight != h) {
                equalsRowHeight = false;
            }

            if (impl.getRowHeight(r) != h) {
                impl.setRowHeight(r, h);
            }
        }

        if (equalsRowHeight && preferredRowHeight > 0) {
            impl.setRowHeight(preferredRowHeight);
        }

        sw.stop();
    }

    protected boolean allColumnsAreInline() {
        if (generatedColumnsCount <= 0) {
            return true;
        }

        for (Column column : columnsOrder) {
            if (!tableModel.isGeneratedColumn(column)) {
                continue;
            }

            TableColumn tableColumn = getColumn(column);
            if (tableColumn != null) {
                DesktopTableCellEditor cellEditor = (DesktopTableCellEditor) tableColumn.getCellEditor();
                if (cellEditor != null) {
                    boolean inline = cellEditor.isInline();
                    if (!inline) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    protected TableColumn getColumn(Column column) {
        List<TableColumn> tableColumns = getAllColumns();

        for (TableColumn tableColumn : tableColumns) {
            if (column.equals(tableColumn.getIdentifier())) {
                return tableColumn;
            }
        }

        return null;
    }

    @Override
    public void addColumnCollapsedListener(ColumnCollapseListener columnCollapsedListener) {
    }

    @Override
    public void removeColumnCollapseListener(ColumnCollapseListener columnCollapseListener) {
    }

    @Override
    public void setClickListener(String columnId, CellClickListener clickListener) {
    }

    @Override
    public void removeClickListener(String columnId) {
    }

    public AnyTableModelAdapter getTableModel() {
        return tableModel;
    }

    public boolean isContentRepaintEnabled() {
        return contentRepaintEnabled;
    }

    public void setContentRepaintEnabled(boolean contentRepaintEnabled) {
        if (this.contentRepaintEnabled != contentRepaintEnabled) {
            this.contentRepaintEnabled = contentRepaintEnabled;

            packRows();
            repaintImplIfNeeded();
        }
    }

    protected void applyStylename(boolean isSelected, boolean hasFocus, Component component, String style) {
        if (style == null) {
            return;
        }

        DesktopTheme theme = App.getInstance().getTheme();
        if (theme != null) {
            HashSet<String> properties = new HashSet<>();

            if (hasFocus) {
                properties.add("focused");
            } else if (isSelected) {
                properties.add("selected");
            } else {
                properties.add("unselected");
            }
            theme.applyStyle(component, style, properties);
        }
    }

    protected String getStylename(JTable table, int row, int column) {
        if (styleProviders == null) {
            return null;
        }

        Entity item = tableModel.getItem(row);
        int modelColumn = table.convertColumnIndexToModel(column);
        Object property = columnsOrder.get(modelColumn).getId();

        String joinedStyle = null;
        for (StyleProvider styleProvider : styleProviders) {
            //noinspection unchecked
            String styleName = styleProvider.getStyleName(item, property.toString());
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

    protected TableCellEditor getColumnEditor(int column) {
        TableColumn tableColumn = impl.getColumnModel().getColumn(column);
        if (tableColumn.getIdentifier() instanceof Table.Column) {
            Table.Column columnConf = (Table.Column) tableColumn.getIdentifier();

            if (columnConf.getId() instanceof MetaPropertyPath
                    && !(isEditable() && columnConf.isEditable())
                    && !getTableModel().isGeneratedColumn(columnConf)) {
                MetaPropertyPath propertyPath = (MetaPropertyPath) columnConf.getId();

                final CellProvider cellProvider = getCustomCellEditor(propertyPath);
                if (cellProvider != null) {
                    return new CellProviderEditor(cellProvider);
                }
            }
        }
        return null;
    }

    protected TableCellRenderer getColumnRenderer(int column) {
        TableColumn tableColumn = impl.getColumnModel().getColumn(column);
        if (tableColumn.getIdentifier() instanceof Table.Column) {
            Table.Column columnConf = (Table.Column) tableColumn.getIdentifier();
            if (columnConf.getId() instanceof MetaPropertyPath
                    && !(isEditable() && columnConf.isEditable())
                    && !getTableModel().isGeneratedColumn(columnConf)) {
                MetaPropertyPath propertyPath = (MetaPropertyPath) columnConf.getId();

                final CellProvider cellViewProvider = getCustomCellView(propertyPath);
                if (cellViewProvider != null) {
                    return new CellProviderRenderer(cellViewProvider);
                } else if (multiLineCells && String.class == columnConf.getType()) {
                    return new MultiLineTableCellRenderer();
                }
            }
        }
        return null;
    }

    protected boolean isCustomCellEditable(int row, int column) {
        TableColumn tableColumn = impl.getColumnModel().getColumn(column);
        if (tableColumn.getIdentifier() instanceof Table.Column) {
            Table.Column columnConf = (Table.Column) tableColumn.getIdentifier();
            if (columnConf.getId() instanceof MetaPropertyPath && !getTableModel().isGeneratedColumn(columnConf)) {
                return isCustomCellEditable(tableModel.getItem(row), (MetaPropertyPath) columnConf.getId());
            }
        }
        return false;
    }

    @SuppressWarnings("UnusedParameters")
    protected CellProvider getCustomCellView(MetaPropertyPath mpp) {
        return null;
    }

    @SuppressWarnings("UnusedParameters")
    protected CellProvider getCustomCellEditor(MetaPropertyPath mpp) {
        return null;
    }

    @SuppressWarnings("UnusedParameters")
    protected boolean isCustomCellEditable(Entity e, MetaPropertyPath mpp) {
        return false;
    }

    @Override
    public void setId(String id) {
        super.setId(id);

        if (id != null && App.getInstance().isTestMode()) {
            getComposition().setName(id + "_composition");
        }
    }

    @Override
    public void assignAutoDebugId() {
        super.assignAutoDebugId();

        if (buttonsPanel != null) {
            for (com.haulmont.cuba.gui.components.Component subComponent : buttonsPanel.getComponents()) {
                if (subComponent instanceof DesktopAbstractComponent) {
                    ((DesktopAbstractComponent) subComponent).assignAutoDebugId();
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

    @Override
    public void showCustomPopup(com.haulmont.cuba.gui.components.Component popupComponent) {
    }

    @Override
    public void showCustomPopupActions(List<Action> actions) {
    }

    @Override
    public void setColumnSortable(String columnId, boolean sortable) {
    }

    @Override
    public boolean getColumnSortable(String columnId) {
        return true;
    }

    @Override
    public void setColumnSortable(Column column, boolean sortable) {
    }

    @Override
    public boolean getColumnSortable(Column column) {
        return true;
    }

    @Override
    public void setColumnHeaderVisible(boolean visible){
        columnHeaderVisible = visible;
    }

    @Override
    public boolean isColumnHeaderVisible(){
        return columnHeaderVisible;
    }

    @Override
    public void setShowSelection(boolean showSelection) {
        this.showSelection = showSelection;
    }

    @Override
    public boolean isShowSelection() {
        return showSelection;
    }

    /**
     * Uses delegate renderer to create cell component.
     * Then applies desktop styles to cell component.
     */
    protected class StylingCellRenderer implements TableCellRenderer {

        private TableCellRenderer delegate;

        public StylingCellRenderer(TableCellRenderer delegate) {
            this.delegate = delegate;
        }

        public StylingCellRenderer() {
        }

        public TableCellRenderer getDelegate() {
            return delegate;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            TableCellRenderer renderer = delegate;
            if (renderer == null) {
                renderer = table.getDefaultRenderer(value != null ? value.getClass() : Object.class);
            }
            java.awt.Component component = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (0 <= row) {
                String style = getStylename(table, row, column);
                applyStylename(isSelected, hasFocus, component, style);
            }
            return component;
        }
    }

    protected class CellProviderEditor extends AbstractCellEditor implements TableCellEditor {
        private final CellProvider cellProvider;

        public CellProviderEditor(CellProvider cellProvider) {
            this.cellProvider = cellProvider;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            Entity item = getTableModel().getItem(row);
            TableColumn tableColumn = impl.getColumnModel().getColumn(column);
            Column columnConf = (Column) tableColumn.getIdentifier();

            Component component = cellProvider.generateCell(item, (MetaPropertyPath) columnConf.getId());

            if (component == null) {
                return new JLabel("");
            }

            if (component instanceof JComponent) {
                ((JComponent) component).putClientProperty(DesktopTableCellEditor.CELL_EDITOR_TABLE, impl);
            }

            return component;
        }

        @Override
        public Object getCellEditorValue() {
            DesktopComponentsHelper.flushCurrentInputField();
            return "";
        }
    }

    protected class CellProviderRenderer implements TableCellRenderer {
        private final CellProvider cellViewProvider;

        public CellProviderRenderer(CellProvider cellViewProvider) {
            this.cellViewProvider = cellViewProvider;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Entity item = getTableModel().getItem(row);
            TableColumn tableColumn = impl.getColumnModel().getColumn(column);
            Column columnConf = (Column) tableColumn.getIdentifier();

            Component component = cellViewProvider.generateCell(item, (MetaPropertyPath) columnConf.getId());

            if (component == null) {
                return new JLabel("");
            }

            if (component instanceof JComponent) {
                ((JComponent) component).putClientProperty(DesktopTableCellEditor.CELL_EDITOR_TABLE, impl);
            }

            String style = getStylename(table, row, column);
            applyStylename(isSelected, hasFocus, component, style);

            return component;
        }
    }

    protected class MultiLineTableCellRenderer extends JTextArea implements TableCellRenderer {
        private List<List<Integer>> rowColHeight = new ArrayList<>();

        public MultiLineTableCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setBorder(new EmptyBorder(0,0,0,0));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                Color background = UIManager.getDefaults().getColor("Table:\"Table.cellRenderer\".background");
                if (row % 2 == 1) {
                    Color alternateColor = UIManager.getDefaults().getColor("Table.alternateRowColor");
                    if (alternateColor != null) {
                        background = alternateColor;
                    }
                }
                setBackground(background);
            }
            setFont(table.getFont());

            Border border = null;
            if (isSelected) {
                border = UIManager.getDefaults().getBorder("Table.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = UIManager.getDefaults().getBorder("Table.focusCellHighlightBorder");
            }

            if (hasFocus) {
                setBorder(border);
                if (table.isCellEditable(row, column)) {
                    setForeground(UIManager.getColor("Table.focusCellForeground"));
                    setBackground(UIManager.getColor("Table.focusCellBackground"));
                }
            } else {
                setBorder(UIManager.getDefaults().getBorder("Table.cellNoFocusBorder"));
            }

            if (value != null) {
                setText(value.toString());
            } else {
                setText("");
            }
            adjustRowHeight(table, row, column);
            return this;
        }

        /**
         * Calculate the new preferred height for a given row, and sets the height on the table.
         */
        private void adjustRowHeight(JTable table, int row, int column) {
            //The trick to get this to work properly is to set the width of the column to the
            //textarea. The reason for this is that getPreferredSize(), without a width tries
            //to place all the text in one line. By setting the size with the with of the column,
            //getPreferredSize() returnes the proper height which the row should have in
            //order to make room for the text.
            int cWidth = table.getTableHeader().getColumnModel().getColumn(column).getWidth();
            setSize(new Dimension(cWidth, 1000));
            int prefH = getPreferredSize().height;
            while (rowColHeight.size() <= row) {
                rowColHeight.add(new ArrayList<Integer>(column));
            }
            List<Integer> colHeights = rowColHeight.get(row);
            while (colHeights.size() <= column) {
                colHeights.add(0);
            }
            colHeights.set(column, prefH);
            int maxH = prefH;
            for (Integer colHeight : colHeights) {
                if (colHeight > maxH) {
                    maxH = colHeight;
                }
            }
            if (table.getRowHeight(row) != maxH) {
                table.setRowHeight(row, maxH);
            }
        }
    }
}