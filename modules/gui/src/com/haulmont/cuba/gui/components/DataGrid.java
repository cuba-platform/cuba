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

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;

import static com.haulmont.cuba.gui.components.Component.MouseEventDetails.MouseButton;

public interface DataGrid<E extends Entity>
        extends
        ListComponent<E>, Component.HasButtonsPanel, Component.HasCaption, Component.HasIcon,
        Component.HasRowsCount, Component.HasSettings {

    String NAME = "dataGrid";

    /**
     * Returns a copy of currently configured columns in their current visual
     * order in this DataGrid.
     *
     * @return unmodifiable copy of current columns
     * @see #getVisibleColumns()
     */
    List<Column> getColumns();

    /**
     * Returns a copy of currently visible columns in their current visual
     * order in this DataGrid.
     *
     * @return copy of current visible columns
     * @see #getColumns()
     */
    List<Column> getVisibleColumns();

    /**
     * Returns a column based on the Id.
     *
     * @param id the column Id
     * @return the column or {@code null} if not found
     * @see #getColumnNN(String)
     */
    @Nullable
    Column getColumn(String id);

    /**
     * Returns a column by its Id.
     *
     * @param id the column Id
     * @return the column with given Id
     * @throws java.lang.IllegalStateException if not found
     * @see #getColumn(String)
     */
    Column getColumnNN(String id);

    /**
     * Adds the given column to DataGrid.
     *
     * @param column the column to add
     * @see #addColumn(String, MetaPropertyPath)
     */
    void addColumn(Column column);

    void addColumn(Column column, int index);

    /**
     * Creates new column with given Id and property, then adds this column to DataGrid.
     *
     * @param id           the column Id
     * @param propertyPath the instance of {@link MetaPropertyPath} representing a relative path
     *                     to a property from certain MetaClass
     * @return the newly created column
     * @see #addColumn(Column)
     */
    Column addColumn(String id, MetaPropertyPath propertyPath);

    Column addColumn(String id, MetaPropertyPath propertyPath, int index);

    /**
     * Removes the given column from DataGrid or do nothing if column is {@code null}.
     *
     * @param column the column to add
     * @see #removeColumn(String)
     */
    void removeColumn(Column column);

    /**
     * Removes a column from DataGrid by its Id or do nothing if column is not found.
     *
     * @param id the columns Id
     * @see #removeColumn(Column)
     */
    void removeColumn(String id);

    /**
     * @return the DataGrid data source
     */
    CollectionDatasource getDatasource();

    /**
     * Sets an instance of {@link CollectionDatasource} as the DataGrid data source.
     *
     * @param datasource the DataGrid data source, not null
     */
    void setDatasource(CollectionDatasource datasource);

    /**
     * Marks all the items in the current data provider as selected
     */
    void selectAll();

    /**
     * Returns whether column reordering is allowed. Default value is {@code true}.
     *
     * @return {@code true} if reordering is allowed
     */
    boolean isColumnReorderingAllowed();

    /**
     * Sets whether or not column reordering is allowed. Default value is {@code true}.
     *
     * @param columnReorderingAllowed specifies whether column reordering is allowed
     */
    void setColumnReorderingAllowed(boolean columnReorderingAllowed);

    /**
     * Returns the visibility of the header section.
     *
     * @return {@code true} if visible, {@code false} otherwise
     */
    boolean isHeaderVisible();

    /**
     * Sets the visibility of the header section.
     *
     * @param headerVisible {@code true} to show header section, {@code false} to hide
     */
    void setHeaderVisible(boolean headerVisible);

    /**
     * @return an action to be executed on double click on a DataGrid row,
     * assigned by {@link #setItemClickAction(Action)}
     * @see #setItemClickAction(Action)
     */
    Action getItemClickAction();

    /**
     * Assigns an action to be executed on double click on a DataGrid row.
     * <p>
     * If such action is not set, the table responds to pressing Enter by trying to find and execute the following
     * actions:
     * <ul>
     * <li>action, assigned to Enter key press by setting its {@code shortcut} property</li>
     * <li>action named "edit"</li>
     * <li>action named "view"</li>
     * </ul>
     * <p>
     * If one of these actions is found and it is enabled, it is executed.
     *
     * @param action an action to be executed on double click on a DataGrid row
     */
    void setItemClickAction(Action action);

    /**
     * @return an action to be executed on Enter key press, assigned by {@link #setEnterPressAction(Action)}
     * @see #setEnterPressAction(Action)
     */
    Action getEnterPressAction();

    /**
     * Assigns an action to be executed on Enter key press.
     * <p>
     * If such action is not set, the table responds to pressing Enter by trying to find and execute the following
     * actions:
     * <ul>
     * <li>action, assigned by {@link #setItemClickAction(Action)}</li>
     * <li>action, assigned to Enter key press by setting its {@code shortcut} property</li>
     * <li>action named "edit"</li>
     * <li>action named "view"</li>
     * </ul>
     * <p>
     * If one of these actions is found and it is enabled, it is executed.
     *
     * @param action an action to be executed on Enter key press
     * @see #setItemClickAction(Action)
     */
    void setEnterPressAction(Action action);

    /**
     * Gets the number of frozen columns in this DataGrid. 0 means that no data
     * columns will be frozen, but the built-in selection checkbox column will
     * still be frozen if it's in use. -1 means that not even the selection
     * column is frozen.
     * <p>
     * <em>NOTE:</em> this count includes {@link Column#isHidden() hidden
     * columns} in the count.
     *
     * @return the number of frozen columns
     * @see #setFrozenColumnCount(int)
     */
    int getFrozenColumnCount();

    /**
     * Sets the number of frozen columns in this grid. Setting the count to 0
     * means that no data columns will be frozen, but the built-in selection
     * checkbox column will still be frozen if it's in use. Setting the count to
     * -1 will also disable the selection column.
     * <p>
     * The default value is 0.
     *
     * @param numberOfColumns the number of columns that should be frozen
     * @throws IllegalArgumentException if the column count is < 0 or > the number of visible columns
     */
    void setFrozenColumnCount(int numberOfColumns);

    /**
     * @return {@code true} if individual column sortable
     * attribute can be set to {@code true}, {@code false} otherwise
     */
    boolean isSortable();

    /**
     * Defines if this attribute can be changed for individual column or not. Default value is {@code true}.
     *
     * @param sortable {@code true} if individual column sortable
     *                 attribute can be set to {@code true}, {@code false} otherwise
     */
    void setSortable(boolean sortable);

    /**
     * @return {@code true} if individual column hidable attribute
     * can be set to {@code true}, {@code false} otherwise
     */
    boolean isColumnsHidingAllowed();

    /**
     * Defines if hidable attribute can be changed for individual column or not. Default value is {@code true}.
     *
     * @param columnsHidingAllowed {@code true} if individual column hidable attribute
     *                             can be set to {@code true}, {@code false} otherwise
     */
    void setColumnsHidingAllowed(boolean columnsHidingAllowed);

    /**
     * Repaint UI representation of the DataGrid without refreshing the table data.
     */
    void repaint();

    enum ScrollDestination {
        /**
         * Scroll as little as possible to show the target element. If the element
         * fits into view, this works as START or END depending on the current
         * scroll position. If the element does not fit into view, this works as
         * START.
         */
        ANY,

        /**
         * Scrolls so that the element is shown at the start of the viewport. The
         * viewport will, however, not scroll beyond its contents.
         */
        START,

        /**
         * Scrolls so that the element is shown in the middle of the viewport. The
         * viewport will, however, not scroll beyond its contents, given more
         * elements than what the viewport is able to show at once. Under no
         * circumstances will the viewport scroll before its first element.
         */
        MIDDLE,

        /**
         * Scrolls so that the element is shown at the end of the viewport. The
         * viewport will, however, not scroll before its first element.
         */
        END
    }

    /**
     * Scrolls to a certain item, using {@link ScrollDestination#ANY}.
     *
     * @param item item to scroll to
     * @see #scrollTo(Entity, ScrollDestination)
     * @see #scrollToStart()
     * @see #scrollToEnd()
     */
    void scrollTo(E item);

    /**
     * Scrolls to a certain item, using user-specified scroll destination.
     *
     * @param item        item to scroll to
     * @param destination value specifying desired position of scrolled-to row
     * @see #scrollTo(Entity)
     * @see #scrollToStart()
     * @see #scrollToEnd()
     */
    void scrollTo(E item, ScrollDestination destination);

    /**
     * Scrolls to the first data item.
     *
     * @see #scrollTo(Entity)
     * @see #scrollTo(Entity, ScrollDestination)
     * @see #scrollToEnd()
     */
    void scrollToStart();

    /**
     * Scrolls to the last data item.
     *
     * @see #scrollTo(Entity)
     * @see #scrollTo(Entity, ScrollDestination)
     * @see #scrollToStart()
     */
    void scrollToEnd();

    enum ColumnResizeMode {
        /**
         * When column resize mode is set to Animated, columns
         * are resized as they are dragged.
         */
        ANIMATED,

        /**
         * When column resize mode is set to Simple, dragging to resize
         * a column will show a marker, and the column will resize only
         * after the mouse button or touch is released.
         */
        SIMPLE
    }

    /**
     * Returns the current column resize mode. The default mode is {@link ColumnResizeMode#ANIMATED}.
     *
     * @return a ColumnResizeMode value
     */
    ColumnResizeMode getColumnResizeMode();

    /**
     * Sets the column resize mode to use. The default mode is {@link ColumnResizeMode#ANIMATED}.
     *
     * @param mode a ColumnResizeMode value
     */
    void setColumnResizeMode(ColumnResizeMode mode);

    enum SelectionMode {
        /**
         * A SelectionMode that supports for only single rows to be selected at a time.
         */
        SINGLE,

        /**
         * A SelectionMode that supports multiple selections to be made.
         */
        MULTI,

        /**
         * A SelectionMode that supports multiple selections to be made, using built-in selection
         * checkbox column.
         */
        MULTI_CHECK,

        /**
         * A SelectionMode that does not allow for rows to be selected.
         */
        NONE
    }

    /**
     * @return the currently used {@link SelectionMode}
     */
    SelectionMode getSelectionMode();

    /**
     * Sets the DataGrid's selection mode.
     *
     * @param selectionMode the selection mode to use
     */
    void setSelectionMode(SelectionMode selectionMode);

    /**
     * Allows to define different styles for DataGrid rows.
     */
    interface RowStyleProvider<E extends Entity> {
        /**
         * Called by {@link DataGrid} to get a style for row.
         *
         * @param entity an entity instance represented by the current row
         * @return style name or null to apply the default
         */
        String getStyleName(E entity);
    }

    /**
     * Adds style provider for the DataGrid rows.
     * <p>
     * DataGrid can use several providers to obtain many style names for rows.
     */
    void addRowStyleProvider(RowStyleProvider<? super E> styleProvider);

    /**
     * Removes style provider for the DataGrid rows.
     */
    void removeRowStyleProvider(RowStyleProvider<? super E> styleProvider);

    /**
     * Allows to define different styles for DataGrid cells.
     */
    interface CellStyleProvider<E extends Entity> {
        /**
         * Called by {@link DataGrid} to get a style for cell.
         *
         * @param entity   an entity instance represented by the current row
         * @param property data item property identifier
         * @return style name or null to apply the default
         */
        String getStyleName(E entity, @Nullable String property);
    }

    /**
     * Adds style provider for the DataGrid cells.
     * <p>
     * DataGrid can use several providers to obtain many style names for cells.
     */
    void addCellStyleProvider(CellStyleProvider<? super E> styleProvider);

    /**
     * Removes style provider for the DataGrid cells.
     */
    void removeCellStyleProvider(CellStyleProvider<? super E> styleProvider);

    abstract class AbstractDataGridEvent extends EventObject {

        public AbstractDataGridEvent(DataGrid component) {
            super(component);
        }

        @Override
        public DataGrid getSource() {
            return (DataGrid) super.getSource();
        }
    }

    /**
     * An event listener for column visibility change events in the DataGrid.
     */
    interface ColumnVisibilityChangeListener {
        /**
         * Called when a column has become hidden or unhidden.
         *
         * @param event an event providing more information
         */
        void columnVisibilityChanged(ColumnVisibilityChangeEvent event);
    }

    /**
     * An event that is fired when a column's visibility changes.
     */
    class ColumnVisibilityChangeEvent extends AbstractDataGridEvent {
        protected final Column column;
        protected final boolean hidden;

        /**
         * Constructor for a column visibility change event.
         *
         * @param component the DataGrid from which this event originates
         * @param column    the column that changed its visibility
         * @param hidden    {@code true} if the column was hidden,
         *                  {@code false} if it became visible
         */
        public ColumnVisibilityChangeEvent(DataGrid component, Column column, boolean hidden) {
            super(component);
            this.column = column;
            this.hidden = hidden;
        }

        /**
         * Gets the column that became hidden or visible.
         *
         * @return the column that became hidden or visible.
         * @see Column#isHidden()
         */
        public Column getColumn() {
            return column;
        }

        /**
         * @return {@code true} if the column was hidden {@code false} if it was set visible
         */
        public boolean isHidden() {
            return hidden;
        }
    }

    /**
     * Registers a new column visibility change listener
     *
     * @param listener the listener to register
     */
    void addColumnVisibilityChangeListener(ColumnVisibilityChangeListener listener);

    /**
     * Removes a previously registered column visibility change listener
     *
     * @param listener the listener to remove
     */
    void removeColumnVisibilityChangeListener(ColumnVisibilityChangeListener listener);

    /**
     * An event listener for column reorder events in the DataGrid.
     */
    interface ColumnReorderListener {
        /**
         * Called when the columns of the DataGrid have been reordered.
         *
         * @param event an event providing more information
         */
        void columnReordered(ColumnReorderEvent event);
    }

    /**
     * An event that is fired when the columns are reordered.
     */
    class ColumnReorderEvent extends AbstractDataGridEvent {

        /**
         * Constructor for a column reorder change event.
         *
         * @param component the DataGrid from which this event originates
         */
        public ColumnReorderEvent(DataGrid component) {
            super(component);
        }
    }

    /**
     * Registers a new column reorder listener.
     *
     * @param listener the listener to register
     */
    void addColumnReorderListener(ColumnReorderListener listener);

    /**
     * Removes a previously registered column reorder listener.
     *
     * @param listener the listener to remove
     */
    void removeColumnReorderListener(ColumnReorderListener listener);

    /**
     * An event listener for column resize events in the DataGrid.
     */
    interface ColumnResizeListener {
        /**
         * Called when the columns of the DataGrid have been resized.
         *
         * @param event an event providing more information
         */
        void columnResized(ColumnResizeEvent event);
    }

    /**
     * An event that is fired when a column is resized.
     */
    class ColumnResizeEvent extends AbstractDataGridEvent {
        protected final Column column;

        /**
         * Constructor for a column resize event.
         *
         * @param component the DataGrid from which this event originates
         */
        public ColumnResizeEvent(DataGrid component, Column column) {
            super(component);
            this.column = column;
        }

        /**
         * Returns the column that was resized.
         *
         * @return the resized column.
         */
        public Column getColumn() {
            return column;
        }
    }

    /**
     * Registers a new column resize listener.
     *
     * @param listener the listener to register
     */
    void addColumnResizeListener(ColumnResizeListener listener);

    /**
     * Removes a previously registered column resize listener.
     *
     * @param listener the listener to remove
     */
    void removeColumnResizeListener(ColumnResizeListener listener);

    /**
     * An event listener for selection events in the DataGrid.
     */
    interface SelectionListener<E> {
        /**
         * Called when the selection state has changed.
         *
         * @param event an event providing more information
         */
        void selected(SelectionEvent<E> event);
    }

    /**
     * An event that specifies what in a selection has changed, and where the
     * selection took place.
     */
    class SelectionEvent<E> extends AbstractDataGridEvent {
        protected final List<E> added;
        protected final List<E> removed;
        protected final List<E> selected;

        /**
         * Constructor for a selection event.
         *
         * @param component the DataGrid from which this event originates
         * @param added     items that became selected
         * @param removed   items that became deselected
         * @param selected  items that are currently selected
         */
        public SelectionEvent(DataGrid component,
                              List<E> added, List<E> removed, List<E> selected) {
            super(component);

            this.added = Collections.unmodifiableList(added);
            this.removed = Collections.unmodifiableList(removed);
            this.selected = Collections.unmodifiableList(selected);
        }

        /**
         * A {@link List} of all the items that became selected.
         * <p>
         * <em>Note:</em> this excludes all items that might have been previously
         * selected.
         *
         * @return a List of the items that became selected
         */
        public List<E> getAdded() {
            return added;
        }

        /**
         * A {@link List} of all the items that became deselected.
         * <p>
         * <em>Note:</em> this excludes all items that might have been previously
         * deselected.
         *
         * @return a List of the items that became deselected
         */
        public List<E> getRemoved() {
            return removed;
        }

        /**
         * A {@link List} of all the items that are currently selected.
         *
         * @return a List of the items that are currently selected
         */
        public List<E> getSelected() {
            return selected;
        }
    }

    /**
     * Registers a new selection listener
     *
     * @param listener the listener to register
     */
    void addSelectionListener(SelectionListener<E> listener);

    /**
     * Removes a previously registered selection change listener
     *
     * @param listener the listener to remove
     */
    void removeSelectionListener(SelectionListener<E> listener);

    /**
     * Listener for sort order change events.
     */
    interface SortListener {
        /**
         * Called when the sort order has changed.
         *
         * @param event an event providing more information
         */
        void sorted(SortEvent event);
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

    /**
     * Sort order descriptor. Links together a {@link SortDirection} value and a
     * DataGrid column Id.
     */
    class SortOrder {
        protected final String columnId;
        protected final SortDirection direction;

        /**
         * Constructor for a SortOrder object. Both arguments must be non-null.
         *
         * @param columnId  id of the DataGrid column to sort by
         * @param direction value indicating whether the property id should be sorted in
         *                  ascending or descending order
         */
        public SortOrder(String columnId, SortDirection direction) {
            this.columnId = columnId;
            this.direction = direction;
        }

        /**
         * @return the column Id
         */
        public String getColumnId() {
            return columnId;
        }

        /**
         * Returns the {@link SortDirection} value.
         *
         * @return a sort direction value
         */
        public SortDirection getDirection() {
            return direction;
        }
    }

    /**
     * An event that is fired when a sort order is changed.
     */
    class SortEvent extends AbstractDataGridEvent {
        protected final List<SortOrder> sortOrder;

        /**
         * Creates a new sort order change event with a sort order list.
         *
         * @param component the DataGrid from which this event originates
         * @param sortOrder the new sort order list
         */
        public SortEvent(DataGrid component, List<SortOrder> sortOrder) {
            super(component);
            this.sortOrder = sortOrder;
        }

        /**
         * @return the sort order list
         */
        public List<SortOrder> getSortOrder() {
            return sortOrder;
        }
    }

    /**
     * Registers a new sort order change listener
     *
     * @param listener the listener to register
     */
    void addSortListener(SortListener listener);

    /**
     * Removes a previously registered sort order change listener
     *
     * @param listener the listener to remove
     */
    void removeSortListener(SortListener listener);

    /**
     * Listener for context click events.
     */
    interface ContextClickListener {
        /**
         * Called when the context click happens.
         *
         * @param event an event providing more information
         */
        void onContextClick(ContextClickEvent event);
    }

    /**
     * Context click event fired by a {@link DataGrid}. ContextClickEvent happens
     * when context click happens on the client-side inside the DataGrid.
     */
    class ContextClickEvent extends DataGridClickEvent {

        /**
         * Constructor for a context click event.
         *
         * @param component the DataGrid from which this event originates
         * @param details   an instance of {@link MouseEventDetails} with information about mouse event details
         */
        public ContextClickEvent(DataGrid component, MouseEventDetails details) {
            super(component, details);
        }
    }

    /**
     * Registers a new context click listener
     *
     * @param listener the listener to register
     */
    void addContextClickListener(ContextClickListener listener);

    /**
     * Removes a previously registered context click listener
     *
     * @param listener the listener to remove
     */
    void removeContextClickListener(ContextClickListener listener);

    /**
     * Listener for item click events.
     */
    interface ItemClickListener<E> {
        /**
         * Called when the item click happens.
         *
         * @param event an event providing more information
         */
        void onItemClick(ItemClickEvent<E> event);
    }

    /**
     * Click event fired by a {@link DataGrid}
     */
    class ItemClickEvent<E> extends DataGridClickEvent {
        private E item;
        private Object itemId;
        private String columnId;

        /**
         * Constructor for a item click event.
         *
         * @param component the DataGrid from which this event originates
         * @param details   an instance of {@link MouseEventDetails} with information about mouse event details
         * @param item      an entity instance represented by the clicked row
         * @param itemId    an item Id
         * @param columnId  id of the clicked DataGrid column
         */
        public ItemClickEvent(DataGrid component,
                              MouseEventDetails details, E item, Object itemId, String columnId) {
            super(component, details);

            this.item = item;
            this.itemId = itemId;
            this.columnId = columnId;
        }

        /**
         * @return an entity instance represented by the clicked row
         */
        public E getItem() {
            return item;
        }

        /**
         * @return an item Id
         */
        public Object getItemId() {
            return itemId;
        }

        /**
         * @return id of the clicked DataGrid column
         */
        public String getColumnId() {
            return columnId;
        }
    }

    /**
     * Registers a new item click listener
     *
     * @param listener the listener to register
     */
    void addItemClickListener(ItemClickListener<E> listener);

    /**
     * Removes a previously registered item click listener
     *
     * @param listener the listener to remove
     */
    void removeItemClickListener(ItemClickListener<E> listener);

    /**
     * Class for holding information about a mouse click event. A
     * {@link DataGridClickEvent} is fired when the user clicks on a {@code Component}.
     */
    class DataGridClickEvent extends AbstractDataGridEvent {
        protected final MouseEventDetails details;

        public DataGridClickEvent(DataGrid component, MouseEventDetails details) {
            super(component);
            this.details = details;
        }

        /**
         * Returns an identifier describing which mouse button the user pushed.
         * Compare with {@link MouseButton#LEFT},{@link MouseButton#MIDDLE},
         * {@link MouseButton#RIGHT} to find out which button it is.
         *
         * @return one of {@link MouseButton#LEFT}, {@link MouseButton#MIDDLE}, {@link MouseButton#RIGHT}.
         */
        public MouseButton getButton() {
            return details.getButton();
        }

        /**
         * Returns the mouse position (x coordinate) when the click took place.
         * The position is relative to the browser client area.
         *
         * @return The mouse cursor x position
         */
        public int getClientX() {
            return details.getClientX();
        }

        /**
         * Returns the mouse position (y coordinate) when the click took place.
         * The position is relative to the browser client area.
         *
         * @return The mouse cursor y position
         */
        public int getClientY() {
            return details.getClientY();
        }

        /**
         * Returns the relative mouse position (x coordinate) when the click
         * took place. The position is relative to the clicked component.
         *
         * @return The mouse cursor x position relative to the clicked layout
         *         component or -1 if no x coordinate available
         */
        public int getRelativeX() {
            return details.getRelativeX();
        }

        /**
         * Returns the relative mouse position (y coordinate) when the click
         * took place. The position is relative to the clicked component.
         *
         * @return The mouse cursor y position relative to the clicked layout
         *         component or -1 if no y coordinate available
         */
        public int getRelativeY() {
            return details.getRelativeY();
        }

        /**
         * Checks if the event is a double click event.
         *
         * @return {@code true} if the event is a double click event, {@code false} otherwise
         */
        public boolean isDoubleClick() {
            return details.isDoubleClick();
        }

        /**
         * Checks if the Alt key was down when the mouse event took place.
         *
         * @return {@code true} if Alt was down when the event occurred, {@code false} otherwise
         */
        public boolean isAltKey() {
            return details.isAltKey();
        }

        /**
         * Checks if the Ctrl key was down when the mouse event took place.
         *
         * @return {@code true} if Ctrl was pressed when the event occurred, {@code false} otherwise
         */
        public boolean isCtrlKey() {
            return details.isCtrlKey();
        }

        /**
         * Checks if the Meta key was down when the mouse event took place.
         *
         * @return {@code true} if Meta was pressed when the event occurred, {@code false} otherwise
         */
        public boolean isMetaKey() {
            return details.isMetaKey();
        }

        /**
         * Checks if the Shift key was down when the mouse event took place.
         *
         * @return {@code true} if Shift was pressed when the event occurred, {@code false} otherwise
         */
        public boolean isShiftKey() {
            return details.isShiftKey();
        }
    }

    /**
     * A column in the DataGrid.
     */
    interface Column extends HasXmlDescriptor, HasFormatter, Serializable {

        /**
         * @return id of a column
         */
        String getId();

        /**
         * @return the instance of {@link MetaPropertyPath} representing a relative path
         *         to a property from certain MetaClass
         */
        @Nullable
        MetaPropertyPath getPropertyPath();

        /**
         * @return the caption of the header
         */
        String getCaption();

        /**
         * Sets the caption of the header. This caption is also used as the
         * hiding toggle caption, unless it is explicitly set via
         * {@link #setHidingToggleCaption(String)}.
         *
         * @param caption the text to show in the caption
         */
        void setCaption(String caption);

        /**
         * @return the caption for the hiding toggle for this column
         */
        String getHidingToggleCaption();

        /**
         * Sets the caption of the hiding toggle for this column. Shown in the
         * toggle for this column in the DataGrid's sidebar when the column is
         * {@link #isHidable() hidable}.
         * <p>
         * The default value is <code>null</code>, and in that case the column's
         * {@link #getCaption() header caption} is used.
         * <p>
         * <em>NOTE:</em> setting this to empty string might cause the hiding
         * toggle to not render correctly.
         *
         * @param hidingToggleCaption the text to show in the column hiding toggle
         */
        void setHidingToggleCaption(String hidingToggleCaption);

        /**
         * @return the width in pixels of the column
         */
        double getWidth();

        /**
         * Sets the width (in pixels).
         * <p>
         * This overrides any configuration set by any of
         * {@link #setExpandRatio(int)}, {@link #setMinimumWidth(double)} or
         * {@link #setMaximumWidth(double)}.
         *
         * @param width the new pixel width of the column
         */
        void setWidth(double width);

        /**
         * @return whether the width is auto
         */
        boolean isWidthAuto();

        /**
         * Marks the column width as auto. An auto width means the
         * DataGrid is free to resize the column based on the cell contents and
         * available space in the grid.
         */
        void setWidthAuto();

        /**
         * @return the column's expand ratio
         * @see #setExpandRatio(int)
         */
        int getExpandRatio();

        /**
         * Sets the ratio with which the column expands.
         * <p>
         * By default, all columns expand equally (treated as if all of them had
         * an expand ratio of 1). Once at least one column gets a defined expand
         * ratio, the implicit expand ratio is removed, and only the defined
         * expand ratios are taken into account.
         * <p>
         * If a column has a defined width ({@link #setWidth(double)}), it
         * overrides this method's effects.
         * <p>
         * <em>Example:</em> A DataGrid with three columns, with expand ratios 0, 1
         * and 2, respectively. The column with a <strong>ratio of 0 is exactly
         * as wide as its contents requires</strong>. The column with a ratio of
         * 1 is as wide as it needs, <strong>plus a third of any excess
         * space</strong>, because we have 3 parts total, and this column
         * reserves only one of those. The column with a ratio of 2, is as wide
         * as it needs to be, <strong>plus two thirds</strong> of the excess
         * width.
         *
         * @param expandRatio the expand ratio of this column. {@code 0} to not have it
         *                    expand at all. A negative number to clear the expand
         *                    value.
         * @see #setWidth(double)
         */
        void setExpandRatio(int expandRatio);

        /**
         * Clears the expand ratio for this column.
         * <p>
         * Equal to calling {@link #setExpandRatio(int) setExpandRatio(-1)}
         */
        void clearExpandRatio();

        /**
         * @return the minimum width for this column
         * @see #setMinimumWidth(double)
         */
        double getMinimumWidth();

        /**
         * Sets the minimum width for this column.
         * <p>
         * This defines the minimum guaranteed pixel width of the column
         * <em>when it is set to expand</em>.
         *
         * @param pixels the new minimum pixel width of the column
         * @see #setWidth(double)
         * @see #setExpandRatio(int)
         */
        void setMinimumWidth(double pixels);

        /**
         * @return the maximum width for this column
         * @see #setMaximumWidth(double)
         */
        double getMaximumWidth();

        /**
         * Sets the maximum width for this column.
         * <p>
         * This defines the maximum allowed pixel width of the column
         * <em>when it is set to expand</em>.
         *
         * @param pixels the new maximum pixel width of the column
         * @see #setWidth(double)
         * @see #setExpandRatio(int)
         */
        void setMaximumWidth(double pixels);

        /**
         * @return {@code true} if the column is currently hidden, {@code false} otherwise
         */
        boolean isHidden();

        /**
         * Hides or shows the column. By default columns are visible before
         * explicitly hiding them.
         *
         * @param hidden {@code true} to hide the column, {@code false} to show
         */
        void setHidden(boolean hidden);

        /**
         * Returns whether this column can be hidden by the user. Default is {@code true}.
         * <p>
         * <em>Note:</em> the column can be programmatically hidden using
         * {@link #setHidden(boolean)} regardless of the returned value.
         *
         * @return {@code true} if the user can hide the column, {@code false} if not
         * @see DataGrid#isColumnsHidingAllowed()
         * @see DataGrid#setColumnsHidingAllowed(boolean)
         */
        boolean isHidable();

        /**
         * Sets whether this column can be hidden by the user. Hidable columns
         * can be hidden and shown via the sidebar menu.
         *
         * @param hidable {@code true} if the column may be hidden by the user via UI interaction
         * @see DataGrid#isColumnsHidingAllowed()
         * @see DataGrid#setColumnsHidingAllowed(boolean)
         */
        void setHidable(boolean hidable);

        /**
         * Returns whether the user can sort the grid by this column.
         *
         * @return {@code true} if the column is sortable by the user, {@code false} otherwise
         */
        boolean isSortable();

        /**
         * Sets whether this column is sortable by the user. The DataGrid can be
         * sorted by a sortable column by clicking or tapping the column's
         * default header.
         *
         * @param sortable {@code true} if the user should be able to sort the
         *                 column, {@code false} otherwise
         * @see DataGrid#setSortable(boolean)
         */
        void setSortable(boolean sortable);

        /**
         * Returns whether this column can be resized by the user. Default is
         * {@code true}.
         * <p>
         * <em>Note:</em> the column can be programmatically resized using
         * {@link #setWidth(double)} and {@link #setWidthAuto()} regardless
         * of the returned value.
         *
         * @return {@code true} if this column is resizable, {@code false} otherwise
         */
        boolean isResizable();

        /**
         * Sets whether this column can be resized by the user.
         *
         * @param resizable {@code true} if this column should be resizable, {@code false} otherwise
         */
        void setResizable(boolean resizable);

        /**
         * Sets this column as the last frozen column in its grid.
         *
         * @see DataGrid#setFrozenColumnCount(int)
         */
        void setLastFrozenColumn();

        /**
         * @return the type of value represented by this column
         */
        @Nullable
        Class getType();

        /**
         * @return The DataGrid this column belongs to
         */
        DataGrid getOwner();

        /**
         * @param owner The DataGrid this column belongs to
         */
        void setOwner(DataGrid owner);
    }
}
