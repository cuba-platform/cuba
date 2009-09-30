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
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.xml.ParametersHelper;
import org.apache.commons.collections.map.LinkedMap;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.util.*;

public class CollectionDatasourceImpl<T extends Entity<K>, K>
    extends
        AbstractCollectionDatasource<T, K>
    implements
        CollectionDatasource.Sortable<T, K>
{
    protected LinkedMap data = new LinkedMap();

    protected SortInfo<MetaPropertyPath>[] sortInfos;

    private Map<String, Object> savedParameters;

    private boolean inRefresh;

    public CollectionDatasourceImpl(
            DsContext context, DataService dataservice,
                String id, MetaClass metaClass, String viewName)
    {
        super(context, dataservice, id, metaClass, viewName);
    }

    public CollectionDatasourceImpl(
            DsContext context, DataService dataservice,
                String id, MetaClass metaClass, String viewName, boolean softDeletion)
    {
        super(context, dataservice, id, metaClass, viewName);
        setSoftDeletion(softDeletion);
    }

    @Override
    public synchronized void invalidate() {
        super.invalidate();
    }

    @Override
    public synchronized void refresh() {
        if (savedParameters == null)
            refresh(Collections.<String, Object>emptyMap());
        else
            refresh(savedParameters);
    }

    public void refresh(Map<String, Object> parameters) {
        if (inRefresh)
            return;

        inRefresh = true;
        try {
            savedParameters = parameters;

            Collection prevIds = data.keySet();
            invalidate();

            loadData(parameters);

            State prevState = state;
            if (!prevState.equals(State.VALID)) {
                state = State.VALID;
                forceStateChanged(prevState);
            }

            if (prevIds != null && this.item != null && !prevIds.contains(this.item.getId())) {
                setItem(null);
            } else if (this.item != null) {
                setItem(getItem(this.item.getId()));
            } else {
                setItem(null);
            }

            forceCollectionChanged(CollectionDatasourceListener.Operation.REFRESH);
        } finally {
            inRefresh = false;
        }
    }

    public synchronized T getItem(K key) {
        if (State.NOT_INITIALIZED.equals(state)) {
            throw new IllegalStateException("Invalid datasource state " + state);
        } else {
            final T item = (T) data.get(key);
            return item;
        }
    }

    public K getItemId(T item) {
        return item == null ? null : item.getId();
    }

    public synchronized Collection<K> getItemIds() {
        if (State.NOT_INITIALIZED.equals(state)) {
            return Collections.emptyList();
        } else {
            return (Collection<K>) data.keySet();
        }
    }

    public synchronized int size() {
        if (State.NOT_INITIALIZED.equals(state)) {
            return 0;
        } else {
            return data.size();
        }
    }

    public void sort(SortInfo[] sortInfos) {
        if (sortInfos.length != 1)
            throw new UnsupportedOperationException("Supporting sort by one field only");

        if (!Arrays.equals(this.sortInfos, sortInfos)) {
            //noinspection unchecked
            this.sortInfos = sortInfos;
            doSort();
        }
    }

    protected void doSort() {
        final MetaPropertyPath propertyPath = sortInfos[0].getPropertyPath();
        final boolean asc = Order.ASC.equals(sortInfos[0].getOrder());

        @SuppressWarnings({"unchecked"})
        List<T> list = new ArrayList<T>(data.values());
        Collections.sort(list, new EntityComparator<T>(propertyPath, asc));
        data.clear();
        for (T t : list) {
            data.put(t.getId(), t);
        }
    }

    public K firstItemId() {
        if (!data.isEmpty()) {
            return (K) data.firstKey();
        }
        return null;
    }

    public K lastItemId() {
        if (!data.isEmpty()) {
            return (K) data.lastKey();
        }
        return null;
    }

    public K nextItemId(K itemId) {
        return (K) data.nextKey(itemId);
    }

    public K prevItemId(K itemId) {
        return (K) data.previousKey(itemId);
    }

    public boolean isFirstId(K itemId) {
        return itemId != null && itemId.equals(firstItemId());
    }

    public boolean isLastId(K itemId) {
        return itemId != null && itemId.equals(lastItemId());
    }

    private void checkState() {
        if (!State.VALID.equals(state)) {
            refresh();
        }
    }

    public synchronized void addItem(T item) throws UnsupportedOperationException {
        checkState();

        data.put(item.getId(), item);
        attachListener((Instance) item);

        if (PersistenceHelper.isNew(item)) {
            itemToCreate.add(item);
        }

        modified = true;
        forceCollectionChanged(CollectionDatasourceListener.Operation.ADD);
    }

    public synchronized void removeItem(T item) throws UnsupportedOperationException {
        checkState();

        data.remove(item.getId());
        detachListener((Instance) item);

        if (PersistenceHelper.isNew(item)) {
            itemToCreate.remove(item);
        } else {
            itemToDelete.add(item);
        }

        modified = true;
        forceCollectionChanged(CollectionDatasourceListener.Operation.REMOVE);
    }

    public void updateItem(T item) {
        checkState();

        if (data.containsKey(item.getId())) {
            data.put(item.getId(), item);
            attachListener((Instance) item);
            forceCollectionChanged(CollectionDatasourceListener.Operation.REFRESH);
        }
    }

    public synchronized boolean containsItem(K itemId) {
        return data.containsKey(itemId);
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

            CommitContext<Entity> context =
                    new CommitContext<Entity>(commitInstances, deleteInstances);
            for (Entity entity : commitInstances) {
                context.getViews().put(entity, getView());
            }
            for (Entity entity : deleteInstances) {
                context.getViews().put(entity, getView());
            }

            final Map<Entity, Entity> map = service.commit(context);

            commited(map);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void commited(Map<Entity, Entity> map) {
        if (map.containsKey(item)) {
            item = (T) map.get(item);
        }
        for (Entity newEntity : map.values()) {
            updateItem((T) newEntity);
        }

        modified = false;
        clearCommitLists();
    }

    protected void loadData(Map<String, Object> params) {
        StopWatch sw = new Log4JStopWatch("CDS " + id);

        for (Object entity : data.values()) {
            detachListener((Instance) entity);
        }
        data.clear();

        final LoadContext context =
                new LoadContext(metaClass);

        if (query != null && queryParameters != null) {
            final Map<String, Object> parameters = getQueryParameters(params);
            for (ParametersHelper.ParameterInfo info : queryParameters) {
                if (ParametersHelper.ParameterInfo.Type.DATASOURCE.equals(info.getType())) {
                    final Object value = parameters.get(info.getFlatName());
                    if (value == null)
                        return;
                }
            }

            String queryString = getJPQLQuery(getTemplateParams(params));
            context.setQueryString(queryString).setParameters(parameters);
        } else {
            context.setQueryString("select e from " + metaClass.getName() + " e");
        }

        context.setView(view);
        context.setSoftDeletion(isSoftDeletion());

        final Collection<T> entities = dataservice.loadList(context);

        for (T entity : entities) {
            data.put(entity.getId(), entity);
            attachListener((Instance) entity);
        }

        sw.stop();
    }
}
