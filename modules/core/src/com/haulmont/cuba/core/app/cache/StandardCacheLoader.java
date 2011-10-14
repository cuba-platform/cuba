/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.cache;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.global.View;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * JPQL cache loader <br/>
 * Load objects tree with jpql query and view
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@SuppressWarnings({"unused"})
public class StandardCacheLoader implements CacheLoader {

    @Inject
    private Persistence persistence;

    @Inject
    private Metadata metadata;

    @Inject
    private Scripting scripting;

    private static Log log = LogFactory.getLog(ObjectsCache.class);

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
                dbQuery = scripting.getResourceAsString(queryPath);
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
            em.setView(view);
            Query query = em.createQuery(dbQuery);

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
        Collection<Object> items = cacheSet.getItems();

        List updateItems = (List) params.get("items");

        if ((updateItems != null) && (updateItems.size() > 0)) {
            MetaClass metaClass = metadata.getSession().getClass(metaClassName);
            View view = metadata.getViewRepository().getView(metaClass, viewName);

            Transaction tx = persistence.createTransaction();

            try {
                EntityManager em = persistence.getEntityManager();
                em.setView(view);

                for (Object item : updateItems) {
                    Entity entity = (Entity) item;
                    entity = em.find(entity.getClass(), entity.getId());

                    items.remove(item);
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
}