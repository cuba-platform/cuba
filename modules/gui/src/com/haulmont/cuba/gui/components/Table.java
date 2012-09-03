/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 13:21:13
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.formatters.DateFormatter;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.dom4j.Element;

import java.util.List;

public interface Table
    extends
        ListComponent, Component.Editable, Component.HasSettings,
        Component.HasButtonsPanel, Component.HasPresentations
{
    String NAME = "table";

    String INSERT_SHORTCUT_ID = "INSERT_SHORTCUT";
    String REMOVE_SHORTCUT_ID = "REMOVE_SHORTCUT";

    List<Column> getColumns();
    Column getColumn(String id);
    void addColumn(Column column);
    void removeColumn(Column column);

    void setDatasource(CollectionDatasource datasource);

    void setRequired(Column column, boolean required, String message);
    void addValidator(Column column, com.haulmont.cuba.gui.components.Field.Validator validator);

    void addValidator(com.haulmont.cuba.gui.components.Field.Validator validator);

    void setItemClickAction(Action action);
    Action getItemClickAction();

    void setEnterPressAction(Action action);
    Action getEnterPressAction();

    List<Column> getNotCollapsedColumns();

    void setSortable(boolean sortable);
    boolean isSortable();

    void setAggregatable(boolean aggregatable);
    boolean isAggregatable();

    void setShowTotalAggregation(boolean showAggregation);
    boolean isShowTotalAggregation();

    void sortBy(Object propertyId, boolean ascending);

    void selectAll();

    RowsCount getRowsCount();
    void setRowsCount(RowsCount rowsCount);

    boolean isAllowMultiStringCells();
    void setAllowMultiStringCells(boolean value);

    interface ColumnCollapseListener {
        void columnCollapsed(Column collapsedColumn, boolean collapsed);
    }

    void addColumnCollapsedListener(ColumnCollapseListener columnCollapsedListener);
    void removeColumnCollapseListener(ColumnCollapseListener columnCollapseListener);

    public static class Column implements HasXmlDescriptor, HasCaption, HasFomatter {

        protected Object id;
        protected String caption;
        protected boolean editable;
        protected Formatter formatter;
        protected Integer width;
        protected boolean collapsed;
        protected AggregationInfo aggregation;
        protected boolean calculatable;

        protected Class type;
        private Element element;

        public Column(Object id) {
            this.id = id;
        }

        public Column(Object id, String caption) {
            this.id = id;
            this.caption = caption;
        }

        public Object getId() {
            return id;
        }

        @Override
        public String getCaption() {
            return caption;
        }

        @Override
        public void setCaption(String caption) {
            this.caption = caption;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public void setDescription(String description) {
        }

        public Boolean isEditable() {
            return editable;
        }

        public void setEditable(Boolean editable) {
            this.editable = editable;
        }

        public Class getType() {
            return type;
        }

        public void setType(Class type) {
            this.type = type;
        }

        @Override
        public Formatter getFormatter() {
            return formatter;
        }

        @Override
        public void setFormatter(Formatter formatter) {
            this.formatter = formatter;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public boolean isCollapsed() {
            return collapsed;
        }

        public void setCollapsed(boolean collapsed) {
            this.collapsed = collapsed;
        }

        public AggregationInfo getAggregation() {
            return aggregation;
        }

        public void setAggregation(AggregationInfo aggregation) {
            this.aggregation = aggregation;
        }

        public boolean isCalculatable() {
            return calculatable;
        }

        public void setCalculatable(boolean calculatable) {
            this.calculatable = calculatable;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Column column = (Column) o;

            return id.equals(column.id);

        }

        @Override
        public int hashCode() {
            return id.hashCode();
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
        public String toString() {
            return id == null ? super.toString() : id.toString();
        }

        public enum FormatterType {
            DATE(DateFormatter.class),
            DATETIME(DateFormatter.class);

            private Class formatterClass;

            private FormatterType(Class formatterClass) {
                this.formatterClass = formatterClass;
            }

            public Class getFormatterClass() {
                return formatterClass;
            }
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    enum RowHeaderMode {
        NONE,
        ICON
    }

    void setRowHeaderMode(RowHeaderMode mode);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    interface StyleProvider {
        String getStyleName(Entity item, Object property);
        String getItemIcon(Entity item);
    }

    void setStyleProvider(StyleProvider styleProvider);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Deprecated
    enum PagingMode {
        PAGE,
        SCROLLING
    }

    @Deprecated
    void setPagingMode(PagingMode mode);

    interface PagingProvider {
        String firstCaption();
        String prevCaption();
        String nextCaption();
        String lastCaption();

        String pageLengthSelectorCaption();
        boolean showPageLengthSelector();
        int[] pageLengths();
    }

    @Deprecated
    void setPagingProvider(PagingProvider pagingProvider);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public interface ColumnGenerator {
        Component generateCell(Table table, Object itemId);
    }

    void addGeneratedColumn(String columnId, ColumnGenerator generator);

    /**
     * Method useful for desktop UI.
     * Table can make addititional look, feel and performance tweaks
     * if it knows class of components that will be generated.
     *
     * @param columnId column identifier
     * @param generator column generator
     * @param componentClass class of components that generator will provide
     */
    void addGeneratedColumn(String columnId, ColumnGenerator generator, Class<? extends Component> componentClass);

    void removeGeneratedColumn(String columnId);

    /**
     * Repaint ui representation of table (columns, generated columns) without refresh table data
     */
    void repaint();
}
