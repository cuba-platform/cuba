/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.security.jmx;

import com.haulmont.cuba.security.app.Authenticated;
import com.haulmont.cuba.security.app.EntityLogAPI;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_EntityLogMBean")
public class EntityLog implements EntityLogMBean {

    @Inject
    protected EntityLogAPI entityLog;

    @Override
    public boolean isEnabled() {
        return entityLog.isEnabled();
    }

    @Authenticated
    @Override
    public void setEnabled(boolean enabled) {
        entityLog.setEnabled(enabled);
    }

    @Override
    public void invalidateCache() {
        entityLog.invalidateCache();
    }
}
