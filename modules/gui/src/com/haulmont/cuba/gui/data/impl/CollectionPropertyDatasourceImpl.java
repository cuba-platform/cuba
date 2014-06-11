/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.haulmont.chile.core.model.*;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.AggregationInfo;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.haulmont.cuba.gui.filter.QueryFilter;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.ObjectUtils;

import javax.annotation.Nullable;
import javax.persistence.ManyToMany;
import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
public class CollectionPropertyDatasourceImpl<T extends Entity<K>, K>
        extends
            PropertyDatasourceImpl<T>
        implements
            CollectionDatasource<T, K>,
            CollectionDatasource.Sortable<T, K>,
            CollectionDatasource.Aggregatable<T, K> {

    protected T item;
    protected boolean cascadeProperty;

    protected SortInfo<MetaPropertyPath>[] sortInfos;
    protected boolean listenersSuspended;
    protected CollectionDatasourceListener.Operation lastCollectionChangeOperation;
    protected List<Entity> lastCollectionChangeItems;

    protected boolean doNotModify;

    private AggregatableDelegate<K> aggregatableDelegate = new AggregatableDelegate<K>() {
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

    @Override
    protected void initParentDsListeners() {
        masterDs.addListener(new DatasourceListener<Entity>() {

            @Override
            public void itemChanged(Datasource<Entity> ds, Entity prevItem, Entity item) {
                log.trace("itemChanged: prevItem=" + prevItem + ", item=" + item);

                Collection prevColl = prevItem == null ? null : (Collection) prevItem.getValue(metaProperty.getName());
                Collection coll = item == null ? null : (Collection) item.getValue(metaProperty.getName());
                reattachListeners(prevColl, coll);

                if (coll != null && metadata.getTools().isPersistent(metaProperty)) {
                    for (Object collItem : coll) {
                        if (PersistenceHelper.isNew(collItem)) {
                            itemToCreate.remove(collItem);
                            itemToCreate.add(collItem);
                            modified = true;
                        }
                    }
                }

                fireCollectionChanged(CollectionDatasourceListener.Operation.REFRESH, Collections.<Entity>emptyList());
            }

            @Override
            public void stateChanged(Datasource<Entity> ds, State prevState, State state) {
                for (DatasourceListener dsListener : new ArrayList<>(dsListeners)) {
                    dsListener.stateChanged(CollectionPropertyDatasourceImpl.this, prevState, state);
                }
                fireCollectionChanged(CollectionDatasourceListener.Operation.REFRESH, Collections.<Entity>emptyList());
            }

            @Override
            public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                if (property.equals(metaProperty.getName()) && !ObjectUtils.equals(prevValue, value)) {
                    log.trace("valueChanged: prop=" + property + ", prevValue=" + prevValue + ", value=" + value);

                    reattachListeners((Collection) prevValue, (Collection) value);

                    fireCollectionChanged(CollectionDatasourceListener.Operation.REFRESH, Collections.<Entity>emptyList());
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

    @Override
    public T getItem(K id) {
        if (id instanceof Entity)
            return (T) id;
        else {
            Collection<T> collection = __getCollection();
            if (collection != null) {
                for (T t : collection) {
                    if (t.getId().equals(id))
                        return t;
                }
            }
            return null;
        }
    }

    @Override
    public T getItemNN(K id) {
        T it = getItem(id);
        if (it != null)
            return it;
        else
            throw new NullPointerException("Item with id=" + id + " is not found in datasource " + this.id);
    }

    @Override
    public Collection<K> getItemIds() {
        if (State.NOT_INITIALIZED.equals(masterDs.getState())) {
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
    public Collection<T> getItems() {
        if (State.NOT_INITIALIZED.equals(masterDs.getState())) {
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
    public T getItem() {
        return State.VALID.equals(getState()) ? item : null;
    }

    @Override
    public void setItem(T item) {
        if (State.VALID.equals(getState())) {
            Object prevItem = this.item;

            if (prevItem != item) {

                if (item != null) {
                    final MetaClass aClass = item.getMetaClass();
                    MetaClass metaClass = getMetaClass();
                    if (!aClass.equals(metaClass) && !metaClass.getDescendants().contains(aClass)) {
                        throw new IllegalStateException(String.format("Invalid item metaClass"));
                    }
                }
                this.item = item;

                fireItemChanged(prevItem);
            }
        }
    }

    @Override
    public void refresh() {
        fireCollectionChanged(CollectionDatasourceListener.Operation.REFRESH, Collections.<Entity>emptyList());
    }

    @Override
    public int size() {
        if (State.NOT_INITIALIZED.equals(masterDs.getState())) {
            return 0;
        } else {
            final Collection<T> collection = __getCollection();
            return collection == null ? 0 : collection.size();
        }
    }

    protected Collection<T> __getCollection() {
        UserSession userSession = AppBeans.get(UserSessionSource.class).getUserSession();
        if (!userSession.isEntityOpPermitted(metaProperty.getRange().asClass(), EntityOp.READ)
                || !userSession.isEntityAttrPermitted(metaProperty.getDomain(), metaProperty.getName(), EntityAttrAccess.VIEW))
            return new ArrayList<>(); // Don't use Collections.emptyList() to avoid confusing UnsupportedOperationExceptions
        else {
            final Instance master = masterDs.getItem();
            return master == null ? null : (Collection<T>) master.getValue(metaProperty.getName());
        }
    }

    private void checkState() {
        if (!State.VALID.equals(getState()))
            throw new IllegalStateException("Invalid datasource state: " + getState());
    }

    private void checkPermission() {
        UserSession userSession = AppBeans.get(UserSessionSource.class).getUserSession();
        if (!userSession.isEntityAttrPermitted(metaProperty.getDomain(), metaProperty.getName(), EntityAttrAccess.MODIFY))
            throw new AccessDeniedException(PermissionType.ENTITY_ATTR, metaProperty.getDomain() + "." + metaProperty.getName());
    }

    @Override
    public void addItem(T item) {
        checkState();
        checkPermission();

        if (__getCollection() == null) {
            initCollection();
        }

        // Don't add the same object instance twice (this is possible when committing nested datasources)
        if (!containsObjectInstance(item))
            __getCollection().add(item);
        attachListener(item);

        if (ObjectUtils.equals(this.item, item)) {
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

        fireCollectionChanged(CollectionDatasourceListener.Operation.ADD, Collections.<Entity>singletonList(item));
    }

    /**
     * Search the collection using object identity.
     * @param instance
     * @return true if the collection already contains the instance
     */
    protected boolean containsObjectInstance(T instance) {
        Collection<T> collection = __getCollection();
        if (collection != null) {
            for (T item : __getCollection()) {
                if (instance == item)
                    return true;
            }
        }
        return false;
    }

    private void initCollection() {
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
        checkState();
        checkPermission();

        Collection<T> collection = __getCollection();
        if (collection != null) {
            if (this.item != null && this.item.equals(item)) {
                setItem(null);
            }

            collection.remove(item);
            detachListener(item);

            modified = true;
            if (cascadeProperty) {
                final Entity parentItem = masterDs.getItem();
                ((DatasourceImplementation) masterDs).modified(parentItem);
            } else {
                deleted(item);
                if (PersistenceHelper.isNew(item) && commitMode == CommitMode.PARENT) {
                    if (parentDs instanceof CollectionDatasource)
                        ((CollectionDatasource) parentDs).removeItem(item);
                }
            }

            fireCollectionChanged(CollectionDatasourceListener.Operation.REMOVE, Collections.<Entity>singletonList(item));
        }
    }

    @Override
    public void excludeItem(T item) {
        checkState();
        checkPermission();

        Collection<T> collection = __getCollection();
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

                fireCollectionChanged(CollectionDatasourceListener.Operation.REMOVE, Collections.<Entity>singletonList(item));
            } finally {
                doNotModify = false;
            }
        }
    }

    @Override
    public void includeItem(T item) {
        checkState();
        checkPermission();

        if (__getCollection() == null) {
            initCollection();
        }

        doNotModify = true;
        try {
            // Don't add the same object instance twice
            if (!containsObjectInstance(item))
                __getCollection().add(item);

            MetaProperty inverseProperty = metaProperty.getInverse();
            if (inverseProperty != null)
                item.setValue(inverseProperty.getName(), masterDs.getItem());

            // attach listener only after setting value to the link property
            attachListener(item);

            fireCollectionChanged(CollectionDatasourceListener.Operation.ADD, Collections.<Entity>singletonList(item));
        } finally {
            doNotModify = false;
        }
    }

    @Override
    public void clear() {
        checkState();
        Collection<T> collection = __getCollection();
        if (collection != null) {
            Collection<Object> collectionItems = new ArrayList<Object>(collection);
            doNotModify = true;
            try {
                // Clear collection
                collection.clear();
                // Notify listeners
                for (Object obj : collectionItems) {
                    T item = (T) obj;

                    MetaProperty inverseProperty = metaProperty.getInverse();
                    if (inverseProperty == null)
                        throw new UnsupportedOperationException("No inverse property for " + metaProperty);

                    item.setValue(inverseProperty.getName(), null);

                    // detach listener only after setting value to the link property
                    detachListener(item);
                }

                fireCollectionChanged(CollectionDatasourceListener.Operation.CLEAR, Collections.<Entity>emptyList());
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
        Collection<T> collection = __getCollection();
        if (collection != null) {
            for (T t : collection) {
                if (t.equals(item)) {
                    EntityCopyUtils.copyCompositionsBack(item, t);

                    modified = true;
                    if (cascadeProperty) {
                        final Entity parentItem = masterDs.getItem();
                        ((DatasourceImplementation) masterDs).modified(parentItem);
                    } else {
                        modified(t);
                    }
                }
            }
            fireCollectionChanged(CollectionDatasourceListener.Operation.UPDATE, Collections.<Entity>singletonList(item));
        }
    }

    @Override
    public void updateItem(T item) {
        Collection<T> collection = __getCollection();
        if (collection != null) {
            // this method must not change the "modified" state by contract
            boolean saveModified = modified;
            for (T t : collection) {
                if (t.equals(item)) {
                    InstanceUtils.copy(item, t);
                }
            }
            modified = saveModified;
            fireCollectionChanged(CollectionDatasourceListener.Operation.UPDATE, Collections.<Entity>singletonList(item));
        }
    }

    @Override
    public void modified(T item) {
        if (doNotModify)
            return;
        // Never modify not new objects linked as ManyToMany. CollectionPropertyDatasource should only handle adding
        // and removing of ManyToMany items.
        if (!PersistenceHelper.isNew(item) && metaProperty.getAnnotatedElement().getAnnotation(ManyToMany.class) != null)
            return;
        super.modified(item);
    }

    public void replaceItem(T item) {
        Collection<T> collection = __getCollection();
        if (collection != null) {
            for (T t : collection) {
                if (t.equals(item)) {
                    detachListener(t);
                    collection.remove(t);
                    collection.add(item);
                    attachListener(item);

                    if (item.equals(this.item)) {
                        this.item = item;
                    }
                    break;
                }
            }
            if (sortInfos != null)
                doSort();

            fireCollectionChanged(CollectionDatasourceListener.Operation.UPDATE, Collections.<Entity>singletonList(item));
        }
    }

    @Override
    public boolean containsItem(K itemId) {
        Collection<T> collection = __getCollection();
        if (collection == null)
            return false;

        if (itemId instanceof Entity)
            return collection.contains(itemId);
        else {
            for (T item : collection) {
                if (item.getId().equals(itemId))
                    return true;
            }
            return false;
        }
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
    public boolean getRefreshOnComponentValueChange() {
        return false;
    }

    @Override
    public void setRefreshOnComponentValueChange(boolean refresh) {
    }

    @Override
    public void committed(Set<Entity> entities) {
        if (!State.VALID.equals(masterDs.getState()))
            return;

        Collection<T> collection = __getCollection();
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

    protected void fireCollectionChanged(CollectionDatasourceListener.Operation operation, List<Entity> items) {
        if (listenersSuspended) {
            lastCollectionChangeOperation = operation;
            lastCollectionChangeItems = items;
            return;
        }
        for (DatasourceListener dsListener : new ArrayList<>(dsListeners)) {
            if (dsListener instanceof CollectionDatasourceListener) {
                ((CollectionDatasourceListener) dsListener).collectionChanged(this, operation, items);
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

        if (lastCollectionChangeOperation != null) {
            fireCollectionChanged(lastCollectionChangeOperation,
                    lastCollectionChangeItems != null ? lastCollectionChangeItems : Collections.<Entity>emptyList());
        }

        lastCollectionChangeOperation = null;
        lastCollectionChangeItems = null;
    }

    @Override
    public boolean isSoftDeletion() {
        return false;
    }

    @Override
    public void setSoftDeletion(boolean softDeletion) {
    }

    //Implementation of CollectionDatasource.Sortable<T, K> interface
    @Override
    public void sort(SortInfo[] sortInfos) {
        if (sortInfos.length != 1)
            throw new UnsupportedOperationException("Supporting sort by one field only");

        if (!Arrays.equals(this.sortInfos, sortInfos)) {
            //noinspection unchecked
            this.sortInfos = sortInfos;
            doSort();

            fireCollectionChanged(CollectionDatasourceListener.Operation.REFRESH, Collections.<Entity>emptyList());
        }
    }

    @Override
    public void resetSortOrder() {
        this.sortInfos = null;
    }

    protected void doSort() {
        Collection<T> collection = __getCollection();
        if (collection == null)
            return;

        final MetaPropertyPath propertyPath = sortInfos[0].getPropertyPath();
        final boolean asc = Order.ASC.equals(sortInfos[0].getOrder());

        @SuppressWarnings({"unchecked"})
        List<T> list = new LinkedList<>(collection);
        Collections.sort(list, new EntityComparator<T>(propertyPath, asc));
        collection.clear();
        collection.addAll(list);
    }

    @Override
    public K firstItemId() {
        Collection<T> collection = __getCollection();
        if (collection != null && !collection.isEmpty()) {
            return Iterables.getFirst(collection, null).getId();
        }
        return null;
    }

    @Override
    public K lastItemId() {
        Collection<T> collection = __getCollection();
        if (collection != null && !collection.isEmpty()) {
            return Iterables.getLast(collection).getId();
        }
        return null;
    }

    @Override
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

    @Override
    public K prevItemId(K itemId) {
        if (itemId == null) return null;
        Collection<T> collection = __getCollection();
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
    public Map<Object, String> aggregate(AggregationInfo[] aggregationInfos, Collection<K> itemIds) {
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
}