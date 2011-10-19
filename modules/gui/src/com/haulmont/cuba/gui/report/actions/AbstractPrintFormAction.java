/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.report.actions;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.report.ReportHelper;
import com.haulmont.cuba.report.*;

import javax.annotation.Nullable;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
abstract class AbstractPrintFormAction extends AbstractAction {

    protected static final String ENTITY_SPECIAL_KEY = "entity_special_key";

    protected AbstractPrintFormAction(String id) {
        super(id);
    }

    protected void openRunReportScreen(final Window window, final String paramAlias, final Object paramValue,
                                     String javaClassName, ReportType reportType) {
        openRunReportScreen(window, paramAlias, paramValue, javaClassName, reportType, null);
    }

    protected DataSet findDataSet(BandDefinition bandDefinition, DataSetType dsType) {
        if (bandDefinition == null)
            return null;

        if (bandDefinition.getDataSets() == null)
            return null;

        for (DataSet ds : bandDefinition.getDataSets()) {
            if (ds.getType() == dsType)
                return ds;
        }

        for (BandDefinition child : bandDefinition.getChildrenBandDefinitions()) {
            DataSet queryDataSet = findDataSet(child, dsType);
            if (queryDataSet != null)
                return queryDataSet;
        }

        return null;
    }

    protected void openRunReportScreen(final Window window, final String paramAlias, final Object paramValue,
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
                        handleReportLookup(report, window, paramAlias, paramValue, name);
                    }
                }
            }, WindowManager.OpenType.DIALOG, params);
        }
    }

    protected void handleReportLookup(Report report, Window window, String paramAlias, Object paramValue, String name) {
        ReportHelper.runReport(report, window, paramAlias, paramValue, name);
    }

    protected boolean checkReportsForStart(final Window window, final String paramAlias, final Object paramValue,
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
        reports = ReportHelper.applySecurityPolicies(UserSessionProvider.getUserSession().getUser(), window.getId(), reports);
        if (reports.size() == 1) {
            Report report = reports.get(0);
            window.getDsContext().getDataService().reload(report, "report.edit");
            ReportHelper.runReport(report, window, paramAlias, paramValue, name);
        } else if (reports.size() == 0) {
            String msg = MessageProvider.getMessage(ReportHelper.class, "report.notFoundReports");
            window.showNotification(msg, IFrame.NotificationType.HUMANIZED);
        } else
            result = true;
        return result;
    }
}