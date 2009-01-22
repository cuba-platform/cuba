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
import com.haulmont.cuba.web.ui.Screen;
import com.haulmont.cuba.web.components.ComponentsHelper;
import com.haulmont.cuba.security.entity.User;

public class SecurityUserBrowser extends AbstractFrame implements Window {
    public SecurityUserBrowser(Screen frame) {
        super(frame);
    }

    protected void init() {
        final Button button  = getComponent("filter.apply");
        final Table table  = getComponent("users");

        table.addAction(new Action() {
            public String getCaption() {
                return "Refresh";
            }

            public boolean isEnabled() {
                final User user = table.getSingleSelected();
                return user != null && user.getName().equals("Administrator");
            }

            public void actionPerform(Component component) {
                table.getDatasource().refresh();
            }
        });

        button.setAction(new Action() {
            public String getCaption() {
                return null;
            }

            public boolean isEnabled() {
                return true;
            }

            public void actionPerform(Component component) {
                table.getDatasource().refresh();
            }
        });
    }

    protected boolean close() {
        final com.itmill.toolkit.ui.Window window = ComponentsHelper.unwrap(this).getWindow();
        window.showNotification("Closing screen", com.itmill.toolkit.ui.Window.Notification.TYPE_TRAY_NOTIFICATION);
        return true;
    }
}
