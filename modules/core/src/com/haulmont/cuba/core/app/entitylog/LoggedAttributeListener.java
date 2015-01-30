/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.entitylog;

import com.haulmont.cuba.core.listener.AfterDeleteEntityListener;
import com.haulmont.cuba.core.listener.AfterInsertEntityListener;
import com.haulmont.cuba.security.app.EntityLogAPI;
import com.haulmont.cuba.security.entity.LoggedAttribute;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author gorelov
 * @version $Id$
 */
@ManagedBean("cuba_LoggedAttributeListener")
public class LoggedAttributeListener implements AfterInsertEntityListener<LoggedAttribute>,
        AfterDeleteEntityListener<LoggedAttribute> {
    @Inject
    protected EntityLogAPI logAPI;

    @Override
    public void onAfterInsert(LoggedAttribute entity) {
        logAPI.invalidateCache();
    }

    @Override
    public void onAfterDelete(LoggedAttribute entity) {
        logAPI.invalidateCache();
    }
}
