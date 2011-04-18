/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.desktop.sys.layout.LayoutAdapter;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.presentations.Presentations;
import net.miginfocom.swing.MigLayout;
import org.dom4j.Element;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopAbstractTable
        extends DesktopAbstractActionOwnerComponent<JTable>
        implements Table
{
    protected MigLayout layout;
    protected JPanel panel;
    protected CollectionDatasource datasource;

    public DesktopAbstractTable() {
        layout = new MigLayout("flowy, fill, insets 0");
        panel = new JPanel(layout);
        jComponent = new JTable();
        initComponent((JTable) jComponent);
        panel.add(jComponent, "grow");
    }

    protected void initComponent(JTable table) {
        table.setModel(
                new DefaultTableModel(new String[]{"col1", "col2"}, 3)
        );
    }

    @Override
    public JComponent getComposition() {
        return panel;
    }

    public List<Column> getColumns() {
        return null;
    }

    public Column getColumn(String id) {
        return null;
    }

    public void addColumn(Column column) {
    }

    public void removeColumn(Column column) {
    }

    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
    }

    public void setRequired(Column column, boolean required, String message) {
    }

    public void addValidator(Column column, Field.Validator validator) {
    }

    public void addValidator(Field.Validator validator) {
    }

    public void setItemClickAction(com.haulmont.cuba.gui.components.Action action) {
    }

    public Action getItemClickAction() {
        return null;
    }

    public List<Column> getNotCollapsedColumns() {
        return null;
    }

    public void setSortable(boolean sortable) {
    }

    public boolean isSortable() {
        return false;
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
        return null;
    }

    public void setRowsCount(RowsCount rowsCount) {
    }

    public boolean isAllowMultiStringCells() {
        return false;
    }

    public void setAllowMultiStringCells(boolean value) {
    }

    public void setRowHeaderMode(RowHeaderMode mode) {
    }

    public void setStyleProvider(StyleProvider styleProvider) {
    }

    public void setPagingMode(PagingMode mode) {
    }

    public void setPagingProvider(PagingProvider pagingProvider) {
    }

    public void addGeneratedColumn(String columnId, ColumnGenerator generator) {
    }

    public boolean isEditable() {
        return false;
    }

    public void setEditable(boolean editable) {
    }

    public ButtonsPanel getButtonsPanel() {
        return null;
    }

    public void setButtonsPanel(ButtonsPanel panel) {
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
    }

    public boolean saveSettings(Element element) {
        return false;
    }

    public boolean isMultiSelect() {
        return false;
    }

    public void setMultiSelect(boolean multiselect) {
    }

    public <T extends Entity> T getSingleSelected() {
        return null;
    }

    public Set getSelected() {
        return null;
    }

    public void setSelected(Entity item) {
    }

    public void setSelected(Collection<Entity> items) {
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    public void refresh() {
    }
}
