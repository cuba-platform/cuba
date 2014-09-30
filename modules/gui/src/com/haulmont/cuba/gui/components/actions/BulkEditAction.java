/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class BulkEditAction extends ItemTrackingAction {

    protected ListComponent owner;
    protected WindowManager.OpenType openType = WindowManager.OpenType.DIALOG;
    protected String exclude;
    protected Map<String, Field.Validator> fieldValidators;
    protected List<Field.Validator> moduleValidators;

    public BulkEditAction(ListComponent owner) {
        super("bulkEdit");

        this.owner = owner;
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

    public List<Field.Validator> getModuleValidators() {
        return moduleValidators;
    }

    public void setModuleValidators(List<Field.Validator> moduleValidators) {
        this.moduleValidators = moduleValidators;
    }

    public Map<String, Field.Validator> getFieldValidators() {
        return fieldValidators;
    }

    public void setFieldValidators(Map<String, Field.Validator> fieldValidators) {
        this.fieldValidators = fieldValidators;
    }

    @Override
    public void refreshState() {
        super.refreshState();

        CollectionDatasource ds = owner.getDatasource();
        if (ds != null) {
            updateApplicableTo(isApplicableTo(ds.getState(), ds.getState() == Datasource.State.VALID ? ds.getItem() : null));
        }
    }

    @Override
    public void actionPerform(Component component) {
        if (!userSession.isSpecificPermitted(BulkEditor.PERMISSION)) {
            owner.getFrame().showNotification(messages.getMainMessage("accessDenied.message"), IFrame.NotificationType.ERROR);
            return;
        }

        if (owner.getSelected().isEmpty()) {
            owner.getFrame().showNotification(messages.getMainMessage("actions.BulkEdit.emptySelection"),
                    IFrame.NotificationType.HUMANIZED);
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("metaClass", owner.getDatasource().getMetaClass());
        params.put("selected", owner.getSelected());
        params.put("exclude", exclude);
        params.put("fieldValidators", fieldValidators);
        params.put("moduleValidators", moduleValidators);

        if (openType == WindowManager.OpenType.DIALOG) {
            ThemeConstantsManager themeManager = AppBeans.get(ThemeConstantsManager.NAME);
            ThemeConstants theme = themeManager.getConstants();

            owner.getFrame().getDialogParams()
                    .setWidth(theme.getInt("cuba.gui.BulkEditAction.editorDialog.width"))
                    .setHeight(theme.getInt("cuba.gui.BulkEditAction.editorDialog.height"))
                    .setResizable(true);
        }

        Window bulkEditor = owner.getFrame().openWindow("bulkEditor", openType, params);
        bulkEditor.addListener(new Window.CloseListener() {
            @Override
            public void windowClosed(String actionId) {
                if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                    owner.getDatasource().refresh();
                }
                owner.requestFocus();
            }
        });
    }
}