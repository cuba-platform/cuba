/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 01.06.2010 13:16:03
 *
 * $Id$
 */
package com.haulmont.cuba.gui.report;

import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.export.ReportPrintHelper;
import com.haulmont.cuba.gui.report.actions.EditorPrintFormAction;
import com.haulmont.cuba.gui.report.actions.RunReportAction;
import com.haulmont.cuba.gui.report.actions.TablePrintFormAction;
import com.haulmont.cuba.report.Report;
import com.haulmont.cuba.report.ReportInputParameter;
import com.haulmont.cuba.report.ReportOutputDocument;
import com.haulmont.cuba.report.ReportScreen;
import com.haulmont.cuba.report.app.ReportService;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

@SuppressWarnings({"serial"})
public class ReportHelper {

    private ReportHelper() {
    }

    public static void runReport(Report report, Window window) {
        if (report != null) {
            if (report.getInputParameters() != null && report.getInputParameters().size() > 0) {
                openReportParamsDialog(report, window);
            } else {
                printReport(report, Collections.<String, Object>emptyMap());
            }
        }
    }

    private static void openReportParamsDialog(Report report, Window window) {
        window.openWindow("report$inputParameters", WindowManager.OpenType.DIALOG,
                Collections.<String, Object>singletonMap("report", report));
    }

    public static void runReport(Report report, Window window, final String paramAlias, final Object paramValue) {
        runReport(report, window, paramAlias, paramValue, null);
    }

    public static void runReport(Report report, Window window, final String paramAlias, final Object paramValue, @Nullable final String name) {
        if (report != null) {
            List<ReportInputParameter> params = report.getInputParameters();
            if (params != null && params.size() > 1) {
                openReportParamsDialog(report, window);
            } else {
                if (params != null && params.size() == 1) {
                    if (name == null)
                        printReport(report,
                                Collections.<String, Object>singletonMap(paramAlias, paramValue));
                    else
                        printReport(report, Collections.<String, Object>singletonMap(paramAlias, paramValue));
                } else {
                    if (name == null)
                        printReport(report, Collections.<String, Object>emptyMap());
                    else
                        printReport(report, Collections.<String, Object>emptyMap());
                }
            }
        }
    }

    public static void printReport(Report report, String defaultOutputFileName, Map<String, Object> params) {
        printReport(report, "", defaultOutputFileName, params);
    }

    public static void printReport(Report report, Map<String, Object> params) {
        printReport(report, report.getName(), params);
    }

    public static void printReport(Report report, String templateCode, String defaultOutputFileName, Map<String, Object> params) {
        try {
            if (StringUtils.isBlank(defaultOutputFileName))
                defaultOutputFileName = report.getName();

            ReportService srv = ServiceLocator.lookup(ReportService.NAME);

            ReportOutputDocument document;
            if (StringUtils.isEmpty(templateCode))
                document = srv.createReport(report, params);
            else
                document = srv.createReport(report, templateCode, params);

            byte[] byteArr = document.getContent();
            ExportFormat exportFormat = ReportPrintHelper.getExportFormat(document.getOutputType());

            ExportDisplay exportDisplay = AppConfig.createExportDisplay();
            String documentName = document.getDocumentName();
            exportDisplay.show(new ByteArrayDataProvider(byteArr), StringUtils.isNotBlank(documentName) ? documentName : defaultOutputFileName, exportFormat);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create RunReport action
     * @deprecated Use directly {@link RunReportAction}
     * @param captionId Action id
     * @param window Window
     * @return Action
     */
    @Deprecated
    public static AbstractAction createRunReportButton(String captionId, final Window window) {
        return new RunReportAction(window, captionId);
    }

    /**
     * Create Printform action for editor
     * @deprecated Use directly {@link EditorPrintFormAction}
     * @param captionId Action id
     * @param editor Editor window
     * @return Action
     */
    @Deprecated
    public static AbstractAction createPrintformFromEditorAction(String captionId, final Window.Editor editor) {
        return createPrintformFromEditorAction(captionId, editor, null);
    }

    /**
     * Create PrintForm action for editor
     * @deprecated Use directly {@link EditorPrintFormAction}
     * @param captionId Action id
     * @param editor Editor window
     * @param name Report name
     * @return Action
     */
    @Deprecated
    public static AbstractAction createPrintformFromEditorAction(String captionId, final Window.Editor editor,
                                                                 @Nullable final String name) {
        return new EditorPrintFormAction(captionId, editor, name);
    }

    /**
     * Create PrintForm action for table
     * @deprecated Use directly {@link TablePrintFormAction}
     * @param captionId Action id
     * @param window Window
     * @param table Table
     * @param multiObjects Multiple objects support
     * @return Action
     */
    @Deprecated
    public static AbstractAction createPrintformFromTableAction(String captionId, final Window window,
                                                                final Table table, final boolean multiObjects) {
        return new TablePrintFormAction(captionId, window, table, multiObjects);
    }

    private static List<Report> checkRoles(User user, List<Report> reports) {
        List<Report> filter = new ArrayList<Report>();
        for (Report report : reports) {
            List<Role> reportRoles = report.getRoles();
            if (reportRoles == null || reportRoles.size() == 0) {
                filter.add(report);
            } else {
                Set<UserRole> userRoles = user.getUserRoles();
                for (UserRole userRole : userRoles) {
                    if (reportRoles.contains(userRole.getRole()) ||
                            Boolean.TRUE.equals(userRole.getRole().getSuperRole())) {
                        filter.add(report);
                        break;
                    }
                }
            }
        }
        return filter;
    }

    private static List<Report> checkScreens(User user, List<Report> reports, String screen) {
        List<Report> filter = new ArrayList<Report>();
        for (Report report : reports) {
            List<ReportScreen> reportScreens = report.getReportScreens();
            List<String> reportScreensAliases = new ArrayList<String>();
            for (ReportScreen reportScreen : reportScreens) {
                reportScreensAliases.add(reportScreen.getScreenId());
            }

            if ((reportScreensAliases.contains(screen) || reportScreensAliases.size() == 0))
                filter.add(report);
        }
        return filter;
    }

    public static List<Report> applySecurityPolicies(User user, String screen, List<Report> reports) {
        List<Report> filter = checkRoles(user, reports);
        filter = checkScreens(user, filter, screen);
        return filter;
    }
}