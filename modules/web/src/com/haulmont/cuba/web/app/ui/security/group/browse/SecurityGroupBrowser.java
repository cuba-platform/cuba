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

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.data.Datasource;

public class SecurityGroupBrowser extends AbstractWindow
{
    public SecurityGroupBrowser(Window frame) {
        super(frame);
    }

    protected void init() {
        Tree tree = getComponent("groups");
        tree.getDatasource().refresh();
        tree.expandTree();
    }
}
