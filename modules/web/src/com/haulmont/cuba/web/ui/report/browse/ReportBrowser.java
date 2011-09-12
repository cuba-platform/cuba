/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 07.05.2010 15:37:08
 *
 * $Id$
 */
package com.haulmont.cuba.web.ui.report.browse;

import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.report.Report;
import com.haulmont.cuba.report.app.ReportService;
import com.haulmont.cuba.web.filestorage.WebExportDisplay;
import com.haulmont.cuba.web.app.ui.report.ReportHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.web.ui.report.fileuploaddialog.ReportImportDialog;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ReportBrowser extends BasicBrowser {
    public ReportBrowser(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        final Table reportsTable = getComponent("table");
        Button runReport = getComponent("runReport");
        runReport.setAction(new AbstractAction("runReport") {
            public void actionPerform(Component component) {
                Report report = reportsTable.getSingleSelected();
                if (report != null) {
                    report = getDsContext().getDataService().reload(report, "report.edit");
                    if (report.getInputParameters() != null && report.getInputParameters().size() > 0) {
                        openWindow("report$inputParameters", WindowManager.OpenType.DIALOG, Collections.<String, Object>singletonMap("report", report));
                    } else {
                        ReportHelper.printReport(report, Collections.<String, Object>emptyMap());
                    }
                }
            }
        });

        Button importReport = getComponent("import");
        importReport.setAction(new AbstractAction("import") {
            public void actionPerform(Component component) {
                final ReportImportDialog dialog = openWindow("report$Report.fileUploadDialog", WindowManager.OpenType.DIALOG);
                dialog.addListener(new CloseListener() {
                    public void windowClosed(String actionId) {
                        if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                            ReportService rs = ServiceLocator.lookup(ReportService.NAME);
                            try {
                                rs.importReports(dialog.getBytes());
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                            reportsTable.getDatasource().refresh();
                        }
                    }
                });
            }
        });

        Button exportReport = getComponent("export");
        exportReport.setAction(new AbstractAction("export") {
            public void actionPerform(Component component) {
                Set<Report> reports = reportsTable.getSelected();
                if ((reports != null) && (!reports.isEmpty())) {
                    try {
                        ReportService rs = ServiceLocator.lookup(ReportService.NAME);
                        new WebExportDisplay().show(new ByteArrayDataProvider(rs.exportReports(reports)), "Reports", ExportFormat.ZIP);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}