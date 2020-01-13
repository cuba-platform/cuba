/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.gui.components.data.table;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.TreeTableItems;

import java.util.Collection;
import java.util.Collections;

public class EmptyTreeTableItems<E extends Entity> extends EmptyTableItems<E> implements TreeTableItems<E> {

    public EmptyTreeTableItems(MetaClass metaClass) {
        super(metaClass);
    }

    @Override
    public String getHierarchyPropertyName() {
        return "";
    }

    @Override
    public Collection<?> getRootItemIds() {
        return Collections.emptyList();
    }

    @Override
    public Object getParent(Object itemId) {
        return null;
    }

    @Override
    public Collection<?> getChildren(Object itemId) {
        return Collections.emptyList();
    }

    @Override
    public boolean isRoot(Object itemId) {
        return false;
    }

    @Override
    public boolean hasChildren(Object itemId) {
        return false;
    }

    @Override
    public Object nextItemId(Object itemId) {
        return null;
    }

    @Override
    public Object prevItemId(Object itemId) {
        return null;
    }

    @Override
    public Object firstItemId() {
        return null;
    }

    @Override
    public Object lastItemId() {
        return null;
    }

    @Override
    public boolean isFirstId(Object itemId) {
        return false;
    }

    @Override
    public boolean isLastId(Object itemId) {
        return false;
    }

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
        // do nothing
    }

    @Override
    public void resetSortOrder() {
        // do nothing
    }
}
