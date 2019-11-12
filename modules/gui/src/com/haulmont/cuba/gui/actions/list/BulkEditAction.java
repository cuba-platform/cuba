/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.gui.actions.list;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.BulkEditors;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.app.core.bulk.ColumnsMode;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.meta.EntityDataUnit;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.meta.PropertyType;
import com.haulmont.cuba.gui.meta.StudioAction;
import com.haulmont.cuba.gui.meta.StudioDelegate;
import com.haulmont.cuba.gui.meta.StudioPropertiesItem;
import com.haulmont.cuba.gui.screen.OpenMode;

import javax.annotation.Nullable;
import javax.inject.Inject;

import java.util.List;

import static com.haulmont.cuba.gui.ComponentsHelper.getScreenContext;

/**
 * Standard action for changing attribute values for several entity instances at once.
 * <p>
 * Should be defined for a list component ({@code Table}, {@code DataGrid}, etc.) in a screen XML descriptor.
 * <p>
 * The action instance can be parameterized using the nested {@code properties} XML element or programmatically in the
 * screen controller.
 */
@StudioAction(category = "List Actions", description = "Opens an editor for changing attribute values for several entity instances at once")
@ActionType(BulkEditAction.ID)
public class BulkEditAction extends SecuredListAction {

    public static final String ID = "bulkEdit";

    protected Messages messages;

    protected BulkEditors bulkEditors;

    protected ColumnsMode columnsMode;
    protected String exclude;
    protected BulkEditors.FieldSorter fieldSorter;
    protected List<String> includeProperties;
    protected OpenMode openMode;
    protected Boolean loadDynamicAttributes;
    protected Boolean useConfirmDialog;

    public BulkEditAction() {
        this(ID);
    }

    public BulkEditAction(String id) {
        super(id);
    }

    /**
     * Returns the columns mode which defines the number of columns either it was set by {@link #setColumnsMode(ColumnsMode)}
     * or in the screen XML. Otherwise returns null.
     */
    @Nullable
    public ColumnsMode getColumnsMode() {
        return columnsMode;
    }

    /**
     * Sets the columns mode which defines the number of columns.
     *
     * @see ColumnsMode#ONE_COLUMN
     * @see ColumnsMode#TWO_COLUMNS
     */
    @StudioPropertiesItem
    public void setColumnsMode(ColumnsMode columnsMode) {
        this.columnsMode = columnsMode;
    }

    /**
     * Returns a regular expression to exclude fields if it was set by {@link #setExclude(String)} or in the screen XML.
     * Otherwise returns null.
     */
    @Nullable
    public String getExclude() {
        return exclude;
    }

    /**
     * Sets a regular expression to exclude some fields explicitly
     * from the list of attributes available for editing.
     */
    @StudioPropertiesItem
    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

    /**
     * Sets field sorter that allows you to sort fields by custom logic.
     */
    @StudioDelegate
    public void setFieldSorter(BulkEditors.FieldSorter fieldSorter) {
        this.fieldSorter = fieldSorter;
    }

    /**
     * Returns a list entity attributes to be included to bulk editor window if it was set by {@link #setIncludeProperties(List)} or in the screen XML.
     * Otherwise returns null.
     */
    @Nullable
    public List<String> getIncludeProperties() {
        return includeProperties;
    }

    /**
     * Sets the entity attributes to be included to bulk editor window.
     * If set, other attributes will be ignored.
     */
    @StudioPropertiesItem(type = PropertyType.STRING)
    public void setIncludeProperties(List<String> includeProperties) {
        this.includeProperties = includeProperties;
    }

    /**
     * Returns the bulk editor screen open mode if it was set by {@link #setOpenMode(OpenMode)} or in the screen XML.
     * Otherwise returns null.
     */
    @Nullable
    public OpenMode getOpenMode() {
        return openMode;
    }

    /**
     * Sets the bulk editor screen open mode.
     */
    @StudioPropertiesItem
    public void setOpenMode(OpenMode openMode) {
        this.openMode = openMode;
    }

    /**
     * Returns true/false if the flag was set by {@link #setLoadDynamicAttributes(Boolean)} or in the screen XML.
     * Otherwise returns null.
     */
    @Nullable
    public Boolean getLoadDynamicAttributes() {
        return loadDynamicAttributes;
    }

    /**
     * Sets whether dynamic attributes of the edited entity should be displayed on
     * the entity's bulk editor screen. The default value is true.
     */
    @StudioPropertiesItem
    public void setLoadDynamicAttributes(Boolean loadDynamicAttributes) {
        this.loadDynamicAttributes = loadDynamicAttributes;
    }

    /**
     * Returns true/false if the flag was set by {@link #setUseConfirmDialog(Boolean)} or in the screen XML.
     * Otherwise returns null.
     */
    @Nullable
    public Boolean getUseConfirmDialog() {
        return useConfirmDialog;
    }

    /**
     * Sets whether or not the confirmation dialog should be displayed to
     * the user before saving the changes. The default value is true.
     */
    @StudioPropertiesItem
    public void setUseConfirmDialog(Boolean useConfirmDialog) {
        this.useConfirmDialog = useConfirmDialog;
    }

    @Inject
    protected void setIcons(Icons icons) {
        this.icon = icons.get(CubaIcon.BULK_EDIT_ACTION);
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.messages = messages;
        this.caption = messages.getMainMessage("actions.BulkEdit");
    }

    @Inject
    protected void setSecurity(Security security) {
        this.security = security;

        if (!security.isSpecificPermitted(BulkEditor.PERMISSION)) {
            setVisible(false);
            setEnabled(false);
        }
    }

    @Inject
    public void setBulkEditors(BulkEditors bulkEditors) {
        this.bulkEditors = bulkEditors;
    }

    @Override
    protected boolean isPermitted() {
        if (target == null || !(target.getItems() instanceof EntityDataUnit)) {
            return false;
        }

        MetaClass metaClass = ((EntityDataUnit) target.getItems()).getEntityMetaClass();
        if (metaClass == null) {
            return true;
        }

        boolean permitted = security.isScreenPermitted(BulkEditor.PERMISSION);
        if (!permitted) {
            return false;
        }

        return super.isPermitted();
    }

    @Override
    public void actionPerform(Component component) {
        // if standard behaviour
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            execute();
        } else {
            super.actionPerform(component);
        }
    }

    /**
     * Executes the action.
     */
    @SuppressWarnings("unchecked")
    public void execute() {
        if (!(target.getItems() instanceof EntityDataUnit)) {
            throw new IllegalStateException("BulkEditAction target Items is null " +
                    "or does not implement EntityDataUnit");
        }

        MetaClass metaClass = ((EntityDataUnit) target.getItems()).getEntityMetaClass();
        if (metaClass == null) {
            throw new IllegalStateException("Target is not bound to entity");
        }

        if (!security.isSpecificPermitted(BulkEditor.PERMISSION)) {
            Notifications notifications = getScreenContext(target.getFrame()).getNotifications();
            notifications.create(NotificationType.ERROR)
                    .withCaption(messages.getMainMessage("accessDenied.message"))
                    .show();
            return;
        }

        if (target.getSelected().isEmpty()) {
            Notifications notifications = getScreenContext(target.getFrame()).getNotifications();
            notifications.create(NotificationType.ERROR)
                    .withCaption(messages.getMainMessage("actions.BulkEdit.emptySelection"))
                    .show();
            return;
        }

        Window window = ComponentsHelper.getWindowNN(target);

        BulkEditors.EditorBuilder builder = bulkEditors.builder(metaClass, target.getSelected(), window.getFrameOwner())
                .withListComponent(target);

        if (columnsMode != null) {
            builder = builder.withColumnsMode(columnsMode);
        }

        if (exclude != null) {
            builder = builder.withExclude(exclude);
        }

        if (fieldSorter != null) {
            builder = builder.withFieldSorter(fieldSorter);
        }

        if (includeProperties != null) {
            builder = builder.withIncludeProperties(includeProperties);
        }

        if (openMode != null) {
            builder = builder.withLaunchMode(openMode);
        }

        if (loadDynamicAttributes != null) {
            builder = builder.withLoadDynamicAttributes(loadDynamicAttributes);
        }

        if (useConfirmDialog != null) {
            builder = builder.withUseConfirmDialog(useConfirmDialog);
        }

        builder.create().show();
    }
}
