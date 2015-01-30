/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.entitylog;

import com.haulmont.cuba.core.app.EntityLogService;
import com.haulmont.cuba.core.listener.AfterDeleteEntityListener;
import com.haulmont.cuba.core.listener.AfterInsertEntityListener;
import com.haulmont.cuba.security.entity.LoggedAttribute;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author gorelov
 * @version $Id$
 */
@ManagedBean("cuba_LoggedAttributeLifecycleListener")
public class LoggedAttributeLifecycleListener implements AfterInsertEntityListener<LoggedAttribute>, AfterDeleteEntityListener<LoggedAttribute> {
    @Inject
    protected EntityLogService logService;

    @Override
    public void onAfterInsert(LoggedAttribute entity) {
        logService.invalidateCache();
    }

    @Override
    public void onAfterDelete(LoggedAttribute entity) {
        logService.invalidateCache();
    }
}
