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

package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;

/**
 * CollectionDatasource supporting hierarchy of items.
 * @param <T> type of entity
 * @param <K> type of entity ID
 */
public interface HierarchicalDatasource<T extends Entity<K>, K> extends CollectionDatasource<T, K> {

    /** Property of entity which forms the hierarchy */
    String getHierarchyPropertyName();

    /** Set property of entity which forms the hierarchy */
    void setHierarchyPropertyName(String parentPropertyName);

    /** Identifiers of all root items */
    Collection<K> getRootItemIds();

    /** Get parent ID for item with the given ID */
    K getParent(K itemId);

    /** Get children IDs for item with the given ID */
    Collection<K> getChildren(K itemId);

    /** True if item with the given ID is a root item */
    boolean isRoot(K itemId);

    /** True if item with the given ID has children */
    boolean hasChildren(K itemId);
}