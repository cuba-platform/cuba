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
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import org.apache.commons.lang.ObjectUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CollectionPropertyDatasourceImpl<T extends Entity, K>
    extends
        PropertyDatasourceImpl<T>
    implements
        CollectionDatasource<T, K>
{
    private T item;

    public CollectionPropertyDatasourceImpl(String id, Datasource<Entity> ds, String property) {
        super(id, ds, property);
        
        ds.addListener(new DatasourceListener<Entity>() {
            public void itemChanged(Datasource<Entity> ds, Entity prevItem, Entity item) {
                forceCollectionChanged(
                        new CollectionDatasourceListener.CollectionOperation<T>(
                                CollectionDatasourceListener.CollectionOperation.Type.REFRESH, null));
            }

            public void stateChanged(Datasource<Entity> ds, State prevState, State state) {
                forceCollectionChanged(
                        new CollectionDatasourceListener.CollectionOperation<T>(
                                CollectionDatasourceListener.CollectionOperation.Type.REFRESH, null));
            }

            public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                // Do nothing
            }
        });
    }

    public T getItem(K key) {
        return (T) key;
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
                    detatchListener((Instance) this.item);
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
        // Do nothing
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

    public void addItem(T item) throws UnsupportedOperationException {
        if (!ObjectUtils.equals(getState(), State.VALID)) throw new IllegalStateException("Datasource have state" + getState());
        __getCollection().add(item);

        modified = true;
        forceCollectionChanged(
                new CollectionDatasourceListener.CollectionOperation<T>(
                        CollectionDatasourceListener.CollectionOperation.Type.ADD, null));
    }

    public void removeItem(T item) throws UnsupportedOperationException {
        if (!ObjectUtils.equals(getState(), State.VALID)) throw new IllegalStateException("Datasource have state" + getState());
        __getCollection().remove(item);

        modified = true;
        forceCollectionChanged(
                new CollectionDatasourceListener.CollectionOperation<T>(
                    CollectionDatasourceListener.CollectionOperation.Type.REMOVE, null));
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

    protected void forceCollectionChanged(CollectionDatasourceListener.CollectionOperation operation) {
        for (DatasourceListener dsListener : dsListeners) {
            if (dsListener instanceof CollectionDatasourceListener) {
                ((CollectionDatasourceListener) dsListener).collectionChanged(this, operation);
            }
        }
    }
}
