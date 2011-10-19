package com.haulmont.cuba.web.ui.report.run;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.gui.report.ReportHelper;
import com.haulmont.cuba.report.Report;

import java.util.Map;

public class ReportRun extends AbstractLookup {
    private static final long serialVersionUID = -1223276050757586365L;
    private static final String RUN_ACTION_ID = "runReport";
    private Table reportsTable;

    public ReportRun(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        reportsTable = getComponent("table");

        AbstractAction runAction = new AbstractAction(RUN_ACTION_ID) {
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

        if (params.get("param$screen") != null) runAction.setEnabled(false);//this is print form or run report
        if (params.get("param$user") == null) params.put("param$user", UserSessionProvider.getUserSession().getUser());

        reportsTable.getDatasource().refresh(params);

        reportsTable.getDatasource().addListener(new DsListenerAdapter() {
            private static final long serialVersionUID = 5878129003987291804L;
            @Override
            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                boolean value = item != null;
                enableRunAction(value);
            }
        });

        enableRunAction(false);
    }

    private void enableRunAction(boolean value) {
        reportsTable.getAction(RUN_ACTION_ID).setEnabled(value);
    }
}
