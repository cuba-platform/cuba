/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.actions;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;

import java.util.List;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class BulkEditAction extends ItemTrackingAction {

    protected WindowManager.OpenType openType = WindowManager.OpenType.DIALOG;
    protected String exclude;
    protected Map<String, Field.Validator> fieldValidators;
    protected List<Field.Validator> modelValidators;

    public BulkEditAction(ListComponent owner) {
        super(owner, "bulkEdit");

        this.icon = "icons/bulk-edit.png";
        this.caption = messages.getMessage(getClass(), "actions.BulkEdit");

        boolean permitted = userSession.isSpecificPermitted(BulkEditor.PERMISSION);

        setVisible(permitted);
        setEnabled(permitted);
    }

    public WindowManager.OpenType getOpenType() {
        return openType;
    }

    public void setOpenType(WindowManager.OpenType openType) {
        this.openType = openType;
    }

    public String getExcludePropertyRegex() {
        return exclude;
    }

    public void setExcludePropertyRegex(String exclude) {
        this.exclude = exclude;
    }

    public List<Field.Validator> getModelValidators() {
        return modelValidators;
    }

    public void setModelValidators(List<Field.Validator> modelValidators) {
        this.modelValidators = modelValidators;
    }

    public Map<String, Field.Validator> getFieldValidators() {
        return fieldValidators;
    }

    public void setFieldValidators(Map<String, Field.Validator> fieldValidators) {
        this.fieldValidators = fieldValidators;
    }

    @Override
    public void actionPerform(Component component) {
        if (!userSession.isSpecificPermitted(BulkEditor.PERMISSION)) {
            target.getFrame().showNotification(messages.getMainMessage("accessDenied.message"), IFrame.NotificationType.ERROR);
            return;
        }

        if (target.getSelected().isEmpty()) {
            target.getFrame().showNotification(messages.getMainMessage("actions.BulkEdit.emptySelection"),
                    IFrame.NotificationType.HUMANIZED);
            return;
        }

        if (openType == WindowManager.OpenType.DIALOG) {
            ThemeConstantsManager themeManager = AppBeans.get(ThemeConstantsManager.NAME);
            ThemeConstants theme = themeManager.getConstants();

            target.getFrame().getDialogParams()
                    .setWidth(theme.getInt("cuba.gui.BulkEditAction.editorDialog.width"))
                    .setHeight(theme.getInt("cuba.gui.BulkEditAction.editorDialog.height"))
                    .setResizable(true);
        }

        Map<String, Object> params = ParamsMap.of(
                "metaClass", target.getDatasource().getMetaClass(),
                "selected", target.getSelected(),
                "exclude", exclude,
                "fieldValidators", fieldValidators,
                "modelValidators", modelValidators
        );

        Window bulkEditor = target.getFrame().openWindow("bulkEditor", openType, params);
        bulkEditor.addListener(new Window.CloseListener() {
            @Override
            public void windowClosed(String actionId) {
                if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                    target.getDatasource().refresh();
                }
                target.requestFocus();
            }
        });
    }
}