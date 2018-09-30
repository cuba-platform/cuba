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
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.EditorScreens;
import com.haulmont.cuba.gui.components.ActionType;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.actions.ListAction;
import com.haulmont.cuba.gui.components.data.meta.EntityDataSource;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.security.entity.EntityOp;

import javax.inject.Inject;

@ActionType(CreateAction.ID)
public class CreateAction extends ListAction {

    public static final String ID = "create";

    protected Security security;
    protected EditorScreens editorScreens;

    public CreateAction() {
        super(ID);
    }

    public CreateAction(String id) {
        super(id);
    }

    @Inject
    protected void setSecurity(Security security) {
        this.security = security;
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.caption = messages.getMainMessage("actions.Create");
    }

    @Inject
    protected void setIcons(Icons icons) {
        this.icon = icons.get(CubaIcon.CREATE_ACTION);
    }

    @Inject
    protected void setConfiguration(Configuration configuration) {
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        setShortcut(clientConfig.getTableInsertShortcut());
    }

    @Inject
    protected void setEditorScreens(EditorScreens editorScreens) {
        this.editorScreens = editorScreens;
    }

    @Override
    protected boolean isPermitted() {
        if (target == null || !(target.getDataSource() instanceof EntityDataSource)) {
            return false;
        }

        MetaClass metaClass = ((EntityDataSource) target.getDataSource()).getEntityMetaClass();
        if (metaClass == null) {
            return true;
        }

        boolean createPermitted = security.isEntityOpPermitted(metaClass, EntityOp.CREATE);
        if (!createPermitted) {
            return false;
        }

        return super.isPermitted();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void actionPerform(Component component) {
        // if standard behaviour
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            if (!(target.getDataSource() instanceof EntityDataSource)) {
                throw new IllegalStateException("CreateAction target dataSource is null or does not implement EntityDataSource");
            }

            MetaClass metaClass = ((EntityDataSource) target.getDataSource()).getEntityMetaClass();
            if (metaClass == null) {
                throw new IllegalStateException("Target is not bound to entity");
            }

            Window window = ComponentsHelper.getWindowNN(target);
            Class<Entity> entityClass = metaClass.getJavaClass();

            Screen editor = editorScreens.builder(entityClass, window.getFrameOwner())
                    .newEntity()
                    .withListComponent(target)
                    .create();
            editor.show();
        } else {
            super.actionPerform(component);
        }
    }
}