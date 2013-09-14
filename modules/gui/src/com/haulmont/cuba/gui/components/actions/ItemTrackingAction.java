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

import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public class ItemTrackingAction extends AbstractAction implements CollectionDatasourceListener<Entity> {

    protected boolean enabledFlag = true;

    protected ItemTrackingAction(String id) {
        super(id);
    }

    @Override
    public void actionPerform(Component component) {
    }

    @Override
    public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
        super.setEnabled(enabledFlag
                && isApplicableTo(ds.getState(), ds.getState() == Datasource.State.VALID ? ds.getItem() : null));
    }

    @Override
    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
        super.setEnabled(enabledFlag && isApplicableTo(ds.getState(), item));
    }

    public boolean isApplicableTo(Datasource.State state, Entity item) {
        return Datasource.State.VALID.equals(state) && item != null;
    }

    public void updateApplicableTo(boolean applicableTo) {
        super.setEnabled(enabledFlag && applicableTo);
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
        super.setEnabled(this.enabledFlag
                && isApplicableTo(ds.getState(), ds.getState() == Datasource.State.VALID ? ds.getItem() : null));
    }

    @Override
    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
        super.setEnabled(enabledFlag && isApplicableTo(Datasource.State.VALID, source));
    }
}