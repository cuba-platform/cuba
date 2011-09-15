/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.cache;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.core.global.View;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import java.util.List;

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

    public CacheSet loadData(ObjectsCache cache) throws CacheException {
        CacheSet cacheSet;

        if (StringUtils.isEmpty(dbQuery)) {
            try {
                dbQuery = ScriptingProvider.getResourceAsString(queryPath);
            } catch (Exception e) {
                log.error("Broken or missing query file for cache: " + cache.getName());
                throw new CacheException(e);
            }
        }

        MetaClass metaClass = MetadataProvider.getSession().getClass(metaClassName);
        View view = MetadataProvider.getViewRepository().getView(metaClass, viewName);

        Transaction tx = persistence.createTransaction();

        try {
            EntityManager em = PersistenceProvider.getEntityManager();
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
}