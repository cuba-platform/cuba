/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.cache;

import com.haulmont.bali.datastruct.Pair;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Set of elements for ObjectsCache
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@SuppressWarnings({"unused"})
public class CacheSet implements Cloneable {
    private Collection<Object> items;

    public CacheSet() {
        this(Collections.<Object>emptyList());
    }

    public CacheSet(Collection<Object> items) {
        this.items = items;
    }

    public Collection<Object> getItems() {
        return items;
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
}