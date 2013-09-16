/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
