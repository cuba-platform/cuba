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
import com.haulmont.cuba.gui.MetadataHelper;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.chile.core.common.ValueListener;
import com.haulmont.chile.core.model.Instance;

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
    protected ValueListener listener;

    public AbstractDataSource(String id) {
        this.id = id;
        listener = new ItemListener();
    }

    public String getId() {
        return id;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
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

    public void modified(T item) {
        if (PersistenceHelper.isNew(item)) {
            itemToCreate.add(item);
        } else {
            itemToUpdate.add(item);
        }
        modified = true;
    }

    public void deleted(T item) {
        itemToDelete.add(item);
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

    protected void attachListener(Instance item) {
        if (item == null) return;
        item.addListener(listener);
    }

    protected void detachListener(Instance item) {
        if (item == null) return;
        item.removeListener(listener);
    }

    protected void forceItemChanged(Object prevItem) {
        for (DatasourceListener dsListener : new ArrayList<DatasourceListener>(dsListeners)) {
            dsListener.itemChanged(this, (Entity) prevItem, getItem());
        }
    }

    protected void forceStateChanged(State prevStatus) {
        for (DatasourceListener dsListener : new ArrayList<DatasourceListener>(dsListeners)) {
            dsListener.stateChanged(this, prevStatus, getState());
        }
    }

    protected class ItemListener implements ValueListener {
        public void propertyChanged(Object item, String property, Object prevValue, Object value) {
            for (DatasourceListener dsListener : new ArrayList<DatasourceListener>(dsListeners)) {
                dsListener.valueChanged(item, property, prevValue, value);
            }
            if (!MetadataHelper.isTransient(item, property))
                modified((T)item);
        }
    }
}
