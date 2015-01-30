/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.entitylog;

import com.haulmont.cuba.core.listener.AfterDeleteEntityListener;
import com.haulmont.cuba.core.listener.AfterInsertEntityListener;
import com.haulmont.cuba.core.listener.AfterUpdateEntityListener;
import com.haulmont.cuba.security.app.EntityLogAPI;
import com.haulmont.cuba.security.entity.LoggedEntity;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author gorelov
 * @version $Id$
 */
@ManagedBean("cuba_LoggedEntityListener")
public class LoggedEntityListener implements AfterInsertEntityListener<LoggedEntity>,
        AfterUpdateEntityListener<LoggedEntity>, AfterDeleteEntityListener<LoggedEntity> {
    @Inject
    protected EntityLogAPI logAPI;

    @Override
    public void onAfterInsert(LoggedEntity entity) {
        logAPI.invalidateCache();
    }

    @Override
    public void onAfterUpdate(LoggedEntity entity) {
        logAPI.invalidateCache();
    }

    @Override
    public void onAfterDelete(LoggedEntity entity) {
        logAPI.invalidateCache();
    }
}
