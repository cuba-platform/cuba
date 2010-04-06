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
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.components.AggregationInfo;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.xml.ParameterInfo;
import org.apache.commons.collections.map.LinkedMap;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.util.*;

public class CollectionDatasourceImpl<T extends Entity<K>, K>
    extends
        AbstractCollectionDatasource<T, K>
    implements
        CollectionDatasource.Sortable<T, K>,
        CollectionDatasource.Aggregatable<T, K>
{
    protected LinkedMap data = new LinkedMap();

    protected SortInfo<MetaPropertyPath>[] sortInfos;

    private Map<String, Object> savedParameters;

    private boolean inRefresh;

    private AggregatableDelegate<K> aggregatableDelegate = new AggregatableDelegate<K>() {
        @Override
        public Object getItem(K itemId) {
            return CollectionDatasourceImpl.this.getItem(itemId);
        }

        public Object getItemValue(MetaPropertyPath property, K itemId) {
            return CollectionDatasourceImpl.this.getItemValue(property, itemId);
        }
    };

    public CollectionDatasourceImpl(
            DsContext context, DataService dataservice,
                String id, MetaClass metaClass, String viewName)
    {
        super(context, dataservice, id, metaClass, viewName);
    }

     public CollectionDatasourceImpl(
            DsContext context, DataService dataservice,
                String id, MetaClass metaClass, View view)
    {
        super(context, dataservice, id, metaClass, view);
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
        @SuppressWarnings({"unchecked"})
        List<T> list = new ArrayList<T>(data.values());
        Collections.sort(list, createEntityComparator());
        data.clear();
        for (T t : list) {
            data.put(t.getId(), t);
        }
    }

    protected Comparator<T> createEntityComparator() {
        final MetaPropertyPath propertyPath = sortInfos[0].getPropertyPath();
        final boolean asc = Order.ASC.equals(sortInfos[0].getOrder());
        return new EntityComparator<T>(propertyPath, asc);
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

        deleted(item);

        forceCollectionChanged(CollectionDatasourceListener.Operation.REMOVE);
    }

    public void modifyItem(T item) {
        if (data.containsKey(item.getId())) {
            if (PersistenceHelper.isNew(item)) {
                Object existingItem = data.get(item.getId());
                InstanceUtils.copy((Instance) item, (Instance) existingItem);
                modified((T) existingItem);
            } else {
                updateItem(item);
                modified(item);
            }
        }
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

        final LoadContext context = new LoadContext(metaClass);

        LoadContext.Query q;

        if (query != null && queryParameters != null) {
            final Map<String, Object> parameters = getQueryParameters(params);
            for (ParameterInfo info : queryParameters) {
                if (ParameterInfo.Type.DATASOURCE.equals(info.getType())) {
                    final Object value = parameters.get(info.getFlatName());
                    if (value == null)
                        return;
                }
            }

            String queryString = getJPQLQuery(getTemplateParams(params));
            q = context.setQueryString(queryString);
            q.setParameters(parameters);
        } else {
            q = context.setQueryString("select e from " + metaClass.getName() + " e");
        }

        if (maxResults > 0) {
            q.setMaxResults(maxResults);
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

    @SuppressWarnings("unchecked")
    public Map<Object, String> aggregate(AggregationInfo[] aggregationInfos, Collection itemIds) {
        return aggregatableDelegate.aggregate(aggregationInfos, itemIds);
    }

    protected Object getItemValue(MetaPropertyPath property, K itemId) {
        Instance instance = (Instance) getItem(itemId);
        if (property.getMetaProperties().length == 1) {
            return instance.getValue(property.getMetaProperty().getName());
        } else {
            return instance.getValueEx(property.toString());
        }
    }
}
