/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.report.actions;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.report.ReportHelper;
import com.haulmont.cuba.report.*;

import javax.annotation.Nullable;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class EditorPrintFormAction extends AbstractPrintFormAction {

    private final Window.Editor editor;
    private final String name;

    private final static String DEFAULT_ACTION_ID = "editorPrintForm";

    public EditorPrintFormAction(Window.Editor editor, String name) {
        this(DEFAULT_ACTION_ID, editor, name);
    }

    public EditorPrintFormAction(String captionId, Window.Editor editor, @Nullable final String name) {
        super(captionId);
        this.editor = editor;
        this.name = name;
    }

    @Override
    public void actionPerform(Component component) {
        final Entity entity = editor.getItem();
        if (entity != null) {
            final String javaClassName = entity.getClass().getCanonicalName();
            openRunReportScreen(editor, ENTITY_SPECIAL_KEY, entity, javaClassName, ReportType.PRINT_FORM, name);
        } else
            editor.showNotification(MessageProvider.getMessage(ReportHelper.class, "notifications.noSelectedEntity"),
                    IFrame.NotificationType.HUMANIZED);

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
        }
        super.handleReportLookup(report, window, paramAlias, paramValue, name);
    }

    @Override
    public String getCaption() {
        final String messagesPackage = AppConfig.getMessagesPack();
        return MessageProvider.getMessage(messagesPackage, "actions.Report");
    }
}