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

import com.haulmont.bali.collections.ReadOnlyLinkedMapValuesView;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.filter.Condition;
import com.haulmont.cuba.core.global.filter.DenyingClause;
import com.haulmont.cuba.core.global.filter.LogicalCondition;
import com.haulmont.cuba.core.global.filter.LogicalOp;
import com.haulmont.cuba.gui.components.AggregationInfo;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import com.haulmont.cuba.security.entity.EntityOp;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.util.*;

/**
 * Most commonly used {@link CollectionDatasource} implementation.
 * Contains collection of standalone (not property) entities, and can request data from database and commit changes.
 * <p/>
 * Can be used as a base class for custom datasources that override e.g. {@link #loadData(java.util.Map)} method.
 *
 * @param <T> type of entity
 * @param <K> type of entity ID
 */
public class CollectionDatasourceImpl<T extends Entity<K>, K>
        extends
            AbstractCollectionDatasource<T, K>
        implements
            CollectionDatasource.Sortable<T, K>,
            CollectionDatasource.Aggregatable<T, K>,
            CollectionDatasource.Suspendable<T, K>,
            CollectionDatasource.SupportsPaging<T, K>,
            CollectionDatasource.SupportsApplyToSelected<T, K> {

    protected LinkedMap data = new LinkedMap();

    private boolean inRefresh;

    private AggregatableDelegate<K> aggregatableDelegate = new AggregatableDelegate<K>() {
        @Override
        public Object getItem(K itemId) {
            return CollectionDatasourceImpl.this.getItem(itemId);
        }

        @Override
        public Object getItemValue(MetaPropertyPath property, K itemId) {
            return CollectionDatasourceImpl.this.getItemValue(property, itemId);
        }
    };

    protected boolean suspended;

    protected boolean refreshOnResumeRequired;

    protected int firstResult;

    protected boolean sortOnDb = AppBeans.<Configuration>get(Configuration.NAME)
            .getConfig(ClientConfig.class).getCollectionDatasourceDbSortEnabled();

    protected LoadContext.Query lastQuery;
    protected LinkedList<LoadContext.Query> prevQueries = new LinkedList<>();
    protected Integer queryKey;

    @Override
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
    public void refresh() {
        if (savedParameters == null)
            refresh(Collections.<String, Object>emptyMap());
        else
            refresh(savedParameters);
    }

    @Override
    public void refresh(Map<String, Object> parameters) {
        backgroundWorker.checkUIAccess();

        if (inRefresh)
            return;

        if (refreshMode == RefreshMode.NEVER) {
            savedParameters = parameters;

            invalidate();

            State prevState = state;
            if (!prevState.equals(State.VALID)) {
                valid();
                fireStateChanged(prevState);
            }
            inRefresh = true;

            setItem(getItem());

            if (sortInfos != null && sortInfos.length > 0)
                doSort();

            suspended = false;
            refreshOnResumeRequired = false;

            fireCollectionChanged(Operation.REFRESH, Collections.<T>emptyList());

            inRefresh = false;
            return;
        }

        inRefresh = true;
        try {
            Collection prevIds = beforeRefresh(parameters);

            loadData(parameters);

            afterRefresh(parameters, prevIds);
        } finally {
            inRefresh = false;
        }
    }

    protected Collection beforeRefresh(Map<String, Object> parameters) {
        savedParameters = parameters;
        Collection prevIds = data.keySet();
        invalidate();
        return prevIds;
    }

    @SuppressWarnings("unused")
    protected void afterRefresh(Map<String, Object> parameters, Collection prevIds) {
        State prevState = state;
        if (!prevState.equals(State.VALID)) {
            state = State.VALID;
            fireStateChanged(prevState);
        }

        if (this.item != null && !prevIds.contains(this.item.getId())) {
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

        fireCollectionChanged(Operation.REFRESH, Collections.<T>emptyList());

        checkDataLoadError();
    }

    @Override
    public T getItem(K id) {
        backgroundWorker.checkUIAccess();

        if (state == State.NOT_INITIALIZED) {
            throw new IllegalStateException("Invalid datasource state " + state);
        } else {
            T item = (T) data.get(id);
            return item;
        }
    }

    @Override
    public Collection<K> getItemIds() {
        backgroundWorker.checkUIAccess();

        if (state == State.NOT_INITIALIZED) {
            return Collections.emptyList();
        } else {
            return (Collection<K>) data.keySet();
        }
    }

    @Override
    public Collection<T> getItems() {
        backgroundWorker.checkUIAccess();

        if (state == State.NOT_INITIALIZED) {
            return Collections.emptyList();
        } else {
            //noinspection unchecked
            return new ReadOnlyLinkedMapValuesView(data);
        }
    }

    @Override
    public int size() {
        backgroundWorker.checkUIAccess();

        if ((state == State.NOT_INITIALIZED) || suspended) {
            return 0;
        } else {
            return data.size();
        }
    }

    @Override
    public void sort(SortInfo[] sortInfos) {
        if (sortInfos.length != 1)
            throw new UnsupportedOperationException("Supporting sort by one field only");

        if (!Arrays.equals(this.sortInfos, sortInfos)) {
            //noinspection unchecked
            this.sortInfos = sortInfos;
            if (data.size() > 0) {
                if (!sortOnDb || containsAllDataFromDb()) {
                    doSort();

                    fireCollectionChanged(Operation.REFRESH, Collections.<T>emptyList());
                } else {
                    refresh();
                }
            }
        }
    }

    @Override
    public void resetSortOrder() {
        this.sortInfos = null;
    }

    protected boolean containsAllDataFromDb() {
        return firstResult == 0 && data.size() < maxResults;
    }

    protected void doSort() {
        List<T> list = new ArrayList<>(data.values());
        Collections.sort(list, createEntityComparator());
        data.clear();
        for (T t : list) {
            data.put(t.getId(), t);
        }
    }

    @Override
    public K firstItemId() {
        if (!data.isEmpty()) {
            return (K) data.firstKey();
        }
        return null;
    }

    @Override
    public K lastItemId() {
        if (!data.isEmpty()) {
            return (K) data.lastKey();
        }
        return null;
    }

    @Override
    public K nextItemId(K itemId) {
        return (K) data.nextKey(itemId);
    }

    @Override
    public K prevItemId(K itemId) {
        return (K) data.previousKey(itemId);
    }

    @Override
    public boolean isFirstId(K itemId) {
        return itemId != null && itemId.equals(firstItemId());
    }

    @Override
    public boolean isLastId(K itemId) {
        return itemId != null && itemId.equals(lastItemId());
    }

    protected void checkState() {
        if (state != State.VALID ) {
            refresh();
        }
    }

    protected void checkStateBeforeAdd() {
        if (state != State.VALID || isSuspended()) {
            this.suspended = false;
            refresh();
        }
    }

    @Override
    public void addItem(T item) {
        backgroundWorker.checkUIAccess();

        checkStateBeforeAdd();

        data.put(item.getId(), item);
        attachListener(item);

        if (PersistenceHelper.isNew(item)) {
            itemsToCreate.add(item);
        }

        modified = true;
        fireCollectionChanged(Operation.ADD, Collections.singletonList(item));
    }

    @Override
    public void removeItem(T item) {
        backgroundWorker.checkUIAccess();

        checkState();

        if (this.item != null && this.item.equals(item)) {
            setItem(null);
        }

        data.remove(item.getId());
        detachListener(item);

        deleted(item);

        fireCollectionChanged(Operation.REMOVE, Collections.singletonList(item));
    }

    @Override
    public void includeItem(T item) {
        backgroundWorker.checkUIAccess();

        checkStateBeforeAdd();

        data.put(item.getId(), item);
        attachListener(item);

        fireCollectionChanged(Operation.ADD, Collections.singletonList(item));
    }

    @Override
    public void excludeItem(T item) {
        backgroundWorker.checkUIAccess();

        checkState();

        if (this.item != null && this.item.equals(item)) {
            setItem(null);
        }

        data.remove(item.getId());
        detachListener(item);

        fireCollectionChanged(Operation.REMOVE, Collections.singletonList(item));
    }

    @Override
    public void clear() {
        backgroundWorker.checkUIAccess();

        // replaced refresh call with state initialization
        if (state != State.VALID) {
            invalidate();

            State prevState = state;
            if (prevState != State.VALID) {
                valid();
                fireStateChanged(prevState);
            }
        }

        // Get items
        List<Object> collectionItems = new ArrayList<>(data.values());
        // Clear container
        data.clear();
        // Notify listeners
        for (Object obj : collectionItems) {
            T item = (T) obj;
            detachListener(item);
        }

        setItem(null);

        fireCollectionChanged(Operation.CLEAR, Collections.emptyList());
    }

    @Override
    public void revert() {
        if (refreshMode != RefreshMode.NEVER) {
            refresh();
        } else {
            clear();
        }
    }

    @Override
    public void modifyItem(T item) {
        if (data.containsKey(item.getId())) {
            if (PersistenceHelper.isNew(item)) {
                Object existingItem = data.get(item.getId());
                metadata.getTools().copy(item, (Instance) existingItem);
                modified((T) existingItem);
            } else {
                updateItem(item);
                modified(item);
            }
        }
    }

    @Override
    public void updateItem(T item) {
        backgroundWorker.checkUIAccess();

        checkState();

        if (this.item != null && this.item.equals(item)) {
            this.item = item;
        }

        if (data.containsKey(item.getId())) {
            data.put(item.getId(), item);
            attachListener(item);
            fireCollectionChanged(Operation.UPDATE, Collections.singletonList(item));
        }
    }

    @Override
    public boolean containsItem(K itemId) {
        return data.containsKey(itemId);
    }

    @Override
    public void committed(Set<Entity> entities) {
        if (!State.VALID.equals(state)) {
            return;
        }

        for (Entity newEntity : entities) {
            if (newEntity.equals(item)) {
                item = (T) newEntity;
            }

            updateItem((T) newEntity);
        }

        modified = false;
        clearCommitLists();
    }

    protected boolean needLoading() {
        if (filter != null) {
            if (filter.getRoot() instanceof DenyingClause) {
                return false;
            }

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
        context.setSoftDeletion(softDeletion);

        prepareLoadContext(context);

        return context;
    }

    /**
     * Load data from middleware into {@link #data} field.
     * <p>In case of error sets {@link #dataLoadError} field to the exception object.</p>
     * @param params    datasource parameters, as described in {@link CollectionDatasource#refresh(java.util.Map)}
     */
    protected void loadData(Map<String, Object> params) {
        Security security = AppBeans.get(Security.NAME);
        if (!security.isEntityOpPermitted(metaClass, EntityOp.READ)) {
            return;
        }

        String tag = getLoggingTag("CDS");
        StopWatch sw = new Log4JStopWatch(tag, Logger.getLogger(UIPerformanceLogger.class));

        if (needLoading()) {
            LoadContext context = beforeLoadData(params);
            if (context == null) {
                return;
            }
            try {
                final Collection<T> entities = dataSupplier.loadList(context);

                afterLoadData(params, context, entities);
            } catch (Throwable e) {
                dataLoadError = e;
            }
        }

        sw.stop();
    }

    /**
     * This method is invoked by {@link #loadData(Map)} method immediately before loading entities from {@code DataSupplier}.
     * <p>If you override this method, be sure to call {@code super()}.
     *
     * @param params    datasource parameters, as described in {@link CollectionDatasource#refresh(java.util.Map)}
     * @return          LoadContext which will be used to load data
     */
    protected LoadContext beforeLoadData(Map<String, Object> params) {
        final LoadContext context = new LoadContext(metaClass);

        LoadContext.Query q = createLoadContextQuery(context, params);
        if (q == null) {
            detachListener(data.values());
            data.clear();
            return null;
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

        prepareLoadContext(context);

        dataLoadError = null;
        return context;
    }

    /**
     * This method is invoked by {@link #loadData(Map)} method immediately after loading entities from {@code DataSupplier}.
     * <p>If you override this method, be sure to call {@code super()}. If you process the loaded entities somehow,
     * call {@code super()} after processing.
     *
     * @param params        datasource parameters, as described in {@link CollectionDatasource#refresh(java.util.Map)}
     * @param context       {@code LoadContext} which was used for loading data
     * @param entities      loaded entities
     */
    protected void afterLoadData(@SuppressWarnings("unused") Map<String, Object> params, LoadContext context, Collection<T> entities) {
        detachListener(data.values());
        data.clear();

        for (T entity : entities) {
            data.put(entity.getId(), entity);
            attachListener(entity);
        }

        lastQuery = context.getQuery();
    }

    @Override
    protected void prepareLoadContext(LoadContext context) {
        context.setLoadDynamicAttributes(loadDynamicAttributes);
        context.setQueryKey(queryKey == null ? 0 : queryKey);
        context.getPrevQueries().addAll(prevQueries);
    }

    protected void detachListener(Collection instances) {
        for (Object obj : instances) {
            if (obj instanceof Instance)
                detachListener((Instance) obj);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<AggregationInfo, String> aggregate(AggregationInfo[] aggregationInfos, Collection itemIds) {
        return aggregatableDelegate.aggregate(aggregationInfos, itemIds);
    }

    protected Object getItemValue(MetaPropertyPath property, K itemId) {
        Instance instance = getItemNN(itemId);
        if (property.getMetaProperties().length == 1) {
            return instance.getValue(property.getMetaProperty().getName());
        } else {
            return instance.getValueEx(property.toString());
        }
    }

    @Override
    public boolean isSuspended() {
        return suspended;
    }

    @Override
    public void setSuspended(boolean suspended) {
        boolean wasSuspended = this.suspended;
        this.suspended = suspended;

        if (wasSuspended && !suspended && refreshOnResumeRequired) {
            refresh();
        }
    }

    @Override
    public int getFirstResult() {
        return firstResult;
    }

    @Override
    public void setFirstResult(int startPosition) {
        this.firstResult = startPosition;
    }

    protected void incrementQueryKey() {
        queryKey = userSession.getAttribute("_queryKey");
        if (queryKey == null)
            queryKey = 1;
        else
            queryKey++;
        userSession.setAttribute("_queryKey", queryKey);
    }

    @Override
    public void pinQuery() {
        if (prevQueries.isEmpty())
            incrementQueryKey();

        if (lastQuery != null)
            prevQueries.add(lastQuery);
    }

    @Override
    public void unpinLastQuery() {
        if (!prevQueries.isEmpty()) {
            prevQueries.removeLast();
        }
    }

    @Override
    public void unpinAllQuery() {
        if (!prevQueries.isEmpty()) {
            prevQueries.clear();
        }
    }
}