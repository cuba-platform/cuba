/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author krivopustov
 * @version $Id$
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
