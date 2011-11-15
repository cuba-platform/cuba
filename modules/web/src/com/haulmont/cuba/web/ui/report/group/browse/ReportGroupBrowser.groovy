/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.ui.report.group.browse

import com.haulmont.cuba.gui.components.AbstractLookup
import com.haulmont.cuba.gui.components.IFrame
import com.haulmont.cuba.gui.components.Table
import com.haulmont.cuba.gui.components.actions.CreateAction
import com.haulmont.cuba.gui.WindowManager.OpenType
import com.haulmont.cuba.gui.components.actions.EditAction
import com.haulmont.cuba.gui.components.actions.RemoveAction

/**
 * 
 * <p>$Id$</p>
 *
 * @author artamonov
 */
class ReportGroupBrowser extends AbstractLookup {

    ReportGroupBrowser(IFrame frame) {
        super(frame)
    }

    @Override
    void init(Map<String, Object> params) {
        super.init(params)

        Table table = getComponent("table");
        table.addAction(new CreateAction(table, OpenType.DIALOG))
        table.addAction(new EditAction(table, OpenType.DIALOG))
        table.addAction(new RemoveAction(table))
    }
}
