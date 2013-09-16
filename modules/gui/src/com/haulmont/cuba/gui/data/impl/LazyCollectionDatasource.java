/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.haulmont.cuba.gui.data.LazyCollectionDatasourceListener;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import javax.annotation.Nullable;
import java.util.*;

/**
 * DEPRECATED. To restrict data loading use {@link com.haulmont.cuba.gui.components.SearchPickerField} and
 * {@link com.haulmont.cuba.gui.components.RowsCount} components with usual {@link CollectionDatasourceImpl}.
 *
 * @author abramov
 * @version $Id$
 */
@Deprecated
public class LazyCollectionDatasource<T extends Entity<K>, K>
    extends
        AbstractCollectionDatasource<T, K>
    implements
        CollectionDatasource.Sortable<T, K>,
        CollectionDatasource.Lazy<T, K>,
        CollectionDatasource.Suspendable<T, K> {

    protected LinkedMap data = new LinkedMap();
    protected Integer size;

    protected Map<String, Object> params = Collections.emptyMap();

    protected int chunk = 50;

    private boolean inRefresh;

    protected boolean suspended;

    protected boolean refreshOnResumeRequired;

    protected boolean disableLoad;

    @Override
    public void addItem(T item) throws UnsupportedOperationException {
        checkState();

        attachListener(item);

        data.put(item.getId(), item);
        
        if (size != null)
            size++;

        if (sortInfos != null)
            sortInMemory();

        if (PersistenceHelper.isNew(item)) {
            itemToCreate.add(item);
        }

        modified = true;
        fireCollectionChanged(CollectionDatasourceListener.Operation.ADD, Collections.<Entity>singletonList(item));
    }

    @Override
    public void removeItem(T item) throws UnsupportedOperationException {
        checkState();

        data.remove(item.getId());
        detachListener(item);

        if (size != null && size > 0)
            size--;

        deleted(item);

        fireCollectionChanged(CollectionDatasourceListener.Operation.REMOVE, Collections.<Entity>singletonList(item));
    }

    @Override
    public void excludeItem(T item) throws UnsupportedOperationException {
        checkState();

        data.remove(item.getId());
        detachListener(item);

        if (size != null && size > 0)
            size--;

        fireCollectionChanged(CollectionDatasourceListener.Operation.REMOVE, Collections.<Entity>singletonList(item));
    }

    @Override
    public void includeItem(T item) throws UnsupportedOperationException {
        checkState();

        data.put(item.getId(), item);
        attachListener(item);

        if (size != null && size > 0)
            size++;

        fireCollectionChanged(CollectionDatasourceListener.Operation.ADD, Collections.<Entity>singletonList(item));
    }

    @Override
    public void clear() throws UnsupportedOperationException {
        checkState();
        // Get items
        Collection<Object> collectionItems = new LinkedList<Object>(data.values());
        // Clear
        data.clear();
        // Notify listeners
        for (Object obj : collectionItems) {
            T item = (T) obj;
            detachListener(item);

            if (size != null && size > 0)
                size--;

            fireCollectionChanged(CollectionDatasourceListener.Operation.CLEAR, Collections.<Entity>emptyList());
        }
    }

    @Override
    public void revert() throws UnsupportedOperationException {
        refresh();
    }

    @Override
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

    @Override
    public void updateItem(T item) {
        checkState();

        if (data.containsKey(item.getId())) {
            data.put(item.getId(), item);
            attachListener(item);
            fireCollectionChanged(CollectionDatasourceListener.Operation.REFRESH, Collections.<Entity>singletonList(item));
        }
    }

    @Override
    public boolean containsItem(K itemId) {
        return data.containsKey(itemId);
    }

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
        this.params = parameters;
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

            fireCollectionChanged(CollectionDatasourceListener.Operation.REFRESH, Collections.<Entity>emptyList());

            inRefresh = false;
            return;
        }

        inRefresh = true;
        try {
            savedParameters = parameters;

            size = null;
            for (Object entity : data.values()) {
                detachListener((Instance) entity);
            }
            data.clear();

            invalidate();

            suspended = false;
            refreshOnResumeRequired = false;

            getSize();
            checkDataLoadError();

            if (!State.VALID.equals(state))
                loadNextChunk(false);

            if (sortInfos != null && sortInfos.length > 0 && isCompletelyLoaded())
                sortInMemory();

            fireCollectionChanged(CollectionDatasourceListener.Operation.REFRESH, Collections.<Entity>emptyList());

            checkDataLoadError();
        } finally {
            inRefresh = false;
        }
    }

    private int getSize() {
        if (suspended)
            return 0;

        if (size == null) {
            int realSize = getCount();
            if (maxResults == 0)
                size = realSize;
            else
                size = Math.min(realSize, maxResults);
        }

        return size;
    }

    @Override
    public T getItem(K id) {
        if (State.NOT_INITIALIZED.equals(state)) {
            throw new IllegalStateException("Invalid datasource state " + state);
        } else {
            T item = (T) data.get(id);
            return item;
        }
    }

    @Override
    public Collection<K> getItemIds() {
        if (State.NOT_INITIALIZED.equals(state)) {
            return Collections.emptyList();
        } else {
            if (!isCompletelyLoaded()) loadNextChunk(true);
            //noinspection unchecked
            return data.keySet();
        }
    }

    @Override
    public Collection<T> getItems() {
        if (state == State.NOT_INITIALIZED) {
            return Collections.emptyList();
        } else {
            return Collections2.transform(getItemIds(), new Function<K, T>() {
                @Nullable
                @Override
                public T apply(@Nullable K id) {
                    return id == null ? null : getItem(id);
                }
            });
        }
    }

    @Override
    public int size() {
        if (State.NOT_INITIALIZED.equals(state)) {
            return 0;
        } else {
            return size == null ? 0 : size;
        }
    }

    @Override
    public K nextItemId(K itemId) {
        @SuppressWarnings({"unchecked"})
        K nextId = (K) data.nextKey(itemId);
        if (nextId == null && !isCompletelyLoaded()) {
            loadNextChunk(false);
            //noinspection unchecked
            nextId = (K) data.nextKey(itemId);
        }
        return nextId;
    }

    @Override
    public K prevItemId(K itemId) {
        //noinspection unchecked
        return (K) data.previousKey(itemId);
    }

    @Override
    public K firstItemId() {
        if (suspended)
            return null;

        if (data.isEmpty())
            loadNextChunk(false);

        if (!data.isEmpty()) {
            //noinspection unchecked
            return (K) data.firstKey();
        } else {
            return null;
        }
    }

    @Override
    public K lastItemId() {
        if (!isCompletelyLoaded())
            loadNextChunk(true);

        if (!data.isEmpty()) {
            //noinspection unchecked
            return (K) data.lastKey();
        } else {
            return null;
        }
    }

    @Override
    public boolean isFirstId(K itemId) {
        return itemId != null && itemId.equals(firstItemId());
    }

    @Override
    public boolean isLastId(K itemId) {
        //noinspection SimplifiableConditionalExpression
        return itemId != null && (isCompletelyLoaded() ? itemId.equals(lastItemId()) : false);
    }

    @Override
    public boolean isCompletelyLoaded() {
        return data.size() == getSize();
    }

    protected void loadNextChunk(boolean all) {
        if (disableLoad)
            return;

        StopWatch sw = new Log4JStopWatch(getLoggingTag("LCDS"), Logger.getLogger(UIPerformanceLogger.class));

        getSize(); // ensure size is loaded

        LoadContext ctx = new LoadContext(metaClass);
        LoadContext.Query q = createLoadContextQuery(ctx, params);
        if (q != null) {
            if (sortInfos != null) {
                setSortDirection(q);
            }

            if (maxResults == 0 || data.size() < maxResults) {
                ctx.getQuery().setFirstResult(data.size());
                ctx.setView(view);

                if (all) {
                    if (maxResults > 0)
                        ctx.getQuery().setMaxResults(maxResults);
                } else
                    ctx.getQuery().setMaxResults(chunk);

                dataLoadError = null;
                List<T> res = null;
                try {
                    res = dataSupplier.loadList(ctx);
                    for (T t : res) {
                        data.put(t.getId(), t);
                        attachListener(t);
                    }
                } catch (Throwable e) {
                    dataLoadError = e;
                }

                if (res.size() < chunk || (maxResults > 0 && data.size() >= maxResults)) {
                    size = data.size(); // all is loaded
                    for (DatasourceListener listener : dsListeners) {
                        if (listener instanceof LazyCollectionDatasourceListener) {
                            ((LazyCollectionDatasourceListener) listener).completelyLoaded(this);
                        }
                    }
                }
            }
        }

        State prevState = state;
        if (!prevState.equals(State.VALID)) {
            state = State.VALID;
            fireStateChanged(prevState);
        }

        sw.stop();
    }

    @Override
    public void sort(SortInfo[] sortInfos) {
        if (sortInfos.length != 1)
            throw new UnsupportedOperationException("Supporting sort by one field only");

        if (!Arrays.equals(this.sortInfos, sortInfos)) {
            this.sortInfos = sortInfos;
            doSort();

            fireCollectionChanged(CollectionDatasourceListener.Operation.REFRESH, Collections.<Entity>emptyList());
        }
    }

    @Override
    public void resetSortOrder() {
        this.sortInfos = null;
    }

    private void doSort() {
        if (isCompletelyLoaded()) {
            sortInMemory();
        } else {
            for (Object entity : data.values()) {
                detachListener((Instance) entity);
            }
            data.clear();
            loadNextChunk(false);
        }
    }

    private void sortInMemory() {
        List<T> order = new ArrayList<T>(data.values());
        Collections.sort(order, createEntityComparator());
        data.clear();
        for (T t : order) {
            data.put(t.getId(), t);
        }
    }

    private void checkState() {
        if (!State.VALID.equals(state)) {
            refresh();
        }
    }

    @Override
    public void committed(Set<Entity> entities) {
        for (Entity newEntity : entities) {
            if (newEntity.equals(item))
                item = (T) newEntity;

            updateItem((T) newEntity);
        }

        modified = false;
        clearCommitLists();
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
        refreshOnResumeRequired = false;
    }

    @Override
    protected void fireCollectionChanged(CollectionDatasourceListener.Operation operation, List<Entity> items) {
        disableLoad = true;
        try {
            super.fireCollectionChanged(operation, items);
        } finally {
            disableLoad = false;
        }
    }
}