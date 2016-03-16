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
package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.app.ConfigStorageAPI;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesManagerAPI;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.core.sys.AbstractViewRepository;
import com.haulmont.cuba.security.app.EntityLogAPI;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

/**
 */
@Component("cuba_CachingFacadeMBean")
public class CachingFacade implements CachingFacadeMBean {

    @Inject
    private ConfigStorageAPI configStorage;

    @Inject
    private EntityLogAPI entityLog;

    @Inject
    private Scripting scripting;

    @Inject
    private Messages messages;

    @Inject
    private ViewRepository viewRepository;

    @Inject
    protected DynamicAttributesManagerAPI dynamicAttributesManagerAPI;

    @Override
    public int getMessagesCacheSize() {
        return messages.getCacheSize();
    }

    public void clearGroovyCache() {
        scripting.clearCache();
    }

    public void clearMessagesCache() {
        messages.clearCache();
    }

    public void clearConfigStorageCache() {
        configStorage.clearCache();
    }

    public void clearEntityLogCache() {
        entityLog.invalidateCache();
    }

    public void clearDynamicAttributesCache() {
        dynamicAttributesManagerAPI.loadCache();
    }

    @Override
    public void clearViewRepositoryCache() {
        ((AbstractViewRepository) viewRepository).reset();
    }
}
