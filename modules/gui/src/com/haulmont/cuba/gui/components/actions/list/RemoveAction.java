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

package com.haulmont.cuba.gui.components.actions.list;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.screen.UiControllerUtils;

import javax.inject.Inject;

@ActionType(RemoveAction.ID)
public class RemoveAction extends SecuredListAction {

    public static final String ID = "entity_remove";

    protected Messages messages;

    public RemoveAction() {
        super(ID);
    }

    public RemoveAction(String id) {
        super(id);
    }

    @Inject
    protected void setIcons(Icons icons) {
        this.icon = icons.get(CubaIcon.REMOVE_ACTION);
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.messages = messages;
        this.caption = messages.getMainMessage("actions.Remove");
    }

    @Inject
    protected void setConfiguration(Configuration configuration) {
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        setShortcut(clientConfig.getTableRemoveShortcut());
    }

    @Override
    protected boolean isPermitted() {
        return super.isPermitted();
    }

    @Override
    public void setTarget(ListComponent target) {
        if (target != null
                && !(target instanceof SupportsEntityBinding)) {
            throw new IllegalStateException("RemoveAction target does not implement SupportsEntityBinding");
        }

        super.setTarget(target);
    }

    @Override
    public void actionPerform(Component component) {
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            if (!(target instanceof SupportsEntityBinding)) {
                throw new IllegalStateException("RemoveAction target is null or does not implement SupportsEntityBinding");
            }
            if (!(target instanceof SupportsContainerBinding)) {
                throw new IllegalStateException("RemoveAction target is null or does not implement SupportsContainerBinding");
            }

            CollectionContainer container = ((SupportsContainerBinding) target).getBindingContainer();
            if (container == null) {
                throw new IllegalStateException("RemoveAction target is not bound to CollectionContainer");
            }

            MetaClass metaClass = ((SupportsEntityBinding) target).getBindingMetaClass();
            if (metaClass == null) {
                throw new IllegalStateException("Target is not bound to entity");
            }

            Entity entityForRemove = target.getSingleSelected();
            if (entityForRemove == null) {
                throw new IllegalStateException("There is not selected item in EditAction target");
            }

            Window window = ComponentsHelper.getWindowNN(target);
            ScreenData screenData = UiControllerUtils.getScreenData(window.getFrameOwner());
            Dialogs dialogs = window.getScreenContext().getDialogs();

            dialogs.createOptionDialog()
                    .setCaption(messages.getMainMessage("dialogs.Confirmation"))
                    .setMessage(messages.getMainMessage("dialogs.Confirmation.Remove"))
                    .setActions(
                            new DialogAction(Type.YES).withHandler(e -> {
                                container.getMutableItems().remove(entityForRemove);
                                screenData.getDataContext().remove(entityForRemove);
                                screenData.getDataContext().commit();

                                if (target instanceof Component.Focusable) {
                                    ((Component.Focusable) target).focus();
                                }
                            }),
                            new DialogAction(Type.NO).withHandler(e -> {
                                if (target instanceof Component.Focusable) {
                                    ((Component.Focusable) target).focus();
                                }
                            })
                    )
                    .show();
        } else {
            super.actionPerform(component);
        }
    }
}