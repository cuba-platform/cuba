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
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.core.global.queryconditions.Condition;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstanceLoader;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class StandardInstanceLoader<E extends Entity> implements InstanceLoader<E> {

    private final ApplicationContext applicationContext;

    private DataContext dataContext;
    private InstanceContainer<E> container;
    private String query;
    private Condition condition;
    private Map<String, Object> parameters = new HashMap<>();
    private Object entityId;
    private boolean softDeletion = true;
    private View view;
    private String viewName;

    public StandardInstanceLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected DataManager getDataManager() {
        return applicationContext.getBean(DataManager.NAME, DataManager.class);
    }

    protected ViewRepository getViewRepository() {
        return applicationContext.getBean(ViewRepository.NAME, ViewRepository.class);
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

        @SuppressWarnings("unchecked")
        LoadContext<E> loadContext = LoadContext.create(container.getEntityMetaClass().getJavaClass());

        if (entityId != null) {
            loadContext.setId(entityId);
        } else {
            LoadContext.Query query = loadContext.setQueryString(this.query);
            query.setCondition(condition);
            query.setParameters(parameters);
        }

        if (view == null && viewName != null) {
            this.view = getViewRepository().getView(container.getEntityMetaClass(), viewName);
        }
        if (view != null) {
            loadContext.setView(view);
        }

        E entity = getDataManager().load(loadContext);

        if (dataContext != null) {
            entity = dataContext.merge(entity);
        }
        container.setItem(entity);
    }

    @Override
    public InstanceContainer<E> getContainer() {
        return container;
    }

    @Override
    public void setContainer(InstanceContainer<E> container) {
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
        if (value == null || (value instanceof String && value.equals(""))) {
            parameters.remove(name);
        } else {
            parameters.put(name, value);
        }
    }

    @Override
    public Object getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Object entityId) {
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
