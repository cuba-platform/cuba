/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.security.jmx;

import com.haulmont.cuba.security.app.Authenticated;
import com.haulmont.cuba.security.app.EntityLogAPI;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component("cuba_EntityLogMBean")
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
