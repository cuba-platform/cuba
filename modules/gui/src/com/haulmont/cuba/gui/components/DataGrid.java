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

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.DataGridItems;
import com.haulmont.cuba.gui.components.data.ValueSourceProvider;
import com.haulmont.cuba.gui.components.data.datagrid.DatasourceDataGridItems;
import com.haulmont.cuba.gui.components.data.datagrid.SortableDatasourceDataGridItems;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasource.Sortable;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.model.InstanceContainer;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.haulmont.cuba.gui.components.MouseEventDetails.MouseButton;

/**
 * A grid component for displaying tabular data bound to entity type.
 *
 * @param <E> row item type
 */
public interface DataGrid<E extends Entity> extends ListComponent<E>, HasButtonsPanel, Component.HasCaption,
        Component.HasIcon, HasContextHelp, HasRowsCount, HasSettings, HasDataLoadingSettings, HasHtmlCaption, HasHtmlDescription,
        LookupComponent<E>, Component.Focusable,
        RowsCount.RowsCountTarget, HasSubParts {

    String NAME = "dataGrid";

    static <T extends Entity> TypeToken<DataGrid<T>> of(Class<T> itemClass) {
        return new TypeToken<DataGrid<T>>() {
        };
    }

    /**
     * Returns a copy of currently configured columns in their current visual
     * order in this DataGrid.
     *
     * @return unmodifiable copy of current columns
     * @see #getVisibleColumns()
     */
    List<Column<E>> getColumns();

    /**
     * Returns a copy of columns not hidden by security permissions.
     *
     * @return copy of columns not hidden by security permissions
     * @see #getColumns()
     */
    List<Column<E>> getVisibleColumns();

    /**
     * Returns a column based on the Id.
     *
     * @param id the column Id
     * @return the column or {@code null} if not found
     * @see #getColumnNN(String)
     */
    @Nullable
    Column<E> getColumn(String id);

    /**
     * Returns a column by its Id.
     *
     * @param id the column Id
     * @return the column with given Id
     * @throws java.lang.IllegalStateException if not found
     * @see #getColumn(String)
     */
    Column<E> getColumnNN(String id);

    /**
     * Adds the given column to DataGrid.
     *
     * @param column the column to add
     * @see #addColumn(Column, int)
     * @see #addColumn(String, MetaPropertyPath)
     * @see #addColumn(String, MetaPropertyPath, int)
     */
    void addColumn(Column<E> column);

    /**
     * Adds the given column at the specified index to DataGrid.
     *
     * @param column the column to add
     * @param index  index of a new column
     * @see #addColumn(Column)
     * @see #addColumn(String, MetaPropertyPath)
     * @see #addColumn(String, MetaPropertyPath, int)
     */
    void addColumn(Column<E> column, int index);

    /**
     * Creates new column with given Id and property, then adds this column to DataGrid.
     *
     * @param id           the column Id
     * @param propertyPath the instance of {@link MetaPropertyPath} representing a relative path
     *                     to a property from certain MetaClass
     * @return the newly created column
     * @see #addColumn(Column)
     * @see #addColumn(Column, int)
     * @see #addColumn(String, MetaPropertyPath, int)
     */
    Column<E> addColumn(String id, MetaPropertyPath propertyPath);

    /**
     * Creates new column with given Id and property at the specified index,
     * then adds this column to DataGrid.
     *
     * @param id           the column Id
     * @param propertyPath the instance of {@link MetaPropertyPath} representing a relative path
     *                     to a property from certain MetaClass
     * @param index        index of a new column
     * @return the newly created column
     * @see #addColumn(Column)
     * @see #addColumn(Column, int)
     * @see #addColumn(String, MetaPropertyPath)
     */
    Column<E> addColumn(String id, MetaPropertyPath propertyPath, int index);

    /**
     * Removes the given column from DataGrid or do nothing if column is {@code null}.
     *
     * @param column the column to add
     * @see #removeColumn(String)
     */
    void removeColumn(Column<E> column);

    /**
     * Removes a column from DataGrid by its Id or do nothing if column is not found.
     *
     * @param id the columns Id
     * @see #removeColumn(Column)
     */
    void removeColumn(String id);

    /**
     * @return The DataGrid source
     */
    @Override
    @Nullable
    DataGridItems<E> getItems();

    /**
     * Sets an instance of {@link DataGridItems} as the DataGrid data source.
     *
     * @param dataGridItems the DataGrid data source
     */
    void setItems(@Nullable DataGridItems<E> dataGridItems);

    /**
     * @return the DataGrid data source
     * @deprecated use {@link #getItems()} instead
     */
    @Deprecated
    default CollectionDatasource getDatasource() {
        DataGridItems<E> dataGridItems = getItems();
        return dataGridItems instanceof DatasourceDataGridItems
                ? ((DatasourceDataGridItems) dataGridItems).getDatasource()
                : null;
    }

    /**
     * Sets an instance of {@link CollectionDatasource} as the DataGrid data source.
     *
     * @param datasource the DataGrid data source, not null
     * @deprecated use {@link #setItems(DataGridItems)} instead
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    default void setDatasource(CollectionDatasource datasource) {
        if (datasource == null) {
            setItems(null);
        } else {
            DataGridItems<E> dataGridItems;
            if (datasource instanceof Sortable) {
                dataGridItems = new SortableDatasourceDataGridItems<>((Sortable) datasource);
            } else {
                dataGridItems = new DatasourceDataGridItems<>(datasource);
            }
            setItems(dataGridItems);
        }
    }

    /**
     * Marks all the items in the current data source as selected.
     */
    void selectAll();

    /**
     * Deselects the given item. If the item is not currently selected, does nothing.
     *
     * @param item the item to deselect, not null
     */
    void deselect(E item);

    /**
     * Deselects all the items in the current data source.
     */
    void deselectAll();

    /**
     * Sorts the DataGrid data for passed column id in the chosen sort direction.
     *
     * @param columnId  id of the column to sort
     * @param direction sort direction
     */
    void sort(String columnId, SortDirection direction);

    /**
     * @return sort order list
     */
    List<SortOrder> getSortOrder();

    /**
     * @return {@code true} if text selection is enabled.
     */
    boolean isTextSelectionEnabled();

    /**
     * Enable or disable text selection in DataGrid cells. Default value is {@code false}.
     *
     * @param textSelectionEnabled specifies whether text selection in DataGrid cells is enabled
     */
    void setTextSelectionEnabled(boolean textSelectionEnabled);

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
     * @param headerVisible {@code true} to show the header section, {@code false} to hide
     */
    void setHeaderVisible(boolean headerVisible);

    /**
     * Returns the visibility of the footer section.
     *
     * @return {@code true} if visible, {@code false} otherwise
     */
    boolean isFooterVisible();

    /**
     * Sets the visibility of the footer section.
     *
     * @param footerVisible {@code true} to show the footer section, {@code false} to hide
     */
    void setFooterVisible(boolean footerVisible);

    /**
     * Returns the current body row height.
     *
     * @return body row height, -1 if height is AUTO
     */
    double getBodyRowHeight();

    /**
     * Sets the height of a body row. If -1 (default), the row height is
     * calculated based on the theme for an empty row before the DataGrid is
     * displayed.
     *
     * @param rowHeight the height of a row in pixels or -1 for AUTO
     */
    void setBodyRowHeight(double rowHeight);

    /**
     * Returns the current header row height.
     *
     * @return header row height, -1 if height is AUTO
     */
    double getHeaderRowHeight();

    /**
     * Sets the body of a body row. If -1 (default), the row height is
     * calculated based on the theme for an empty row before the DataGrid is
     * displayed.
     *
     * @param rowHeight the height of a row in pixels or -1 for AUTO
     */
    void setHeaderRowHeight(double rowHeight);

    /**
     * Returns the current footer row height.
     *
     * @return footer row height, -1 if height is AUTO
     */
    double getFooterRowHeight();

    /**
     * Sets the body of a footer row. If -1 (default), the row height is
     * calculated based on the theme for an empty row before the DataGrid is
     * displayed.
     *
     * @param rowHeight the height of a row in pixels or -1 for AUTO
     */
    void setFooterRowHeight(double rowHeight);

    /**
     * @return {@code true} if context menu is enabled, {@code false} otherwise
     */
    boolean isContextMenuEnabled();

    /**
     * Sets whether or not context menu is enabled. Default value is {@code true}.
     *
     * @param contextMenuEnabled specifies whether context menu is enabled
     */
    void setContextMenuEnabled(boolean contextMenuEnabled);

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
     *
     * <em>NOTE:</em> this count includes {@link Column#isCollapsed() hidden
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
     * @throws IllegalArgumentException if the column count is &lt; 0 or &gt; the number of visible columns
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
     * @return {@code true} if individual column collapsible attribute
     * can be set to {@code true}, {@code false} otherwise
     */
    boolean isColumnsCollapsingAllowed();

    /**
     * Defines if collapsible attribute can be changed for individual column or not.
     * Default value is {@code true}.
     *
     * @param columnsCollapsingAllowed {@code true} if individual column collapsible attribute
     *                                 can be set to {@code true}, {@code false} otherwise
     */
    void setColumnsCollapsingAllowed(boolean columnsCollapsingAllowed);

    /**
     * Checks whether the item editor UI is enabled for this DataGrid.
     *
     * @return {@code true} if the editor is enabled for this grid
     * @see #setEditorEnabled(boolean)
     * @see #getEditedItem()
     */
    boolean isEditorEnabled();

    /**
     * Sets whether or not the item editor UI is enabled for this DataGrid.
     * When the editor is enabled, the user can open it by double-clicking
     * a row or hitting enter when a row is focused. The editor can also be opened
     * programmatically using the {@link #edit(Entity)} method.
     *
     * @param isEnabled {@code true} to enable the feature, {@code false} otherwise
     * @see #getEditedItem()
     */
    void setEditorEnabled(boolean isEnabled);

    /**
     * Gets the buffered editor mode.
     *
     * @return {@code true} if buffered editor is enabled, {@code false} otherwise
     */
    boolean isEditorBuffered();

    /**
     * Sets the buffered editor mode. The default mode is buffered ({@code true}).
     *
     * @param editorBuffered {@code true} to enable buffered editor, {@code false} to disable it
     */
    void setEditorBuffered(boolean editorBuffered);

    /**
     * Gets the current caption of the save button in the DataGrid editor.
     *
     * @return the current caption of the save button
     */
    String getEditorSaveCaption();

    /**
     * Sets the caption on the save button in the DataGrid editor.
     *
     * @param saveCaption the caption to set
     */
    void setEditorSaveCaption(String saveCaption);

    /**
     * Gets the current caption of the cancel button in the DataGrid editor.
     *
     * @return the current caption of the cancel button
     */
    String getEditorCancelCaption();

    /**
     * Sets the caption on the cancel button in the DataGrid editor.
     *
     * @param cancelCaption the caption to set
     */
    void setEditorCancelCaption(String cancelCaption);

    /**
     * Gets the id of the item that is currently being edited.
     *
     * @return the id of the item that is currently being edited, or
     * {@code null} if no item is being edited at the moment
     * @deprecated use {@link #getEditedItem()} instead
     */
    @Deprecated
    @Nullable
    Object getEditedItemId();

    /**
     * Returns the item that is currently being edited.
     *
     * @return the item that is currently being edited, or
     * {@code null} if no item is being edited at the moment
     */
    @Nullable
    E getEditedItem();

    /**
     * Returns whether an item is currently being edited in the editor.
     *
     * @return {@code true} if the editor is open
     */
    boolean isEditorActive();

    /**
     * Opens the editor interface for the provided item Id. Scrolls the Grid to
     * bring the item to view if it is not already visible.
     *
     * @param itemId the id of the item to edit
     * @throws IllegalStateException    if the editor is not enabled or already editing an item in buffered mode
     * @throws IllegalArgumentException if datasource doesn't contain item with given id
     * @see #setEditorEnabled(boolean)
     * @deprecated Use {@link #edit(Entity)}
     */
    @Deprecated
    void editItem(Object itemId);

    /**
     * Opens the editor interface for the provided entity. Scrolls the Grid to
     * bring the entity to view if it is not already visible.
     *
     * @param item the item to edit
     * @throws IllegalStateException    if the editor is not enabled or already editing an entity in buffered mode
     * @throws IllegalArgumentException if datasource doesn't contain the entity
     * @see #setEditorEnabled(boolean)
     */
    void edit(E item);

    /**
     * Enables cross field validation in the inline editor. True by default.
     *
     * @param validate validate option, true if an editor should validate cross field rules
     */
    void setEditorCrossFieldValidate(boolean validate);

    /**
     * @return true if editor validates cross field rules
     */
    boolean isEditorCrossFieldValidate();

    /**
     * Field generator that generates component for column in {@link DataGrid} editor.
     */
    @Deprecated
    interface ColumnEditorFieldGenerator {
        /**
         * Generates component for {@link DataGrid} editor.
         *
         * @param datasource editing item datasource
         * @param property   editing item property
         * @return generated component
         */
        Field createField(Datasource datasource, String property);
    }

    /**
     * class which stores information that can be used
     * when creating a component for a {@link DataGrid} editor.
     *
     * @param <T> the bean type
     */
    class EditorFieldGenerationContext<T> {

        protected T item;
        protected ValueSourceProvider valueSourceProvider;

        public EditorFieldGenerationContext(T item, ValueSourceProvider valueSourceProvider) {
            this.item = item;
            this.valueSourceProvider = valueSourceProvider;
        }

        /**
         * @return an item that is being edited
         */
        public T getItem() {
            return item;
        }

        /**
         * @return a value source provider in order to obtain value sources
         */
        public ValueSourceProvider getValueSourceProvider() {
            return valueSourceProvider;
        }
    }

    /**
     * The root class from which all DataGrid editor event state objects shall be derived.
     */
    abstract class AbstractDataGridEditorEvent<E extends Entity> extends AbstractDataGridEvent {
        protected E item;
        protected Map<String, Field> fields;

        /**
         * @param component the DataGrid from which this event originates
         * @param item      the editing item
         */
        public AbstractDataGridEditorEvent(DataGrid component, E item, Map<String, Field> fields) {
            super(component);
            this.item = item;
            this.fields = fields;
        }

        /**
         * @return an item Id
         * @deprecated use {@link #getItem()} instead
         */
        @Deprecated
        public Object getItemId() {
            return item.getId();
        }

        /**
         * @return an item
         */
        public E getItem() {
            return item;
        }

        /**
         * @return the components that are used in the editor
         */
        public Map<String, Field> getFields() {
            return fields;
        }

        /**
         * Returns the field corresponding to the given column id.
         *
         * @param columnId a column id
         * @return the field corresponding to the given column id
         */
        public Field getField(String columnId) {
            Field field = fields.get(columnId);
            Preconditions.checkState(field != null, "Field for the given id isn't found. Id: " + columnId);
            return field;
        }
    }

    /**
     * An event that is fired before the item is updated.
     * Provides access to the components that were used in the editor,
     * giving the possibility to use their values programmatically.
     */
    class EditorPreCommitEvent<E extends Entity> extends AbstractDataGridEditorEvent<E> {
        /**
         * Constructor for a DataGrid editor pre commit event.
         *
         * @param component the DataGrid from which this event originates
         * @param item      the editing item
         */
        public EditorPreCommitEvent(DataGrid component, E item, Map<String, Field> fields) {
            super(component, item, fields);
        }
    }

    /**
     * Registers a new DataGrid editor pre commit listener. Works in buffered mode only.
     *
     * @param listener the listener to register
     */
    Subscription addEditorPreCommitListener(Consumer<EditorPreCommitEvent> listener);

    /**
     * Removes a previously registered DataGrid editor pre commit listener.
     *
     * @param listener the listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeEditorPreCommitListener(Consumer<EditorPreCommitEvent> listener);

    /**
     * An event that is fired after the item is updated.
     * Provides access to the components that were used in the editor,
     * giving the possibility to use their values programmatically.
     */
    class EditorPostCommitEvent<E extends Entity> extends AbstractDataGridEditorEvent<E> {
        /**
         * Constructor for a DataGrid editor post commit event.
         *
         * @param component the DataGrid from which this event originates
         * @param item      the edited item
         */
        public EditorPostCommitEvent(DataGrid component, E item, Map<String, Field> fields) {
            super(component, item, fields);
        }
    }

    /**
     * Registers a new DataGrid editor post commit listener. Works in buffered mode only.
     *
     * @param listener the listener to register
     */
    Subscription addEditorPostCommitListener(Consumer<EditorPostCommitEvent> listener);

    /**
     * Removes a previously registered DataGrid editor post commit listener.
     *
     * @param listener the listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeEditorPostCommitListener(Consumer<EditorPostCommitEvent> listener);

    /**
     * An event that is fired when the DataGrid editor is closed.
     * Provides access to the components that were used in the editor,
     * giving the possibility to use their values programmatically.
     */
    class EditorCloseEvent<E extends Entity> extends AbstractDataGridEditorEvent<E> {
        /**
         * Constructor for a DataGrid editor close event.
         *
         * @param component the DataGrid from which this event originates
         * @param item      the edited item
         */
        public EditorCloseEvent(DataGrid component, E item, Map<String, Field> fields) {
            super(component, item, fields);
        }
    }

    /**
     * Registers a new DataGrid editor close listener.
     *
     * @param listener the listener to register
     */
    Subscription addEditorCloseListener(Consumer<EditorCloseEvent> listener);

    /**
     * Removes a previously registered DataGrid editor close listener.
     *
     * @param listener the listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeEditorCloseListener(Consumer<EditorCloseEvent> listener);

    /**
     * An event that is fired before the DataGrid editor is opened.
     * Provides access to the components that will be used in the editor,
     * giving the possibility to change their values programmatically.
     * <p>
     * Sample usage:
     * <pre>{@code
     * dataGrid.addEditorOpenListener(event -> {
     *      Map<String, Field> fields = event.getFields();
     *      Field field1 = fields.get("field1");
     *      Field field2 = fields.get("field2");
     *      Field sum = fields.get("sum");
     *
     *      ValueChangeListener valueChangeListener = e ->
     *      sum.setValue((int) field1.getValue() + (int) field2.getValue());
     *      field1.addValueChangeListener(valueChangeListener);
     *      field2.addValueChangeListener(valueChangeListener);
     * });
     * }</pre>
     */
    class EditorOpenEvent<E extends Entity> extends AbstractDataGridEditorEvent<E> {
        /**
         * @param component the DataGrid from which this event originates
         * @param item      the editing item
         * @param fields    the map, where key - DataGrid column's id
         *                  and value - the field that is used in the editor for this column
         */
        public EditorOpenEvent(DataGrid component, E item, Map<String, Field> fields) {
            super(component, item, fields);
        }
    }

    /**
     * Registers a new DataGrid editor open listener.
     *
     * @param listener the listener to register
     */
    Subscription addEditorOpenListener(Consumer<EditorOpenEvent> listener);

    /**
     * Removes a previously registered DataGrid editor open listener.
     *
     * @param listener the listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeEditorOpenListener(Consumer<EditorOpenEvent> listener);

    /**
     * Repaint UI representation of the DataGrid without refreshing the table data.
     */
    void repaint();

    /**
     * Enumeration, specifying the destinations that are supported when scrolling
     * rows or columns into view.
     */
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
     *
     * @deprecated use {@link Function} instead
     */
    @Deprecated
    interface RowStyleProvider<E extends Entity> extends Function<E, String> {
    }

    /**
     * Adds style provider for the DataGrid rows.
     * <p>
     * DataGrid can use several providers to obtain many style names for rows.
     *
     * @param styleProvider a style provider to add, not null
     */
    void addRowStyleProvider(Function<? super E, String> styleProvider);

    /**
     * Removes style provider for the DataGrid rows.
     *
     * @param styleProvider a style provider to remove, not null
     */
    void removeRowStyleProvider(Function<? super E, String> styleProvider);

    /**
     * Allows to define different styles for DataGrid cells.
     *
     * @deprecated use {@link Column#setStyleProvider(Function)} instead
     */
    @Deprecated
    interface CellStyleProvider<E extends Entity> {
        /**
         * Called by {@link DataGrid} to get a style for cell.
         *
         * @param entity   an entity instance represented by the current row
         * @param columnId id of the DataGrid column
         * @return style name or null to apply the default
         */
        String getStyleName(E entity, String columnId);
    }

    /**
     * Adds style provider for the DataGrid cells.
     * <p>
     * DataGrid can use several providers to obtain many style names for cells.
     *
     * @deprecated use {@link Column#setStyleProvider(Function)} instead
     */
    @Deprecated
    void addCellStyleProvider(CellStyleProvider<? super E> styleProvider);

    /**
     * Removes style provider for the DataGrid cells.
     *
     * @deprecated use {@link Column#setStyleProvider(Function)} instead
     */
    @Deprecated
    void removeCellStyleProvider(CellStyleProvider<? super E> styleProvider);

    /**
     * A callback interface for generating optional descriptions (tooltips) for
     * DataGrid cells. If a cell has both a {@link RowDescriptionProvider row
     * description} and a cell description, the latter has precedence.
     *
     * @deprecated use {@link Column#getDescriptionProvider()} instead
     */
    @Deprecated
    interface CellDescriptionProvider<E extends Entity> {

        /**
         * Called by DataGrid to generate a description (tooltip) for a cell. The
         * description may contain HTML markup.
         *
         * @param entity   an entity instance represented by the current row
         * @param columnId id of the DataGrid column
         * @return the cell description or {@code null} for no description
         */
        String getDescription(E entity, String columnId);
    }

    /**
     * Returns the {@code CellDescriptionProvider} instance used to generate
     * descriptions (tooltips) for DataGrid cells.
     *
     * @return the description provider or {@code null} if no provider is set
     * @deprecated use {@link Column#getDescriptionProvider()} instead
     */
    @Deprecated
    CellDescriptionProvider<E> getCellDescriptionProvider();

    /**
     * Sets the {@code CellDescriptionProvider} instance for generating
     * optional descriptions (tooltips) for individual DataGrid cells. If a
     * {@link RowDescriptionProvider} is also set, the row description it
     * generates is displayed for cells for which {@code provider} returns null.
     *
     * @param provider the description provider to use or {@code null} to remove a
     *                 previously set provider if any
     * @deprecated use {@link Column#setDescriptionProvider(Function)} instead
     */
    @Deprecated
    void setCellDescriptionProvider(CellDescriptionProvider<? super E> provider);

    /**
     * A callback interface for generating optional descriptions (tooltips) for
     * DataGrid rows. If a description is generated for a row, it is used for
     * all the cells in the row for which a {@link CellDescriptionProvider cell
     * description} is not generated.
     *
     * @deprecated use {@link Function} instead
     */
    @Deprecated
    interface RowDescriptionProvider<E extends Entity> extends Function<E, String> {
    }

    /**
     * Returns the {@code RowDescriptionProvider} instance used to generate
     * descriptions (tooltips) for DataGrid rows
     *
     * @return the description provider or {@code} null if no provider is set
     */
    Function<E, String> getRowDescriptionProvider();

    /**
     * Sets the {@code RowDescriptionProvider} instance for generating
     * optional descriptions (tooltips) for DataGrid rows. If a
     * {@link Column#setDescriptionProvider(Function)} is also set,
     * the row description generated by {@code provider} is used for cells
     * for which the cell description provider returns null.
     * <p>
     * This method uses the {@link ContentMode#PREFORMATTED} content mode.
     *
     * @param provider the description provider to use or {@code null} to remove a
     *                 previously set provider if any
     */
    void setRowDescriptionProvider(Function<? super E, String> provider);

    /**
     * Sets the {@code RowDescriptionProvider} instance for generating
     * optional descriptions (tooltips) for DataGrid rows. If a
     * {@link Column#setDescriptionProvider(Function)} is also set,
     * the row description generated by {@code provider} is used for cells
     * for which the cell description provider returns null.
     *
     * @param provider    the description provider to use or {@code null} to remove a
     *                    previously set provider if any
     * @param contentMode the content mode for row tooltips
     */
    void setRowDescriptionProvider(Function<? super E, String> provider, ContentMode contentMode);

    /**
     * The root class from which all DataGrid event state objects shall be derived.
     */
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
     * @param <E> DataGrid data type
     * @param <T> column data type
     */
    interface ColumnGenerator<E extends Entity, T> {
        /**
         * Returns value for given event.
         *
         * @param event an event providing more information
         * @return generated value
         */
        T getValue(ColumnGeneratorEvent<E> event);

        /**
         * Return column type for this generator.
         *
         * @return type of generated property
         */
        Class<T> getType();
    }

    /**
     * INTERNAL
     * <p>
     * ColumnGenerator that is used for declaratively installed generators.
     *
     * @param <E> DataGrid data type
     * @param <T> Column data type
     */
    interface GenericColumnGenerator<E extends Entity, T> {

        /**
         * Returns value for given event.
         *
         * @param event an event providing more information
         * @return generated value
         */
        T getValue(ColumnGeneratorEvent<E> event);
    }

    /**
     * Event provided by a {@link ColumnGenerator}
     */
    class ColumnGeneratorEvent<E extends Entity> extends AbstractDataGridEvent {
        protected E item;
        protected String columnId;
        protected InstanceContainer<E> container;
        protected Function<E, InstanceContainer<E>> containerProvider;

        /**
         * Constructor for a column generator event
         *
         * @param component         the DataGrid from which this event originates
         * @param item              an entity instance represented by the current row
         * @param columnId          a generated column id
         * @param containerProvider a provider that returns an instance container associated with the item
         */
        public ColumnGeneratorEvent(DataGrid component, E item, String columnId,
                                    Function<E, InstanceContainer<E>> containerProvider) {
            super(component);

            this.item = item;
            this.columnId = columnId;
            this.containerProvider = containerProvider;
        }

        /**
         * @return an entity instance represented by the current row
         */
        public E getItem() {
            return item;
        }

        /**
         * @return a generated column id
         */
        public String getColumnId() {
            return columnId;
        }

        /**
         * @return an instance container associated with the item
         */
        public InstanceContainer<E> getContainer() {
            if (container == null) {
                container = containerProvider.apply(item);
            }

            return container;
        }
    }

    /**
     * Add a generated column to the DataGrid.
     *
     * @param columnId  column identifier as defined in XML descriptor
     * @param generator column generator instance
     * @see #addGeneratedColumn(String, ColumnGenerator, int)
     */
    Column<E> addGeneratedColumn(String columnId, ColumnGenerator<E, ?> generator);

    /**
     * Add a generated column to the DataGrid.
     *
     * @param columnId  column identifier as defined in XML descriptor
     * @param generator column generator instance
     * @param index     index of a new generated column
     * @see #addGeneratedColumn(String, ColumnGenerator)
     */
    Column<E> addGeneratedColumn(String columnId, ColumnGenerator<E, ?> generator, int index);

    /**
     * INTERNAL
     * <p>
     * Adds a generated column to the DataGrid.
     *
     * @param columnId  column identifier as defined in XML descriptor
     * @param generator column generator instance
     */
    Column<E> addGeneratedColumn(String columnId, GenericColumnGenerator<E, ?> generator);

    /**
     * Gets the columns generator for the given column id.
     *
     * @param columnId the column id for which to return column generator
     * @return the column generator for given column id
     */
    ColumnGenerator<E, ?> getColumnGenerator(String columnId);

    /**
     * A callback interface for generating details for a particular row in Grid.
     *
     * @param <E> DataGrid data type
     */
    @FunctionalInterface
    interface DetailsGenerator<E extends Entity> {

        /**
         * Returns the component which will be used as details for the given row.
         *
         * @param entity an entity instance represented by the current row
         * @return the details for the given row, or {@code null} to leave the details empty.
         */
        @Nullable
        Component getDetails(E entity);
    }

    /**
     * @return the current details generator for row details or {@code null} if not set
     */
    @Nullable
    DetailsGenerator<E> getDetailsGenerator();

    /**
     * Sets a new details generator for row details.
     * <p>
     * The currently opened row details will be re-rendered.
     *
     * @param detailsGenerator the details generator to set
     */
    void setDetailsGenerator(DetailsGenerator<E> detailsGenerator);

    /**
     * Checks whether details are visible for the given item.
     *
     * @param entity the item for which to check details visibility
     * @return {@code true} if the details are visible
     */
    boolean isDetailsVisible(E entity);

    /**
     * Shows or hides the details for a specific item.
     *
     * @param entity  the item for which to set details visibility
     * @param visible {@code true} to show the details, or {@code false} to hide them
     */
    void setDetailsVisible(E entity, boolean visible);

    /**
     * Marker interface to indicate that the implementing class can be used as a renderer.
     */
    interface Renderer {
    }

    /**
     * A renderer has a null representation.
     * String value which will be used for rendering if the original value is null.
     */
    interface HasNullRepresentation {
        /**
         * Null representation for the renderer.
         *
         * @return a textual representation of {@code null}
         */
        String getNullRepresentation();

        /**
         * Sets null representation for the renderer.
         *
         * @param nullRepresentation a textual representation of {@code null}
         */
        void setNullRepresentation(String nullRepresentation);
    }

    /**
     * A renderer has a locale.
     */
    interface HasLocale {
        /**
         * @return the locale which is used to present values
         */
        Locale getLocale();

        /**
         * Sets the locale in which to present values.
         *
         * @param locale the locale in which to present values
         */
        void setLocale(Locale locale);
    }

    /**
     * A renderer has a DateTimeFormatter.
     */
    interface HasDateTimeFormatter {
        /**
         * @return the pattern describing the date format
         */
        String getFormatPattern();

        /**
         * @param formatPattern the pattern describing the date and time format
         *                      which will be used to create {@link DateTimeFormatter} instance.
         * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#patterns">
         * Format Pattern Syntax</a>
         */
        void setFormatPattern(String formatPattern);

        /**
         * @return the instance of {@link DateTimeFormatter} which is used to present dates
         */
        DateTimeFormatter getFormatter();

        /**
         * @param formatter the instance of {@link DateTimeFormatter} with which to present dates
         */
        void setFormatter(DateTimeFormatter formatter);
    }

    /**
     * Click event fired by a {@link HasRendererClickListener}
     */
    class RendererClickEvent<T extends Entity> extends DataGridClickEvent {
        protected T item;
        protected String columnId;

        /**
         * Constructor for a renderer click event.
         *
         * @param component the DataGrid from which this event originates
         * @param details   an instance of {@link MouseEventDetails} with information about mouse event details
         * @param item      an item
         * @param columnId  id of the DataGrid column
         */
        public RendererClickEvent(DataGrid<T> component,
                                  MouseEventDetails details, T item, String columnId) {
            super(component, details);

            this.item = item;
            this.columnId = columnId;
        }

        /**
         * @return an item
         */
        public T getItem() {
            return item;
        }

        /**
         * @return an item Id
         * @deprecated use {@link #getItem()} instead
         */
        @Deprecated
        public Object getItemId() {
            return item.getId();
        }

        /**
         * @return id of the DataGrid column
         */
        public String getColumnId() {
            return columnId;
        }
    }

    /**
     * Renderer has click listener.
     */
    @FunctionalInterface
    interface HasRendererClickListener<T extends Entity> {
        /**
         * Sets new renderer click listener.
         *
         * @param listener the listener to set
         */
        void setRendererClickListener(Consumer<RendererClickEvent<T>> listener);
    }

    /**
     * A renderer for presenting simple plain-text string values.
     */
    interface TextRenderer extends Renderer, HasNullRepresentation {
    }

    /**
     * A renderer for presenting simple plain-text string values as a link with call back handler.
     */
    interface ClickableTextRenderer<T extends Entity>
            extends Renderer, HasNullRepresentation, HasRendererClickListener<T> {
    }

    /**
     * A renderer for presenting HTML content.
     */
    interface HtmlRenderer extends Renderer, HasNullRepresentation {
    }

    /**
     * A renderer that represents a double values as a graphical progress bar.
     */
    interface ProgressBarRenderer extends Renderer {
    }

    /**
     * A renderer for presenting date values.
     */
    interface DateRenderer extends Renderer, HasNullRepresentation, HasLocale {
        /**
         * @return the pattern describing the date and time format
         */
        String getFormatString();

        /**
         * @param formatString the pattern describing the date and time format
         *                     which will be used to create {@link DateFormat} instance.
         * @see <a href="https://docs.oracle.com/javase/tutorial/i18n/format/simpleDateFormat.html">Format
         * String Syntax</a>
         */
        void setFormatString(String formatString);

        /**
         * @return the instance of {@link DateFormat} which is used to present dates
         */
        DateFormat getDateFormat();

        /**
         * @param dateFormat the instance of {@link DateFormat} with which to present dates
         */
        void setDateFormat(DateFormat dateFormat);
    }

    /**
     * A renderer for presenting LocalDate values.
     */
    interface LocalDateRenderer extends Renderer, HasNullRepresentation, HasLocale, HasDateTimeFormatter {
    }

    /**
     * A renderer for presenting LocalDateTime values.
     */
    interface LocalDateTimeRenderer extends Renderer, HasNullRepresentation, HasLocale, HasDateTimeFormatter {
    }

    /**
     * A renderer for presenting number values.
     */
    interface NumberRenderer extends Renderer, HasNullRepresentation {
        /**
         * @return the locale which is used to present numbers
         */
        Locale getLocale();

        /**
         * Sets the locale in which to present numbers.
         *
         * @param locale the locale in which to present numbers
         */
        void setLocale(Locale locale);

        /**
         * @return the format string with which is used to format the number
         */
        String getFormatString();

        /**
         * @param formatString the format string with which to format the number
         * @see <a href=
         * "http://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html#dnum">Format String Syntax</a>
         */
        void setFormatString(String formatString);

        /**
         * @return the instance of {@link NumberFormat} which is used to present numbers
         */
        NumberFormat getNumberFormat();

        /**
         * @param numberFormat the instance of {@link NumberFormat} with which to present numbers
         */
        void setNumberFormat(NumberFormat numberFormat);
    }

    /**
     * A Renderer that displays a button with a textual caption. The value of the
     * corresponding property is used as the caption. Click listeners can be added
     * to the renderer, invoked when any of the rendered buttons is clicked.
     */
    interface ButtonRenderer<T extends Entity>
            extends Renderer, HasNullRepresentation, HasRendererClickListener<T> {
    }

    /**
     * A renderer for presenting images. The value of the corresponding property
     * is used as the image location. Location can be a theme resource or URL.
     */
    interface ImageRenderer<T extends Entity> extends Renderer, HasRendererClickListener<T> {
    }

    /**
     * A renderer that represents a boolean values as a graphical check box icons.
     */
    interface CheckBoxRenderer extends Renderer {
    }

    /**
     * A renderer for UI components.
     */
    interface ComponentRenderer extends Renderer {
    }

    /**
     * A renderer that represents {@link CubaIcon}.
     */
    interface IconRenderer<T extends Entity> extends Renderer {
    }

    /**
     * Creates renderer implementation by its type.
     *
     * @param type renderer type
     * @return renderer instance with given type
     */
    <T extends Renderer> T createRenderer(Class<T> type);

    /**
     * Interface that implements conversion between a model and a presentation type.
     *
     * @param <P> the presentation type. Must be compatible with what
     *            {@link #getPresentationType()} returns.
     * @param <M> the model type. Must be compatible with what
     *            {@link #getModelType()} returns.
     * @deprecated Use {@link Column#setRenderer(Renderer, Function)}
     * and presentation provider represented by a {@link Function} instead
     */
    @Deprecated
    interface Converter<P, M> {

        /**
         * Converts the given value from target type to source type.
         *
         * @param value      the value to convert, compatible with the target type.
         *                   Can be null
         * @param targetType the requested type of the return value
         * @param locale     the locale to use for conversion. Can be null
         * @return the converted value compatible with the source type
         */
        M convertToModel(P value, Class<? extends M> targetType, Locale locale);

        /**
         * Converts the given value from source type to target type.
         *
         * @param value      the value to convert, compatible with the target type.
         *                   Can be null
         * @param targetType the requested type of the return value
         * @param locale     the locale to use for conversion. Can be null
         * @return the converted value compatible with the source type
         */
        P convertToPresentation(M value, Class<? extends P> targetType, Locale locale);

        /**
         * The source type of the converter.
         * <p>
         * Values of this type can be passed to
         * {@link #convertToPresentation(Object, Class, Locale)}.
         *
         * @return The source type
         */
        Class<M> getModelType();

        /**
         * The target type of the converter.
         * <p>
         * Values of this type can be passed to
         * {@link #convertToModel(Object, Class, Locale)}.
         *
         * @return The target type
         */
        Class<P> getPresentationType();
    }

    /**
     * An event that is fired when a column's collapsing changes.
     */
    class ColumnCollapsingChangeEvent extends AbstractDataGridEvent implements HasUserOriginated {
        protected final Column column;
        protected final boolean collapsed;
        protected final boolean userOriginated;

        /**
         * Constructor for a column visibility change event.
         *
         * @param component the DataGrid from which this event originates
         * @param column    the column that changed its visibility
         * @param collapsed {@code true} if the column was collapsed,
         *                  {@code false} if it became visible
         */
        public ColumnCollapsingChangeEvent(DataGrid component, Column column, boolean collapsed) {
            this(component, column, collapsed, false);
        }

        /**
         * Constructor for a column visibility change event.
         *
         * @param component      the DataGrid from which this event originates
         * @param column         the column that changed its visibility
         * @param collapsed      {@code true} if the column was collapsed,
         *                       {@code false} if it became visible
         * @param userOriginated {@code true} if an event is a result of user interaction,
         *                       {@code false} if from the API call
         */
        public ColumnCollapsingChangeEvent(DataGrid component, Column column, boolean collapsed, boolean userOriginated) {
            super(component);
            this.column = column;
            this.collapsed = collapsed;
            this.userOriginated = userOriginated;
        }

        /**
         * Gets the column that became hidden or visible.
         *
         * @return the column that became hidden or visible.
         * @see Column#isCollapsed()
         */
        public Column getColumn() {
            return column;
        }

        /**
         * @return {@code true} if the column was collapsed, {@code false} if it was set visible
         */
        public boolean isCollapsed() {
            return collapsed;
        }

        @Override
        public boolean isUserOriginated() {
            return userOriginated;
        }
    }

    /**
     * Registers a new column collapsing change listener.
     *
     * @param listener the listener to register
     */
    Subscription addColumnCollapsingChangeListener(Consumer<ColumnCollapsingChangeEvent> listener);

    /**
     * Removes a previously registered column collapsing change listener.
     *
     * @param listener the listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeColumnCollapsingChangeListener(Consumer<ColumnCollapsingChangeEvent> listener);

    /**
     * An event that is fired when the columns are reordered.
     */
    class ColumnReorderEvent extends AbstractDataGridEvent implements HasUserOriginated {
        protected final boolean userOriginated;

        /**
         * Constructor for a column reorder change event.
         *
         * @param component the DataGrid from which this event originates
         */
        public ColumnReorderEvent(DataGrid component) {
            this(component, false);
        }

        /**
         * Constructor for a column reorder change event.
         *
         * @param component      the DataGrid from which this event originates
         * @param userOriginated {@code true} if an event is a result of user interaction,
         *                       {@code false} if from the API call
         */
        public ColumnReorderEvent(DataGrid component, boolean userOriginated) {
            super(component);
            this.userOriginated = userOriginated;
        }

        @Override
        public boolean isUserOriginated() {
            return userOriginated;
        }
    }

    /**
     * Registers a new column reorder listener.
     *
     * @param listener the listener to register
     */
    Subscription addColumnReorderListener(Consumer<ColumnReorderEvent> listener);

    /**
     * Removes a previously registered column reorder listener.
     *
     * @param listener the listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeColumnReorderListener(Consumer<ColumnReorderEvent> listener);

    /**
     * An event that is fired when a column is resized.
     */
    class ColumnResizeEvent extends AbstractDataGridEvent implements HasUserOriginated {
        protected final Column column;
        protected final boolean userOriginated;

        /**
         * Constructor for a column resize event.
         *
         * @param component the DataGrid from which this event originates
         */
        public ColumnResizeEvent(DataGrid component, Column column) {
            this(component, column, false);
        }

        /**
         * Constructor for a column resize event.
         *
         * @param component      the DataGrid from which this event originates
         * @param userOriginated {@code true} if an event is a result of user interaction,
         *                       {@code false} if from the API call
         */
        public ColumnResizeEvent(DataGrid component, Column column, boolean userOriginated) {
            super(component);
            this.column = column;
            this.userOriginated = userOriginated;
        }

        /**
         * Returns the column that was resized.
         *
         * @return the resized column.
         */
        public Column getColumn() {
            return column;
        }

        @Override
        public boolean isUserOriginated() {
            return userOriginated;
        }
    }

    /**
     * Registers a new column resize listener.
     *
     * @param listener the listener to register
     */
    Subscription addColumnResizeListener(Consumer<ColumnResizeEvent> listener);

    /**
     * Removes a previously registered column resize listener.
     *
     * @param listener the listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeColumnResizeListener(Consumer<ColumnResizeEvent> listener);

    /**
     * Event sent when the selection changes. It specifies what in a selection has changed, and where the
     * selection took place.
     */
    class SelectionEvent<E extends Entity> extends AbstractDataGridEvent implements HasUserOriginated {
        protected final Set<E> selected;
        protected final Set<E> oldSelection;
        protected final boolean userOriginated;

        /**
         * Constructor for a selection event.
         *
         * @param component      the DataGrid from which this event originates
         * @param oldSelection   the old set of selected items
         * @param userOriginated {@code true} if an event is a result of user interaction,
         *                       {@code false} if from the API call
         */
        public SelectionEvent(DataGrid<E> component, Set<E> oldSelection, boolean userOriginated) {
            super(component);
            this.oldSelection = oldSelection;
            this.selected = component.getSelected();
            this.userOriginated = userOriginated;
        }

        /**
         * A {@link Set} of all the items that became selected.
         *
         * <em>Note:</em> this excludes all items that might have been previously
         * selected.
         *
         * @return a set of the items that became selected
         */
        public Set<E> getAdded() {
            LinkedHashSet<E> copy = new LinkedHashSet<>(getSelected());
            copy.removeAll(getOldSelection());
            return copy;
        }

        /**
         * A {@link Set} of all the items that became deselected.
         *
         * <em>Note:</em> this excludes all items that might have been previously
         * deselected.
         *
         * @return a set of the items that became deselected
         */
        public Set<E> getRemoved() {
            LinkedHashSet<E> copy = new LinkedHashSet<>(getOldSelection());
            copy.removeAll(getSelected());
            return copy;
        }

        /**
         * A {@link Set} of all the items that are currently selected.
         *
         * @return a set of the items that are currently selected
         */
        public Set<E> getSelected() {
            return selected;
        }

        /**
         * A {@link Set} of all the items that were selected before the selection was changed.
         *
         * @return a set of items selected before the selection was changed
         */
        public Set<E> getOldSelection() {
            return oldSelection;
        }

        @Override
        public boolean isUserOriginated() {
            return userOriginated;
        }
    }

    /**
     * Registers a new selection listener
     *
     * @param listener the listener to register
     */
    Subscription addSelectionListener(Consumer<SelectionEvent<E>> listener);

    /**
     * Removes a previously registered selection change listener
     *
     * @param listener the listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeSelectionListener(Consumer<SelectionEvent<E>> listener);

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
    class SortOrder implements Serializable {
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
    class SortEvent extends AbstractDataGridEvent implements HasUserOriginated {
        protected final List<SortOrder> sortOrder;
        protected final boolean userOriginated;

        /**
         * Creates a new sort order change event with a sort order list.
         *
         * @param component the DataGrid from which this event originates
         * @param sortOrder the new sort order list
         */
        public SortEvent(DataGrid component, List<SortOrder> sortOrder) {
            this(component, sortOrder, false);
        }

        /**
         * Creates a new sort order change event with a sort order list.
         *
         * @param component      the DataGrid from which this event originates
         * @param sortOrder      the new sort order list
         * @param userOriginated {@code true} if an event is a result of user interaction,
         *                       {@code false} if from the API call
         */
        public SortEvent(DataGrid component, List<SortOrder> sortOrder, boolean userOriginated) {
            super(component);
            this.sortOrder = sortOrder;
            this.userOriginated = userOriginated;
        }

        /**
         * @return the sort order list
         */
        public List<SortOrder> getSortOrder() {
            return sortOrder;
        }

        @Override
        public boolean isUserOriginated() {
            return userOriginated;
        }
    }

    /**
     * Registers a new sort order change listener
     *
     * @param listener the listener to register
     */
    Subscription addSortListener(Consumer<SortEvent> listener);

    /**
     * Removes a previously registered sort order change listener
     *
     * @param listener the listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeSortListener(Consumer<SortEvent> listener);

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
    Subscription addContextClickListener(Consumer<ContextClickEvent> listener);

    /**
     * Removes a previously registered context click listener
     *
     * @param listener the listener to remove
     */
    void removeContextClickListener(Consumer<ContextClickEvent> listener);

    /**
     * Click event fired by a {@link DataGrid}
     */
    class ItemClickEvent<E> extends DataGridClickEvent {
        protected E item;
        protected Object itemId;
        protected String columnId;

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
    Subscription addItemClickListener(Consumer<ItemClickEvent<E>> listener);

    /**
     * Removes a previously registered item click listener
     *
     * @param listener the listener to remove
     */
    void removeItemClickListener(Consumer<ItemClickEvent<E>> listener);

    /**
     * Class for holding information about a mouse click event. A {@link DataGridClickEvent} is fired when the user
     * clicks on a {@code Component}.
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
         * component or -1 if no x coordinate available
         */
        public int getRelativeX() {
            return details.getRelativeX();
        }

        /**
         * Returns the relative mouse position (y coordinate) when the click
         * took place. The position is relative to the clicked component.
         *
         * @return The mouse cursor y position relative to the clicked layout
         * component or -1 if no y coordinate available
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
     * Base interface for DataGrid header and footer rows.
     *
     * @param <T> the type of the cells in the row
     */
    interface StaticRow<T extends StaticCell> {
        /**
         * Returns the custom style name for this row.
         *
         * @return the style name or null if no style name has been set
         */
        String getStyleName();

        /**
         * Sets a custom style name for this row.
         *
         * @param styleName the style name to set or
         *                  null to not use any style name
         */
        void setStyleName(String styleName);

        /**
         * Merges columns cells in a row.
         *
         * @param columnIds the ids of columns to merge
         * @return the remaining visible cell after the merge
         */
        T join(String... columnIds);

        /**
         * Returns the cell for the given column id on this row. If the
         * column is merged returned cell is the cell for the whole group.
         *
         * @param columnId column id
         * @return the cell for the given column id,
         * merged cell for merged properties,
         * null if not found
         */
        T getCell(String columnId);
    }

    /**
     * Base interface for DataGrid header or footer cells.
     */
    interface StaticCell {

        /**
         * Returns the custom style name for this cell.
         *
         * @return the style name or null if no style name has been set
         */
        String getStyleName();

        /**
         * Sets a custom style name for this cell.
         *
         * @param styleName the style name to set or null to not use any style name
         */
        void setStyleName(String styleName);

        /**
         * Returns the type of content stored in this cell.
         *
         * @return cell content type
         */
        DataGridStaticCellType getCellType();

        /**
         * Returns the component displayed in this cell.
         *
         * @return the component
         */
        Component getComponent();

        /**
         * Sets the component displayed in this cell.
         *
         * @param component the component to set
         */
        void setComponent(Component component);

        /**
         * Returns the HTML content displayed in this cell.
         *
         * @return the html
         */
        String getHtml();

        /**
         * Sets the HTML content displayed in this cell.
         *
         * @param html the html to set
         */
        void setHtml(String html);

        /**
         * Returns the text displayed in this cell.
         *
         * @return the plain text caption
         */
        String getText();

        /**
         * Sets the text displayed in this cell.
         *
         * @param text a plain text caption
         */
        void setText(String text);

        /**
         * Gets the row where this cell is.
         *
         * @return row for this cell
         */
        StaticRow<?> getRow();
    }

    /**
     * Enumeration, specifying the content type of a Cell in a DataGrid header or footer.
     */
    enum DataGridStaticCellType {
        /**
         * Text content
         */
        TEXT,

        /**
         * HTML content
         */
        HTML,

        /**
         * Component content
         */
        COMPONENT
    }

    /**
     * Represents a header row in DataGrid.
     */
    interface HeaderRow extends StaticRow<HeaderCell> {
    }

    /**
     * Represents a header cell in DataGrid.
     * Can be a merged cell for multiple columns.
     */
    interface HeaderCell extends StaticCell {
    }

    /**
     * Gets the header row at given index.
     *
     * @param index 0 based index for row. Counted from top to bottom
     * @return header row at given index
     */
    HeaderRow getHeaderRow(int index);

    /**
     * Adds a new row at the bottom of the header section.
     *
     * @return the new row
     * @see #prependHeaderRow()
     * @see #addHeaderRowAt(int)
     * @see #removeHeaderRow(HeaderRow)
     * @see #removeHeaderRow(int)
     */
    HeaderRow appendHeaderRow();

    /**
     * Adds a new row at the top of the header section.
     *
     * @return the new row
     * @see #appendHeaderRow()
     * @see #addHeaderRowAt(int)
     * @see #removeHeaderRow(HeaderRow)
     * @see #removeHeaderRow(int)
     */
    HeaderRow prependHeaderRow();

    /**
     * Inserts a new row at the given position to the header section. Shifts the
     * row currently at that position and any subsequent rows down (adds one to
     * their indices).
     *
     * @param index the position at which to insert the row
     * @return the new row
     * @see #appendHeaderRow()
     * @see #prependHeaderRow()
     * @see #removeHeaderRow(HeaderRow)
     * @see #removeHeaderRow(int)
     */
    HeaderRow addHeaderRowAt(int index);

    /**
     * Removes the given row from the header section.
     *
     * @param headerRow the row to be removed
     * @see #removeHeaderRow(int)
     * @see #addHeaderRowAt(int)
     * @see #appendHeaderRow()
     * @see #prependHeaderRow()
     */
    void removeHeaderRow(HeaderRow headerRow);

    /**
     * Removes the row at the given position from the header section.
     *
     * @param index the position of the row
     * @see #removeHeaderRow(HeaderRow)
     * @see #addHeaderRowAt(int)
     * @see #appendHeaderRow()
     * @see #prependHeaderRow()
     */
    void removeHeaderRow(int index);

    /**
     * Returns the current default row of the header section. The default row is
     * a special header row providing a user interface for sorting columns.
     * Setting a header text for column updates cells in the default header.
     *
     * @return the default row or null if no default row set
     */
    HeaderRow getDefaultHeaderRow();

    /**
     * Sets the default row of the header. The default row is a special header
     * row providing a user interface for sorting columns.
     *
     * @param headerRow the new default row, or null for no default row
     */
    void setDefaultHeaderRow(HeaderRow headerRow);

    /**
     * Gets the row count for the header section.
     *
     * @return row count
     */
    int getHeaderRowCount();

    /**
     * Represents a footer row in DataGrid.
     */
    interface FooterRow extends StaticRow<FooterCell> {
    }

    /**
     * Represents a footer cell in DataGrid.
     * Can be a merged cell for multiple columns.
     */
    interface FooterCell extends StaticCell {
    }

    /**
     * Gets the footer row at given index.
     *
     * @param index 0 based index for row. Counted from top to bottom
     * @return footer row at given index
     */
    FooterRow getFooterRow(int index);

    /**
     * Adds a new row at the bottom of the footer section.
     *
     * @return the new row
     * @see #prependFooterRow()
     * @see #addFooterRowAt(int)
     * @see #removeFooterRow(FooterRow)
     * @see #removeFooterRow(int)
     */
    FooterRow appendFooterRow();

    /**
     * Adds a new row at the top of the footer section.
     *
     * @return the new row
     * @see #appendFooterRow()
     * @see #addFooterRowAt(int)
     * @see #removeFooterRow(FooterRow)
     * @see #removeFooterRow(int)
     */
    FooterRow prependFooterRow();

    /**
     * Inserts a new row at the given position to the footer section. Shifts the
     * row currently at that position and any subsequent rows down (adds one to
     * their indices).
     *
     * @param index the position at which to insert the row
     * @return the new row
     * @see #appendFooterRow()
     * @see #prependFooterRow()
     * @see #removeFooterRow(FooterRow)
     * @see #removeFooterRow(int)
     */
    FooterRow addFooterRowAt(int index);

    /**
     * Removes the given row from the footer section.
     *
     * @param footerRow the row to be removed
     * @see #removeFooterRow(int)
     * @see #addFooterRowAt(int)
     * @see #appendFooterRow()
     * @see #prependFooterRow()
     */
    void removeFooterRow(FooterRow footerRow);

    /**
     * Removes the row at the given position from the footer section.
     *
     * @param index the position of the row
     * @see #removeFooterRow(FooterRow)
     * @see #addFooterRowAt(int)
     * @see #appendFooterRow()
     * @see #prependFooterRow()
     */
    void removeFooterRow(int index);

    /**
     * Gets the row count for the footer.
     *
     * @return row count
     */
    int getFooterRowCount();

    @Nullable
    @Override
    default Object getSubPart(String name) {
        Column<E> column = getColumn(name);
        if (column != null) {
            return column;
        }

        return getAction(name);
    }

    /**
     * @return true if DataGrid is aggregatable
     */
    boolean isAggregatable();

    /**
     * Set to true if aggregation should be enabled. Default value is false.
     *
     * @param aggregatable aggregatable option
     */
    void setAggregatable(boolean aggregatable);

    /**
     * @return return aggregation row position
     */
    AggregationPosition getAggregationPosition();

    /**
     * Sets aggregation row position. Default value is {@link AggregationPosition#TOP}.
     *
     * @param position position: {@link AggregationPosition#TOP} or {@link AggregationPosition#BOTTOM}
     */
    void setAggregationPosition(AggregationPosition position);

    /**
     * @return aggregated values for columns
     */
    Map<String, Object> getAggregationResults();

    /**
     * Sets a message to the middle of DataGrid body that should be appeared when DataGrid is empty.
     *
     * @param message message that appears when DataGrid is empty
     */
    void setEmptyStateMessage(String message);

    /**
     * @return message that should be appeared when DataGrid is empty
     */
    String getEmptyStateMessage();

    /**
     * Sets a link message to the middle of DataGrid body that should be appeared when DataGrid is empty.
     *
     * @param linkMessage message that appears when DataGrid is empty
     * @see #setEmptyStateLinkClickHandler(Consumer)
     */
    void setEmptyStateLinkMessage(String linkMessage);

    /**
     * @return link message that should be appeared when DataGrid is empty
     */
    String getEmptyStateLinkMessage();

    /**
     * Sets click handler for link message. Link message can be shown when DataGrid is empty.
     *
     * @param handler handler to set
     * @see #setEmptyStateLinkMessage(String)
     */
    void setEmptyStateLinkClickHandler(Consumer<EmptyStateClickEvent<E>> handler);

    /**
     * @return click handler for link message
     */
    Consumer<EmptyStateClickEvent<E>> getEmptyStateLinkClickHandler();

    /**
     * A column in the DataGrid.
     */
    interface Column<E extends Entity> extends HasFormatter, Serializable {

        /**
         * @return id of a column
         */
        String getId();

        /**
         * @return the instance of {@link MetaPropertyPath} representing a relative path
         * to a property from certain MetaClass
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
         * {@link #setCollapsingToggleCaption(String)}.
         *
         * @param caption the text to show in the caption
         */
        void setCaption(String caption);

        /**
         * @return the caption for the hiding toggle for this column
         */
        String getCollapsingToggleCaption();

        /**
         * Sets the caption of the hiding toggle for this column. Shown in the
         * toggle for this column in the DataGrid's sidebar when the column is
         * {@link #isCollapsible() hidable}.
         * <p>
         * The default value is <code>null</code>, and in that case the column's
         * {@link #getCaption() header caption} is used.
         *
         * <em>NOTE:</em> setting this to empty string might cause the hiding
         * toggle to not render correctly.
         *
         * @param collapsingToggleCaption the text to show in the column hiding toggle
         */
        void setCollapsingToggleCaption(String collapsingToggleCaption);

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
         *
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
         * @return {@code false} if the column is currently hidden by security permissions,
         * {@code true} otherwise
         */
        boolean isVisible();

        /**
         * Hides or shows the column according to security permissions.
         * Invisible column doesn't send any data to client side.
         *
         * @param visible {@code false} to hide the column, {@code true} to show
         */
        void setVisible(boolean visible);

        /**
         * @return {@code true} if the column is currently hidden, {@code false} otherwise
         */
        boolean isCollapsed();

        /**
         * Hides or shows the column. By default columns are visible before
         * explicitly hiding them.
         *
         * @param collapsed {@code true} to hide the column, {@code false} to show
         */
        void setCollapsed(boolean collapsed);

        /**
         * Returns whether this column can be hidden by the user. Default is {@code true}.
         *
         * <em>Note:</em> the column can be programmatically hidden using
         * {@link #setCollapsed(boolean)} regardless of the returned value.
         *
         * @return {@code true} if the user can hide the column, {@code false} if not
         * @see DataGrid#isColumnsCollapsingAllowed()
         * @see DataGrid#setColumnsCollapsingAllowed(boolean)
         */
        boolean isCollapsible();

        /**
         * Sets whether this column can be hidden by the user. Hidable columns
         * can be hidden and shown via the sidebar menu.
         *
         * @param collapsible {@code true} if the column may be hidden by the user via UI interaction
         * @see DataGrid#isColumnsCollapsingAllowed()
         * @see DataGrid#setColumnsCollapsingAllowed(boolean)
         */
        void setCollapsible(boolean collapsible);

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
         *
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
         * Returns the renderer instance used by this column.
         *
         * @return the renderer
         */
        Renderer getRenderer();

        /**
         * Sets the renderer for this column.
         * If given renderer is null, then the default renderer will be used.
         *
         * @param renderer the renderer to use
         * @see #setRenderer(Renderer, Function)
         */
        void setRenderer(Renderer renderer);

        /**
         * Sets the renderer for this column. If given renderer is null, then
         * the default renderer will be used.
         * <p>
         * The presentation provider is a {@link Function} that takes the value of this
         * column on a single row, and converts that to a value that the renderer accepts.
         * <p>
         * The presentation provider takes precedence over {@link Converter} and {@link Function formatter}.
         *
         * @param renderer             the renderer to use
         * @param presentationProvider the presentation provider to use
         * @see #setRenderer(Renderer)
         */
        void setRenderer(Renderer renderer, Function presentationProvider);

        /**
         * @return a function to get presentations from the value of this column
         */
        Function getPresentationProvider();

        /**
         * @deprecated use {@link #getPresentationProvider()} instead
         */
        @Deprecated
        @Override
        Function getFormatter();

        /**
         * If either {@link Function presentation provider} or {@link Converter}
         * are set they take precedence over {@link Function formatter}.
         *
         * @deprecated use {@link #getPresentationProvider()} instead
         */
        @Deprecated
        @Override
        void setFormatter(Function formatter);

        /**
         * Returns the converter instance used by this column.
         *
         * @return the converter
         * @deprecated use {@link #getPresentationProvider()} instead
         */
        @Deprecated
        Converter<?, ?> getConverter();

        /**
         * Sets the converter used to convert from the property value type to
         * the renderer presentation type. If given converter is null, then the
         * default converter will be used.
         * <p>
         * Takes precedence over {@link Function formatter}, but is inferior to the {@link Function presentation provider}.
         *
         * @param converter the converter to use, or {@code null} to not use any
         *                  converters
         * @deprecated use {@link #setRenderer(Renderer, Function)} instead
         */
        @Deprecated
        void setConverter(Converter<?, ?> converter);

        /**
         * @return the type of value represented by this column
         */
        Class getType();

        /**
         * INTERNAL
         * <p>
         * Sets a type of generated column.
         *
         * @param generatedType generated column type
         */
        void setGeneratedType(Class generatedType);

        /**
         * INTERNAL.
         *
         * @return generated column type
         */
        Class getGeneratedType();

        /**
         * Returns whether the properties corresponding to this column should be
         * editable when the item editor is active.
         *
         * @return {@code true} if this column is editable, {@code false} otherwise
         * @see DataGrid#edit(Entity)
         * @see #setEditable(boolean)
         */
        boolean isEditable();

        /**
         * Sets whether the properties corresponding to this column should be
         * editable when the item editor is active. By default columns are
         * editable.
         * <p>
         * Values in non-editable columns are currently not displayed when the
         * editor is active, but this will probably change in the future. They
         * are not automatically assigned an editor field and, if one is
         * manually assigned, it is not used. Columns that cannot (or should
         * not) be edited even in principle should be set non-editable.
         *
         * @param editable {@code true} if this column should be editable, {@code false} otherwise
         * @see DataGrid#edit(Entity)
         * @see DataGrid#isEditorActive()
         */
        void setEditable(boolean editable);

        /**
         * @return field generator that generates component for
         * this column in {@link DataGrid} editor.
         */
        @Deprecated
        ColumnEditorFieldGenerator getEditorFieldGenerator();

        /**
         * @param fieldFactory field generator that generates a component
         *                     for this column in {@link DataGrid} editor.
         * @deprecated Use {{@link #setEditFieldGenerator(Function)}} instead
         */
        @Deprecated
        void setEditorFieldGenerator(ColumnEditorFieldGenerator fieldFactory);

        /**
         * @return field generator that generates a component
         * for this column in {@link DataGrid} editor
         */
        Function<EditorFieldGenerationContext<E>, Field<?>> getEditFieldGenerator();

        /**
         * @param generator field generator that generates a component
         *                  for this column in {@link DataGrid} editor.
         */
        void setEditFieldGenerator(Function<EditorFieldGenerationContext<E>, Field<?>> generator);

        /**
         * @return the style provider that is used for generating styles for cells
         */
        Function<E, String> getStyleProvider();

        /**
         * Sets the style provider for the DataGrid column.
         *
         * @param styleProvider a style provider to set
         */
        void setStyleProvider(Function<? super E, String> styleProvider);

        /**
         * @return the description provider that is used for generating
         * descriptions for cells in this column
         */
        Function<E, String> getDescriptionProvider();

        /**
         * Sets the description provider that is used for generating
         * descriptions for cells in this column.
         * <p>
         * This method uses the {@link ContentMode#PREFORMATTED} content mode.
         * <p>
         * It has priority over {@link DataGrid#setCellDescriptionProvider(CellDescriptionProvider)}.
         *
         * @param descriptionProvider a description provider to set,
         *                            or {@code null} to remove a previously set generator
         */
        void setDescriptionProvider(Function<? super E, String> descriptionProvider);

        /**
         * Sets the description provider that is used for generating
         * descriptions for cells in this column.
         * <p>
         * It has priority over {@link DataGrid#setCellDescriptionProvider(CellDescriptionProvider)}.
         *
         * @param descriptionProvider a description provider to set,
         *                            or {@code null} to remove a previously set generator
         * @param contentMode         a content mode for row tooltips
         */
        void setDescriptionProvider(Function<? super E, String> descriptionProvider, ContentMode contentMode);

        /**
         * @return The DataGrid this column belongs to
         */
        DataGrid<E> getOwner();

        /**
         * @param owner The DataGrid this column belongs to
         */
        void setOwner(DataGrid<E> owner);

        /**
         * INTERNAL
         * <p>
         * Intended to install declarative {@code ColumnGenerator} instance.
         *
         * @param columnGenerator column generator instance
         */
        default void setColumnGenerator(GenericColumnGenerator<E, Object> columnGenerator) {
            getOwner().addGeneratedColumn(getId(), columnGenerator);
        }

        /**
         * @return aggregation info
         * @see DataGrid#setAggregatable(boolean)
         */
        AggregationInfo getAggregation();

        /**
         * Sets an aggregation info in order to perform aggregation for this column.
         *
         * @param info aggregation info
         * @see DataGrid#setAggregatable(boolean)
         */
        void setAggregation(AggregationInfo info);

        /**
         * @return value description for aggregation row cells
         * @see DataGrid#setAggregatable(boolean)
         */
        String getValueDescription();

        /**
         * Sets value description for aggregated row cells.
         *
         * @param valueDescription value description
         * @see DataGrid#setAggregatable(boolean)
         */
        void setValueDescription(String valueDescription);
    }

    /**
     * Describes empty state link click event.
     *
     * @param <E> entity class
     * @see #setEmptyStateLinkMessage(String)
     * @see #setEmptyStateLinkClickHandler(Consumer)
     */
    class EmptyStateClickEvent<E extends Entity> extends EventObject {

        public EmptyStateClickEvent(DataGrid<E> source) {
            super(source);
        }

        @SuppressWarnings("unchecked")
        @Override
        public DataGrid<E> getSource() {
            return (DataGrid<E>) super.getSource();
        }
    }

    /**
     * Defines the position of aggregation row.
     */
    enum AggregationPosition {
        TOP,
        BOTTOM
    }
}