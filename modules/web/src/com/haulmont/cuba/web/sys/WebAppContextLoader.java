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
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.client.sys.cache.ClientCacheManager;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.sys.AbstractWebAppContextLoader;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.AppConfig;

/**
 * {@link AppContext} loader of the web client application.
 *
 */
public class WebAppContextLoader extends AbstractWebAppContextLoader {

    @Override
    protected String getBlock() {
        return "web";
    }

    @Override
    protected void beforeInitAppContext() {
        super.beforeInitAppContext();

        AppContext.setProperty(AppConfig.CLIENT_TYPE_PROP, ClientType.WEB.toString());
    }

    @Override
    protected void afterInitAppContext() {
        super.afterInitAppContext();
        ClientCacheManager clientCacheManager = AppBeans.get(ClientCacheManager.class);
        clientCacheManager.initialize();
    }
}