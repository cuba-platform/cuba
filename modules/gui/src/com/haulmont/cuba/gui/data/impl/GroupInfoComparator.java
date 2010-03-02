/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 01.03.2010 19:43:02
 *
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.GroupInfo;

public class GroupInfoComparator extends AbstractComparator<GroupInfo> {
    public GroupInfoComparator(boolean asc) {
        super(asc);
    }

    public int compare(GroupInfo o1, GroupInfo o2) {
        return __compare(o1.getValue(), o2.getValue());
    }
}
