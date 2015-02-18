/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.data.Datasource;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public class DeclarativeTrackingAction extends DeclarativeAction
        implements Action.HasTarget, Action.UiPermissionAware, CollectionDatasourceListener<Entity> {

    public DeclarativeTrackingAction(String id, String caption, String description, String icon, String enable, String visible,
                                     String methodName, @Nullable String shortcut, Component.ActionsHolder holder) {
        super(id, caption, description, icon, enable, visible, methodName, shortcut, holder);
    }

    @Override
    protected boolean isApplicable() {
        return target != null && !target.getSelected().isEmpty();
    }

    @Override
    public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
        refreshState();
    }

    @Override
    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
        refreshState();
    }

    @Override
    public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
        refreshState();
    }

    @Override
    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
        refreshState();
    }
}