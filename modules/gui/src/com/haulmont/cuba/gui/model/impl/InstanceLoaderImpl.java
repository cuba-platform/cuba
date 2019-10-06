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

import com.google.common.base.Strings;
import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.queryconditions.Condition;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.HasLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstanceLoader;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 */
public class InstanceLoaderImpl<E extends Entity> implements InstanceLoader<E> {

    private final ApplicationContext applicationContext;

    protected DataContext dataContext;
    protected InstanceContainer<E> container;
    protected String query;
    protected Condition condition;
    protected Map<String, Object> parameters = new HashMap<>();
    protected Object entityId;
    protected boolean softDeletion = true;
    protected boolean loadDynamicAttributes;
    protected View view;
    protected String viewName;
    protected Function<LoadContext<E>, E> delegate;
    protected EventHub events = new EventHub();

    public InstanceLoaderImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected DataManager getDataManager() {
        return applicationContext.getBean(DataManager.NAME, DataManager.class);
    }

    protected ViewRepository getViewRepository() {
        return applicationContext.getBean(ViewRepository.NAME, ViewRepository.class);
    }

    protected QueryStringProcessor getQueryStringProcessor() {
        return applicationContext.getBean(QueryStringProcessor.NAME, QueryStringProcessor.class);
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

        E entity;

        LoadContext<E> loadContext = createLoadContext();

        if (delegate == null) {
            if (!needLoad())
                return;

            if (!sendPreLoadEvent(loadContext)) {
                return;
            }

            entity = getDataManager().load(loadContext);

            if (entity == null) {
                throw new EntityAccessException(container.getEntityMetaClass(), entityId);
            }
        } else {
            if (!sendPreLoadEvent(loadContext)) {
                return;
            }
            entity = delegate.apply(createLoadContext());
        }

        if (dataContext != null) {
            entity = dataContext.merge(entity);
        }
        container.setItem(entity);

        sendPostLoadEvent(entity);
    }

    protected boolean needLoad() {
        return entityId != null || !Strings.isNullOrEmpty(query);
    }

    public LoadContext<E> createLoadContext() {
        Class<E> entityClass = container.getEntityMetaClass().getJavaClass();

        LoadContext<E> loadContext = LoadContext.create(entityClass);

        if (entityId != null) {
            loadContext.setId(entityId);
        } else {
            String queryString = getQueryStringProcessor().process(this.query, entityClass);
            LoadContext.Query query = loadContext.setQueryString(queryString);
            query.setCondition(condition);
            query.setParameters(parameters);
        }

        loadContext.setView(resolveView());
        loadContext.setSoftDeletion(softDeletion);
        loadContext.setLoadDynamicAttributes(loadDynamicAttributes);

        return loadContext;
    }

    protected View resolveView() {
        View view = this.view;
        if (view == null && viewName != null) {
            view = getViewRepository().getView(container.getEntityMetaClass(), viewName);
        }
        if (view == null) {
            view = container.getView();
        }
        return view;
    }

    protected boolean sendPreLoadEvent(LoadContext<E> loadContext) {
        PreLoadEvent<E> preLoadEvent = new PreLoadEvent<>(this, loadContext);
        events.publish(PreLoadEvent.class, preLoadEvent);
        return !preLoadEvent.isLoadPrevented();
    }

    protected void sendPostLoadEvent(E entity) {
        PostLoadEvent<E> postLoadEvent = new PostLoadEvent<>(this, entity);
        events.publish(PostLoadEvent.class, postLoadEvent);
    }

    @Override
    public InstanceContainer<E> getContainer() {
        return container;
    }

    @Override
    public void setContainer(InstanceContainer<E> container) {
        this.container = container;
        if (container instanceof HasLoader) {
            ((HasLoader) container).setLoader(this);
        }
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
    public boolean isLoadDynamicAttributes() {
        return loadDynamicAttributes;
    }

    @Override
    public void setLoadDynamicAttributes(boolean loadDynamicAttributes) {
        this.loadDynamicAttributes = loadDynamicAttributes;
    }

    @Override
    public Function<LoadContext<E>, E> getLoadDelegate() {
        return delegate;
    }

    @Override
    public void setLoadDelegate(Function<LoadContext<E>, E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Subscription addPreLoadListener(Consumer<PreLoadEvent> listener) {
        return events.subscribe(PreLoadEvent.class, listener);
    }

    @Override
    public Subscription addPostLoadListener(Consumer<PostLoadEvent> listener) {
        return events.subscribe(PostLoadEvent.class, listener);
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
