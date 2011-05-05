/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.cache;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.Collection;

/**
 * Items set for ObjectsCache
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class CacheSet {
    private Collection items;

    public CacheSet(Collection items) {
        this.items = items;
    }

    public Collection getItems() {
        return items;
    }

    public CacheSet query(Predicate selector) {
        CacheSet resultSet;
        if (selector != null) {
            Collection setItems = CollectionUtils.select(items, selector);
            resultSet = new CacheSet(setItems);
        } else
            resultSet = new CacheSet(items);
        return resultSet;
    }
}