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

package com.haulmont.cuba.gui.components.data.table;

import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.TableItems;
import com.haulmont.cuba.gui.components.data.meta.EntityTableItems;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

public class EmptyTableItems<E extends Entity> implements EntityTableItems<E>, TableItems.Sortable<E> {

    protected MetaClass metaClass;

    public EmptyTableItems(MetaClass metaClass) {
        this.metaClass = metaClass;
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
    public Collection<?> getItemIds() {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public E getItem(Object itemId) {
        return null;
    }

    @Override
    public Object getItemValue(Object itemId, Object propertyId) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean containsId(Object itemId) {
        return false;
    }

    @Override
    public Class<?> getType(Object propertyId) {
        return null;
    }

    @Override
    public boolean supportsProperty(Object propertyId) {
        return false;
    }

    @Override
    public Collection<E> getItems() {
        return Collections.emptyList();
    }

    @Override
    public void updateItem(E item) {
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
    public Object nextItemId(Object itemId) {
        return null;
    }

    @Override
    public Object prevItemId(Object itemId) {
        return null;
    }

    @Override
    public Object firstItemId() {
        return null;
    }

    @Override
    public Object lastItemId() {
        return null;
    }

    @Override
    public boolean isFirstId(Object itemId) {
        return false;
    }

    @Override
    public boolean isLastId(Object itemId) {
        return false;
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
