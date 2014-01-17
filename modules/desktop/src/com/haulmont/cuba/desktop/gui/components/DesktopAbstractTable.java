/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.google.common.collect.Lists;
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
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.CollectionDsActionsNotifier;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.ColumnControlButton;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.table.TableColumnModelExt;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import javax.annotation.Nullable;
import javax.swing.AbstractAction;
import javax.swing.*;
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

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author krivopustov
 * @version $Id$
 */
public abstract class DesktopAbstractTable<C extends JXTable>
        extends DesktopAbstractActionsHolderComponent<C>
        implements Table {

    protected static final int DEFAULT_ROW_MARGIN = 4;

    protected boolean allowPopupMenu = true;
    protected MigLayout layout;
    protected JPanel panel;
    protected JPanel topPanel;
    protected JScrollPane scrollPane;
    protected AnyTableModelAdapter tableModel;
    protected CollectionDatasource datasource;
    protected ButtonsPanel buttonsPanel;
    protected RowsCount rowsCount;
    protected Map<Object, Column> columns = new HashMap<>();
    protected List<Table.Column> columnsOrder = new ArrayList<>();
    protected boolean sortable = true;
    protected TableSettings tableSettings;
    protected boolean editable;
    protected StyleProvider styleProvider;
    protected IconProvider iconProvider;

    protected Action itemClickAction;
    protected Action enterPressAction;

    protected boolean columnsInitialized = false;
    protected int generatedColumnsCount = 0;

    // Indicates that model is being changed.
    protected boolean isAdjusting = false;

    protected DesktopTableFieldFactory tableFieldFactory = new DesktopTableFieldFactory();

    protected List<MetaPropertyPath> editableColumns = new LinkedList<>();

    protected Map<Table.Column, String> requiredColumns = new HashMap<>();

    protected Map<String, KeyCombination> shortcuts = new HashMap<>();

    protected Security security = AppBeans.get(Security.class);

//    disable for #PL-2035
    // Disable listener that points selection model to folow ds item.
//    protected boolean disableItemListener = false;

    protected boolean fontInitialized = false;

    protected int defaultRowHeight = 24;
    protected int defaultEditableRowHeight = 28;

    protected Set<Entity> selectedItems = Collections.emptySet();

    protected Map<String, Printable> printables = new HashMap<>();

    protected Map<Entity, Datasource> fieldDatasources = new WeakHashMap<>();

    // Manual control for content repaint process
    protected boolean contentRepaintEnabled = true;

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
                        if (e.getClickCount() == 2) {
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
                        if (e.isPopupTrigger()) {
                            // select row
                            Point p = e.getPoint();
                            int rowNumber = impl.convertRowIndexToModel(impl.rowAtPoint(p));
                            ListSelectionModel model = impl.getSelectionModel();
                            model.setSelectionInterval(rowNumber, rowNumber);
                            // show popup menu
                            if (allowPopupMenu) {
                                createPopupMenu().show(e.getComponent(), e.getX(), e.getY());
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

        // Ability to configure fonts in table
        // Add action to column control
        String configureFontsLabel = AppBeans.get(Messages.class).getMessage(
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

    @Override
    public void addAction(Action action) {
        super.addAction(action);

        removeShortcut(action);

        if (action.getShortcut() != null) {
            addShortcutActionBridge(action.getId(), action.getShortcut());
        }
    }

    @Override
    public void removeAction(Action action) {
        super.removeAction(action);

        removeShortcut(action);
    }

    protected void removeShortcut(Action action) {
        String actionId = action.getId();
        if (shortcuts.containsKey(actionId)) {
            KeyCombination kc = shortcuts.get(actionId);

            impl.getInputMap().remove(DesktopComponentsHelper.convertKeyCombination(kc));
            impl.getActionMap().remove(actionId);
            shortcuts.remove(actionId);
        }
    }

    protected void addShortcutActionBridge(final String actionId, final KeyCombination keyCombination) {
        if ((keyCombination.getModifiers() == null || keyCombination.getModifiers().length == 0)
                && keyCombination.getKey() == KeyCombination.Key.ENTER) {
            return;
        }

        impl.getInputMap().put(DesktopComponentsHelper.convertKeyCombination(keyCombination), actionId);
        impl.getActionMap().put(actionId, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Action action = getAction(actionId);
                if ((action != null) && (action.isEnabled()) && (action.isVisible())) {
                    action.actionPerform(DesktopAbstractTable.this);
                }
            }
        });
        shortcuts.put(actionId, keyCombination);
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                adjustColumnHeaders();
            }
        });
    }

    protected void adjustColumnHeaders() {
        List<TableColumn> notInited = new LinkedList<>();
        int summaryWidth = 0;
        int componentWidth = impl.getParent().getWidth();

        // take into account only visible columns
        Enumeration<TableColumn> columnEnumeration = impl.getColumnModel().getColumns();
        int i = 0;
        while (columnEnumeration.hasMoreElements()) {
            TableColumn tableColumn = columnEnumeration.nextElement();
            Column column = columnsOrder.get(i++);

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
    }

    @Override
    public void removeColumn(Column column) {
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
    }

    @Override
    public void setDatasource(final CollectionDatasource datasource) {
        UserSession userSession = AppBeans.get(UserSessionSource.class).getUserSession();
        MetadataTools metadataTools = AppBeans.get(MetadataTools.class);

        final Collection<Object> properties;
        if (this.columns.isEmpty()) {
            Collection<MetaPropertyPath> paths = metadataTools.getViewPropertyPaths(datasource.getView(), datasource.getMetaClass());
            for (MetaPropertyPath metaPropertyPath : paths) {
                MetaProperty property = metaPropertyPath.getMetaProperty();
                if (!property.getRange().getCardinality().isMany() && !metadataTools.isSystem(property)) {
                    Table.Column column = new Table.Column(metaPropertyPath);

                    column.setCaption(AppBeans.get(MessageTools.class).getPropertyCaption(property));
                    column.setType(metaPropertyPath.getRangeJavaClass());

                    Element element = DocumentHelper.createElement("column");
                    column.setXmlDescriptor(element);

                    addColumn(column);
                }
            }
        }
        properties = this.columns.keySet();

        this.datasource = datasource;

        datasource.addListener(new CollectionDsListenerAdapter<Entity>() {
            @Override
            public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
                switch (operation) {
                    case CLEAR:
                    case REFRESH:
                        fieldDatasources.clear();
                        break;

                    case UPDATE:
                    case REMOVE:
                        for (Entity entity : items) {
                            fieldDatasources.remove(entity);
                        }
                        break;
                }
            }
        });

        initTableModel(datasource);

        initChangeListener();

        setColumnIdentifiers();

        if (isSortable()) {
            impl.setRowSorter(new RowSorterImpl(tableModel));
        }

        initSelectionListener(datasource);

        List<MetaPropertyPath> editableColumns = null;
        if (isEditable()) {
            editableColumns = new LinkedList<>();
        }

        for (final Object property : properties) {
            final Table.Column column = this.columns.get(property);

            final String caption;
            if (column != null) {
                caption = StringUtils.capitalize(column.getCaption() != null ? column.getCaption() : getColumnCaption(property));
            } else {
                caption = StringUtils.capitalize(getColumnCaption(property));
            }

            setColumnHeader(property, caption);

            if (column != null) {
                if (editableColumns != null && column.isEditable() && (property instanceof MetaPropertyPath)) {
                    MetaProperty colMetaProperty = ((MetaPropertyPath) property).getMetaProperty();
                    MetaClass colMetaClass = colMetaProperty.getDomain();
                    if (userSession.isEntityAttrPermitted(colMetaClass, colMetaProperty.getName(), EntityAttrAccess.MODIFY)) {
                        editableColumns.add((MetaPropertyPath) column.getId());
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
        }

        setVisibleColumns(columnsOrder);

        if (AppBeans.get(UserSessionSource.class).getUserSession().isSpecificPermitted(ShowInfoAction.ACTION_PERMISSION)) {
            ShowInfoAction action = (ShowInfoAction) getAction(ShowInfoAction.ACTION_ID);
            if (action == null) {
                action = new ShowInfoAction();
                addAction(action);
            }
            action.setDatasource(datasource);
        }

        datasource.addListener(
                new CollectionDsListenerAdapter<Entity>() {
                    @Override
                    public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
                        onDataChange();
                        packRows();

                        // #PL-2035, reload selection from ds
                        Set<Entity> selectedItems = getSelected();
                        if (selectedItems == null) {
                            selectedItems = Collections.emptySet();
                        }

                        Set<Entity> newSelection = new HashSet<>();
                        for (Entity entity : selectedItems) {
                            if (ds.containsItem(entity.getId())) {
                                newSelection.add(entity);
                            }
                        }

                        if (newSelection.isEmpty()) {
                            setSelected((Entity) null);
                        } else {
                            setSelected(newSelection);
                        }
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        List<Column> columns = getColumns();
                        boolean find = false;
                        int i = 0;
                        while ((i < columns.size()) & !find) {
                            Object columnId = columns.get(i).getId();
                            if (columnId instanceof MetaPropertyPath) {
                                String propertyName = ((MetaPropertyPath) columnId).getMetaProperty().getName();
                                if (propertyName.equals(property)) {
                                    find = true;
                                }
                            }
                            i++;
                        }
                        if (find) {
                            onDataChange();
                        }
                        packRows();
                    }

//                    disabled for #PL-2035
//                    @Override
//                    public void itemChanged(Datasource<Entity> ds, @Nullable Entity prevItem, @Nullable Entity item) {
//                        if (!disableItemListener && !selectedItems.contains(item)) {
//                            setSelected(item);
//                        }
//                    }
                }
        );

        if (rowsCount != null) {
            rowsCount.setDatasource(datasource);
        }

        datasource.addListener(new CollectionDsActionsNotifier(this));

        for (Action action : getActions()) {
            action.refreshState();
        }
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
            tableColumn.setIdentifier(column);
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
            private ThreadLocal<Set<Entity>> selectionBackup = new ThreadLocal<>();

            @Override
            public void beforeChange(boolean structureChanged) {
                if (!structureChanged)
                    return;

                isAdjusting = true;
                focused = impl.isFocusOwner();
                selectionBackup.set(selectedItems);
            }

            @Override
            public void afterChange(boolean structureChanged) {
                if (!structureChanged)
                    return;

                isAdjusting = false;
                applySelection(filterSelection(selectionBackup.get()));
                selectionBackup.remove();
            }

            @SuppressWarnings("unchecked")
            private Set<Entity> filterSelection(Set<Entity> selection) {
                if (selection == null)
                    return Collections.emptySet();

                Set<Entity> newSelection = new HashSet<>(2 * selection.size());
                for (Entity item : selection) {
                    if (datasource.containsItem(item.getId())) {
                        newSelection.add(datasource.getItem(item.getId()));
                    }
                }
                return newSelection;
            }

            @SuppressWarnings("unchecked")
            private void applySelection(Set<Entity> selection) {
                if (focused && !selection.isEmpty()) {
                    int minimalSelectionRowIndex = Integer.MAX_VALUE;

                    if (datasource instanceof CollectionDatasource.Ordered) {
                        CollectionDatasource.Ordered orderedDs = (CollectionDatasource.Ordered) datasource;
                        Object itemId = orderedDs.firstItemId();

                        int entityIndex = 0;
                        while (itemId != null && minimalSelectionRowIndex == Integer.MAX_VALUE) {
                            if (selection.contains(datasource.getItem(itemId))) {
                                minimalSelectionRowIndex = entityIndex;
                            }
                            itemId = orderedDs.nextItemId(itemId);
                            entityIndex++;
                        }
                    } else {
                        for (Entity entity : selection) {
                            int rowIndex = tableModel.getRowIndex(entity);
                            if (rowIndex < minimalSelectionRowIndex && rowIndex >= 0) {
                                minimalSelectionRowIndex = rowIndex;
                            }
                        }
                    }

                    TableFocusManager focusManager = ((FocusableTable) impl).getFocusManager();
                    if (focusManager != null) {
                        focusManager.focusSelectedRow(minimalSelectionRowIndex);
                    }
                } else if (impl.getCellEditor() != null) {
                    if (!impl.getCellEditor().stopCellEditing()) {
                        impl.getCellEditor().cancelCellEditing();
                    }
                }

                setSelected(selection);
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
                    DesktopTableCellEditor cellEditor = (DesktopTableCellEditor) tableColumn.getCellEditor();
                    cellEditor.clearCache();
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
            return DesktopComponentsHelper.getComposition(cellComponent);
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
                JCheckBox checkboxImpl = ((DesktopCheckBox) columnComponent).getComponent();
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

            return new EditableColumnTableCellEditor(columnComponent);
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
//                        disabled for #PL-2035
//                        disableItemListener = true;
                        if (!selectedItems.isEmpty()) {
                            datasource.setItem(selectedItems.iterator().next());
                        } else {
                            datasource.setItem(null);
                        }
//                        disabled for #PL-2035
//                        disableItemListener = false;
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
    public boolean isAllowPopupMenu() {
        return allowPopupMenu;
    }

    @Override
    public void setAllowPopupMenu(boolean allowPopupMenu) {
        this.allowPopupMenu = allowPopupMenu;
    }

    protected void setEditableColumns(List<MetaPropertyPath> editableColumns) {
        this.editableColumns.clear();
        this.editableColumns.addAll(editableColumns);
    }

    protected void setColumnHeader(Object propertyPath, String caption) {
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
        this.sortable = sortable;
        if (sortable) {
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
    public boolean isAggregatable() {
        return false;
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
    public boolean isAllowMultiStringCells() {
        return false;
    }

    @Override
    public void setAllowMultiStringCells(boolean value) {
    }

    @Override
    public void setRowHeaderMode(RowHeaderMode mode) {
    }

    @Override
    public void setStyleProvider(StyleProvider styleProvider) {
        this.styleProvider = styleProvider;

        for (Table.Column col : columnsOrder) {
            // generated column handles styles himself
            if (!tableModel.isGeneratedColumn(col)) {
                TableColumn tableColumn = getColumn(col);

                // If column is not hidden by security
                if (tableColumn != null) {
                    tableColumn.setCellRenderer(styleProvider != null ? new StylingCellRenderer() : null);
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
    public void addGeneratedColumn(String columnId, ColumnGenerator generator) {
        addGeneratedColumn(columnId, generator, null);
    }

    @Override
    public void addGeneratedColumn(String columnId, ColumnGenerator generator,
                                   Class<? extends com.haulmont.cuba.gui.components.Component> componentClass) {
        checkArgument(columnId != null, "columnId is null");
        checkArgument(generator != null, "generator is null for column id '%s'", columnId);

        Column col = getColumn(columnId);
        Column associatedRuntimeColumn = null;
        if (col == null) {
            col = addRuntimeGeneratedColumn(columnId);
            associatedRuntimeColumn = col;
        }

        col.setEditable(false); // generated column must be non-editable, see TableModelAdapter.setValueAt()
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
    public void setColumnCaption(String columnId, String caption) {
        checkArgument(columnId != null, "columnId is null");

        Column column = getColumn(columnId);
        if (column == null) {
            throw new IllegalStateException(String.format("Column with id '%s' not found", columnId));
        }

        column.setCaption(caption);
        TableColumn tableColumn = getColumn(column);

        // If column is not hidden by security
        if (tableColumn != null) {
            tableColumn.setHeaderValue(caption);
        }
    }

    @Override
    public void setColumnCollapsed(String columnId, boolean collapsed) {
        checkArgument(columnId != null, "columnId is null");

        Column column = getColumn(columnId);
        if (column == null) {
            throw new IllegalStateException(String.format("Column with id '%s' not found", columnId));
        }

        column.setCollapsed(collapsed);

        TableColumn tableColumn = getColumn(column);
        if (tableColumn instanceof TableColumnExt) {
            ((TableColumnExt) tableColumn).setVisible(!collapsed);
        }
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
        checkArgument(column != null, "column is null");

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
        for (Column column : getColumns()) {
            TableCellEditor columnCellEditor = impl.getColumn(column).getCellEditor();
            if (columnCellEditor instanceof DesktopTableCellEditor) {
                ((DesktopTableCellEditor) columnCellEditor).clearCache();
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
        impl.setEnabled(enabled);
        if (buttonsPanel != null) {
            buttonsPanel.setEnabled(enabled);
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
        }
        buttonsPanel = panel;
        if (panel != null) {
            topPanel.add(DesktopComponentsHelper.unwrap(panel), BorderLayout.WEST);
            topPanel.setVisible(true);
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
        tableSettings.apply(element, isSortable());
    }

    @Override
    public boolean saveSettings(Element element) {
        return tableSettings.saveSettings(element);
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
    public <T extends Entity> T getSingleSelected() {
        Set<T> selected = getSelected();
        return selected.isEmpty() ? null : selected.iterator().next();
    }

    @Override
    public <T extends Entity> Set<T> getSelected() {
        Set<T> set = new HashSet<>();
        int[] rows = impl.getSelectedRows();
        for (int row : rows) {
            int modelRow = impl.convertRowIndexToModel(row);
            Object item = tableModel.getItem(modelRow);
            if (item != null) {
                set.add((T) item);
            }
        }
        return set;
    }

    @Override
    public void setSelected(Entity item) {
        if (item != null) {
            setSelected(Collections.singleton(item));
        } else {
            setSelected(Collections.<Entity>emptySet());
        }
    }

    @Override
    public void setSelected(Collection<Entity> items) {
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

    protected List<Integer> getSelectionIndexes(Collection<Entity> items) {
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
            if (StringUtils.isNotBlank(action.getCaption())) {
                menuItem = new JMenuItem(action.getCaption());
                if (action.getIcon() != null) {
                    menuItem.setIcon(App.getInstance().getResources().getIcon(action.getIcon()));
                }
                if (action.getShortcut() != null) {
                    menuItem.setAccelerator(DesktopComponentsHelper.convertKeyCombination(action.getShortcut()));
                }
                menuItem.setEnabled(action.isEnabled());
                menuItem.setVisible(action.isVisible());
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
     * Sets the height of each row into the preferred height of the
     * tallest cell in that row.
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

        StopWatch sw = new Log4JStopWatch("DAT packRows " + id);
        for (int r = 0; r < impl.getRowCount(); r++) {
            int h = getPreferredRowHeight(r);

            if (impl.getRowHeight(r) != h) {
                impl.setRowHeight(r, h);
            }
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
                boolean inline = cellEditor.isInline();
                if (!inline) {
                    return false;
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
        if (styleProvider == null) {
            return null;
        }
        Entity item = tableModel.getItem(row);
        int modelColumn = table.convertColumnIndexToModel(column);
        Object property = columnsOrder.get(modelColumn).getId();

        return styleProvider.getStyleName(item, property.toString());
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
}