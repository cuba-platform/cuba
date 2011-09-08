/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 06.08.2009 16:25:05
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import org.apache.openjpa.datacache.ConcurrentDataCache;
import org.apache.openjpa.datacache.ConcurrentQueryCache;
import org.apache.openjpa.datacache.QueryCache;
import org.apache.openjpa.persistence.*;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.Collection;

/**
 * DataCache MBean implementation.
 * <p>
 * This MBean is a facade to OpenJPA data cache functionality.
 * It allows to control data cache through JMX-console. 
 */
@ManagedBean(DataCacheAPI.NAME)
public class DataCache implements DataCacheAPI, DataCacheMBean {

    private OpenJPAEntityManagerFactorySPI jpaEmf;

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

    public DataCacheAPI getAPI() {
        return this;
    }

    public boolean isStoreCacheEnabled() {
        String s = jpaEmf.getConfiguration().getDataCache();
        return s != null && !s.startsWith("false");
    }

    public boolean isQueryCacheEnabled() {
        String s = jpaEmf.getConfiguration().getQueryCache();
        return s != null && !s.startsWith("false");
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
