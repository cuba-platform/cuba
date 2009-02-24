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
import com.haulmont.cuba.core.global.DataServiceRemote;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.TemplateHelper;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.xml.ParametersHelper;
import org.apache.commons.lang.ObjectUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CollectionDatasourceImpl<T extends Entity, K>
    extends 
        DatasourceImpl<T>
    implements
        CollectionDatasource<T, K>
{
    private String query;
    private ParametersHelper.ParameterInfo[] queryParameters;

    private Collection<T> collection = Collections.emptyList();

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
        this.collection = Collections.emptyList();
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
        invalidate();

        Collection prevIds = collection;
        loadData();

        State prevState = state;
        if (!prevState.equals(State.VALID)) {
            state = State.VALID;
            forceStateChanged(prevState);
        }

        // HACK need somehow get id from item
        if (prevIds != null && !prevIds.contains(this.item)) {
            setItem(null);
        }

        forceCollectionChanged(new CollectionDatasourceListener.CollectionOperation<T>(CollectionDatasourceListener.CollectionOperation.Type.REFRESH, null));
    }

    public synchronized T getItem(K key) {
        if (State.NOT_INITIALIZAED.equals(state)) {
            throw new IllegalStateException("Invalid datasource state " + state);
        } else {
            return (T) key;
        }
    }

    public synchronized Collection<K> getItemIds() {
        if (State.NOT_INITIALIZAED.equals(state)) {
            return Collections.emptyList();
        } else {
            return (Collection<K>) getCollection();
        }
    }

    public synchronized int size() {
        if (State.NOT_INITIALIZAED.equals(state)) {
            return 0;
        } else {
            return getCollection().size();
        }
    }

    public synchronized void addItem(T item) throws UnsupportedOperationException {
        getCollection().add(item);
        if (PersistenceHelper.isNew(item)) {
            itemToCreate.add(item);
        }
    }

    public synchronized void removeItem(T item) throws UnsupportedOperationException {
        getCollection().remove(item);
        if (PersistenceHelper.isNew(item)) {
            itemToCreate.remove(item);
        }
    }

    public synchronized boolean containsItem(K itemId) {
        return getCollection().contains(itemId);
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
                        // TODO create lazy task
                        throw new UnsupportedOperationException();
                    }
                }
            }

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

    protected Collection<T> getCollection() {
        return collection;
    }

    protected Collection<T> loadData() {
        final DataServiceRemote.CollectionLoadContext context =
                new DataServiceRemote.CollectionLoadContext(metaClass);

        if (query != null && queryParameters != null) {
            final Map<String, Object> parameters = getQueryParameters();
            for (ParametersHelper.ParameterInfo info : queryParameters) {
                if (ParametersHelper.ParameterInfo.Type.DATASOURCE.equals(info.getType())) {
                    final Object value = parameters.get(info.getFlatName());
                    if (value == null) return Collections.emptyList();
                }
            }
            context.setQueryString(getJPQLQuery(this.query, parameters)).setParameters(parameters);
        } else {
            context.setQueryString("select e from " + metaClass.getName() + " e");
        }

        context.setView(view);

        collection = (Collection) dataservice.loadList(context);

        return collection;
    }

    private Map<String, Object> getQueryParameters() {
        final Map<String, Object> map = new HashMap<String, Object>();
        for (ParametersHelper.ParameterInfo info : queryParameters) {
            String name = info.getFlatName();

            switch (info.getType()) {
                case DATASOURCE: {
                    final Datasource datasource = dsContext.get(info.getPath());
                    if (Datasource.State.VALID.equals(datasource.getState())) {
                        map.put(name, datasource.getItem());
                    } else {
                        map.put(name, null);
                    }
                    
                    break;
                }
                case CONTEXT: {
                    final Object value =
                            dsContext.getContext() == null ?
                                    null : dsContext.getContext().getValue(info.getPath());
                    map.put(name, value);
                    break;
                }
                case COMPONENT: {
                    final Object value =
                            dsContext.getContext() == null ?
                                    null : dsContext.getContext().getValue(info.getPath());
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
