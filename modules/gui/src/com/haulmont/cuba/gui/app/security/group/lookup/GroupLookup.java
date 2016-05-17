/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
 */
public class GroupLookup extends AbstractLookup {

    @Inject
    protected Tree<Group> groups;

    @Inject
    protected HierarchicalDatasource<Group, UUID> groupsDs;

    @Override
    public void init(Map<String, Object> params) {
        groupsDs.refresh();
        groups.expandTree();

        if (params.containsKey("exclude")) {
            excludeItem((Group) params.get("exclude"));
        }

        Collection<UUID> rootItemIds = groupsDs.getRootItemIds();
        if ((rootItemIds != null) && (!rootItemIds.isEmpty())) {
            UUID firstId = rootItemIds.iterator().next();
            Group item = groupsDs.getItem(firstId);
            groups.setSelected(item);
        }
    }

    protected void excludeItem(Group group) {
        for (UUID childId : groupsDs.getChildren(group.getId())) {
            excludeItem(groupsDs.getItem(childId));
        }

        groupsDs.excludeItem(group);
    }
}