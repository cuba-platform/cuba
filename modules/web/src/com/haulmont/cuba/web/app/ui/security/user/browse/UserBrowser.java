/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.01.2009 10:15:26
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.security.user.browse;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.web.rpt.ReportHelper;
import com.haulmont.cuba.web.rpt.ReportOutput;
import com.haulmont.cuba.web.rpt.WebExportDisplay;
import com.haulmont.cuba.core.entity.Entity;

import java.util.Map;

public class UserBrowser extends AbstractLookup {
    public UserBrowser(Window frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        final Table table  = getComponent("users");

        final TableActionsHelper helper = new TableActionsHelper(this, table);

//        helper.createFilterApplyAction("filter.apply");
//        helper.createFilterClearAction("filter.clear", "group-box");

        helper.createCreateAction();
        helper.createEditAction();
        helper.createRemoveAction();
        helper.createExcelAction(new WebExportDisplay());

        table.addAction(
                new AbstractAction("changePassw")
                {
                    public void actionPerform(Component component) {
                        if (!table.getSelected().isEmpty()) {
                            openEditor(
                                    "sec$User.changePassw",
                                    (Entity) table.getSelected().iterator().next(),
                                    WindowManager.OpenType.DIALOG
                            );
                        }
                    }
                }
        );

        table.addAction(
                new AbstractAction("print")
                {
                    public void actionPerform(Component component) {
                        ReportHelper.printJasperReport(
                                "cuba/report/users",
                                new ReportOutput(ExportFormat.HTML).setNewWindow(true)
                        );
                    }
                }
        );

//        getDsContext().get("users").refresh();
    }
}
