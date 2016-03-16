/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.NestedDatasource;
import com.haulmont.cuba.gui.data.PropertyDatasource;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.security.entity.EntityAttrAccess;

import java.util.Set;

/**
 * The <code>RemoveAction</code> variant that excludes instances from the list, but doesn't delete them from DB.
 *
 */
public class ExcludeAction extends RemoveAction {

    public static final String ACTION_ID = ListActionType.EXCLUDE.getId();

    protected boolean confirm;

    protected Metadata metadata;

    /**
     * The simplest constructor. Autocommit and Confirm properties are set to false, the action has default name.
     * @param target     component containing this action
     */
    public ExcludeAction(ListComponent target) {
        this(target, false, false, ACTION_ID);
    }

    /**
     * Constructor that allows to specify autocommit and confirm value. The action has default name.
     * @param target         component containing this action
     * @param autocommit    whether to commit datasource immediately
     * @param confirm       whether to show the confirmation dialog to user
     */
    public ExcludeAction(ListComponent target, boolean autocommit, boolean confirm) {
        this(target, autocommit, confirm, ACTION_ID);
    }

    /**
     * Constructor that allows to specify all parameters.
     * @param target         component containing this action
     * @param autocommit    whether to commit datasource immediately
     * @param confirm       whether to show the confirmation dialog to user
     * @param id            action's name
     */
    public ExcludeAction(ListComponent target, boolean autocommit, boolean confirm, String id) {
        super(target, autocommit, id);

        this.confirm = confirm;
        this.caption = messages.getMainMessage("actions.Exclude");
        this.metadata = AppBeans.get(Metadata.NAME);

        ThemeConstantsManager thCM = AppBeans.get(ThemeConstantsManager.NAME);
        this.icon = thCM.getThemeValue("actions.Exclude.icon");
    }

    @Override
    protected boolean isPermitted() {
        if (target == null || target.getDatasource() == null) {
            return false;
        }

        CollectionDatasource ds = target.getDatasource();
        if (ds instanceof PropertyDatasource) {
            PropertyDatasource propertyDatasource = (PropertyDatasource) ds;

            MetaClass parentMetaClass = propertyDatasource.getMaster().getMetaClass();
            MetaProperty metaProperty = propertyDatasource.getProperty();

            return security.isEntityAttrPermitted(parentMetaClass, metaProperty.getName(), EntityAttrAccess.MODIFY);
        }

        return true;
    }

    @Override
    public void actionPerform(Component component) {
        if (!isEnabled())
            return;

        if (beforeActionPerformedHandler != null) {
            beforeActionPerformedHandler.run();
        }

        Set selected = target.getSelected();
        if (!selected.isEmpty()) {
            if (confirm) {
                confirmAndRemove(selected);
            } else {
                doRemove(selected, autocommit);
                afterRemove(selected);
            }
        }

        if (afterActionPerformedHandler != null) {
            afterActionPerformedHandler.run();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doRemove(Set selected, boolean autocommit) {
        @SuppressWarnings({"unchecked"})
        CollectionDatasource ds = target.getDatasource();
        if (ds instanceof NestedDatasource) {
            // Clear reference to master entity
            Datasource masterDs = ((NestedDatasource) ds).getMaster();
            MetaProperty metaProperty = ((NestedDatasource) ds).getProperty();
            if (masterDs != null && metaProperty != null) {
                MetaProperty inverseProp = metaProperty.getInverse();
                if (inverseProp != null) {
                    ExtendedEntities extendedEntities = metadata.getExtendedEntities();

                    Class inversePropClass = extendedEntities.getEffectiveClass(inverseProp.getDomain());
                    Class dsClass = extendedEntities.getEffectiveClass(ds.getMetaClass());
                    if (inversePropClass.isAssignableFrom(dsClass)) {
                        for (Object item : selected) {
                            ((Entity) item).setValue(inverseProp.getName(), null);
                        }
                    }
                }
            }
        }

        for (Object item : selected) {
            ds.excludeItem((Entity) item);
        }

        if (autocommit && (ds.getCommitMode() != Datasource.CommitMode.PARENT)) {
            try {
                ds.commit();
            } catch (RuntimeException e) {
                ds.refresh();
                throw e;
            }
        }
    }

    /**
     * @return  whether to show the confirmation dialog to user
     */
    public boolean isConfirm() {
        return confirm;
    }

    /**
     * @param confirm   whether to show the confirmation dialog to user
     */
    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }
}