/*
 * Copyright (c) 2008-2017 Haulmont.
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

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;

import javax.annotation.Nullable;
import java.util.List;

/**
 *
 */
public class StandardCollectionLoader<T extends Entity> implements CollectionLoader<T> {

    private Metadata metadata;
    private DataManager dataManager;
    private DataContext dataContext;
    private CollectionContainer<T> container;
    private String query;
    private int maxResults;
    private boolean softDeletion;
    private boolean cacheable;
    private View view;
    private String viewName;

    public StandardCollectionLoader(Metadata metadata, DataManager dataManager) {
        this.metadata = metadata;
        this.dataManager = dataManager;
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

        @SuppressWarnings("unchecked")
        LoadContext<T> loadContext = LoadContext.create(container.getMetaClass().getJavaClass());

        LoadContext.Query query = loadContext.setQueryString(this.query);

        query.setCacheable(cacheable);

        if (maxResults > 0)
            query.setMaxResults(maxResults);

        loadContext.setSoftDeletion(softDeletion);

        if (view == null && viewName != null) {
            this.view = metadata.getViewRepository().getView(container.getMetaClass(), viewName);
        }
        if (view != null) {
            loadContext.setView(view);
        }

        List<T> list = dataManager.loadList(loadContext);

        if (dataContext != null) {
            for (T entity : list) {
                dataContext.merge(entity);
            }
        }
        container.setItems(list);
    }

    @Override
    public CollectionContainer<T> getContainer() {
        return container;
    }

    @Override
    public void setContainer(CollectionContainer<T> container) {
        this.container = container;
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
    public int getMaxResults() {
        return maxResults;
    }

    @Override
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
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
    public boolean isCacheable() {
        return cacheable;
    }

    @Override
    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void setView(String viewName) {
        if (this.view != null)
            throw new IllegalStateException("view is already set");
        this.viewName = viewName;
    }
}
