/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.web.gui.components.tree;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.TreeItems;
import com.haulmont.cuba.web.widgets.tree.EnhancedTreeDataProvider;
import com.vaadin.data.provider.*;
import com.vaadin.server.SerializablePredicate;

import java.util.stream.Stream;

public class TreeDataProvider<T> extends AbstractDataProvider<T, SerializablePredicate<T>>
        implements HierarchicalDataProvider<T, SerializablePredicate<T>>, EnhancedTreeDataProvider<T> {

    protected TreeItems<T> treeItems;
    protected TreeSourceEventsDelegate<T> eventsDelegate;

    protected Subscription itemSetChangeSubscription;
    protected Subscription valueChangeSubscription;
    protected Subscription stateChangeSubscription;
    protected Subscription selectedItemChangeSubscription;

    public TreeDataProvider(TreeItems<T> treeItems,
                            TreeSourceEventsDelegate<T> eventsDelegate) {
        this.treeItems = treeItems;
        this.eventsDelegate = eventsDelegate;

        this.itemSetChangeSubscription =
                this.treeItems.addItemSetChangeListener(this::datasourceItemSetChanged);
        this.valueChangeSubscription =
                this.treeItems.addValueChangeListener(this::datasourceValueChanged);
        this.stateChangeSubscription =
                this.treeItems.addStateChangeListener(this::datasourceStateChanged);
        this.selectedItemChangeSubscription =
                this.treeItems.addSelectedItemChangeListener(this::datasourceSelectedItemChanged);
    }

    public void unbind() {
        if (itemSetChangeSubscription != null) {
            this.itemSetChangeSubscription.remove();
            this.itemSetChangeSubscription = null;
        }

        if (valueChangeSubscription != null) {
            this.valueChangeSubscription.remove();
            this.valueChangeSubscription = null;
        }

        if (stateChangeSubscription != null) {
            this.stateChangeSubscription.remove();
            this.stateChangeSubscription = null;
        }

        if (selectedItemChangeSubscription != null) {
            this.selectedItemChangeSubscription.remove();
            this.selectedItemChangeSubscription = null;
        }
    }

    public TreeItems<T> getTreeItems() {
        return treeItems;
    }

    @Override
    public Object getId(T item) {
        return treeItems.getItemId(item);
    }

    @Override
    public boolean isInMemory() {
        return true;
    }

    @Override
    public int size(Query<T, SerializablePredicate<T>> query) {
        // FIXME: gg, query?
        if (treeItems.getState() == BindingState.INACTIVE) {
            return 0;
        }

        return treeItems.size();
    }

    @Override
    public int getChildCount(HierarchicalQuery<T, SerializablePredicate<T>> query) {
        if (treeItems.getState() == BindingState.INACTIVE) {
            return 0;
        }

        return treeItems.getChildCount(query.getParent());
    }

    @Override
    public Stream<T> fetchChildren(HierarchicalQuery<T, SerializablePredicate<T>> query) {
        if (treeItems.getState() == BindingState.INACTIVE) {
            return Stream.empty();
        }

        return treeItems.getChildren(query.getParent())
                .skip(query.getOffset())
                .limit(query.getLimit());
    }

    @Override
    public boolean hasChildren(T item) {
        return treeItems.hasChildren(item);
    }

    @Override
    public Stream<T> getItems() {
        return treeItems.getItems();
    }

    @Override
    public T getParent(T item) {
        if (treeItems.getState() == BindingState.INACTIVE) {
            return null;
        }

        return treeItems.getParent(item);
    }

    protected void datasourceItemSetChanged(TreeItems.ItemSetChangeEvent<T> event) {
        fireEvent(new DataChangeEvent<>(this));

        eventsDelegate.treeSourceItemSetChanged(event);
    }

    protected void datasourceValueChanged(TreeItems.ValueChangeEvent<T> event) {
        fireEvent(new DataChangeEvent.DataRefreshEvent<>(this, event.getItem()));

        eventsDelegate.treeSourcePropertyValueChanged(event);
    }

    protected void datasourceStateChanged(TreeItems.StateChangeEvent event) {
        eventsDelegate.treeSourceStateChanged(event);
    }

    protected void datasourceSelectedItemChanged(TreeItems.SelectedItemChangeEvent<T> event) {
        eventsDelegate.treeSourceSelectedItemChanged(event);
    }
}
