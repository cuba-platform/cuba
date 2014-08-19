/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.desktop.sys.config;

import com.haulmont.cuba.client.sys.config.ConfigPersisterClientImpl;
import com.haulmont.cuba.core.app.ConfigStorageService;

/**
 * Desktop specific configuration properties provider that uses cache for db properties.
 *
 * @see com.haulmont.cuba.desktop.sys.config.DesktopConfigStorageCache
 *
 * @author krivopustov
 * @version $Id$
 */
public class DesktopConfigPersisterImpl extends ConfigPersisterClientImpl {
    private DesktopConfigStorageCache configStorageCache;

    public DesktopConfigPersisterImpl(DesktopConfigStorageCache configStorageCache) {
        this.configStorageCache = configStorageCache;
    }

    @Override
    protected ConfigStorageService getConfigStorage() {
        return configStorageCache;
    }
}