/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.report.run;

import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.report.ReportHelper;
import com.haulmont.cuba.report.Report;

import javax.inject.Inject;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class ReportRun extends AbstractLookup {
    private static final long serialVersionUID = -1223276050757586365L;
    private static final String RUN_ACTION_ID = "runReport";

    @Inject
    private Table reportsTable;

    public ReportRun(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        AbstractAction runAction = new ItemTrackingAction(RUN_ACTION_ID) {
            private static final long serialVersionUID = 8363252904120435825L;

            @Override
            public void actionPerform(Component component) {
                Report report = reportsTable.getSingleSelected();
                if (report != null) {
                    report = getDsContext().getDataService().reload(report, "report.edit");
                    ReportHelper.runReport(report, ReportRun.this);
                }
            }
        };
        reportsTable.addAction(runAction);
        reportsTable.setItemClickAction(runAction);

        if (params.get("param$user") == null) params.put("param$user", UserSessionProvider.getUserSession().getUser());

        reportsTable.getDatasource().refresh(params);
    }
}
