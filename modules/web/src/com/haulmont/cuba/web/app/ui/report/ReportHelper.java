/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 01.06.2010 13:16:03
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.report;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.report.Report;
import com.haulmont.cuba.report.ReportOutputType;
import com.haulmont.cuba.report.ReportType;
import com.haulmont.cuba.report.app.ReportService;
import com.haulmont.cuba.web.rpt.WebExportDisplay;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class ReportHelper {
    static HashMap<ReportOutputType, ExportFormat> exportFormats = new HashMap<ReportOutputType, ExportFormat>();
    static {
        exportFormats.put(ReportOutputType.XLS, ExportFormat.XLS);
        exportFormats.put(ReportOutputType.DOC, ExportFormat.DOC);
        exportFormats.put(ReportOutputType.PDF, ExportFormat.PDF);
    }

    private ReportHelper() {
    }

    public static void printReport(Report report, Map<String, Object> params) {
        ReportOutputType reportOutputType = report.getReportOutputType();
        Iterator iterator  = exportFormats.entrySet().iterator();
        boolean find = false;
        Map.Entry<ReportOutputType, ExportFormat> item = null;
        while (iterator.hasNext() && !find){
            item = (Map.Entry<ReportOutputType, ExportFormat>)iterator.next();
            find = item.getKey().equals(reportOutputType);
        }
        if (find)
            printReport(report, params, item.getKey(), item.getValue());
        /*
        if (ReportOutputType.XLS.equals(reportOutputType))
            printReport(report, params, ReportOutputType.XLS, ExportFormat.XLS);
        else if (ReportOutputType.DOC.equals(reportOutputType))
            printReport(report, params, ReportOutputType.DOC, ExportFormat.DOC);
        else if (ReportOutputType.PDF.equals(reportOutputType))
            printReport(report, params, ReportOutputType.PDF, ExportFormat.PDF);  
        */
    }

    private static void printReport(Report report, Map<String, Object> params, ReportOutputType reportOutputType, ExportFormat exportFormat) {
        try {
            ReportService srv = ServiceLocator.lookup(ReportService.NAME);
            byte[] byteArr = srv.createReport(report, reportOutputType, params);
            new WebExportDisplay().show(new ByteArrayDataProvider(byteArr), "report", exportFormat);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* This method ignores report's security settings*/
    @Deprecated
    public static void createRunReportWithSingleObjectButton(Button runReportButton, final Window frame, final Table table) {
        final String javaClassName = table.getDatasource().getMetaClass().getJavaClass().getCanonicalName();
        runReportButton.setAction(new AbstractAction(frame.getMessage("runReport")) {
            public void actionPerform(Component component) {
                final Entity entity = table.getSingleSelected();
                if (entity != null)
                    frame.openLookup("report$Report.run", new Window.Lookup.Handler() {

                        public void handleLookup(Collection items) {
                            if (items != null && items.size() > 0) {
                                Report report = (Report) items.iterator().next();
                                report = frame.getDsContext().getDataService().reload(report, "report.edit");
                                if (report != null) {
                                    if (report.getInputParameters() != null && report.getInputParameters().size() > 0) {
                                        HashMap<String, Object> params = new HashMap<String, Object>();
                                        params.put("report", report);
                                        params.put("entity", entity);
                                        frame.openWindow("report$inputParameters", WindowManager.OpenType.DIALOG, params);
                                    } else {
                                        ReportHelper.printReport(report, Collections.<String, Object>emptyMap());
                                    }
                                }
                            }
                        }
                    }, WindowManager.OpenType.DIALOG, Collections.<String, Object>singletonMap("javaClassName", javaClassName));
                else
                    frame.showNotification(MessageProvider.getMessage(ReportHelper.class, "notifications.noSelectedEntity"), IFrame.NotificationType.HUMANIZED);
            }
        });
    }

    public static AbstractAction createRunReportButton(String captionId, final Window window) {
        return new AbstractAction(captionId) {
            public void actionPerform(Component component) {
                final Map<String, Object> params = new HashMap<String, Object>();
                params.put("screen", window.getId());

                window.openLookup("report$Report.run", new Window.Lookup.Handler() {
                    public void handleLookup(Collection items) {
                        if (items != null && items.size() > 0) {
                            Report report = (Report) items.iterator().next();
                            report = window.getDsContext().getDataService().reload(report, "report.edit");
                            if (report != null) {
                                if (report.getInputParameters() != null && report.getInputParameters().size() > 0) {
                                    window.openWindow("report$inputParameters", WindowManager.OpenType.DIALOG, Collections.<String, Object>singletonMap("report", report));
                                } else {
                                    ReportHelper.printReport(report, Collections.<String, Object>emptyMap());
                                }
                            }
                        }
                    }
                }, WindowManager.OpenType.DIALOG, params);
            }

            @Override
            public String getCaption() {
                return window.getMessage(getId());
            }
        };
    }

    public static AbstractAction createPrintformFromEditorAction(String captionId, final Window.Editor editor) {
        return new AbstractAction(captionId) {
            public void actionPerform(Component component) {
                final Entity entity = editor.getItem();
                if (entity != null) {
                    final String javaClassName = entity.getClass().getCanonicalName();
                    openRunReportScreen(editor, "entity", entity, javaClassName, ReportType.PRINT_FORM);
                } else
                    editor.showNotification(MessageProvider.getMessage(ReportHelper.class, "notifications.noSelectedEntity"), IFrame.NotificationType.HUMANIZED);
            }

            @Override
            public String getCaption() {
                return editor.getMessage(getId());
            }
        };
    }

    public static AbstractAction createPrintformFromTableAction(String captionId, final Window window, final Table table, final boolean multiObjects) {
        return new AbstractAction(captionId) {
            public void actionPerform(Component component) {
                Object selected = multiObjects ? table.getSelected() : table.getSingleSelected();
                if (selected != null && (!multiObjects || ((Collection) selected).size() > 0)) {
                    ReportType reportType = multiObjects ? ReportType.LIST_PRINT_FORM : ReportType.PRINT_FORM;
                    String paramName = multiObjects ? "entities" : "entity";
                    String javaClassName = multiObjects ? ((Collection) selected).iterator().next().getClass().getCanonicalName() : selected.getClass().getCanonicalName();

                    openRunReportScreen(window, paramName, selected, javaClassName, reportType);
                } else
                    window.showNotification(MessageProvider.getMessage(ReportHelper.class, "notifications.noSelectedEntity"), IFrame.NotificationType.HUMANIZED);
            }

            @Override
            public String getCaption() {
                return window.getMessage(getId());
            }
        };
    }

    private static void openRunReportScreen(final Window window, final String paramAlias, final Object paramValue, String javaClassName, ReportType reportType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("javaClassName", javaClassName);
        params.put("reportType", reportType.getId());
        params.put("screen", window.getId());

        window.openLookup("report$Report.run", new Window.Lookup.Handler() {
            public void handleLookup(Collection items) {
                if (items != null && items.size() > 0) {
                    Report report = (Report) items.iterator().next();
                    report = window.getDsContext().getDataService().reload(report, "report.edit");
                    ReportHelper.printReport(report, Collections.<String, Object>singletonMap(paramAlias, paramValue));
                }
            }
        }, WindowManager.OpenType.DIALOG, params);
    }

}
