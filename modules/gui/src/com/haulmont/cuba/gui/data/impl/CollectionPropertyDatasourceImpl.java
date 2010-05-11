/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 16:06:25
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.*;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.MetadataHelper;
import com.haulmont.cuba.gui.components.AggregationInfo;
import com.haulmont.cuba.gui.filter.QueryFilter;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class CollectionPropertyDatasourceImpl<T extends Entity<K>, K>
    extends
        PropertyDatasourceImpl<T>
    implements
        CollectionDatasource<T, K>,
        CollectionDatasource.Sortable<T, K>,
        CollectionDatasource.Aggregatable<T, K>
{
    private T item;
    protected boolean cascadeProperty;

    private Log log = LogFactory.getLog(CollectionPropertyDatasourceImpl.class);

    protected SortInfo<MetaPropertyPath>[] sortInfos;

    private AggregatableDelegate<K> aggregatableDelegate = new AggregatableDelegate<K>() {
        @Override
        public Object getItem(K itemId) {
            return CollectionPropertyDatasourceImpl.this.getItem(itemId);
        }

        public Object getItemValue(MetaPropertyPath property, K itemId) {
            return CollectionPropertyDatasourceImpl.this.getItemValue(property, itemId);
        }
    };

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
                log.trace("itemChanged: prevItem=" + prevItem + ", item=" + item);

                Collection prevColl = prevItem == null ? null : (Collection) ((Instance) prevItem).getValue(metaProperty.getName());
                Collection coll = item == null ? null : (Collection) ((Instance) item).getValue(metaProperty.getName());
                reattachListeners(prevColl, coll);

                forceCollectionChanged(CollectionDatasourceListener.Operation.REFRESH);
            }

            public void stateChanged(Datasource<Entity> ds, State prevState, State state) {
                for (DatasourceListener dsListener : new ArrayList<DatasourceListener>(dsListeners)) {
                    dsListener.stateChanged(CollectionPropertyDatasourceImpl.this, prevState, state);
                }
                forceCollectionChanged(CollectionDatasourceListener.Operation.REFRESH);
            }

            public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                if (property.equals(metaProperty.getName()) && !ObjectUtils.equals(prevValue, value)) {
                    log.trace("valueChanged: prop=" + property + ", prevValue=" + prevValue + ", value=" + value);

                    reattachListeners((Collection) prevValue, (Collection) value);

                    forceCollectionChanged(CollectionDatasourceListener.Operation.REFRESH);
                }
            }

            private void reattachListeners(Collection prevColl, Collection coll) {
                if (prevColl != null)
                    for (Object entity : prevColl) {
                        if (entity instanceof Instance)
                            detachListener((Instance) entity);
                    }

                if (coll != null)
                    for (Object entity : coll) {
                        if (entity instanceof Instance)
                            attachListener((Instance) entity);
                    }
            }
        });
    }

    public T getItem(K key) {
        if (key instanceof Entity)
            return (T) key;
        else {
            Collection<T> collection = __getCollection();
            if (collection != null) {
                for (T t : collection) {
                    if (t.getId().equals(key))
                        return t;
                }
            }
            return null;
        }
    }

    public K getItemId(T item) {
        if (item instanceof Entity)
            return item.getId();
        else
            return (K) item;
    }

    public Collection<K> getItemIds() {
        if (State.NOT_INITIALIZED.equals(ds.getState())) {
            return Collections.emptyList();
        } else {
            Collection<T> items = __getCollection();
            if (items == null)
                return Collections.emptyList();
            else {
                List<K> ids = new ArrayList(items.size());
                for (T item : items) {
                    ids.add(item.getId());
                }
                return ids;
            }
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

                if (item instanceof Instance) {
                    final MetaClass aClass = ((Instance) item).getMetaClass();
                    if (!aClass.equals(getMetaClass())) {
                        throw new IllegalStateException(String.format("Invalid item metaClass"));
                    }
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
        if (State.NOT_INITIALIZED.equals(ds.getState())) {
            return 0;
        } else {
            final Collection<T> collection = __getCollection();
            return collection == null ? 0 : collection.size();
        }
    }
                                                                                         
    protected Collection<T> __getCollection() {
        final Instance master = (Instance) ds.getItem();
        return master == null ? null : (Collection<T>) master.getValue(metaProperty.getName());
    }

    private void checkState() {
        if (!State.VALID.equals(getState()))
            throw new IllegalStateException("Invalid datasource state: " + getState());
    }

    public void addItem(T item) throws UnsupportedOperationException {
        checkState();

        if (__getCollection() == null) {
            initCollection();
        }

        __getCollection().add(item);
        attachListener((Instance) item);

        if (ObjectUtils.equals(this.item, item)) {
            this.item = item;
        }

        modified = true;
        if (cascadeProperty) {
            final Entity parentItem = ds.getItem();
            ((DatasourceImplementation) ds).modified(parentItem);
        }
        modified(item);

        forceCollectionChanged(CollectionDatasourceListener.Operation.ADD);
    }

    private void initCollection() {
        Instance item = (Instance) ds.getItem();
        if (item == null)
            throw new IllegalStateException("Item is null");

        Class<?> type = metaProperty.getJavaType();
        if (List.class.isAssignableFrom(type)) {
            item.setValue(metaProperty.getName(), new ArrayList());
        } else if (Set.class.isAssignableFrom(type)) {
            item.setValue(metaProperty.getName(), new HashSet());
        } else {
            throw new UnsupportedOperationException("Type " + type + " not supported, should implement List or Set");
        }

        if (item.getValue(metaProperty.getName()) == null) {
            throw new RuntimeException("Cannot set collection property " + metaProperty.getName() + ". Probably not contained in view.");
        }
    }

    public void removeItem(T item) throws UnsupportedOperationException {
        checkState();
        __getCollection().remove(item);
        detachListener((Instance) item);

        modified = true;
        if (cascadeProperty) {
            final Entity parentItem = ds.getItem();
            ((DatasourceImplementation) ds).modified(parentItem);
        } else {
            deleted(item);
        }

        forceCollectionChanged(CollectionDatasourceListener.Operation.REMOVE);
    }

    public void modifyItem(T item) {
        for (T t : __getCollection()) {
            if (t.equals(item)) {
                InstanceUtils.copy((Instance) item, (Instance) t);

                modified = true;
                if (cascadeProperty) {
                    final Entity parentItem = ds.getItem();
                    ((DatasourceImplementation) ds).modified(parentItem);
                } else {
                    modified(t);
                }
            }
        }
        forceCollectionChanged(CollectionDatasourceListener.Operation.REFRESH);
    }

    public void updateItem(T item) {
        for (T t : __getCollection()) {
            if (t.equals(item)) {
                InstanceUtils.copy((Instance) item, (Instance) t);
            }
        }
        forceCollectionChanged(CollectionDatasourceListener.Operation.REFRESH);
    }

    public boolean containsItem(K itemId) {
        Collection<T> coll = __getCollection();
        if (coll == null)
            return false;
        
        if (itemId instanceof Entity)
            return __getCollection().contains(itemId);
        else {
            Collection<T> collection = __getCollection();
            for (T item : collection) {
                if (item.getId().equals(itemId))
                    return true;
            }
            return false;
        }
    }

    public String getQuery() {
        return null;
    }

    public QueryFilter getQueryFilter() {
        return null;
    }

    public void setQuery(String query) {
        throw new UnsupportedOperationException();
    }

    public void setQuery(String query, QueryFilter filter) {
        throw new UnsupportedOperationException();
    }

    public void setQueryFilter(QueryFilter filter) {
        throw new UnsupportedOperationException();
    }

    public int getMaxResults() {
        return 0;
    }

    public void setMaxResults(int maxResults) {
    }

    public void refresh(Map<String, Object> parameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void commited(Map<Entity, Entity> map) {
        Collection<T> collection = __getCollection();
        if (collection != null) {
            for (T item : collection) {
                Entity committedItem = map.get(item);
                if (committedItem != null) {
                    if (collection instanceof List) {
                        List list = (List) collection;
                        list.set(list.indexOf(item), committedItem);
                    } else if (collection instanceof Set) {
                        Set set = (Set) collection;
                        set.remove(item);
                        set.add(committedItem);
                    }
                    attachListener((Instance) committedItem);
                }
            }
        }

        if (map.containsKey(this.item)) {
            this.item = (T) map.get(this.item);
        }

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

    public boolean isSoftDeletion() {
        return false;
    }

    public void setSoftDeletion(boolean softDeletion) {
    }


    //Implementation of CollectionDatasource.Sortable<T, K> interface
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
        if (__getCollection() == null) return;
        final MetaPropertyPath propertyPath = sortInfos[0].getPropertyPath();
        final boolean asc = Order.ASC.equals(sortInfos[0].getOrder());

        @SuppressWarnings({"unchecked"})
        List<T> list = new LinkedList<T>(__getCollection());
        Collections.sort(list, new EntityComparator<T>(propertyPath, asc));
        __getCollection().clear();
        __getCollection().addAll(list);
    }

    public K firstItemId() {
        Collection<T> collection = __getCollection();
        if ((collection != null) && !collection.isEmpty()) {
            return new LinkedList<T>(collection).getFirst().getId();
        }
        return null;
    }

    public K lastItemId() {
        Collection<T> collection = __getCollection();
        if ((collection != null) && !collection.isEmpty()) {
            return new LinkedList<T>(collection).getLast().getId();
        }
        return null;
    }

    public K nextItemId(K itemId) {
        if (itemId == null) return null;
        Collection<T> collection = __getCollection();
        if ((collection != null) && !collection.isEmpty() && !itemId.equals(lastItemId())) {
            List<T> list = new ArrayList<T>(collection);
            T currentItem = getItem(itemId);
            return list.get(list.indexOf(currentItem) + 1).getId();
        }
        return null;
    }

    public K prevItemId(K itemId) {
        if (itemId == null) return null;
        Collection<T> collection = __getCollection();
        if ((collection != null) && !collection.isEmpty() && !itemId.equals(firstItemId())) {
            List<T> list = new ArrayList<T>(collection);
            T currentItem = getItem(itemId);
            return list.get(list.indexOf(currentItem) - 1).getId();
        }
        return null;
    }

    public boolean isFirstId(K itemId) {
        return itemId != null && itemId.equals(firstItemId());
    }

    public boolean isLastId(K itemId) {
        return itemId != null && itemId.equals(lastItemId());
    }

    public Map<Object, String> aggregate(AggregationInfo[] aggregationInfos, Collection<K> itemIds) {
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
