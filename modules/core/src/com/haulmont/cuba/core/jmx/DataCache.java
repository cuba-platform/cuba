/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.app.DataCacheAPI;
import org.apache.openjpa.datacache.ConcurrentDataCache;
import org.apache.openjpa.datacache.ConcurrentQueryCache;
import org.apache.openjpa.datacache.QueryCache;
import org.apache.openjpa.persistence.*;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.Collection;

/**
 * JMX interface for {@link DataCacheAPI}.
 */
@ManagedBean("cuba_DataCacheMBean")
public class DataCache implements DataCacheMBean {

    @Inject
    protected DataCacheAPI dataCache;

    protected OpenJPAEntityManagerFactorySPI jpaEmf;

    @Inject
    public void setJpaEmf(OpenJPAEntityManagerFactory jpaEmf) {
        this.jpaEmf = (OpenJPAEntityManagerFactorySPI) jpaEmf;
    }

    private StoreCache getStoreCache() {
        return jpaEmf.getStoreCache();
    }

    private QueryResultCache getQueryCache() {
        return jpaEmf.getQueryResultCache();
    }

    public boolean isStoreCacheEnabled() {
        return dataCache.isStoreCacheEnabled();
    }

    public boolean isQueryCacheEnabled() {
        return dataCache.isQueryCacheEnabled();
    }

    public int getStoreCacheSize() {
        if (!isStoreCacheEnabled())
            return 0;

        org.apache.openjpa.datacache.DataCache cache = ((StoreCacheImpl) jpaEmf.getStoreCache()).getDelegate();
        if (cache instanceof ConcurrentDataCache) {
            return ((ConcurrentDataCache) cache).getCacheMap().size();
        } else {
            return -1;
        }
    }

    public int getStoreCacheMaxSize() {
        if (!isStoreCacheEnabled())
            return 0;

        org.apache.openjpa.datacache.DataCache cache = ((StoreCacheImpl) jpaEmf.getStoreCache()).getDelegate();
        if (cache instanceof ConcurrentDataCache) {
            return ((ConcurrentDataCache) cache).getCacheSize();
        } else {
            return -1;
        }
    }

    public int getQueryCacheSize() {
        if (!isQueryCacheEnabled())
            return 0;

        QueryCache cache = ((QueryResultCacheImpl) jpaEmf.getQueryResultCache()).getDelegate();
        if (cache instanceof ConcurrentQueryCache) {
            return ((ConcurrentQueryCache) cache).getCacheMap().size();
        } else {
            return -1;
        }
    }

    public int getQueryCacheMaxSize() {
        if (!isQueryCacheEnabled())
            return 0;

        QueryCache cache = ((QueryResultCacheImpl) jpaEmf.getQueryResultCache()).getDelegate();
        if (cache instanceof ConcurrentQueryCache) {
            return ((ConcurrentQueryCache) cache).getCacheSize();
        } else {
            return -1;
        }
    }

    public void dataCacheEvictAll() {
        dataCache.dataCacheEvictAll();
    }

    public void queryCacheEvictAll() {
        dataCache.queryCacheEvictAll();
    }
}
