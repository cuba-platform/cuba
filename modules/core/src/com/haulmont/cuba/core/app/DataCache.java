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

import org.apache.openjpa.persistence.*;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.datacache.QueryCache;
import org.apache.openjpa.datacache.ConcurrentDataCache;
import org.apache.openjpa.datacache.ConcurrentQueryCache;
import com.haulmont.cuba.core.sys.EntityManagerFactoryImpl;
import com.haulmont.cuba.core.PersistenceProvider;

import java.util.Collection;

public class DataCache implements DataCacheAPI, DataCacheMBean {

    private StoreCache getStoreCache() {
        return ((EntityManagerFactoryImpl) PersistenceProvider.getEntityManagerFactory())
                .getDelegate().getStoreCache();
    }

    private QueryResultCache getQueryCache() {
        return ((EntityManagerFactoryImpl) PersistenceProvider.getEntityManagerFactory())
                .getDelegate().getQueryResultCache();
    }

    public boolean isStoreCacheEnabled() {
        EntityManagerFactoryImpl emf = (EntityManagerFactoryImpl) PersistenceProvider.getEntityManagerFactory();
        OpenJPAConfiguration configuration =
                ((OpenJPAEntityManagerFactorySPI) emf.getDelegate()).getConfiguration();
        String s = configuration.getDataCache();
        return s != null && !s.startsWith("false");
    }

    public boolean isQueryCacheEnabled() {
        EntityManagerFactoryImpl emf = (EntityManagerFactoryImpl) PersistenceProvider.getEntityManagerFactory();
        OpenJPAConfiguration configuration =
                ((OpenJPAEntityManagerFactorySPI) emf.getDelegate()).getConfiguration();
        String s = configuration.getQueryCache();
        return s != null && !s.startsWith("false");
    }

    public int getStoreCacheSize() {
        if (!isStoreCacheEnabled())
            return 0;

        EntityManagerFactoryImpl emf = (EntityManagerFactoryImpl) PersistenceProvider.getEntityManagerFactory();
        org.apache.openjpa.datacache.DataCache cache = ((StoreCacheImpl) emf.getDelegate().getStoreCache()).getDelegate();
        if (cache instanceof ConcurrentDataCache) {
            return ((ConcurrentDataCache) cache).getCacheMap().size();
        } else {
            return -1;
        }
    }

    public int getStoreCacheMaxSize() {
        if (!isStoreCacheEnabled())
            return 0;

        EntityManagerFactoryImpl emf = (EntityManagerFactoryImpl) PersistenceProvider.getEntityManagerFactory();
        org.apache.openjpa.datacache.DataCache cache = ((StoreCacheImpl) emf.getDelegate().getStoreCache()).getDelegate();
        if (cache instanceof ConcurrentDataCache) {
            return ((ConcurrentDataCache) cache).getCacheSize();
        } else {
            return -1;
        }
    }

    public int getQueryCacheSize() {
        if (!isQueryCacheEnabled())
            return 0;

        EntityManagerFactoryImpl emf = (EntityManagerFactoryImpl) PersistenceProvider.getEntityManagerFactory();
        QueryCache cache = ((QueryResultCacheImpl) emf.getDelegate().getQueryResultCache()).getDelegate();
        if (cache instanceof ConcurrentQueryCache) {
            return ((ConcurrentQueryCache) cache).getCacheMap().size();
        } else {
            return -1;
        }
    }

    public int getQueryCacheMaxSize() {
        if (!isQueryCacheEnabled())
            return 0;

        EntityManagerFactoryImpl emf = (EntityManagerFactoryImpl) PersistenceProvider.getEntityManagerFactory();
        QueryCache cache = ((QueryResultCacheImpl) emf.getDelegate().getQueryResultCache()).getDelegate();
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
