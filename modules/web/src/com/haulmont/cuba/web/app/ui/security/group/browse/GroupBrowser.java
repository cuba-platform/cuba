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

import java.util.Map;
import java.util.HashMap;

public class GroupBrowser extends AbstractWindow
{
    public GroupBrowser(Window frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        final Tree tree = getComponent("groups");
        tree.getDatasource().refresh();
        tree.expandTree();

        final TreeActionsHelper helper = new TreeActionsHelper(this, tree);
        helper.createCreateAction(WindowManager.OpenType.DIALOG);
        helper.createEditAction(WindowManager.OpenType.DIALOG);

        Table users = getComponent("users");

        final TableActionsHelper usersActions = new TableActionsHelper(this, users);
        usersActions.createCreateAction(new ValueProvider() {
            public Map<String, Object> getValues() {
                final Map<String, Object> map = new HashMap<String, Object>();
                map.put("group", tree.getSelected());
                return map;
            }
        });
        usersActions.createEditAction();
        usersActions.createRefreshAction();
    }
}
