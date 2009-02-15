/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 14.02.2009 22:38:29
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.security.role.browse;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.security.entity.Role;

import java.util.Set;

public class RoleBrowser extends AbstractWindow
{
    public RoleBrowser(IFrame frame) {
        super(frame);
    }

    protected void init() {
        final Table table  = getComponent("roles");

        table.addAction(
                new AbstractAction("edit")
                {
                    public String getCaption() {
                        return "Edit";
                    }

                    public boolean isEnabled() {
                        return true;
                    }

                    public void actionPerform(Component component) {
                        final Set selected = table.getSelected();
                        if (selected.size() == 1) {
                            Role user = (Role) selected.iterator().next();
                            openEditor("sec$Role.edit", user, WindowManager.OpenType.THIS_TAB);
                        }
                    }
                });
        table.addAction(
                new AbstractAction("refresh")
                {
                    public String getCaption() {
                        return "Refresh";
                    }

                    public boolean isEnabled() {
                        return true;
                    }

                    public void actionPerform(Component component) {
                        table.getDatasource().refresh();
                    }
                });

    }
}
