/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 27.11.2009 18:50:17
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.sys.FetchPlanManager;
import com.haulmont.cuba.security.app.EntityLogAPI;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

@ManagedBean("cuba_CachingFacade")
public class CachingFacade implements CachingFacadeMBean {

    @Inject
    private ResourceRepositoryAPI resourceRepository;

    @Inject
    private ConfigStorageAPI configStorage;

    @Inject
    private EntityLogAPI entityLog;

    @Inject
    private Scripting scripting;

    @Inject
    private Messages messages;

    @Inject
    private FetchPlanManager fetchPlanManager;

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

    public void clearResourceRepositoryCache() {
        resourceRepository.evictAll();
    }

    public void clearConfigStorageCache() {
        configStorage.clearCache();
    }

    public void clearEntityLogCache() {
        entityLog.invalidateCache();
    }

    @Override
    public void clearViewsCache() {
        fetchPlanManager.clearCache();
    }
}
