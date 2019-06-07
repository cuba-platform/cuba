/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.gui.components.data.datagrid;

import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.DataGridItems;
import com.haulmont.cuba.gui.components.data.meta.EntityDataGridItems;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class EmptyDataGridItems<E extends Entity>
        implements EntityDataGridItems<E>, DataGridItems.Sortable<E> {

    protected MetaClass metaClass;

    public EmptyDataGridItems(MetaClass metaClass) {
        this.metaClass = metaClass;
    }

    @Override
    public Object getItemId(E item) {
        return null;
    }

    @Override
    public E getItem(@Nullable Object itemId) {
        return null;
    }

    @Override
    public int indexOfItem(E item) {
        return 0;
    }

    @Nullable
    @Override
    public E getItemByIndex(int index) {
        return null;
    }

    @Override
    public Stream<E> getItems() {
        return Stream.empty();
    }

    @Override
    public List<E> getItems(int startIndex, int numberOfItems) {
        return Collections.emptyList();
    }

    @Override
    public boolean containsItem(E item) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Nullable
    @Override
    public E getSelectedItem() {
        return null;
    }

    @Override
    public void setSelectedItem(@Nullable E item) {
        // do nothing
    }

    @Override
    public Subscription addValueChangeListener(Consumer<ValueChangeEvent<E>> listener) {
        return null;
    }

    @Override
    public Subscription addItemSetChangeListener(Consumer<ItemSetChangeEvent<E>> listener) {
        return null;
    }

    @Override
    public Subscription addSelectedItemChangeListener(Consumer<SelectedItemChangeEvent<E>> listener) {
        return null;
    }

    @Override
    public MetaClass getEntityMetaClass() {
        return metaClass;
    }

    @Override
    public BindingState getState() {
        return BindingState.ACTIVE;
    }

    @Override
    public Subscription addStateChangeListener(Consumer<StateChangeEvent> listener) {
        return null;
    }

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
        // do nothing
    }

    @Override
    public void resetSortOrder() {
        // do nothing
    }
}
