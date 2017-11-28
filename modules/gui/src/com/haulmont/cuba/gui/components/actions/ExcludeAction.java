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
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import org.springframework.context.annotation.Scope;

import java.util.Set;

/**
 * The <code>RemoveAction</code> variant that excludes instances from the list, but doesn't delete them from DB.
 * <p>
 * In order to provide your own implementation globally, create a subclass and register it in {@code web-spring.xml},
 * for example:
 * <pre>
 * &lt;bean id="cuba_ExcludeAction" class="com.company.sample.gui.MyExcludeAction" scope="prototype"/&gt;
 * </pre>
 * Also, use {@code create()} static methods instead of constructors when creating the action programmatically.
 */
@org.springframework.stereotype.Component("cuba_ExcludeAction")
@Scope("prototype")
public class ExcludeAction extends RemoveAction {

    public static final String ACTION_ID = ListActionType.EXCLUDE.getId();

    protected Metadata metadata;

    /**
     * Creates an action with default id. Autocommit and Confirm properties are set to false.
     * @param target    component containing this action
     */
    public static ExcludeAction create(ListComponent target) {
        return AppBeans.getPrototype("cuba_ExcludeAction", target);
    }

    /**
     * Creates an action with default id.
     * @param target    component containing this action
     * @param autocommit    whether to commit datasource immediately
     * @param confirm       whether to show the confirmation dialog to user
     */
    public static ExcludeAction create(ListComponent target, boolean autocommit, boolean confirm) {
        return AppBeans.getPrototype("cuba_ExcludeAction", target, autocommit, confirm);
    }

    /**
     * Creates an action with the given id.
     * @param target    component containing this action
     * @param autocommit    whether to commit datasource immediately
     * @param confirm       whether to show the confirmation dialog to user
     * @param id            action's name
     */
    public static ExcludeAction create(ListComponent target, boolean autocommit, boolean confirm, String id) {
        return AppBeans.getPrototype("cuba_ExcludeAction", target, autocommit, confirm, id);
    }

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

        this.icon = AppBeans.get(Icons.class).get(CubaIcon.EXCLUDE_ACTION);
    }

    @Override
    protected boolean checkRemovePermission() {
        CollectionDatasource ds = target.getDatasource();
        if (ds instanceof PropertyDatasource) {
            PropertyDatasource propertyDatasource = (PropertyDatasource) ds;

            MetaClass parentMetaClass = propertyDatasource.getMaster().getMetaClass();
            MetaProperty metaProperty = propertyDatasource.getProperty();

            boolean attrPermitted = security.isEntityAttrPermitted(parentMetaClass, metaProperty.getName(), EntityAttrAccess.MODIFY);
            if (!attrPermitted) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void actionPerform(Component component) {
        if (!isEnabled())
            return;

        if (beforeActionPerformedHandler != null) {
            if (!beforeActionPerformedHandler.beforeActionPerformed())
                return;
        }

        @SuppressWarnings("unchecked")
        Set<Entity> selected = target.getSelected();
        if (!selected.isEmpty()) {
            if (confirm) {
                confirmAndRemove(selected);
            } else {
                doRemove(selected, autocommit);
                afterRemove(selected);

                if (afterRemoveHandler != null) {
                    afterRemoveHandler.handle(selected);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doRemove(Set<Entity> selected, boolean autocommit) {
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
                        for (Entity item : selected) {
                            item.setValue(inverseProp.getName(), null);
                        }
                    }
                }
            }
        }

        for (Entity item : selected) {
            ds.modifyItem(item);
            ds.excludeItem(item);
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
}