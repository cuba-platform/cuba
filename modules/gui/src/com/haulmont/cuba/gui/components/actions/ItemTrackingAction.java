/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.data.Datasource;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class ItemTrackingAction extends AbstractAction implements CollectionDatasourceListener {

    protected ItemTrackingAction(String id) {
        super(id);
    }

    @Override
    public void actionPerform(Component component) {
    }

    @Override
    public void collectionChanged(CollectionDatasource ds, Operation operation) {
    }

    @Override
    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
        setEnabled(item != null);
    }

    @Override
    public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
        setEnabled(Datasource.State.VALID.equals(state) && ds.getItem() != null);
    }

    @Override
    public void valueChanged(Object source, String property, Object prevValue, Object value) {
    }
}
