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

import java.util.Map;

public class BasicBrowser extends AbstractLookup{

    public BasicBrowser(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);
        Table table = getComponent("table");
        TableActionsHelper helper = new TableActionsHelper(this, table);
        helper.createCreateAction();
        helper.createEditAction();
        helper.createRemoveAction();

        if (getComponent("filter") != null) {
            helper.createFilterApplyAction("filter.apply");
            helper.createFilterClearAction("filter.clear", "filterPanel");
        } else {
            helper.createRefreshAction();
        }
    }    
}
