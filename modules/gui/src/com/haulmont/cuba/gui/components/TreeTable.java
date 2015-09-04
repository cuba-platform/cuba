/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;

/**
 * @author abramov
 * @version $Id$
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