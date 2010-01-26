/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 18.11.2009 14:44:36
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.GroupInfo;

public interface GroupTable extends Table {

    void groupBy(Object[] properties);

    void expandAll();
    void expand(GroupInfo groupId);

    void collapseAll();
    void collapse(GroupInfo groupId);

    boolean isExpanded(GroupInfo groupId);
}
