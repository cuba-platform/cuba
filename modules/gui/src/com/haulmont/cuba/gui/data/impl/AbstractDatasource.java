/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 13.02.2009 15:35:22
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.common.ValueListener;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataHelper;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.haulmont.cuba.gui.data.NestedDatasource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.*;

public abstract class AbstractDatasource<T extends Entity>
    implements
        Datasource<T>, DatasourceImplementation<T>
{
    private static Log log = LogFactory.getLog(AbstractDatasource.class);

    protected String id;
    protected boolean modified;
    protected CommitMode commitMode;
    protected Datasource parentDs;
    protected Metadata metadata;

    protected List<DatasourceListener> dsListeners = new ArrayList<DatasourceListener>();

    protected Collection itemToCreate = new HashSet();
    protected Collection itemToUpdate = new HashSet();
    protected Collection itemToDelete = new HashSet();
    protected ValueListener listener;

    protected volatile boolean listenersEnabled = true;

    public AbstractDatasource(String id) {
        this.id = id;
        listener = new ItemListener();
        commitMode = CommitMode.DATASTORE;
        metadata = AppBeans.get(Metadata.class);
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

    public Collection getItemsToCreate() {
        return Collections.unmodifiableCollection(itemToCreate);
    }

    public Collection getItemsToUpdate() {
        return Collections.unmodifiableCollection(itemToUpdate);
    }

    public Collection getItemsToDelete() {
        return Collections.unmodifiableCollection(itemToDelete);
    }

    public void modified(T item) {
        if (PersistenceHelper.isNew(item)) {
            itemToCreate.remove(item);
            itemToCreate.add(item);
        } else {
            itemToUpdate.add(item);
        }
        modified = true;
    }

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

    public CommitMode getCommitMode() {
        return commitMode;
    }

    public void setCommitMode(CommitMode commitMode) {
        this.commitMode = commitMode;
    }

    public Datasource getParent() {
        return parentDs;
    }

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

    protected void fireItemChanged(Object prevItem) {
        for (DatasourceListener dsListener : new ArrayList<DatasourceListener>(dsListeners)) {
            dsListener.itemChanged(this, (Entity) prevItem, getItem());
        }
    }

    protected void fireStateChanged(State prevStatus) {
        for (DatasourceListener dsListener : new ArrayList<DatasourceListener>(dsListeners)) {
            dsListener.stateChanged(this, prevStatus, getState());
        }
    }

    protected class ItemListener implements ValueListener, Serializable {

        private static final long serialVersionUID = 358102907204482975L;

        public void propertyChanged(Object item, String property, Object prevValue, Object value) {
            if (!listenersEnabled)
                return;

            log.trace("propertyChanged: item=" + item + ", property=" + property + ", value=" + value + ", prevValue=" + prevValue);

            for (DatasourceListener dsListener : new ArrayList<DatasourceListener>(dsListeners)) {
                dsListener.valueChanged(item, property, prevValue, value);
            }
            if (!MetadataHelper.isTransient(item, property))
                modified((T)item);
        }
    }

    @Override
    public String toString() {
        return id + "{modified=" + modified + ", parent=" + parentDs + "}";
    }
}
