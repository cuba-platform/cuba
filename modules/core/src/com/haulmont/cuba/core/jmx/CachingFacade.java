/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.app.ConfigStorageAPI;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.security.app.EntityLogAPI;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_CachingFacadeMBean")
public class CachingFacade implements CachingFacadeMBean {

    @Inject
    private ConfigStorageAPI configStorage;

    @Inject
    private EntityLogAPI entityLog;

    @Inject
    private Scripting scripting;

    @Inject
    private Messages messages;

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
}
