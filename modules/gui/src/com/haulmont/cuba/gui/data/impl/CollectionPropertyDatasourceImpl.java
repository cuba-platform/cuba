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

import com.google.common.collect.Iterables;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.*;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.filter.QueryFilter;
import com.haulmont.cuba.gui.components.AggregationInfo;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.model.impl.EntityValuesComparator;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.PermissionType;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class CollectionPropertyDatasourceImpl<T extends Entity<K>, K>
        extends
            PropertyDatasourceImpl<T>
        implements
            CollectionDatasource<T, K>,
            CollectionDatasource.Indexed<T, K>,
            CollectionDatasource.Sortable<T, K>,
            CollectionDatasource.Aggregatable<T, K>,
            CollectionDatasource.SupportsSortDelegate<T, K> {

    private static final Logger log = LoggerFactory.getLogger(CollectionPropertyDatasourceImpl.class);

    protected T item;
    protected boolean cascadeProperty;

    protected SortInfo<MetaPropertyPath>[] sortInfos;
    protected boolean listenersSuspended;
    protected final LinkedList<CollectionChangeEvent<T,K>> suspendedEvents = new LinkedList<>();

    protected boolean doNotModify;

    protected List<CollectionChangeListener<? super T, K>> collectionChangeListeners;

    protected SortDelegate<T, K> sortDelegate = (entities, sortInfo) -> entities.sort(createEntityComparator());

    protected AggregatableDelegate<K> aggregatableDelegate = new AggregatableDelegate<K>() {
        @Override
        public Object getItem(K itemId) {
            return CollectionPropertyDatasourceImpl.this.getItem(itemId);
        }

        @Override
        public Object getItemValue(MetaPropertyPath property, K itemId) {
            return CollectionPropertyDatasourceImpl.this.getItemValue(property, itemId);
        }
    };

    @Override
    public void setup(String id, Datasource masterDs, String property) {
        super.setup(id, masterDs, property);
        cascadeProperty = metadata.getTools().isCascade(metaProperty);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initParentDsListeners() {
        masterDs.addItemChangeListener(e -> {
            log.trace("itemChanged: prevItem={}, item={}", e.getPrevItem(), e.getItem());

            Collection prevColl = e.getPrevItem() == null ? null : (Collection) e.getPrevItem().getValue(metaProperty.getName());
            Collection coll = e.getItem() == null ? null : (Collection) e.getItem().getValue(metaProperty.getName());
            reattachListeners(prevColl, coll);

            if (coll != null && metadata.getTools().isPersistent(metaProperty)) {
                for (Object collItem : coll) {
                    if (PersistenceHelper.isNew(collItem)) {
                        itemsToCreate.remove(collItem);
                        itemsToCreate.add((T) collItem);
                        modified = true;
                    }
                }
            }

            if (item != null && (coll == null || !coll.contains(item))) {
                T prevItem = item;
                item = null;
                fireItemChanged(prevItem);
            }
            fireCollectionChanged(Operation.REFRESH, Collections.emptyList());
        });

        masterDs.addStateChangeListener(e -> {
            fireStateChanged(e.getPrevState());

            fireCollectionChanged(Operation.REFRESH, Collections.emptyList());
        });

        masterDs.addItemPropertyChangeListener(e -> {
            if (e.getProperty().equals(metaProperty.getName()) && !Objects.equals(e.getPrevValue(), e.getValue())) {
                log.trace("master valueChanged: prop={}, prevValue={}, value={}", e.getProperty(), e.getPrevValue(), e.getValue());

                reattachListeners((Collection) e.getPrevValue(), (Collection) e.getValue());

                fireCollectionChanged(Operation.REFRESH, Collections.emptyList());
            }
        });
    }

    protected void reattachListeners(Collection prevColl, Collection coll) {
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

    @Override
    public T getItem(K id) {
        backgroundWorker.checkUIAccess();

        Collection<T> collection = getCollection();
        if (collection != null) {
            for (T t : collection) {
                if (t.getId().equals(id)) {
                    return t;
                }
            }
        }
        return null;
    }

    @Override
    public T getItemNN(K id) {
        backgroundWorker.checkUIAccess();

        T it = getItem(id);
        if (it != null) {
            return it;
        } else {
            throw new IllegalStateException("Item with id=" + id + " is not found in datasource " + this.id);
        }
    }

    @Override
    public Collection<K> getItemIds() {
        backgroundWorker.checkUIAccess();

        if (masterDs.getState() == State.NOT_INITIALIZED) {
            return Collections.emptyList();
        } else {
            Collection<T> items = getCollection();
            if (items == null)
                return Collections.emptyList();
            else {
                List<K> ids = new ArrayList<>(items.size());
                for (T item : items) {
                    ids.add(item.getId());
                }
                return ids;
            }
        }
    }

    @Override
    public Collection<T> getItems() {
        backgroundWorker.checkUIAccess();

        if (masterDs.getState() == State.NOT_INITIALIZED) {
            return Collections.emptyList();
        } else {
            Collection<T> items = getCollection();
            if (items == null) {
                return Collections.emptyList();
            }
            if (items.isEmpty()) {
                return Collections.emptyList();
            }
            return Collections.unmodifiableCollection(items);
        }
    }

    @Override
    public T getItem() {
        backgroundWorker.checkUIAccess();

        return getState() == State.VALID ? item : null;
    }

    @Override
    public void setItem(T item) {
        backgroundWorker.checkUIAccess();

        if (getState() == State.VALID) {
            Object prevItem = this.item;

            if (prevItem != item) {
                if (item != null) {
                    final MetaClass aClass = item.getMetaClass();
                    MetaClass metaClass = getMetaClass();
                    if (!aClass.equals(metaClass) && !metaClass.getDescendants().contains(aClass)) {
                        throw new DevelopmentException(String.format("Invalid item metaClass '%s'",  aClass),
                                ParamsMap.of("datasource", getId(), "metaClass", aClass));
                    }
                }
                this.item = item;

                //noinspection unchecked
                fireItemChanged((T) prevItem);
            }
        }
    }

    @Override
    public void refresh() {
        backgroundWorker.checkUIAccess();

        fireCollectionChanged(Operation.REFRESH, Collections.emptyList());
    }

    @Override
    public int size() {
        backgroundWorker.checkUIAccess();

        if (masterDs.getState() == State.NOT_INITIALIZED) {
            return 0;
        } else {
            final Collection<T> collection = getCollection();
            return collection == null ? 0 : collection.size();
        }
    }

    protected Collection<T> getCollection() {
        Security security = AppBeans.get(Security.NAME);

        MetaClass parentMetaClass = masterDs.getMetaClass();
        MetaClass propertyMetaClass = metaProperty.getRange().asClass();

        if (!security.isEntityOpPermitted(propertyMetaClass, EntityOp.READ)
                || !security.isEntityAttrPermitted(parentMetaClass, metaProperty.getName(), EntityAttrAccess.VIEW)) {
            return new ArrayList<>(); // Don't use Collections.emptyList() to avoid confusing UnsupportedOperationExceptions
        } else {
            final Instance master = masterDs.getItem();
            //noinspection unchecked
            return master == null ? null : (Collection<T>) master.getValue(metaProperty.getName());
        }
    }

    protected void checkState() {
        State state = getState();
        if (state != State.VALID) {
            throw new IllegalStateException("Invalid datasource state: " + state);
        }
    }

    protected void checkPermission() {
        Security security = AppBeans.get(Security.NAME);
        MetaClass parentMetaClass = masterDs.getMetaClass();

        if (!security.isEntityAttrPermitted(parentMetaClass, metaProperty.getName(), EntityAttrAccess.MODIFY)) {
            throw new AccessDeniedException(PermissionType.ENTITY_ATTR, parentMetaClass + "." + metaProperty.getName());
        }
    }

    @Override
    public void addItem(T item) {
        checkNotNullArgument(item, "item is null");
        internalAddItem(item, () -> {
            getCollection().add(item);
        });
    }

    @Override
    public void addItemFirst(T item) {
        checkNotNullArgument(item, "item is null");
        internalAddItem(item, () -> {
            addToCollectionFirst(item);
        });
    }

    @SuppressWarnings("unchecked")
    protected void internalAddItem(T item, Runnable addToCollection) {
        backgroundWorker.checkUIAccess();

        checkState();
        checkPermission();

        if (getCollection() == null) {
            if (masterDs.getItem() == null) {
                // Last chance to find and set a master item
                MetaProperty inverseProp = metaProperty.getInverse();
                if (inverseProp != null) {
                    Entity probableMasterItem = item.getValue(inverseProp.getName());
                    if (probableMasterItem != null) {
                        Collection<Entity> masterCollection = ((CollectionPropertyDatasourceImpl) masterDs).getCollection();
                        for (Entity masterCollectionItem : masterCollection) {
                            if (masterCollectionItem.equals(probableMasterItem)) {
                                masterDs.setItem(masterCollectionItem);
                                break;
                            }
                        }
                    }
                }
                if (masterDs.getItem() == null) {
                    throw new IllegalStateException("Master datasource item is null");
                }
            } else {
                initCollection();
            }
        }

        // Don't add the same object instance twice (this is possible when committing nested datasources)
        if (!containsObjectInstance(item))
            addToCollection.run();

        attachListener(item);

        if (Objects.equals(this.item, item)) {
            this.item = item;
        }

        modified = true;
        if (cascadeProperty) {
            final Entity parentItem = masterDs.getItem();
            ((DatasourceImplementation) masterDs).modified(parentItem);
        }
        if (metaProperty != null && metaProperty.getRange() != null && metaProperty.getRange().getCardinality() != null
                && metaProperty.getRange().getCardinality() == Range.Cardinality.MANY_TO_MANY
                && !PersistenceHelper.isNew(item)) {
            // do not mark for update existing many-to-many item;
            // item is not updated here, but many-to-many table entry is added
        } else {
            modified(item);
        }

        fireCollectionChanged(Operation.ADD, Collections.singletonList(item));
    }

    @SuppressWarnings("unchecked")
    protected void addToCollectionFirst(T item) {
        Collection<T> collection = getCollection();
        if (collection instanceof List) {
            ((List) collection).add(0, item);
        } else if (collection instanceof LinkedHashSet) {
            LinkedHashSet tmpSet = (LinkedHashSet) ((LinkedHashSet) collection).clone();
            collection.clear();
            ((LinkedHashSet) collection).add(item);
            ((LinkedHashSet) collection).addAll(tmpSet);
        } else {
            collection.add(item);
        }
    }

    /**
     * Search the collection using object identity.
     * @return true if the collection already contains the instance
     */
    protected boolean containsObjectInstance(T instance) {
        Collection<T> collection = getCollection();
        if (collection != null) {
            for (T item : collection) {
                if (instance == item)
                    return true;
            }
        }
        return false;
    }

    protected void initCollection() {
        Instance item = masterDs.getItem();
        if (item == null)
            throw new IllegalStateException("Item is null");

        Class<?> type = metaProperty.getJavaType();
        if (List.class.isAssignableFrom(type)) {
            item.setValue(metaProperty.getName(), new ArrayList());
        } else if (Set.class.isAssignableFrom(type)) {
            item.setValue(metaProperty.getName(), new LinkedHashSet());
        } else {
            throw new UnsupportedOperationException("Type " + type + " not supported, should implement List or Set");
        }

        if (item.getValue(metaProperty.getName()) == null) {
            throw new RuntimeException("Cannot set collection property " + metaProperty.getName() + ". Probably not contained in view.");
        }
    }

    @Override
    public void removeItem(T item) {
        checkNotNullArgument(item, "item is null");
        checkState();
        checkPermission();

        Collection<T> collection = getCollection();
        if (collection != null) {
            if (this.item != null && this.item.equals(item)) {
                setItem(null);
            }

            // In case of 2nd-level composition and using List as attribute type, there may be duplicated instances
            // after repeated editing of newly added item. So remove them all.
            Iterator<T> iterator = collection.iterator();
            while (iterator.hasNext()) {
                T entity = iterator.next();
                if (entity.equals(item)) {
                    detachListener(entity);
                    iterator.remove();
                }
            }

            modified = true;
            if (cascadeProperty) {
                final Entity parentItem = masterDs.getItem();
                //noinspection unchecked
                ((DatasourceImplementation) masterDs).modified(parentItem);
            } else {
                deleted(item);
            }

            fireCollectionChanged(Operation.REMOVE, Collections.singletonList(item));
        }
    }

    @Override
    public void excludeItem(T item) {
        checkNotNullArgument(item, "item is null");
        backgroundWorker.checkUIAccess();

        checkState();
        checkPermission();

        Collection<T> collection = getCollection();
        if (collection != null) {
            if (this.item != null && this.item.equals(item)) {
                setItem(null);
            }

            doNotModify = true;
            try {
                collection.remove(item);

                MetaProperty inverseProperty = metaProperty.getInverse();
                if (inverseProperty != null)
                    item.setValue(inverseProperty.getName(), null);

                // detach listener only after setting value to the link property
                detachListener(item);

                fireCollectionChanged(Operation.REMOVE, Collections.singletonList(item));
            } finally {
                doNotModify = false;
            }
        }
    }

    @Override
    public void includeItem(T item) {
        checkNotNullArgument(item, "item is null");
        internalIncludeItem(item, () -> {
            getCollection().add(item);
        });
    }

    @Override
    public void includeItemFirst(T item) {
        checkNotNullArgument(item, "item is null");
        internalIncludeItem(item, () -> {
            addToCollectionFirst(item);
        });
    }

    protected void internalIncludeItem(T item, Runnable addToCollection) {
        backgroundWorker.checkUIAccess();

        checkState();
        checkPermission();

        if (getCollection() == null) {
            initCollection();
        }

        doNotModify = true;
        try {
            // Don't add the same object instance twice
            if (!containsObjectInstance(item))
                addToCollection.run();

            MetaProperty inverseProperty = metaProperty.getInverse();
            if (inverseProperty != null)
                item.setValue(inverseProperty.getName(), masterDs.getItem());

            // attach listener only after setting value to the link property
            attachListener(item);

            fireCollectionChanged(Operation.ADD, Collections.singletonList(item));
        } finally {
            doNotModify = false;
        }
    }

    @Override
    public void clear() {
        backgroundWorker.checkUIAccess();

        checkState();
        Collection<T> collection = getCollection();
        if (collection != null) {
            Collection<T> collectionItems = new ArrayList<>(collection);
            doNotModify = true;
            try {
                // Clear collection
                collection.clear();
                // Notify listeners
                for (T item : collectionItems) {
                    if (metaProperty.getRange().getCardinality() == Range.Cardinality.ONE_TO_MANY) {
                        MetaProperty inverseProperty = metaProperty.getInverse();
                        if (inverseProperty == null) {
                            throw new UnsupportedOperationException("No inverse property for " + metaProperty);
                        }

                        item.setValue(inverseProperty.getName(), null);
                    }

                    // detach listener only after setting value to the link property
                    detachListener(item);
                }

                setItem(null);

                fireCollectionChanged(Operation.CLEAR, Collections.emptyList());
            } finally {
                doNotModify = false;
            }
        }
    }

    @Override
    public void revert() {
        refresh();
    }

    @Override
    public void modifyItem(T item) {
        checkNotNullArgument(item, "item is null");
        Collection<T> collection = getCollection();
        if (collection != null) {
            for (T t : collection) {
                if (t.equals(item)) {
                    EntityCopyUtils.copyCompositionsBack(item, t);

                    modified = true;
                    if (cascadeProperty) {
                        final Entity parentItem = masterDs.getItem();
                        //noinspection unchecked
                        ((DatasourceImplementation) masterDs).modified(parentItem);
                    } else {
                        modified(t);
                    }
                }
            }
            fireCollectionChanged(Operation.UPDATE, Collections.singletonList(item));
        }
    }

    @Override
    public void updateItem(T item) {
        checkNotNullArgument(item, "item is null");
        backgroundWorker.checkUIAccess();

        Collection<T> collection = getCollection();
        if (collection != null) {
            // this method must not change the "modified" state by contract
            boolean saveModified = modified;
            for (T t : collection) {
                if (t.equals(item)) {
                    metadata.getTools().copy(item, t);
                }
            }
            modified = saveModified;
            fireCollectionChanged(Operation.UPDATE, Collections.singletonList(item));
        }
    }

    @Override
    public void modified(T item) {
        checkNotNullArgument(item, "item is null");
        if (doNotModify)
            return;
        super.modified(item);
    }

    @SuppressWarnings("unchecked")
    public void replaceItem(T item) {
        checkNotNullArgument(item, "item is null");
        Collection<T> collection = getCollection();
        if (collection != null) {
            for (T t : collection) {
                if (t.equals(item)) {
                    detachListener(t);
                    if (collection instanceof List) {
                        List list = (List) collection;
                        int itemIdx = list.indexOf(t);
                        list.set(itemIdx, item);
                    } else if (collection instanceof LinkedHashSet) {
                        LinkedHashSet set = (LinkedHashSet) collection;

                        List list = new ArrayList(set);
                        int itemIdx = list.indexOf(t);
                        list.set(itemIdx, item);

                        set.clear();
                        set.addAll(list);
                    } else {
                        collection.remove(t);
                        collection.add(item);
                    }
                    attachListener(item);

                    if (item.equals(this.item)) {
                        this.item = item;
                    }
                    break;
                }
            }
            if (sortInfos != null)
                doSort();

            fireCollectionChanged(Operation.UPDATE, Collections.singletonList(item));
        }
    }

    @Override
    public boolean containsItem(K itemId) {
        Collection<T> collection = getCollection();
        if (collection == null) {
            return false;
        }

        for (T item : collection) {
            if (item.getId().equals(itemId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getQuery() {
        return null;
    }

    @Override
    public LoadContext getCompiledLoadContext() {
        return null;
    }

    @Override
    public QueryFilter getQueryFilter() {
        return null;
    }

    @Override
    public void setQuery(String query) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setQuery(String query, QueryFilter filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setQueryFilter(QueryFilter filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaxResults() {
        return 0;
    }

    @Override
    public void setMaxResults(int maxResults) {
    }

    @Override
    public void refresh(Map<String, Object> parameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getLastRefreshParameters() {
        return Collections.emptyMap();
    }

    @Override
    public boolean getRefreshOnComponentValueChange() {
        return false;
    }

    @Override
    public void setRefreshOnComponentValueChange(boolean refresh) {
    }

    @Override
    public void addCollectionChangeListener(CollectionChangeListener<? super T, K> listener) {
        Preconditions.checkNotNullArgument(listener, "listener cannot be null");

        if (collectionChangeListeners == null) {
            collectionChangeListeners = new ArrayList<>();
        }
        if (!collectionChangeListeners.contains(listener)) {
            collectionChangeListeners.add(listener);
        }
    }

    @Override
    public void removeCollectionChangeListener(CollectionChangeListener<? super T, K> listener) {
        if (collectionChangeListeners != null) {
            collectionChangeListeners.remove(listener);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void committed(Set<Entity> entities) {
        if (!State.VALID.equals(masterDs.getState()))
            return;

        Collection<T> collection = getCollection();
        if (collection != null) {
            for (T item : new ArrayList<>(collection)) {
                for (Entity entity : entities) {
                    if (entity.equals(item)) {
                        if (collection instanceof List) {
                            List list = (List) collection;
                            list.set(list.indexOf(item), entity);
                        } else if (collection instanceof Set) {
                            Set set = (Set) collection;
                            set.remove(item);
                            set.add(entity);
                        }
                        attachListener(entity);
                        fireCollectionChanged(Operation.UPDATE, Collections.singletonList(item));
                    }
                }
                detachListener(item); // to avoid duplication in any case
                attachListener(item);
            }
        }

        for (Entity entity : entities) {
            if (entity.equals(item)) {
                item = (T) entity;
            }
        }

        modified = false;
        clearCommitLists();
    }

    protected void fireCollectionChanged(Operation operation, List<T> items) {
        if (listenersSuspended) {
            if (!suspendedEvents.isEmpty() && suspendedEvents.getFirst().getOperation().equals(operation)) {
                suspendedEvents.getFirst().getItems().addAll(items);
            } else {
                suspendedEvents.addFirst(new CollectionChangeEvent<>(this, operation, new ArrayList<>(items)));
            }
            return;
        }
        if (collectionChangeListeners != null && !collectionChangeListeners.isEmpty()) {
            CollectionChangeEvent event = new CollectionChangeEvent<>(this, operation, items);

            for (CollectionChangeListener<? super T, K> listener : new ArrayList<>(collectionChangeListeners)) {
                //noinspection unchecked
                listener.collectionChanged(event);
            }
        }
    }

    @Override
    public void suspendListeners() {
        listenersSuspended = true;
    }

    @Override
    public void resumeListeners() {
        listenersSuspended = false;

        while(!suspendedEvents.isEmpty()) {
            CollectionChangeEvent<T,K> suspendedEvent = suspendedEvents.removeLast();
            fireCollectionChanged(suspendedEvent.getOperation(), Collections.unmodifiableList(suspendedEvent.getItems()));
        }
    }

    @Override
    public void mute() {
        listenersSuspended = true;
    }

    @Override
    public void unmute(UnmuteEventsMode mode) {
        listenersSuspended = false;

        if (mode ==  UnmuteEventsMode.FIRE_REFRESH_EVENT) {
            fireCollectionChanged(Operation.REFRESH, Collections.emptyList());
        }
    }

    @Override
    public boolean isSoftDeletion() {
        return false;
    }

    @Override
    public void setSoftDeletion(boolean softDeletion) {
    }

    @Override
    public boolean isCacheable() {
        return false;
    }

    @Override
    public void setCacheable(boolean cacheable) {
    }

    //Implementation of CollectionDatasource.Sortable<T, K> interface
    @Override
    public void sort(SortInfo[] sortInfos) {
        if (sortInfos.length != 1) {
            throw new UnsupportedOperationException("Supporting sort by one field only");
        }

        //noinspection unchecked
        this.sortInfos = sortInfos;

        doSort();
        fireCollectionChanged(Operation.REFRESH, Collections.emptyList());
    }

    @Override
    public void resetSortOrder() {
        this.sortInfos = null;
    }

    protected void doSort() {
        Collection<T> collection = getCollection();
        if (collection == null)
            return;

        List<T> list = new LinkedList<>(collection);
        sortDelegate.sort(list, sortInfos);
        collection.clear();
        collection.addAll(list);
    }

    @Override
    public void setSortDelegate(SortDelegate<T, K> sortDelegate) {
        this.sortDelegate = sortDelegate;
    }

    protected Comparator<T> createEntityComparator() {
        MetaPropertyPath propertyPath = sortInfos[0].getPropertyPath();
        boolean asc = Order.ASC.equals(sortInfos[0].getOrder());
        return Comparator.comparing(e -> e.getValueEx(propertyPath), EntityValuesComparator.asc(asc));
    }

    @Override
    public int indexOfId(K itemId) {
        if (itemId == null) return -1;
        Collection<T> collection = getCollection();
        if (CollectionUtils.isNotEmpty(collection)) {
            List<T> list = new ArrayList<>(collection);
            T currentItem = getItem(itemId);
            return list.indexOf(currentItem);
        }
        return -1;
    }

    @Override
    public K getIdByIndex(int index) {
        Collection<T> collection = getCollection();
        if (CollectionUtils.isNotEmpty(collection)) {
            return Iterables.get(collection, index).getId();
        }
        return null;
    }

    @Override
    public List<K> getItemIds(int startIndex, int numberOfItems) {
        List<K> list = (List<K>) getItemIds();
        return list.subList(startIndex, Math.min(startIndex + numberOfItems, list.size()));
    }

    @Override
    public K firstItemId() {
        Collection<T> collection = getCollection();
        if (collection != null && !collection.isEmpty()) {
            T first = Iterables.getFirst(collection, null);
            return first == null ? null : first.getId();
        }
        return null;
    }

    @Override
    public K lastItemId() {
        Collection<T> collection = getCollection();
        if (collection != null && !collection.isEmpty()) {
            return Iterables.getLast(collection).getId();
        }
        return null;
    }

    @Override
    public K nextItemId(K itemId) {
        if (itemId == null) return null;
        Collection<T> collection = getCollection();
        if ((collection != null) && !collection.isEmpty() && !itemId.equals(lastItemId())) {
            List<T> list = new ArrayList<>(collection);
            T currentItem = getItem(itemId);
            return list.get(list.indexOf(currentItem) + 1).getId();
        }
        return null;
    }

    @Override
    public K prevItemId(K itemId) {
        if (itemId == null) return null;
        Collection<T> collection = getCollection();
        if ((collection != null) && !collection.isEmpty() && !itemId.equals(firstItemId())) {
            List<T> list = new ArrayList<>(collection);
            T currentItem = getItem(itemId);
            return list.get(list.indexOf(currentItem) - 1).getId();
        }
        return null;
    }

    @Override
    public boolean isFirstId(K itemId) {
        return itemId != null && itemId.equals(firstItemId());
    }

    @Override
    public boolean isLastId(K itemId) {
        return itemId != null && itemId.equals(lastItemId());
    }

    @Override
    public Map<AggregationInfo, String> aggregate(AggregationInfo[] aggregationInfos, Collection<K> itemIds) {
        return aggregatableDelegate.aggregate(aggregationInfos, itemIds);
    }

    @Override
    public Map<AggregationInfo, Object> aggregateValues(AggregationInfo[] aggregationInfos, Collection<K> itemIds) {
        return aggregatableDelegate.aggregateValues(aggregationInfos, itemIds);
    }

    protected Object getItemValue(MetaPropertyPath property, K itemId) {
        Instance instance = getItemNN(itemId);
        return instance.getValueEx(property);
    }
}
