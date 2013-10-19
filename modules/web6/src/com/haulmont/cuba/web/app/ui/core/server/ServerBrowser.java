/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.app.ui.core.server;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.actions.RefreshAction;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;

import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ServerBrowser extends AbstractWindow {

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        Table table = getComponent("servers");
        table.addAction(new RefreshAction(table));

        com.vaadin.ui.Table impl = (com.vaadin.ui.Table) WebComponentsHelper.unwrap(table);
        impl.setPageLength(10);
    }
}