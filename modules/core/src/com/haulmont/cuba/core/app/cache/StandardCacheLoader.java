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

package com.haulmont.cuba.core.app.cache;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.PersistenceConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * JPQL cache loader <br/>
 * Loads objects tree with jpql query and view
 *
 */
public class StandardCacheLoader implements CacheLoader {

    @Inject
    private Persistence persistence;

    @Inject
    private Metadata metadata;

    @Inject
    private Resources resources;

    @Inject
    private Configuration configuration;

    private static Logger log = LoggerFactory.getLogger(ObjectsCache.class);

    private String viewName;
    private String queryPath;
    private String metaClassName;

    private String dbQuery;

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getQueryPath() {
        return queryPath;
    }

    public void setQueryPath(String queryPath) {
        this.queryPath = queryPath;
    }

    public String getMetaClassName() {
        return metaClassName;
    }

    public void setMetaClassName(String metaClassName) {
        this.metaClassName = metaClassName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CacheSet loadData(ObjectsCache cache) throws CacheException {
        CacheSet cacheSet;

        if (StringUtils.isEmpty(dbQuery)) {
            try {
                dbQuery = resources.getResourceAsString(queryPath);
            } catch (Exception e) {
                log.error("Broken or missing query file for cache: " + cache.getName());
                throw new CacheException(e);
            }
        }

        MetaClass metaClass = metadata.getSession().getClass(metaClassName);
        View view = metadata.getViewRepository().getView(metaClass, viewName);

        Transaction tx = persistence.createTransaction();

        try {
            EntityManager em = persistence.getEntityManager();
            Query query = em.createQuery(dbQuery);
            query.setView(view);
            query.setMaxResults(getMaxQueryResults());
            List<Object> resultList = query.getResultList();
            cacheSet = new CacheSet(resultList);
            tx.commit();
        } catch (Exception e) {
            throw new CacheException(e);
        } finally {
            tx.end();
        }

        return cacheSet;
    }

    @Override
    public void updateData(CacheSet cacheSet, Map<String, Object> params) throws CacheException {
        if (configuration.getConfig(GlobalConfig.class).getTestMode())
            return;

        Collection<Object> items = cacheSet.getItems();

        List updateItems = (List) params.get("items");

        if ((updateItems != null) && (updateItems.size() > 0)) {
            MetaClass metaClass = metadata.getSession().getClass(metaClassName);
            View view = metadata.getViewRepository().getView(metaClass, viewName);

            Transaction tx = persistence.createTransaction();

            try {
                EntityManager em = persistence.getEntityManager();

                for (Object item : updateItems) {
                    Entity entity = (Entity) item;
                    entity = em.find(entity.getClass(), entity.getId(), view);

                    items.remove(item);
                    if(entity != null)
                        items.add(entity);
                }

                tx.commit();
            } catch (Exception e) {
                throw new CacheException(e);
            } finally {
                tx.end();
            }
        } else {
            log.debug("Nothing to update");
        }
    }

    protected int getMaxQueryResults() {
        return configuration.getConfig(GlobalConfig.class).getTestMode() ?
                500 : configuration.getConfig(PersistenceConfig.class).getDefaultMaxFetchUI();
    }
}