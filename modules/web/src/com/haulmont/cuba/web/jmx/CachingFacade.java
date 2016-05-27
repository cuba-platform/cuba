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

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.client.sys.PersistenceManagerClient;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.core.sys.AbstractViewRepository;
import com.haulmont.cuba.gui.config.MenuConfig;
import com.haulmont.cuba.gui.config.WindowConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

@Component("cuba_CachingFacadeMBean")
public class CachingFacade implements CachingFacadeMBean {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private Scripting scripting;

    @Inject
    private Messages messages;

    @Inject
    private PersistenceManagerClient persistenceManagerClient;

    @Inject
    private ViewRepository viewRepository;

    @Inject
    private WindowConfig windowConfig;

    @Inject
    private MenuConfig menuConfig;

    @Override
    public int getMessagesCacheSize() {
        return messages.getCacheSize();
    }

    @Override
    public void clearGroovyCache() {
        scripting.clearCache();
        log.info("Scripting provider cache has been cleared");
    }

    @Override
    public void clearMessagesCache() {
        messages.clearCache();
        log.info("Messages cache has been cleared");
    }

    @Override
    public void clearPersistenceManagerClientCache() {
        persistenceManagerClient.clearCache();
        log.info("PersistenceManagerClient cache has been cleared");
    }

    @Override
    public void clearViewRepositoryCache() {
        ((AbstractViewRepository) viewRepository).reset();
    }

    @Override
    public void clearWindowConfig() {
        windowConfig.reset();
    }

    @Override
    public void clearMenuConfig() {
        menuConfig.reset();
    }
}