/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 15:06:46
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataServiceRemote;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.xml.ParametersHelper;
import org.apache.commons.lang.ObjectUtils;

import java.util.*;

public class CollectionDatasourceImpl<T extends Entity, K>
    extends
        AbstractCollectionDatasource<T, K>
    implements
        CollectionDatasource<T, K>
{

    protected Data data = new Data(Collections.<K>emptyList(), Collections.<K,T>emptyMap());

    public CollectionDatasourceImpl(
            DsContext context, DataService dataservice,
                String id, MetaClass metaClass, String viewName)
    {
        super(context, dataservice, id, metaClass, viewName);
    }

    @Override
    public synchronized void invalidate() {
        super.invalidate();
        this.data = new Data(Collections.<K>emptyList(), Collections.<K, T>emptyMap());
    }

    @Override
    public synchronized void refresh() {
        refresh(Collections.<String, Object>emptyMap());
    }

    public void refresh(Map<String, Object> parameters) {
        Collection prevIds = data.itemIds;
        invalidate();

        data = loadData(parameters);

        State prevState = state;
        if (!prevState.equals(State.VALID)) {
            state = State.VALID;
            forceStateChanged(prevState);
        }

        if (prevIds != null && this.item != null && !prevIds.contains(this.item.getId())) {
            setItem(null);
        } else if (this.item != null) {
            setItem(getItem((K) this.item.getId()));
        } else {
            setItem(null);
        }

        forceCollectionChanged(new CollectionDatasourceListener.CollectionOperation<T>(CollectionDatasourceListener.CollectionOperation.Type.REFRESH, null));
    }

    public synchronized T getItem(K key) {
        if (State.NOT_INITIALIZAED.equals(state)) {
            throw new IllegalStateException("Invalid datasource state " + state);
        } else {
            final T item = (T) data.itemsByKey.get(key);
            attachListener((Instance) item);
            return item;
        }
    }

    public K getItemId(T item) {
        return item == null ? null : (K) item.getId();
    }

    public synchronized Collection<K> getItemIds() {
        if (State.NOT_INITIALIZAED.equals(state)) {
            return Collections.emptyList();
        } else {
            return (Collection<K>) data.itemIds;
        }
    }

    public synchronized int size() {
        if (State.NOT_INITIALIZAED.equals(state)) {
            return 0;
        } else {
            return data.itemIds.size();
        }
    }

    public synchronized void addItem(T item) throws UnsupportedOperationException {
        if (!ObjectUtils.equals(state, State.VALID)) throw new IllegalStateException("Datasource have state" + state);

        data.itemIds.add((K) item.getId());
        data.itemsByKey.put((K)item.getId(), item);

        if (PersistenceHelper.isNew(item)) {
            itemToCreate.add(item);
        }

        modified = true;
        forceCollectionChanged(
                new CollectionDatasourceListener.CollectionOperation<T>(
                        CollectionDatasourceListener.CollectionOperation.Type.ADD, null));
    }

    public synchronized void removeItem(T item) throws UnsupportedOperationException {
        if (!ObjectUtils.equals(state, State.VALID)) throw new IllegalStateException("Datasource have state" + state);

        data.itemIds.remove((K) item.getId());
        data.itemsByKey.remove((K)item.getId());

        if (PersistenceHelper.isNew(item)) {
            itemToCreate.remove(item);
        } else {
            itemToDelete.add(item);
        }

        modified = true;
        forceCollectionChanged(
                new CollectionDatasourceListener.CollectionOperation<T>(
                    CollectionDatasourceListener.CollectionOperation.Type.REMOVE, null));
    }

    public synchronized boolean containsItem(K itemId) {
        return data.itemIds.contains(itemId);
    }

    @Override
    public void commit() {
        if (Datasource.CommitMode.DATASTORE.equals(getCommitMode())) {
            final DataService service = getDataService();
            Set<Entity> commitInstances = new HashSet<Entity>();
            Set<Entity> deleteInstances = new HashSet<Entity>();

            commitInstances.addAll(itemToCreate);
            commitInstances.addAll(itemToUpdate);
            deleteInstances.addAll(itemToDelete);

            final Map<Entity, Entity> map =
                    service.commit(new DataServiceRemote.CommitContext<Entity>(commitInstances, deleteInstances));
            commited(map);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void commited(Map<Entity, Entity> map) {
        if (map.containsKey(item)) {
            item = (T) map.get(item);
            // TODO update collection elements
        }

        modified = false;
        clearCommitLists();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected class Data {
        protected Collection<K> itemIds = Collections.emptyList();
        protected Map<K, T> itemsByKey = Collections.emptyMap();

        public Data(Collection<K> itemIds, Map<K, T> itemsByKey) {
            this.itemIds = itemIds;
            this.itemsByKey = itemsByKey;
        }
    }

    protected Data loadData(Map<String, Object> params) {
        final DataServiceRemote.CollectionLoadContext context =
                new DataServiceRemote.CollectionLoadContext(metaClass);

        if (query != null && queryParameters != null) {
            final Map<String, Object> parameters = getQueryParameters(params);
            for (ParametersHelper.ParameterInfo info : queryParameters) {
                if (ParametersHelper.ParameterInfo.Type.DATASOURCE.equals(info.getType())) {
                    final Object value = parameters.get(info.getFlatName());
                    if (value == null) return new Data(Collections.<K>emptyList(), Collections.<K,T>emptyMap());
                }
            }
            context.setQueryString(getJPQLQuery(this.query, parameters)).setParameters(parameters);
        } else {
            context.setQueryString("select e from " + metaClass.getName() + " e");
        }

        context.setView(view);

        @SuppressWarnings({"unchecked"})
        final Collection<T> entities = (Collection) dataservice.loadList(context);
        return wrapAsData(entities);
    }

    protected Data wrapAsData(Collection<T> entities) {
        List<K> ids = new ArrayList<K>();
        Map<K, T> itemsById = new HashMap<K,T>();

        for (T entity : entities) {
            final K id = (K) entity.getId();
            ids.add(id);
            itemsById.put(id, entity);
        }

        return new Data(ids, itemsById);
    }

}
