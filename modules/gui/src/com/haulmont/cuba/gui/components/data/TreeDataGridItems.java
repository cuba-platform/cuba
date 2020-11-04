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

import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * A common interface for providing data for the {@link com.haulmont.cuba.gui.components.TreeDataGrid} component.
 *
 * @param <T> items type
 */
public interface TreeDataGridItems<T> extends DataGridItems.Sortable<T> {

    /**
     * @param parent the parent item
     * @return child count of the given parent item
     */
    int getChildCount(T parent);

    /**
     * @param item the item to obtain children or {@code null} to get root items
     * @return children of the given item
     */
    Stream<T> getChildren(@Nullable T item);

    /**
     * @param item the item to check
     * @return {@code true} if the item has children, {@code false} otherwise
     */
    boolean hasChildren(T item);

    /**
     * @param item the item to get parent
     * @return the parent of the given item or {@code null} if no parent
     */
    @Nullable
    T getParent(T item);

    /**
     * Returns the property of entity which forms the hierarchy.
     *
     * @return hierarchy property name
     */
    String getHierarchyPropertyName();
}