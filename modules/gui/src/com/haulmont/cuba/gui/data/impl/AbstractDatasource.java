/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.bali.events.EventRouter;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;

@Deprecated
public abstract class AbstractDatasource<T extends Entity> implements Datasource<T>, DatasourceImplementation<T> {

    private static final Logger log = LoggerFactory.getLogger(AbstractDatasource.class);

    protected String id;
    protected boolean modified;
    protected boolean allowCommit = true;
    protected CommitMode commitMode = CommitMode.DATASTORE;
    protected Datasource parentDs;
    protected Metadata metadata = AppBeans.get(Metadata.NAME);

    protected BackgroundWorker backgroundWorker = AppBeans.get(BackgroundWorker.NAME);

    private EventRouter eventRouter;

    protected Collection<Entity> itemsToCreate = new HashSet<>();
    protected Collection<Entity> itemsToUpdate = new HashSet<>();
    protected Collection<Entity> itemsToDelete = new HashSet<>();
    protected Instance.PropertyChangeListener listener = new ItemListener();

    protected boolean listenersEnabled = true;

    protected boolean loadDynamicAttributes;

    /**
     * Use EventRouter for listeners instead of fields with listeners List.
     *
     * @return lazily initialized {@link EventRouter} instance.
     * @see EventRouter
     */
    protected EventRouter getEventRouter() {
        if (eventRouter == null) {
            eventRouter = new EventRouter();
        }
        return eventRouter;
    }

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
        return (Collection<T>) itemsToCreate;
    }

    @Override
    public Collection<T> getItemsToUpdate() {
        return (Collection<T>) itemsToUpdate;
    }

    @Override
    public Collection<T> getItemsToDelete() {
        return (Collection<T>) itemsToDelete;
    }

    @Override
    public void modified(T item) {
        if (PersistenceHelper.isNew(item)) {
            itemsToCreate.remove(item);
            itemsToCreate.add(item);
        } else {
            itemsToUpdate.remove(item);
            itemsToUpdate.add(item);
        }
        itemsToDelete.remove(item);
        modified = true;
    }

    @Override
    public void deleted(T item) {
        if (PersistenceHelper.isNew(item)) {
            itemsToCreate.remove(item);
        } else {
            itemsToDelete.add(item);
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
            if (sibling instanceof NestedDatasource
                    && ((NestedDatasource) sibling).getMaster().equals(this)
                    && !metadata.getTools().isEmbeddable(sibling.getMetaClass())) {
                // Look for corresponding property datasource in the Parent's DsContext
                for (Datasource siblingOfParent : parentDs.getDsContext().getAll()) {
                    if (siblingOfParent instanceof NestedDatasource
                            && ((NestedDatasource) siblingOfParent).getProperty().equals(((NestedDatasource) sibling).getProperty())
                            && ((NestedDatasource) siblingOfParent).getMaster() == parentDs) {
                        // If such corresponding datasource found, set it as a parent for our property datasource
                        ((DatasourceImplementation) sibling).setParent(siblingOfParent);
                    }
                }
            }
        }
    }

    @Override
    public void addItemChangeListener(ItemChangeListener<T> listener) {
        getEventRouter().addListener(ItemChangeListener.class, listener);
    }

    @Override
    public void removeItemChangeListener(ItemChangeListener<T> listener) {
        getEventRouter().removeListener(ItemChangeListener.class, listener);
    }

    @Override
    public void addItemPropertyChangeListener(ItemPropertyChangeListener<T> listener) {
        getEventRouter().addListener(ItemPropertyChangeListener.class, listener);
    }

    @Override
    public void removeItemPropertyChangeListener(ItemPropertyChangeListener<T> listener) {
        getEventRouter().removeListener(ItemPropertyChangeListener.class, listener);
    }

    @Override
    public void addStateChangeListener(StateChangeListener<T> listener) {
        getEventRouter().addListener(StateChangeListener.class, listener);
    }

    @Override
    public void removeStateChangeListener(StateChangeListener<T> listener) {
        getEventRouter().removeListener(StateChangeListener.class, listener);
    }

    @Override
    public void clearCommitLists() {
        itemsToCreate.clear();
        itemsToUpdate.clear();
        itemsToDelete.clear();
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
        ItemChangeEvent<T> itemChangeEvent = new ItemChangeEvent<>(this, prevItem, getItem());
        //noinspection unchecked
        getEventRouter().fireEvent(ItemChangeListener.class, ItemChangeListener::itemChanged, itemChangeEvent);
    }

    @SuppressWarnings("unchecked")
    protected void fireStateChanged(State prevStatus) {
        StateChangeEvent<T> stateChangeEvent = new StateChangeEvent<>(this, prevStatus, getState());
        getEventRouter().fireEvent(StateChangeListener.class, StateChangeListener::stateChanged, stateChangeEvent);
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

            modified((T) e.getItem());

            ItemPropertyChangeEvent<T> itemPropertyChangeEvent = new ItemPropertyChangeEvent<>(AbstractDatasource.this,
                    (T) e.getItem(), e.getProperty(), e.getPrevValue(), e.getValue());
            getEventRouter().fireEvent(ItemPropertyChangeListener.class, ItemPropertyChangeListener::itemPropertyChanged,
                    itemPropertyChangeEvent);
        }
    }

    @Override
    public String toString() {
        return id + "{modified=" + modified + ", parent=" + parentDs + "}";
    }
}