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
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.RemoveHelper;
import com.haulmont.cuba.gui.components.ActionType;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.components.data.meta.EntityDataUnit;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.Nested;
import com.haulmont.cuba.security.entity.EntityOp;

import javax.inject.Inject;

@ActionType(RemoveAction.ID)
public class RemoveAction extends SecuredListAction {

    public static final String ID = "remove";

    @Inject
    protected RemoveHelper removeHelper;

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
        this.caption = messages.getMainMessage("actions.Remove");
    }

    @Inject
    protected void setConfiguration(Configuration configuration) {
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        setShortcut(clientConfig.getTableRemoveShortcut());
    }

    @Override
    protected boolean isPermitted() {
        if (target == null || !(target.getItems() instanceof ContainerDataUnit)) {
            return false;
        }

        if (!checkRemovePermission()) {
            return false;
        }

        return super.isPermitted();
    }

    protected boolean checkRemovePermission() {
        MetaClass metaClass = ((ContainerDataUnit) target.getItems()).getEntityMetaClass();
        if (metaClass == null) {
            return true;
        }

        boolean entityOpPermitted = security.isEntityOpPermitted(metaClass, EntityOp.DELETE);
        if (!entityOpPermitted) {
            return false;
        }

        EntityDataUnit dataUnit = (EntityDataUnit) target.getItems();
        if (dataUnit instanceof Nested) {
            Nested nestedContainer = (Nested) dataUnit;

            MetaClass masterMetaClass = nestedContainer.getMaster().getEntityMetaClass();
            MetaProperty metaProperty = masterMetaClass.getPropertyNN(nestedContainer.getProperty());

            boolean attrPermitted = security.isEntityAttrUpdatePermitted(masterMetaClass, metaProperty.getName());
            if (!attrPermitted) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void actionPerform(Component component) {
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            if (!(target.getItems() instanceof ContainerDataUnit)) {
                throw new IllegalStateException("RemoveAction target items is null or does not implement ContainerDataUnit");
            }

            ContainerDataUnit items = (ContainerDataUnit) target.getItems();
            CollectionContainer container = items.getContainer();
            if (container == null) {
                throw new IllegalStateException("RemoveAction target is not bound to CollectionContainer");
            }

            removeHelper.removeSelected(target);
        } else {
            super.actionPerform(component);
        }
    }
}