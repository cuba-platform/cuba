/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.GroupInfo;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author gorodnov
 * @version $Id$
 */
public interface GroupTable<E extends Entity> extends Table<E> {

    String NAME = "groupTable";

    @Override
    GroupDatasource getDatasource();

    void groupBy(Object[] properties);
    void disableGroupBy(List<Object> properties);

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