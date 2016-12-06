/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.cache;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author chekashkin
 * @version $Id: CacheUpdateMessage.java 24463 2016-05-17 08:26:36Z shishov $
 */
public class CacheUpdateMessage implements Serializable {
    protected String cacheName;
    protected Collection<Object> itemsToRemove;
    protected Collection<Object> itemsToAdd;

    public CacheUpdateMessage(String cacheName, Collection<Object> itemsToRemove, Collection<Object> itemsToAdd) {
        this.cacheName = cacheName;
        this.itemsToRemove = itemsToRemove;
        this.itemsToAdd = itemsToAdd;
    }

    public Collection<Object> getItemsToRemove() {
        return itemsToRemove;
    }

    public void setItemsToRemove(Collection<Object> itemsToRemove) {
        this.itemsToRemove = itemsToRemove;
    }

    public Collection<Object> getItemsToAdd() {
        return itemsToAdd;
    }

    public void setItemsToAdd(Collection<Object> itemsToAdd) {
        this.itemsToAdd = itemsToAdd;
    }
}