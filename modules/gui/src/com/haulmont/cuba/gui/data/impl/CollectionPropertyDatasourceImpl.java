/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 16:06:25
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.MetadataHelper;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import org.apache.commons.lang.ObjectUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.ArrayList;

public class CollectionPropertyDatasourceImpl<T extends Entity, K>
    extends
        PropertyDatasourceImpl<T>
    implements
        CollectionDatasource<T, K>
{
    private T item;
    protected boolean cascadeProperty;

    public CollectionPropertyDatasourceImpl(String id, Datasource<Entity> ds, String property) {
        super(id, ds, property);

        final MetaClass metaClass = ds.getMetaClass();
        final MetaProperty metaProperty = metaClass.getProperty(property);
        cascadeProperty = MetadataHelper.isCascade(metaProperty);
    }

    @Override
    protected void initParentDsListeners() {
        ds.addListener(new DatasourceListener<Entity>() {
            public void itemChanged(Datasource<Entity> ds, Entity prevItem, Entity item) {
                forceCollectionChanged(CollectionDatasourceListener.Operation.REFRESH);
            }

            public void stateChanged(Datasource<Entity> ds, State prevState, State state) {
                for (DatasourceListener dsListener : new ArrayList<DatasourceListener>(dsListeners)) {
                    dsListener.stateChanged(CollectionPropertyDatasourceImpl.this, prevState, state);
                }
                forceCollectionChanged(CollectionDatasourceListener.Operation.REFRESH);
            }

            public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                // Do nothing
            }
        });
    }

    public T getItem(K key) {
        return (T) key;
    }

    public K getItemId(T item) {
        return (K) item;
    }

    public Collection<K> getItemIds() {
        if (State.NOT_INITIALIZAED.equals(ds.getState())) {
            return Collections.emptyList();
        } else {
            return (Collection<K>) __getCollection();
        }
    }

    @Override
    public T getItem() {
        if (State.VALID.equals(getState())) return item;
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void setItem(T item) {
        if (State.VALID.equals(getState())) {
            Object prevItem = this.item;

            if (!ObjectUtils.equals(prevItem, item)) {
                if (this.item != null) {
                    detachListener((Instance) this.item);
                }

                if (item instanceof Instance) {
                    final MetaClass aClass = ((Instance) item).getMetaClass();
                    if (!aClass.equals(getMetaClass())) {
                        throw new IllegalStateException(String.format("Invalid item metaClass"));
                    }
                    attachListener((Instance) item);
                }
                this.item = item;

                forceItemChanged(prevItem);
            }
        }
    }

    @Override
    public void refresh() {
        forceCollectionChanged(CollectionDatasourceListener.Operation.REFRESH);
    }

    public int size() {
        if (State.NOT_INITIALIZAED.equals(ds.getState())) {
            return 0;
        } else {
            final Collection<T> collection = __getCollection();
            return collection == null ? 0 : collection.size();
        }
    }

    protected Collection<T> __getCollection() {
        final Instance item = (Instance) ds.getItem();
        return item == null ? null : (Collection<T>) item.getValue(metaProperty.getName());
    }

    private void checkState() {
        if (!State.VALID.equals(getState()))
            throw new IllegalStateException("Invalid datasource state: " + getState());
    }

    public void addItem(T item) throws UnsupportedOperationException {
        checkState();
        __getCollection().add(item);

        modified = true;
        if (cascadeProperty) {
            final Entity parentItem = ds.getItem();
            ((DatasourceImplementation) ds).modified(parentItem);
        } else {
            modified(item);
        }

        forceCollectionChanged(CollectionDatasourceListener.Operation.ADD);
    }

    public void removeItem(T item) throws UnsupportedOperationException {
        checkState();
        __getCollection().remove(item);

        modified = true;
        if (cascadeProperty) {
            final Entity parentItem = ds.getItem();
            ((DatasourceImplementation) ds).modified(parentItem);
        } else {
            deleted(item);
        }

        forceCollectionChanged(CollectionDatasourceListener.Operation.REMOVE);
    }

    public void updateItem(T item) {
        // do nothing ?
    }

    public boolean containsItem(K itemId) {
        return __getCollection().contains(itemId);
    }

    public String getQuery() {
        return null;
    }

    public void setQuery(String query) {
        throw new UnsupportedOperationException();
    }

    public void refresh(Map<String, Object> parameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CommitMode getCommitMode() {
        final MetaProperty.Type type = metaProperty.getType();
        return MetaProperty.Type.AGGREGATION.equals(type) ? CommitMode.NOT_SUPPORTED : CommitMode.DATASTORE;
    }

    @Override
    public void commited(Map<Entity, Entity> map) {
        modified = false;
        clearCommitLists();
    }

    protected void forceCollectionChanged(CollectionDatasourceListener.Operation operation) {
        for (DatasourceListener dsListener : dsListeners) {
            if (dsListener instanceof CollectionDatasourceListener) {
                ((CollectionDatasourceListener) dsListener).collectionChanged(this, operation);
            }
        }
    }
}
