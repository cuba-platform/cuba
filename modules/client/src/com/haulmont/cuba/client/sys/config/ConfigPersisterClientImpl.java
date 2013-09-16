/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.client.sys.config;

import com.haulmont.cuba.core.app.ConfigStorageService;
import com.haulmont.cuba.core.config.ConfigPersister;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ConfigPersisterClientImpl implements ConfigPersister {

    private Map<String, String> cache = new ConcurrentHashMap<String, String>();

    private volatile boolean cacheLoaded;

    private final Log log = LogFactory.getLog(ConfigPersisterClientImpl.class);

    public String getProperty(SourceType sourceType, String name) {
        log.trace("Getting property '" + name + "', source=" + sourceType.name());
        String value;
        switch (sourceType) {
            case SYSTEM:
                value = System.getProperty(name);
                break;
            case APP:
                value = AppContext.getProperty(name);
                break;
            case DATABASE:
                value = AppContext.getProperty(name);
                if (StringUtils.isEmpty(value)) {
                    loadCache();
                    value = cache.get(name);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported config source type: " + sourceType);
        }
        return value;
    }

    private void loadCache() {
        if (!cacheLoaded) {
            synchronized (this) {
                if (!cacheLoaded) {
                    Map<String, String> properties = getConfigStorage().getDbProperties();
                    cache.clear();
                    cache.putAll(properties);
                    cacheLoaded = true;
                }
            }
        }
    }

    public void setProperty(SourceType sourceType, String name, String value) {
        log.debug("Setting property '" + name + "' to '" + value + "', source=" + sourceType.name());
        switch (sourceType) {
            case SYSTEM:
                System.setProperty(name, value);
                break;
            case APP:
                AppContext.setProperty(name, value);
                break;
            case DATABASE:
                if (value != null)
                    cache.put(name, value);
                else
                    cache.remove(name);
                getConfigStorage().setDbProperty(name, value);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported config source type: " + sourceType);
        }
    }

    private ConfigStorageService getConfigStorage() {
        return (ConfigStorageService) AppBeans.get(ConfigStorageService.NAME);
    }
}
