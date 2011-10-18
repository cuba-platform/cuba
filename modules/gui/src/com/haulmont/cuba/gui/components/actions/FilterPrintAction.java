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
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
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
public class FilterPrintAction extends AbstractAction {

    static HashMap<ReportOutputType, ExportFormat> exportFormats = new HashMap<ReportOutputType, ExportFormat>();

    static {
        exportFormats.put(ReportOutputType.XLS, ExportFormat.XLS);
        exportFormats.put(ReportOutputType.DOC, ExportFormat.DOC);
        exportFormats.put(ReportOutputType.PDF, ExportFormat.PDF);
        exportFormats.put(ReportOutputType.HTML, ExportFormat.HTML);
    }

    public static final String ACTION_ID = "filterPrint";
    private static final String REPORT_LOADING_VIEW = "report.edit";

    protected final Table table;
    protected final ExportDisplay display;
    protected final String reportId;
    protected final String templateId;
    protected final String defaultName;

    public FilterPrintAction(Table table, String reportId) {
        this(table, reportId, null, null);
    }

    public FilterPrintAction(Table table, String reportId, String defaultName) {
        this(table, reportId, null, defaultName);
    }

    public FilterPrintAction(Table table, String reportId, @Nullable String templateId, @Nullable String defaultName) {
        this(table, AppConfig.createExportDisplay(), reportId, templateId, defaultName, ACTION_ID);
    }

    public FilterPrintAction(Table table, ExportDisplay display, String reportId,
                             @Nullable String templateId, String defaultName, String id) {
        super(id);

        checkState(StringUtils.isNotEmpty(reportId));

        this.defaultName = defaultName;
        this.reportId = reportId;
        this.templateId = templateId;
        this.table = table;
        if (display != null)
            this.display = display;
        else
            this.display = AppConfig.createExportDisplay();
    }

    @Override
    public void actionPerform(Component component) {
        CollectionDatasource datasource = table.getDatasource();
        checkNotNull(datasource);

        // Get load context for datasource
        LoadContext loadContext = datasource.getCompiledLoadContext();
        if (loadContext == null)
            throw new RuntimeException("Null load context from collection datasource");

        // Get Report by Id
        Report report = loadReport(datasource.getDataService());

        if (report == null)
            throw new RuntimeException("Couldn't found report : " + reportId);

        // Find dataset for type QUERY
        DataSet queryDataSet = findQueryDataSet(report.getRootBandDefinition());

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
                    exportFormats.get(document.getOutputType()));

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
