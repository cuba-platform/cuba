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

package com.haulmont.cuba.core.app.cache;

import com.google.common.collect.Sets;
import com.haulmont.bali.datastruct.Pair;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Set of elements for ObjectsCache
 *
 */
@SuppressWarnings({"unused"})
public class CacheSet implements Cloneable {
    protected Set<Object> addedItems = Sets.newHashSet();
    protected Set<Object> removedItems = Sets.newHashSet();

    private Collection<Object> items;

    public CacheSet() {
        this(Collections.<Object>emptyList());
    }

    public CacheSet(Collection<Object> items) {
        this.items = items;
    }

    public Collection<Object> getItems() {
        return new DifferencesDecorator<>(items);
    }

    public Set<Object> getAddedItems() {
        return addedItems;
    }

    public void setAddedItems(Set<Object> addedItems) {
        this.addedItems = addedItems;
    }

    public Set<Object> getRemovedItems() {
        return removedItems;
    }

    public void setRemovedItems(Set<Object> removedItems) {
        this.removedItems = removedItems;
    }

    /**
     * Single predicate query
     *
     * @param selector Selector
     * @return CacheSet
     */
    public CacheSet query(Predicate selector) {
        checkNotNull(selector);

        LinkedList<Object> setItems = new LinkedList<>();
        CollectionUtils.select(items, selector, setItems);
        return new CacheSet(setItems);
    }

    /**
     * Sequential filtering by selectors
     *
     * @param selectors Selectors
     * @return CacheSet
     */
    public CacheSet querySequential(Predicate... selectors) {
        checkNotNull(selectors);

        Collection<Object> resultCollection = new ArrayList<>(items);
        Collection<Object> filterCollection = new LinkedList<>();
        Collection<Object> tempCollection;

        int i = 0;
        while ((i < selectors.length) && (resultCollection.size() > 0)) {
            CollectionUtils.select(resultCollection, selectors[i], filterCollection);

            tempCollection = resultCollection;
            resultCollection = filterCollection;
            filterCollection = tempCollection;

            filterCollection.clear();
            i++;
        }

        return new CacheSet(resultCollection);
    }

    /**
     * Conjunction count matches
     *
     * @param selectors Selectors
     * @return CacheSet
     */
    public int countConjunction(Predicate... selectors) {
        checkNotNull(selectors);

        ConjunctionPredicate predicate = new ConjunctionPredicate(selectors);

        return CollectionUtils.countMatches(items, predicate);
    }

    public Pair<Integer, Integer> countConjunction(Collection<Predicate> selectors, Predicate amplifyingSelector) {
        checkNotNull(selectors);
        checkNotNull(amplifyingSelector);

        ConjunctionPredicate conjunctionPredicate = new ConjunctionPredicate(selectors);

        int count1 = 0;
        int count2 = 0;

        for (Object item : items) {
            if (conjunctionPredicate.evaluate(item)) {
                count1++;
                if (amplifyingSelector.evaluate(item)) {
                    count2++;
                }
            }
        }

        return new Pair<>(count1, count2);
    }

    /**
     * Conjunction filtering by selectors
     *
     * @param selectors Selectors
     * @return CacheSet
     */
    public CacheSet queryConjunction(Predicate... selectors) {
        return query(new ConjunctionPredicate(selectors));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        CacheSet cloneInstance = (CacheSet) super.clone();
        cloneInstance.items = new ArrayList<>(items);
        return cloneInstance;
    }

    /**
     * Size
     *
     * @return Cache set size
     */
    public int getSize() {
        return (items != null) ? items.size() : 0;
    }

    /**
     * Predicate with conjunction operation
     */
    public static class ConjunctionPredicate implements Predicate {

        private Predicate[] selectors;

        public ConjunctionPredicate(Predicate... selectors) {
            checkNotNull(selectors);
            this.selectors = selectors;
        }

        public ConjunctionPredicate(Collection<Predicate> selectors) {
            checkNotNull(selectors);

            this.selectors = new Predicate[selectors.size()];
            int i = 0;
            for (Predicate selector : selectors) {
                this.selectors[i++] = selector;
            }
        }

        @Override
        public boolean evaluate(Object object) {
            checkNotNull(selectors);
            for (Predicate p : selectors) {
                if (!p.evaluate(object))
                    return false;
            }
            return true;
        }
    }

    public class DifferencesDecorator<E> implements Collection<E> {
        protected Collection<E> items;


        public DifferencesDecorator(Collection<E> items) {
            this.items = items;
        }

        @Override
        public int size() {
            return items.size();
        }

        @Override
        public boolean isEmpty() {
            return items.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return items.contains(o);
        }

        @Override
        public Iterator<E> iterator() {
            return new IteratorDecorator(items.iterator());
        }

        @Override
        public Object[] toArray() {
            return items.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return items.toArray(a);
        }

        @Override
        public boolean add(E o) {
            addedItems.add(o);
            return items.add(o);
        }

        @Override
        public boolean remove(Object o) {
            removedItems.add(o);
            return items.remove(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return items.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            addedItems.addAll(c);
            return items.addAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            for (E item : items) {
                if (!c.contains(item))
                    removedItems.add(item);
            }
            return items.retainAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            removedItems.addAll(c);
            return items.removeAll(c);
        }

        @Override
        public void clear() {
            items.clear();
        }

        protected class IteratorDecorator implements Iterator<E> {
            protected Iterator<E> iterator;
            protected E current;

            public IteratorDecorator(Iterator<E> iterator) {
                this.iterator = iterator;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                current = iterator.next();
                return current;
            }

            @Override
            public void remove() {
                removedItems.add(current);
                iterator.remove();
            }
        }
    }
}