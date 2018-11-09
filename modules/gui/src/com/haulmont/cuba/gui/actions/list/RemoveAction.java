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
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.components.ActionType;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.components.data.meta.EntityDataUnit;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.Nested;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.screen.ScreenContext;
import com.haulmont.cuba.security.entity.EntityOp;

import javax.inject.Inject;

import static com.haulmont.cuba.gui.screen.UiControllerUtils.getScreenContext;
import static com.haulmont.cuba.gui.screen.UiControllerUtils.getScreenData;

@ActionType(RemoveAction.ID)
public class RemoveAction extends SecuredListAction {

    public static final String ID = "remove";

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
        if (target == null || !(target.getItems() instanceof EntityDataUnit)) {
            return false;
        }

        if (!checkRemovePermission()) {
            return false;
        }

        return super.isPermitted();
    }

    protected boolean checkRemovePermission() {
        MetaClass metaClass = ((EntityDataUnit) target.getItems()).getEntityMetaClass();
        if (metaClass == null) {
            return true;
        }

        boolean entityOpPermitted = security.isEntityOpPermitted(metaClass, EntityOp.DELETE);
        if (!entityOpPermitted) {
            return false;
        }

        // todo support nested properties or use separate action ?

        return true;
    }

    @Override
    public void actionPerform(Component component) {
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            if (!(target.getItems() instanceof EntityDataUnit)) {
                throw new IllegalStateException("RemoveAction target dataSource is null or does not implement EntityDataUnit");
            }
            if (!(target.getItems() instanceof ContainerDataUnit)) {
                throw new IllegalStateException("RemoveAction target dataSource is null or does not implement ContainerDataUnit");
            }

            CollectionContainer container = ((ContainerDataUnit) target.getItems()).getContainer();
            if (container == null) {
                throw new IllegalStateException("RemoveAction target is not bound to CollectionContainer");
            }

            MetaClass metaClass = ((EntityDataUnit) target.getItems()).getEntityMetaClass();
            if (metaClass == null) {
                throw new IllegalStateException("Target is not bound to entity");
            }

            Entity entityToRemove = target.getSingleSelected();
            if (entityToRemove == null) {
                throw new IllegalStateException("There is not selected item in EditAction target");
            }

            Window window = ComponentsHelper.getWindowNN(target);
            ScreenData screenData = getScreenData(window.getFrameOwner());
            ScreenContext screenContext = getScreenContext(window.getFrameOwner());

            Dialogs dialogs = screenContext.getDialogs();

            dialogs.createOptionDialog()
                    .withCaption(messages.getMainMessage("dialogs.Confirmation"))
                    .withMessage(messages.getMainMessage("dialogs.Confirmation.Remove"))
                    .withActions(
                            new DialogAction(Type.YES).withHandler(e -> {
                                container.getMutableItems().remove(entityToRemove);
                                screenData.getDataContext().remove(entityToRemove);
                                commitIfNeeded(container, screenData);

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

    protected void commitIfNeeded(CollectionContainer container, ScreenData screenData) {
        boolean needCommit = true;
        if (container instanceof Nested) {
            InstanceContainer parentContainer = ((Nested) container).getParent();
            String property = ((Nested) container).getProperty();

            MetaClass parentMetaClass = parentContainer.getEntityMetaClass();
            MetaProperty metaProperty = parentMetaClass.getPropertyNN(property);

            needCommit = metaProperty.getType() != MetaProperty.Type.COMPOSITION;
        }
        if (needCommit) {
            screenData.getDataContext().commit();
        }
    }
}