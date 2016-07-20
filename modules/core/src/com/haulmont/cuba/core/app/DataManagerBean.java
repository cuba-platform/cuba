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

package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

@Component(DataManager.NAME)
public class DataManagerBean implements DataManager {

    private Logger log = LoggerFactory.getLogger(DataManagerBean.class);

    @Inject
    protected Metadata metadata;

    @Inject
    protected ServerConfig serverConfig;

    @Inject
    protected StoreFactory storeFactory;

    @Nullable
    @Override
    public <E extends Entity> E load(LoadContext<E> context) {
        MetaClass metaClass = metadata.getClassNN(context.getMetaClass());
        String storeName = metadata.getTools().getStoreName(metaClass);
        if (storeName == null) {
            log.debug("Storage for {} is not defined, returning null", metaClass);
            return null;
        }
        DataStore storage = storeFactory.get(storeName);
        return storage.load(context);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends Entity> List<E> loadList(LoadContext<E> context) {
        MetaClass metaClass = metadata.getClassNN(context.getMetaClass());
        String storeName = metadata.getTools().getStoreName(metaClass);
        if (storeName == null) {
            log.debug("Storage for {} is not defined, returning empty list", metaClass);
            return null;
        }
        DataStore storage = storeFactory.get(storeName);
        return storage.loadList(context);
    }

    @Override
    public long getCount(LoadContext<? extends Entity> context) {
        MetaClass metaClass = metadata.getClassNN(context.getMetaClass());
        String storeName = metadata.getTools().getStoreName(metaClass);
        if (storeName == null) {
            log.debug("Storage for {} is not defined, returning 0", metaClass);
            return 0;
        }
        DataStore storage = storeFactory.get(storeName);
        return storage.getCount(context);
    }

    @Override
    public <E extends Entity> E reload(E entity, String viewName) {
        Objects.requireNonNull(viewName, "viewName is null");
        return reload(entity, metadata.getViewRepository().getView(entity.getClass(), viewName));
    }

    @Override
    public <E extends Entity> E reload(E entity, View view) {
        return reload(entity, view, null);
    }

    @Override
    public <E extends Entity> E reload(E entity, View view, @Nullable MetaClass metaClass) {
        return reload(entity, view, metaClass, entityHasDynamicAttributes(entity));
    }

    @Override
    public <E extends Entity> E reload(E entity, View view, @Nullable MetaClass metaClass, boolean loadDynamicAttributes) {
        if (metaClass == null) {
            metaClass = metadata.getSession().getClass(entity.getClass());
        }
        LoadContext<E> context = new LoadContext<>(metaClass);
        context.setId(entity.getId());
        context.setView(view);
        context.setLoadDynamicAttributes(loadDynamicAttributes);

        E reloaded = load(context);
        if (reloaded == null)
            throw new EntityAccessException();

        return reloaded;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Entity> commit(CommitContext context) {
        Map<String, CommitContext> storeToContextMap = new TreeMap<>();
        for (Entity entity : context.getCommitInstances()) {
            MetaClass metaClass = metadata.getClassNN(entity.getClass());
            String storeName = metadata.getTools().getStoreName(metaClass);
            if (storeName == null)
                continue;
            CommitContext cc = storeToContextMap.get(storeName);
            if (cc == null) {
                cc = createCommitContext(context);
                storeToContextMap.put(storeName, cc);
            }
            cc.getCommitInstances().add(entity);
            View view = context.getViews().get(entity);
            if (view != null)
                cc.getViews().put(entity, view);
        }
        for (Entity entity : context.getRemoveInstances()) {
            MetaClass metaClass = metadata.getClassNN(entity.getClass());
            String storeName = metadata.getTools().getStoreName(metaClass);
            if (storeName == null)
                continue;
            CommitContext cc = storeToContextMap.get(storeName);
            if (cc == null) {
                cc = createCommitContext(context);
                storeToContextMap.put(storeName, cc);
            }
            cc.getRemoveInstances().add(entity);
            View view = context.getViews().get(entity);
            if (view != null)
                cc.getViews().put(entity, view);
        }

        Set<Entity> result = new HashSet<>();
        for (Map.Entry<String, CommitContext> entry : storeToContextMap.entrySet()) {
            DataStore dataStore = storeFactory.get(entry.getKey());
            Set<Entity> committed = dataStore.commit(entry.getValue());
            if (!committed.isEmpty()) {
                Entity committedEntity = committed.iterator().next();
                if (committedEntity instanceof AbstractNotPersistentEntity) {
                    BaseEntityInternalAccess.setNew((AbstractNotPersistentEntity) committedEntity, false);
                }
                result.addAll(committed);
            }
        }
        return result;
    }

    @Override
    public <E extends Entity> E commit(E entity, @Nullable View view) {
        Set<Entity> res = commit(new CommitContext().addInstanceToCommit(entity, view));

        for (Entity e : res) {
            if (e.equals(entity)) {
                //noinspection unchecked
                return (E) e;
            }
        }
        return null;
    }

    @Override
    public <E extends Entity> E commit(E entity, @Nullable String viewName) {
        if (viewName != null) {
            View view = metadata.getViewRepository().getView(metadata.getClassNN(entity.getClass()), viewName);
            return commit(entity, view);
        } else {
            return commit(entity, (View) null);
        }
    }

    @Override
    public <E extends Entity> E commit(E entity) {
        return commit(entity, (View) null);
    }

    @Override
    public void remove(Entity entity) {
        CommitContext context = new CommitContext(
                Collections.<Entity>emptyList(),
                Collections.singleton(entity));
        commit(context);
    }

    protected boolean entityHasDynamicAttributes(Entity entity) {
        return entity instanceof BaseGenericIdEntity
                && ((BaseGenericIdEntity) entity).getDynamicAttributes() != null;
    }

    protected CommitContext createCommitContext(CommitContext context) {
        if (context instanceof NotDetachedCommitContext) {
            NotDetachedCommitContext newCtx = new NotDetachedCommitContext();
            newCtx.setNewInstanceIds(((NotDetachedCommitContext) context).getNewInstanceIds());
            return newCtx;
        } else {
            return new CommitContext();
        }
    }

    @Override
    public DataManager secure() {
        if (serverConfig.getDataManagerChecksSecurityOnMiddleware()) {
            return this;
        } else {
            return (DataManager) Proxy.newProxyInstance(
                    getClass().getClassLoader(),
                    new Class[]{DataManager.class},
                    new SecureDataManagerInvocationHandler(this)
            );
        }
    }

    private class SecureDataManagerInvocationHandler implements InvocationHandler {

        private final DataManager impl;

        private SecureDataManagerInvocationHandler(DataManager impl) {
            this.impl = impl;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            boolean authorizationRequired = AppContext.getSecurityContextNN().isAuthorizationRequired();
            AppContext.getSecurityContextNN().setAuthorizationRequired(true);
            try {
                return method.invoke(impl, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            } finally {
                AppContext.getSecurityContextNN().setAuthorizationRequired(authorizationRequired);
            }
        }
    }
}