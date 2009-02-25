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
import java.util.Map;

public class RoleBrowser extends AbstractWindow
{
    public RoleBrowser(IFrame frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        final Table table  = getComponent("roles");

        final TableActionsHelper helper = new TableActionsHelper(this, table);
        helper.createCreateAction();
        helper.createEditAction();
        helper.createRefreshAction();

        table.refresh();
    }
}
