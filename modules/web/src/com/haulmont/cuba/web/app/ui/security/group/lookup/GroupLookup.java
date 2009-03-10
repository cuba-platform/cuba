/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 10.03.2009 17:14:52
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.security.group.lookup;

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
