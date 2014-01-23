/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml;

import com.haulmont.cuba.core.entity.Entity;
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
public class DeclarativeTrackingAction extends DeclarativeAction implements CollectionDatasourceListener<Entity> {

    protected boolean enabledFlag = true;

    public DeclarativeTrackingAction(String id, String caption, String description, String icon, String enable, String visible,
                                     String methodName, @Nullable String shortcut, Component.ActionsHolder holder) {
        super(id, caption, description, icon, enable, visible, methodName, shortcut, holder);
    }

    @Override
    public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
        super.setEnabled(enabledFlag
                && ds.getState() == Datasource.State.VALID && ds.getItem() != null);
    }

    @Override
    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
        super.setEnabled(item != null);
    }

    @Override
    public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
        super.setEnabled(state == Datasource.State.VALID && ds.getItem() != null);
    }

    @Override
    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
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
}