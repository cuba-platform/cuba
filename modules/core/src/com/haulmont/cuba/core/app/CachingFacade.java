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

import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.security.app.EntityLogMBean;

import javax.annotation.ManagedBean;

@ManagedBean("cuba_CachingFacade")
public class CachingFacade implements CachingFacadeMBean {

    public void clearGroovyCache() {
        ScriptingProvider.clearCache();
    }

    public void clearMessagesCache() {
        MessageProvider.clearCache();
    }

    public void clearResourceRepositoryCache() {
        Locator.lookupMBean(ResourceRepositoryMBean.class).evictAll();
    }

    public void clearConfigStorageCache() {
        Locator.lookupMBean(ConfigStorageMBean.class).clearCache();
    }

    public void clearEntityLogCache() {
        Locator.lookupMBean(EntityLogMBean.class).invalidateCache();
    }
}
