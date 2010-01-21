/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.01.2009 15:08:16
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.security.group.browse;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.core.entity.Entity;

import java.util.*;

public class GroupBrowser extends AbstractWindow {
    public GroupBrowser(Window frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        final Tree tree = getComponent("groups");

        final CollectionDatasource treeDS = tree.getDatasource();
        treeDS.refresh();
        tree.expandTree();

        final Collection itemIds = treeDS.getItemIds();
        if (!itemIds.isEmpty()) {
            tree.setSelected(treeDS.getItem(itemIds.iterator().next()));
        }

        final TreeActionsHelper helper = new TreeActionsHelper(this, tree);
        helper.createCreateAction(WindowManager.OpenType.DIALOG);
        helper.createEditAction(WindowManager.OpenType.DIALOG);
        helper.createRemoveAction();

        final Table users = getComponent("users");
        Table constraints = getComponent("constraints");

        final TableActionsHelper usersActions = new TableActionsHelper(this, users);
        usersActions.createCreateAction(new ValueProvider() {
            public Map<String, Object> getValues() {
                final Map<String, Object> map = new HashMap<String, Object>();
                map.put("group", tree.getSelected());
                return map;
            }

            public Map<String, Object> getParameters() {
                return Collections.emptyMap();
            }
        });
        usersActions.createEditAction();
        usersActions.addListener(new EditUserListener(users));
        users.addAction(new AbstractAction("moveToGroup") {
            public String getCaption() {
                return getMessage("users.moveToGroup");
            }

            public boolean isEnabled() {
                return true;
            }

            public void actionPerform(Component component) {
                final Set<User> selected = users.getSelected();
                if (!selected.isEmpty()) {
                    openLookup("sec$Group.lookup", new Lookup.Handler() {
                        public void handleLookup(Collection items) {
                            if (items.size() == 1) {
                                Group group = (Group) items.iterator().next();
                                for (User user : selected) {
                                    user.setGroup(group);
                                }

                                final CollectionDatasource ds = users.getDatasource();
                                ds.commit();
                                ds.refresh();
                                users.setSelected((Entity) null);
                            }
                        }
                    }, WindowManager.OpenType.DIALOG);
                }
            }
        });
        usersActions.createRefreshAction();

        final TableActionsHelper constraintsActions = new TableActionsHelper(this, constraints);
        constraintsActions.createCreateAction(new ValueProvider() {
            public Map<String, Object> getValues() {
                final Map<String, Object> map = new HashMap<String, Object>();
                map.put("group", tree.getSelected());
                return map;
            }

            public Map<String, Object> getParameters() {
                return Collections.emptyMap();
            }
        });
        constraintsActions.createEditAction();
        constraintsActions.createRemoveAction();
        constraintsActions.createRefreshAction();
    }

    private class EditUserListener implements TableActionsHelper.Listener {

        private Table users;

        private EditUserListener(Table users) {
            this.users = users;
        }

        public void entityCreated(Entity entity) {
        }

        public void entityEdited(Entity entity) {
            final CollectionDatasource ds = users.getDatasource();
            ds.refresh();
            users.setSelected((Entity) null);
        }

        public void entityRemoved(Set<Entity> entity) {
        }
    }
}
