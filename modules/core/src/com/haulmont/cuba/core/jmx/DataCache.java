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

/**
 * JMX interface for {@link DataCacheAPI}.
 *
 * @author krivopustov
 * @version $Id$
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

    @Override
    public boolean isStoreCacheEnabled() {
        return dataCache.isStoreCacheEnabled();
    }

    @Override
    public boolean isQueryCacheEnabled() {
        return dataCache.isQueryCacheEnabled();
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public void dataCacheEvictAll() {
        dataCache.dataCacheEvictAll();
    }

    @Override
    public void queryCacheEvictAll() {
        dataCache.queryCacheEvictAll();
    }
}