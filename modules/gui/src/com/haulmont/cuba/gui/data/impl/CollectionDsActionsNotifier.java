/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CollectionDsActionsNotifier implements Datasource.ItemChangeListener,
                                                    Datasource.ItemPropertyChangeListener,
                                                    Datasource.StateChangeListener,
                                                    CollectionDatasource.CollectionChangeListener {

    private final Component.ActionsHolder actionsHolder;

    public CollectionDsActionsNotifier(Component.ActionsHolder actionsHolder) {
        this.actionsHolder = actionsHolder;
    }

    @Override
    public void collectionChanged(CollectionDatasource.CollectionChangeEvent e) {
        for (Action action : actionsHolder.getActions()) {
            action.refreshState();
        }
    }

    @Override
    public void itemChanged(Datasource.ItemChangeEvent e) {
        for (Action action : actionsHolder.getActions()) {
            action.refreshState();
        }
    }

    @Override
    public void itemPropertyChanged(Datasource.ItemPropertyChangeEvent e) {
        for (Action action : actionsHolder.getActions()) {
            action.refreshState();
        }
    }

    @Override
    public void stateChanged(Datasource.StateChangeEvent e) {
        for (Action action : actionsHolder.getActions()) {
            action.refreshState();
        }
    }

    public void bind(CollectionDatasource ds) {
        ds.addItemChangeListener(this);
        ds.addItemPropertyChangeListener(this);
        ds.addStateChangeListener(this);
        ds.addCollectionChangeListener(this);
    }
}