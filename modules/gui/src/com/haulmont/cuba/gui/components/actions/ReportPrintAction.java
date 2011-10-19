/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components.actions;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ReportPrintHelper;
import com.haulmont.cuba.report.*;
import com.haulmont.cuba.report.app.ReportService;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Print action for filtered tables
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class ReportPrintAction extends AbstractAction {

    public static final String ACTION_ID = "filterPrint";
    private static final String REPORT_LOADING_VIEW = "report.edit";

    protected final CollectionDatasource collectionDatasource;
    protected final ExportDisplay display;
    protected final String reportId;
    protected final String templateId;
    protected final String defaultName;

    public ReportPrintAction(CollectionDatasource collectionDatasource, String reportId) {
        this(collectionDatasource, reportId, null, null);
    }

    public ReportPrintAction(CollectionDatasource collectionDatasource, String reportId, String defaultName) {
        this(collectionDatasource, reportId, null, defaultName);
    }

    public ReportPrintAction(CollectionDatasource collectionDatasource, String reportId, @Nullable String templateId, @Nullable String defaultName) {
        this(collectionDatasource, AppConfig.createExportDisplay(), reportId, templateId, defaultName, ACTION_ID);
    }

    public ReportPrintAction(CollectionDatasource collectionDatasource, ExportDisplay display, String reportId,
                             @Nullable String templateId, String defaultName, String id) {
        super(id);

        checkState(StringUtils.isNotEmpty(reportId));
        checkNotNull(collectionDatasource);

        this.defaultName = defaultName;
        this.reportId = reportId;
        this.templateId = templateId;
        this.collectionDatasource = collectionDatasource;
        if (display != null)
            this.display = display;
        else
            this.display = AppConfig.createExportDisplay();
    }

    @Override
    public void actionPerform(Component component) {
        // Get load context for datasource
        LoadContext loadContext = collectionDatasource.getCompiledLoadContext();
        if (loadContext == null)
            throw new RuntimeException("Null load context from collection datasource");

        // Get Report by Id
        Report report = loadReport(collectionDatasource.getDataService());

        if (report == null)
            throw new RuntimeException("Couldn't found report : " + reportId);

        // Find dataset for type QUERY
        DataSet queryDataSet = findQueryDataSet(report.getRootBandDefinition());

        if (queryDataSet == null)
            throw new RuntimeException("No such dataset with type QUERY in report");

        // Fill params
        Map<String, Object> params = prepareParams(loadContext, queryDataSet);

        // Print
        printReport(report, params);
    }

    private void printReport(Report report, Map<String, Object> params) {
        ReportService reportService = ServiceLocator.lookup(ReportService.NAME);
        try {
            ReportOutputDocument document;
            if (templateId != null) {
                document = reportService.createReport(report, templateId, params);
            } else {
                document = reportService.createReport(report, params);
            }

            String documentName;
            if (defaultName != null)
                documentName = defaultName;
            else
                documentName = document.getDocumentName();

            display.show(
                    new ByteArrayDataProvider(document.getContent()),
                    StringUtils.isNotBlank(documentName) ? documentName : report.getName(),
                    ReportPrintHelper.getExportFormat(document.getOutputType()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> prepareParams(LoadContext loadContext, DataSet queryDataSet) {
        Map<String, Object> params = new HashMap<String, Object>();
        LoadContext.Query query = loadContext.getQuery();

        params.put(queryDataSet.getQueryParamName(), query.getQueryString());
        params.put(queryDataSet.getViewParamName(), loadContext.getView().getName());

        String metaClassName = loadContext.getMetaClass();
        Class loadClass = MetadataProvider.getReplacedClass(metaClassName);
        params.put(queryDataSet.getEntityClassParamName(), loadClass.getCanonicalName());

        Map<String, Object> queryParams = query.getParameters();
        params.put(queryDataSet.getQueryParamName() + ".params", queryParams);
        return params;
    }

    private DataSet findQueryDataSet(BandDefinition bandDefinition) {
        if (bandDefinition == null)
            return null;

        if (bandDefinition.getDataSets() == null)
            return null;

        for (DataSet ds : bandDefinition.getDataSets()) {
            if (ds.getType() == DataSetType.QUERY)
                return ds;
        }

        for (BandDefinition child : bandDefinition.getChildrenBandDefinitions()) {
            DataSet queryDataSet = findQueryDataSet(child);
            if (queryDataSet != null)
                return queryDataSet;
        }

        return null;
    }

    private Report loadReport(DataService dataService) {
        MetaClass reportMetaClass = MetadataProvider.getSession().getClass(Report.class);
        LoadContext reportLoadContext = new LoadContext(reportMetaClass);
        reportLoadContext.setView(REPORT_LOADING_VIEW);
        reportLoadContext.setId(UUID.fromString(reportId));

        return dataService.load(reportLoadContext);
    }

    @Override
    public String getCaption() {
        final String messagesPackage = AppConfig.getMessagesPack();
        return MessageProvider.getMessage(messagesPackage, "actions.filterPrint");
    }
}
