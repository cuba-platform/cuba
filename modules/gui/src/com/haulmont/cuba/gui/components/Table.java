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

import java.io.Serializable;
import java.util.List;

public interface Table
    extends
        com.haulmont.cuba.gui.components.List, Component.Editable, Component.HasSettings, Component.Expandable,
        Component.HasButtonsPanel, Component.HasPresentations
{
    String NAME = "table";

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

    List<Column> getNotCollapsedColumns();

    void setSortable(boolean sortable);
    boolean isSortable();

    void setAggregatable(boolean aggregatable);
    boolean isAggregatable();

    void setShowTotalAggregation(boolean showAggregation);
    boolean isShowTotalAggregation();

    void sortBy(Object propertyId, boolean ascending);

    RowsCount getRowsCount();
    void setRowsCount(RowsCount rowsCount);

    boolean isAllowMultiStringCells();
    void setAllowMultiStringCells(boolean value);

    public class Column implements HasXmlDescriptor, HasCaption, HasFomatter, Serializable {

        private static final long serialVersionUID = -8462478820056909896L;

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

        public Object getId() {
            return id;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getDescription() {
            return null;
        }

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

        public Formatter getFormatter() {
            return formatter;
        }

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

        public Element getXmlDescriptor() {
            return element;
        }

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

    enum PagingMode {
        PAGE,
        SCROLLING
    }

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

    void setPagingProvider(PagingProvider pagingProvider);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public interface ColumnGenerator {
        Component generateCell(Table table, Object itemId);
    }

    void addGeneratedColumn(String columnId, ColumnGenerator generator);
}
