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

import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.global.ValueLoadContext;
import com.haulmont.cuba.core.global.queryconditions.Condition;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.HasLoader;
import com.haulmont.cuba.gui.model.KeyValueContainer;
import com.haulmont.cuba.gui.model.KeyValueInstanceLoader;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class KeyValueInstanceLoaderImpl implements KeyValueInstanceLoader {

    protected ApplicationContext applicationContext;

    protected DataContext dataContext;
    protected KeyValueContainer container;
    protected String query;
    protected Condition condition;
    protected Map<String, Object> parameters = new HashMap<>();
    protected boolean softDeletion = true;

    protected String storeName = Stores.MAIN;
    protected Function<ValueLoadContext, KeyValueEntity> delegate;
    protected EventHub events = new EventHub();

    public KeyValueInstanceLoaderImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected DataManager getDataManager() {
        return applicationContext.getBean(DataManager.NAME, DataManager.class);
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
        if (query == null && delegate == null)
            throw new IllegalStateException("both query and delegate are null");

        ValueLoadContext loadContext = createLoadContext();

        if (!sendPreLoadEvent(loadContext)) {
            return;
        }

        KeyValueEntity result = null;
        if (delegate == null) {
            List<KeyValueEntity> list = getDataManager().loadValues(loadContext);
            if (!list.isEmpty()) {
                result = list.get(0);
            }
        } else {
            result = delegate.apply(loadContext);
        }

        container.setItem(result);
        sendPostLoadEvent(result);
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
        query.setParameters(parameters);
        query.setMaxResults(1);

        loadContext.setSoftDeletion(softDeletion);
        return loadContext;
    }

    protected boolean sendPreLoadEvent(ValueLoadContext loadContext) {
        PreLoadEvent preLoadEvent = new PreLoadEvent(this, loadContext);
        events.publish(PreLoadEvent.class, preLoadEvent);
        return !preLoadEvent.isLoadPrevented();
    }

    protected void sendPostLoadEvent(@Nullable KeyValueEntity entity) {
        PostLoadEvent postLoadEvent = new PostLoadEvent(this, entity);
        events.publish(PostLoadEvent.class, postLoadEvent);
    }

    @Override
    public KeyValueContainer getContainer() {
        return container;
    }

    @Override
    public void setContainer(KeyValueContainer container) {
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
    public Function<ValueLoadContext, KeyValueEntity> getLoadDelegate() {
        return delegate;
    }

    @Override
    public void setLoadDelegate(Function<ValueLoadContext, KeyValueEntity> delegate) {
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
}
