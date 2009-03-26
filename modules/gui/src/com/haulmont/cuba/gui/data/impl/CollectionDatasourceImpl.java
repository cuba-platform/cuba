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
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataServiceRemote;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.TemplateHelper;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.xml.ParametersHelper;
import org.apache.commons.lang.ObjectUtils;

import java.util.*;

public class CollectionDatasourceImpl<T extends Entity, K>
    extends 
        DatasourceImpl<T>
    implements
        CollectionDatasource<T, K>
{
    protected String query;
    protected ParametersHelper.ParameterInfo[] queryParameters;

    protected Data data = new Data(Collections.<K>emptyList(), Collections.<K,T>emptyMap());

    public CollectionDatasourceImpl(
            DsContext context, DataService dataservice,
                String id, MetaClass metaClass, String viewName)
    {
        super(context, dataservice, id, metaClass, viewName);
    }

    @Override
    public CommitMode getCommitMode() {
        return CommitMode.DATASTORE;
    }

    @Override
    public synchronized void invalidate() {
        super.invalidate();
        this.data = new Data(Collections.<K>emptyList(), Collections.<K, T>emptyMap());
    }

    @Override
    public synchronized void setItem(T item) {
        if (State.VALID.equals(state)) {
            Object prevItem = this.item;

            if (!ObjectUtils.equals(prevItem, item)) {
                if (this.item != null) {
                    detatchListener((Instance) this.item);
                }

                if (item instanceof Instance) {
                    final MetaClass aClass = ((Instance) item).getMetaClass();
                    if (!aClass.equals(metaClass)) {
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
    public synchronized void refresh() {
        Collection prevIds = data.itemIds;
        invalidate();

        data = loadData();

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

    public String getQuery() {
        return query;
    }

    public synchronized void setQuery(String query) {
        if (!ObjectUtils.equals(this.query, query)) {
            this.query = query;
            invalidate();

            queryParameters = ParametersHelper.parseQuery(query);
            for (ParametersHelper.ParameterInfo info : queryParameters) {
                final ParametersHelper.ParameterInfo.Type type = info.getType();
                if (ParametersHelper.ParameterInfo.Type.DATASOURCE.equals(type)) {
                    final String path = info.getPath();

                    final String[] strings = path.split("\\.");
                    String source = strings[0];

                    final Datasource ds = dsContext.get(source);
                    if (ds != null) {
                        dsContext.regirterDependency(this, ds);
                    } else {
                        ((DsContextImplementation) dsContext).addLazyTask(new DsContextImplementation.LazyTask() {
                            public void execute(DsContext context) {
                                final String[] strings = path.split("\\.");
                                String source = strings[0];

                                final Datasource ds = dsContext.get(source);
                                if (ds != null) {
                                    dsContext.regirterDependency(CollectionDatasourceImpl.this, ds);
                                }
                            }
                        });
                    }
                }
            }

        }
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

    protected Data loadData() {
        final DataServiceRemote.CollectionLoadContext context =
                new DataServiceRemote.CollectionLoadContext(metaClass);

        if (query != null && queryParameters != null) {
            final Map<String, Object> parameters = getQueryParameters();
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

    protected Map<String, Object> getQueryParameters() {
        final Map<String, Object> map = new HashMap<String, Object>();
        for (ParametersHelper.ParameterInfo info : queryParameters) {
            String name = info.getFlatName();

            final String path = info.getPath();
            final String[] elements = path.split("\\.");
            switch (info.getType()) {
                case DATASOURCE: {
                    final Datasource datasource = dsContext.get(elements[0]);
                    if (Datasource.State.VALID.equals(datasource.getState())) {
                        final Entity item = datasource.getItem();
                        if (elements.length > 1) {
                            final List<String> list = Arrays.asList(elements);
                            final List<String> valuePath = list.subList(1, list.size() - 1);
                            final String propertyName = InstanceUtils.formatValuePath(valuePath.toArray(new String[valuePath.size()]));

                            map.put(name, InstanceUtils.getValueEx((Instance) item, propertyName));
                        } else {
                            map.put(name, item);
                        }
                    } else {
                        map.put(name, null);
                    }
                    
                    break;
                }
                case PARAM: {
                    final Object value =
                            dsContext.getContext() == null ?
                                    null : dsContext.getContext().getValue(path);
                    map.put(name, value);
                    break;
                }
                case COMPONENT: {
                    final Object value =
                            dsContext.getContext() == null ?
                                    null : dsContext.getContext().getValue(path);
                    map.put(name, value);
                    break;
                }
                default: {
                    throw new UnsupportedOperationException();
                }
            }
        }

        return map;
    }

    private String getJPQLQuery(String query, Map<String, Object> parameterValues) {
        for (ParametersHelper.ParameterInfo info : queryParameters) {
            final String paramName = info.getName();
            final String jpaParamName = info.getFlatName();

            query = query.replaceAll(paramName.replaceAll("\\$", "\\\\\\$"), jpaParamName);
        }

        query = TemplateHelper.processTemplate(query, parameterValues);

        return query;
    }

    protected void forceCollectionChanged(CollectionDatasourceListener.CollectionOperation operation) {
        for (DatasourceListener dsListener : dsListeners) {
            if (dsListener instanceof CollectionDatasourceListener) {
                ((CollectionDatasourceListener) dsListener).collectionChanged(this, operation);
            }
        }
    }
}
