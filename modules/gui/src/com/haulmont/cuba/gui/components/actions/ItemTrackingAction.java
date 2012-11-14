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
 * @author artamonov
 * @version $Id$
 */
public class ItemTrackingAction extends AbstractAction implements CollectionDatasourceListener {

    protected boolean enabledFlag = true;

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
        super.setEnabled(enabledFlag && item != null);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabledFlag = enabled;
        super.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return this.enabledFlag && super.isEnabled();
    }

    @Override
    public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
        super.setEnabled(Datasource.State.VALID.equals(state) && ds.getItem() != null);
    }

    @Override
    public void valueChanged(Object source, String property, Object prevValue, Object value) {
    }
}