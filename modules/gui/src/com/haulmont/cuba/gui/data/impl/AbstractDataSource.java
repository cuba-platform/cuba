/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 13.02.2009 15:35:22
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.chile.core.common.ValueListener;

import java.util.*;

public abstract class AbstractDataSource<T extends Entity>
    implements
        Datasource<T>, DatasourceImplementation<T>
{
    protected String id;
    protected boolean modified;

    protected List<DatasourceListener> dsListeners = new ArrayList<DatasourceListener>();

    protected Collection<T> itemToCreate = new HashSet<T>();
    protected Collection<T> itemToUpdate = new HashSet<T>();
    protected Collection<T> itemToDelete = new HashSet<T>();

    public AbstractDataSource(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isModified() {
        return modified;
    }

    public Collection<T> getItemsToCreate() {
        return Collections.unmodifiableCollection(itemToCreate);
    }

    public Collection<T> getItemsToUpdate() {
        return Collections.unmodifiableCollection(itemToUpdate);
    }

    public Collection<T> getItemsToDelete() {
        return Collections.unmodifiableCollection(itemToDelete);
    }

    public void addListener(DatasourceListener<T> listener) {
        if (dsListeners.indexOf(listener) < 0) {
            dsListeners.add(listener);
        }
    }

    public void removeListener(DatasourceListener<T> listener) {
        dsListeners.remove(listener);
    }

    protected void clearCommitLists() {
        itemToCreate.clear();
        itemToUpdate.clear();
        itemToDelete.clear();
    }

    protected class ItemListener implements ValueListener {
        public void propertyChanged(Object item, String property, Object prevValue, Object value) {
            for (DatasourceListener dsListener : dsListeners) {
                dsListener.valueChanged(item, property, prevValue, value);
            }

            if (PersistenceHelper.isNew((Entity) item)) {
                itemToCreate.add((T) item);
            } else {
                itemToUpdate.add((T) item);
            }

            modified = true;
        }
    }
}
