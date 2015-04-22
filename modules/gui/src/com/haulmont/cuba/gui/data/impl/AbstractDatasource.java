/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.common.ValueListener;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
public abstract class AbstractDatasource<T extends Entity> implements Datasource<T>, DatasourceImplementation<T> {

    protected Log log = LogFactory.getLog(getClass());

    protected String id;
    protected boolean modified;
    protected boolean allowCommit = true;
    protected CommitMode commitMode = CommitMode.DATASTORE;
    protected Datasource parentDs;
    protected Metadata metadata = AppBeans.get(Metadata.NAME);

    protected List<DatasourceListener> dsListeners = new ArrayList<>();
    protected List<WeakDatasourceListener> weakListeners = new ArrayList<>();

    protected Collection itemToCreate = new HashSet();
    protected Collection itemToUpdate = new HashSet();
    protected Collection itemToDelete = new HashSet();
    protected ValueListener listener = new ItemListener();

    protected boolean listenersEnabled = true;

    protected boolean needToLoadRuntimeProperties;

    @Override
    public void setup(DsContext dsContext, DataSupplier dataSupplier, String id,
                      MetaClass metaClass, @Nullable View view) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isModified() {
        return allowCommit && modified;
    }

    @Override
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    @Override
    public boolean isAllowCommit() {
        return allowCommit;
    }

    @Override
    public void setAllowCommit(boolean allowCommit) {
        this.allowCommit = allowCommit;
    }

    @Override
    public Collection<T> getItemsToCreate() {
        return itemToCreate;
    }

    @Override
    public Collection<T> getItemsToUpdate() {
        return itemToUpdate;
    }

    @Override
    public Collection<T> getItemsToDelete() {
        return itemToDelete;
    }

    @Override
    public void modified(T item) {
        if (PersistenceHelper.isNew(item)) {
            itemToCreate.remove(item);
            itemToCreate.add(item);
        } else {
            itemToUpdate.remove(item);
            itemToUpdate.add(item);
        }
        modified = true;
    }

    @Override
    public void deleted(T item) {
        if (PersistenceHelper.isNew(item)) {
            itemToCreate.remove(item);
        } else {
            itemToDelete.add(item);
        }
        modified = true;
    }

    @Override
    public boolean enableListeners(boolean enable) {
        boolean oldValue = listenersEnabled;
        listenersEnabled = enable;
        return oldValue;
    }

    @Override
    public CommitMode getCommitMode() {
        return commitMode;
    }

    @Override
    public void setCommitMode(CommitMode commitMode) {
        this.commitMode = commitMode;
    }

    @Override
    public Datasource getParent() {
        return parentDs;
    }

    @Override
    public void setParent(Datasource datasource) {
        parentDs = datasource;
        commitMode = parentDs != null ? CommitMode.PARENT : CommitMode.DATASTORE;
        addParentsToNested();
    }

    protected void addParentsToNested() {
        if (parentDs == null || getDsContext() == parentDs.getDsContext())
            return;

        // Iterate through all datasources in the same DsContext
        for (Datasource sibling : getDsContext().getAll()) {
            // If the datasource is a property datasource of the Child
            if (sibling instanceof NestedDatasource && ((NestedDatasource) sibling).getMaster().equals(this)) {
                // Look for corresponding property datasource in the Parent's DsContext
                for (Datasource siblingOfParent : parentDs.getDsContext().getAll()) {
                    if (siblingOfParent instanceof NestedDatasource &&
                            ((NestedDatasource) siblingOfParent).getProperty().equals(((NestedDatasource) sibling).getProperty())) {
                        // If such corresponding datasource found, set it as a parent for our property datasource
                        ((DatasourceImplementation) sibling).setParent(siblingOfParent);
                    }
                }
            }
        }
    }

    @Override
    public void addListener(DatasourceListener<T> listener) {
        if (!weakListeners.isEmpty()) {
            for (WeakDatasourceListener weakListener : new ArrayList<>(weakListeners)) {
                if (!weakListener.isAlive()) {
                    dsListeners.remove(weakListener);
                }
            }
        }

        if (dsListeners.indexOf(listener) < 0) {
            dsListeners.add(listener);

            if (listener instanceof WeakDatasourceListener) {
                weakListeners.add((WeakDatasourceListener) listener);
            }
        }
    }

    @Override
    public void removeListener(DatasourceListener<T> listener) {
        boolean removed = dsListeners.remove(listener);

        if (removed && listener instanceof WeakDatasourceListener) {
            weakListeners.remove(listener);
        }

        if (!weakListeners.isEmpty()) {
            for (WeakDatasourceListener weakListener : new ArrayList<>(weakListeners)) {
                if (!weakListener.isAlive()) {
                    dsListeners.remove(weakListener);
                }
            }
        }
    }

    @Override
    public void clearCommitLists() {
        itemToCreate.clear();
        itemToUpdate.clear();
        itemToDelete.clear();
    }

    @Override
    public boolean needToLoadRuntimeProperties() {
        return needToLoadRuntimeProperties;
    }

    @Override
    public void setNeedToLoadRuntimeProperties(boolean value) {
        this.needToLoadRuntimeProperties = value;
    }

    protected void attachListener(Instance item) {
        if (item == null) return;
        item.addListener(listener);
    }

    protected void detachListener(Instance item) {
        if (item == null) return;
        item.removeListener(listener);
    }

    protected void fireItemChanged(Object prevItem) {
        for (DatasourceListener dsListener : new ArrayList<>(dsListeners)) {
            dsListener.itemChanged(this, (Entity) prevItem, getItem());
        }
    }

    protected void fireStateChanged(State prevStatus) {
        for (DatasourceListener dsListener : new ArrayList<>(dsListeners)) {
            dsListener.stateChanged(this, prevStatus, getState());
        }
    }

    protected class ItemListener implements ValueListener {
        @Override
        public void propertyChanged(Object item, String property, Object prevValue, Object value) {
            if (!listenersEnabled)
                return;

            log.trace("propertyChanged: item=" + item + ", property=" + property +
                    ", value=" + value + ", prevValue=" + prevValue);

            for (DatasourceListener dsListener : new ArrayList<>(dsListeners)) {
                dsListener.valueChanged(item, property, prevValue, value);
            }
            if (!metadata.getTools().isTransient(item, property))
                modified((T)item);
        }
    }

    @Override
    public String toString() {
        return id + "{modified=" + modified + ", parent=" + parentDs + "}";
    }
}