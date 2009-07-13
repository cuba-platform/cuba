/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 15:18:26
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.DataService;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

public class PropertyDatasourceImpl<T extends Entity>
    extends
        AbstractDataSource<T>
    implements
        Datasource<T>, DatasourceImplementation<T>
{
    protected Datasource ds;
    protected MetaProperty metaProperty;

    public PropertyDatasourceImpl(String id, Datasource ds, String property) {
        super(id);
        this.ds = ds;
        metaProperty = ds.getMetaClass().getProperty(property);
        this.ds.addListener(new DatasourceListener<Entity>() {

            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                __itemChanged(prevItem, item);
            }

            public void stateChanged(Datasource ds, State prevState, State state) {
                for (DatasourceListener dsListener : new ArrayList<DatasourceListener>(dsListeners)) {
                    dsListener.stateChanged(PropertyDatasourceImpl.this, prevState, state);
                }
            }

            public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                if (property.equals(metaProperty.getName()) && !ObjectUtils.equals(prevValue, value)) {
                    __itemChanged((Entity) prevValue, (Entity) value);
                }
            }

            private void __itemChanged(Entity prevItem, Entity item) {
                Entity prevValue = getItem((Instance) prevItem);
                Entity newValue = getItem((Instance) item);

                if (!ObjectUtils.equals(prevValue, newValue)) {
                    detachListener((Instance) prevValue);
                    attachListener((Instance) newValue);

                    forceItemChanged(prevValue);
                }
            }

        });
    }

    public State getState() {
        return ds.getState();
    }

    public T getItem() {
        final Instance item = (Instance) ds.getItem();
        return getItem(item);
    }

    private T getItem(Instance item) {
        return item == null ? null : (T) item.getValue(metaProperty.getName());
    }

    public MetaClass getMetaClass() {
        return metaProperty.getRange().asClass();
    }

    public View getView() {
        final ViewProperty property = ds.getView().getProperty(metaProperty.getName());
        return property == null ? null : property.getView();
    }

    public DsContext getDsContext() {
        return ds.getDsContext();
    }

    public DataService getDataService() {
        return ds.getDataService();
    }

    public CommitMode getCommitMode() {
        // TODO support embedded
        return CommitMode.DATASTORE;
    }

    public void commit() {
        throw new UnsupportedOperationException();
    }

    public void refresh() {
    }

    public void setItem(T item) {
        throw new UnsupportedOperationException();
    }

    public void invalidate() {
    }


    public void initialized() {
    }

    public void commited(Map<Entity, Entity> map) {
        modified = false;
        clearCommitLists();
    }
}
