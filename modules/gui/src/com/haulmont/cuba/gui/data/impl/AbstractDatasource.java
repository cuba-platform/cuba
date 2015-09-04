/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.compatibility.CompatibleDatasourceListenerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author abramov
 * @version $Id$
 */
public abstract class AbstractDatasource<T extends Entity> implements Datasource<T>, DatasourceImplementation<T> {

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected String id;
    protected boolean modified;
    protected boolean allowCommit = true;
    protected CommitMode commitMode = CommitMode.DATASTORE;
    protected Datasource parentDs;
    protected Metadata metadata = AppBeans.get(Metadata.NAME);

    protected List<ItemChangeListener<T>> itemChangeListeners; // lazily initialized list
    protected List<ItemPropertyChangeListener<T>> itemPropertyChangeListeners; // lazily initialized list
    protected List<StateChangeListener<T>> stateChangeListeners; // lazily initialized list

    protected Collection<Entity> itemToCreate = new HashSet<>();
    protected Collection<Entity> itemToUpdate = new HashSet<>();
    protected Collection<Entity> itemToDelete = new HashSet<>();
    protected Instance.PropertyChangeListener listener = new ItemListener();

    protected boolean listenersEnabled = true;

    protected boolean loadDynamicAttributes;

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
        return (Collection<T>) itemToCreate;
    }

    @Override
    public Collection<T> getItemsToUpdate() {
        return (Collection<T>) itemToUpdate;
    }

    @Override
    public Collection<T> getItemsToDelete() {
        return (Collection<T>) itemToDelete;
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
        if (parentDs == null || getDsContext() == parentDs.getDsContext()) {
            return;
        }

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
        CompatibleDatasourceListenerWrapper wrapper = new CompatibleDatasourceListenerWrapper(listener);

        addItemChangeListener(wrapper);
        addItemPropertyChangeListener(wrapper);
        addStateChangeListener(wrapper);
    }

    @Override
    public void removeListener(DatasourceListener<T> listener) {
        CompatibleDatasourceListenerWrapper wrapper = new CompatibleDatasourceListenerWrapper(listener);

        removeItemChangeListener(wrapper);
        removeItemPropertyChangeListener(wrapper);
        removeStateChangeListener(wrapper);
    }

    @Override
    public void addItemChangeListener(ItemChangeListener<T> listener) {
        if (itemChangeListeners == null) {
            itemChangeListeners = new ArrayList<>();
        }
        if (!itemChangeListeners.contains(listener)) {
            itemChangeListeners.add(listener);
        }
    }

    @Override
    public void removeItemChangeListener(ItemChangeListener<T> listener) {
        if (itemChangeListeners != null) {
            itemChangeListeners.remove(listener);
        }
    }

    @Override
    public void addItemPropertyChangeListener(ItemPropertyChangeListener<T> listener) {
        if (itemPropertyChangeListeners == null) {
            itemPropertyChangeListeners = new ArrayList<>();
        }
        if (!itemPropertyChangeListeners.contains(listener)) {
            itemPropertyChangeListeners.add(listener);
        }
    }

    @Override
    public void removeItemPropertyChangeListener(ItemPropertyChangeListener<T> listener) {
        if (itemPropertyChangeListeners != null) {
            itemPropertyChangeListeners.remove(listener);
        }
    }

    @Override
    public void addStateChangeListener(StateChangeListener<T> listener) {
        if (stateChangeListeners == null) {
            stateChangeListeners = new ArrayList<>();
        }
        if (!stateChangeListeners.contains(listener)) {
            stateChangeListeners.add(listener);
        }
    }

    @Override
    public void removeStateChangeListener(StateChangeListener<T> listener) {
        if (stateChangeListeners != null) {
            stateChangeListeners.remove(listener);
        }
    }

    @Override
    public void clearCommitLists() {
        itemToCreate.clear();
        itemToUpdate.clear();
        itemToDelete.clear();
    }

    @Override
    public boolean getLoadDynamicAttributes() {
        return loadDynamicAttributes;
    }

    @Override
    public void setLoadDynamicAttributes(boolean value) {
        this.loadDynamicAttributes = value;
    }

    protected void attachListener(Instance item) {
        if (item == null) {
            return;
        }

        item.addPropertyChangeListener(listener);
    }

    protected void detachListener(Instance item) {
        if (item == null) {
            return;
        }

        item.removePropertyChangeListener(listener);
    }

    protected void fireItemChanged(T prevItem) {
        if (itemChangeListeners != null && !itemChangeListeners.isEmpty()) {
            ItemChangeEvent<T> itemChangeEvent = new ItemChangeEvent<T>(this, prevItem, getItem());

            for (ItemChangeListener<T> listener : new ArrayList<>(itemChangeListeners)) {
                listener.itemChanged(itemChangeEvent);
            }
        }
    }

    protected void fireStateChanged(State prevStatus) {
        if (stateChangeListeners != null && !stateChangeListeners.isEmpty()) {
            StateChangeEvent<T> stateChangeEvent = new StateChangeEvent<>(this, prevStatus, getState());

            for (StateChangeListener<T> listener : new ArrayList<>(stateChangeListeners)) {
                listener.stateChanged(stateChangeEvent);
            }
        }
    }

    protected class ItemListener implements Instance.PropertyChangeListener {
        @SuppressWarnings("unchecked")
        @Override
        public void propertyChanged(Instance.PropertyChangeEvent e) {
            if (!listenersEnabled) {
                return;
            }

            log.trace("propertyChanged: item={}, property={}, value={}, prevValue={}",
                    e.getItem(), e.getProperty(), e.getValue(), e.getPrevValue());

            if (!metadata.getTools().isTransient(e.getItem(), e.getProperty())) {
                modified((T) e.getItem());
            }

            if (itemPropertyChangeListeners != null && !itemPropertyChangeListeners.isEmpty()) {
                AbstractDatasource<T> ds = AbstractDatasource.this;
                ItemPropertyChangeEvent<T> itemPropertyChangeEvent =
                        new ItemPropertyChangeEvent<T>(ds, (T) e.getItem(), e.getProperty(), e.getPrevValue(), e.getValue());

                for (ItemPropertyChangeListener<T> listener : new ArrayList<>(itemPropertyChangeListeners)) {
                    listener.itemPropertyChanged(itemPropertyChangeEvent);
                }
            }
        }
    }

    @Override
    public String toString() {
        return id + "{modified=" + modified + ", parent=" + parentDs + "}";
    }
}