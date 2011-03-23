/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maxim Gorbunkov
 * Created: 24.11.2009 11:22:46
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.actions.FilterApplyAction;
import com.haulmont.cuba.gui.components.actions.FilterClearAction;
import com.haulmont.cuba.gui.components.actions.RefreshAction;

import java.util.Map;

public class BasicBrowser extends AbstractLookup{

    public BasicBrowser(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);
        Table table = getComponent("table");
        ComponentsHelper.createActions(table);

        if (getComponent("filter") != null) {
            table.addAction(new FilterApplyAction(table));
            table.addAction(new FilterClearAction(table, "filterPanel"));
        } else {
            table.addAction(new RefreshAction(table));
        }
    }    
}
