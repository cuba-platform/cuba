/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.core.global.MetadataHelper;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.data.AnyTableModelAdapter;
import com.haulmont.cuba.desktop.gui.data.RowSorterImpl;
import com.haulmont.cuba.desktop.theme.DesktopTheme;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsActionsNotifier;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.swing.AbstractAction;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.Component;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class DesktopAbstractTable<C extends JTable>
        extends DesktopAbstractActionOwnerComponent<C>
        implements Table
{
    private static final int HEIGHT_MARGIN_FOR_ROWS = 2;
    private static final int WIDTH_MARGIN_FOR_CELL = 2;

    protected MigLayout layout;
    protected JPanel panel;
    protected JPanel topPanel;
    protected AnyTableModelAdapter tableModel;
    protected CollectionDatasource datasource;
    protected ButtonsPanel buttonsPanel;
    protected RowsCount rowsCount;
    protected Map<Object, Column> columns = new HashMap<Object, Column>();
    protected List<Table.Column> columnsOrder = new ArrayList<Table.Column>();
    protected boolean sortable = true;
    protected TableSettings tableSettings;
    private boolean editable;
    private StyleProvider styleProvider;

    private Action itemClickAction;
    private Action enterPressAction;

    private boolean columnsInited = false;

    protected void initComponent() {
        layout = new MigLayout("flowy, fill, insets 0", "", "[min!][fill]");
        panel = new JPanel(layout);

        topPanel = new JPanel(new BorderLayout());
        topPanel.setVisible(false);
        panel.add(topPanel, "growx");

        JScrollPane scrollPane = new JScrollPane(impl);
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

                    private void showPopup(MouseEvent e) {
                        if (e.isPopupTrigger()) {
                            // select row
                            Point p = e.getPoint();
                            int rowNumber = impl.convertRowIndexToModel(impl.rowAtPoint(p));
                            ListSelectionModel model = impl.getSelectionModel();
                            model.setSelectionInterval(rowNumber, rowNumber);
                            // show popup menu
                            createPopupMenu().show(e.getComponent(), e.getX(), e.getY());
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

        scrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (!columnsInited)
                    adjustColumnHeaders();
                columnsInited = true;
            }
        });
    }

    protected void handleClickAction() {
        Action action = getItemClickAction();
        if (action == null) {
            action = getAction(EditAction.ACTION_ID);
        }
        if (action != null && action.isEnabled()) {
            Window window = ComponentsHelper.getWindow(DesktopAbstractTable.this);
            if (!(window instanceof Window.Lookup))
                action.actionPerform(DesktopAbstractTable.this);
            else if (action.getId().equals(Window.Lookup.LOOKUP_ITEM_CLICK_ACTION_ID)) {
                action.actionPerform(DesktopAbstractTable.this);
            }
        }
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
        List<TableColumn> notInited = new LinkedList<TableColumn>();
        int summaryWidth = 0;
        int componentWidth = impl.getParent().getWidth();

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
            } else
                notInited.add(tableColumn);
        }

        if (notInited.size() != impl.getColumnCount()) {
            impl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            if (!notInited.isEmpty() && (componentWidth > summaryWidth)) {
                int defaultWidth = (componentWidth - summaryWidth) / notInited.size();
                for (TableColumn column : notInited)
                    column.setPreferredWidth( Math.max(defaultWidth, column.getWidth()) );
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
            if (column.getId().toString().equals(id))
                return column;
        }
        return null;
    }

    @Override
    public void addColumn(Column column) {
        columns.put(column.getId(), column);
        columnsOrder.add(column);

        if (tableModel != null)
            tableModel.addColumn(column);

        setColumnIdentifiers();
        refresh();
    }

    @Override
    public void removeColumn(Column column) {
        String name;
        if (column.getId() instanceof MetaPropertyPath) {
            MetaPropertyPath metaPropertyPath = (MetaPropertyPath) column.getId();
            name = metaPropertyPath.getMetaProperty().getName();
        } else {
            name = column.getId().toString();
        }

        TableColumn tableColumn = null;
        Enumeration<TableColumn> columnEnumeration = impl.getColumnModel().getColumns();
        while (columnEnumeration.hasMoreElements() && (tableColumn == null)) {
            TableColumn xColumn = columnEnumeration.nextElement();
            Object identifier = xColumn.getIdentifier();
            if (identifier instanceof String) {
                if (identifier.equals(name))
                    tableColumn = xColumn;
            } else
            if (column.equals(identifier))
                tableColumn = xColumn;
        }

        if (tableColumn != null) {
            impl.getColumnModel().removeColumn(tableColumn);
            impl.removeColumn(tableColumn);

            columns.remove(column.getId());
            columnsOrder.remove(column);

            if (tableModel != null)
                tableModel.removeColumn(column);

            setColumnIdentifiers();
        }
    }

    @Override
    public void setDatasource(final CollectionDatasource datasource) {
        UserSession userSession = UserSessionProvider.getUserSession();
        if (!userSession.isEntityOpPermitted(datasource.getMetaClass(), EntityOp.READ)) {
            impl.setVisible(false);
            return;
        }

        final Collection<Object> properties;
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
        properties = this.columns.keySet();

        this.datasource = datasource;

        initTableModel(datasource);

        initChangeListener();

        setColumnIdentifiers();

        impl.setRowSorter(new RowSorterImpl(tableModel));

        initSelectionListener(datasource);

        List<MetaPropertyPath> editableColumns = null;
        if (isEditable()) {
            editableColumns = new LinkedList<MetaPropertyPath>();
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
        }

        setVisibleColumns(columnsOrder);

        if (UserSessionProvider.getUserSession().isSpecificPermitted(ShowInfoAction.ACTION_PERMISSION)) {
            ShowInfoAction action = (ShowInfoAction) getAction(ShowInfoAction.ACTION_ID);
            if (action == null) {
                action = new ShowInfoAction();
                addAction(action);
            }
            action.setDatasource(datasource);
        }

        datasource.addListener(
                new CollectionDsListenerAdapter() {
                    @Override
                    public void collectionChanged(CollectionDatasource ds, Operation operation) {
                        onDataChange();
                        packRows();
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
                                if (propertyName.equals(property))
                                    find = true;
                            }
                            i++;
                        }
                        if (find)
                            onDataChange();
                        packRows();
                    }
                }
        );

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

    private void setColumnIdentifiers() {
        Enumeration<TableColumn> columnEnumeration = impl.getColumnModel().getColumns();
        int i = 0;
        while (columnEnumeration.hasMoreElements()) {
            TableColumn tableColumn = columnEnumeration.nextElement();
            Column column = columnsOrder.get(i++);
            tableColumn.setIdentifier(column);
        }
    }

    protected void onDataChange() {
        Enumeration<TableColumn> columnEnumeration = impl.getColumnModel().getColumns();
        while (columnEnumeration.hasMoreElements()) {
            TableColumn tableColumn = columnEnumeration.nextElement();
            TableCellEditor cellEditor = tableColumn.getCellEditor();
            if (cellEditor instanceof DesktopAbstractTable.CellEditor) {
                ((CellEditor) cellEditor).clearCache();
            }
        }
        impl.repaint();
    }

    protected void initChangeListener() {
        tableModel.addChangeListener(new AnyTableModelAdapter.DataChangeListener() {
            private Set selection;

            @Override
            public void beforeChange() {
                selection = getSelected();
            }

            @Override
            public void afterChange() {
                Set<Entity> newSelection = null;
                if (selection != null) {
                    newSelection = new HashSet<Entity>();
                    // filter selection
                    for (Object item : selection)
                        if (tableModel.getRowIndex((Entity) item) >= 0)
                            newSelection.add((Entity) item);
                }
                // apply selection
                setSelected(newSelection);
                selection = null;
            }
        });
    }

    protected void initSelectionListener(final CollectionDatasource datasource) {
        impl.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {

                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (e.getValueIsAdjusting())
                            return;

                        Entity entity = getSingleSelected();
                        datasource.setItem(entity);
                    }
                }
        );
    }

    protected void setVisibleColumns(List<Object> columnsOrder) {
    }

    protected void setEditableColumns(List<MetaPropertyPath> editableColumns) {
    }

    protected void setColumnHeader(Object propertyPath, String caption) {
    }

    @Override
    public void setRequired(Column column, boolean required, String message) {
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
        List<Column> visibleColumns = new LinkedList<Column>();
        for (Column column : columnsOrder) {
            if (!column.isCollapsed())
                visibleColumns.add(column);
        }
        return visibleColumns;
    }

    @Override
    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    @Override
    public boolean isSortable() {
        return sortable;
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
        final Enumeration<TableColumn> columnEnumeration = impl.getColumnModel().getColumns();
        TableCellRenderer newRenderer = styleProvider != null ? new CustomCellRenderer() : null;
        while (columnEnumeration.hasMoreElements()) {
            TableColumn column = columnEnumeration.nextElement();
            column.setCellRenderer(newRenderer);
        }
    }

    private class CustomCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            TableCellRenderer renderer = table.getDefaultRenderer(value != null ? value.getClass() : Object.class);
            java.awt.Component component = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            Entity item = tableModel.getItem(row);
            int modelColumn = table.convertColumnIndexToModel(column);
            Object property = columnsOrder.get(modelColumn).getId();

            String style = styleProvider.getStyleName(item, property);
            if (style != null) {
                DesktopTheme theme = App.getInstance().getTheme();
                if (theme != null) {
                    HashSet<String> properties = new HashSet<String>();

                    if (hasFocus) {
                        properties.add("focused");
                    }
                    else if (isSelected) {
                        properties.add("selected");
                    }
                    else {
                        properties.add("unselected");
                    }
                    theme.applyStyle(component, style, properties);
                }
            }
            return component;
        }
    }

    @Override
    public void setPagingMode(PagingMode mode) {
    }

    @Override
    public void setPagingProvider(PagingProvider pagingProvider) {
    }

    @Override
    public void addGeneratedColumn(String columnId, ColumnGenerator generator) {
        if (columnId == null)
            throw new IllegalArgumentException("columnId is null");
        if (generator == null)
            throw new IllegalArgumentException("generator is null");

        Column col = getColumn(columnId);
        tableModel.addGeneratedColumn(col);
        col.setEditable(false); // generated column must be non-editable, see TableModelAdapter.setValueAt()
        TableColumnModel columnModel = impl.getColumnModel();
        TableColumn tableColumn = columnModel.getColumn(columnModel.getColumnIndex(col));
        CellEditor cellEditor = new CellEditor(generator);
        tableColumn.setCellEditor(cellEditor);
        tableColumn.setCellRenderer(cellEditor);

        packRows();
    }

    @Override
    public void removeGeneratedColumn(String columnId){
        if (id == null)
            throw new IllegalArgumentException("columnId is null");

        Column col = getColumn(columnId);
        tableModel.removeGeneratedColumn(col);
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
        if (multiselect)
            impl.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        else
            impl.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public <T extends Entity> T getSingleSelected() {
        Set selected = getSelected();
        return selected.isEmpty() ? null : (T) selected.iterator().next();
    }

    @Override
    public Set getSelected() {
        Set set = new HashSet();
        int[] rows = impl.getSelectedRows();
        for (int row : rows) {
            int modelRow = impl.convertRowIndexToModel(row);
            Object item = tableModel.getItem(modelRow);
            if (item != null)
                set.add(item);
        }
        return set;
    }

    @Override
    public void setSelected(Entity item) {
        if (item != null) {
            int rowIndex = impl.convertRowIndexToView(tableModel.getRowIndex(item));
            impl.getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
        } else {
            int[] rows = impl.getSelectedRows();
            for (int row : rows) {
                int rowIndex = impl.convertRowIndexToView(row);
                impl.getSelectionModel().removeSelectionInterval(rowIndex, rowIndex);
            }
        }
    }

    @Override
    public void setSelected(Collection<Entity> items) {
        for (Entity item : items) {
            int rowIndex = impl.convertRowIndexToView(tableModel.getRowIndex(item));
            impl.getSelectionModel().addSelectionInterval(rowIndex, rowIndex);
        }
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
            impl.repaint();
        }
    }

    protected JPopupMenu createPopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem;
        for (final Action action : actionsOrder) {
            menuItem = new JMenuItem(action.getCaption());
            if (action.getIcon() != null) {
                menuItem.setIcon(App.getInstance().getResources().getIcon(action.getIcon()));
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
        return popup;
    }

    /**
     * Returns the preferred height of a row.
     * The result is equal to the tallest cell in the row.
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

    /**
     * Sets the height of each row into the preferred height of the
     * tallest cell in that row.
     */
    public void packRows() {
        for (int r = 0; r < impl.getRowCount(); r++) {
            int h = getPreferredRowHeight(r);

            if (impl.getRowHeight(r) != h) {
                impl.setRowHeight(r, h);
            }
        }
    }

    protected class ComponentWrapper extends JPanel {
        protected ComponentWrapper(Component component) {
            setOpaque(true);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(HEIGHT_MARGIN_FOR_ROWS, WIDTH_MARGIN_FOR_CELL,
                    HEIGHT_MARGIN_FOR_ROWS, WIDTH_MARGIN_FOR_CELL));
            add(component, BorderLayout.CENTER);
        }
    }

    protected class CellEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

        private static final long serialVersionUID = 5217563286634642347L;

        private ColumnGenerator columnGenerator;
        private Component activeComponent;
        private Map<Integer, Component> cache = new HashMap<Integer, Component>();

        public CellEditor(ColumnGenerator columnGenerator) {
            this.columnGenerator = columnGenerator;
        }

        protected Component getCellComponent(int row) {
            Entity item = tableModel.getItem(row);
            com.haulmont.cuba.gui.components.Component component = columnGenerator.generateCell(DesktopAbstractTable.this, item.getId());
            Component comp;
            if (component == null)
                comp = new ComponentWrapper(new JLabel(""));
            else
                comp = new ComponentWrapper(DesktopComponentsHelper.getComposition(component));

            cache.put(row, comp);
            return comp;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return getCellComponent(row);
        }

        @Override
        public Object getCellEditorValue() {
            if (activeComponent != null) {
                // normally handle focus lost
                activeComponent.dispatchEvent(new FocusEvent(activeComponent, FocusEvent.FOCUS_LOST));
            }
            return "";
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            activeComponent = cache.get(row);
            if (activeComponent == null) {
                activeComponent = getCellComponent(row);
                cache.put(row, activeComponent);
            }
            return activeComponent;
        }

        public void clearCache() {
            cache.clear();
        }
    }

    @Override
    public void addColumnCollapsedListener(ColumnCollapseListener columnCollapsedListener) {
    }

    @Override
    public void removeColumnCollapseListener(ColumnCollapseListener columnCollapseListener) {
    }
}
