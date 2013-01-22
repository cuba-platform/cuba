/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 27.03.2009 15:14:18
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.core.server;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.actions.RefreshAction;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;

import java.util.Map;

public class ServerBrowser extends AbstractWindow
{
    public ServerBrowser(IFrame frame) {
        super(frame);
    }

    public void init(Map<String, Object> params) {
        super.init(params);

        Table table = getComponent("servers");
        table.addAction(new RefreshAction(table));

        com.vaadin.ui.Table impl = (com.vaadin.ui.Table) WebComponentsHelper.unwrap(table);
        impl.setPageLength(10);
    }
}
