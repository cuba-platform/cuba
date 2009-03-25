/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.01.2009 10:15:26
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.security.user.browse;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.web.gui.components.ComponentsHelper;

import java.util.Map;

public class UserBrowser extends AbstractLookup {
    public UserBrowser(Window frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        final Button button  = getComponent("filter.apply");
        final Table table  = getComponent("users");

        final TableActionsHelper helper = new TableActionsHelper(this, table);
        helper.createCreateAction();
        helper.createEditAction();
        helper.createRemoveAction();

        final Action refreshAction = helper.createRefreshAction();
        button.setAction(refreshAction);

        table.addAction(
                new AbstractAction("changePassw")
                {
                    public void actionPerform(Component component) {
                        if (!table.getSelected().isEmpty()) {
                            openEditor(
                                    "sec$User.changePassw",
                                    table.getSelected().iterator().next(),
                                    WindowManager.OpenType.DIALOG
                            );
                        }
                    }
                }
        );
    }

    public boolean close(String actionId) {
        final com.itmill.toolkit.ui.Window window = ComponentsHelper.unwrap(this).getWindow();
        window.showNotification("Closing screen", com.itmill.toolkit.ui.Window.Notification.TYPE_TRAY_NOTIFICATION);
        return super.close(actionId);
    }
}
