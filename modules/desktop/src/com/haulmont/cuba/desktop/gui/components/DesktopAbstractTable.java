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
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.swing.*;
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
    protected MigLayout layout;
    protected JPanel panel;
    protected JPanel topPanel;
    protected AnyTableModelAdapter tableModel;
    protected CollectionDatasource datasource;
    protected ButtonsPanel buttonsPanel;
    protected RowsCount rowsCount;
    protected Map<MetaPropertyPath, Column> columns = new HashMap<MetaPropertyPath, Column>();
    protected List<Table.Column> columnsOrder = new ArrayList<Table.Column>();
    protected boolean sortable = true;
    protected TableSettings tableSettings;
    private boolean editable;
    private StyleProvider styleProvider;

    private Action itemClickAction;

    private boolean columnsSizeInited = false;

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
                            Action action = getItemClickAction();
                            if (action == null) {
                                action = getAction(EditAction.ACTION_ID);
                            }
                            if (action != null && action.isEnabled()) {
                                Window window = ComponentsHelper.getWindow(DesktopAbstractTable.this);
                                if (!(window instanceof Window.Lookup))
                                    action.actionPerform(DesktopAbstractTable.this);
                            }
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

        // Listener for adjust column widths
        impl.getTableHeader().addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (!columnsSizeInited) {
                    adjustColumnHeaders();
                    columnsSizeInited = true;
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
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

    public List<Column> getColumns() {
        return columnsOrder;
    }

    public Column getColumn(String id) {
        for (Table.Column column : columnsOrder) {
            if (column.getId().toString().equals(id))
                return column;
        }
        return null;
    }

    public void addColumn(Column column) {
        columns.put((MetaPropertyPath) column.getId(), column);
        columnsOrder.add(column);
    }

    public void removeColumn(Column column) {
        columns.remove((MetaPropertyPath) column.getId());
        columnsOrder.remove(column);
    }

    public void setDatasource(final CollectionDatasource datasource) {
        UserSession userSession = UserSessionProvider.getUserSession();
        if (!userSession.isEntityOpPermitted(datasource.getMetaClass(), EntityOp.READ)) {
            impl.setVisible(false);
            return;
        }

        final Collection<MetaPropertyPath> properties;
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

        Enumeration<TableColumn> columnEnumeration = impl.getColumnModel().getColumns();
        int i = 0;
        while (columnEnumeration.hasMoreElements()) {
            TableColumn tableColumn = columnEnumeration.nextElement();
            Column column = columnsOrder.get(i++);
            tableColumn.setIdentifier(column);
        }

        impl.setRowSorter(new RowSorterImpl(tableModel));

        initSelectionListener(datasource);

        List<MetaPropertyPath> editableColumns = null;
        if (isEditable()) {
            editableColumns = new LinkedList<MetaPropertyPath>();
        }

        for (final MetaPropertyPath propertyPath : properties) {
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
                    MetaProperty colMetaProperty = propertyPath.getMetaProperty();
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

        List<MetaPropertyPath> columnsOrder = new ArrayList<MetaPropertyPath>();
        for (Table.Column column : this.columnsOrder) {
            MetaProperty colMetaProperty = ((MetaPropertyPath) column.getId()).getMetaProperty();
            MetaClass colMetaClass = colMetaProperty.getDomain();
            if (userSession.isEntityOpPermitted(colMetaClass, EntityOp.READ)
                    && userSession.isEntityAttrPermitted(
                    colMetaClass, colMetaProperty.getName(), EntityAttrAccess.VIEW)) {
                columnsOrder.add((MetaPropertyPath) column.getId());
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
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        onDataChange();
                    }
                }
        );

        if (rowsCount != null)
            rowsCount.setDatasource(datasource);
    }

    protected void onDataChange() {
        packRows(2);

        Enumeration<TableColumn> columnEnumeration = impl.getColumnModel().getColumns();
        while (columnEnumeration.hasMoreElements()) {
            TableColumn tableColumn = columnEnumeration.nextElement();
            TableCellEditor cellEditor = tableColumn.getCellEditor();
            if (cellEditor instanceof DesktopAbstractTable.CellEditor) {
                ((CellEditor) cellEditor).clearCache();
            }
        }
    }

    protected void initSelectionListener(final CollectionDatasource datasource) {
        impl.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        if (e.getValueIsAdjusting())
                            return;

                        Entity entity = getSingleSelected();
                        datasource.setItem(entity);
                    }
                }
        );
    }

    protected void setVisibleColumns(List<MetaPropertyPath> columnsOrder) {
    }

    protected void setEditableColumns(List<MetaPropertyPath> editableColumns) {
    }

    protected void setColumnHeader(MetaPropertyPath propertyPath, String caption) {
    }

    public void setRequired(Column column, boolean required, String message) {
    }

    public void addValidator(Column column, Field.Validator validator) {
    }

    public void addValidator(Field.Validator validator) {
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

    public List<Column> getNotCollapsedColumns() {
        return null;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setAggregatable(boolean aggregatable) {
    }

    public boolean isAggregatable() {
        return false;
    }

    public void setShowTotalAggregation(boolean showAggregation) {
    }

    public boolean isShowTotalAggregation() {
        return false;
    }

    public void sortBy(Object propertyId, boolean ascending) {
    }

    public RowsCount getRowsCount() {
        return rowsCount;
    }

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

    public boolean isAllowMultiStringCells() {
        return false;
    }

    public void setAllowMultiStringCells(boolean value) {
    }

    public void setRowHeaderMode(RowHeaderMode mode) {
    }

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

    public void setPagingMode(PagingMode mode) {
    }

    public void setPagingProvider(PagingProvider pagingProvider) {
    }

    public void addGeneratedColumn(String columnId, ColumnGenerator generator) {
        if (columnId == null)
            throw new IllegalArgumentException("columnId is null");
        if (generator == null)
            throw new IllegalArgumentException("generator is null");

        Column col = getColumn(columnId);
        tableModel.addGeneratedColumn(col);
        TableColumnModel columnModel = impl.getColumnModel();
        TableColumn tableColumn = columnModel.getColumn(columnModel.getColumnIndex(col));
        CellEditor cellEditor = new CellEditor(generator);
        tableColumn.setCellEditor(cellEditor);
        tableColumn.setCellRenderer(cellEditor);
    }

    public void removeGeneratedColumn(String columnId){
        if (id == null)
            throw new IllegalArgumentException("columnId is null");

        Column col = getColumn(columnId);
        tableModel.removeGeneratedColumn(col);
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public ButtonsPanel getButtonsPanel() {
        return buttonsPanel;
    }

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

    public void usePresentations(boolean b) {
    }

    public boolean isUsePresentations() {
        return false;
    }

    public void loadPresentations() {
    }

    public Presentations getPresentations() {
        return null;
    }

    public void applyPresentation(Object id) {
    }

    public void applyPresentationAsDefault(Object id) {
    }

    public Object getDefaultPresentationId() {
        return null;
    }

    public void applySettings(Element element) {
        tableSettings.apply(element, isSortable());
    }

    public boolean saveSettings(Element element) {
        return tableSettings.saveSettings(element);
    }

    public boolean isMultiSelect() {
        return impl.getSelectionModel().getSelectionMode() != ListSelectionModel.SINGLE_SELECTION;
    }

    public void setMultiSelect(boolean multiselect) {
        if (multiselect)
            impl.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        else
            impl.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public <T extends Entity> T getSingleSelected() {
        Set selected = getSelected();
        return selected.isEmpty() ? null : (T) selected.iterator().next();
    }

    public Set getSelected() {
        Set set = new HashSet();
        int[] rows = impl.getSelectedRows();
        for (int row : rows) {
            int modelRow = impl.convertRowIndexToModel(row);
            Object item = tableModel.getItem(modelRow);
            set.add(item);
        }
        return set;
    }

    public void setSelected(Entity item) {
        int rowIndex = impl.convertRowIndexToView(tableModel.getRowIndex(item));
        impl.getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
    }

    public void setSelected(Collection<Entity> items) {
        for (Entity item : items) {
            int rowIndex = impl.convertRowIndexToView(tableModel.getRowIndex(item));
            impl.getSelectionModel().addSelectionInterval(rowIndex, rowIndex);
        }
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    public void refresh() {
        datasource.refresh();
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
     * @param margin margin to add to the renderer height
     * @return row height
     */
    public int getPreferredRowHeight(int rowIndex, int margin) {
        // Get the current default height for all rows
        int height = impl.getRowHeight();

        // Determine highest cell in the row
        for (int c = 0; c < impl.getColumnCount(); c++) {
            TableCellRenderer renderer = impl.getCellRenderer(rowIndex, c);
            Component comp = impl.prepareRenderer(renderer, rowIndex, c);
            int h = comp.getPreferredSize().height + 2 * margin;
            height = Math.max(height, h);
        }
        return height;
    }

    /**
     * Sets the height of each row into the preferred height of the
     * tallest cell in that row.
     * @param margin margin to add to the renderer height
     */
    public void packRows(int margin) {
        for (int r = 0; r < impl.getRowCount(); r++) {
            int h = getPreferredRowHeight(r, margin);

            if (impl.getRowHeight(r) != h) {
                impl.setRowHeight(r, h);
            }
        }
    }

    private class CellEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

        private ColumnGenerator columnGenerator;
        private Map<Integer, Component> cache = new HashMap<Integer, Component>();

        public CellEditor(ColumnGenerator columnGenerator) {
            this.columnGenerator = columnGenerator;
        }

        private Component getCellComponent(int row) {
            Entity item = tableModel.getItem(row);
            com.haulmont.cuba.gui.components.Component component = columnGenerator.generateCell(DesktopAbstractTable.this, item.getId());
            Component comp;
            if (component == null)
                comp = new JLabel("");
            else {
                comp = DesktopComponentsHelper.getComposition(component);
            }
            cache.put(row, comp);
            return comp;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return getCellComponent(row);
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = cache.get(row);
            if (component == null) {
                component = getCellComponent(row);
                cache.put(row, component);
            }
            return component;
        }

        public void clearCache() {
            cache.clear();
        }
    }
}
