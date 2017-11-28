/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.components.actions;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.WindowManager.OpenMode;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import org.springframework.context.annotation.Scope;

import java.util.List;
import java.util.Map;

/**
 * Action used in {@link BulkEditor} visual component.
 * <p>
 * In order to provide your own implementation globally, create a subclass and register it in {@code web-spring.xml},
 * for example:
 * <pre>
 * &lt;bean id="cuba_BulkEditAction" class="com.company.sample.gui.MyBulkEditAction" scope="prototype"/&gt;
 * </pre>
 */
@org.springframework.stereotype.Component("cuba_BulkEditAction")
@Scope("prototype")
public class BulkEditAction extends ItemTrackingAction implements Action.HasBeforeActionPerformedHandler  {

    protected OpenType openType = OpenType.DIALOG;
    protected String exclude;
    protected Map<String, Field.Validator> fieldValidators;
    protected List<Field.Validator> modelValidators;

    protected BeforeActionPerformedHandler beforeActionPerformedHandler;

    /**
     * Creates an action with default id.
     * @param target    component containing this action
     */
    public static BulkEditAction create(ListComponent target) {
        return AppBeans.getPrototype("cuba_BulkEditAction", target);
    }

    public BulkEditAction(ListComponent target) {
        super(target, "bulkEdit");

        this.icon = AppBeans.get(Icons.class).get(CubaIcon.BULK_EDIT_ACTION);
        this.caption = messages.getMessage(getClass(), "actions.BulkEdit");
        this.constraintOperationType = ConstraintOperationType.UPDATE;

        boolean permitted = userSession.isSpecificPermitted(BulkEditor.PERMISSION);

        setVisible(permitted);
        setEnabled(permitted);
    }

    public OpenType getOpenType() {
        return openType;
    }

    public void setOpenType(OpenType openType) {
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
        if (beforeActionPerformedHandler != null) {
            if (!beforeActionPerformedHandler.beforeActionPerformed())
                return;
        }

        if (!userSession.isSpecificPermitted(BulkEditor.PERMISSION)) {
            target.getFrame().showNotification(messages.getMainMessage("accessDenied.message"), Frame.NotificationType.ERROR);
            return;
        }

        if (target.getSelected().isEmpty()) {
            target.getFrame().showNotification(messages.getMainMessage("actions.BulkEdit.emptySelection"),
                    Frame.NotificationType.HUMANIZED);
            return;
        }

        if (openType.getOpenMode() == OpenMode.DIALOG) {
            ThemeConstantsManager themeManager = AppBeans.get(ThemeConstantsManager.NAME);
            ThemeConstants theme = themeManager.getConstants();

            target.getFrame().getDialogParams()
                    .setWidth(theme.get("cuba.gui.BulkEditAction.editorDialog.width"))
                    .setHeight(theme.get("cuba.gui.BulkEditAction.editorDialog.height"))
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
        bulkEditor.addCloseListener(actionId -> {
            if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                target.getDatasource().refresh();
            }
            target.requestFocus();
        });
    }

    @Override
    public BeforeActionPerformedHandler getBeforeActionPerformedHandler() {
        return beforeActionPerformedHandler;
    }

    @Override
    public void setBeforeActionPerformedHandler(BeforeActionPerformedHandler handler) {
        beforeActionPerformedHandler = handler;
    }
}