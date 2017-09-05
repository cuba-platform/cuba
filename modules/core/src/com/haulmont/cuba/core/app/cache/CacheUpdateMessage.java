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

import java.io.Serializable;
import java.util.Collection;

/**
 * @deprecated Will be removed in release 7.0.
 */
@Deprecated
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