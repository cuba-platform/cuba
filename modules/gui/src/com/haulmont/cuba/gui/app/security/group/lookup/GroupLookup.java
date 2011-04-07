/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.app.security.group.lookup;

import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.Map;

public class GroupLookup extends AbstractLookup {
    public GroupLookup(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        final Tree tree = getComponent("groups");

        final CollectionDatasource treeDS = tree.getDatasource();
        treeDS.refresh();
        tree.expandTree();
    }
}
