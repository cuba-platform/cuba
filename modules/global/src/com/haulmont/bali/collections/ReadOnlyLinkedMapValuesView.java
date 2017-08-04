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

package com.haulmont.bali.collections;

import org.apache.commons.collections4.map.AbstractLinkedMap;
import org.apache.commons.collections4.map.LinkedMap;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

/**
 * Serializable, unmodifiable collection view for {@link LinkedMap}. <br>
 * Use this collection view instead of {@link LinkedMap#values()} to have ability to serialize collection.
 */
public class ReadOnlyLinkedMapValuesView extends AbstractCollection implements Serializable {
    private final LinkedMap parent;

    public ReadOnlyLinkedMapValuesView(LinkedMap parent) {
        this.parent = parent;
    }

    @Override
    public int size() {
        return parent.size();
    }

    @Override
    public boolean contains(Object value) {
        return parent.containsValue(value);
    }

    @Override
    @Nonnull
    public Iterator iterator() {
        return LinkedMapValuesIteratorProvider.createValuesIterator(parent);
    }

    @Override
    public boolean add(Object e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(Predicate filter) {
        throw new UnsupportedOperationException();
    }

    // hack access to commons-collections internals
    protected static class LinkedMapValuesIteratorProvider extends AbstractLinkedMap {
        public static Iterator createValuesIterator(LinkedMap linkedMap) {
            return new ReadOnlyValuesIterator(linkedMap);
        }

        protected static class ReadOnlyValuesIterator extends ValuesIterator {
            public ReadOnlyValuesIterator(AbstractLinkedMap parent) {
                super(parent);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }
}