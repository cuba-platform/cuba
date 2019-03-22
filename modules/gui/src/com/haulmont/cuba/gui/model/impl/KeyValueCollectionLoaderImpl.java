/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.gui.model.impl;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Sort;
import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.global.ValueLoadContext;
import com.haulmont.cuba.core.global.queryconditions.Condition;
import com.haulmont.cuba.gui.model.*;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

/**
 *
 */
public class KeyValueCollectionLoaderImpl implements KeyValueCollectionLoader {

    private ApplicationContext applicationContext;

    private DataContext dataContext;
    private KeyValueCollectionContainer container;
    private String query;
    private Condition condition;
    private Map<String, Object> parameters = new HashMap<>();
    private int firstResult = 0;
    private int maxResults = Integer.MAX_VALUE;
    private boolean softDeletion = true;
    private Sort sort;

    private String storeName = Stores.MAIN;
    private Function<ValueLoadContext, Collection<KeyValueEntity>> delegate;

    public KeyValueCollectionLoaderImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected DataManager getDataManager() {
        return applicationContext.getBean(DataManager.NAME, DataManager.class);
    }

    protected SorterFactory getSorterFactory() {
        return applicationContext.getBean(SorterFactory.NAME, SorterFactory.class);
    }

    @Nullable
    @Override
    public DataContext getDataContext() {
        return dataContext;
    }

    @Override
    public void setDataContext(DataContext dataContext) {
        this.dataContext = dataContext;
    }

    @Override
    public void load() {
        if (container == null)
            throw new IllegalStateException("container is null");
        if (query == null)
            throw new IllegalStateException("query is null");

        ValueLoadContext loadContext = createLoadContext();

        Collection<KeyValueEntity> list;
        if (delegate == null) {
            list = getDataManager().loadValues(loadContext);
        } else {
            list = delegate.apply(loadContext);
        }

        if (dataContext != null) {
            List<KeyValueEntity> mergedList = new ArrayList<>(list.size());
            for (KeyValueEntity entity : list) {
                mergedList.add(dataContext.merge(entity));
            }
            container.setItems(mergedList);
        } else {
            container.setItems(list);
        }
    }

    @Override
    public ValueLoadContext createLoadContext() {
        ValueLoadContext loadContext = ValueLoadContext.create();
        loadContext.setStoreName(storeName);
        loadContext.setIdName(container.getIdName());
        for (MetaProperty property : container.getEntityMetaClass().getProperties()) {
            loadContext.addProperty(property.getName());
        }

        ValueLoadContext.Query query = loadContext.setQueryString(this.query);

        query.setCondition(condition);
        query.setSort(sort);
        query.setParameters(parameters);

        if (firstResult > 0)
            query.setFirstResult(firstResult);
        if (maxResults < Integer.MAX_VALUE)
            query.setMaxResults(maxResults);

        loadContext.setSoftDeletion(softDeletion);
        return loadContext;
    }

    @Override
    public KeyValueCollectionContainer getContainer() {
        return container;
    }

    @Override
    public void setContainer(KeyValueCollectionContainer container) {
        this.container = container;
        container.setSorter(getSorterFactory().createCollectionContainerSorter(container, this));
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public Condition getCondition() {
        return condition;
    }

    @Override
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    @Override
    public void setParameters(Map<String, Object> parameters) {
        this.parameters.clear();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            setParameter(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Object getParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public void setParameter(String name, Object value) {
        parameters.put(name, value);
    }

    @Override
    public void removeParameter(String name) {
        parameters.remove(name);
    }

    @Override
    public int getMaxResults() {
        return maxResults;
    }

    @Override
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public void setSort(Sort sort) {
        if (sort == null || sort.getOrders().isEmpty()) {
            this.sort = null;
        } else {
            this.sort = sort;
        }
    }

    @Override
    public Function<ValueLoadContext, Collection<KeyValueEntity>> getDelegate() {
        return delegate;
    }

    @Override
    public void setLoadDelegate(Function<ValueLoadContext, Collection<KeyValueEntity>> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean isSoftDeletion() {
        return softDeletion;
    }

    @Override
    public void setSoftDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
    }

    @Override
    public String getStoreName() {
        return storeName;
    }

    @Override
    public void setStoreName(String name) {
        storeName = name != null ? name : Stores.MAIN;
    }

    @Override
    public int getFirstResult() {
        return firstResult;
    }

    @Override
    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }
}
