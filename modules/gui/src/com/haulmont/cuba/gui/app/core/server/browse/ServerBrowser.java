/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.server.browse;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Table;

import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ServerBrowser extends AbstractWindow {

    public ServerBrowser(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        Table table = getComponent("table");
        ComponentsHelper.createActions(table);
    }
}
