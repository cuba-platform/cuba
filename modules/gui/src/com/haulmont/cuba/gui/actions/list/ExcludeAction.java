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
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.components.ActionType;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.components.data.meta.EntityDataUnit;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.Nested;

import javax.inject.Inject;
import java.util.Set;

@ActionType(ExcludeAction.ID)
public class ExcludeAction extends SecuredListAction {

    public static final String ID = "exclude";

    protected Security security;
    protected ExtendedEntities extendedEntities;

    public ExcludeAction() {
        super(ID);
    }

    public ExcludeAction(String id) {
        super(id);
    }

    @Inject
    protected void setSecurity(Security security) {
        this.security = security;
    }

    @Inject
    protected void setExtendedEntities(ExtendedEntities extendedEntities) {
        this.extendedEntities = extendedEntities;
    }

    @Inject
    protected void setIcons(Icons icons) {
        this.icon = icons.get(CubaIcon.EXCLUDE_ACTION);
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.caption = messages.getMainMessage("actions.Exclude");
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
            if (!(target.getItems() instanceof ContainerDataUnit)) {
                throw new IllegalStateException("ExcludeAction target items is null or does not implement ContainerDataUnit");
            }

            CollectionContainer<Entity> collectionDc = ((ContainerDataUnit) target.getItems()).getContainer();

            Set<? extends Entity> selectedItems = target.getSelected();

            if (!selectedItems.isEmpty()) {
                if (collectionDc instanceof Nested) {
                    InstanceContainer holderDc = ((Nested) collectionDc).getParent();

                    String property = ((Nested) collectionDc).getProperty();
                    Entity holderItem = holderDc.getItem();

                    MetaProperty metaProperty = holderItem.getMetaClass().getPropertyNN(property);
                    MetaProperty inverseMetaProperty = metaProperty.getInverse();

                    if (inverseMetaProperty != null
                            && !inverseMetaProperty.getRange().getCardinality().isMany()) {

                        Class inversePropClass = extendedEntities.getEffectiveClass(inverseMetaProperty.getDomain());
                        Class dcClass = extendedEntities.getEffectiveClass(collectionDc.getEntityMetaClass());

                        if (inversePropClass.isAssignableFrom(dcClass)) {

                            // update reference for One-To-Many
                            for (Entity item : selectedItems) {
                                item.setValue(inverseMetaProperty.getName(), null);
                            }
                        }
                    }
                }

                collectionDc.getMutableItems().removeAll(selectedItems);
            }
        } else {
            super.actionPerform(component);
        }
    }
}