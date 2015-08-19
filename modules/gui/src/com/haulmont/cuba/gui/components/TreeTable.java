/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;

/**
 * @author abramov
 * @version $Id$
 */
public interface TreeTable extends Table {

    String NAME = "treeTable";

    String getHierarchyProperty();
    void setDatasource(HierarchicalDatasource datasource);

    void expandAll();
    void expand(Object itemId);

    void collapseAll();
    void collapse(Object itemId);

    int getLevel(Object itemId);

    boolean isExpanded(Object itemId);

    @Override
    HierarchicalDatasource getDatasource();
}