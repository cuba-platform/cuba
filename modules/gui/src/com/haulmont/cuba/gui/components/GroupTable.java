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
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.GroupInfo;

import javax.annotation.Nullable;

/**
 */
public interface GroupTable<E extends Entity> extends Table<E> {

    String NAME = "groupTable";

    @Override
    GroupDatasource getDatasource();

    void groupBy(Object[] properties);

    boolean getColumnGroupAllowed(String columnId);
    void setColumnGroupAllowed(String columnId, boolean allowed);

    boolean getColumnGroupAllowed(Table.Column column);
    void setColumnGroupAllowed(Table.Column column, boolean allowed);

    void expandAll();
    void expand(GroupInfo groupId);

    /**
     * Expand all groups for specified item.
     */
    void expandPath(Entity item);

    void collapseAll();
    void collapse(GroupInfo groupId);

    boolean isExpanded(GroupInfo groupId);

    boolean isFixedGrouping();
    void setFixedGrouping(boolean fixedGrouping);

    /**
     * @return true if GroupTable shows items count for group
     */
    boolean isShowItemsCountForGroup();
    /**
     * Show or hide items count for GroupTable groups. <br/>
     * Default value is true.
     */
    void setShowItemsCountForGroup(boolean showItemsCountForGroup);

    /**
     * Allows to define different styles for table cells.
     */
    interface GroupStyleProvider<E extends Entity> extends StyleProvider<E> {
        /**
         * Called by {@link GroupTable} to get a style for group row.
         *
         * @param info   an group represented by the current row
         * @return style name or null to apply the default
         */
        @Nullable
        String getStyleName(GroupInfo info);
    }
}