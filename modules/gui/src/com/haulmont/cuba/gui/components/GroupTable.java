/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.GroupInfo;

/**
 * @author gorodnov
 * @version $Id$
 */
public interface GroupTable extends Table {

    String NAME = "groupTable";

    void groupBy(Object[] properties);

    void expandAll();
    void expand(GroupInfo groupId);

    void collapseAll();
    void collapse(GroupInfo groupId);

    boolean isExpanded(GroupInfo groupId);

    boolean isFixedGrouping();
    void setFixedGrouping(boolean fixedGrouping);
}