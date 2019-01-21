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
package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Table<E extends Entity>
        extends
            ListComponent<E>, Component.Editable, Component.HasSettings,
            Component.HasButtonsPanel, Component.HasPresentations, Component.HasCaption,
            Component.HasIcon, Component.HasRowsCount, LookupComponent, Component.Focusable {

    enum ColumnAlignment {
        LEFT,
        CENTER,
        RIGHT
    }

    String NAME = "table";

    List<Column> getColumns();

    Column getColumn(String id);

    void addColumn(Column column);

    void removeColumn(Column column);

    Map<Object, Object> getAggregationResults();

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
     * Enable or disable text selection in Table cells.
     * Set true to enable.
     */
    void setTextSelectionEnabled(boolean value);
    /**
     * @return true if text selection is enabled.
     */
    boolean isTextSelectionEnabled();

    /**
     * Assign description for column in runtime.
     */
    void setColumnDescription(String columnId, String description);
    void setColumnDescription(Table.Column column, String description);

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

    void addAggregationProperty(String columnId, AggregationInfo.Type type);
    void addAggregationProperty(Column columnId, AggregationInfo.Type type);
    void removeAggregationProperty(String columnId);

    /**
     * Assign action to be executed on double click inside a table row.
     * <p>If such action is not set, the table responds to double click by trying to find and execute the following
     * actions:
     * <ul>
     *     <li>action, assigned to Enter key press by setting its {@code shortcut} property</li>
     *     <li>action named "edit"</li>
     *     <li>action named "view"</li>
     * </ul>
     * If one of these actions is found and it is enabled, it is executed.
     */
    void setItemClickAction(Action action);
    Action getItemClickAction();

    /**
     * Assign action to be executed on Enter key press.
     * <p>If such action is not set, the table responds to pressing Enter by trying to find and execute the following
     * actions:
     * <ul>
     *     <li>action, assigned by {@link #setItemClickAction(Action)}</li>
     *     <li>action, assigned to Enter key press by setting its {@code shortcut} property</li>
     *     <li>action named "edit"</li>
     *     <li>action named "view"</li>
     * </ul>
     * If one of these actions is found and it is enabled, it is executed.
     */
    void setEnterPressAction(Action action);
    Action getEnterPressAction();

    List<Column> getNotCollapsedColumns();

    void setSortable(boolean sortable);
    boolean isSortable();

    /**
     * Enables or disables automatic scroll to a selected row after table update.
     */
    void setAutoScrolling(boolean autoScroll);

    /**
     * @return whether automatic scroll to a selected row is enabled for this table.
     */
    boolean isAutoScrolling();

    void setAggregatable(boolean aggregatable);
    boolean isAggregatable();

    void setShowTotalAggregation(boolean showAggregation);
    boolean isShowTotalAggregation();

    void setColumnReorderingAllowed(boolean columnReorderingAllowed);
    boolean getColumnReorderingAllowed();

    void setColumnControlVisible(boolean columnCollapsingAllowed);
    boolean getColumnControlVisible();

    void setColumnSortable(String columnId, boolean sortable);
    boolean getColumnSortable(String columnId);

    void setColumnSortable(Column column, boolean sortable);
    boolean getColumnSortable(Column column);

    /**
     * Set focus on inner field of editable/generated column.
     *
     * @param entity   entity
     * @param columnId column id
     */
    void requestFocus(E entity, String columnId);

    /**
     * Scroll table to specified row.
     *
     * @param entity   entity
     */
    void scrollTo(E entity);

    /**
     * Sort the table by a column.
     * For example:
     * <pre>table.sortBy(table.getDatasource().getMetaClass().getPropertyPath("name"), ascending);</pre>
     *
     * @param propertyId    column indicated by a corresponding {@code MetaPropertyPath} object
     * @param ascending     sort direction
     * @deprecated Use {@link Table#sort(String, SortDirection)} method
     */
    @Deprecated
    void sortBy(Object propertyId, boolean ascending);

    /**
     * Sorts the Table data for passed column id in the chosen sort direction.
     *
     * @param columnId  id of the column to sort
     * @param direction sort direction
     */
    void sort(String columnId, SortDirection direction);

    /**
     * @return current sort information or null if no column is sorted
     */
    @Nullable
    SortInfo getSortInfo();

    void selectAll();

    boolean isMultiLineCells();
    void setMultiLineCells(boolean multiLineCells);

    boolean isContextMenuEnabled();
    void setContextMenuEnabled(boolean contextMenuEnabled);

    /**
     * Set width of row header column. Row header shows icons if {@link IconProvider} is specified.
     *
     * @param width width of row header column in px
     */
    void setRowHeaderWidth(int width);
    int getRowHeaderWidth();

    void setMultiSelect(boolean multiselect);

    /**
     * @deprecated refresh datasource instead
     */
    @Deprecated
    void refresh();

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

    enum AggregationStyle {
        TOP,
        BOTTOM
    }

    void setAggregationStyle(AggregationStyle aggregationStyle);
    AggregationStyle getAggregationStyle();

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Allows to define different styles for table cells.
     */
    interface StyleProvider<E extends Entity> {
        /**
         * Called by {@link Table} to get a style for row or cell.<br>
         * All unhandled exceptions from StyleProvider in Web components by default are logged with ERROR level
         * and not shown to users.
         *
         * @param entity   an entity instance represented by the current row
         * @param property column identifier if getting a style for a cell, or null if getting the style for a row
         * @return style name or null to apply the default
         */
        String getStyleName(E entity, @Nullable String property);
    }

    /**
     * Set the cell style provider for the table.<br>
     * All style providers added before this call will be removed.
     */
    void setStyleProvider(@Nullable StyleProvider<? super E> styleProvider);

    /**
     * Add style provider for the table.<br>
     * Table can use several providers to obtain many style names for cells and rows.
     */
    void addStyleProvider(StyleProvider<? super E> styleProvider);
    /**
     * Remove style provider for the table.
     */
    void removeStyleProvider(StyleProvider<? super E> styleProvider);

    /**
     * Set the row icon provider for the table.
     *
     * @see #setRowHeaderWidth(int)
     */
    void setIconProvider(IconProvider<? super E> iconProvider);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method returns the datasource which contains the provided item. It can be used in data-aware components,
     * created in generated columns. <br>
     *
     * <b>Do not save to final variables, just get it from table when you need.</b>
     *
     * <pre>{@code
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
     * }</pre>
     *
     * @param item entity item
     * @return datasource containing the item
     */
    Datasource getItemDatasource(Entity item);

    /**
     * Allows rendering of an arbitrary {@link Component} inside a table cell.
     */
    interface ColumnGenerator<E extends Entity> {
        /**
         * Called by {@link Table} when rendering a column for which the generator was created.
         *
         * @param entity an entity instance represented by the current row
         * @return a component to be rendered inside of the cell
         */
        Component generateCell(E entity);
    }

    /**
     * Allows set Printable representation for column in Excel export. <br>
     * If for column specified Printable then value for Excel cell gets from Printable representation.
     *
     * @param <E> type of item
     * @param <P> type of printable value, e.g. String/Date/Integer/Double/BigDecimal
     */
    interface Printable<E extends Entity, P> {
        P getValue(E item);
    }

    /**
     * Column generator, which supports print to Excel.
     *
     * @param <E> entity type
     * @param <P> printable value type
     */
    interface PrintableColumnGenerator<E extends Entity, P> extends ColumnGenerator<E>, Printable<E, P> {
    }

    /**
     * Add a generated column to the table.
     *
     * @param columnId  column identifier as defined in XML descriptor. May or may not correspond to an entity property.
     * @param generator column generator instance
     */
    void addGeneratedColumn(String columnId, ColumnGenerator<? super E> generator);

    /**
     * Add a generated column to the table.
     * <br> This method useful for desktop UI. Table can make additional look, feel and performance tweaks
     * if it knows the class of components that will be generated.
     *
     * @param columnId       column identifier as defined in XML descriptor. May or may not correspond to an entity property.
     * @param generator      column generator instance
     * @param componentClass class of components that generator will provide
     */
    void addGeneratedColumn(String columnId, ColumnGenerator<? super E> generator, Class<? extends Component> componentClass);

    void removeGeneratedColumn(String columnId);

    /**
     * Adds {@link Printable} representation for column. <br>
     * Explicitly added Printable will be used instead of inherited from generated column.
     *
     * @param columnId  column id
     * @param printable printable representation
     */
    void addPrintable(String columnId, Printable<? super E, ?> printable);

    /**
     * Removes {@link Printable} representation of column. <br>
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

    /**
     * Add lightweight click handler for column cells.<br>
     * Web specific: cell value will be wrapped in span with cuba-table-clickable-cell style name.<br>
     * You can use .cuba-table-clickable-cell for CSS rules to specify custom representation of cell value.
     *
     * @param columnId id of column
     * @param clickListener click listener
     */
    void setClickListener(String columnId, CellClickListener clickListener);

    /**
     * Remove click listener.
     *
     * @param columnId id of column
     */
    void removeClickListener(String columnId);

    /**
     * Lightweight click listener for table cells.
     */
    interface CellClickListener {
        /**
         * @param item row item
         * @param columnId id of column
         */
        void onClick(Entity item, String columnId);
    }

    /**
     * Set aggregation distribution provider to handle distribution of data on rows. Supports only TOP
     * aggregation style.
     *
     * @param distributionProvider distribution provider
     */
    void setAggregationDistributionProvider(AggregationDistributionProvider<E> distributionProvider);

    /**
     * @return aggregation distribution provider
     */
    AggregationDistributionProvider<E> getAggregationDistributionProvider();

    /**
     * Show popup inside of Table, relative to last cell click event.<br>
     * Call this method from {@link com.haulmont.cuba.gui.components.Table.CellClickListener} implementation.
     *
     * @param popupComponent popup content
     */
    void showCustomPopup(Component popupComponent);

    /**
     * Show autocloseable popup view with actions, relative to last cell click event.<br>
     * Call this method from {@link com.haulmont.cuba.gui.components.Table.CellClickListener} implementation.<br>
     * Autocloseable means that after any click on action popup will be closed.
     *
     * @param actions actions
     */
    void showCustomPopupActions(List<Action> actions);

    /**
     * Set visibility for table header
     */
    void setColumnHeaderVisible(boolean columnHeaderVisible);
    boolean isColumnHeaderVisible();

    /**
     * Hide or show selection
     */
    void setShowSelection(boolean showSelection);

    /**
     * @return true if selection is visible
     */
    boolean isShowSelection();

    class SortInfo {
        protected final Object propertyId;
        protected final boolean ascending;

        /**
         * Constructor for a SortInfo object.
         *
         * @param propertyId column indicated by a corresponding {@code MetaPropertyPath} object
         * @param ascending  sort direction
         */
        public SortInfo(Object propertyId, boolean ascending) {
            this.propertyId = propertyId;
            this.ascending = ascending;
        }

        /**
         * @return the property Id
         */
        public Object getPropertyId() {
            return propertyId;
        }

        /**
         * @return a sort direction value
         */
        public boolean getAscending() {
            return ascending;
        }
    }

    /**
     * Describes sorting direction.
     */
    enum SortDirection {
        /**
         * Ascending (e.g. A-Z, 1..9) sort order
         */
        ASCENDING,

        /**
         * Descending (e.g. Z-A, 9..1) sort order
         */
        DESCENDING
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    class Column implements HasXmlDescriptor, HasCaption, HasFormatter {

        private static final Logger log = LoggerFactory.getLogger(Table.class);

        protected Object id;
        protected String caption;
        protected String description;
        protected String valueDescription;
        protected boolean editable;
        protected Formatter formatter;
        protected Integer width;
        protected boolean collapsed;
        protected boolean groupAllowed = true;
        protected boolean sortable = true;
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

        public Column(Class<? extends Entity> entityClass, String propertyPath) {
            MetaClass metaClass = AppBeans.get(Metadata.class).getClassNN(entityClass);
            MetaPropertyPath mpp = metaClass.getPropertyPath(propertyPath);

            if (mpp == null) {
                throw new IllegalArgumentException(String.format("Unable to find %s in %s", propertyPath, entityClass));
            }

            this.id = mpp;
            this.caption = AppBeans.get(MessageTools.class).getPropertyCaption(metaClass, propertyPath);
            this.type = mpp.getRangeJavaClass();
        }

        public Object getId() {
            return id;
        }

        @Nullable
        public MetaPropertyPath getMetaPropertyPath() {
            if (id instanceof MetaPropertyPath) {
                return (MetaPropertyPath) id;
            }
            return null;
        }

        public MetaPropertyPath getMetaPropertyPathNN() {
            if (id instanceof MetaPropertyPath) {
                return (MetaPropertyPath) id;
            }
            throw new IllegalStateException("Column is not bound to meta property " + id);
        }

        public String getIdString() {
            if (id instanceof MetaPropertyPath) {
                return id.toString();
            }
            return String.valueOf(id);
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
            if (owner != null) {
                owner.setColumnDescription(this, description);
            }
        }

        public String getValueDescription() {
            return valueDescription;
        }

        public void setValueDescription(String valueDescription) {
            this.valueDescription = valueDescription;
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

        public boolean isGroupAllowed() {
            return groupAllowed;
        }

        public void setGroupAllowed(boolean groupAllowed) {
            this.groupAllowed = groupAllowed;
            if (owner != null && owner instanceof GroupTable) {
                ((GroupTable) owner).setColumnGroupAllowed(this, groupAllowed);
            }
        }

        public boolean isSortable() {
            return sortable;
        }

        public void setSortable(boolean sortable) {
            this.sortable = sortable;
            if (owner != null) {
                owner.setColumnSortable(this, sortable);
            }
        }

        public AggregationInfo getAggregation() {
            return aggregation;
        }

        public void setAggregation(AggregationInfo aggregation) {
            this.aggregation = aggregation;
            if (owner != null) {
                owner.addAggregationProperty(this, aggregation.getType());
            }
        }

        @Deprecated
        public boolean isCalculatable() {
            return calculatable;
        }

        @Deprecated
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

        public boolean isAggregationEditable() {
            return aggregation != null && aggregation.isEditable();
        }

        @Override
        public String toString() {
            return id == null ? super.toString() : id.toString();
        }
    }

    /**
     * Special component for generated columns which will be rendered as simple text cell.
     * Very useful for heavy tables to decrease rendering time in browser.
     */
    class PlainTextCell implements Component {
        
        protected Component parent;
        protected String text;

        public PlainTextCell(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public void setId(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Component getParent() {
            return parent;
        }

        @Override
        public void setParent(Component parent) {
            this.parent = parent;
        }

        @Override
        public String getDebugId() {
            return null;
        }

        @Override
        public void setDebugId(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public void setEnabled(boolean enabled) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isResponsive() {
            return false;
        }

        @Override
        public void setResponsive(boolean responsive) {
        }

        @Override
        public boolean isVisible() {
            return true;
        }

        @Override
        public void setVisible(boolean visible) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isVisibleItself() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isEnabledItself() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void requestFocus() {
        }

        @Override
        public float getHeight() {
            return -1;
        }

        @Override
        public int getHeightUnits() {
            return UNITS_PIXELS;
        }

        @Override
        public void setHeight(String height) {
            throw new UnsupportedOperationException();
        }

        @Override
        public float getWidth() {
            return -1;
        }

        @Override
        public int getWidthUnits() {
            return UNITS_PIXELS;
        }

        @Override
        public void setWidth(String width) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Alignment getAlignment() {
            return Alignment.TOP_LEFT;
        }

        @Override
        public void setAlignment(Alignment alignment) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getStyleName() {
            return null;
        }

        @Override
        public void setStyleName(String styleName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addStyleName(String styleName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void removeStyleName(String styleName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <X> X unwrap(Class<X> internalComponentClass) {
            return null;
        }

        @Override
        public <X> X unwrapComposition(Class<X> internalCompositionClass) {
            return null;
        }
    }

    /**
     * Allows to handle a group or total aggregation value changes.
     */
    interface AggregationDistributionProvider<E> {

        /**
         * Invoked when a group or total aggregation value is changed.
         *
         * @param context context
         */
        void onDistribution(AggregationDistributionContext<E> context);
    }

    /**
     * Object that contains information about aggregation distribution.
     *
     * @param <E> entity type
     */
    class AggregationDistributionContext<E> {
        protected Column column;
        protected Object value;
        protected Collection<E> scope;
        protected boolean isTotalAggregation;

        public AggregationDistributionContext(Column column, Object value, Collection<E> scope,
                                              boolean isTotalAggregation) {
            this.column = column;
            this.value = value;
            this.scope = scope;
            this.isTotalAggregation = isTotalAggregation;
        }

        public Column getColumn() {
            return column;
        }

        public String getColumnId() {
            return column.getIdString();
        }

        public Object getValue() {
            return value;
        }

        public Collection<E> getScope() {
            return scope;
        }

        public boolean isTotalAggregation() {
            return isTotalAggregation;
        }
    }
}