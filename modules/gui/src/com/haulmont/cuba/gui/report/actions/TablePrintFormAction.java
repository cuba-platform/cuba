/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.report.actions;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.report.ReportHelper;
import com.haulmont.cuba.report.*;

import java.util.Collection;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class TablePrintFormAction extends AbstractPrintFormAction {
    private final Window window;
    private final Table table;
    private final boolean multiObjects;

    private final static String DEFAULT_ACTION_ID = "tablePrintForm";

    protected final static String ENTITIES_LIST_SPECIAL_KEY = "entities_special_key";

    public TablePrintFormAction(Window window, Table table, final boolean multiObjects) {
        this(DEFAULT_ACTION_ID, window, table, multiObjects);
    }

    public TablePrintFormAction(String captionId, final Window window,
                                final Table table, final boolean multiObjects) {
        super(captionId);
        this.window = window;
        this.table = table;
        this.multiObjects = multiObjects;
    }

    @Override
    public void actionPerform(Component component) {
        final Object selected = multiObjects ? table.getSelected() : table.getSingleSelected();
        if (selected != null && (!multiObjects || ((Collection) selected).size() > 0)) {

            Action printSelectedAction = new AbstractAction("actions.printSelected") {
                @Override
                public void actionPerform(Component component) {
                    printSelected(selected);
                }

                @Override
                public String getIcon() {
                    return "icons/doc.png";
                }
            };

            Action printAllAction = new AbstractAction("actions.printAll") {
                @Override
                public void actionPerform(Component component) {
                    printAll();
                }

                @Override
                public String getIcon() {
                    return "icons/documents.png";
                }
            };

            Action cancelAction = new DialogAction(DialogAction.Type.CANCEL);

            window.showOptionDialog(MessageProvider.getMessage(ReportHelper.class, "notifications.confirmPrintSelectedheader"),
                    MessageProvider.getMessage(ReportHelper.class, "notifications.confirmPrintSelected"),
                    IFrame.MessageType.CONFIRMATION,
                    new Action[]{printAllAction, printSelectedAction, cancelAction});
        } else {

            if ((table.getDatasource().getState() == Datasource.State.VALID) &&
                    (table.getDatasource().getItemIds().size() > 0)) {
                Action yesAction = new DialogAction(DialogAction.Type.OK) {
                    @Override
                    public void actionPerform(Component component) {
                        printAll();
                    }
                };

                Action cancelAction = new DialogAction(DialogAction.Type.CANCEL);

                window.showOptionDialog(MessageProvider.getMessage(ReportHelper.class, "notifications.confirmPrintAllheader"),
                        MessageProvider.getMessage(ReportHelper.class, "notifications.confirmPrintAll"),
                        IFrame.MessageType.CONFIRMATION, new Action[]{yesAction, cancelAction});
            } else {
                window.showNotification(MessageProvider.getMessage(ReportHelper.class, "notifications.noSelectedEntity"),
                    IFrame.NotificationType.HUMANIZED);
            }
        }
    }

    @Override
    protected void handleReportLookup(Report report, Window window, String paramAlias, Object paramValue, String name) {
        if (ENTITY_SPECIAL_KEY.equals(paramAlias)) {
            DataSet singleDataSet = findDataSet(report.getRootBandDefinition(), DataSetType.SINGLE);
            if (singleDataSet == null)
                throw new IllegalStateException("Couldn't found single entity dataset in report");
            paramAlias = singleDataSet.getEntityParamName();
            if (paramValue instanceof ParameterPrototype) {
                ((ParameterPrototype) paramValue).setParamName(paramAlias);
            }
        } else if (ENTITIES_LIST_SPECIAL_KEY.equals(paramAlias)) {
            DataSet singleDataSet = findDataSet(report.getRootBandDefinition(), DataSetType.MULTI);
            if (singleDataSet == null)
                throw new IllegalStateException("Couldn't found multi entity dataset in report");
            paramAlias = singleDataSet.getListEntitiesParamName();
            if (paramValue instanceof ParameterPrototype) {
                ((ParameterPrototype) paramValue).setParamName(paramAlias);
            }
        }
        super.handleReportLookup(report, window, paramAlias, paramValue, name);
    }

    private void printSelected(Object selected) {
        ReportType reportType = multiObjects ? ReportType.LIST_PRINT_FORM : ReportType.PRINT_FORM;
        String paramKey = multiObjects ? ENTITIES_LIST_SPECIAL_KEY : ENTITY_SPECIAL_KEY;
        Class<?> selectedClass = ((Collection) selected).iterator().next().getClass();

        String javaClassName = multiObjects ? selectedClass.getCanonicalName() :
                selected.getClass().getCanonicalName();

        openRunReportScreen(window, paramKey, selected, javaClassName, reportType);
    }

    private void printAll() {
        ReportType reportType = multiObjects ? ReportType.LIST_PRINT_FORM : ReportType.PRINT_FORM;
        String paramKey = multiObjects ? ENTITIES_LIST_SPECIAL_KEY : ENTITY_SPECIAL_KEY;
        CollectionDatasource datasource = table.getDatasource();

        MetaClass metaClass = datasource.getMetaClass();
        String javaClassName = metaClass.getJavaClass().getCanonicalName();

        LoadContext loadContext = datasource.getCompiledLoadContext();

        ParameterPrototype parameterPrototype = new ParameterPrototype(paramKey);
        parameterPrototype.setMetaClassName(metaClass.getFullName());
        parameterPrototype.setQueryString(loadContext.getQuery().getQueryString());
        parameterPrototype.setQueryParams(loadContext.getQuery().getParameters());
        parameterPrototype.setViewName(loadContext.getView().getName());

        openRunReportScreen(window, paramKey, parameterPrototype, javaClassName, reportType);
    }

    @Override
    public String getCaption() {
        final String messagesPackage = AppConfig.getMessagesPack();
        return MessageProvider.getMessage(messagesPackage, "actions.Report");
    }
}