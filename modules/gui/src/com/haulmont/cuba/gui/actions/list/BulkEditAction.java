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
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.BulkEditors;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.ActionType;
import com.haulmont.cuba.gui.components.BulkEditor;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.data.meta.EntityDataUnit;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;

import javax.inject.Inject;

import static com.haulmont.cuba.gui.ComponentsHelper.getScreenContext;

@ActionType(BulkEditAction.ID)
public class BulkEditAction extends SecuredListAction {

    public static final String ID = "bulkEdit";

    protected Messages messages;

    protected BulkEditors bulkEditors;

    public BulkEditAction() {
        this(ID);
    }

    public BulkEditAction(String id) {
        super(id);
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

    @SuppressWarnings("unchecked")
    @Override
    public void actionPerform(Component component) {
        // if standard behaviour
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
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
                notifications.create()
                        .setCaption(messages.getMainMessage("accessDenied.message"))
                        .setType(Notifications.NotificationType.ERROR)
                        .show();
                return;
            }

            if (target.getSelected().isEmpty()) {
                Notifications notifications = getScreenContext(target.getFrame()).getNotifications();
                notifications.create()
                        .setCaption(messages.getMainMessage("actions.BulkEdit.emptySelection"))
                        .setType(Notifications.NotificationType.HUMANIZED)
                        .show();
                return;
            }

            Window window = ComponentsHelper.getWindowNN(target);

            bulkEditors.builder(metaClass, target.getSelected(), window.getFrameOwner())
                    .withListComponent(target)
                    .create()
                    .show();
        } else {
            super.actionPerform(component);
        }
    }
}
