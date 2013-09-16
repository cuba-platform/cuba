/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.*;

import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CollectionDsActionsNotifier implements CollectionDatasourceListener<Entity> {
    
    private Component.ActionsHolder actionsHolder;

    public CollectionDsActionsNotifier(Component.ActionsHolder actionsHolder) {
        this.actionsHolder = actionsHolder;
    }

    @Override
    public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
        for (Action action : actionsHolder.getActions()) {
            if (action instanceof DatasourceListener) {
                ((DatasourceListener) action).stateChanged(ds, prevState, state);
            }
        }
    }

    @Override
    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
        for (Action action : actionsHolder.getActions()) {
            if (action instanceof DatasourceListener) {
                ((DatasourceListener) action).itemChanged(ds, prevItem, item);
            }
        }
    }

    @Override
    public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
        for (Action action : actionsHolder.getActions()) {
            if (action instanceof CollectionDatasourceListener) {
                ((CollectionDatasourceListener) action).collectionChanged(ds, operation, items);
            }
        }
    }

    @Override
    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
        for (Action action : actionsHolder.getActions()) {
            if (action instanceof ValueListener) {
                ((ValueListener) action).valueChanged(source, property, prevValue, value);
            }
        }
    }
}