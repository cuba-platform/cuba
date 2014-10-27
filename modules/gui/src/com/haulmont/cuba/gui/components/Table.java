/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author abramov
 * @version $Id$
 */
public interface Table
        extends
            ListComponent, Component.Editable, Component.HasSettings,
            Component.HasButtonsPanel, Component.HasPresentations {

    public enum ColumnAlignment {
        LEFT,
        CENTER,
        RIGHT
    }

    String NAME = "table";

    List<Column> getColumns();

    Column getColumn(String id);

    void addColumn(Column column);

    void removeColumn(Column column);

    void setDatasource(CollectionDatasource datasource);

    void setRequired(Column column, boolean required, String message);

    void addValidator(Column column, com.haulmont.cuba.gui.components.Field.Validator validator);
    void addValidator(com.haulmont.cuba.gui.components.Field.Validator validator);

    /**
     * Assign caption for column in runtime.
     */
    void setColumnCaption(String columnId, String caption);
    void setColumnCaption(Table.Column column, String caption);

    /**
     * Show/hide column in runtime. Hidden column will be available in column control.
     */
    void setColumnCollapsed(String columnId, boolean collapsed);
    void setColumnCollapsed(Table.Column column, boolean collapsed);

    /**
     * Set column width in runtime.
     */
    void setColumnWidth(String columnId, int width);
    void setColumnWidth(Table.Column column, int width);

    void setColumnAlignment(String columnId, ColumnAlignment alignment);
    void setColumnAlignment(Column column, ColumnAlignment alignment);

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

    void setColumnReorderingAllowed(boolean columnReorderingAllowed);
    boolean getColumnReorderingAllowed();

    void setColumnControlVisible(boolean columnCollapsingAllowed);
    boolean getColumnControlVisible();

    void sortBy(Object propertyId, boolean ascending);

    void selectAll();

    RowsCount getRowsCount();
    void setRowsCount(RowsCount rowsCount);

    /**
     * Use {@link #isMultiLineCells()}
     */
    @Deprecated
    boolean isAllowMultiStringCells();
    /**
     * Use {@link #setMultiLineCells(boolean)}
     */
    @Deprecated
    void setAllowMultiStringCells(boolean value);

    boolean isMultiLineCells();
    void setMultiLineCells(boolean multiLineCells);

    /**
     * Use {@link #isContextMenuEnabled()}
     */
    @Deprecated
    boolean isAllowPopupMenu();

    /**
     * Use {@link #setContextMenuEnabled(boolean)}
     */
    @Deprecated
    void setAllowPopupMenu(boolean value);

    boolean isContextMenuEnabled();
    void setContextMenuEnabled(boolean contextMenuEnabled);

    int getRowHeaderWidth();
    void setRowHeaderWidth(int width);

    /**
     * Repaint UI representation of the table (columns, generated columns) without refreshing the table data
     */
    void repaint();

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    interface ColumnCollapseListener {
        void columnCollapsed(Column collapsedColumn, boolean collapsed);
    }

    void addColumnCollapsedListener(ColumnCollapseListener columnCollapsedListener);
    void removeColumnCollapseListener(ColumnCollapseListener columnCollapseListener);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    enum RowHeaderMode {
        NONE,
        ICON
    }

    void setRowHeaderMode(RowHeaderMode mode);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Allows to define different styles for table cells.
     */
    interface StyleProvider<E extends Entity> {
        /**
         * Called by {@link Table} to get a style for row or cell.
         *
         * @param entity   an entity instance represented by the current row
         * @param property column identifier if getting a style for a cell, or null if getting the style for a row
         * @return style name or null to apply the default
         */
        @Nullable
        String getStyleName(E entity, @Nullable String property);
    }

    /**
     * Allows to set icons for particular rows in the table.
     *
     * @param <E> entity class
     */
    interface IconProvider<E extends Entity> {
        /**
         * Called by {@link Table} to get an icon to be shown for a row.
         *
         * @param entity an entity instance represented by the current row
         * @return icon name or null to show no icon
         */
        @Nullable
        String getItemIcon(E entity);
    }

    /**
     * Set the cell style provider for the table.
     */
    void setStyleProvider(StyleProvider styleProvider);

    /**
     * Set the row icon provider for the table.
     */
    void setIconProvider(IconProvider iconProvider);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method returns the datasource which contains the provided item. It can be used in data-aware components,
     * created in generated columns. <br/>
     *
     * <b>Do not save to final variables, just get it from table when you need.</b>
     *
     * <pre>
     * modelsTable.addGeneratedColumn(
     *     "numberOfSeats",
     *     new Table.ColumnGenerator<Model>() {
     *         public Component generateCell(Model entity) {
     *             LookupField lookupField = componentsFactory.createComponent(LookupField.NAME);
     *             lookupField.setDatasource(modelsTable.getItemDatasource(entity), "numberOfSeats");
     *             lookupField.setOptionsList(Arrays.asList(2, 4, 5));
     *             lookupField.setWidth("100px");
     *             return lookupField;
     *         }
     *     }
     * );
     * </pre>
     *
     * @param item entity item
     * @return datasource containing the item
     */
    public Datasource getItemDatasource(Entity item);

    /**
     * Allows rendering of an arbitrary {@link Component} inside a table cell.
     */
    public interface ColumnGenerator<E extends Entity> {
        /**
         * Called by {@link Table} when rendering a column for which the generator was created.
         *
         * @param entity an entity instance represented by the current row
         * @return a component to be rendered inside of the cell
         */
        Component generateCell(E entity);
    }

    /**
     * Allows set Printable representation for column in Excel export. <br/>
     * If for column specified Printable then value for Excel cell gets from Printable representation.
     *
     * @param <E> type of item
     * @param <P> type of printable value, e.g. String/Date/Integer/Double/BigDecimal
     */
    public interface Printable<E extends Entity, P> {
        P getValue(E item);
    }

    /**
     * Column generator, which supports print to Excel.
     *
     * @param <E> entity type
     * @param <P> printable value type
     */
    public interface PrintableColumnGenerator<E extends Entity, P> extends ColumnGenerator<E>, Printable<E, P> {
    }

    /**
     * Add a generated column to the table.
     *
     * @param columnId  column identifier as defined in XML descriptor. May or may not correspond to an entity property.
     * @param generator column generator instance
     */
    void addGeneratedColumn(String columnId, ColumnGenerator generator);

    /**
     * Add a generated column to the table.
     * <p/> This method useful for desktop UI. Table can make addititional look, feel and performance tweaks
     * if it knows the class of components that will be generated.
     *
     * @param columnId       column identifier as defined in XML descriptor. May or may not correspond to an entity property.
     * @param generator      column generator instance
     * @param componentClass class of components that generator will provide
     */
    void addGeneratedColumn(String columnId, ColumnGenerator generator, Class<? extends Component> componentClass);

    void removeGeneratedColumn(String columnId);

    /**
     * Adds {@link Printable} representation for column. <br/>
     * Excplicitly added Printable will be used instead of inherited from generated column.
     *
     * @param columnId  column id
     * @param printable printable representation
     */
    void addPrintable(String columnId, Printable printable);

    /**
     * Removes {@link Printable} representation of column. <br/>
     * Unable to remove Printable representation inherited from generated column.
     *
     * @param columnId column id
     */
    void removePrintable(String columnId);

    /**
     * Get {@link Printable} representation for column.
     *
     * @param column table column
     * @return printable
     */
    @Nullable
    Printable getPrintable(Table.Column column);

    /**
     * Get {@link Printable} representation for column.
     *
     * @param columnId column id
     * @return printable
     */
    @Nullable
    Printable getPrintable(String columnId);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class Column implements HasXmlDescriptor, HasCaption, HasFormatter {

        private static final Log log = LogFactory.getLog(Table.class);

        protected Object id;
        protected String caption;
        protected String description;
        protected boolean editable;
        protected Formatter formatter;
        protected Integer width;
        protected boolean collapsed;
        protected AggregationInfo aggregation;
        protected boolean calculatable;
        protected Integer maxTextLength;
        protected ColumnAlignment alignment;

        protected Class type;
        protected Element element;

        protected Table owner;

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
            if (owner != null) {
                owner.setColumnCaption(this, caption);
            }
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public void setDescription(String description) {
            this.description = description;
        }

        public Boolean isEditable() {
            return editable;
        }

        public void setEditable(Boolean editable) {
            this.editable = editable;
            if (owner != null) {
                log.warn("Changing editable for column in runtime is not supported");
            }
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

        public ColumnAlignment getAlignment() {
            return alignment;
        }

        public void setAlignment(ColumnAlignment alignment) {
            this.alignment = alignment;
            if (alignment != null && owner != null) {
                owner.setColumnAlignment(this, alignment);
            }
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
            if (width != null && owner != null) {
                owner.setColumnWidth(this, width);
            }
        }

        public boolean isCollapsed() {
            return collapsed;
        }

        public void setCollapsed(boolean collapsed) {
            this.collapsed = collapsed;
            if (owner != null) {
                owner.setColumnCollapsed(this, collapsed);
            }
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

        public Integer getMaxTextLength() {
            return maxTextLength;
        }

        public void setMaxTextLength(Integer maxTextLength) {
            this.maxTextLength = maxTextLength;
        }

        public Table getOwner() {
            return owner;
        }

        public void setOwner(Table owner) {
            this.owner = owner;
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
    }
}