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
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.RemoveOperation;
import com.haulmont.cuba.gui.components.ActionType;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.components.data.meta.EntityDataUnit;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.Nested;

import javax.inject.Inject;

@ActionType(ExcludeAction.ID)
public class ExcludeAction extends SecuredListAction {

    public static final String ID = "exclude";

    @Inject
    protected Security security;
    @Inject
    protected RemoveOperation removeOperation;

    public ExcludeAction() {
        super(ID);
    }

    public ExcludeAction(String id) {
        super(id);
    }

    @Inject
    protected void setIcons(Icons icons) {
        this.icon = icons.get(CubaIcon.EXCLUDE_ACTION);
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.caption = messages.getMainMessage("actions.Exclude");
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

        MetaClass metaClass = ((EntityDataUnit) target.getItems()).getEntityMetaClass();
        if (metaClass == null) {
            return true;
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

        return super.isPermitted();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void actionPerform(Component component) {
        // if standard behaviour
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            if (!(target.getItems() instanceof ContainerDataUnit)) {
                throw new IllegalStateException("ExcludeAction target items is null or does not implement ContainerDataUnit");
            }

            ContainerDataUnit items = (ContainerDataUnit) target.getItems();
            CollectionContainer container = items.getContainer();
            if (container == null) {
                throw new IllegalStateException("ExcludeAction target is not bound to CollectionContainer");
            }

            removeOperation.excludeSelected(target);
        } else {
            super.actionPerform(component);
        }
    }
}