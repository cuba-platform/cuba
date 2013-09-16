/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app.security.group.lookup;

import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.security.entity.Group;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * @author abramov
 * @version $Id$
 */
public class GroupLookup extends AbstractLookup {

    @Inject
    protected Tree groups;

    @Inject
    protected HierarchicalDatasource<Group, UUID> groupsDs;

    @Override
    public void init(Map<String, Object> params) {
        getDialogParams().setWidth(600).setHeight(400);

        groupsDs.refresh();
        groups.expandTree();

        Collection<UUID> rootItemIds = groupsDs.getRootItemIds();
        if ((rootItemIds != null) && (!rootItemIds.isEmpty())) {
            UUID firstId = rootItemIds.iterator().next();
            Group item = groupsDs.getItem(firstId);
            groups.setSelected(item);
        }
    }
}
