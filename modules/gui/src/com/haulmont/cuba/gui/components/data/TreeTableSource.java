/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.components.data;

import java.util.Collection;

/**
 * todo JavaDoc
 *
 * @param <I>
 */
public interface TreeTableSource<I> extends TableSource.Sortable<I> {
    /** Property of entity which forms the hierarchy */
    String getHierarchyPropertyName();

    /** Identifiers of all root items */
    Collection<?> getRootItemIds();

    /** Get parent ID for item with the given ID */
    Object getParent(Object itemId);

    /** Get children IDs for item with the given ID */
    Collection<?> getChildren(Object itemId);

    /** True if item with the given ID is a root item */
    boolean isRoot(Object itemId);

    /** True if item with the given ID has children */
    boolean hasChildren(Object itemId);
}