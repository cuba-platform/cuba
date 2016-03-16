/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.desktop.sys.config;

import com.haulmont.cuba.client.sys.config.ConfigPersisterClientImpl;
import com.haulmont.cuba.core.app.ConfigStorageService;

/**
 * Desktop specific configuration properties provider that uses cache for db properties.
 *
 * @see com.haulmont.cuba.desktop.sys.config.DesktopConfigStorageCache
 *
 */
public class DesktopConfigPersisterImpl extends ConfigPersisterClientImpl {
    private DesktopConfigStorageCache configStorageCache;

    public DesktopConfigPersisterImpl(DesktopConfigStorageCache configStorageCache, boolean caching) {
        super(caching);
        this.configStorageCache = configStorageCache;
    }

    @Override
    protected ConfigStorageService getConfigStorage() {
        return configStorageCache;
    }
}