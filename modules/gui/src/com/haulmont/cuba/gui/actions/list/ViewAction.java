/*
 * Copyright (c) 2008-2019 Haulmont.
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
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.ActionType;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.data.meta.EntityDataUnit;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.screen.ReadOnlyAwareScreen;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.security.entity.EntityOp;

import javax.inject.Inject;

/**
 * An action that opens an editor screen in the read-only mode.
 * <p>
 * The opening screen must implement the {@link ReadOnlyAwareScreen} interface.
 */
@ActionType(ViewAction.ID)
public class ViewAction extends SecuredListAction {

    public static final String ID = "view";

    protected Messages messages;
    protected ScreenBuilders screenBuilders;

    public ViewAction() {
        this(ID);
    }

    public ViewAction(String id) {
        super(id);
    }

    @Inject
    protected void setScreenBuilders(ScreenBuilders screenBuilders) {
        this.screenBuilders = screenBuilders;
    }

    @Inject
    protected void setIcons(Icons icons) {
        this.icon = icons.get(CubaIcon.VIEW_ACTION);
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.messages = messages;
        this.caption = messages.getMainMessage("actions.View");
    }

    @Inject
    protected void setConfiguration(Configuration configuration) {
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        setShortcut(clientConfig.getTableViewShortcut());
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

        boolean entityOpPermitted = security.isEntityOpPermitted(metaClass, EntityOp.READ);
        if (!entityOpPermitted) {
            return false;
        }

        return super.isPermitted();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void actionPerform(Component component) {
        // if standard behaviour
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            if (target == null) {
                throw new IllegalStateException("ViewAction target is not set");
            }

            if (!(target.getItems() instanceof EntityDataUnit)) {
                throw new IllegalStateException("ViewAction target dataSource is null or does not implement EntityDataUnit");
            }

            MetaClass metaClass = ((EntityDataUnit) target.getItems()).getEntityMetaClass();
            if (metaClass == null) {
                throw new IllegalStateException("Target is not bound to entity");
            }

            Entity editedEntity = target.getSingleSelected();
            if (editedEntity == null) {
                throw new IllegalStateException("There is not selected item in ViewAction target");
            }

            Screen editor = screenBuilders.editor(target)
                    .editEntity(editedEntity)
                    .build();

            if (editor instanceof ReadOnlyAwareScreen) {
                ((ReadOnlyAwareScreen) editor).setReadOnly(true);
            } else {
                throw new IllegalStateException(String.format("Screen '%s' does not implement ReadOnlyAwareScreen: %s",
                        editor.getId(), editor.getClass()));
            }

            editor.show();
        } else {
            super.actionPerform(component);
        }
    }
}
