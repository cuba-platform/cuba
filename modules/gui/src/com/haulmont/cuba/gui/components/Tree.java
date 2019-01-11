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

import com.google.common.reflect.TypeToken;
import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.TreeItems;
import com.haulmont.cuba.gui.components.data.tree.DatasourceTreeItems;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;

import javax.annotation.Nullable;
import java.util.EventObject;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Tree<E extends Entity> extends ListComponent<E>, HasButtonsPanel,
                                                Component.HasCaption, Component.HasIcon, LookupComponent<E>,
                                                Component.Focusable, HasContextHelp, HasItemCaptionProvider<E>,
                                                HasHtmlCaption, HasHtmlDescription {

    String NAME = "tree";

    static <T extends Entity> TypeToken<Tree<T>> of(Class<T> itemClass) {
        return new TypeToken<Tree<T>>() {};
    }

    void expandTree();

    /**
     * @param itemId the id of item to expand
     * @deprecated Use {@link #expand(Entity)} instead
     */
    @Deprecated
    void expand(Object itemId);

    void expand(E item);

    void collapseTree();

    /**
     * @param itemId the id of item to collapse
     * @deprecated Use {@link #collapse(Entity)} instead
     */
    @Deprecated
    void collapse(Object itemId);

    void collapse(E item);

    /**
     * Expand tree including specified level
     *
     * @param level level of Tree nodes to expand, if passed level = 1 then root items will be expanded
     * @throws IllegalArgumentException if level &lt; 1
     */
    void expandUpTo(int level);

    boolean isExpanded(Object itemId);

    String getHierarchyProperty();

    @Deprecated
    default void setDatasource(HierarchicalDatasource datasource) {
        //noinspection unchecked
        setItems(datasource != null
                ? new DatasourceTreeItems(datasource)
                : null);
    }

    @Deprecated
    @Override
    default HierarchicalDatasource getDatasource() {
        TreeItems<E> treeItems = getItems();
        return treeItems != null
                ? ((DatasourceTreeItems) treeItems).getDatasource()
                : null;
    }

    @Override
    TreeItems<E> getItems();
    void setItems(TreeItems<E> treeItems);

    /**
     * Assign action to be executed on double click inside a tree node.
     */
    void setItemClickAction(Action action);
    Action getItemClickAction();

    /**
     * Sets a single style provider for tree items.
     *
     * @param styleProvider a style provider to set
     */
    void setStyleProvider(@Nullable Function<? super E, String> styleProvider);

    /**
     * Add a style provider for tree items.
     *
     * @param styleProvider a style provider to add
     */
    void addStyleProvider(Function<? super E, String> styleProvider);

    /**
     * Removes a previously added style provider.
     *
     * @param styleProvider a style provider to remove
     */
    void removeStyleProvider(Function<? super E, String> styleProvider);

    /**
     * Allows to define different styles for tree items.
     */
    @Deprecated
    interface StyleProvider<E extends Entity> extends Function<E, String> {
        @Override
        default String apply(E entity) {
            return getStyleName(entity);
        }

        /**
         * Called by {@link Tree} to get a style for item. <br>
         * All unhandled exceptions from StyleProvider in Web components by default are logged with ERROR level
         * and not shown to users.
         *
         * @param entity an entity instance represented by the current item
         * @return style name or null to apply the default
         */
        String getStyleName(E entity);
    }

    /**
     * Set the icon provider for the tree.
     */
    void setIconProvider(Function<? super E, String> iconProvider);

    /**
     * Repaint UI representation of the tree including style providers and icon providers without refreshing the tree data.
     */
    void repaint();

    /**
     * Set action to be executed on Enter key press.
     */
    void setEnterPressAction(Action action);
    /**
     * @return Enter key press action.
     */
    Action getEnterPressAction();

    /**
     * @param multiselect {@code true} for multiselect, {@code false} otherwise
     * @deprecated Use {@link #setSelectionMode(SelectionMode)} instead
     */
    @Deprecated
    void setMultiSelect(boolean multiselect);

    /**
     * @deprecated refresh datasource instead
     */
    @Deprecated
    void refresh();

    /**
     * @return the currently used {@link SelectionMode}
     */
    SelectionMode getSelectionMode();

    /**
     * Sets the Tree's selection mode.
     *
     * @param selectionMode the selection mode to use
     */
    void setSelectionMode(SelectionMode selectionMode);

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
         * A SelectionMode that does not allow for rows to be selected.
         */
        NONE
    }

    /**
     * Event sent when the selection changes. It specifies what in a selection has changed, and where the
     * selection took place.
     */
    class SelectionEvent<E extends Entity> extends EventObject implements HasUserOriginated {
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
        public SelectionEvent(Tree<E> component, Set<E> oldSelection, boolean userOriginated) {
            super(component);
            this.oldSelection = oldSelection;
            this.selected = component.getSelected();
            this.userOriginated = userOriginated;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Tree<E> getSource() {
            return (Tree<E>) super.getSource();
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
}