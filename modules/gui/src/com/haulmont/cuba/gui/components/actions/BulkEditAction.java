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
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.app.core.bulk.ColumnsMode;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.context.annotation.Scope;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.haulmont.cuba.gui.ComponentsHelper.getScreenContext;

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
public class BulkEditAction extends ItemTrackingAction implements Action.HasBeforeActionPerformedHandler {

    protected OpenType openType = OpenType.DIALOG;
    protected String exclude;
    protected List<String> includeProperties = Collections.emptyList();
    protected Map<String, Field.Validator> fieldValidators;
    protected List<Field.Validator> modelValidators;
    protected Boolean loadDynamicAttributes;
    protected Boolean useConfirmDialog;
    protected ColumnsMode columnsMode;

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

        Messages messages = AppBeans.get(Messages.NAME);
        this.caption = messages.getMessage(getClass(), "actions.BulkEdit");
        this.constraintOperationType = ConstraintOperationType.UPDATE;

        UserSession userSession = AppBeans.get(UserSessionSource.class).getUserSession();
        if (!userSession.isSpecificPermitted(BulkEditor.PERMISSION)) {
            setVisible(false);
            setEnabled(false);
        }
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

    public List<String> getIncludeProperties() {
        return includeProperties;
    }

    public void setIncludeProperties(List<String> includeProperties) {
        this.includeProperties = includeProperties;
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

    public Boolean getLoadDynamicAttributes() {
        return loadDynamicAttributes;
    }

    public void setLoadDynamicAttributes(Boolean loadDynamicAttribute) {
        this.loadDynamicAttributes = loadDynamicAttribute;
    }

    public void setUseConfirmDialog(Boolean useConfirmDialog) {
        this.useConfirmDialog = useConfirmDialog;
    }

    public Boolean getUseConfirmDialog() {
        return useConfirmDialog;
    }

    public ColumnsMode getColumnsMode() {
        return columnsMode;
    }

    public void setColumnsMode(ColumnsMode columnsMode) {
        this.columnsMode = columnsMode;
    }

    @Override
    public void actionPerform(Component component) {
        if (beforeActionPerformedHandler != null
                && !beforeActionPerformedHandler.beforeActionPerformed()) {
            return;
        }

        UserSession userSession = AppBeans.get(UserSessionSource.class).getUserSession();
        if (!userSession.isSpecificPermitted(BulkEditor.PERMISSION)) {
            Messages messages = AppBeans.get(Messages.NAME);

            Notifications notifications = getScreenContext(target.getFrame()).getNotifications();
            notifications.create(NotificationType.ERROR)
                    .withCaption(messages.getMainMessage("accessDenied.message"))
                    .show();
            return;
        }

        if (target.getSelected().isEmpty()) {
            Messages messages = AppBeans.get(Messages.NAME);

            Notifications notifications = getScreenContext(target.getFrame()).getNotifications();
            notifications.create(NotificationType.HUMANIZED)
                    .withCaption(messages.getMainMessage("actions.BulkEdit.emptySelection"))
                    .show();

            return;
        }

        OpenType openType = this.openType;

        if (openType.getOpenMode() == OpenMode.DIALOG) {
            ThemeConstantsManager themeManager = AppBeans.get(ThemeConstantsManager.NAME);
            ThemeConstants theme = themeManager.getConstants();

            openType = openType.copy()
                .width(theme.get("cuba.gui.BulkEditAction.editorDialog.width"))
                .height(theme.get("cuba.gui.BulkEditAction.editorDialog.height"))
                .resizable(true);
        }

        Map<String, Object> params = ParamsMap.of()
                .pair("metaClass", target.getDatasource().getMetaClass())
                .pair("selected", target.getSelected())
                .pair("exclude", exclude)
                .pair("includeProperties", includeProperties != null ? includeProperties : Collections.EMPTY_LIST)
                .pair("fieldValidators", fieldValidators)
                .pair("modelValidators", modelValidators)
                .pair("loadDynamicAttributes", loadDynamicAttributes)
                .pair("useConfirmDialog", useConfirmDialog)
                .pair("columnsMode", columnsMode)
                .create();

        WindowManager wm = ((WindowManager) getScreenContext(target.getFrame()).getScreens());
        WindowInfo windowInfo = AppBeans.get(WindowConfig.class).getWindowInfo("bulkEditor");

        Window bulkEditor = wm.openWindow(windowInfo, openType, params);
        bulkEditor.addCloseListener(actionId -> {
            if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                target.getDatasource().refresh();
            }
            if (target instanceof Component.Focusable) {
                ((Component.Focusable) target).focus();
            }
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