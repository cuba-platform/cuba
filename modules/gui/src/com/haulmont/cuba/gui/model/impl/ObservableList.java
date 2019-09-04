/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.gui.model.impl;

import com.google.common.collect.ForwardingList;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.model.CollectionChangeType;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressWarnings("NullableProblems")
public class ObservableList<T> extends ForwardingList<T> implements Serializable {

    private static final long serialVersionUID = -1887633822578545041L;

    private List<T> delegate;
    private transient BiConsumer<CollectionChangeType, Collection<? extends T>> onCollectionChanged;
    private Map<Object, Integer> idMap;
    private Consumer<T> onRemoveItem;
    private Consumer<T> onAddItem;

    public ObservableList(List<T> delegate, BiConsumer<CollectionChangeType, Collection<? extends T>> onCollectionChanged) {
        this.delegate = delegate;
        this.onCollectionChanged = onCollectionChanged;
    }

    public ObservableList(List<T> delegate, Map<Object, Integer> idMap,
                          BiConsumer<CollectionChangeType, Collection<? extends T>> onCollectionChanged) {
        this(delegate, onCollectionChanged);
        this.idMap = idMap;
    }

    public ObservableList(List<T> delegate, Map<Object, Integer> idMap,
                          BiConsumer<CollectionChangeType, Collection<? extends T>> onCollectionChanged,
                          Consumer<T> onRemoveItem, Consumer<T> onAddItem) {
        this(delegate, idMap, onCollectionChanged);
        this.onRemoveItem = onRemoveItem;
        this.onAddItem = onAddItem;
    }

    private Object writeReplace() throws ObjectStreamException {
        List result = delegate;
        while (result instanceof ObservableList) {
            result = ((ObservableList) result).delegate;
        }
        return result;
    }

    protected void fireCollectionChanged(CollectionChangeType type, Collection<? extends T> changes) {
        if (onCollectionChanged != null)
            onCollectionChanged.accept(type, changes);
    }

    protected void fireCollectionRefreshed() {
        fireCollectionChanged(CollectionChangeType.REFRESH, Collections.emptyList());
    }

    protected void doOnAddItem(T item) {
        if (item != null && onAddItem != null) {
            onAddItem.accept(item);
        }
    }

    protected void doOnRemoveItem(T item) {
        if (item != null && onRemoveItem != null) {
            onRemoveItem.accept(item);
        }
    }

    @Override
    protected List<T> delegate() {
        return delegate;
    }

    @Override
    public void add(int index, T element) {
        super.add(index, element);
        doOnAddItem(element);
        fireCollectionChanged(CollectionChangeType.ADD_ITEMS, Collections.singletonList(element));
    }

    @Override
    public boolean add(T element) {
        boolean changed = super.add(element);
        doOnAddItem(element);
        if (changed)
            fireCollectionChanged(CollectionChangeType.ADD_ITEMS, Collections.singletonList(element));
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> elements) {
        boolean changed = super.addAll(index, elements);
        for (T element : elements) {
            doOnAddItem(element);
        }
        if (changed)
            fireCollectionChanged(CollectionChangeType.ADD_ITEMS, elements);
        return changed;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean changed = super.addAll(collection);
        for (T element : collection) {
            doOnAddItem(element);
        }
        if (changed)
            fireCollectionChanged(CollectionChangeType.ADD_ITEMS, collection);
        return changed;
    }

    @Override
    public T set(int index, T element) {
        T prev = super.set(index, element);
        if (prev != element) {
            doOnRemoveItem(prev);
            doOnAddItem(element);
            fireCollectionChanged(CollectionChangeType.SET_ITEM, Collections.singletonList(element));
        }
        return prev;
    }

    @Override
    public T remove(int index) {
        T entity = super.remove(index);
        doOnRemoveItem(entity);
        fireCollectionChanged(CollectionChangeType.REMOVE_ITEMS, Collections.singletonList(entity));
        return entity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object object) {
        if (idMap != null && object instanceof Entity) {
            Integer index = idMap.get(((Entity) object).getId());
            if (index != null) {
                T itemForRemove = delegate.get(index);

                boolean changed = super.remove(object);
                doOnRemoveItem(itemForRemove);
                if (changed)
                    fireCollectionChanged(CollectionChangeType.REMOVE_ITEMS, Collections.singletonList(itemForRemove));
                return changed;
            }
            return false;
        } else {
            boolean changed = super.remove(object);
            if (changed)
                fireCollectionChanged(CollectionChangeType.REMOVE_ITEMS, Collections.singletonList((T) object));
            return changed;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean removeAll(Collection<?> collection) {
        if (idMap != null) {
            List<T> itemsForRemove = new ArrayList<>(collection.size());

            for (Object object : collection) {
                if (object instanceof Entity) {
                    Integer index = idMap.get(((Entity) object).getId());
                    if (index != null) {
                        itemsForRemove.add((T) object);
                    }
                }
            }

            boolean changed = super.removeAll(itemsForRemove);
            for (T itemToRemove : itemsForRemove) {
                doOnRemoveItem(itemToRemove);
            }
            if (changed) {
                fireCollectionChanged(CollectionChangeType.REMOVE_ITEMS, itemsForRemove);
            }
            return changed;
        } else {
            boolean changed = super.removeAll(collection);
            if (changed)
                fireCollectionChanged(CollectionChangeType.REMOVE_ITEMS, (Collection<? extends T>) collection);
            return changed;
        }
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        for (T item : delegate) {
            doOnRemoveItem(item);
        }
        boolean changed = super.retainAll(collection);
        for (T item : delegate) {
            doOnAddItem(item);
        }
        if (changed)
            fireCollectionRefreshed();
        return changed;
    }

    @Override
    public void clear() {
        for (T item : delegate) {
            doOnRemoveItem(item);
        }
        boolean wasEmpty = isEmpty();
        super.clear();
        if (!wasEmpty) {
            fireCollectionRefreshed();
        }
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ObservableListIterator<>(super.listIterator(), onCollectionChanged, onAddItem);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ObservableListIterator<>(super.listIterator(index), onCollectionChanged, onAddItem);
    }

    @Override
    public Iterator<T> iterator() {
        return new ObservableIterator<>(super.iterator());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void sort(Comparator<? super T> c) {
        Object[] array = this.toArray();
        Arrays.sort(array, (Comparator) c);

        boolean changed = false;
        for (int i = 0; i < super.size(); i++) {
            T element = (T) array[i];
            T prev = super.set(i, element);
            if (prev != element) {
                changed = true;
            }
        }
        if (changed) {
            fireCollectionRefreshed();
        }
    }
}
