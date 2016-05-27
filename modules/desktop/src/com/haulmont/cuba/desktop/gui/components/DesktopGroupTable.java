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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.GroupInfo;

public class DesktopGroupTable<E extends Entity>
        extends DesktopTable<E>
        implements GroupTable<E> {

    protected boolean showItemsCountForGroup = true;

    @Override
    public GroupDatasource getDatasource() {
        return (GroupDatasource) super.getDatasource();
    }

    @Override
    public void groupBy(Object[] properties) {
    }

    @Override
    public boolean getColumnGroupAllowed(String columnId) {
        return false;
    }

    @Override
    public void setColumnGroupAllowed(String columnId, boolean allowed) {
    }

    @Override
    public boolean getColumnGroupAllowed(Column column) {
        return false;
    }

    @Override
    public void setColumnGroupAllowed(Column column, boolean allowed) {
    }

    @Override
    public void expandAll() {
    }

    @Override
    public void expand(GroupInfo groupId) {
    }

    @Override
    public void expandPath(Entity item) {
    }

    @Override
    public void collapseAll() {
    }

    @Override
    public void collapse(GroupInfo groupId) {
    }

    @Override
    public boolean isExpanded(GroupInfo groupId) {
        return true;
    }

    @Override
    public boolean isFixedGrouping() {
        return false;
    }

    @Override
    public void setFixedGrouping(boolean groupingByUserEnabled) {
    }

    @Override
    public boolean isShowItemsCountForGroup() {
        return showItemsCountForGroup;
    }

    @Override
    public void setShowItemsCountForGroup(boolean showItemsCountForGroup) {
        this.showItemsCountForGroup = showItemsCountForGroup;
    }
}