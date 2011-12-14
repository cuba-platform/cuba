/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.app.security.group.lookup;

import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Tree;

import javax.inject.Inject;
import java.util.Map;

public class GroupLookup extends AbstractLookup {

    @Inject
    protected Tree groups;

    @Override
    public void init(Map<String, Object> params) {
        groups.getDatasource().refresh();
        groups.expandTree();
    }
}
