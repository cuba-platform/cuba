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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.export.ReportPrintHelper;
import com.haulmont.cuba.report.*;
import com.haulmont.cuba.report.app.ReportService;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

@SuppressWarnings({"serial", "unused"})
public class ReportHelper {
    private ReportHelper() {
    }

    public static void runReport(Report report, Window window) {
        if (report != null) {
            if (report.getInputParameters() != null && report.getInputParameters().size() > 0) {
                openReportParamsDialog(report, window);
            } else {
                ReportHelper.printReport(report, Collections.<String, Object>emptyMap());
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
                        ReportHelper.printReport(report,
                                Collections.<String, Object>singletonMap(paramAlias, paramValue));
                    else
                        ReportHelper.printReport(report, Collections.<String, Object>singletonMap(paramAlias, paramValue));
                } else {
                    if (name == null)
                        ReportHelper.printReport(report, Collections.<String, Object>emptyMap());
                    else
                        ReportHelper.printReport(report, Collections.<String, Object>emptyMap());
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
                                    openReportParamsDialog(report, window);
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
        return createPrintformFromEditorAction(captionId, editor, null);
    }

    public static AbstractAction createPrintformFromEditorAction(String captionId, final Window.Editor editor,
                                                                 @Nullable final String name) {
        return new AbstractAction(captionId) {

            @Override
            public void actionPerform(Component component) {
                final Entity entity = editor.getItem();
                if (entity != null) {
                    final String javaClassName = entity.getClass().getCanonicalName();
                    openRunReportScreen(editor, "entity", entity, javaClassName, ReportType.PRINT_FORM, name);
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

    private static void openRunReportScreen(final Window window, final String paramAlias, final Object paramValue,
                                            String javaClassName, ReportType reportType) {
        openRunReportScreen(window, paramAlias, paramValue, javaClassName, reportType, null);
    }

    private static void openRunReportScreen(final Window window, final String paramAlias, final Object paramValue,
                                            String javaClassName, ReportType reportType, @Nullable final String name) {
        Map<String, Object> params = new HashMap<String, Object>();

        String metaClass;
        try {
            metaClass = MetadataProvider.getSession().getClass(Class.forName(javaClassName)).getName();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        params.put("entityMetaClass", metaClass);
        params.put("reportType", reportType.getId());
        params.put("screen", window.getId());

        if (checkReportsForStart(window, paramAlias, paramValue, javaClassName, reportType, name)) {
            window.openLookup("report$Report.run", new Window.Lookup.Handler() {

                @Override
                public void handleLookup(Collection items) {
                    if (items != null && items.size() > 0) {
                        Report report = (Report) items.iterator().next();
                        report = window.getDsContext().getDataService().reload(report, "report.edit");
                        runReport(report, window, paramAlias, paramValue, name);
                    }
                }
            }, WindowManager.OpenType.DIALOG, params);
        }
    }

    private static boolean checkReportsForStart(final Window window, final String paramAlias, final Object paramValue,
                                                String javaClassName, ReportType reportType) {
        return checkReportsForStart(window, paramAlias, paramValue, javaClassName, reportType, null);
    }

    private static boolean checkReportsForStart(final Window window, final String paramAlias, final Object paramValue,
                                                String javaClassName, ReportType reportType, @Nullable final String name) {
        Collection<MetaClass> metaClasses = MetadataHelper.getAllMetaClasses();
        String metaClassName = "";
        Iterator<MetaClass> iterator = metaClasses.iterator();
        while (iterator.hasNext() && ("".equals(metaClassName))) {
            MetaClass metaClass = iterator.next();
            if (metaClass.getJavaClass().getCanonicalName().equals(javaClassName))
                metaClassName = metaClass.getName();
        }

        boolean result = false;
        LoadContext lContext = new LoadContext(Report.class);
        lContext.setView("report.edit");
        String queryStr = "select r from report$Report r left join r.inputParameters ip where " +
                "(ip.entityMetaClass like :param$entityMetaClass or :param$entityMetaClass is null) " +
                " and (r.reportType = :param$reportType)";
        LoadContext.Query query = new LoadContext.Query(queryStr);
        query.addParameter("param$entityMetaClass", metaClassName);
        query.addParameter("param$reportType", reportType);
        lContext.setQuery(query);

        DsContext dsContext = window.getDsContext();
        List<Report> reports = dsContext.getDataService().loadList(lContext);
        reports = applySecurityPolicies(UserSessionProvider.getUserSession().getUser(), window.getId(), reports);
        if (reports.size() == 1) {
            Report report = reports.get(0);
            window.getDsContext().getDataService().reload(report, "report.edit");
            runReport(report, window, paramAlias, paramValue, name);
        } else if (reports.size() == 0) {
            String msg = MessageProvider.getMessage(ReportHelper.class, "report.notFoundReports");
            window.showNotification(msg, IFrame.NotificationType.HUMANIZED);
        } else
            result = true;
        return result;
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