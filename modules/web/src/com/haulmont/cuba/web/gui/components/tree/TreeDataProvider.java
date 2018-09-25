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
import com.haulmont.cuba.gui.components.data.TreeSource;
import com.haulmont.cuba.web.widgets.tree.EnhancedTreeDataProvider;
import com.vaadin.data.provider.*;
import com.vaadin.server.SerializablePredicate;

import java.util.stream.Stream;

public class TreeDataProvider<T> extends AbstractDataProvider<T, SerializablePredicate<T>>
        implements HierarchicalDataProvider<T, SerializablePredicate<T>>, EnhancedTreeDataProvider<T> {

    protected TreeSource<T> treeSource;
    protected TreeSourceEventsDelegate<T> eventsDelegate;

    protected Subscription itemSetChangeSubscription;
    protected Subscription valueChangeSubscription;
    protected Subscription stateChangeSubscription;
    protected Subscription selectedItemChangeSubscription;

    public TreeDataProvider(TreeSource<T> treeSource,
                            TreeSourceEventsDelegate<T> eventsDelegate) {
        this.treeSource = treeSource;
        this.eventsDelegate = eventsDelegate;

        this.itemSetChangeSubscription =
                this.treeSource.addItemSetChangeListener(this::datasourceItemSetChanged);
        this.valueChangeSubscription =
                this.treeSource.addValueChangeListener(this::datasourceValueChanged);
        this.stateChangeSubscription =
                this.treeSource.addStateChangeListener(this::datasourceStateChanged);
        this.selectedItemChangeSubscription =
                this.treeSource.addSelectedItemChangeListener(this::datasourceSelectedItemChanged);
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

    public TreeSource<T> getTreeSource() {
        return treeSource;
    }

    @Override
    public Object getId(T item) {
        return treeSource.getItemId(item);
    }

    @Override
    public boolean isInMemory() {
        return true;
    }

    @Override
    public int size(Query<T, SerializablePredicate<T>> query) {
        // FIXME: gg, query?
        if (treeSource.getState() == BindingState.INACTIVE) {
            return 0;
        }

        return treeSource.size();
    }

    @Override
    public int getChildCount(HierarchicalQuery<T, SerializablePredicate<T>> query) {
        if (treeSource.getState() == BindingState.INACTIVE) {
            return 0;
        }

        return treeSource.getChildCount(query.getParent());
    }

    @Override
    public Stream<T> fetchChildren(HierarchicalQuery<T, SerializablePredicate<T>> query) {
        if (treeSource.getState() == BindingState.INACTIVE) {
            return Stream.empty();
        }

        return treeSource.getChildren(query.getParent())
                .skip(query.getOffset())
                .limit(query.getLimit());
    }

    @Override
    public boolean hasChildren(T item) {
        return treeSource.hasChildren(item);
    }

    @Override
    public Stream<T> getItems() {
        return treeSource.getItems();
    }

    @Override
    public T getParent(T item) {
        if (treeSource.getState() == BindingState.INACTIVE) {
            return null;
        }

        return treeSource.getParent(item);
    }

    protected void datasourceItemSetChanged(TreeSource.ItemSetChangeEvent<T> event) {
        fireEvent(new DataChangeEvent<>(this));

        eventsDelegate.treeSourceItemSetChanged(event);
    }

    protected void datasourceValueChanged(TreeSource.ValueChangeEvent<T> event) {
        fireEvent(new DataChangeEvent.DataRefreshEvent<>(this, event.getItem()));

        eventsDelegate.treeSourcePropertyValueChanged(event);
    }

    protected void datasourceStateChanged(TreeSource.StateChangeEvent<T> event) {
        eventsDelegate.treeSourceStateChanged(event);
    }

    protected void datasourceSelectedItemChanged(TreeSource.SelectedItemChangeEvent<T> event) {
        eventsDelegate.treeSourceSelectedItemChanged(event);
    }
}
