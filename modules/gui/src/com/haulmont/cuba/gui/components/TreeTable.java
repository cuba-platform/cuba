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
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;

/**
 */
public interface TreeTable<E extends Entity> extends Table<E> {

    String NAME = "treeTable";

    String getHierarchyProperty();
    void setDatasource(HierarchicalDatasource datasource);

    void expandAll();
    void expand(Object itemId);

    void collapseAll();
    void collapse(Object itemId);

    /**
     * Expand tree table including specified level
     *
     * @param level level of TreeTable nodes to expand, if passed level = 1 then root items will be expanded
     * @throws IllegalArgumentException if level < 1
     */
    void expandUpTo(int level);

    int getLevel(Object itemId);

    boolean isExpanded(Object itemId);

    @Override
    HierarchicalDatasource getDatasource();
}