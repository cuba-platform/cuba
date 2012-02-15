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
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.AggregationInfo;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.filter.Condition;
import com.haulmont.cuba.gui.filter.DenyingClause;
import com.haulmont.cuba.gui.filter.LogicalCondition;
import com.haulmont.cuba.gui.filter.LogicalOp;
import org.apache.commons.collections.map.LinkedMap;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.util.*;

public class CollectionDatasourceImpl<T extends Entity<K>, K>
        extends
        AbstractCollectionDatasource<T, K>
        implements
        CollectionDatasource.Sortable<T, K>,
        CollectionDatasource.Aggregatable<T, K>,
        CollectionDatasource.Suspendable<T, K>,
        CollectionDatasource.SupportsPaging<T, K> {

    protected LinkedMap data = new LinkedMap();

    private boolean inRefresh;
    protected RefreshMode refreshMode = RefreshMode.ALWAYS;

    private AggregatableDelegate<K> aggregatableDelegate = new AggregatableDelegate<K>() {
        @Override
        public Object getItem(K itemId) {
            return CollectionDatasourceImpl.this.getItem(itemId);
        }

        public Object getItemValue(MetaPropertyPath property, K itemId) {
            return CollectionDatasourceImpl.this.getItemValue(property, itemId);
        }
    };

    protected boolean suspended;

    protected boolean refreshOnResumeRequired;

    protected int firstResult;

    protected boolean sortOnDb = ConfigProvider.getConfig(ClientConfig.class).getCollectionDatasourceDbSortEnabled();

    /**
     * This constructor is invoked by DsContextLoader, so inheritors must contain a constructor
     * with the same signature
     */
    public CollectionDatasourceImpl(
            DsContext context, DataService dataservice,
            String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

    public CollectionDatasourceImpl(
            DsContext context, DataService dataservice,
            String id, MetaClass metaClass, View view) {
        super(context, dataservice, id, metaClass, view);
    }

    public CollectionDatasourceImpl(
            DsContext context, DataService dataservice,
            String id, MetaClass metaClass, String viewName, boolean softDeletion) {
        super(context, dataservice, id, metaClass, viewName);
        setSoftDeletion(softDeletion);
    }

    public CollectionDatasourceImpl(
            DsContext context, DataService dataservice,
            String id, MetaClass metaClass, View view, boolean softDeletion) {
        super(context, dataservice, id, metaClass, view);
        setSoftDeletion(softDeletion);
    }

    @Override
    public synchronized void invalidate() {
        super.invalidate();
    }

    public void refreshIfNotSuspended() {
        if (suspended) {
            if (!state.equals(State.VALID)) {
                state = State.VALID;
            }
            refreshOnResumeRequired = true;
        } else {
            refresh();
        }
    }

    @Override
    public synchronized void refresh() {
        if (savedParameters == null)
            refresh(Collections.<String, Object>emptyMap());
        else
            refresh(savedParameters);
    }

    @Override
    public void refresh(Map<String, Object> parameters) {
        if (inRefresh)
            return;

        if (refreshMode == RefreshMode.NEVER)
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
                fireStateChanged(prevState);
            }

            if (prevIds != null && this.item != null && !prevIds.contains(this.item.getId())) {
                setItem(null);
            } else if (this.item != null) {
                setItem(getItem(this.item.getId()));
            } else {
                setItem(null);
            }

            if (sortInfos != null && sortInfos.length > 0)
                doSort();

            suspended = false;
            refreshOnResumeRequired = false;

            forceCollectionChanged(CollectionDatasourceListener.Operation.REFRESH);

            checkDataLoadError();
        } finally {
            inRefresh = false;
        }
    }

    public RefreshMode getRefreshMode() {
        return refreshMode;
    }

    public void setRefreshMode(RefreshMode refreshMode) {
        this.refreshMode = refreshMode;
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
        if (State.NOT_INITIALIZED.equals(state) || suspended) {
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
            if (data.size() > 0) {
                if (!sortOnDb || containsAllDataFromDb()) {
                    doSort();
                } else {
                    refresh();
                }
            }
        }
    }

    protected boolean containsAllDataFromDb() {
        return firstResult == 0 && data.size() < maxResults;
    }

    protected void doSort() {
        List<T> list = new ArrayList<T>(data.values());
        Collections.sort(list, createEntityComparator());
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
        attachListener(item);

        if (PersistenceHelper.isNew(item)) {
            itemToCreate.add(item);
        }

        modified = true;
        forceCollectionChanged(CollectionDatasourceListener.Operation.ADD);
    }

    public synchronized void removeItem(T item) throws UnsupportedOperationException {
        checkState();

        data.remove(item.getId());
        detachListener(item);

        deleted(item);

        if (this.item != null && this.item.equals(item)) {
            setItem(null);
        }

        forceCollectionChanged(CollectionDatasourceListener.Operation.REMOVE);
    }

    public synchronized void includeItem(T item) throws UnsupportedOperationException {
        checkState();

        data.put(item.getId(), item);
        attachListener(item);

        forceCollectionChanged(CollectionDatasourceListener.Operation.ADD);
    }

    public synchronized void excludeItem(T item) throws UnsupportedOperationException {
        checkState();

        data.remove(item.getId());
        detachListener(item);

        if (this.item != null && this.item.equals(item)) {
            this.item = null;
        }

        forceCollectionChanged(CollectionDatasourceListener.Operation.REMOVE);
    }

    public synchronized void clear() throws UnsupportedOperationException {
        // Get items
        List<Object> collectionItems = new ArrayList<Object>(data.values());
        // Clear container
        data.clear();
        // Notify listeners
        for (Object obj : collectionItems) {
            T item = (T) obj;
            detachListener(item);
            if (state == State.VALID)
                forceCollectionChanged(CollectionDatasourceListener.Operation.REMOVE);
        }
    }

    public void revert() throws UnsupportedOperationException {
        if (refreshMode != RefreshMode.NEVER)
            refresh();
        else {
            clear();
            invalidate();
            valid();
        }
    }

    public void modifyItem(T item) {
        if (data.containsKey(item.getId())) {
            if (PersistenceHelper.isNew(item)) {
                Object existingItem = data.get(item.getId());
                InstanceUtils.copy(item, (Instance) existingItem);
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
            attachListener(item);
            forceCollectionChanged(CollectionDatasourceListener.Operation.REFRESH);
        }

        if (this.item != null && this.item.equals(item)) {
            this.item = item;
        }
    }

    public synchronized boolean containsItem(K itemId) {
        return data.containsKey(itemId);
    }

    public void committed(Set<Entity> entities) {
        for (Entity newEntity : entities) {
            if (newEntity.equals(item))
                item = (T) newEntity;

            updateItem((T) newEntity);
        }

        modified = false;
        clearCommitLists();
    }

    protected boolean needLoading() {
        if (filter != null) {
            if (filter.getRoot() instanceof DenyingClause)
                return false;
            if ((filter.getRoot() instanceof LogicalCondition)
                    && ((LogicalCondition) filter.getRoot()).getOperation().equals(LogicalOp.AND)) {
                for (Condition condition : filter.getRoot().getConditions()) {
                    if (condition instanceof DenyingClause) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public LoadContext getCompiledLoadContext() {
        LoadContext context = new LoadContext(metaClass);
        Map<String, Object> params;
        if (savedParameters == null) {
            params = Collections.emptyMap();
        } else
            params = savedParameters;
        LoadContext.Query q = createLoadContextQuery(context, params);
        if (sortInfos != null && sortOnDb) {
            setSortDirection(q);
        }
        context.setView(view);
        return context;
    }

    /**
     * Load data from middleware into {@link #data} field.
     * <p>This method can be overridden in descendants to provide specific load functionality.</p>
     * <p>In case of error sets {@link #dataLoadError} field to the exception object.</p>
     * @param params    datasource parameters, as described in {@link CollectionDatasource#refresh(java.util.Map)}
     */
    protected void loadData(Map<String, Object> params) {
        StopWatch sw = new Log4JStopWatch("CDS " + id);

        if (needLoading()) {
            final LoadContext context = new LoadContext(metaClass);

            LoadContext.Query q = createLoadContextQuery(context, params);
            if (q == null) {
                detachListener(data.values());
                data.clear();
                return;
            }

            if (sortInfos != null && sortOnDb) {
                setSortDirection(q);
            }

            if (firstResult > 0)
                q.setFirstResult(firstResult);

            if (maxResults > 0) {
                q.setMaxResults(maxResults);
            }

            context.setView(view);
            context.setSoftDeletion(isSoftDeletion());

            dataLoadError = null;
            try {
                final Collection<T> entities = dataservice.loadList(context);

                detachListener(data.values());
                data.clear();

                for (T entity : entities) {
                    data.put(entity.getId(), entity);
                    attachListener(entity);
                }
            } catch (Throwable e) {
                dataLoadError = e;
            }
        }

        sw.stop();
    }

    protected void detachListener(Collection instances) {
        for (Object obj : instances) {
            if (obj instanceof Instance)
                detachListener((Instance) obj);
        }
    }
    
    protected void setSortDirection(LoadContext.Query q) {
        boolean asc = Order.ASC.equals(sortInfos[0].getOrder());
        MetaPropertyPath propertyPath = sortInfos[0].getPropertyPath();
        if (MetadataHelper.isPersistent(propertyPath)) {
            QueryTransformer transformer = QueryTransformerFactory.createTransformer(q.getQueryString(), metaClass.getName());
            transformer.replaceOrderBy(propertyPath.toString(), !asc);
            String jpqlQuery = transformer.getResult();
            q.setQueryString(jpqlQuery);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<Object, String> aggregate(AggregationInfo[] aggregationInfos, Collection itemIds) {
        return aggregatableDelegate.aggregate(aggregationInfos, itemIds);
    }

    protected Object getItemValue(MetaPropertyPath property, K itemId) {
        Instance instance = getItem(itemId);
        if (property.getMetaProperties().length == 1) {
            return instance.getValue(property.getMetaProperty().getName());
        } else {
            return instance.getValueEx(property.toString());
        }
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        boolean wasSuspended = this.suspended;
        this.suspended = suspended;

        if (wasSuspended && !suspended && refreshOnResumeRequired) {
            refresh();
        }
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int startPosition) {
        this.firstResult = startPosition;
    }
}
