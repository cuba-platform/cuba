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
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.LookupScreens;
import com.haulmont.cuba.gui.components.ActionType;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.actions.ListAction;
import com.haulmont.cuba.gui.components.data.meta.EntityDataUnit;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.model.Nested;
import com.haulmont.cuba.gui.screen.Screen;

import javax.inject.Inject;

@ActionType(AddAction.ID)
public class AddAction extends ListAction {

    public static final String ID = "add";

    protected Security security;
    protected LookupScreens lookupScreens;

    public AddAction() {
        super(ID);
    }

    public AddAction(String id) {
        super(id);
    }

    @Inject
    protected void setSecurity(Security security) {
        this.security = security;
    }

    @Inject
    protected void setLookupScreens(LookupScreens lookupScreens) {
        this.lookupScreens = lookupScreens;
    }

    @Inject
    protected void setIcons(Icons icons) {
        this.icon = icons.get(CubaIcon.ADD_ACTION);
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.caption = messages.getMainMessage("actions.Add");
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

        EntityDataUnit dataUnit = (EntityDataUnit) target.getItems();
        if (dataUnit instanceof Nested) {
            Nested nestedContainer = (Nested) dataUnit;

            MetaClass holderMetaClass = nestedContainer.getParent().getEntityMetaClass();
            MetaProperty metaProperty = holderMetaClass.getPropertyNN(nestedContainer.getProperty());

            boolean attrPermitted = security.isEntityAttrUpdatePermitted(holderMetaClass, metaProperty.getName());
            if (!attrPermitted) {
                return false;
            }
        }

        return super.isPermitted();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void actionPerform(Component component) {
        // if standard behaviour
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            if (!(target.getItems() instanceof EntityDataUnit)) {
                throw new IllegalStateException("AddAction target items is null or does not implement EntityDataUnit");
            }

            MetaClass metaClass = ((EntityDataUnit) target.getItems()).getEntityMetaClass();
            if (metaClass == null) {
                throw new IllegalStateException("Target is not bound to entity");
            }

            Window window = ComponentsHelper.getWindowNN(target);
            Class<Entity> entityClass = metaClass.getJavaClass();

            Screen lookupScreen = lookupScreens.builder(entityClass, window.getFrameOwner())
                    .withListComponent(target)
                    .build();

            lookupScreen.show();
        } else {
            super.actionPerform(component);
        }
    }
}