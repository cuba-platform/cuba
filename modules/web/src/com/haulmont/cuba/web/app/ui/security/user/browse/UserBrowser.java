/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.01.2009 10:15:26
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.security.user.browse;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.components.ComponentsHelper;

import java.util.Set;

public class UserBrowser extends AbstractLookup {
    public UserBrowser(Window frame) {
        super(frame);
    }

    protected void init() {
        final Button button  = getComponent("filter.apply");
        final Table table  = getComponent("users");

        table.addAction(new AbstractAction("edit") {
            public String getCaption() {
                return "Edit";
            }

            public boolean isEnabled() {
                return true;
            }

            public void actionPerform(Component component) {
                final Set selected = table.getSelected();
                if (selected.size() == 1) {
                    User user = (User) selected.iterator().next();
//                    openEditor(GenericEditorWindow.class, user, WindowManager.OpenType.THIS_TAB);
                    openEditor("sec$User.edit", user, WindowManager.OpenType.THIS_TAB);
                }
            }
        });
        table.addAction(new AbstractAction("refresh") {
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

        button.setAction(new AbstractAction("refresh") {
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

    public boolean close() {
        final com.itmill.toolkit.ui.Window window = ComponentsHelper.unwrap(this).getWindow();
        window.showNotification("Closing screen", com.itmill.toolkit.ui.Window.Notification.TYPE_TRAY_NOTIFICATION);
        return super.close();
    }
}
