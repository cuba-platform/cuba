/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.GroupInfo;

import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
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
    public void disableGroupBy(List<Object> properties) {
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