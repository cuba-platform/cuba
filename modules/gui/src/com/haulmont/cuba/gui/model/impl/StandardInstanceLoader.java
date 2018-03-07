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
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstanceLoader;

import javax.annotation.Nullable;

/**
 *
 */
public class StandardInstanceLoader<T extends Entity<K>, K> implements InstanceLoader<T, K> {

    private Metadata metadata;
    private DataManager dataManager;
    private DataContext dataContext;
    private InstanceContainer<T> container;
    private K entityId;
    private boolean softDeletion;
    private View view;
    private String viewName;

    public StandardInstanceLoader(Metadata metadata, DataManager dataManager) {
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
        if (entityId == null)
            throw new IllegalStateException("entityId is null");

        @SuppressWarnings("unchecked")
        LoadContext<T> loadContext = LoadContext.create(container.getMetaClass().getJavaClass());

        loadContext.setId(entityId);

        if (view == null && viewName != null) {
            this.view = metadata.getViewRepository().getView(container.getMetaClass(), viewName);
        }
        if (view != null) {
            loadContext.setView(view);
        }

        T entity = dataManager.load(loadContext);

        if (dataContext != null) {
            dataContext.merge(entity);
        }
        container.setItem(entity);
    }

    @Override
    public InstanceContainer<T> getContainer() {
        return container;
    }

    @Override
    public void setContainer(InstanceContainer<T> container) {
        this.container = container;
    }

    @Override
    public K getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(K entityId) {
        this.entityId = entityId;
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
