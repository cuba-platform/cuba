/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

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
}