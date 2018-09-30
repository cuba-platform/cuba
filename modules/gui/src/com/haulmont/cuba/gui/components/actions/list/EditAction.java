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
import com.haulmont.cuba.gui.EditorScreens;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.security.entity.EntityOp;

import javax.inject.Inject;

@ActionType(EditAction.ID)
public class EditAction extends SecuredListAction {

    public static final String ID = "edit";

    protected EditorScreens editorScreens;

    // Set default caption only once
    protected boolean captionInitialized = false;
    protected Messages messages;

    public EditAction() {
        super(ID);
    }

    public EditAction(String id) {
        super(id);
    }

    @Inject
    protected void setIcons(Icons icons) {
        this.icon = icons.get(CubaIcon.EDIT_ACTION);
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.messages = messages;
        this.caption = messages.getMainMessage("actions.Edit");
    }

    @Inject
    protected void setConfiguration(Configuration configuration) {
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        setShortcut(clientConfig.getTableEditShortcut());
    }

    @Inject
    protected void setEditorScreens(EditorScreens editorScreens) {
        this.editorScreens = editorScreens;
    }

    @Override
    public void setTarget(ListComponent target) {
        if (target != null
                && !(target instanceof SupportsEntityBinding)) {
            throw new IllegalStateException("EditAction target does not implement SupportsEntityBinding");
        }

        super.setTarget(target);
    }

    @Override
    public void setCaption(String caption) {
        super.setCaption(caption);

        this.captionInitialized = true;
    }

    @Override
    protected boolean isPermitted() {
        if (!(target instanceof SupportsEntityBinding)) {
            return false;
        }

        MetaClass metaClass = ((SupportsEntityBinding) target).getBindingMetaClass();
        if (metaClass == null) {
            return true;
        }

        boolean entityOpPermitted = security.isEntityOpPermitted(metaClass, EntityOp.READ);
        if (!entityOpPermitted) {
            return false;
        }

        return super.isPermitted();
    }

    @Override
    public void refreshState() {
        super.refreshState();

        if (!(target instanceof SupportsEntityBinding))
            return;

        if (!captionInitialized) {
            MetaClass metaClass = ((SupportsEntityBinding) target).getBindingMetaClass();
            if (metaClass != null) {
                if (security.isEntityOpPermitted(metaClass, EntityOp.UPDATE)) {
                    setCaption(messages.getMainMessage("actions.Edit"));
                } else {
                    setCaption(messages.getMainMessage("actions.View"));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void actionPerform(Component component) {
        // if standard behaviour
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            if (!(target instanceof SupportsEntityBinding)) {
                throw new IllegalStateException("EditAction target is null or does not implement SupportsEntityBinding");
            }

            MetaClass metaClass = ((SupportsEntityBinding) target).getBindingMetaClass();
            if (metaClass == null) {
                throw new IllegalStateException("Target is not bound to entity");
            }

            Entity editedEntity = target.getSingleSelected();
            if (editedEntity == null) {
                throw new IllegalStateException("There is not selected item in EditAction target");
            }

            Window window = ComponentsHelper.getWindowNN(target);
            Class<Entity> entityClass = metaClass.getJavaClass();

            Screen editor = editorScreens.builder(entityClass, window.getFrameOwner())
                    .editEntity(editedEntity)
                    .withListComponent(target)
                    .create();
            editor.show();
        } else {
            super.actionPerform(component);
        }
    }
}