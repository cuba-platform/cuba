/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.apache.openjpa.persistence.QueryResultCache;
import org.apache.openjpa.persistence.StoreCache;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.Collection;

/**
 * Facade to OpenJPA data cache functionality.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(DataCacheAPI.NAME)
public class DataCache implements DataCacheAPI {

    protected OpenJPAEntityManagerFactorySPI jpaEmf;

    @Inject
    public void setJpaEmf(OpenJPAEntityManagerFactory jpaEmf) {
        this.jpaEmf = (OpenJPAEntityManagerFactorySPI) jpaEmf;
    }

    protected StoreCache getStoreCache() {
        return jpaEmf.getStoreCache();
    }

    protected QueryResultCache getQueryCache() {
        return jpaEmf.getQueryResultCache();
    }

    public boolean isStoreCacheEnabled() {
        String s = jpaEmf.getConfiguration().getDataCache();
        return s != null && !s.startsWith("false");
    }

    public boolean isQueryCacheEnabled() {
        String s = jpaEmf.getConfiguration().getQueryCache();
        return s != null && !s.startsWith("false");
    }

    public void dataCacheEvict(Class cls, Object id) {
        StoreCache cache = getStoreCache();
        if (cache != null)
            cache.evict(cls, id);
    }

    public void dataCacheEvictAll(Class cls, Collection ids) {
        StoreCache cache = getStoreCache();
        if (cache != null)
            cache.evictAll(cls, ids);
    }

    public void dataCacheEvictAll() {
        StoreCache cache = getStoreCache();
        if (cache != null)
            cache.evictAll();
    }

    public void queryCacheEvictAll(Class cls) {
        QueryResultCache cache = getQueryCache();
        if (cache != null)
            cache.evictAll(cls);
    }

    public void queryCacheEvictAll() {
        QueryResultCache cache = getQueryCache();
        if (cache != null)
            cache.evictAll();
    }
}
