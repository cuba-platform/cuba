package cuba.client.web.ui.report.run;

import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.report.Report;
import com.haulmont.cuba.web.app.ui.report.ReportHelper;

import java.util.Map;

public class ReportRun extends BasicBrowser {
    public ReportRun(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);
        final Table reportsTable = getComponent("table");
        Button runReport = getComponent("runReport");
        runReport.setAction(new AbstractAction("runReport") {
            public void actionPerform(Component component) {
                Report report = reportsTable.getSingleSelected();
                report = getDsContext().getDataService().reload(report, "report.edit");
                ReportHelper.runReport(report,ReportRun.this);
            }
        });
        if (params.get("param$screen") != null) runReport.setVisible(false);//this is print form or run report 
        if (params.get("param$user") == null) params.put("param$user", UserSessionClient.getUserSession().getUser());
        reportsTable.getDatasource().refresh(params);
    }
}
