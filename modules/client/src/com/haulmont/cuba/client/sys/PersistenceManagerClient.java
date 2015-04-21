/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.core.app.PersistenceManagerService;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Client-side caching proxy for the <code>PersistenceManagerService</code>.
 * <p>
 * Caches the PersistenceManager information for the whole life time of the client application.
 * The web-client's <code>Caching</code> MBean contains a method to clear this cache.
 * </p>
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(PersistenceManagerClient.NAME)
public class PersistenceManagerClient implements PersistenceManagerService {

    public static final String NAME = "cuba_PersistenceManagerClient";

    protected static class CacheEntry {
        Boolean useLazyCollection;
        Boolean useLookupScreen;
        Integer fetchUI;
        Integer maxFetchUI;
    }

    protected Map<String, CacheEntry> cache = new ConcurrentHashMap<String, CacheEntry>();

    protected volatile String dbmsType;
    protected volatile String dbmsVersion;
    protected volatile String uniqueConstraintViolationPattern;
    protected volatile Boolean defaultNullSorting;

    @Inject
    protected PersistenceManagerService service;

    protected CacheEntry getCacheEntry(String entityName) {
        CacheEntry cacheEntry = cache.get(entityName);
        if (cacheEntry == null) {
            cacheEntry = new CacheEntry();
            cache.put(entityName, cacheEntry);
        }
        return cacheEntry;
    }

    @Override
    public boolean useLazyCollection(String entityName) {
        CacheEntry cacheEntry = getCacheEntry(entityName);
        if (cacheEntry.useLazyCollection == null) {
            cacheEntry.useLazyCollection = service.useLazyCollection(entityName);
        }
        return cacheEntry.useLazyCollection;
    }

    @Override
    public boolean useLookupScreen(String entityName) {
        CacheEntry cacheEntry = getCacheEntry(entityName);
        if (cacheEntry.useLookupScreen == null) {
            cacheEntry.useLookupScreen = service.useLookupScreen(entityName);
        }
        return cacheEntry.useLookupScreen;
    }

    @Override
    public int getFetchUI(String entityName) {
        CacheEntry cacheEntry = getCacheEntry(entityName);
        if (cacheEntry.fetchUI == null) {
            cacheEntry.fetchUI = service.getFetchUI(entityName);
        }
        return cacheEntry.fetchUI;
    }

    @Override
    public int getMaxFetchUI(String entityName) {
        CacheEntry cacheEntry = getCacheEntry(entityName);
        if (cacheEntry.maxFetchUI == null) {
            cacheEntry.maxFetchUI = service.getMaxFetchUI(entityName);
        }
        return cacheEntry.maxFetchUI;
    }

    @Override
    public String getDbmsType() {
        if (dbmsType == null)
            dbmsType = service.getDbmsType();
        return dbmsType;
    }

    @Override
    public String getDbmsVersion() {
        if (dbmsVersion == null)
            dbmsVersion = service.getDbmsVersion();
        return dbmsVersion;
    }

    @Override
    public String getUniqueConstraintViolationPattern() {
        if (uniqueConstraintViolationPattern == null)
            uniqueConstraintViolationPattern = service.getUniqueConstraintViolationPattern();
        return uniqueConstraintViolationPattern;
    }

    @Override
    public boolean isNullsLastSorting() {
        if (defaultNullSorting == null)
            defaultNullSorting = service.isNullsLastSorting();
        return defaultNullSorting;
    }

    public void clearCache() {
        cache.clear();
    }
}
